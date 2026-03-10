package es.edwardbelt.hycraft.patches.interaction;

import es.edwardbelt.hycraft.patches.PatchHelper;
import es.edwardbelt.hycraft.util.Logger;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.matcher.ElementMatchers;

public class InteractionPatcher {
    public static void install() {
        try {
            Class<?> target = Class.forName(
                    "com.hypixel.hytale.server.core.entity.InteractionManager"
            );

            /*ClassLoader loader = target.getClassLoader();

            PatchHelper.injectAdvice(loader, TickChainAdvice.class, "es.edwardbelt.hycraft");
            PatchHelper.injectAdvice(loader, DoTickChainAdvice.class, "es.edwardbelt.hycraft");
            PatchHelper.injectAdvice(loader, SendSyncPacketAdvice.class, "es.edwardbelt.hycraft");
            PatchHelper.injectAdvice(loader, SendCancelPacketAdvice.class, "es.edwardbelt.hycraft");*/

            new ByteBuddy()
                    .redefine(target)
                    .visit(Advice.to(TickChainAdvice.class)
                            .on(ElementMatchers.named("tickChain")))
                    .visit(Advice.to(DoTickChainAdvice.class)
                            .on(ElementMatchers.named("doTickChain")))
                    .visit(Advice.to(SendSyncPacketAdvice.class)
                            .on(ElementMatchers.named("sendSyncPacket")))
                    .visit(Advice.to(SendCancelPacketAdvice.class)
                            .on(ElementMatchers.named("sendCancelPacket")))
                    .make()
                    .load(target.getClassLoader(),
                            ClassReloadingStrategy.fromInstalledAgent());

            Logger.INFO.log("InteractionManager patched!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("InteractionManager not found!", e);
        }
    }
}
