package im.abe.panda.internal.ast;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;

public class Text implements Node {
    @NotNull
    private String value;

    public Text(String value) {
        this.value = value;
    }

    @Override
    public void writeTo(@NotNull BufferedWriter writer) throws IOException {
        writer.write(value);
    }

    @NotNull
    public String getValue() {
        return value;
    }

    public void setValue(@NotNull String value) {
        this.value = value;
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
