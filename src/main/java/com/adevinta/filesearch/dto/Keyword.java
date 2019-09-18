package com.adevinta.filesearch.dto;

import java.math.BigDecimal;

/**
 * DTO for search keyword.
 */
public class Keyword {
    private String word;
    private BigDecimal emphasisFactor;

    /**
     * Constructor to initialize word.
     *
     * @param word {@link String} The actual word
     */
    public Keyword(final String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public BigDecimal getEmphasisFactor() {
        return emphasisFactor;
    }

    public void setEmphasisFactor(BigDecimal emphasisFactor) {
        this.emphasisFactor = emphasisFactor;
    }
}
