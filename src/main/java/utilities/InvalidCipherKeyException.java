package utilities;

/**
 * Exception thrown when the cipher keys are invalid.
 * 
 * @see Cipher
 * @see InvalidCipherKeyFileException
 */
public class InvalidCipherKeyException extends RuntimeException {
    public InvalidCipherKeyException(String message) {
        super(message);
    }
}
