package es.edwardbelt.hycraft.patches.advice;

import es.edwardbelt.hycraft.patches.PatchRegistry;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

public class ReturnAdvice {
    @Advice.OnMethodExit
    static void onExit(
            @Advice.Origin("#t.#m#d") String origin,
            @Advice.AllArguments Object[] args,
            @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object ret
    ) {
        Object[] state = PatchRegistry.invoke(origin, null, args, ret);
        if ((boolean) state[2]) {
            ret = state[3];
        }
    }
}
