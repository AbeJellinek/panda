package im.abe.panda;

import im.abe.panda.internal.TemplateParser;
import im.abe.panda.internal.ast.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class Main {
    public static void main(String[] args) throws IOException {
        TemplateParser parser = new TemplateParser(new BufferedReader(new StringReader("abc@xyz.a123@a 123")));
        Node node;
        while ((node = parser.next()) != null) {
            System.out.println(node);
        }
    }
}
