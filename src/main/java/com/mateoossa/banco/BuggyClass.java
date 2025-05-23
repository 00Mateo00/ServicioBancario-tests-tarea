package com.mateoossa.banco;

import java.util.ArrayList;
import java.util.List;

public class BuggyClass {

    private List<String> items = new ArrayList<>();

    public BuggyClass() {
        items.add("item1");
    }

    public List<String> getItems() {
        return items;
    }
}
