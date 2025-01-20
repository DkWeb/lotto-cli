package de.dkweb.lotto.de.dkweb.lotto.i18n;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public final class GermanTranslator implements Translator {
    private final Locale GERMAN_LOCALE = Locale.GERMAN;

    private ResourceBundle resourceBundle;

    public GermanTranslator() {
        this.resourceBundle = ResourceBundle.getBundle("i18n", GERMAN_LOCALE);
    }

    @Override
    public String i18n(String key, Object... params) {
        Object[] preprocessedParams = preprocessParams(params);
        String text = resourceBundle.getString(key);
        MessageFormat formatter = createFormatter();
        formatter.applyPattern(text);
        return formatter.format(preprocessedParams);
    }

    private MessageFormat createFormatter() {
        MessageFormat formatter = new MessageFormat("");
        formatter.setLocale(GERMAN_LOCALE);
        return formatter;
    }

    private Object[] preprocessParams(Object... params) {
        Object[] result = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            // LocalDate is not supported by MessageFormat
            // -> we transform it to a "legacy" Date
            if (param instanceof LocalDate toTransform) {
                result[i] = new Date(toTransform.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            } else {
                result[i] = param;
            }
        }
        return result;
    }
}
