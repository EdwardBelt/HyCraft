package es.edwardbelt.hycraft.patches.interaction;

import net.bytebuddy.asm.Advice;

public class TickChainAdvice {
    @Advice.OnMethodEnter
    static boolean onEnter(
            @Advice.Argument(0) Object chain,
            @Advice.FieldValue(value = "hasRemoteClient", readOnly = false) boolean hasRemoteClient
    ) {
        boolean original = hasRemoteClient;
        if (original && chain.getClass().getName().contains("ForcedLocalClientChain")) {
            hasRemoteClient = false;
        }
        return original;
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    static void onExit(
            @Advice.Enter boolean originalValue,
            @Advice.FieldValue(value = "hasRemoteClient", readOnly = false) boolean hasRemoteClient
    ) {
        hasRemoteClient = originalValue;
    }
}
