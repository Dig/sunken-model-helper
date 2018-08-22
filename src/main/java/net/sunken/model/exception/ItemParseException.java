package net.sunken.model.exception;

public class ItemParseException extends Exception {

    public ItemParseException(String message) {
        super(message);
    }

    public ItemParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
