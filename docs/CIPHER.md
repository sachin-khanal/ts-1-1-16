# Cipher Utility Documentation

## Overview

The Cipher utility provides an implementation of a cipher for encrypting and decrypting text with built-in validation of the cipher key.

---

## Architecture

### Class Diagram

```
┌──────────────────────────┐
│   CipherInterface        │
├──────────────────────────┤
│ + encrypt(String)        │
│ + decrypt(String)        │
└──────────▲───────────────┘  
           │
           │ implements
           │
┌──────────┴───────────────┐
│       Cipher             │
├──────────────────────────┤
│ - encryptMap: HashMap    │
│ - decryptMap: HashMap    │
│ - CIPHER_KEY_FILE_PATH   │
│ - DEFAULT_KEY_FILE_PATH  │
│ - keys: char[][]         │
├──────────────────────────┤
│ + Cipher()               │
│ + Cipher(String)         │
│ + encrypt(String)        │
│ + decrypt(String)        │
│ - loadCipherKeys()       │
│ - validateCipherKeys()   │
│ - hasDuplicates()        │
│ - createEncryptMap()     │
│ - createDecryptMap()     │
└──────────────────────────┘
```

### Components

- **`CipherInterface`**: Defines the contract for cipher implementations
- **`Cipher`**: HashMap-based cipher with key file validation
- **`InvalidCipherKeyException`**: Thrown when cipher key validation fails
- **`InvalidCipherKeyFileException`**: Thrown when cipher key file is invalid
- **`CipherTest`**: JUnit 5 Tests

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
    }
}
```

---

# OR
```java
import utilities.Cipher;

public class Example {
    public static void main(String[] args) {
        Cipher cipher = new Cipher("ciphers/key.txt");
        
        // Encrypt plaintext
        String plaintext = "Hello, World!";
        String encrypted = cipher.encrypt(plaintext);
        System.out.println("Encrypted: " + encrypted);
        
        // Decrypt ciphertext
        String decrypted = cipher.decrypt(encrypted);
        System.out.println("Decrypted: " + decrypted);
    }
}
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

Decrypts ciphertext using the cipher mapping.

**Parameters:**
- `ciphertext` - Text to decrypt

**Returns:**
- Decrypted text

---

### `Cipher` Class

#### Constructor

```java
public Cipher();
public Cipher(String keyFilePath);
```

Loads cipher key from `ciphers/key.txt` (default) or specified path.

**Throws:**
- `InvalidCipherKeyException` - if key file validation fails
- `InvalidCipherKeyFileException` - if key file is missing or unreadable

---

## Exception Classes

- **`InvalidCipherKeyException`**: Validation errors.
- **`InvalidCipherKeyFileException`**: IO errors.

---

## Cipher Key Validation

Rules validated at initialization:

1. **File Existence**: File must exist.
2. **Line Count**: Exactly 2 lines.
3. **Non-Empty**: Lines cannot be empty.
4. **Equal Length**: Lines must have same length.
5. **Unique Characters**: No duplicates within either line.
---

## Testing

### Test Strategy

The `CipherTest` suite uses **dynamic file creation within the `ciphers/` directory** to isolate most tests. Data integration tests use the production `ciphers/key.txt`. Test files are automatically cleaned up after each test execution.

### Test Categories

1.  **Validation Tests**: Verify all validation rules using dynamically created files in `ciphers/` with specific invalid content.
2.  **Constructor Tests**: Verify `Cipher` can be instantiated with custom paths.
3.  **Logic Tests**: Verify encryption/decryption (Round trip, empty string, special chars) using a known standard cipher key file created in `ciphers/`.
4.  **Data Integration Tests**: Verify encryption/decryption of `data/carnivore.txt` and `data/cointelpro.txt` using the production `ciphers/key.txt`.
5.  **Error Handling**: Verify null inputs throw `NullPointerException`.

### Running Tests

```bash
./gradlew test
```
