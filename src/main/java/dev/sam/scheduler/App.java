package dev.sam.scheduler;

import dev.sam.scheduler.model.LocalizationEnum;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("dev.sam.scheduler.strings", LocalizationEnum.INSTANCE.getCurrentLocale());
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login.fxml"), bundle);
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}