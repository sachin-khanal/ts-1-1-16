import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TopSecretTest {

    @Test
    void getArgs_returnsSameReference() {
        String[] args = { "01", "key" };
        assertSame(args, TopSecret.getArgs(args));
    }

    @Test
    void getArgs_emptyArray() {
        String[] args = {};
        assertArrayEquals(new String[] {}, TopSecret.getArgs(args));
    }
}
