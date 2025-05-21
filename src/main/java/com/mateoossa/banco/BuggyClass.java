package com.mateoossa.banco;

import java.util.ArrayList;
import java.util.List;

public class BuggyClass {

    private List<String> items = new ArrayList<>();

    public BuggyClass() {
        items.add("item1");
    }

    // This method will trigger SpotBugs EI_EXPOSE_REP
    public List<String> getItems() {
        return items; // Directly returning the internal mutable list
    }
}