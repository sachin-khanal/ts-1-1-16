import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class FileHandlerTest {
    @Test
    void checkForFileReturn() {
        FileHandler filehandler = new FileHandler();// filehandler instance
        List<String> files = filehandler.getFiles();
        // calls getfiles method and stores in string

        assertNotNull(files);
        assertTrue(files.size() > 0);
        // checks that method didn't return null and there is at least 1 file.
    }// test to check if files exist

    @Test
    void checkMissingFile() {
        FileHandler filehandler = new FileHandler();

        try {
            filehandler.readFile("test.txt");
            fail("if the test passes, the readFile error handling doesnt work");
        } catch (RuntimeException e) {
            assertTrue(true);
            // test passes if the filehandler method returns an error
        }
    }

    @Test
    void readFileTest() {
        FileHandler filehandler = new FileHandler();
        String realFile = filehandler.readFile("carnivore.txt");
        // store real file to be tested

        assertNotNull(realFile);// check to see if file is null
        assertFalse(realFile.isEmpty());
        // false if file is empty

    }

    @Test
    void checkForCIP() {
        FileHandler filehandler = new FileHandler();
        String testedFile = filehandler.readFile("carnivore.txt");
        String encryptedFile = filehandler.readFile("carnivore.cip");
        assertNotNull(encryptedFile);
        assertEquals(testedFile.trim(), encryptedFile.trim());
    }// check to see if the encrypted file is decryped after read.readFile is called.
}
