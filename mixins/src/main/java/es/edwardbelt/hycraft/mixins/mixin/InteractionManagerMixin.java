package es.edwardbelt.hycraft.mixins.mixin;

import com.hypixel.hytale.common.util.ListUtil;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.ForkedChainId;
import com.hypixel.hytale.protocol.InteractionSyncData;
import com.hypixel.hytale.protocol.packets.interaction.SyncInteractionChain;
import com.hypixel.hytale.server.core.entity.InteractionChain;
import com.hypixel.hytale.server.core.entity.InteractionManager;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import es.edwardbelt.hycraft.mixins.MixinConstants;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Mixin(InteractionManager.class)
public abstract class InteractionManagerMixin {

    @Unique
    private static volatile MethodHandle cachedCheckForcedInteraction;

    @Shadow
    private boolean hasRemoteClient;

    @Shadow
    @Final
    private static HytaleLogger LOGGER;

    @Shadow
    public abstract void sendCancelPacket(int chainId, ForkedChainId forkedChainId);

    @Shadow
    @Final
    @Nullable
    private PlayerRef playerRef;

    @Shadow
    @Final
    private ObjectList<SyncInteractionChain> syncPackets;

    @Shadow
    private static SyncInteractionChain makeSyncPacket(InteractionChain chain, int operationBaseIndex, List<InteractionSyncData> interactionData) {
        return null;
    }

    @Unique
    private boolean hycraft$isInteractionChainForced(InteractionChain chain) {
        try {
            Object bridgeObj = System.getProperties().get(MixinConstants.BRIDGE_KEY);
            if (!(bridgeObj instanceof Map<?, ?> bridge)) return false;

            Object hook = bridge.get(MixinConstants.INTERACTION_HOOK);
            if (hook == null) return false;

            if (cachedCheckForcedInteraction == null) {
                cachedCheckForcedInteraction = MethodHandles.publicLookup().findVirtual(
                        hook.getClass(),
                        "isInteractionChainForced",
                        MethodType.methodType(boolean.class, InteractionChain.class)
                );
            }

            return (boolean) cachedCheckForcedInteraction.invoke(hook, chain);
        } catch (Throwable e) {
            LOGGER.at(Level.WARNING).log("HyCraft Mixins error on InteractionManager mixin: " + e.getMessage());
        }

        return false;
    }

    @Inject(method = "tickChain", at = @At("HEAD"))
    private void hycraft$beforeTickChain(InteractionChain chain, CallbackInfoReturnable<Boolean> cir) {
        if (hycraft$isInteractionChainForced(chain)) {
            this.hasRemoteClient = false;
        }
    }

    @Inject(method = "tickChain", at = @At("RETURN"))
    private void hycraft$afterTickChain(InteractionChain chain, CallbackInfoReturnable<Boolean> cir) {
        if (hycraft$isInteractionChainForced(chain)) {
            this.hasRemoteClient = true;
        }
    }

    @Inject(method = "doTickChain", at = @At("HEAD"))
    private void hycraft$beforeDoTickChain(Ref<EntityStore> ref, InteractionChain chain, CallbackInfo ci) {
        if (hycraft$isInteractionChainForced(chain)) {
            this.hasRemoteClient = false;
        }
    }

    @Inject(method = "doTickChain", at = @At("RETURN"))
    private void hycraft$afterDoTickChain(Ref<EntityStore> ref, InteractionChain chain, CallbackInfo ci) {
        if (hycraft$isInteractionChainForced(chain)) {
            this.hasRemoteClient = true;
        }
    }
    /**
     * @author Edward
     * @reason Cancel HyCraft forced chain packets
     * Using Overwrite rather than Inject because CallbackInfo isn't resolvable at runtime because TransformingClassLoader can't see the app classloader where mixin classes live, so @Inject with cancel/setReturnValue is unusable
     */
    @Overwrite
    private void sendCancelPacket(InteractionChain chain) {
        if (hycraft$isInteractionChainForced(chain)) return;
        this.sendCancelPacket(chain.getChainId(), chain.getForkedChainId());
    }

    /**
     * @author Edward
     * @reason Cancel HyCraft forced chain packets
     * Using Overwrite rather than Inject because CallbackInfo isn't resolvable at runtime because TransformingClassLoader can't see the app classloader where mixin classes live, so @Inject with cancel/setReturnValue is unusable
     */
    @Overwrite
    public void sendSyncPacket(InteractionChain chain, int operationBaseIndex, List<InteractionSyncData> interactionData) {
        if (hycraft$isInteractionChainForced(chain)) return;
        if (!chain.hasSentInitial() || interactionData != null && !ListUtil.emptyOrAllNull(interactionData) || !chain.getNewForks().isEmpty()) {
            if (this.playerRef != null) {
                SyncInteractionChain packet = makeSyncPacket(chain, operationBaseIndex, interactionData);
                this.syncPackets.add(packet);
            }
        }
    }
}