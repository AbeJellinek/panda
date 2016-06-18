package im.abe.panda;

import im.abe.panda.internal.TemplateParser;
import im.abe.panda.internal.ast.Node;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A renderable template. This class serves as an abstraction over the actual template implementation.
 */
public class Template {
    @NotNull
    private List<Node> rootNodes;
    @NotNull
    private Interpreter interpreter;
    @NotNull
    private Escape escapeStrategy;

    private Template(@NotNull List<Node> rootNodes, @NotNull Interpreter interpreter,
                     @NotNull Escape escapeStrategy) {
        this.rootNodes = rootNodes;
        this.interpreter = interpreter;
        this.escapeStrategy = escapeStrategy;
    }

    public Template escapeStrategy(@NotNull Escape escapeStrategy) {
        this.escapeStrategy = escapeStrategy;
        return this;
    }

    public String writeToString(@NotNull Map<String, Object> context) {
        StringWriter writer = new StringWriter();
        try {
            write(new BufferedWriter(writer), context);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }

    public void write(@NotNull BufferedWriter writer, @NotNull Map<String, Object> context) throws IOException {
        for (Node node : rootNodes) {
            interpreter.execute(this, node, context, writer);
            writer.flush();
        }
    }

    public static Template from(String text) {
        try {
            return from(new BufferedReader(new StringReader(text)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Template from(File file) throws IOException {
        return from(new BufferedReader(new FileReader(file)));
    }

    public static Template from(BufferedReader reader) throws IOException {
        TemplateParser parser = new TemplateParser(reader);
        List<Node> rootNodes = new ArrayList<>();
        while (true) {
            Node node = parser.next();
            if (node == null)
                break;
            rootNodes.add(node);
        }

        return new Template(rootNodes, new Interpreter(), Escape.HTML);
    }

    String escape(String text) {
        switch (escapeStrategy) {
            case HTML:
                return text
                        .replace("&", "&amp;")
                        .replace("<", "&lt;")
                        .replace(">", "&gt;")
                        .replace("\"", "&quot;")
                        .replace("'", "&#39;");
            default:
                return text;
        }
    }
}
