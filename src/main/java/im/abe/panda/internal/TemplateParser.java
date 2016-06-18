package im.abe.panda.internal;

import im.abe.panda.Escape;
import im.abe.panda.internal.ast.Node;
import im.abe.panda.internal.ast.Text;
import im.abe.panda.internal.ast.Variable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * The main template parser class. Receives a stream of characters as input and produces an AST.
 */
public class TemplateParser {
    /**
     * The reader used by this parser. Must be open.
     */
    @NotNull
    private BufferedReader reader;

    /**
     * Construct a new parser.
     *
     * @param reader The reader to read characters from. Must be open.
     */
    public TemplateParser(@NotNull BufferedReader reader) {
        this.reader = reader;
    }

    /**
     * Reads and returns a node from the reader. Returns null if end of stream is reached.
     *
     * @return The node read, or null.
     * @throws IOException If an IO error occurs while reading.
     */
    @Nullable
    public Node next() throws IOException {
        int code = reader.read();
        if (code == -1) {
            return null;
        } else {
            if (code == '[') {
                reader.mark(1);
                if (reader.read() == '[')
                    return parseValue();
                reader.reset();
                return parseText(code);
            } else {
                return parseText(code);
            }
        }
    }

    /**
     * Parse a text (static) node.
     *
     * @param startCode The first (already read) code point in the text.
     * @return The text node.
     * @throws IOException If an IO error occurs while reading.
     */
    @NotNull
    private Text parseText(int startCode) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.appendCodePoint(startCode);

        while (true) {
            reader.mark(2);
            int code = reader.read();
            if (code == -1)
                break;

            if (code == '[') {
                if (reader.read() == '[') {
                    reader.reset();
                    break;
                } else {
                    reader.reset();
                    builder.appendCodePoint(reader.read());
                }
            } else {
                builder.appendCodePoint(code);
            }
        }

        return new Text(builder.toString());
    }

    /**
     * Parse a value (variable) node.
     *
     * @return The variable node.
     * @throws IOException If an IO error occurs while reading.
     */
    @Nullable
    private Variable parseValue() throws IOException {
        StringBuilder builder = new StringBuilder();
        reader.mark(1);
        boolean raw = reader.read() == '^';
        if (!raw)
            reader.reset();

        while (true) {
            reader.mark(1);
            int code = reader.read();
            if (code == -1)
                break;

            if (code == ']') {
                reader.mark(1);
                if (reader.read() == ']')
                    break;
                reader.reset();
            } else {
                builder.appendCodePoint(code);
            }
        }

        return splitValue(builder.toString(), raw);
    }

    /**
     * Splits a path into its parts and returns a single nested value node.
     *
     * @param path The path string. May be one part, or multiple separated by a dot.
     * @param raw  Whether to leave this value node unescaped when rendering (see {@link im.abe.panda.Template#escapeStrategy(Escape)}).
     * @return The produced value node.
     */
    @Nullable
    private Variable splitValue(String path, boolean raw) {
        if (path.indexOf('.') != -1) {
            String[] parts = path.split("\\.");
            Variable variable = null;
            for (String part : parts) {
                variable = new Variable(variable, part, raw);
            }

            return variable;
        } else {
            return new Variable(null, path, raw);
        }
    }
}
