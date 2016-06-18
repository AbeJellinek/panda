package im.abe.panda.internal.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Value implements Node {
    @Nullable
    private Value left;
    @NotNull
    private String name;
    private boolean raw;

    public Value(@Nullable Value left, @NotNull String name, boolean raw) {
        this.left = left;
        this.name = name;
        this.raw = raw;
    }

    @Nullable
    public Value getLeft() {
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
        Value value = (Value) o;
        return raw == value.raw &&
                Objects.equals(left, value.left) &&
                Objects.equals(name, value.name);
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
