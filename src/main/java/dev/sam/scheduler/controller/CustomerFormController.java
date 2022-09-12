package dev.sam.scheduler.controller;

import dev.sam.scheduler.dao.CustomerDAO;
import dev.sam.scheduler.model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerFormController extends Form implements Initializable, Controller  {

    @FXML
    private TextField addressInput;
    @FXML
    private TextField customerIdInput;
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
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;

    CustomerDAO customerDAO;
    Customer customer;
    int maxCustomerId;


    /**
     * Runs when the scene is loaded into the stage.
     * <p>
     * Set up the nodes, click listeners, and any other necessary items to prepare the form for user use.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerDAO = new CustomerDAO();
        initializeNodes();
        initializeClickListeners();

        // Executes if user is updating/editing customer information instead of creating a new user
        // If creating a new user, finds the max customer ID in the database and load the next int into the customer ID field
        if (SharedData.INSTANCE.getActiveCustomer() != null) {
            customer = SharedData.INSTANCE.getActiveCustomer();
            loadCustomerInfo(customer);
        } else {
            try {
                maxCustomerId = customerDAO.getMaxCustomerId();
                customerIdInput.setText(String.valueOf(maxCustomerId + 1));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Additional setup for nodes that cannot be defined in the fxml.
     */
    @Override
    public void initializeNodes() {
        for (Country country : SharedData.INSTANCE.getCountries()) {
            countryComboBox.getItems().add(country);
        }
    }

    /**
     * Set up the click listeners for the form.
     */
    @Override
    public void initializeClickListeners() {
        countryComboBox.setOnAction(actionEvent -> {
            populateFirstLevelDivisions(countryComboBox.getValue());
        });

        saveButton.setOnAction(actionEvent -> {
            List<String> formErrors = new ArrayList<>();
            ArrayList<ValidationCode> formCodes = validateForm();
            for (ValidationCode validationCode : formCodes) {
                switch (validationCode) {
                    case OK -> {
                        try {
                            saveForm();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        return;
                    }
                    case NAME_ERR -> formErrors.add("Customer Name");
                    case ADDRESS_ERR -> formErrors.add("Customer Address");
                    case PHONE_ERR -> formErrors.add("Phone Number");
                    case POSTAL_ERR -> formErrors.add("Postal Code");
                    case COUNTRY_ERR -> formErrors.add("Country");
                    case FIRST_DIV_ERR -> formErrors.add("State/Province");
                }
            }
            StringBuilder errorMsg = new StringBuilder();
            // Adds each error to the error message string builder
            formErrors.forEach(err -> {
                errorMsg.append(err);
                errorMsg.append("\n");
            });

            // Show errors in alert popup to user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Form Error");
            alert.setHeaderText("Please correct errors in the following fields:");
            alert.setContentText(errorMsg.toString());
            alert.showAndWait();
        });

        cancelButton.setOnAction(actionEvent -> {
            Stage stage = (Stage) nameInput.getScene().getWindow();
            stage.close();
        });

    }

    /**
     * If editing a customer instead of creating one, this method loads their info into the form.
     *
     * @param customer The customer that is being edited
     */
    private void loadCustomerInfo(Customer customer) {
        // Get the first level division object that matches the customers' division ID
        FirstLevelDivision customerDivision = SharedData.INSTANCE.getFirstLevelDivisions().stream()
                .filter(d -> d.getDivisionId() == customer.getDivisionId()).toList().get(0);
        // Get the customer country using the customers' first level division object
        Country customerCountry = SharedData.INSTANCE.getCountries().stream()
                .filter(c -> c.getCountryId() == customerDivision.getCountryId()).toList().get(0);

        // Fill the form fields and combo boxes
        customerIdInput.setText(customer.getId().toString());
        nameInput.setText(customer.getName());
        addressInput.setText(customer.getAddress());
        postalCodeInput.setText(customer.getPostalCode());
        phoneNumberInput.setText(customer.getPhone());
        countryComboBox.setValue(customerCountry);
        firstDivComboBox.setValue(customerDivision);
        populateFirstLevelDivisions(customerCountry);
    }

    /**
     * Populates the first level division combo box based on the country parameter.
     *
     * @param country the country that we want to see the first level divisions of in the combo box
     */
    private void populateFirstLevelDivisions(Country country) {
        // Get the currently selected country
        int countryId = country.getCountryId();

        // Clear the divisions list in case the user previously selected a different country
        firstDivComboBox.getItems().clear();

        // Populate the combo box division list
        for (FirstLevelDivision division : SharedData.INSTANCE.getFirstLevelDivisions()) {
            if (division.getCountryId() == countryId) {
                firstDivComboBox.getItems().add(division);
            }
        }
    }

    /**
     * Check each form input for errors and return list of validation codes.
     *
     * @return List of validation codes
     */
    // TODO: implement more robust error checking
    // TODO: refactor to extend from form class
    @Override
    public ArrayList<ValidationCode> validateForm() {

        ArrayList<ValidationCode> returnCodes = new ArrayList<>();
        if (nameInput.getText().isBlank()) {
            returnCodes.add(ValidationCode.NAME_ERR);
        }

        if (addressInput.getText().isBlank()) {
            returnCodes.add(ValidationCode.ADDRESS_ERR);
        }

        if (postalCodeInput.getText().isBlank()) {
            returnCodes.add(ValidationCode.POSTAL_ERR);
        }

        if (phoneNumberInput.getText().isBlank()) {
            returnCodes.add(ValidationCode.PHONE_ERR);
        }

        if (countryComboBox.getValue() == null) {
            returnCodes.add(ValidationCode.COUNTRY_ERR);
        }

        if (firstDivComboBox.getValue() == null) {
            returnCodes.add(ValidationCode.FIRST_DIV_ERR);
        }

        // If no errors were found
        if (returnCodes.isEmpty()) {
            returnCodes.add(ValidationCode.OK);
        }

        return returnCodes;
    }

    /**
     * Save the form.
     * @throws SQLException
     */
    @Override
    public void saveForm() throws SQLException {
        User activeUser = SharedData.INSTANCE.getActiveUser();

        Integer customerId = Integer.valueOf(customerIdInput.getText());
        String customerName = nameInput.getText();
        String address = addressInput.getText();
        String postalCode = postalCodeInput.getText();
        String phone = phoneNumberInput.getText();
        OffsetDateTime creationDate = OffsetDateTime.now(ZoneOffset.UTC);
        String createdBy = activeUser.getUserName();
        OffsetDateTime modifyDate = OffsetDateTime.now(ZoneOffset.UTC);
        String modifiedBy = activeUser.getUserName();
        Integer firstDivId = firstDivComboBox.getValue().getDivisionId();

        if (SharedData.INSTANCE.getActiveCustomer() == null) {
            Customer newCustomer = new Customer(
                    customerId,
                    customerName,
                    address,
                    postalCode,
                    phone,
                    creationDate,
                    createdBy,
                    modifyDate,
                    modifiedBy,
                    firstDivId
            );
            customerDAO.insert(newCustomer);
        } else {
            customer.setName(customerName);
            customer.setAddress(address);
            customer.setPostalCode(postalCode);
            customer.setPhone(phone);
            customer.setLastUpdatedDate(modifyDate);
            customer.setLastUpdatedBy(activeUser.getUserName());
            customer.setDivisionId(firstDivId);
            customerDAO.update(customer);
        }


        CustomerTableController customerTableController = SharedData.INSTANCE.getCustomerTableController();
        customerTableController.refreshTable();

        // Active customer is not null since are finished editing it
        SharedData.INSTANCE.setActiveCustomer(null);

        Stage stage = (Stage) nameInput.getScene().getWindow();
        stage.close();
    }
}
