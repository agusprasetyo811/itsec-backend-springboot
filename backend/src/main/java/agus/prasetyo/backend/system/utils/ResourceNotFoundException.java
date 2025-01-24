package agus.prasetyo.backend.system.utils;

public class ResourceNotFoundException extends Exception{

    public ResourceNotFoundException() {
        super("Resource not found");
    }


    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
