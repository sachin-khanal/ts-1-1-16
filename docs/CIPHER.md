# Cipher Utility Documentation

## Overview

The Cipher utility provides an implementation of a cipher for encrypting and decrypting text with built-in validation of the cipher key.
---

## Architecture

### Class Diagram

```
┌─────────────────────────┐
│   CipherInterface       │
├─────────────────────────┤
│ + encrypt(String)       │
│ + decrypt(String)       │
└──────────▲──────────────┘
           │
           │ implements
           │
┌──────────┴──────────────┐
│       Cipher            │
├─────────────────────────┤
│ - encryptMap: HashMap   │
│ - decryptMap: HashMap   │
│ - CIPHER_KEY_PATH       │
├─────────────────────────┤
│ + Cipher()              │
│ + encrypt(String)       │
│ + decrypt(String)       │
│ - loadCipherKeys()      │
│ - createEncryptMap()    │
│ - createDecryptMap()    │
│ - hasDuplicates()       │
└─────────────────────────┘
```

### Components

- **`CipherInterface`**: Defines the contract for cipher implementations
- **`Cipher`**: HashMap-based substitution cipher with key file validation
- **`InvalidCipherKeyException`**: Thrown when cipher key validation fails
- **`CipherTest`**: 25 comprehensive test cases

---

## Quick Start

### Basic Usage

```java
import utilities.Cipher;

public class Example {
    public static void main(String[] args) {
        Cipher cipher = new Cipher();
        
        // Encrypt plaintext
        String plaintext = "Hello, World!";
        String encrypted = cipher.encrypt(plaintext);
        System.out.println("Encrypted: " + encrypted);
        
        // Decrypt ciphertext
        String decrypted = cipher.decrypt(encrypted);
        System.out.println("Decrypted: " + decrypted);
        
        // Verify round-trip
        assert plaintext.equals(decrypted);
    }
}
```

### Integration with Other Programs

The Cipher can be bound to other programs using **dependency injection**:

```java
public class TopSecret {
    private CipherInterface cipher;

    // Constructor injection (recommended)
    public TopSecret(CipherInterface cipher) {
        this.cipher = cipher;
    }

    public void processFile(String inputFile, String outputFile) {
        String content = readFile(inputFile);
        String encrypted = cipher.encrypt(content);
        writeFile(outputFile, encrypted);
    }

}

// Usage
Cipher cipher = new Cipher();
TopSecret app = new TopSecret(cipher);
app.processFile("input.txt", "output.enc");
```

---

## API Reference

### `CipherInterface`

#### `String encrypt(String plaintext)`

Encrypts plaintext using the cipher mapping.

**Parameters:**
- `plaintext` - Text to encrypt

**Returns:**
- Encrypted text with mapped characters substituted, unmapped characters unchanged

---

#### `String decrypt(String ciphertext)`

Decrypts ciphertext using the cipher mapping (inverse of `encrypt()`).

**Parameters:**
- `ciphertext` - Text to decrypt

**Returns:**
- Decrypted text; `decrypt(encrypt(text)).equals(text)` for all text

---

### `Cipher` Class

#### Constructor

```java
public Cipher();
```

Loads cipher key from `ciphers/key.txt` and initializes encryption/decryption mappings.

**Throws:**
- `InvalidCipherKeyException` - if key file is missing, malformed, or fails validation

---

## Exception Classes

### `InvalidCipherKeyException`

Custom exception thrown when cipher key validation fails.

**When it is thrown:**
- Cipher key file does not exist or cannot be read
- File does not contain exactly two lines
- Lines have unequal length
- Either line is empty or null
- Original alphabet contains duplicate characters
- Cipher alphabet contains duplicate characters

**Example:**
```java
try {
    Cipher cipher = new Cipher();
} catch (InvalidCipherKeyException e) {
    System.err.println("Failed to initialize cipher: " + e.getMessage());
}
```

---

## Cipher Key Validation

When the Cipher is initialized, the cipher key file is automatically validated against the following rules:

### Validation Rules

1. **File Existence**: The file must exist at `ciphers/key.txt`
2. **Line Count**: The file must contain exactly 2 lines
3. **Non-Null**: Both lines must not be null
4. **Non-Empty**: Both lines must not be empty
5. **Equal Length**: Both lines must have the same length
6. **Unique Characters (Original)**: The original alphabet line must contain no duplicate characters
7. **Unique Characters (Cipher)**: The cipher alphabet line must contain no duplicate characters

### Validation Failure Handling

If any validation rule is violated, an `InvalidCipherKeyException` is thrown with a descriptive error message:

| Validation Failure | Exception Message |
|---|---|
| Missing or unreadable file | `Failed to read cipher key file at ciphers/key.txt: ...` |
| Missing a line | `Cipher key file must contain exactly two lines. Found: 1` |
| Null line | `Cipher key file must contain exactly two lines. Found: 1` |
| Empty line | `Neither line in the cipher key file can be empty.` |
| Unequal length | `Cipher key lines must have equal length. Original: X, Cipher: Y` |
| Duplicate in original | `Original alphabet contains duplicate characters.` |
| Duplicate in cipher | `Cipher alphabet contains duplicate characters.` |

---

### Cipher Key File (`ciphers/key.txt`)

**Format:**
```
<original_alphabet>
<cipher_alphabet>
```

**Requirements:**
- Exactly 2 lines
- Both lines must have equal length
- Each character in line 1 maps to the character at the same position in line 2
- No duplicate characters within a line

**Example:**
```
abcdefghijklmnopqrstuvwxyz
bcdefghijklmnopqrstuvwxyza
```

---
## Testing

### Test Coverage

The `CipherTest` suite includes **29 comprehensive test cases** organized into the following categories:

#### Key Validation Tests (5)
Tests that verify the cipher key file is properly validated:

1. **`testCipherKeyMissingFile`** - Verifies exception thrown when key file doesn't exist
2. **`testCipherKeyUnequalLength`** - Validates error when original and cipher alphabets have different lengths
3. **`testCipherKeyDuplicateInOriginal`** - Ensures duplicate characters in original alphabet are detected
4. **`testCipherKeyDuplicateInCipher`** - Ensures duplicate characters in cipher alphabet are detected
5. **`testCipherKeyEmptyLine`** - Validates error when cipher key file contains empty lines

#### Basic Operations Tests (3)
Core functionality tests:

6. **`testBasicEncryption`** - Tests simple encryption ("abc" → "bcd")
7. **`testBasicDecryption`** - Tests simple decryption ("bcd" → "abc")
8. **`testEncryptDecryptRoundTrip`** - Verifies encrypt→decrypt returns original text

#### Edge Cases Tests (5)
Tests for boundary conditions and special scenarios:

9. **`testEmptyString`** - Handles empty string input for both encrypt and decrypt
10. **`testSingleCharacter`** - Tests encryption/decryption of single character
11. **`testLongText`** - Validates handling of long text strings
12. **`testMixedContent`** - Tests text with both mapped and unmapped characters
13. **`testDoubleRoundTrip`** - Tests double encryption and double decryption round-trip

#### Character Types Tests (5)
Tests for different character categories:

14. **`testFullAlphabet`** - Encrypts and decrypts entire lowercase alphabet
15. **`testNumbers`** - Verifies numeric characters are handled correctly
16. **`testCaseSensitivity`** - Ensures uppercase and lowercase produce different results
17. **`testUnmappedCharacters`** - Verifies unmapped characters (e.g., special symbols) remain unchanged
18. **`testWhitespace`** - Tests that whitespace is preserved during encryption/decryption

#### Transformation Tests (2)
Validates that cipher actually transforms text:

19. **`testEncryptionChangesText`** - Confirms encryption modifies the input
20. **`testDecryptionChangesText`** - Confirms decryption modifies the input

#### Null Handling Tests (2)
Tests for null input handling:

21. **`testNullEncryptInput`** - Verifies NullPointerException thrown for null encrypt input
22. **`testNullDecryptInput`** - Verifies NullPointerException thrown for null decrypt input

#### Instance Tests (1)
Tests for proper instantiation:

23. **`testRepeatedInstantiation`** - Verifies multiple Cipher instances behave consistently

#### Real Data Tests (6)
Integration tests using actual data files:

24. **`testCarnivoreEncryption`** - Encrypts carnivore.txt and compares to carnivore.cip
25. **`testCarnivoreDecryption`** - Decrypts carnivore.cip and compares to carnivore.txt
26. **`testCointelproEncryption`** - Encrypts cointelpro.txt and compares to cointelpro.cip
27. **`testCointelproDecryption`** - Decrypts cointelpro.cip and compares to cointelpro.txt
28. **`testCarnivoreRoundTrip`** - Verifies encrypt→decrypt round-trip with carnivore.txt
29. **`testCointelproRoundTrip`** - Verifies encrypt→decrypt round-trip with cointelpro.txt

### Running Tests

```bash
# Run all tests
./gradlew test

# Run with verbose output
./gradlew test --info

# Run specific test
./gradlew test --tests "utilities.CipherTest.testBasicEncryption"
```
---

## Example

```java
Cipher cipher = new Cipher();
String message = "Meet me at noon";
String encrypted = cipher.encrypt(message);
String decrypted = cipher.decrypt(encrypted);

System.out.println("Original:  " + message);
System.out.println("Encrypted: " + encrypted);
System.out.println("Decrypted: " + decrypted);
```