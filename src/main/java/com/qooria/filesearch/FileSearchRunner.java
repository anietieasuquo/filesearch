package com.qooria.filesearch;

import com.qooria.filesearch.engine.SearchEngine;
import com.qooria.filesearch.engine.SearchOption;
import com.qooria.filesearch.common.ConsoleMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * The main application runner.
 */
public class FileSearchRunner {
    private static final int MAXIMUM_RESULT_COUNT = 10;
    private static final String CASE_SENSITIVITY_FLAG = "-s";

    /**
     * Main method to run the application.
     *
     * @param args Array of {@link String} arguments
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            throw new IllegalArgumentException(ConsoleMessage.NO_DIRECTORY_PROVIDED.getMessage());
        }

        boolean isCaseSensitive = args.length == 2 && args[1].equalsIgnoreCase(CASE_SENSITIVITY_FLAG);
        SearchOption options = SearchOption
                .builder()
                .withCaseSensitive(isCaseSensitive)
                .withMaxResultCount(MAXIMUM_RESULT_COUNT) //Would be passed from terminal
                .build();
        SearchEngine engine = new SearchEngine(options);

        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
        PrintStream outputStream = System.out;

        FileSearchApplication application = new FileSearchApplication(args[0], engine, inputReader, outputStream);
        application.start();
    }
}
