package com.adevinta.filesearch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import com.adevinta.filesearch.dto.FileResult;
import com.adevinta.filesearch.engine.SearchEngine;
import com.adevinta.filesearch.engine.SearchOption;
import com.adevinta.filesearch.util.ConsoleMessage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FileSearchApplicationTest {

    private static final String TEST_FILE_FOLDER = "test_files";
    private static final String INPUT_FILE = "input_files/input.txt";
    private static final String EMPTY_INPUT_FILE = "input_files/empty.txt";
    private final PrintStream originalConsoleOut = System.out;
    private final InputStream originalConsoleIn = System.in;
    private final ByteArrayOutputStream consoleOut = new ByteArrayOutputStream();
    private ClassLoader classLoader = this.getClass().getClassLoader();
    private SearchEngine engine;
    private FileSearchApplication application;
    private FileReader fileReader;
    private BufferedReader bufferedReader;
    private InputStream in = new ByteArrayInputStream("".getBytes());
    private File testDirectory;

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        File inputFile = new File(classLoader.getResource(INPUT_FILE).getFile());

        fileReader = new FileReader(inputFile.getAbsolutePath());

        bufferedReader = new BufferedReader(fileReader);

        System.setOut(new PrintStream(consoleOut));
        System.setIn(in);

        SearchOption options = SearchOption
                .builder()
                .withCaseSensitive(false)
                .withMaxResultCount(10)
                .build();

        engine = mock(SearchEngine.class);
        when(engine.getOptions()).thenReturn(options);

        testDirectory = new File(classLoader.getResource(TEST_FILE_FOLDER).getFile());

        application = new FileSearchApplication(testDirectory.getAbsolutePath(), engine, bufferedReader, new PrintStream(consoleOut));
    }

    @Test
    public void start_withNoIndexedFiles_shouldPrintFileNotFoundMessageToConsole() throws IOException {
        List<FileResult> directoryIndexList = new ArrayList<>();

        when(engine.indexDirectory(anyString())).thenReturn(directoryIndexList);
        when(engine.getIndexedFiles()).thenReturn(directoryIndexList);

        application.start();

        assertEquals(ConsoleMessage.FILE_NOT_FOUND.getMessage() + "\n", consoleOut.toString());
    }

    @Test
    public void start_withIndexedFilesAndNoConsoleEntry_shouldPrintIndexResultAndConsolePrefix() throws IOException {
        File inputFile = new File(classLoader.getResource(EMPTY_INPUT_FILE).getFile());

        FileReader input = new FileReader(inputFile.getAbsolutePath());

        BufferedReader br = new BufferedReader(input);

        FileSearchApplication app = new FileSearchApplication(testDirectory.getAbsolutePath(), engine, br, new PrintStream(consoleOut));

        List<FileResult> directoryIndexList = TestUtil.getTestIndexedFiles();

        when(engine.indexDirectory(anyString())).thenReturn(directoryIndexList);
        when(engine.getIndexedFiles()).thenReturn(directoryIndexList);

        app.start();

        String output = String.format("%d files read in directory %s%n%s",
                directoryIndexList.size(), testDirectory, ConsoleMessage.CONSOLE_INPUT_PREFIX.getMessage());

        assertEquals(output, consoleOut.toString());
    }

    @Test
    public void start_withIndexedFilesAndEmptySearchResult_shouldPrintNoResultFoundMessageToConsole() throws IOException {
        File inputFile = new File(classLoader.getResource(INPUT_FILE).getFile());

        FileReader input = new FileReader(inputFile.getAbsolutePath());

        BufferedReader br = new BufferedReader(input);

        FileSearchApplication app = new FileSearchApplication(testDirectory.getAbsolutePath(), engine, br, new PrintStream(consoleOut));

        List<FileResult> directoryIndexList = TestUtil.getTestIndexedFiles();

        when(engine.indexDirectory(anyString())).thenReturn(directoryIndexList);
        when(engine.getIndexedFiles()).thenReturn(directoryIndexList);
        when(engine.search(anyString())).thenReturn(new ArrayList<>());

        app.start();

        String output = String.format("%d files read in directory %s%n%sno matches found%n%s",
                directoryIndexList.size(), testDirectory, ConsoleMessage.CONSOLE_INPUT_PREFIX.getMessage(),
                ConsoleMessage.CONSOLE_INPUT_PREFIX.getMessage());

        assertEquals(output, consoleOut.toString());
    }

    @Test
    public void start_withIndexedFilesAndValidSearchResults_shouldPrintSearchResultsToConsole() throws IOException {
        List<FileResult> directoryIndexList = TestUtil.getTestIndexedFiles();
        List<FileResult> testResultList = TestUtil.getTestResultList();

        when(engine.indexDirectory(anyString())).thenReturn(directoryIndexList);
        when(engine.getIndexedFiles()).thenReturn(directoryIndexList);
        when(engine.search(anyString())).thenReturn(testResultList);

        application.start();

        String output = "11 files read in directory " + testDirectory.getAbsolutePath() + "\n" +
                "search> /usr/path/test/test1.txt:90%\n" +
                "/usr/path/test/test2.txt:80%\n" +
                "/usr/path/test/test3.txt:70%\n" +
                "/usr/path/test/test4.txt:60%\n" +
                "/usr/path/test/test5.txt:50%\n" +
                "/usr/path/test/test6.txt:40%\n" +
                "/usr/path/test/test7.txt:30%\n" +
                "/usr/path/test/test8.txt:20%\n" +
                "/usr/path/test/test9.txt:10%\n" +
                "/usr/path/test/test10.txt:5%\n" +
                "/usr/path/test/test11.txt:5%\n" +
                "search> ";

        assertEquals(output, consoleOut.toString());
    }

    @AfterEach
    public void tearDown() {
        System.setIn(originalConsoleIn);
        System.setOut(originalConsoleOut);
    }
}
