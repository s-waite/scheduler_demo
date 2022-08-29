package dev.sam.scheduler.view;

import javafx.scene.control.Button;

public class FlagButton extends Button {
    boolean buttonIsActive;

    public void check() {
        buttonIsActive = true;
        this.getStyleClass().add("checked");
    }

    public void uncheck() {
        buttonIsActive = false;
        this.getStyleClass().clear();
        this.getStyleClass().add("imageButton");
    }

    public boolean isChecked() {
        return buttonIsActive;
    }
}
