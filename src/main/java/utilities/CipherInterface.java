package utilities;

/**
 * Interface defining the contract for cipher implementations.
 * A cipher is a cryptographic algorithm for performing encryption and
 * decryption. Implementations of this interface should provide substitution
 * cipher functionality where characters in the input text are replaced
 * according to a predefined mapping.
 * 
 */
public interface CipherInterface {
    /**
     * Encrypts the given plaintext using the cipher's character mapping.
     * Each character in the plaintext is replaced with its corresponding character
     * from the cipher alphabet. Characters not present in the cipher mapping should
     * remain unchanged in the output.
     *
     * @param plaintext the text to be encrypted; must not be null
     * @return the encrypted version of the plaintext
     */
    String encrypt(String plaintext);

    /**
     * Decrypts the given ciphertext using the cipher's character mapping.
     * Each character in the ciphertext is replaced with its corresponding character
     * from the original alphabet. Characters not present in the cipher mapping
     * should remain unchanged in the output.
     * 
     * @param ciphertext the encrypted text to be decrypted; must not be null
     * @return the decrypted version of the ciphertext (original plaintext)
     */
    String decrypt(String ciphertext);
}
