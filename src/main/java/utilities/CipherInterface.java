package utilities;

/**
 * Interface defining methods for cipher encryption and decryption.
 * 
 * @see Cipher
 */
public interface CipherInterface {
    /**
     * Encrypts the given plaintext using the cipher's encryption mapping.
     * 
     * @param plaintext the text to encrypt
     * @return the encrypted text
     */
    String encrypt(String plaintext);

    /**
     * Decrypts the given ciphertext using the cipher's decryption mapping.
     * 
     * @param ciphertext the text to decrypt
     * @return the decrypted text
     */
    String decrypt(String ciphertext);
}
