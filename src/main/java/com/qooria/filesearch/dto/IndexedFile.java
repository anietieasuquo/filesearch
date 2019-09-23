package com.qooria.filesearch.dto;

/**
 * DTO for a indexing files.
 */
public class IndexedFile {
    private String path;
    private String content;

    /**
     * Constructor to initialize fields.
     *
     * @param path    {@link String} The file path
     * @param content {@link String} The file content
     */
    public IndexedFile(final String path, final String content) {
        this.path = path;
        this.content = content;
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
}
