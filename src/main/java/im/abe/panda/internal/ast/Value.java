package im.abe.panda.internal.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.IOException;

public class Value implements Node {
    @Nullable
    private Value left;
    @NotNull
    private String name;

    public Value(@Nullable Value left, @NotNull String name) {
        this.left = left;
        this.name = name;
    }

    @Override
    public void writeTo(@NotNull BufferedWriter writer) throws IOException {
        // TODO
    }

    @Nullable
    public Value getLeft() {
        return left;
    }

    public void setLeft(@Nullable Value left) {
        this.left = left;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Value value = (Value) o;

        return left != null ? left.equals(value.left) : value.left == null && name.equals(value.name);
    }

    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Value{" +
                "left=" + left +
                ", name='" + name + '\'' +
                '}';
    }
}
