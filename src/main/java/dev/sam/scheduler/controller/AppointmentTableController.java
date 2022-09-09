package dev.sam.scheduler.controller;

import dev.sam.scheduler.dao.AppointmentDAO;
import dev.sam.scheduler.dao.ContactDAO;
import dev.sam.scheduler.helper.DateAndTimeHelper;
import dev.sam.scheduler.model.Appointment;
import dev.sam.scheduler.model.Contact;
import dev.sam.scheduler.model.Customer;
import dev.sam.scheduler.model.SharedData;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AppointmentTableController extends Table implements Controller, Tab, Initializable {

    @FXML
    private TableView<Appointment> appointmentTable;

    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;

    @FXML
    private TableColumn<Appointment, String> contactColumn;

    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;

    @FXML
    private TableColumn<Appointment, String> descriptionColumn;

    @FXML
    private TableColumn<Appointment, String> endDateColumn;

    @FXML
    private TableColumn<Appointment, String> locationColumn;

    @FXML
    private TableColumn<Appointment, String> startDateColumn;

    @FXML
    private TableColumn<Appointment, String> titleColumn;

    @FXML
    private TableColumn<Appointment, String> typeColumn;

    @FXML
    private TableColumn<Appointment, Integer> userIdColumn;

    @FXML
    private Text appointmentsForText;
    String appointmentsForTextPrompt;

    ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    FilteredList<Appointment> appointmentFilteredList;

    @Override
    public void initializeNodes() {

    }

    @Override
    public void initializeClickListeners() {

    }

    @Override
    <T> void initializeTable(TableView<T> tableView, List<T> tableItems) {
        super.initializeTable(tableView, tableItems);

        ContactDAO contactDAO = new ContactDAO();

        appointmentIdColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(cellData.getValue().getId());
        });

        titleColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(cellData.getValue().getTitle());
        });

        descriptionColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(cellData.getValue().getDescription());
        });

        locationColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(cellData.getValue().getLocation());
        });

        typeColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(cellData.getValue().getType());
        });

        startDateColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(DateAndTimeHelper.offsetDateTimeToLocalTimeStr(cellData.getValue().getStartDateTime()));
        });

        endDateColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(DateAndTimeHelper.offsetDateTimeToLocalTimeStr(cellData.getValue().getEndDateTime()));
        });

        customerIdColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(cellData.getValue().getCustomerId());
        });

        userIdColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(cellData.getValue().getUserId());
        });

        contactColumn.setCellValueFactory(cellData -> {
            int contactId = cellData.getValue().getContactId();
            try {
                Contact contact = contactDAO.getContactFromId(contactId);
                return new ReadOnlyObjectWrapper<>(contact.getContactName());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void onThisTabSelected() {
        Customer activeCustomer = SharedData.INSTANCE.getActiveCustomer();
        if (activeCustomer != null) {
            appointmentsForText.setText(appointmentsForTextPrompt + " " + activeCustomer.getName());
            filterTableForCustomer(activeCustomer);
        } else {
            appointmentsForText.setText(appointmentsForTextPrompt + " Everyone");
            refreshTable(false);
        }
    }

    private void filterTableForCustomer(Customer customer) {
        appointmentFilteredList.setPredicate(a -> a.getCustomerId() == customer.getId());
    }

    // Resets the filter to show all appointments
    // Refreshes the table if a new appointment was added
    public void refreshTable(boolean newItemWasAddedToDb) {
        appointmentFilteredList.setPredicate(appointment -> true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SharedData.INSTANCE.setAppointmentTableController(this);
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        try {
            allAppointments = (ObservableList<Appointment>) appointmentDAO.getAll();
            appointmentFilteredList = new FilteredList<>(allAppointments);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        appointmentsForTextPrompt = appointmentsForText.getText();
        initializeTable(appointmentTable, appointmentFilteredList);
    }
}
