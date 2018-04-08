package koigame.sdk.util.bean;
public class ConversionException extends RuntimeException {

    public ConversionException(String message) {

        super(message);

    }

    public ConversionException(String message, Throwable cause) {

        super(message);
        this.cause = cause;

    }

    public ConversionException(Throwable cause) {

        super(cause.getMessage());
        this.cause = cause;

    }

    protected Throwable cause = null;

    public Throwable getCause() {
        return (this.cause);
    }

}
