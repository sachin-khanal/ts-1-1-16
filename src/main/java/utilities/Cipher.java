package utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.nio.file.*;

/**
 * Substitution cipher that encrypts and decrypts text using a key file.
 * Key file format: two equal-length lines where each character in line 1
 * maps to the corresponding character in line 2.
 *
 * @see CipherInterface
 */
public class Cipher implements CipherInterface {
    final String CIPHER_KEY_PATH = "ciphers/key.txt";
    private final HashMap<Character, Character> encryptMap;
    private final HashMap<Character, Character> decryptMap;

    /**
     * Constructs a Cipher by loading and validating the key file,
     * then creating encryption and decryption mappings.
     * 
     * @throws InvalidCipherKeyException if the key file is invalid or cannot be
     *                                   loaded
     */
    public Cipher() throws InvalidCipherKeyException {
        this.encryptMap = createEncryptMap();
        this.decryptMap = createDecryptMap();
    }

    /**
     * Loads the cipher key file, validates its format, then returns the original
     * and cipher character arrays.
     * 
     * @return
     * @throws InvalidCipherKeyException
     */
    private char[][] loadCipherKeys() throws InvalidCipherKeyException {
        try {
            List<String> lines = Files.readAllLines(Paths.get(CIPHER_KEY_PATH));

            // Validate both lines exist
            if (lines.size() != 2) {
                throw new InvalidCipherKeyException(
                        "Cipher key file must contain exactly two lines. Found: " + lines.size());
            }

            String original = lines.get(0);
            String cipher = lines.get(1);

            // Validate non-empty
            if (original.isEmpty() || cipher.isEmpty()) {
                throw new InvalidCipherKeyException(
                        "Neither line in the cipher key file can be empty.");
            }

            // Validate equal length
            if (original.length() != cipher.length()) {
                throw new InvalidCipherKeyException(
                        "Cipher key lines must have equal length.\n" +
                                "Original: " + original.length() + "\nCipher: " + cipher.length());
            }

            // Validate no duplicates in original
            if (hasDuplicates(original)) {
                throw new InvalidCipherKeyException(
                        "Original alphabet contains duplicate characters.");
            }

            // Validate no duplicates in cipher
            if (hasDuplicates(cipher)) {
                throw new InvalidCipherKeyException(
                        "Cipher alphabet contains duplicate characters.");
            }

            return new char[][] { original.toCharArray(), cipher.toCharArray() };
        } catch (IOException e) {
            throw new InvalidCipherKeyException(
                    "Failed to read cipher key file at " + CIPHER_KEY_PATH + ": " + e.getMessage(), e);
        }
    }

    /**
     * Checks if a string contains duplicate characters.
     * 
     * @param str the string to check for duplicates
     * @return true if the string contains duplicate characters, false otherwise
     */
    private boolean hasDuplicates(String str) {
        java.util.HashSet<Character> seen = new java.util.HashSet<>();
        for (char character : str.toCharArray()) {
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
     * @throws InvalidCipherKeyException if the key file is invalid
     */

    private HashMap<Character, Character> createEncryptMap() throws InvalidCipherKeyException {
        char[][] keys = loadCipherKeys();
        char[] original = keys[0];
        char[] cipher = keys[1];

        HashMap<Character, Character> map = new HashMap<>();
        for (int i = 0; i < original.length; i++) {
            map.put(original[i], cipher[i]);
        }

        return map;
    }

    /**
     * Creates the decryption mapping by reversing the encryption map.
     * 
     * @return HashMap mapping cipher characters to original characters
     */
    private HashMap<Character, Character> createDecryptMap() {
        HashMap<Character, Character> map = new HashMap<>();
        for (Character key : encryptMap.keySet()) {
            map.put(encryptMap.get(key), key);
        }
        return map;
    }

    /**
     * Encrypts plaintext using the cipher mapping.
     * Unmapped characters are preserved unchanged.
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
     * Decrypts ciphertext using the cipher mapping.
     * Unmapped characters are preserved unchanged.
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
