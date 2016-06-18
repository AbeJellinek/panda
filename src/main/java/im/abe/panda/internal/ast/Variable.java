package im.abe.panda.internal.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A variable access AST node.
 * Contains an optional left side and a right side name. Can be raw or escaped.
 */
public class Variable implements Node {
    /**
     * The left side variable. Can be null.
     */
    @Nullable
    private Variable left;

    /**
     * The name of the variable to be accessed.
     */
    @NotNull
    private String name;

    /**
     * Whether this variable should be rendered raw.
     * If false, it will be escaped before rendering based on template settings.
     */
    private boolean raw;

    /**
     * Construct a new variable AST node.
     *
     * @param left The left side.
     * @param name The variable name.
     * @param raw  Whether it should be rendered raw.
     */
    public Variable(@Nullable Variable left, @NotNull String name, boolean raw) {
        this.left = left;
        this.name = name;
        this.raw = raw;
    }

    @Nullable
    public Variable getLeft() {
        return left;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public boolean isRaw() {
        return raw;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return raw == variable.raw &&
                Objects.equals(left, variable.left) &&
                Objects.equals(name, variable.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, name, raw);
    }

    @Override
    public String toString() {
        return "Value{" +
                "left=" + left +
                ", name='" + name + '\'' +
                ", raw=" + raw +
                '}';
    }
}
