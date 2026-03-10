package es.edwardbelt.hycraft.patches.impl;

import com.hypixel.hytale.server.core.entity.InteractionChain;
import com.hypixel.hytale.server.core.entity.InteractionManager;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.ForcedLocalClientChain;
import es.edwardbelt.hycraft.patches.api.At;
import es.edwardbelt.hycraft.patches.api.CallbackInfo;
import es.edwardbelt.hycraft.patches.api.Inject;
import es.edwardbelt.hycraft.patches.api.Patch;

@Patch(InteractionManager.class)
public class InteractionPatch {

    @Inject(method = "tickChain", at = At.HEAD)
    public static void beforeTickChain(CallbackInfo ci) {
        Object chain = ci.getArg(0);
        if (chain instanceof ForcedLocalClientChain) {
            ci.setField("hasRemoteClient", false);
        }
    }

    @Inject(method = "tickChain", at = At.RETURN)
    public static void afterTickChain(CallbackInfo ci) {
        Object chain = ci.getArg(0);
        if (chain instanceof ForcedLocalClientChain) {
            ci.setField("hasRemoteClient", true);
        }
    }

    @Inject(method = "doTickChain", at = At.HEAD)
    public static void beforeDoTickChain(CallbackInfo ci) {
        Object chain = ci.getArg(1);
        if (chain instanceof ForcedLocalClientChain) {
            ci.setField("hasRemoteClient", false);
        }
    }

    @Inject(method = "doTickChain", at = At.RETURN)
    public static void afterDoTickChain(CallbackInfo ci) {
        Object chain = ci.getArg(1);
        if (chain instanceof ForcedLocalClientChain) {
            ci.setField("hasRemoteClient", true);
        }
    }

    @Inject(method = "sendCancelPacket", at = At.HEAD, args = {InteractionChain.class}, cancellable = true)
    public static void onSendCancel(CallbackInfo ci) {
        Object chain = ci.getArg(0);
        if (chain instanceof ForcedLocalClientChain) {
            ci.cancel();
        }
    }

    @Inject(method = "sendSyncPacket", at = At.HEAD, cancellable = true)
    public static void onSendSync(CallbackInfo ci) {
        Object chain = ci.getArg(0);
        if (chain instanceof ForcedLocalClientChain) {
            ci.cancel();
        }
    }
}
