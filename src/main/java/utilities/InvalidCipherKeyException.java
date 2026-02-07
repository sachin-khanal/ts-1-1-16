package utilities;

/**
 * Exception thrown when a cipher key file is invalid or cannot be properly
 * loaded.
 * This exception is raised when the cipher key file:
 * - Does not exist or cannot be read
 * - Does not contain exactly two lines
 * - Has lines of unequal length
 * - Contains duplicate characters within a line
 * - Is empty or null
 */
public class InvalidCipherKeyException extends RuntimeException {
    /**
     * Constructs a new InvalidCipherKeyException with the specified error message.
     *
     * @param message the detail message explaining why the cipher key is invalid
     */
    public InvalidCipherKeyException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidCipherKeyException with the specified error message
     * and cause.
     *
     *  @param message the detail message explaining why the cipher key is invalid
     * @param cause   the underlying cause of this exception
     */
    public InvalidCipherKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}
