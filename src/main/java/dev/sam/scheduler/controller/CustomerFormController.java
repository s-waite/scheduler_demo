package dev.sam.scheduler.controller;

import dev.sam.scheduler.dao.CountryDAOImpl;
import dev.sam.scheduler.model.Country;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {

    @FXML
    private TextField addressInput;

    @FXML
    private ComboBox<String> countryComboBox;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField phoneNumberInput;

    @FXML
    private TextField postalCodeInput;

CountryDAOImpl countryDAO;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryDAO = new CountryDAOImpl();
        try {
            for (Country country : countryDAO.getAllCountries()) {
            countryComboBox.getItems().add(country.getCountryName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
