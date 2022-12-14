package dev.sam.scheduler.controller;

import dev.sam.scheduler.App;
import dev.sam.scheduler.dao.CustomerDAO;
import dev.sam.scheduler.dao.UserDAO;
import dev.sam.scheduler.helper.StageHelper;
import dev.sam.scheduler.model.LocalizationEnum;
import dev.sam.scheduler.model.SharedData;
import dev.sam.scheduler.model.User;
import dev.sam.scheduler.model.WindowSizes;
import dev.sam.scheduler.view.FlagButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller for the login form.
 */
public class LoginController implements Initializable, Controller {
    @FXML
    private FlagButton usFlagButton;
    @FXML
    private FlagButton frenchFlagButton;
    @FXML
    private Button loginButton;
    @FXML
    private Label locationLabel;
    @FXML
    private TextField userNameTextField;
    @FXML
    private PasswordField passwordField;
    private Stage stage;
    private UserDAO userDAO;


    /**
     * Set up the scene.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userDAO = new UserDAO();
        stage = App.getStage();
        initializeNodes();
        initializeClickListeners(resources);
    }

    /**
     * Initialize the click listeners for the scene.
     */
    public void initializeClickListeners(ResourceBundle bundle) {
        usFlagButton.setOnAction(actionEvent -> {
            // Reload the scene with a new locale if it is not already the active locale
            if (!usFlagButton.isChecked()) {
                LocalizationEnum.INSTANCE.setCurrentLocale(Locale.ENGLISH);
                try {
                    StageHelper.loadSceneIntoStage(stage, "login.fxml");
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
                try {
                    StageHelper.loadSceneIntoStage(stage, "login.fxml");
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        });

        loginButton.setOnAction(actionEvent -> {
            File file = new File("login_activity.txt");
            String activityString = "\n" + userNameTextField.getText() + ", " + OffsetDateTime.now(ZoneOffset.UTC);
            BufferedWriter bufferedWriter;
            try {
                file.createNewFile();
                bufferedWriter = new BufferedWriter(new FileWriter(file, true));
                bufferedWriter.append(activityString);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (isValidLogin()) {
                try {
                    bufferedWriter.append(", successful");
                    stage.setWidth(WindowSizes.WIDTH_LARGE.getSize());
                    stage.setHeight(WindowSizes.HEIGHT_MED.getSize());
                    StageHelper.loadSceneIntoStage(stage, "main_tab_view.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Alert invalidLoginAlert = new Alert(Alert.AlertType.ERROR);
                invalidLoginAlert.setHeaderText(bundle.getString("login.invalidHeader"));
                invalidLoginAlert.setContentText(bundle.getString("login.invalidBody"));
                try {
                    bufferedWriter.append(", unsuccessful");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                invalidLoginAlert.showAndWait();
            }
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    /**
     * Check the login to make sure it is valid.
     * @return
     */
    private boolean isValidLogin() {
        String userNameInput = userNameTextField.getText();
        String passwordInput = passwordField.getText();

        CustomerDAO customerDAO = new CustomerDAO();
        try {
            customerDAO.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            for (User user : userDAO.getAll()) {
                if (user.getUserName().equals(userNameInput) && user.getPassword().equals(passwordInput)) {
                    SharedData.INSTANCE.setActiveUser(user);
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Initialize the nodes for the scene.
     * <p>
     * This includes nodes that cannot be easily defined in FXML and inserting dynamic data into the nodes.
     */
    public void initializeNodes() {
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
        if (LocalizationEnum.INSTANCE.getCurrentLocale().getLanguage().equals("en")) {
            usFlagButton.check();
        } else {
            frenchFlagButton.check();
        }

        locationLabel.setTextOverrun(OverrunStyle.CLIP);
        String locationLabelText = locationLabel.getText();
        locationLabel.setText(locationLabelText + ZoneId.systemDefault());

    }

    @Override
    public void initializeClickListeners() {

    }
}