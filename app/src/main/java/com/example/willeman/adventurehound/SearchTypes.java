package com.example.willeman.adventurehound;

/**
 * Created by WILLEMAN on 8/19/2016.
 */
public enum SearchTypes {

    FullList ("FullList"),
    FilteredList ("FilteredList"),
    SearchedList("SearchedList"),
    EmptyList ("EmptyList");

    private final String name;

    private SearchTypes(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
