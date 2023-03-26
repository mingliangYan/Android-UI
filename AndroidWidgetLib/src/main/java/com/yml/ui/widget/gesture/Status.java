package com.yml.ui.widget.gesture;

public enum Status {

    NORMAL(0), SELECTED(1), ERROR(-1);

    private int val;

    private Status(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
