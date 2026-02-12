package utilities;

/**
 * Exception thrown when there is an error reading the cipher key file.
 * 
 * @see Cipher
 * @see InvalidCipherKeyException
 */
public class InvalidCipherKeyFileException extends RuntimeException {
    public InvalidCipherKeyFileException(String message) {
        super(message);
    }
}