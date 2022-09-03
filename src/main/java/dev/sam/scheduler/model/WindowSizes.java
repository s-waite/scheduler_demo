package dev.sam.scheduler.model;

public enum WindowSizes {
    WIDTH_LARGE(1000),
    WIDTH_MED(800),
    WIDTH_SMALL(600),
    HEIGHT_LARGE(800),
    HEIGHT_MED(600),
    HEIGHT_SMALL(400);

    private final int size;
    WindowSizes(int size) {
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }
}
