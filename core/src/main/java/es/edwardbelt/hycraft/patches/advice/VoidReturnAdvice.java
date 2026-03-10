package es.edwardbelt.hycraft.patches.advice;

import es.edwardbelt.hycraft.patches.PatchRegistry;
import net.bytebuddy.asm.Advice;

public class VoidReturnAdvice {
    @Advice.OnMethodExit
    static void onExit(
            @Advice.Origin("#t.#m#d") String origin,
            @Advice.AllArguments Object[] args
    ) {
        PatchRegistry.invoke(origin, null, args, null);
    }
}
