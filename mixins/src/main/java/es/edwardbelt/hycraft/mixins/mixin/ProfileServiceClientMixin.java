package es.edwardbelt.hycraft.mixins.mixin;

import com.hypixel.hytale.codec.EmptyExtraInfo;
import com.hypixel.hytale.codec.util.RawJsonReader;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.auth.AuthConfig;
import com.hypixel.hytale.server.core.auth.ProfileServiceClient;
import es.edwardbelt.hycraft.mixins.MixinConstants;
import org.spongepowered.asm.mixin.*;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;

@Mixin(ProfileServiceClient.class)
public abstract class ProfileServiceClientMixin {

    @Unique
    private static volatile MethodHandle cachedLookupProfile;

    @Shadow
    @Final
    private static HytaleLogger LOGGER;

    @Final
    @Shadow
    private HttpClient httpClient;

    @Final
    @Shadow
    private String profileServiceUrl;

    @Unique
    private ProfileServiceClient.PublicGameProfile hycraft$lookupProfile(String username) {
        try {
            Object bridgeObj = System.getProperties().get(MixinConstants.BRIDGE_KEY);
            if (!(bridgeObj instanceof Map<?, ?> bridge)) return null;

            Object hook = bridge.get(MixinConstants.PROFILE_HOOK);
            if (hook == null) return null;

            if (cachedLookupProfile == null) {
                cachedLookupProfile = MethodHandles.publicLookup().findVirtual(
                        hook.getClass(),
                        "lookupProfile",
                        MethodType.methodType(ProfileServiceClient.PublicGameProfile.class, String.class)
                );
            }

            return (ProfileServiceClient.PublicGameProfile) cachedLookupProfile.invoke(hook, username);
        } catch (Throwable e) {
            LOGGER.at(Level.WARNING).log("HyCraft Mixins error on ProfileServiceClient mixin: " + e.getMessage());
        }

        return null;
    }

    /**
     * @author Edward
     * @reason Resolve Minecraft player UUIDs.
     * Using Overwrite rather than Inject because CallbackInfo isn't resolvable at runtime because TransformingClassLoader can't see the app classloader where mixin classes live, so @Inject with cancel/setReturnValue is unusable
     */
    @Nullable
    @Overwrite
    public ProfileServiceClient.PublicGameProfile getProfileByUsername(String username, String bearerToken) {
        ProfileServiceClient.PublicGameProfile hooked = hycraft$lookupProfile(username);
        if (hooked != null) return hooked;

        try {
            String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.profileServiceUrl + "/profile/username/" + encodedUsername))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + bearerToken)
                    .header("User-Agent", AuthConfig.USER_AGENT)
                    .timeout(AuthConfig.HTTP_TIMEOUT)
                    .GET()
                    .build();
            LOGGER.at(Level.FINE).log("Fetching profile by username: %s", username);
            HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                LOGGER.at(Level.WARNING).log("Failed to fetch profile by username: HTTP %d - %s", response.statusCode(), response.body());
                return null;
            }
            ProfileServiceClient.PublicGameProfile profile = ProfileServiceClient.PublicGameProfile.CODEC.decodeJson(
                    new RawJsonReader(response.body().toCharArray()), EmptyExtraInfo.EMPTY);
            if (profile == null) {
                LOGGER.at(Level.WARNING).log("Profile Service returned invalid response for username: %s", username);
                return null;
            }
            LOGGER.at(Level.FINE).log("Successfully fetched profile: %s (%s)", profile.getUsername(), profile.getUuid());
            return profile;
        } catch (IOException e) {
            LOGGER.at(Level.WARNING).log("IO error while fetching profile by username: %s", e.getMessage());
            return null;
        } catch (InterruptedException e) {
            LOGGER.at(Level.WARNING).log("Request interrupted while fetching profile by username");
            Thread.currentThread().interrupt();
            return null;
        } catch (Exception e) {
            LOGGER.at(Level.WARNING).log("Unexpected error fetching profile by username: %s", e.getMessage());
            return null;
        }
    }
}