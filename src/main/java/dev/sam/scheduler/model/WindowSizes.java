package dev.sam.scheduler.model;

/**
 * Window sizes for the application to use
 */
public enum WindowSizes {
    WIDTH_LARGE(1200),
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
