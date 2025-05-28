package soap.service;

public class TrackingNotFoundException extends RuntimeException{
    public TrackingNotFoundException(String message) {
        super(message);
    }
}
