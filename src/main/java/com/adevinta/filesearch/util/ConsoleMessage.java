package com.adevinta.filesearch.util;

/**
 * Console messages.
 */
public enum ConsoleMessage {

    EMPTY_SEARCH_TERM("No search term entered"),
    NO_DIRECTORY_PROVIDED("No directory given to index."),
    CONSOLE_INPUT_PREFIX("search> "),
    RESULT_COUNT_DESCRIPTION("files read in directory"),
    INVALID_INPUT("Invalid input"),
    COMMAND_NOT_RECOGNIZED("Command not recognized"),
    FILE_NOT_FOUND("Directory or file not found"),
    NO_MATCHES_FOUND("no matches found");

    private String message;

    /**
     * Constructor to initialize message field.
     *
     * @param message {@link String} The message
     */
    ConsoleMessage(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
