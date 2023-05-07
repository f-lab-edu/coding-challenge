package code.dto;

import code.exception.InvalidCodeTypeException;

public record Code(
        String lang,
        String code
) {

    public Code {
        if (lang == null || lang.isBlank()) {
            throw InvalidCodeTypeException.invalidLang();
        }

        if (code == null || code.isBlank()) {
            throw InvalidCodeTypeException.invalidCode();
        }
    }
}
