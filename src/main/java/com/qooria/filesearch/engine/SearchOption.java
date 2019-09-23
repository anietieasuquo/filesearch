package com.qooria.filesearch.engine;

/**
 * DTO to capture search options.
 */
public final class SearchOption {
    private boolean caseSensitive;
    private int maxResultCount;

    /**
     * Constructor to initialize option with builder.
     *
     * @param builder {@link Builder} option builder
     */
    public SearchOption(final Builder builder) {
        this.caseSensitive = builder.caseSensitive;
        this.maxResultCount = builder.maxResultCount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public int getMaxResultCount() {
        return maxResultCount;
    }

    /**
     * Builder class to build optional fields
     */
    public static final class Builder {
        private boolean caseSensitive;
        private int maxResultCount;

        private Builder() {
        }

        public Builder withCaseSensitive(boolean caseSensitive) {
            this.caseSensitive = caseSensitive;
            return this;
        }

        public Builder withMaxResultCount(int maxResultCount) {
            this.maxResultCount = maxResultCount;
            return this;
        }

        public SearchOption build() {
            return new SearchOption(this);
        }
    }
}
