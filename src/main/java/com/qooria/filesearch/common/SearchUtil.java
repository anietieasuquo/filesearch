package com.qooria.filesearch.common;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Utility class to facilitate search operations.
 */
public class SearchUtil {
    private static final Map<ConsoleCommand, Command> COMMANDS = new HashMap<>();
    private static final String COMMAND_PREFIX = ":";
    private static final String SANITATION_PATTERN = "[^-:,. a-zA-Z0-9]";
    private static final String TEXT_FILE_EXTENSION = ".txt";

    static {
        loadCommands();
    }

    /**
     * Get console command.
     *
     * @param entry {@link String} Command entry
     * @return {@link Command} Command
     */
    public static Command getCommand(final String entry) {
        if (!isCommand(entry)) {
            return COMMANDS.get(ConsoleCommand.DISPLAY_UNKNOWN_COMMAND_MESSAGE);
        }

        String command = sanitize(entry.substring(getCommandIndex(entry)), false);

        return COMMANDS.get(ConsoleCommand.valueOf(command.toUpperCase()));
    }

    /**
     * Sanitizes and transforms a string depending on case sensitivity.
     *
     * @param string          {@link String} The String to sanitize
     * @param isCaseSensitive Case sensitivity flag
     * @return {@link String} The transformed search term
     */
    public static String sanitize(final String string, final boolean isCaseSensitive) {
        String cleanTerm = string.replaceAll(SANITATION_PATTERN, "");
        cleanTerm = cleanTerm.trim();
        cleanTerm = isCaseSensitive ? cleanTerm : cleanTerm.toLowerCase();
        return cleanTerm;
    }

    /**
     * Checks if a console entry looks like a command.
     *
     * @param entry {@link String} The console entry
     * @return {@code true} if the entry looks like a command, otherwise {@code false}
     */
    public static boolean looksLikeCommand(final String entry) {
        return entry.startsWith(COMMAND_PREFIX);
    }

    /**
     * Method to check if a file is a text file.
     *
     * @param file {@link File} The file to check
     * @return {@code true} if the file is a text file, otherwise {@code false}
     */
    public static boolean isTextFile(final File file) {
        String name = file.getName();
        int lastIndex = name.lastIndexOf('.');
        if (lastIndex > 0) {
            return name.substring(lastIndex).equalsIgnoreCase(TEXT_FILE_EXTENSION);
        }

        return false;
    }

    /**
     * Method to read file content.
     *
     * @param file {@link File} The file to read content
     * @return {@link String} The content of the file as string
     */
    public static String readFileContent(final File file) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }

    private static boolean isCommand(final String line) {
        String command = sanitize(line.substring(getCommandIndex(line)), false);
        try {
            return looksLikeCommand(line) && COMMANDS.containsKey(ConsoleCommand.valueOf(command.toUpperCase()));
        } catch (Exception ex) {
            return false;
        }
    }

    private static int getCommandIndex(final String command) {
        return command.trim().length() > 1 ? 1 : 0;
    }

    private static void loadCommands() {
        COMMANDS.put(ConsoleCommand.QUIT, () -> System.exit(0));
        COMMANDS.put(ConsoleCommand.DISPLAY_UNKNOWN_COMMAND_MESSAGE, () -> System.out.println(ConsoleMessage.COMMAND_NOT_RECOGNIZED.getMessage()));

        String commands = Arrays.toString(Arrays
                .stream(ConsoleCommand.values())
                .filter(c -> !c.getCommand().equalsIgnoreCase(ConsoleCommand.DISPLAY_UNKNOWN_COMMAND_MESSAGE.getCommand()))
                .map(c -> c.getCommand().toLowerCase())
                .toArray());
        COMMANDS.put(ConsoleCommand.LIST, () -> System.out.println(commands));
    }

    private enum ConsoleCommand {
        QUIT("quit"),
        LIST("list"),
        DISPLAY_UNKNOWN_COMMAND_MESSAGE("");

        private String command;

        ConsoleCommand(final String command) {
            this.command = command;
        }

        public String getCommand() {
            return command;
        }
    }
}
