package dev.sam.scheduler.controller;

import dev.sam.scheduler.dao.*;
import dev.sam.scheduler.helper.DateAndTimeHelper;
import dev.sam.scheduler.helper.StageHelper;
import dev.sam.scheduler.model.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

// TODO: refactor to extend from table class
public class CustomerTableController extends Table implements Initializable, Controller, Tab {

    @FXML
    private Button updateCustomerButton;

    @FXML
    private Button deleteCustomerButton;

    @FXML
    private Button newCustomerButton;

    @FXML
    private Button viewCustomerAppointmentsButton;

    @FXML
    private TableColumn<Customer, String> countryColumn;

    @FXML
    TableColumn<Customer, String> firstDivisionNameColumn;

    @FXML
    private TableColumn<Customer, String> addressColumn;

    @FXML
    private TableColumn<Customer, Integer> divisionIdColumn;

    @FXML
    private TableColumn<Customer, String> creationDateColumn;

    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;

    @FXML
    private TableView<Customer> customerTableView;

    @FXML
    private TableColumn<Customer, String> lastUpdatedDateColumn;

    @FXML
    private TableColumn<Customer, String> nameColumn;

    @FXML
    private TableColumn<Customer, String> postalCodeColumn;

    CustomerDAO customerDAO;
    CountryDAO countryDAO;
    AppointmentDAO appointmentDAO;


    // TODO refactor to inherit from table and extend tab
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SharedData.INSTANCE.setCustomerTableController(this);
        customerDAO = new CustomerDAO();
        countryDAO = new CountryDAOImpl();
        appointmentDAO = new AppointmentDAO();
        initializeNodes();
        initializeClickListeners();
    }

    private void initializeTable() {
        customerTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        try {
            customerTableView.setItems(customerDAO.getAll());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        countryColumn.setCellValueFactory(cellData -> {
            int divisionId = cellData.getValue().getDivisionId();
            Country country = Country.getCountryFromDivisionId(divisionId);
            return new ReadOnlyObjectWrapper<>(country.getCountryName());
        });

        firstDivisionNameColumn.setCellValueFactory(cellData -> {
            FirstLevelDivision division = FirstLevelDivision.getDivisionFromId(cellData.getValue().getDivisionId());
            return new ReadOnlyObjectWrapper<>(division.getDivision());
        });

        customerIdColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(cellData.getValue().getId());
        });

        nameColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(cellData.getValue().getName());
        });

        addressColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(cellData.getValue().getAddress());
        });

        postalCodeColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(cellData.getValue().getPostalCode());
        });

        divisionIdColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(cellData.getValue().getDivisionId());
        });

        creationDateColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(DateAndTimeHelper.offsetDateTimeToLocalTimeStr(cellData.getValue().getCreationDate()));
        });

        lastUpdatedDateColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(DateAndTimeHelper.offsetDateTimeToLocalTimeStr(cellData.getValue().getLastUpdatedDate()));
        });

    }

    @Override
    public void initializeNodes() {
        initializeTable();
    }

    public void refreshTable() {
        initializeTable();
        customerTableView.refresh();
    }

    @Override
    public void initializeClickListeners() {
        newCustomerButton.setOnAction(actionEvent -> {
            Stage newStage = new Stage();
            try {
                StageHelper.loadSceneIntoStage(newStage, "customer_form.fxml");
                // prevents the user from interacting with the customer view while the form is open
                newStage.initModality(Modality.APPLICATION_MODAL);
                newStage.showAndWait();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        updateCustomerButton.setOnAction(actionEvent -> {
            List<Customer> selectedCustomers = customerTableView.getSelectionModel().getSelectedItems();
            if (selectedCustomers.size() > 1) {
                Alert tooManyCustomersAlert = new Alert(Alert.AlertType.ERROR);
                tooManyCustomersAlert.setTitle("Error Updating Customer");
                tooManyCustomersAlert.setHeaderText("Too many customers selected");
                tooManyCustomersAlert.setContentText("Please select only one customer to edit");
                tooManyCustomersAlert.showAndWait();
                return;
            } else if (selectedCustomers.size() == 0) {
                Alert tooManyCustomersAlert = new Alert(Alert.AlertType.ERROR);
                tooManyCustomersAlert.setTitle("Error Updating Customer");
                tooManyCustomersAlert.setHeaderText("No customers Selected");
                tooManyCustomersAlert.setContentText("Please select a customer to edit");
                tooManyCustomersAlert.showAndWait();
                return;
            }
            Stage newStage = new Stage();
            // This will be used by the customer form to load customer information in
            SharedData.INSTANCE.setActiveCustomer(customerTableView.getSelectionModel().getSelectedItem());
            try {
                StageHelper.loadSceneIntoStage(newStage, "customer_form.fxml");
                // prevents the user from interacting with the customer view while the form is open
                newStage.initModality(Modality.APPLICATION_MODAL);
                newStage.showAndWait();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        deleteCustomerButton.setOnAction(actionEvent -> {
            List<Customer> selectedCustomers = customerTableView.getSelectionModel().getSelectedItems();
            StringBuilder appointmentErrors = new StringBuilder();
            selectedCustomers.forEach(customer -> {
                try {
                    int numOfCustomerAppointments = appointmentDAO.getNumberOfAppointmentsFromCustomerId(customer.getId());
                    if (numOfCustomerAppointments > 0) {
                        appointmentErrors
                                .append(customer.getName())
                                .append(" has ")
                                .append(numOfCustomerAppointments);
                    }
                    if (numOfCustomerAppointments == 1) {
                        appointmentErrors
                                .append(" appointment")
                                .append("\n");
                        return;
                    } else if (numOfCustomerAppointments > 1) {
                        appointmentErrors
                                .append(" appointments")
                                .append("\n");
                        return;
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try {
                    customerDAO.delete(customer);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            Alert assignedAppointmentsAlert = new Alert(Alert.AlertType.ERROR);
            assignedAppointmentsAlert.setHeaderText("Some customers could not be deleted because they have associated appointments");
            assignedAppointmentsAlert.setContentText(appointmentErrors.toString());
            assignedAppointmentsAlert.showAndWait();
            refreshTable();
        });

        viewCustomerAppointmentsButton.setOnAction(actionEvent -> {
            List<Customer> selectedCustomers = customerTableView.getSelectionModel().getSelectedItems();
            // Validate exactly one customer is selected
            if (selectedCustomers.size() != 1) {
                Alert incorrectSelectionAlert = new Alert(Alert.AlertType.ERROR);
                incorrectSelectionAlert.setHeaderText("Please select exactly one customer");
                incorrectSelectionAlert.showAndWait();
                return;
            }

            // Whenever the appointment table tab is opened, it is filtered based on the active customer (or not filtered if there is none)
            // Setting the active customer lets the appointment table know that we are filtering for a customer
            SharedData.INSTANCE.setActiveCustomer(selectedCustomers.get(0));
            TabPane tabPane = SharedData.INSTANCE.getMainTabPane();
            tabPane.getSelectionModel().select(1);
        });

    }

    @Override
    public void onThisTabSelected() {
        SharedData.INSTANCE.setActiveCustomer(null);
    }
}
