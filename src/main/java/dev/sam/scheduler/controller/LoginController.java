package dev.sam.scheduler.controller;

import dev.sam.scheduler.App;
import dev.sam.scheduler.model.LocalizationEnum;
import dev.sam.scheduler.view.FlagButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;
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

    private void initializeClickListeners() {
        usFlagButton.setOnAction(actionEvent -> {
            if (!usFlagButton.isChecked()) {
                usFlagButton.check();
                frenchFlagButton.uncheck();
            }
//            stage = getCurrentStage(usFlagButton);
//            LocalizationEnum.INSTANCE.setCurrentLocale(Locale.FRENCH);
//            ResourceBundle bundle = ResourceBundle.getBundle("dev.sam.scheduler.strings", LocalizationEnum.INSTANCE.getCurrentLocale());
//            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login.fxml"), bundle);
//            try {
//                Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//                stage.setScene(scene);
//            } catch (IOException e) {
//               System.err.println(e.getMessage());
//            }
        });

        frenchFlagButton.setOnAction(actionEvent -> {
            if (!frenchFlagButton.isChecked()) {
                frenchFlagButton.check();
                usFlagButton.uncheck();
            }
        });
    }

private Stage getCurrentStage(Node node) {
        return (Stage) node.getScene().getWindow();
}

    private void initializeNodes() {
        final int FLAG_HEIGHT = 20;

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
        usFlagButton.check();

    }
}