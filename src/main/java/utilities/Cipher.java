package utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Implementation of a substitution cipher for encrypting and decrypting text.
 * This utility class provides a simple substitution cipher mechanism where each
 * character in the plaintext is replaced with a corresponding character
 * according to a cipher key loaded from a file.
 * Cipher Key Format:
 * The cipher key file must contain exactly two lines:
 * Line 1: Original alphabet characters
 * Line 2: Corresponding cipher alphabet characters
 * Both lines must have the same length, and each character in line 1 maps to
 * the character at the same position in line 2.
 *
 * @see CipherInterface
 */
public class Cipher implements CipherInterface {
    final String CIPHER_KEY_PATH = "ciphers/key.txt";
    private final HashMap<Character, Character> encryptMap;
    private final HashMap<Character, Character> decryptMap;

    /**
     * Constructs a new Cipher instance and initializes the encryption and
     * decryption mappings.
     * This constructor loads the cipher key from the file specified by
     * CIPHER_KEY_PATH and creates both forward (encryption) and reverse
     * (decryption) character mappings. The cipher key is validated before
     * use to ensure the proper format and integrity.
     * 
     * @throws InvalidCipherKeyException if the cipher key file is invalid,
     *                                   missing, contains malformed data, or cannot
     *                                   be properly parsed
     */
    public Cipher() throws InvalidCipherKeyException {
        this.encryptMap = loadCipherKey();
        this.decryptMap = createDecryptMap();
    }

    /**
     * Loads the cipher key from the specified file and creates the encryption
     * character mapping.
     * This method reads two lines from the cipher key file:
     * 1. The original alphabet characters
     * 2. The corresponding cipher alphabet characters
     * For each position i, the character at original.charAt(i) maps to
     * cipher.charAt(i). This mapping is used for encryption.
     * Validation Rules:
     * - The key file must exist at CIPHER_KEY_PATH
     * - The file must contain exactly two lines
     * - Both lines must have equal length
     * - Neither line can be null or empty
     * - Each character in the first line must be unique
     * - Each character in the second line must be unique
     * 
     * @return a HashMap mapping original characters to their cipher equivalents
     * @throws InvalidCipherKeyException if the cipher key file is invalid or
     *                                   cannot be properly loaded
     */
    private HashMap<Character, Character> loadCipherKey() throws InvalidCipherKeyException {
        HashMap<Character, Character> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CIPHER_KEY_PATH))) {
            String original = br.readLine();
            String cipher = br.readLine();

            // Validate lines exist and are not null
            if (original == null || cipher == null) {
                throw new InvalidCipherKeyException(
                        "Cipher key file must contain exactly two lines. Found: " +
                                (original == null && cipher == null ? "0" : "1"));
            }

            // Validate lines are not empty
            if (original.isEmpty() || cipher.isEmpty()) {
                throw new InvalidCipherKeyException(
                        "Neither line in the cipher key file can be empty.");
            }

            // Validate lines have equal length
            if (original.length() != cipher.length()) {
                throw new InvalidCipherKeyException(
                        "Cipher key lines must have equal length. " +
                                "Original: " + original.length() + ", Cipher: " + cipher.length());
            }

            // Validate no duplicate characters in original alphabet
            if (hasDuplicates(original)) {
                throw new InvalidCipherKeyException(
                        "Original alphabet contains duplicate characters.");
            }

            // Validate no duplicate characters in cipher alphabet
            if (hasDuplicates(cipher)) {
                throw new InvalidCipherKeyException(
                        "Cipher alphabet contains duplicate characters.");
            }

            // Build the mapping
            for (int i = 0; i < original.length(); i++) {
                map.put(original.charAt(i), cipher.charAt(i));
            }
        } catch (IOException e) {
            throw new InvalidCipherKeyException(
                    "Failed to read cipher key file at " + CIPHER_KEY_PATH + ": " + e.getMessage(), e);
        }
        return map;
    }

    /**
     * Checks if a string contains duplicate characters.
     * 
     * @param str the string to check for duplicates
     * @return true if the string contains duplicate characters, false otherwise
     */
    private boolean hasDuplicates(String str) {
        java.util.HashSet<Character> seen = new java.util.HashSet<>();
        for (char c : str.toCharArray()) {
            if (!seen.add(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates the decryption character mapping by reversing the encryption mapping.
     * This method iterates through all entries in encryptMap and creates
     * the inverse mapping. For each pair (original, cipher) in the encryption map,
     * this creates the pair (cipher, original) in the decryption map.
     * This approach ensures that encryption and decryption are symmetric
     * operations: decrypt(encrypt(text)) == text for all text.
     * 
     * @return a HashMap mapping cipher characters back to their original
     *         equivalents
     */
    private HashMap<Character, Character> createDecryptMap() {
        HashMap<Character, Character> map = new HashMap<>();
        for (Character key : encryptMap.keySet()) {
            map.put(encryptMap.get(key), key);
        }
        return map;
    }

    /**
     * Encrypts the given plaintext using the cipher's character mapping.
     * Each character in the plaintext is looked up in the encryption map and
     * replaced with its corresponding cipher character. Characters not found in
     * the mapping (such as punctuation or whitespace not defined in the cipher
     * key) are preserved unchanged in the output.
     * 
     * @param plaintext the text to be encrypted; must not be null
     * @return the encrypted version of the plaintext with mapped characters
     *         substituted
     */
    @Override
    public String encrypt(String plaintext) {
        StringBuilder encrypted = new StringBuilder(plaintext.length());
        for (char character : plaintext.toCharArray()) {
            encrypted.append(encryptMap.getOrDefault(character, character));
        }
        return encrypted.toString();
    }

    /**
     * Decrypts the given ciphertext using the cipher's character mapping.
     * Each character in the ciphertext is looked up in the decryption map and
     * replaced with its corresponding original character. Characters not found
     * in the mapping (such as punctuation or whitespace not defined in the
     * cipher key) are preserved unchanged in the output.
     * This method is the inverse of encrypt(String), such that:
     * decrypt(encrypt(text)).equals(text) for all text strings.
     * 
     * @param ciphertext the encrypted text to be decrypted; must not be null
     * @return the decrypted version of the ciphertext (original plaintext)
     */
    @Override
    public String decrypt(String ciphertext) {
        StringBuilder decrypted = new StringBuilder(ciphertext.length());
        for (char character : ciphertext.toCharArray()) {
            decrypted.append(decryptMap.getOrDefault(character, character));
        }
        return decrypted.toString();
    }
}
