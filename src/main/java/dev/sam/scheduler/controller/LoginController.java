package dev.sam.scheduler.controller;

import dev.sam.scheduler.App;
import dev.sam.scheduler.helper.stageHelper;
import dev.sam.scheduler.model.LocalizationEnum;
import dev.sam.scheduler.view.FlagButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private FlagButton usFlagButton;
    @FXML
    private FlagButton frenchFlagButton;
    private Stage stage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
        initializeClickListeners();
    }

    /**
     * Initialize the click listeners for the scene.
     */
    private void initializeClickListeners() {
        usFlagButton.setOnAction(actionEvent -> {
            // Reload the scene with a new locale if it is not already the active locale
            if (!usFlagButton.isChecked()) {
                LocalizationEnum.INSTANCE.setCurrentLocale(Locale.ENGLISH);
                stage = (Stage) usFlagButton.getScene().getWindow();
                try {
                    stageHelper.loadSceneIntoStage(stage, "login.fxml");
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Same actions as for the US flag button
        frenchFlagButton.setOnAction(actionEvent -> {
            if (!frenchFlagButton.isChecked()) {
                LocalizationEnum.INSTANCE.setCurrentLocale(Locale.FRENCH);
                stage = (Stage) frenchFlagButton.getScene().getWindow();
                try {
                    stageHelper.loadSceneIntoStage(stage, "login.fxml");
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        });
    }


    /**
     * Initialize the nodes for the scene.
     * <p>
     * This includes nodes that cannot be easily defined in FXML and inserting dynamic data into the nodes.
     */
    private void initializeNodes() {
        final int FLAG_HEIGHT = 20;

        // Create two flag buttons for setting the language of the login screen
        Image americanFlag = new Image(String.valueOf(App.class.getResource("usFlag.svg.png")));
        ImageView americanFlagView = new ImageView(americanFlag);
        americanFlagView.setFitHeight(FLAG_HEIGHT);
        americanFlagView.setPreserveRatio(true);

        Image frenchFlag = new Image(String.valueOf(App.class.getResource("frenchFlag.png")));
        ImageView frenchFlagView = new ImageView(frenchFlag);
        frenchFlagView.setFitHeight(FLAG_HEIGHT);
        frenchFlagView.setPreserveRatio(true);

        usFlagButton.setGraphic(americanFlagView);
        frenchFlagButton.setGraphic(frenchFlagView);

        // Highlights ("checks") a flag button based on the current locale of the app
        if (LocalizationEnum.INSTANCE.getCurrentLocale() == Locale.ENGLISH) {
            usFlagButton.check();
        } else {
            frenchFlagButton.check();
        }

    }
}