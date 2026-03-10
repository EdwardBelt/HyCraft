package es.edwardbelt.hycraft.patches.impl;

import com.hypixel.hytale.server.core.auth.ProfileServiceClient;
import es.edwardbelt.hycraft.HyCraft;
import es.edwardbelt.hycraft.patches.api.At;
import es.edwardbelt.hycraft.patches.api.CallbackInfo;
import es.edwardbelt.hycraft.patches.api.Inject;
import es.edwardbelt.hycraft.patches.api.Patch;
import es.edwardbelt.hycraft.util.Logger;
import es.edwardbelt.hycraft.util.UUIDUtil;

import java.util.UUID;

@Patch(ProfileServiceClient.class)
public class ProfilePatch {

    @Inject(method = "getProfileByUsername", at = At.HEAD, cancellable = true)
    public static void onLookup(CallbackInfo ci) {
        String username = ci.getArg(0);
        String prefix = HyCraft.get().getConfigManager().getMain().getPlayerPrefix();

        if (username != null && username.startsWith(prefix)) {
            try {
                UUID uuid = UUIDUtil.getOnlineUUID(username.replace(prefix, ""));
                ci.setReturnValue(new ProfileServiceClient.PublicGameProfile(uuid, username));
            } catch (Exception e) {
                Logger.ERROR.log("Error getting Minecraft player UUID: " + username + ". " + e.getMessage());
            }
        }
    }
}
