package im.abe.panda.test;

import im.abe.panda.Escape;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EscapeTest {
    private final String test = "\"<br>'&\"";

    @Test
    public void noneOn() {
        assertEquals("NONE shouldn't escape anything",
                test, Escape.NONE.on(test));
    }

    @Test
    public void htmlOn() {
        assertEquals("HTML should escape appropriate characters",
                "&quot;&lt;br&gt;&#39;&amp;&quot;", Escape.HTML.on(test));
    }
}
