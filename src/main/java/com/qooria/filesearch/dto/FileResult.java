package com.qooria.filesearch.dto;

import java.math.BigDecimal;

/**
 * DTO for a file search result.
 */
public class FileResult {
    private int score;
    private BigDecimal rank;
    private String path;
    private String content;

    /**
     * Constructor to initialize fields.
     *
     * @param score   The file score
     * @param rank    {@link BigDecimal} The file rank
     * @param path    {@link String} The file path
     * @param content {@link String} The file content
     */
    public FileResult(final int score, final BigDecimal rank, final String path, final String content) {
        this.score = score;
        this.rank = rank;
        this.path = path;
        this.content = content;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public BigDecimal getRank() {
        return rank;
    }

    public void setRank(BigDecimal rank) {
        this.rank = rank;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void upRank(BigDecimal rank) {
        this.rank = this.rank.add(rank);
    }
}
