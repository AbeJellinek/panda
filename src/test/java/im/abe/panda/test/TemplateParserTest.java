package im.abe.panda.test;

import im.abe.panda.internal.TemplateParser;
import im.abe.panda.internal.ast.Text;
import im.abe.panda.internal.ast.Variable;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class TemplateParserTest {
    @Test
    public void next() throws Exception {
        TemplateParser parser = new TemplateParser(
                new BufferedReader(
                        new StringReader("Hello! 123[[^value]][[value2.x]][]]")));

        assertEquals("parser.next() should be 'Hello! 123'",
                new Text("Hello! 123"), parser.next());
        assertEquals("parser.next() should be variable 'value' and raw",
                new Variable(null, "value", true), parser.next());
        assertEquals("parser.next() should be variable 'x' in 'value2'",
                new Variable(new Variable(null, "value2", false), "x", false), parser.next());
        assertEquals("parser.next() should be ']'",
                new Text("[]]"), parser.next());
        assertEquals("parser.next() after end of input should be null",
                null, parser.next());
    }
}
