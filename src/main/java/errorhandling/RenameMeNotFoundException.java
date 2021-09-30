package errorhandling;

public class RenameMeNotFoundException extends Exception {
    public RenameMeNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
