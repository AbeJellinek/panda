package im.abe.panda.internal.ast;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;

public interface Node {
    public void writeTo(@NotNull BufferedWriter writer) throws IOException;
}
