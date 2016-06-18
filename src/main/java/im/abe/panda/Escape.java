package im.abe.panda;

/**
 * Enum representing possible escape strategies for text.
 */
public enum Escape {
    NONE {
        @Override
        public String on(String input) {
            return input;
        }
    }, HTML {
        @Override
        public String on(String input) {
            return input
                    .replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
        }
    };

    /**
     * Escape the given string using this escape strategy.
     * {@link #HTML} will escape dangerous HTML characters. {@link #NONE} won't escape anything.
     *
     * @param input The input text to be escaped.
     * @return The text, escaped as appropriate.
     */
    public abstract String on(String input);
}
