package es.edwardbelt.hycraft.mixin;

import com.hypixel.hytale.server.core.auth.ProfileServiceClient;
import es.edwardbelt.hycraft.HyCraft;
import es.edwardbelt.hycraft.util.Logger;
import es.edwardbelt.hycraft.util.UUIDUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(ProfileServiceClient.class)
public class MixinProfileServiceClient {

    @Inject(method = "getProfileByUsername", at = @At("HEAD"), cancellable = true, remap = false)
    private void hycraft$onLookup(String username, CallbackInfoReturnable<ProfileServiceClient.PublicGameProfile> cir) {
        String prefix = HyCraft.get().getConfigManager().getMain().getPlayerPrefix();

        if (username != null && username.startsWith(prefix)) {
            try {
                UUID uuid = UUIDUtil.getOnlineUUID(username.replace(prefix, ""));
                cir.setReturnValue(new ProfileServiceClient.PublicGameProfile(uuid, username));
            } catch (Exception e) {
                Logger.ERROR.log("Error getting Minecraft player UUID: " + username + ". " + e.getMessage());
            }
        }
    }
}
