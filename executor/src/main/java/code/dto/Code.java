package code.dto;

import code.exception.InvalidCodeTypeException;

public record Code(
        Lang lang,
        String code
) {

    public Code {
        if (lang == null) {
            throw InvalidCodeTypeException.invalidLang();
        }

        if (code == null || code.isBlank()) {
            throw InvalidCodeTypeException.invalidCode();
        }
    }
}
