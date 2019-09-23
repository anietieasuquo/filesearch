package com.qooria.filesearch.engine;

import com.qooria.filesearch.dto.FileResult;
import com.qooria.filesearch.dto.IndexedFile;
import com.qooria.filesearch.dto.Keyword;
import com.qooria.filesearch.common.ConsoleMessage;
import com.qooria.filesearch.common.SearchUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Search engine.
 */
public class SearchEngine {
    private static final String WORD_PATTERN = "^[a-zA-Z0-9]*$";
    private static final String SPACE_PATTERN = "\\s+";
    private static final int WEIGHT_FACTOR = 100;
    private static final int INITIAL_FILE_SCORE = 0;
    private static final BigDecimal INITIAL_FILE_RANK = BigDecimal.valueOf(0.25);
    private static final BigDecimal INITIAL_DAMPING_FACTOR = BigDecimal.valueOf(0.85);
    private static final BigDecimal INITIAL_EMPHASIS_FACTOR = BigDecimal.valueOf(0.1);
    private final List<IndexedFile> files;

    private final SearchOption options;

    /**
     * Construction to initialize search option field.
     *
     * @param options {@link SearchOption} Search options
     */
    public SearchEngine(final SearchOption options) {
        this.options = options;
        files = new ArrayList<>();
    }

    /**
     * Method to index and return directory files and content.
     *
     * @param dir {@link String} The directory
     * @return {@link List} of {@link FileResult} Indexing result
     */
    public List<IndexedFile> indexDirectory(String dir) throws FileNotFoundException {
        if (dir == null || dir.trim().length() == 0) {
            throw new IllegalArgumentException(ConsoleMessage.INVALID_INPUT.getMessage());
        }

        final File dirFile = new File(dir);

        if (!dirFile.exists()) {
            throw new FileNotFoundException(ConsoleMessage.FILE_NOT_FOUND.getMessage());
        }

        Path directory = dirFile.toPath();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path path : stream) {
                File file = path.toFile();

                if (Files.isDirectory(path)) {
                    indexDirectory(path.toString());
                } else if (SearchUtil.isTextFile(file)) {
                    String content = SearchUtil.readFileContent(file);
                    content = options.isCaseSensitive() ? content : SearchUtil.sanitize(content, false);
                    files.add(new IndexedFile(file.getAbsolutePath(), content));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return files;
    }

    /**
     * Get the list of indexed files.
     *
     * @return {@link List} of {@link IndexedFile} Indexed file list
     */
    public List<IndexedFile> getIndexedFiles() {
        return files;
    }

    /**
     * Get the search options.
     *
     * @return {@link SearchOption} The search option
     */
    public SearchOption getOptions() {
        return this.options;
    }

    /**
     * Searches for term in indexed file list.
     *
     * @param term {@link String} The search term
     * @return {@link List} of {@link FileResult} Search result
     */
    public List<FileResult> search(final String term) {
        String searchTerm = SearchUtil.sanitize(term, options.isCaseSensitive());

        if (searchTerm == null || searchTerm.isEmpty()) {
            throw new IllegalArgumentException(ConsoleMessage.EMPTY_SEARCH_TERM.getMessage());
        }

        final List<Keyword> words = Arrays.stream(searchTerm.split(SPACE_PATTERN))
                .filter(this::isWord).distinct().map(Keyword::new).collect(Collectors.toList());

        final int wordWeight = WEIGHT_FACTOR / words.size();

        List<FileResult> files = getIndexedFiles()
                .stream()
                .map(f -> new FileResult(INITIAL_FILE_SCORE, INITIAL_FILE_RANK, f.getPath(), f.getContent()))
                .collect(Collectors.toList());

        files.forEach(file -> words.forEach(word -> searchInFile(words, word, file, wordWeight)));

        files.sort(Comparator.comparing(FileResult::getRank).reversed());

        return files
                .stream()
                .filter(f -> f.getScore() > 0)
                .limit(options.getMaxResultCount())
                .collect(Collectors.toList());
    }

    private int getFrequency(String word, String content) {
        int index = content.indexOf(word);
        int count = 0;
        while (index != -1) {
            count++;
            content = content.substring(index + 1);
            index = content.indexOf(word);
        }
        return count;
    }

    private void searchInFile(List<Keyword> words, Keyword word, FileResult file, int weight) {
        Keyword prev = null;
        int currentIndex = 0;

        for (int i = 0; i < words.size(); i++) {
            currentIndex = i;
            Keyword current = words.get(currentIndex);

            if (current.getWord().equalsIgnoreCase(word.getWord())) {
                prev = words.get(currentIndex == 0 ? currentIndex : currentIndex - 1);
                break;
            }
        }

        String content = file.getContent();

        if (content.contains(word.getWord())) {
            int frequency = getFrequency(word.getWord(), content);
            BigDecimal dampingFactor = INITIAL_DAMPING_FACTOR.divide(BigDecimal.valueOf(files.size()), RoundingMode.UP);
            BigDecimal emphasis = (prev.getEmphasisFactor() == null ? INITIAL_EMPHASIS_FACTOR : prev.getEmphasisFactor()).multiply(dampingFactor);
            BigDecimal rank = emphasis.multiply(BigDecimal.valueOf(frequency));
            file.addScore(weight);
            file.upRank(rank);
            file.upRank(BigDecimal.valueOf(weight));

            words.get(currentIndex).setEmphasisFactor(emphasis);
        }
    }

    private boolean isWord(String line) {
        Pattern pattern = Pattern.compile(WORD_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        return matcher.matches();
    }
}
