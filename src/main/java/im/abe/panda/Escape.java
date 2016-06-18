package im.abe.panda;

/**
 * A function that takes text and returns an escaped form of it.
 */
@FunctionalInterface
public interface Escape {
    /**
     * Returns the input as-is.
     */
    public static final Escape NONE = input -> input;

    /**
     * Escapes dangerous HTML characters with their safe equivalents.
     */
    public static final Escape HTML = input -> input
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");

    /**
     * Escape the given string using this escape strategy.
     *
     * @param input The input text to be escaped.
     * @return The text, escaped as appropriate.
     */
    public String on(String input);
}
