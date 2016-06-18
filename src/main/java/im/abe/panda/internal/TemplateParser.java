package im.abe.panda.internal;

import im.abe.panda.internal.ast.Node;
import im.abe.panda.internal.ast.Text;
import im.abe.panda.internal.ast.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * The main template parser class. Receives a stream of characters as input and produces an AST.
 */
public class TemplateParser {
    @NotNull
    private BufferedReader reader;

    public TemplateParser(@NotNull BufferedReader reader) {
        this.reader = reader;
    }

    @Nullable
    public Node next() throws IOException {
        int code = reader.read();
        if (code == -1) {
            return null;
        } else {
            if (code == '[') {
                return parseValue();
            } else {
                return parseText(code);
            }
        }
    }

    @NotNull
    private Text parseText(int startCode) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.appendCodePoint(startCode);

        while (true) {
            reader.mark(1);
            int code = reader.read();
            if (code == -1)
                break;

            if (code == '[') {
                reader.reset();
                break;
            } else {
                builder.appendCodePoint(code);
            }
        }

        return new Text(builder.toString());
    }

    @Nullable
    private Value parseValue() throws IOException {
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
                break;
            } else {
                builder.appendCodePoint(code);
            }
        }

        return splitValue(builder.toString(), raw);
    }

    @Nullable
    private Value splitValue(String path, boolean raw) {
        if (path.indexOf('.') != -1) {
            String[] parts = path.split("\\.");
            Value value = null;
            for (String part : parts) {
                value = new Value(value, part, raw);
            }

            return value;
        } else {
            return new Value(null, path, raw);
        }
    }
}
