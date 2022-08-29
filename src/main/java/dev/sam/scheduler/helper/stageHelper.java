package dev.sam.scheduler.helper;

import dev.sam.scheduler.App;
import dev.sam.scheduler.model.LocalizationEnum;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class stageHelper {

    /**
     * Load a scene into the FXML application stage.
     *
     * @param stage A reference to the application stage
     * @param fxmlResource The fxml resource to load into the scene
     * @throws IOException
     */
    public static void loadSceneIntoStage(Stage stage, String fxmlResource) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("dev.sam.scheduler.strings", LocalizationEnum.INSTANCE.getCurrentLocale());
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxmlResource), bundle);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

}
