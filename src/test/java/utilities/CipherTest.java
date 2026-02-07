package utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CipherTest {
    private Cipher cipher;

    @BeforeEach
    void setUp() {
        this.cipher = new Cipher();
    }

    // ============================================
    // Cipher Key Validation Tests
    // ============================================

    @Test
    void testCipherKeyMissingFile() {
        assertThrows(InvalidCipherKeyException.class, () -> {
            String originalKeyPath = "ciphers/key.txt";
            String tempKeyPath = "ciphers/test_missing.txt";

            // Temporarily rename key file to simulate missing file
            Files.move(Paths.get(originalKeyPath), Paths.get(tempKeyPath));
            try {
                new Cipher();
            } finally {
                Files.move(Paths.get(tempKeyPath), Paths.get(originalKeyPath));
            }
        });
    }

    @Test
    void testCipherKeyUnequalLength() throws Exception {
        String tempKeyPath = "ciphers/test_unequal.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempKeyPath))) {
            writer.write("abcdef\n");
            writer.write("123\n"); // Different length
        }

        assertThrows(InvalidCipherKeyException.class, () -> {
            String originalKeyPath = "ciphers/key.txt";
            Files.move(Paths.get(originalKeyPath), Paths.get("ciphers/test_backup.txt"));
            try {
                Files.move(Paths.get(tempKeyPath), Paths.get(originalKeyPath));
                new Cipher();
            } finally {
                Files.move(Paths.get(originalKeyPath), Paths.get(tempKeyPath));
                Files.move(Paths.get("ciphers/test_backup.txt"), Paths.get(originalKeyPath));
            }
        });
        Files.deleteIfExists(Paths.get(tempKeyPath));
    }

    @Test
    void testCipherKeyDuplicateInOriginal() throws Exception {
        String tempKeyPath = "ciphers/test_dup_orig.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempKeyPath))) {
            writer.write("aabbcc\n");
            writer.write("123456\n");
        }

        assertThrows(InvalidCipherKeyException.class, () -> {
            String originalKeyPath = "ciphers/key.txt";
            Files.move(Paths.get(originalKeyPath), Paths.get("ciphers/test_backup.txt"));
            try {
                Files.move(Paths.get(tempKeyPath), Paths.get(originalKeyPath));
                new Cipher();
            } finally {
                Files.move(Paths.get(originalKeyPath), Paths.get(tempKeyPath));
                Files.move(Paths.get("ciphers/test_backup.txt"), Paths.get(originalKeyPath));
            }
        });
        Files.deleteIfExists(Paths.get(tempKeyPath));
    }

    @Test
    void testCipherKeyDuplicateInCipher() throws Exception {
        String tempKeyPath = "ciphers/test_dup_cipher.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempKeyPath))) {
            writer.write("abcdef\n");
            writer.write("112233\n");
        }

        assertThrows(InvalidCipherKeyException.class, () -> {
            String originalKeyPath = "ciphers/key.txt";
            Files.move(Paths.get(originalKeyPath), Paths.get("ciphers/test_backup.txt"));
            try {
                Files.move(Paths.get(tempKeyPath), Paths.get(originalKeyPath));
                new Cipher();
            } finally {
                Files.move(Paths.get(originalKeyPath), Paths.get(tempKeyPath));
                Files.move(Paths.get("ciphers/test_backup.txt"), Paths.get(originalKeyPath));
            }
        });
        Files.deleteIfExists(Paths.get(tempKeyPath));
    }

    @Test
    void testCipherKeyEmptyLine() throws Exception {
        String tempKeyPath = "ciphers/test_empty.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempKeyPath))) {
            writer.write("\n");
            writer.write("abcdef\n");
        }

        assertThrows(InvalidCipherKeyException.class, () -> {
            String originalKeyPath = "ciphers/key.txt";
            Files.move(Paths.get(originalKeyPath), Paths.get("ciphers/test_backup.txt"));
            try {
                Files.move(Paths.get(tempKeyPath), Paths.get(originalKeyPath));
                new Cipher();
            } finally {
                Files.move(Paths.get(originalKeyPath), Paths.get(tempKeyPath));
                Files.move(Paths.get("ciphers/test_backup.txt"), Paths.get(originalKeyPath));
            }
        });
        Files.deleteIfExists(Paths.get(tempKeyPath));
    }

    // ============================================
    // Basic Functionality Tests
    // ============================================

    @Test
    void testBasicEncryption() {
        String plaintext = "abc";
        String encrypted = this.cipher.encrypt(plaintext);
        assertEquals("bcd", encrypted);
    }

    // Test basic decryption
    @Test
    void testBasicDecryption() {
        String ciphertext = "bcd";
        String decrypted = this.cipher.decrypt(ciphertext);
        assertEquals("abc", decrypted);
    }

    // Test round-trip (encrypt then decrypt should return original)
    @Test
    void testEncryptDecryptRoundTrip() {
        String original = "Hello World 123";
        String encrypted = this.cipher.encrypt(original);
        String decrypted = this.cipher.decrypt(encrypted);
        assertEquals(original, decrypted);
    }

    // Test with an empty string
    @Test
    void testEmptyString() {
        assertEquals("", this.cipher.encrypt(""));
        assertEquals("", this.cipher.decrypt(""));
    }

    // Test with unmapped characters (should remain unchanged)
    @Test
    void testUnmappedCharacters() {
        String plaintext = "!@#$%";
        String encrypted = this.cipher.encrypt(plaintext);
        assertEquals(plaintext, encrypted); // Special chars not in key remain same
    }

    // Test mixed content (mapped and unmapped)
    @Test
    void testMixedContent() {
        String plaintext = "Hello, World!";
        String encrypted = this.cipher.encrypt(plaintext);
        String decrypted = this.cipher.decrypt(encrypted);
        assertEquals(plaintext, decrypted);
    }

    // Test case sensitivity
    @Test
    void testCaseSensitivity() {
        String lowercase = "abc";
        String uppercase = "ABC";
        assertNotEquals(this.cipher.encrypt(lowercase), this.cipher.encrypt(uppercase));
    }

    // Test the full alphabet
    @Test
    void testFullAlphabet() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        String encrypted = this.cipher.encrypt(alphabet);
        String decrypted = this.cipher.decrypt(encrypted);
        assertEquals(alphabet, decrypted);
    }

    // Test numbers
    @Test
    void testNumbers() {
        String numbers = "1234567890";
        String encrypted = this.cipher.encrypt(numbers);
        String decrypted = this.cipher.decrypt(encrypted);
        assertEquals(numbers, decrypted);
    }

    // Test whitespace preservation
    @Test
    void testWhitespace() {
        String text = "Hello World";
        String encrypted = this.cipher.encrypt(text);
        assertTrue(encrypted.contains(" ")); // Space should be preserved
        assertEquals(text, this.cipher.decrypt(encrypted));
    }

    // Test a single character
    @Test
    void testSingleCharacter() {
        String single = "a";
        String encrypted = this.cipher.encrypt(single);
        assertEquals("b", encrypted);
        assertEquals(single, this.cipher.decrypt(encrypted));
    }

    // Test long text
    @Test
    void testLongText() {
        String longText = "The quick brown fox jumps over the lazy dog 1234567890";
        String encrypted = this.cipher.encrypt(longText);
        String decrypted = this.cipher.decrypt(encrypted);
        assertEquals(longText, decrypted);
    }

    // Test encryption is not identity (actually changes the text)
    @Test
    void testEncryptionChangesText() {
        String plaintext = "abc";
        String encrypted = this.cipher.encrypt(plaintext);
        assertNotEquals(plaintext, encrypted);
    }

    // Test decryption is not identity (actually changes the text)
    @Test
    void testDecryptionChangesText() {
        String ciphertext = "bcd";
        String decrypted = this.cipher.decrypt(ciphertext);
        assertNotEquals(ciphertext, decrypted);
    }

    // Test with real data files - carnivore.txt encryption
    @Test
    void testCarnivoreEncryption() throws Exception {
        String plaintext = readFile("data/carnivore.txt");
        String expectedCipher = readFile("data/carnivore.cip");
        String actualCipher = this.cipher.encrypt(plaintext);
        assertEquals(expectedCipher, actualCipher);
    }

    // Test with real data files - carnivore.cip decryption
    @Test
    void testCarnivoreDecryption() throws Exception {
        String ciphertext = readFile("data/carnivore.cip");
        String expectedPlain = readFile("data/carnivore.txt");
        String actualPlain = this.cipher.decrypt(ciphertext);
        assertEquals(expectedPlain, actualPlain);
    }

    // Test with real data files - cointelpro.txt encryption
    @Test
    void testCointelproEncryption() throws Exception {
        String plaintext = readFile("data/cointelpro.txt");
        String expectedCipher = readFile("data/cointelpro.cip");
        String actualCipher = this.cipher.encrypt(plaintext);
        assertEquals(expectedCipher, actualCipher);
    }

    // Test with real data files - cointelpro.cip decryption
    @Test
    void testCointelproDecryption() throws Exception {
        String ciphertext = readFile("data/cointelpro.cip");
        String expectedPlain = readFile("data/cointelpro.txt");
        String actualPlain = this.cipher.decrypt(ciphertext);
        assertEquals(expectedPlain, actualPlain);
    }

    // Test round-trip with carnivore data
    @Test
    void testCarnivoreRoundTrip() throws Exception {
        String original = readFile("data/carnivore.txt");
        String encrypted = this.cipher.encrypt(original);
        String decrypted = this.cipher.decrypt(encrypted);
        assertEquals(original, decrypted);
    }

    // Test round-trip with cointelpro data
    @Test
    void testCointelproRoundTrip() throws Exception {
        String original = readFile("data/cointelpro.txt");
        String encrypted = this.cipher.encrypt(original);
        String decrypted = this.cipher.decrypt(encrypted);
        assertEquals(original, decrypted);
    }

    // Helper method to read file contents
    private String readFile(String path) throws Exception {
        StringBuilder content = new StringBuilder();
        try (java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!content.isEmpty()) {
                    content.append("\n");
                }
                content.append(line);
            }
        }
        return content.toString();
    }
}