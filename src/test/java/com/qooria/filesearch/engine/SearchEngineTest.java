package com.qooria.filesearch.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


import com.qooria.filesearch.dto.FileResult;
import com.qooria.filesearch.dto.IndexedFile;
import com.qooria.filesearch.common.ConsoleMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SearchEngineTest {
    private static final String TEST_FILE_FOLDER = "test_files";
    private SearchEngine engine;

    @BeforeEach
    public void setUp() {
        SearchOption options = SearchOption
                .builder()
                .withCaseSensitive(false)
                .withMaxResultCount(10)
                .build();

        engine = new SearchEngine(options);
    }

    @Test
    public void indexDirectory_withNullDirectory_shouldThrowIllegalArgumentException() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> engine.indexDirectory(null),
                "Expected IllegalArgumentException but it wasn't thrown");

        Assertions.assertEquals(ConsoleMessage.INVALID_INPUT.getMessage(), exception.getMessage());
    }

    @Test
    public void indexDirectory_withNonExistentDirectory_shouldThrowFileNotFoundException() {
        Throwable exception = assertThrows(FileNotFoundException.class,
                () -> engine.indexDirectory("/adevinta/test"),
                "Expected FileNotFoundException but it wasn't thrown");

        assertEquals(ConsoleMessage.FILE_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    public void indexDirectory_withValidDirectory_shouldReturnListOfIndexedFilesFromDirectoryAndSubdirectory() throws FileNotFoundException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File file = new File(classLoader.getResource(TEST_FILE_FOLDER).getFile());

        List<IndexedFile> indexedFiles = engine.indexDirectory(file.getAbsolutePath());

        assertEquals(30, indexedFiles.size());

        List<String> indexedFileNames = indexedFiles
                .stream()
                .map(f -> f.getPath().substring(f.getPath().lastIndexOf("/") + 1))
                .collect(Collectors.toList());

        List<String> expectedIndexedFileNames = Arrays.asList("test1.txt", "test2.txt",
                "test3.txt", "test4.txt", "test5.txt", "test21.txt",
                "test22.txt", "test23.txt", "test24.txt", "test25.txt");

        assertTrue(indexedFileNames.containsAll(expectedIndexedFileNames));

        IndexedFile firstFile = indexedFiles.stream().filter(f -> f.getPath().endsWith("test1.txt")).findFirst().get();

        assertNotNull(firstFile.getPath());
        assertEquals("this could be done better", firstFile.getContent());
    }

    @Test
    public void getIndexedFiles_withFilesInMemory_shouldReturnListOfIndexedFilesFromDirectoryAndSubdirectory() throws FileNotFoundException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File file = new File(classLoader.getResource(TEST_FILE_FOLDER).getFile());

        List<IndexedFile> indexedFiles = engine.indexDirectory(file.getAbsolutePath());
        List<IndexedFile> gottenFiles = engine.getIndexedFiles();

        assertEquals(indexedFiles.size(), gottenFiles.size());

        List<String> indexedFileNames = gottenFiles
                .stream()
                .map(f -> f.getPath().substring(f.getPath().lastIndexOf("/") + 1))
                .collect(Collectors.toList());

        List<String> expectedIndexedFileNames = Arrays.asList("test1.txt", "test2.txt",
                "test3.txt", "test4.txt", "test5.txt", "test21.txt",
                "test22.txt", "test23.txt", "test24.txt", "test25.txt");

        assertTrue(indexedFileNames.containsAll(expectedIndexedFileNames));
    }

    @Test
    public void getOptions_withInjectedOptions_shouldReturnOption() throws FileNotFoundException {
        SearchOption option = engine.getOptions();

        assertEquals(10, option.getMaxResultCount());
        assertFalse(option.isCaseSensitive());
    }

    @Test
    public void search_withEmptyWord_shouldThrowIllegalArgumentException() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> engine.search(""),
                "Expected IllegalArgumentException but it wasn't thrown");

        assertEquals(ConsoleMessage.EMPTY_SEARCH_TERM.getMessage(), exception.getMessage());
    }

    @Test
    public void search_withValidAndIndexedWord_shouldReturnResultListWithOneResult() throws FileNotFoundException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File file = new File(classLoader.getResource(TEST_FILE_FOLDER).getFile());

        engine.indexDirectory(file.getAbsolutePath());

        List<FileResult> results = engine.search("london");

        assertEquals(1, results.size());

        FileResult result = results.get(0);

        assertEquals(100, result.getScore());
        assertEquals(0, result.getRank().compareTo(BigDecimal.valueOf(100.256)));
    }

    @Test
    public void search_withValidAndIndexedWordAndCaseSensitivity_shouldReturnEmptyResultList() throws FileNotFoundException {
        SearchOption options = SearchOption
                .builder()
                .withCaseSensitive(true)
                .withMaxResultCount(10)
                .build();

        engine = new SearchEngine(options);

        ClassLoader classLoader = this.getClass().getClassLoader();
        File file = new File(classLoader.getResource(TEST_FILE_FOLDER).getFile());

        engine.indexDirectory(file.getAbsolutePath());

        List<FileResult> results = engine.search("LONDON");

        assertTrue(results.isEmpty());
    }

    @Test
    public void search_withValidAndIndexedWords_shouldReturnResultListWithTenResults() throws FileNotFoundException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File file = new File(classLoader.getResource(TEST_FILE_FOLDER).getFile());

        engine.indexDirectory(file.getAbsolutePath());

        List<FileResult> results = engine.search("the brown fox cannot jump over a lazy dog in a place you raise kids in america is a correct statement");

        assertEquals(10, results.size());

        FileResult first = results.get(0);
        assertEquals(75, first.getScore());
        assertTrue(first.getRank().compareTo(new BigDecimal("75.2531441011006728598113433")) == 0);
        assertTrue(first.getPath().endsWith("test24.txt"));

        /*
          Demonstrating the power of the rank. The first and second search results have the same scores,
          but the rank differentiates them due to the frequency of the word "is".
         */
        FileResult second = results.get(1);
        assertEquals(75, second.getScore());
        assertTrue(second.getRank().compareTo(new BigDecimal("75.2531413840309624380581992")) == 0);
        assertTrue(second.getPath().endsWith("test3.txt"));

        FileResult third = results.get(2);
        assertEquals(70, third.getScore());
        assertTrue(third.getRank().compareTo(new BigDecimal("70.2531170840905416395987669")) == 0);
        assertTrue(third.getPath().endsWith("test2.txt"));

        FileResult fourth = results.get(3);
        assertEquals(50, fourth.getScore());
        assertTrue(fourth.getRank().compareTo(new BigDecimal("50.289239562")) == 0);
        assertTrue(fourth.getPath().endsWith("test4.txt"));

        FileResult fifth = results.get(4);
        assertEquals(45, fifth.getScore());
        assertTrue(fifth.getRank().compareTo(new BigDecimal("45.29820495043")) == 0);
        assertTrue(fifth.getPath().endsWith("test5.txt"));

        FileResult sixth = results.get(5);
        assertEquals(35, sixth.getScore());
        assertTrue(sixth.getRank().compareTo(new BigDecimal("35.3080827")) == 0);
        assertTrue(sixth.getPath().endsWith("test7.txt"));

        FileResult seventh = results.get(6);
        assertEquals(35, seventh.getScore());
        assertTrue(seventh.getRank().compareTo(new BigDecimal("35.2905354")) == 0);
        assertTrue(seventh.getPath().endsWith("test6.txt"));

        FileResult eighth = results.get(7);
        assertEquals(35, eighth.getScore());
        assertTrue(eighth.getRank().compareTo(new BigDecimal("35.250119781291600000425152800233614554867")) == 0);
        assertTrue(eighth.getPath().endsWith("test8.txt"));

        FileResult ninth = results.get(8);
        assertEquals(20, ninth.getScore());
        assertTrue(ninth.getRank().compareTo(new BigDecimal("20.2500783012396936601594323")) == 0);
        assertTrue(ninth.getPath().endsWith("test14.txt"));

        FileResult tenth = results.get(9);
        assertEquals(20, tenth.getScore());
        assertTrue(tenth.getRank().compareTo(new BigDecimal("20.2500783012393118099594323")) == 0);
        assertTrue(tenth.getPath().endsWith("test15.txt"));
    }

    @Test
    public void search_withValidButUnindexedWord_shouldReturnEmptyResult() throws FileNotFoundException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File file = new File(classLoader.getResource(TEST_FILE_FOLDER).getFile());

        engine.indexDirectory(file.getAbsolutePath());

        List<FileResult> results = engine.search("CLAP");

        assertEquals(0, results.size());
    }
}
