package assistanceException;

public class RequestIdNotFoundException extends RuntimeException {

    public RequestIdNotFoundException(String message) {
        super(message);
    }
}

