package es.edwardbelt.hycraft.patches.advice;

import es.edwardbelt.hycraft.patches.PatchRegistry;
import net.bytebuddy.asm.Advice;

public class HeadInstanceAdvice {
    @Advice.OnMethodEnter
    static void onEnter(
            @Advice.Origin("#t.#m#d") String origin,
            @Advice.This Object self,
            @Advice.AllArguments Object[] args
    ) {
        PatchRegistry.invoke(origin, self, args, null);
    }
}
