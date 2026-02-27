package es.edwardbelt.hycraft.util;

import com.hypixel.hytale.server.core.modules.i18n.I18nModule;

public class LanguageUtil {
    public static String getMessage(String messageKey) {
        if (messageKey == null) return null;
        return I18nModule.get().getMessage(I18nModule.DEFAULT_LANGUAGE, messageKey);
    }
}
