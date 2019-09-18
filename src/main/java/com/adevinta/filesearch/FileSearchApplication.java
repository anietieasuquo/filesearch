package com.adevinta.filesearch;

import com.adevinta.filesearch.dto.FileResult;
import com.adevinta.filesearch.engine.SearchEngine;
import com.adevinta.filesearch.util.Command;
import com.adevinta.filesearch.util.ConsoleMessage;
import com.adevinta.filesearch.util.SearchUtil;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

/**
 * The main application.
 */
public class FileSearchApplication {
    private static final String RESULT_FORMAT = "%s:%d%%%n";
    private static final String RESULT_COUNT_FORMAT = "%d %s %s%n";
    private final String directory;
    private final SearchEngine engine;
    private final BufferedReader inputReader;
    private final PrintStream outputStream;

    /**
     * Constructor to initialize fields.
     *
     * @param directory    {@link String} The directory to index
     * @param engine       {@link SearchEngine} The search engine
     * @param inputReader  {@link BufferedReader} The input reader
     * @param outputStream {@link PrintStream} The output stream
     */
    public FileSearchApplication(final String directory, final SearchEngine engine, final BufferedReader inputReader, final PrintStream outputStream) throws FileNotFoundException {
        this.directory = directory;
        this.engine = engine;
        this.inputReader = inputReader;
        this.outputStream = outputStream;
        engine.indexDirectory(directory);
    }

    /**
     * Method to start the application.
     */
    public void start() throws IOException {
        List<FileResult> files = engine.getIndexedFiles();

        if (files.size() == 0) {
            outputStream.println(ConsoleMessage.FILE_NOT_FOUND.getMessage());
            return;
        }

        outputStream.printf(RESULT_COUNT_FORMAT, files.size(), ConsoleMessage.RESULT_COUNT_DESCRIPTION.getMessage(), directory);

        try (BufferedReader keyboard = inputReader) {

            while (true) {
                outputStream.print(ConsoleMessage.CONSOLE_INPUT_PREFIX.getMessage());

                final String line = keyboard.readLine();

                if (line == null || line.trim().length() == 0) {
                    return;
                }

                final String cleanLine = SearchUtil.sanitize(line, engine.getOptions().isCaseSensitive());

                if (SearchUtil.looksLikeCommand(cleanLine)) {
                    Command command = SearchUtil.getCommand(cleanLine);
                    command.run();
                } else {

                    List<FileResult> results = engine.search(cleanLine);
                    printResult(results);
                }
            }
        }
    }

    private void printResult(List<FileResult> results) {
        if (results.isEmpty()) {
            outputStream.println(ConsoleMessage.NO_MATCHES_FOUND.getMessage());
            return;
        }

        results.forEach(r -> outputStream.printf(RESULT_FORMAT, r.getPath(), r.getScore()));
    }
}
