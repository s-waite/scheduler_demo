package dev.sam.scheduler.controller;

import dev.sam.scheduler.dao.CustomerDAO;
import dev.sam.scheduler.dao.CustomerDAOImpl;
import dev.sam.scheduler.helper.DateAndTimeHelper;
import dev.sam.scheduler.model.Customer;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerTableController implements Initializable {

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
        customerDAO = new CustomerDAOImpl();
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
}
