package dev.sam.scheduler.controller;

import dev.sam.scheduler.dao.CountryDAOImpl;
import dev.sam.scheduler.dao.FirstLevelDivisionDAO;
import dev.sam.scheduler.dao.FirstLevelDivisionDAOImpl;
import dev.sam.scheduler.model.Country;
import dev.sam.scheduler.model.FirstLevelDivision;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {

    @FXML
    private TextField addressInput;
    @FXML
    private ComboBox<Country> countryComboBox;
    @FXML
    private ComboBox<String> firstDivComboBox;
    @FXML
    private TextField nameInput;
    @FXML
    private TextField phoneNumberInput;
    @FXML
    private TextField postalCodeInput;
    CountryDAOImpl countryDAO;
    FirstLevelDivisionDAOImpl firstLevelDivisionDAO;
    ArrayList<Country> countries;
    ArrayList<FirstLevelDivision> divisions;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            queryStaticDbData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        initializeViews();
        initializeClickListeners();
    }

    private void queryStaticDbData() throws SQLException {
        countryDAO = new CountryDAOImpl();
        firstLevelDivisionDAO = new FirstLevelDivisionDAOImpl();

        countries = countryDAO.getAllCountries();
        divisions = firstLevelDivisionDAO.getAllDivisions();
    }

    private void initializeViews() {
        for (Country country : countries) {
            countryComboBox.getItems().add(country);
        }
    }

    private void initializeClickListeners() {
        countryComboBox.setOnAction(actionEvent -> {
            populateFirstLevelDivisions();
        });

    }

    private void populateFirstLevelDivisions() {
        // Get the currently selected country
        int countryComboSelection = countryComboBox.getValue().getCountryId();

        // Clear the divisions list in case the user previously selected a different country
       firstDivComboBox.getItems().clear();

       // Populate the combo box division list
        for (FirstLevelDivision division : divisions) {
            if (division.getCountryId() == countryComboSelection) {
                firstDivComboBox.getItems().add(division.getDivision());
            }
        }

    }
}
