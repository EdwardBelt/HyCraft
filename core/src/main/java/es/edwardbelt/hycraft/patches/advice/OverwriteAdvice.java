package es.edwardbelt.hycraft.patches.advice;

import es.edwardbelt.hycraft.patches.PatchRegistry;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

public class OverwriteAdvice {
    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    static Object onEnter(
            @Advice.Origin("#t.#m#d") String origin,
            @Advice.AllArguments Object[] args
    ) {
        return PatchRegistry.invoke(origin, null, args, null);
    }

    @Advice.OnMethodExit
    static void onExit(
            @Advice.Enter Object enter,
            @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object ret
    ) {
        if (enter instanceof Object[]) {
            ret = ((Object[]) enter)[3];
        }
    }
}
