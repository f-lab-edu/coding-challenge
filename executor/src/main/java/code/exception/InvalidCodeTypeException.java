package code.exception;

public final class InvalidCodeTypeException extends RuntimeException {
    private InvalidCodeTypeException(String message) {
        super(message);
    }

    public static InvalidCodeTypeException invalidLang() {
        return new InvalidCodeTypeException("Lang is null or empty");
    }

    public static InvalidCodeTypeException invalidCode() {
        return new InvalidCodeTypeException("Code is null or empty");
    }
}
