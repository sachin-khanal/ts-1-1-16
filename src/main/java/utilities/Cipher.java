package utilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.nio.file.*;

/**
 * Class implements cipher encryption and decryption based on the provided
 * cipher keys.
 * 
 * @see CipherInterface
 * @see InvalidCipherKeyException
 * @see InvalidCipherKeyFileException
 */
public class Cipher implements CipherInterface {
    private static final String DEFAULT_KEY_FILE_PATH = "ciphers/key.txt";
    private final String CIPHER_KEY_FILE_PATH;

    private char[][] keys;

    private final HashMap<Character, Character> encryptMap;
    private final HashMap<Character, Character> decryptMap;

    /**
     * Constructs a Cipher by loading and validating the key file,
     * then creating encryption and decryption mappings.
     * 
     * @throws InvalidCipherKeyFileException if there is an error reading the key
     *                                       file
     * @throws InvalidCipherKeyException     if the keys are invalid
     */
    public Cipher() throws InvalidCipherKeyFileException, InvalidCipherKeyException {
        this(DEFAULT_KEY_FILE_PATH); // pass into the constructor that handles the file path
    }

    /**
     * Constructs a Cipher with a specified key file path.
     * 
     * @param cipherKeyFilePath the path to the cipher key file
     * @throws InvalidCipherKeyFileException if there is an error reading the key
     *                                       file
     * @throws InvalidCipherKeyException     if the keys are invalid
     */
    public Cipher(String cipherKeyFilePath) throws InvalidCipherKeyFileException, InvalidCipherKeyException {
        this.CIPHER_KEY_FILE_PATH = cipherKeyFilePath;
        this.loadCipherKeys();
        this.encryptMap = createEncryptMap();
        this.decryptMap = createDecryptMap();
    }

    /**
     * Loads the cipher key file, validates its format, then returns the original
     * and cipher character arrays.
     * 
     * @throws InvalidCipherKeyFileException if there is an error reading the key
     *                                       file
     * @throws InvalidCipherKeyException     if the keys are invalid
     */
    private void loadCipherKeys() throws InvalidCipherKeyFileException, InvalidCipherKeyException {
        try {
            List<String> lines = Files.readAllLines(Paths.get(CIPHER_KEY_FILE_PATH));

            this.validateCipherKeys(lines);

            String original = lines.get(0);
            String cipher = lines.get(1);

            this.keys = new char[2][original.length()];
            keys[0] = original.toCharArray();
            keys[1] = cipher.toCharArray();
        } catch (IOException e) {
            throw new InvalidCipherKeyFileException("Error reading cipher key file: " + e.getMessage());
        }
    }

    /**
     * Validates the cipher key lines read from the file.
     * 
     * @param lines the lines read from the cipher key file
     * @throws InvalidCipherKeyException if the keys are invalid
     */
    private void validateCipherKeys(List<String> lines) throws InvalidCipherKeyException {
        // Validate that the file contains exactly two lines
        if (lines.size() != 2) {
            throw new InvalidCipherKeyException("Cipher key file must contain exactly two lines.");
        }

        String original = lines.get(0);
        String cipher = lines.get(1);

        // Validate both lines exist (are not empty)
        if (original.isEmpty() || cipher.isEmpty()) {
            throw new InvalidCipherKeyException("Cipher keys must contain two non-empty lines.");
        }

        // Validate lines are of equal length
        if (original.length() != cipher.length()) {
            throw new InvalidCipherKeyException("Cipher key lines must be of equal length.");
        }

        // Validate no duplicate characters in the original line
        if (hasDuplicates(original)) {
            throw new InvalidCipherKeyException("Original characters must not contain duplicates.");
        }

        // Validate no duplicate characters in cipher line
        if (hasDuplicates(cipher)) {
            throw new InvalidCipherKeyException("Cipher characters must not contain duplicates.");
        }
    }

    /**
     * Checks if a string contains duplicate characters
     * 
     * @param string the string to check for duplicates
     * @return true if the string contains duplicate characters, false otherwise
     */
    private boolean hasDuplicates(String string) {
        HashSet<Character> seen = new HashSet<>();
        for (char character : string.toCharArray()) {
            if (!seen.add(character)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates the encryption mapping from the original characters to the cipher
     * characters.
     * 
     * @return HashMap mapping original characters to cipher characters
     * @throws InvalidCipherKeyException     if the keys are invalid
     * @throws InvalidCipherKeyFileException if the key file cannot be read
     */
    private HashMap<Character, Character> createEncryptMap()
            throws InvalidCipherKeyFileException, InvalidCipherKeyException {
        char[] original = keys[0];
        char[] cipher = keys[1];

        HashMap<Character, Character> encryptMap = new HashMap<>();
        for (int i = 0; i < original.length; i++) {
            encryptMap.put(original[i], cipher[i]);
        }
        return encryptMap;
    }

    /**
     * Creates the decryption mapping by reversing the encryption map.
     * 
     * @return HashMap mapping cipher characters to original characters
     */
    private HashMap<Character, Character> createDecryptMap() {
        HashMap<Character, Character> decryptMap = new HashMap<>();
        for (Character key : encryptMap.keySet()) {
            decryptMap.put(encryptMap.get(key), key);
        }
        return decryptMap;
    }

    /**
     * Encrypts unencrypted text using the cipher mapping. If a character does not
     * have a mapping, it is returned unchanged.
     * 
     * @param plaintext the text to encrypt
     * @return the encrypted text
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
     * Decrypts encrypted text using the cipher mapping. If a character does not
     * have a mapping, it is returned unchanged.
     * 
     * @param ciphertext the text to decrypt
     * @return the decrypted text
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
