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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
    private Text appointmentsForText;
    String appointmentsForTextPrompt;

    ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    FilteredList<Appointment> appointmentFilteredList;

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
        Customer activeCustomer = SharedData.INSTANCE.getActiveCustomer();
        if (activeCustomer != null) {
            appointmentsForText.setText(appointmentsForTextPrompt + " " + activeCustomer.getName());
            filterTable(activeCustomer);
        } else {
            appointmentsForText.setText(appointmentsForTextPrompt + " Everyone");
            refreshTable(false);
        }
    }

    enum Filter {
        ALL, WEEK, MONTH
    }

    /**
     * Filters the appointment table
     * <p>
     * If a Customer object is passed, filters for appointments of that customer
     *
     * @param t   The object passed
     * @param <T> The type parameter
     */
    private <T> void filterTable(T t) {
        if (!(t instanceof Customer || t instanceof Filter)) {
            throw new IllegalArgumentException();
        }

        if (t instanceof Customer) {
            appointmentFilteredList.setPredicate(a -> a.getCustomerId() == ((Customer) t).getId());
            return;
        }

        if (SharedData.INSTANCE.getActiveCustomer() == null) {
            Filter filter = (Filter) t;
            switch (filter) {
                case ALL -> appointmentFilteredList.setPredicate(appointment -> {
                    if ()
                });
                case WEEK -> appointmentFilteredList.setPredicate(appointment -> {
                    OffsetDateTime currentTime = OffsetDateTime.now(ZoneId.of("+0"));
                    OffsetDateTime startTime = appointment.getStartDateTime();
                    OffsetDateTime weekFromNow = currentTime.plusWeeks(1);
                    return (startTime.isBefore(weekFromNow) && startTime.isAfter(currentTime));
                });
                case MONTH -> appointmentFilteredList.setPredicate(appointment -> {
                    OffsetDateTime currentTime = OffsetDateTime.now(ZoneId.of("+0"));
                    OffsetDateTime startTime = appointment.getStartDateTime();
                    OffsetDateTime monthFromNow = currentTime.plusMonths(1);
                    return (startTime.isBefore(monthFromNow) && startTime.isAfter(currentTime));
                });
            }
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
    }

    /**
     * This function is called after the root elements of the scene has been processed, and sets up the scene when it is loaded
     *
     * @param url
     * @param resourceBundle
     */
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
        initializeNodes();
        initializeClickListeners();
    }
}
