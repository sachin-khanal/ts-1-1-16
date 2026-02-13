import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgramControllerTest {

    @Mock
    private FileHandler fileHandler;

    private ProgramController controller;

    @BeforeEach
    void setUp() {
        controller = new ProgramController(fileHandler);
    }

    @Test
    void noArguments() {

        when(fileHandler.getFiles()).thenReturn(Arrays.asList("file1.txt", "file2.txt"));
        String result = controller.run(new String[] {});

        assertEquals("1. file1.txt\n2. file2.txt\n", result);
        verify(fileHandler).getFiles();
        verifyNoMoreInteractions(fileHandler);
    }

    @Test
    void checkIndex() {

        when(fileHandler.getFiles()).thenReturn(Arrays.asList("a.txt", "b.txt"));
        when(fileHandler.readFile("b.txt")).thenReturn("Hello");
        String result = controller.run(new String[] { "2" });

        assertEquals("Hello", result);
        verify(fileHandler).getFiles();
        verify(fileHandler).readFile("b.txt");
    }

    @Test
    void checkInvalidNumber() {

        when(fileHandler.getFiles()).thenReturn(Arrays.asList("a.txt"));
        String result = controller.run(new String[] { "abc" });

        assertEquals("Error: file needs to be an integer", result);
        verify(fileHandler).getFiles();
        verify(fileHandler, never()).readFile(anyString());
    }

    @Test
    void outOfRange() {

        when(fileHandler.getFiles()).thenReturn(Arrays.asList("a.txt"));
        String result = controller.run(new String[] { "5" });

        assertEquals("Error: out of range", result);
        verify(fileHandler).getFiles();
        verify(fileHandler, never()).readFile(anyString());
    }
}
