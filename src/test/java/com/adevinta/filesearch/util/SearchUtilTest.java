package com.adevinta.filesearch.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SearchUtilTest {

    private static final String TEST_FILE_FOLDER = "test_files";
    private final ByteArrayOutputStream consoleOut = new ByteArrayOutputStream();
    private final PrintStream originalConsole = System.out;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(consoleOut));
    }

    @Test
    public void getCommand_withUnrecognizedCommand_shouldReturnUnrecognizedCommand() {
        Command command = SearchUtil.getCommand(":exit");
        command.run();

        assertEquals(ConsoleMessage.COMMAND_NOT_RECOGNIZED.getMessage() + "\n", consoleOut.toString());
    }

    @Test
    public void getCommand_withRecognizedCommand_shouldReturnRecognizedCommand() {

        Command command = SearchUtil.getCommand(":list");
        command.run();

        assertEquals("[quit, list]\n", consoleOut.toString());
    }

    @Test
    public void sanitize_withValidStringAndCaseInsensitive_shouldSanitizeString() {

        String sanitized = SearchUtil.sanitize(" I AM'/60-9 PERCENT CHILLED ", false);

        assertEquals("i am60-9 percent chilled", sanitized);
    }

    @Test
    public void sanitize_withValidStringAndCaseSensitive_shouldSanitizeString() {

        String sanitized = SearchUtil.sanitize(" I AM'/60-9 PERCENT CHILLED ", true);

        assertEquals("I AM60-9 PERCENT CHILLED", sanitized);
    }

    @Test
    public void looksLikeCommand_withValidCommand_shouldReturnBoolean() {
        assertTrue(SearchUtil.looksLikeCommand(":list"));
        assertFalse(SearchUtil.looksLikeCommand("list"));
    }

    @Test
    public void isTextFile_withValidFile_shouldReturnBoolean() {
        File textFile = new File("/usr/test/test.txt");
        assertTrue(SearchUtil.isTextFile(textFile));

        File pdfFile = new File("/usr/test/test.pdf");
        assertFalse(SearchUtil.isTextFile(pdfFile));
    }

    @Test
    public void readFileContent_withValidFile_shouldReturnBoolean() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File file = new File(classLoader.getResource(TEST_FILE_FOLDER + "/test20.txt").getFile());
        String content = SearchUtil.readFileContent(file);

        assertEquals("This is a simple test file\n", content);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalConsole);
    }
}
