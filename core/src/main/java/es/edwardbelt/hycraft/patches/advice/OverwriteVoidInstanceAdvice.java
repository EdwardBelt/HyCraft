package es.edwardbelt.hycraft.patches.advice;

import es.edwardbelt.hycraft.patches.PatchRegistry;
import net.bytebuddy.asm.Advice;

public class OverwriteVoidInstanceAdvice {
    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    static Object onEnter(
            @Advice.Origin("#t.#m#d") String origin,
            @Advice.This Object self,
            @Advice.AllArguments Object[] args
    ) {
        return PatchRegistry.invoke(origin, self, args, null);
    }
}
