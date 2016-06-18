package im.abe.panda;

import im.abe.panda.internal.Interpreter;
import im.abe.panda.internal.TemplateParser;
import im.abe.panda.internal.ast.Node;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A renderable template.
 * Construct an instance using the {@link #from(String)}, {@link #from(File)},
 * or {@link #from(BufferedReader)} methods.
 */
public class Template {
    @NotNull
    private List<Node> rootNodes;
    @NotNull
    private Interpreter interpreter;
    @NotNull
    private Escape escapeStrategy;

    /**
     * Construct a new Template instance.
     *
     * @param rootNodes      The top-level nodes of the template.
     * @param interpreter    The interpreter instance to use.
     * @param escapeStrategy The variable escape strategy to be used by this template.
     */
    private Template(@NotNull List<Node> rootNodes, @NotNull Interpreter interpreter,
                     @NotNull Escape escapeStrategy) {
        this.rootNodes = rootNodes;
        this.interpreter = interpreter;
        this.escapeStrategy = escapeStrategy;
    }

    /**
     * Returns this template's current escape strategy.
     *
     * @return The escape strategy.
     */
    @NotNull
    public Escape getEscapeStrategy() {
        return escapeStrategy;
    }

    /**
     * Set this template's escape strategy.
     * Don't mess with this if you don't know what you're doing!
     *
     * @param escapeStrategy The new escape strategy to use.
     * @return This template instance, for chaining.
     */
    public Template escapeStrategy(@NotNull Escape escapeStrategy) {
        this.escapeStrategy = escapeStrategy;
        return this;
    }

    /**
     * Renders this template to a {@code String} and returns it.
     *
     * @param context The key-value context map to use. This defines variables accessible to the template.
     * @return The template, rendered to a {@code String} with the given context.
     */
    public String renderToString(@NotNull Map<String, Object> context) {
        StringWriter writer = new StringWriter();
        try {
            render(new BufferedWriter(writer), context);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }

    /**
     * Renders this template to the given {@link BufferedWriter}.
     *
     * @param writer  The writer to render to.
     * @param context The key-value context map to use. This defines variables accessible to the template.
     * @throws IOException If an IO error occurs while writing.
     */
    public void render(@NotNull BufferedWriter writer, @NotNull Map<String, Object> context) throws IOException {
        for (Node node : rootNodes) {
            interpreter.execute(this, node, context, writer);
            writer.flush();
        }
    }

    /**
     * Create a new template instance from the given template string.
     *
     * @param text The template string.
     * @return The constructed template.
     */
    public static Template from(String text) {
        try {
            return from(new BufferedReader(new StringReader(text)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a new template instance from the given template file.
     *
     * @param file The template file.
     * @return The constructed template.
     * @throws IOException If an IO error occurs while reading the file.
     */
    public static Template from(File file) throws IOException {
        return from(new BufferedReader(new FileReader(file)));
    }

    /**
     * Create a new template instance from the given template input.
     *
     * @param reader The template input reader.
     * @return The constructed template.
     * @throws IOException If an IO error occurs while reading.
     */
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
}
