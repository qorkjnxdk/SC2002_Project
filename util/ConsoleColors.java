package util;
/**
 * Utility class that provides ANSI escape codes for printing colored
 * and styled text in the console.
 *
 * <p>These constants can be used to format output for better readability,
 * such as highlighting errors, success messages, or important notices
 * in the NTU Internship System’s command-line interface.</p>
 *
 * <p>Note: ANSI escape codes may not be supported on all terminals,
 * particularly on older Windows consoles unless ANSI support is enabled.</p>
 */
public class ConsoleColors {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ITALICS = "\033[3m";
}

