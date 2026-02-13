import fileRead.fileHandler;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class fileHandlerTest {
    @Test
    void checkForFileReturn(){
        fileHandler filehandler = new fileHandler();//filehandler instance
        List<String> files = filehandler.getFiles();
        //calls getfiles method and stores in string


        assertNotNull(files);
        assertTrue(files.size() > 0);
        //checks that method didn't return null and there is at least 1 file.
    }//test to check if files exist

    @Test
    void checkMissingFile(){
        fileHandler filehandler = new fileHandler();

        try{
            filehandler.readFile("test.txt");
            fail("if the test passes, the readFile error handling doesnt work");
        }
        catch (RuntimeException e){
            assertTrue(true);
            //test passes if the filehandler method returns an error
        }
    }

    @Test
    void readFileTest(){
        fileHandler filehandler = new fileHandler();
        String realFile = filehandler.readFile("carnivore.txt");
        //store real file to be tested

        assertNotNull(realFile);//check to see if file is null
        assertFalse(realFile.isEmpty());
        //false if file is empty

    }
}
