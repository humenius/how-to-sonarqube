import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MessageTest {

    @Test
    public void testRot13() {
        String expected = "nop";
        String actual = Message.rot13("abc");

        assertEquals(expected, actual);
    }
}
