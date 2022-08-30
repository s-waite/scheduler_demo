package dev.sam.scheduler;

import dev.sam.scheduler.dao.UserDAO;
import dev.sam.scheduler.dao.UserDAOImpl;
import dev.sam.scheduler.helper.StageHelper;
import dev.sam.scheduler.model.LocalizationEnum;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

public class App extends Application {
    private final static int APP_WIDTH = 600;
    private final static int APP_HEIGHT = 400;
    private final static int APP_MIN_WIDTH = 450;
    private final static int APP_MIN_HEIGHT = 400;
    private static Stage stage;


    @Override
    public void start(Stage stage) throws IOException, SQLException {
        App.stage = stage;
        LocalizationEnum.INSTANCE.setCurrentLocale(Locale.getDefault());
        StageHelper.loadSceneIntoStage(stage, "login.fxml");
        stage.setWidth(APP_WIDTH);
        stage.setHeight(APP_HEIGHT);
        stage.setMinWidth(APP_MIN_WIDTH);
        stage.setMinHeight(APP_MIN_HEIGHT);
        stage.show();
    }

    public static Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch();
    }
}