package com.example.willeman.adventurehound;

/**
 * Created by WILLEMAN on 8/19/2016.
 */
public enum AttributeTypesExtra {

    romantic ("romantic"),
    llama ("llama"),
    Hot ("hot"),
    day("day"),
    wet ("rain"),
    rain ("rain"),
    sunny ("sunny"),
    creative ("creative"),
    culture ("culture"),
    community ("community"),
    faith ("faith"),
    none("");

    private final String name;

    private AttributeTypesExtra(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }

}

