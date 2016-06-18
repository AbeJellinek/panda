package im.abe.panda.test;

import im.abe.panda.Escape;
import im.abe.panda.Template;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class TemplateTest {
    @Test
    public void simple() {
        class Place {
            private String name;
            private int temperature;

            private Place(String name, int temperature) {
                this.name = name;
                this.temperature = temperature;
            }

            public String getName() {
                return name;
            }
        }

        Template template = Template.from("Hello, [[place.name]]! It's [[place.temperature]] degrees there.");
        Map<String, Object> context = new HashMap<>();
        context.put("place", new Place("world", 72));

        assertEquals("Hello, world! It's 72 degrees there.", template.renderToString(context));
    }

    @Test
    public void nullHandling() {
        Template template = Template.from("Hello, [[person.name]]!");
        Map<String, Object> context = new HashMap<>();
        context.put("person", null);

        assertEquals("Hello, !", template.renderToString(context));
    }

    @Test
    public void escaping() {
        Template template1 = Template.from("Well, well, well. [[message]]");
        Map<String, Object> context1 = new HashMap<>();
        context1.put("message", "<br>Look who we have here.");

        assertEquals("Well, well, well. &lt;br&gt;Look who we have here.", template1.renderToString(context1));

        Template template2 = Template.from("Well, well, well. [[^message]]");
        Map<String, Object> context2 = new HashMap<>();
        context2.put("message", "<br>Look who we have here.");

        assertEquals("Well, well, well. <br>Look who we have here.", template2.renderToString(context2));
        template1.escapeStrategy(Escape.NONE);
        assertEquals(template1.renderToString(context1), template2.renderToString(context2));
    }

    @Test
    public void readingInput() throws IOException, URISyntaxException {
        Template template = Template.from(getClass().getResource("/template1/input.html").toURI());
        Map<String, Object> context = new HashMap<>();
        context.put("title", "Home");
        context.put("name", "Person");
        context.put("time", "12:00");

        Scanner outputScanner = new Scanner(getClass().getResourceAsStream("/template1/output.html"))
                .useDelimiter("\\A");
        String expected = outputScanner.hasNext() ? outputScanner.next() : "";

        assertEquals(expected, template.renderToString(context));
    }
}
