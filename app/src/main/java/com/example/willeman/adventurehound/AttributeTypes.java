package com.example.willeman.adventurehound;

/**
 * Created by WILLEMAN on 8/19/2016.
 */
public enum AttributeTypes {

    category ("category"),
    rating ("rating"),
    date("date"),
    time_length("time_length"),
    price("price");

    private final String name;

    private AttributeTypes(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equalsIgnoreCase(otherName);
    }

    public String toString() {
        return this.name;
    }
}
