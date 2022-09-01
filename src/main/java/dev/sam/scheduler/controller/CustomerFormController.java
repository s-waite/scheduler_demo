package dev.sam.scheduler.controller;

import dev.sam.scheduler.dao.CountryDAOImpl;
import dev.sam.scheduler.dao.FirstLevelDivisionDAOImpl;
import dev.sam.scheduler.database.DB;
import dev.sam.scheduler.model.Country;
import dev.sam.scheduler.model.Customer;
import dev.sam.scheduler.model.FirstLevelDivision;
import dev.sam.scheduler.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable, ControllerInterface {

    @FXML
    private TextField addressInput;
    @FXML
    private ComboBox<Country> countryComboBox;
    @FXML
    private ComboBox<FirstLevelDivision> firstDivComboBox;
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
    Customer customer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            queryStaticDbData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        initializeNodes();
        initializeClickListeners();

        // Executes if user is updating/editing customer information
        if (DB.getActiveCustomer() != null) {
            customer = DB.getActiveCustomer();
            loadCustomerInfo(customer);
        }
    }

    /**
     * This method gets the first level division and country data from the database
     * @throws SQLException
     */
    private void queryStaticDbData() throws SQLException {
        countryDAO = new CountryDAOImpl();
        firstLevelDivisionDAO = new FirstLevelDivisionDAOImpl();

        countries = countryDAO.getAllCountries();
        divisions = firstLevelDivisionDAO.getAllDivisions();
    }

    @Override
    public void initializeNodes() {
        for (Country country : countries) {
            countryComboBox.getItems().add(country);
        }
    }

    @Override
    public void initializeClickListeners() {
        countryComboBox.setOnAction(actionEvent -> {
            populateFirstLevelDivisions(countryComboBox.getValue());
        });
    }

    /**
     * If editing a customer instead of creating one, this method loads their info into the form.
     * @param customer The customer that is being edited
     */
    private void loadCustomerInfo(Customer customer) {

        // TODO: use a stream to get the customers first level division
        FirstLevelDivision customerDivision = divisions.stream()
                .filter(d -> d.getDivisionId() == customer.getDivisionId()).toList().get(0);
        Country customerCountry = countries.stream()
                .filter(c -> c.getCountryId() == customerDivision.getCountryId()).toList().get(0);


        nameInput.setText(customer.getName());
        addressInput.setText(customer.getAddress());
        postalCodeInput.setText(customer.getPostalCode());
        phoneNumberInput.setText(customer.getPhone());
        countryComboBox.setValue(customerCountry);
        firstDivComboBox.setValue(customerDivision);
        populateFirstLevelDivisions(customerCountry);



    }

    /**
     * Populates the first level division combo box based on the country parameter
     * @param country the country that we want to see the first level divisions of
     */
    private void populateFirstLevelDivisions(Country country) {
        // Get the currently selected country
        int countryId = country.getCountryId();

        // Clear the divisions list in case the user previously selected a different country
        firstDivComboBox.getItems().clear();

        // Populate the combo box division list
        for (FirstLevelDivision division : divisions) {
            if (division.getCountryId() == countryId) {
                firstDivComboBox.getItems().add(division);
            }
        }
    }

    // TODO: save method
    // TODO: input validation
}
