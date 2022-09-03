package dev.sam.scheduler;

import dev.sam.scheduler.dao.UserDAO;
import dev.sam.scheduler.dao.UserDAOImpl;
import dev.sam.scheduler.helper.StageHelper;
import dev.sam.scheduler.model.LocalizationEnum;
import dev.sam.scheduler.model.WindowSizes;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

public class App extends Application {
    private static Stage stage;


    @Override
    public void start(Stage stage) throws IOException, SQLException {
        App.stage = stage;
        LocalizationEnum.INSTANCE.setCurrentLocale(Locale.getDefault());
        StageHelper.loadSceneIntoStage(stage, "login.fxml");
        stage.setWidth(WindowSizes.WIDTH_SMALL.getSize());
        stage.setHeight(WindowSizes.HEIGHT_SMALL.getSize());
        stage.show();
    }

    public static Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch();
    }
}