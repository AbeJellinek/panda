package im.abe.panda.internal.ast;

import org.jetbrains.annotations.NotNull;

/**
 * A purely text, static AST node.
 */
public class Text implements Node {
    /**
     * The node's value.
     */
    @NotNull
    private String value;

    /**
     * Construct a new text node with the given value.
     *
     * @param value The value.
     */
    public Text(@NotNull String value) {
        this.value = value;
    }

    @NotNull
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Text text = (Text) o;

        return value.equals(text.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "Text{" +
                "value='" + value + '\'' +
                '}';
    }
}
