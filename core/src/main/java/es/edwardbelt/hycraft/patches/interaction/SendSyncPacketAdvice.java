package es.edwardbelt.hycraft.patches.interaction;

import net.bytebuddy.asm.Advice;

public class SendSyncPacketAdvice {
    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    static boolean onEnter(@Advice.Argument(0) Object chain) {
        return chain.getClass().getName().contains("ForcedLocalClientChain");
    }
}
