package es.edwardbelt.hycraft.patches;

import es.edwardbelt.hycraft.patches.impl.InteractionPatch;
import es.edwardbelt.hycraft.patches.impl.ProfilePatch;
import net.bytebuddy.agent.ByteBuddyAgent;

public class PatchHelper {
    public static void init() {
        ByteBuddyAgent.install();
    }

    public static void apply() {
        PatchManager.apply(ProfilePatch.class);
        PatchManager.apply(InteractionPatch.class);
    }
}
