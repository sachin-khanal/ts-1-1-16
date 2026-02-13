package utilities;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class CipherTest {
    private static final String CIPHERS_DIRECTORY = "ciphers/";

    private Path createTempKeyFile(String filename, List<String> lines) throws IOException {
        Path filePath = Paths.get(CIPHERS_DIRECTORY, filename);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, lines);
        return filePath;
    }

    private Cipher createStandardCipher() throws Exception {
        Path keyFile = createTempKeyFile("standard_key.txt", List.of(
                "abcdefghijklmnopqrstuvwxyz",
                "bcdefghijklmnopqrstuvwxyza"));
        return new Cipher(keyFile.toString());
    }

    private void deleteIfExists(String filename) throws IOException {
        Path path = Paths.get(CIPHERS_DIRECTORY, filename);
        Files.deleteIfExists(path);
    }

    @AfterEach
    void deleteTestKeys() throws IOException {
        deleteIfExists("valid_key.txt");
        deleteIfExists("empty_key.txt");
        deleteIfExists("single_line.txt");
        deleteIfExists("unequal.txt");
        deleteIfExists("empty_line.txt");
        deleteIfExists("duplicate_original.txt");
        deleteIfExists("duplicate_cipher.txt");
        deleteIfExists("standard_key.txt");
    }

    @Test
    void testCipherConstructorWithValidFile() throws Exception {
        Path keyFile = createTempKeyFile("valid_key.txt", List.of(
                "abcdefghijklmnopqrstuvwxyz",
                "zyxwvutsrqponmlkjihgfedcba"));
        assertDoesNotThrow(() -> new Cipher(keyFile.toString()));
    }

    @Test
    void testCipherKeyMissingFile() {
        String missingPath = Paths.get(CIPHERS_DIRECTORY, "non_existent_key.txt").toString();
        try {
            Files.deleteIfExists(Paths.get(missingPath));
        } catch (IOException e) {
            System.err.println("Failed to delete non-existent key file: " + e.getMessage());
        }

        assertThrows(InvalidCipherKeyFileException.class, () -> new Cipher(missingPath));
    }

    @Test
    void testCipherKeyEmptyFile() throws Exception {
        Path keyFile = createTempKeyFile("empty_key.txt", List.of());
        assertThrows(InvalidCipherKeyException.class, () -> new Cipher(keyFile.toString()));
    }

    @Test
    void testCipherKeySingleLine() throws Exception {
        Path keyFile = createTempKeyFile("single_line.txt", List.of("abcdef"));
        assertThrows(InvalidCipherKeyException.class, () -> new Cipher(keyFile.toString()));
    }

    @Test
    void testCipherKeyUnequalLength() throws IOException {
        Path keyFile = createTempKeyFile("unequal.txt", List.of(
                "abc",
                "abcd"));
        assertThrows(InvalidCipherKeyException.class, () -> new Cipher(keyFile.toString()));
    }

    @Test
    void testCipherKeyEmptyLine() throws IOException {
        Path keyFile = createTempKeyFile("empty_line.txt", List.of(
                "abc",
                ""));
        assertThrows(InvalidCipherKeyException.class, () -> new Cipher(keyFile.toString()));
    }

    @Test
    void testCipherKeyDuplicateInOriginal() throws IOException {
        Path keyFile = createTempKeyFile("duplicate_original.txt", List.of(
                "aba",
                "123"));
        assertThrows(InvalidCipherKeyException.class, () -> new Cipher(keyFile.toString()));
    }

    @Test
    void testCipherKeyDuplicateInCipher() throws IOException {
        Path keyFile = createTempKeyFile("duplicate_cipher.txt", List.of(
                "abc",
                "121"));
        assertThrows(InvalidCipherKeyException.class, () -> new Cipher(keyFile.toString()));
    }

    @Test
    void testBasicEncryption() throws Exception {
        Cipher cipher = createStandardCipher();
        assertEquals("bcd", cipher.encrypt("abc"));
    }

    @Test
    void testBasicDecryption() throws Exception {
        Cipher cipher = createStandardCipher();
        assertEquals("abc", cipher.decrypt("bcd"));
    }

    @Test
    void testEncryptDecryptRoundTrip() throws Exception {
        Cipher cipher = createStandardCipher();
        String original = "hello world";
        String encrypted = cipher.encrypt(original);
        String decrypted = cipher.decrypt(encrypted);
        assertEquals(original, decrypted);
    }

    @Test
    void testEmptyString() throws Exception {
        Cipher cipher = createStandardCipher();
        assertEquals("", cipher.encrypt(""));
        assertEquals("", cipher.decrypt(""));
    }

    @Test
    void testUnmappedCharacters() throws Exception {
        Cipher cipher = createStandardCipher();
        String text = "123!@#";
        assertEquals(text, cipher.encrypt(text));
        assertEquals(text, cipher.decrypt(text));
    }

    @Test
    void testMixedContent() throws Exception {
        Cipher cipher = createStandardCipher();
        assertEquals("b1", cipher.encrypt("a1"));
        assertEquals("a1", cipher.decrypt("b1"));
    }

    @Test
    void testCaseSensitivity() throws Exception {
        Cipher cipher = createStandardCipher();
        assertEquals("b", cipher.encrypt("a"));
        assertEquals("A", cipher.encrypt("A"));
    }

    @Test
    void testEncryptionChangesText() throws Exception {
        Cipher cipher = createStandardCipher();
        String plain = "abc";
        assertNotEquals(plain, cipher.encrypt(plain));
    }

    @Test
    void testDecryptionChangesText() throws Exception {
        Cipher cipher = createStandardCipher();
        String cipherText = "bcd";
        assertNotEquals(cipherText, cipher.decrypt(cipherText));
    }

    @Test
    void testNullEncryptInput() throws Exception {
        Cipher cipher = createStandardCipher();
        assertThrows(NullPointerException.class, () -> cipher.encrypt(null));
    }

    @Test
    void testNullDecryptInput() throws Exception {
        Cipher cipher = createStandardCipher();
        assertThrows(NullPointerException.class, () -> cipher.decrypt(null));
    }

    @Test
    void testCarnivore() throws Exception {
        Cipher cipher = new Cipher("ciphers/key.txt");
        String plaintext = Files.readString(Paths.get("data", "carnivore.txt"));
        String ciphertext = Files.readString(Paths.get("data", "carnivore.cip"));

        assertEquals(ciphertext, cipher.encrypt(plaintext), "Encryption of carnivore.txt failed");
        assertEquals(plaintext, cipher.decrypt(ciphertext), "Decryption of carnivore.cip failed");
    }

    @Test
    void testCointelpro() throws Exception {
        Cipher cipher = new Cipher("ciphers/key.txt");
        String plaintext = Files.readString(Paths.get("data", "cointelpro.txt"));
        String ciphertext = Files.readString(Paths.get("data", "cointelpro.cip"));

        assertEquals(ciphertext, cipher.encrypt(plaintext), "Encryption of cointelpro.txt failed");
        assertEquals(plaintext, cipher.decrypt(ciphertext), "Decryption of cointelpro.cip failed");
    }
}
