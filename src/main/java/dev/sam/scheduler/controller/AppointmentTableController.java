package dev.sam.scheduler.controller;

import dev.sam.scheduler.dao.AppointmentDAO;
import dev.sam.scheduler.dao.ContactDAO;
import dev.sam.scheduler.helper.DateAndTimeHelper;
import dev.sam.scheduler.helper.StageHelper;
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
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the appointment table view.
 */
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
    private RadioButton allRadio;

    @FXML
    private RadioButton currentMonthRadio;

    @FXML
    private RadioButton currentWeekRadio;

    @FXML
    private Button resetCustomerButton;

    @FXML
    private Button newAppointmentButton;

    @FXML
    private Button updateAppointmentButton;

    @FXML
    private Button deleteAppointmentButton;

    @FXML
    private Text appointmentsForText;
    String appointmentsForTextPrompt;

    ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    FilteredList<Appointment> appointmentFilteredList;
    AppointmentDAO appointmentDAO;

    enum Filter {ALL, WEEK, MONTH}

    /**
     * This function is called after the root elements of the scene has been processed, and sets up the scene when it is loaded
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SharedData.INSTANCE.setAppointmentTableController(this);
        appointmentDAO = new AppointmentDAO();
        try {
            allAppointments = (ObservableList<Appointment>) appointmentDAO.getAll();
            appointmentFilteredList = new FilteredList<>(allAppointments);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        appointmentsForTextPrompt = appointmentsForText.getText();
        initializeNodes();
        initializeClickListeners();
    }

    /**
     * Set up the nodes for the scene, including the table
     */
    @Override
    public void initializeNodes() {
        initializeTable(appointmentTable, appointmentFilteredList);
        final ToggleGroup filterToggleGroup = new ToggleGroup();
        allRadio.setToggleGroup(filterToggleGroup);
        currentMonthRadio.setToggleGroup(filterToggleGroup);
        currentWeekRadio.setToggleGroup(filterToggleGroup);
    }

    /**
     * Set up the click listeners for the buttons in the scene
     */
    @Override
    public void initializeClickListeners() {
        allRadio.setOnAction(actionEvent -> {
            filterTable(Filter.ALL);
        });

        currentWeekRadio.setOnAction(actionEvent -> {
            filterTable(Filter.WEEK);
        });

        currentMonthRadio.setOnAction(actionEvent -> {
            filterTable(Filter.MONTH);
        });

        resetCustomerButton.setOnAction(actionEvent -> {
            SharedData.INSTANCE.setActiveCustomer(null);
            onThisTabSelected();
        });

        newAppointmentButton.setOnAction(actionEvent -> {
            Stage newStage = new Stage();
            try {
                StageHelper.loadSceneIntoStage(newStage, "appointment_form.fxml");
                // prevents the user from interacting with the customer view while the form is open
                newStage.initModality(Modality.APPLICATION_MODAL);
                newStage.showAndWait();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        deleteAppointmentButton.setOnAction(actionEvent -> {
            List<Appointment> selectedAppointments = appointmentTable.getSelectionModel().getSelectedItems();
            selectedAppointments.forEach(appointment -> {
                try {
                    appointmentDAO.delete(appointment);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            refreshTable(true);
        });

        updateAppointmentButton.setOnAction(actionEvent -> {
            List<Appointment> selectedAppointments = appointmentTable.getSelectionModel().getSelectedItems();
            if (selectedAppointments.size() > 1) {
                Alert appointmentSelectionAlert = new Alert(Alert.AlertType.ERROR);
                appointmentSelectionAlert.setTitle("Error Updating Appointment");
                appointmentSelectionAlert.setHeaderText("Too many appointments selected");
                appointmentSelectionAlert.setContentText("Please select only one appointment to edit");
                appointmentSelectionAlert.showAndWait();
                return;
            } else if (selectedAppointments.size() == 0) {
                Alert appointmentSelectionAlert = new Alert(Alert.AlertType.ERROR);
                appointmentSelectionAlert.setTitle("Error Updating Appointment");
                appointmentSelectionAlert.setHeaderText("No appointment Selected");
                appointmentSelectionAlert.setContentText("Please select an appointment to edit");
                appointmentSelectionAlert.showAndWait();
                return;
            }
            Stage newStage = new Stage();
            // This will be used by the customer form to load customer information in
            SharedData.INSTANCE.setActiveAppointment(appointmentTable.getSelectionModel().getSelectedItem());
            try {
                StageHelper.loadSceneIntoStage(newStage, "appointment_form.fxml");
                // prevents the user from interacting with the customer view while the form is open
                newStage.initModality(Modality.APPLICATION_MODAL);
                newStage.showAndWait();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Populate the table and set the cell value factory for each column
     *
     * @param tableView  The tableView to populate
     * @param tableItems The Filtered List of table items to populate the table
     * @param <T>        The type parameter
     */
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
            return new ReadOnlyObjectWrapper<>(DateAndTimeHelper.offsetDateTimeToLocalDateTimeStr(cellData.getValue().getStartDateTime()));
        });

        endDateColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(DateAndTimeHelper.offsetDateTimeToLocalDateTimeStr(cellData.getValue().getEndDateTime()));
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

    /**
     * This function gets called whenever the "appointments" tab is selected.
     * <p>
     * If a customer is selected in the SharedData singleton, the function displays only the appointments that match
     * that customer ID
     * <p>
     * Otherwise, the table filter is reset to show all items
     */
    @Override
    public void onThisTabSelected() {
        allRadio.setSelected(true);
        Customer activeCustomer = SharedData.INSTANCE.getActiveCustomer();
        if (activeCustomer != null) {
            appointmentsForText.setText(appointmentsForTextPrompt + " " + activeCustomer.getName());
            filterTable(Filter.ALL);
        } else {
            appointmentsForText.setText(appointmentsForTextPrompt + " Everyone");
            refreshTable(false);
        }
    }

    /**
     * Filter the table based on the Filter enum.
     *
     * LAMBDA: The setPredicate method uses a lambda expression because it is clearer and less verbose than creating a class for one predicate
     * @param filter The filter enum
     */
    private void filterTable(Filter filter) {
        Customer customer = SharedData.INSTANCE.getActiveCustomer();
        switch (filter) {
            case ALL -> appointmentFilteredList.setPredicate(appointment -> {
                if (customer != null) {
                    return appointment.getCustomerId() == customer.getId();
                }
                return true;
            });
            case WEEK -> appointmentFilteredList.setPredicate(appointment -> {
                OffsetDateTime currentTime = OffsetDateTime.now(ZoneId.of("+0"));
                OffsetDateTime startTime = appointment.getStartDateTime();
                OffsetDateTime weekFromNow = currentTime.plusWeeks(1);
                if (startTime.isBefore(weekFromNow) && startTime.isAfter(currentTime)) {
                    if (customer != null) {
                        return (customer.getId() == appointment.getCustomerId());
                    }
                    return true;
                }
                return false;
            });
            case MONTH -> appointmentFilteredList.setPredicate(appointment -> {
                OffsetDateTime currentTime = OffsetDateTime.now(ZoneId.of("+0"));
                OffsetDateTime startTime = appointment.getStartDateTime();
                OffsetDateTime monthFromNow = currentTime.plusMonths(1);
                if (startTime.isBefore(monthFromNow) && startTime.isAfter(currentTime)) {
                    if (customer != null) {
                        return (customer.getId() == appointment.getCustomerId());
                    }
                    return true;
                }
                return false;
            });
        }
    }


    /**
     * Resets the table filter to show all items.
     * <p>
     * If a new appointment was just added to the database, refreshes the table so that new appointment shows up.
     *
     * @param newItemWasAddedToDb True if a new appointment was just added to the database
     */
    public void refreshTable(boolean newItemWasAddedToDb) {
        appointmentFilteredList.setPredicate(appointment -> true);
        if (newItemWasAddedToDb) {
            try {
                allAppointments = (ObservableList<Appointment>) appointmentDAO.getAll();
                appointmentFilteredList = new FilteredList<>(allAppointments);
               initializeTable(appointmentTable, appointmentFilteredList);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            appointmentTable.refresh();
        }
    }

}
