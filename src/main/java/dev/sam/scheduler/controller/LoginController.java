package dev.sam.scheduler.controller;

import dev.sam.scheduler.App;
import dev.sam.scheduler.dao.CustomerDAO;
import dev.sam.scheduler.dao.CustomerDAOImpl;
import dev.sam.scheduler.dao.UserDAOImpl;
import dev.sam.scheduler.database.DB;
import dev.sam.scheduler.helper.StageHelper;
import dev.sam.scheduler.model.LocalizationEnum;
import dev.sam.scheduler.model.User;
import dev.sam.scheduler.view.FlagButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable, ControllerInterface {
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
    private UserDAOImpl userDAO;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userDAO = new UserDAOImpl();
        stage = App.getStage();
        initializeNodes();
        initializeClickListeners();
    }

    /**
     * Initialize the click listeners for the scene.
     */
    public void initializeClickListeners() {
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
            if (isValidLogin()) {
                try {
                    StageHelper.loadSceneIntoStage(stage, "main_tab_view.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                //TODO: show error
            }
        });

    }

    private boolean isValidLogin() {
        String userNameInput = userNameTextField.getText();
        String passwordInput = passwordField.getText();

        CustomerDAO customerDAO = new CustomerDAOImpl();
        try {
            customerDAO.getAllCustomers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            for (User user : userDAO.getAllUsers()) {
                if (user.getUserName().equals(userNameInput) && user.getPassword().equals(passwordInput)) {
                    DB.setActiveUser(user);
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
}