package code.domain;

import code.exception.InvalidCodeTypeException;

public record UserCode(
        Lang lang,
        String code
) {

    public UserCode {
        if (lang == null) {
            throw InvalidCodeTypeException.invalidLang();
        }

        if (code == null || code.isBlank()) {
            throw InvalidCodeTypeException.invalidCode();
        }
    }
}
