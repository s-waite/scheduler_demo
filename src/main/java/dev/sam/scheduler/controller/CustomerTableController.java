package dev.sam.scheduler.controller;

import dev.sam.scheduler.dao.CustomerDAO;
import dev.sam.scheduler.dao.CustomerDAOImpl;
import dev.sam.scheduler.database.DB;
import dev.sam.scheduler.helper.DateAndTimeHelper;
import dev.sam.scheduler.helper.StageHelper;
import dev.sam.scheduler.model.Customer;
import dev.sam.scheduler.model.SharedData;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerTableController implements Initializable, Controller {

    @FXML
    private Button updateCustomerButton;

    @FXML
    private Button deleteCustomerButton;

    @FXML
    private Button newCustomerButton;

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SharedData.INSTANCE.setCustomerTableController(this);
        customerDAO = new CustomerDAOImpl();
        initializeNodes();
        initializeClickListeners();
    }

    public void test() {};

    private void initializeTable() {
        try {
            customerTableView.setItems(customerDAO.getAllCustomers());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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

    }

}
