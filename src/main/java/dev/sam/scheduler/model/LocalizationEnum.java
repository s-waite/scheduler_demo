package dev.sam.scheduler.model;

import java.util.ArrayList;
import java.util.Locale;

public enum LocalizationEnum {
    INSTANCE;
    private Locale currentLocale;
    private LocalizationEnum() {
        currentLocale = Locale.ENGLISH;
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }

    public void setCurrentLocale(Locale locale) {
        currentLocale = locale;
    }

}
