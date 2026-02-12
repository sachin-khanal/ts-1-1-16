import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ProgramControllerTest {

    class MockCLI implements CommandLineInterface {

        @Override
        public UserRequest parseArguments(String[] args) {
            if (args.length == 0) {
                return new UserRequest(null);
            }
            return new UserRequest(args[0]);
        }
    }

    class MockFileHandler implements FileHandler {

        public List<String> findFiles() {
            return List.of("file1.txt", "file2.txt");
        }
        public String readFile(String filename) {
            return "Mock file content";
        }
    }

    @Test
    void noArguments() {

        ProgramController controller = new ProgramController(new MockCLI(), new MockFileHandler());
        assertDoesNotThrow(() -> controller.run(new String[]{}));
    }

    @Test
    void validArguments() {

        ProgramController controller = new ProgramController(new MockCLI(), new MockFileHandler());
        assertDoesNotThrow(() -> controller.run(new String[]{"1"}));
    }

    @Test
    void outOfRangeArguments() {

        ProgramController controller = new ProgramController(new MockCLI(), new MockFileHandler());
        assertDoesNotThrow(() -> controller.run(new String[]{"99"}));
    }

    @Test
    void notNumericArguments() {

        ProgramController controller = new ProgramController(new MockCLI(), new MockFileHandler());
        assertDoesNotThrow(() -> controller.run(new String[]{"abc"}));
    }
}