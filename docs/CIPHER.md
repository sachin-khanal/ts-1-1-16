# Cipher Utility Documentation

## Overview

The Cipher utility provides an implementation of a cipher for encrypting and decrypting text with built-in validation of the cipher key to ensure data integrity and proper format.

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
│ - loadCipherKey()       │
│ - createDecryptMap()    │
└─────────────────────────┘
```

### Components

- **`CipherInterface`**: Defines the contract for cipher implementations
- **`Cipher`**: Concrete implementation using HashMap-based character substitution with cipher key validation
- **`InvalidCipherKeyException`**: Custom exception thrown when cipher key validation fails
- **`CipherTest`**: Comprehensive test suite with 26 test cases

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

Encrypts the given plaintext using the cipher's character mapping.

**Parameters:**
- `plaintext` - The text to be encrypted (must not be null)

**Returns:**
- The encrypted version of the plaintext

**Behavior:**
- Characters in the cipher key are substituted
- Unmapped characters (punctuation, whitespace, etc.) remain unchanged

---

#### `String decrypt(String ciphertext)`

Decrypts the given ciphertext using the cipher's character mapping.

**Parameters:**
- `ciphertext` - The encrypted text to be decrypted (must not be null)

**Returns:**
- The decrypted version of the ciphertext (original plaintext)

**Behavior:**
- Inverse operation of `encrypt()`
- Guarantees: `decrypt(encrypt(text)).equals(text)` for all text

---

### `Cipher` Class

#### Constructor

```java
public Cipher();
```

Constructs a new Cipher instance by loading the cipher key from `ciphers/key.txt` and initializing both encryption and decryption mappings.

**Error Handling:**
- If the key file cannot be read, an `InvalidCipherKeyException` is thrown
- If the key file format is invalid, detailed error messages explain the validation failure

**Throws:**
- `InvalidCipherKeyException` - if the cipher key is missing, malformed, or fails validation

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

The `CipherTest` suite provides comprehensive validation with **26 test cases**:

#### Cipher Key Validation Tests (5 tests)
- `testCipherKeyMissingFile()` - Missing cipher key file handling
- `testCipherKeyUnequalLength()` - Lines with different lengths
- `testCipherKeyDuplicateInOriginal()` - Duplicate characters in original alphabet
- `testCipherKeyDuplicateInCipher()` - Duplicate characters in cipher alphabet
- `testCipherKeyEmptyLine()` - Empty lines in cipher key file

#### Basic Functionality (3 tests)
- `testBasicEncryption()` - Simple character substitution
- `testBasicDecryption()` - Reverse substitution
- `testEncryptDecryptRoundTrip()` - Symmetric operation validation

#### Edge Cases (4 tests)
- `testEmptyString()` - Empty input handling
- `testSingleCharacter()` - Minimal input
- `testLongText()` - Large input (60+ characters)

#### Character Types (5 tests)
- `testFullAlphabet()` - All lowercase letters
- `testNumbers()` - Numeric characters
- `testCaseSensitivity()` - Upper vs. lowercase
- `testUnmappedCharacters()` - Special characters
- `testWhitespace()` - Space preservation

#### Validation Tests (3 tests)
- `testEncryptionChangesText()` - Ensures actual transformation
- `testDecryptionChangesText()` - Ensures reverse transformation
- `testMixedContent()` - Combination of mapped/unmapped

#### Real Data Tests (6 tests)
- `testCarnivoreEncryption()` - Encrypt sample file
- `testCarnivoreDecryption()` - Decrypt sample file
- `testCarnivoreRoundTrip()` - Full cycle validation
- `testCointelproEncryption()` - Encrypt sample file
- `testCointelproDecryption()` - Decrypt sample file
- `testCointelproRoundTrip()` - Full cycle validation

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
System.out.println("Original:  " + message);
System.out.println("Encrypted: " + encrypted);
System.out.println("Decrypted: " + cipher.decrypt(encrypted));
```

**Output:**
```
Original:  Meet me at noon
Encrypted: Nffu nf bu oppo
Decrypted: Meet me at noon
```