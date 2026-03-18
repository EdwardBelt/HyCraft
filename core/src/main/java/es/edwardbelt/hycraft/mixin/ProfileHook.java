package es.edwardbelt.hycraft.mixin;

import com.hypixel.hytale.server.core.auth.ProfileServiceClient;
import es.edwardbelt.hycraft.HyCraft;
import es.edwardbelt.hycraft.util.Logger;
import es.edwardbelt.hycraft.util.UUIDUtil;

import java.util.UUID;

public class ProfileHook {
    public ProfileServiceClient.PublicGameProfile lookupProfile(String username) {
        String prefix = HyCraft.get().getConfigManager().getMain().getPlayerPrefix();

        if (username != null && username.startsWith(prefix)) {
            try {
                UUID uuid = UUIDUtil.getOnlineUUID(username.replace(prefix, ""));
                return new ProfileServiceClient.PublicGameProfile(uuid, username);
            } catch (Exception e) {
                Logger.ERROR.log("Error getting Minecraft player UUID: " + username + ". " + e.getMessage());
            }
        }

        return null;
    }
}
