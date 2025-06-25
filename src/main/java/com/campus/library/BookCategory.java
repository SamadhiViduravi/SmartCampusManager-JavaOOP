package com.campus.library;

/**
 * Enumeration for book categories
 */
public enum BookCategory {
    FICTION("Fiction"),
    NON_FICTION("Non-Fiction"),
    SCIENCE("Science"),
    TECHNOLOGY("Technology"),
    MATHEMATICS("Mathematics"),
    HISTORY("History"),
    BIOGRAPHY("Biography"),
    PHILOSOPHY("Philosophy"),
    PSYCHOLOGY("Psychology"),
    BUSINESS("Business"),
    ECONOMICS("Economics"),
    POLITICS("Politics"),
    SOCIOLOGY("Sociology"),
    LITERATURE("Literature"),
    POETRY("Poetry"),
    DRAMA("Drama"),
    ART("Art"),
    MUSIC("Music"),
    SPORTS("Sports"),
    HEALTH("Health"),
    COOKING("Cooking"),
    TRAVEL("Travel"),
    RELIGION("Religion"),
    SELF_HELP("Self Help"),
    CHILDREN("Children"),
    YOUNG_ADULT("Young Adult"),
    REFERENCE("Reference"),
    TEXTBOOK("Textbook"),
    JOURNAL("Journal"),
    MAGAZINE("Magazine");

    private final String displayName;

    BookCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }

    @Override
    public String toString() { return displayName; }
}