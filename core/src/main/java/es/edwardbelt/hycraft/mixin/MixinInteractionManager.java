package es.edwardbelt.hycraft.mixin;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.protocol.InteractionSyncData;
import com.hypixel.hytale.server.core.entity.InteractionChain;
import com.hypixel.hytale.server.core.entity.InteractionManager;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.ForcedLocalClientChain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(InteractionManager.class)
public abstract class MixinInteractionManager {

    @Shadow
    private boolean hasRemoteClient;

    @Inject(method = "tickChain", at = @At("HEAD"), remap = false)
    private void hycraft$beforeTickChain(InteractionChain chain, CallbackInfoReturnable<Boolean> cir) {
        if (chain instanceof ForcedLocalClientChain) {
            this.hasRemoteClient = false;
        }
    }

    @Inject(method = "tickChain", at = @At("RETURN"), remap = false)
    private void hycraft$afterTickChain(InteractionChain chain, CallbackInfoReturnable<Boolean> cir) {
        if (chain instanceof ForcedLocalClientChain) {
            this.hasRemoteClient = true;
        }
    }

    @Inject(method = "doTickChain", at = @At("HEAD"), remap = false)
    private void hycraft$beforeDoTickChain(Ref<EntityStore> ref, InteractionChain chain, CallbackInfo ci) {
        if (chain instanceof ForcedLocalClientChain) {
            this.hasRemoteClient = false;
        }
    }

    @Inject(method = "doTickChain", at = @At("RETURN"), remap = false)
    private void hycraft$afterDoTickChain(Ref<EntityStore> ref, InteractionChain chain, CallbackInfo ci) {
        if (chain instanceof ForcedLocalClientChain) {
            this.hasRemoteClient = true;
        }
    }

    @Inject(method = "sendCancelPacket", at = @At("HEAD"), cancellable = true, remap = false)
    private void hycraft$onSendCancel(InteractionChain chain, CallbackInfo ci) {
        if (chain instanceof ForcedLocalClientChain) {
            ci.cancel();
        }
    }

    @Inject(method = "sendSyncPacket", at = @At("HEAD"), cancellable = true, remap = false)
    private void hycraft$onSendSync(InteractionChain chain, int operationBaseIndex, List<InteractionSyncData> interactionData, CallbackInfo ci) {
        if (chain instanceof ForcedLocalClientChain) {
            ci.cancel();
        }
    }
}
