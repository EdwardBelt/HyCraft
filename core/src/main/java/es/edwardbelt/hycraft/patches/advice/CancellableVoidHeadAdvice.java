package es.edwardbelt.hycraft.patches.advice;

import es.edwardbelt.hycraft.patches.PatchRegistry;
import net.bytebuddy.asm.Advice;

public class CancellableVoidHeadAdvice {
    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    static Object onEnter(
            @Advice.Origin("#t.#m#d") String origin,
            @Advice.AllArguments Object[] args
    ) {
        Object[] state = PatchRegistry.invoke(origin, null, args, null);
        return ((boolean) state[2]) ? state : null;
    }
}
