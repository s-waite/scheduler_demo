package dev.sam.scheduler.controller;

import dev.sam.scheduler.dao.AppointmentDAO;
import dev.sam.scheduler.dao.ContactDAO;
import dev.sam.scheduler.helper.DateAndTimeHelper;
import dev.sam.scheduler.model.Appointment;
import dev.sam.scheduler.model.Contact;
import dev.sam.scheduler.model.LocalizationEnum;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the report tab
 */
public class ReportTabController extends Table implements Controller, Tab, Initializable {

    @FXML
    private Button generateReportButton;
    @FXML
    private ComboBox<Report> reportSelector;
    @FXML
    private GridPane appointmentTypeReportGrid;
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private VBox optionsVbox;
    @FXML
    private Text totalAppointmentsText;
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
    private HBox reportBox;
    AppointmentDAO appointmentDAO;
    @FXML
    private ComboBox<Contact> contactComboBox;

    /**
     * Initialize the nodes of the scene
     */
    @Override
    public void initializeNodes() {
        ContactDAO contactDAO = new ContactDAO();
        removeAllReports();
        try {
            ObservableList<Contact> contacts = FXCollections.observableArrayList(contactDAO.getAll());
            contactComboBox.setItems(contacts);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<Report> reports = FXCollections.observableArrayList(Report.values());
        reportSelector.setItems(reports);

        optionsVbox.getChildren().remove(contactComboBox);
    }

    public void removeAllReports() {
        reportBox.getChildren().remove(appointmentTypeReportGrid);
        reportBox.getChildren().remove(appointmentTable);
        reportBox.getChildren().remove(totalAppointmentsText);
    }

    /**
     * Initialize the table
     * @param tableView The table to set up
     * @param tableItems The items to populate the table with
     * @param <T> The type parameter
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
     * Set up the click listeners for the scene
     */
    @Override
    public void initializeClickListeners() {
        generateReportButton.setOnAction(actionEvent -> {
            Report report = reportSelector.getValue();
            switch (report) {
                case APP_BY_TYPE_MONTH -> showAppointmentTypeReport();
                case SCHEDULE_FOR_CONTACTS -> showScheduleForContact();
                case TOTAL_APPOINTMENTS_FOR_EVERYONE -> showTotalAppointments();
            }
        });

        reportSelector.setOnAction(actionEvent -> {
            if (!reportSelector.getValue().equals(Report.SCHEDULE_FOR_CONTACTS)) {
                optionsVbox.getChildren().remove(contactComboBox);
            } else {
                optionsVbox.getChildren().add(1, contactComboBox);
            }
        });
    }

    @Override
    public void onThisTabSelected() {

    }

    /**
     * Report enum for the different types of reports that can be created.
     */
    enum Report {
        APP_BY_TYPE_MONTH(1, "Number of appointments by type and month"),
        SCHEDULE_FOR_CONTACTS(2, "Schedule for each contact"),
        TOTAL_APPOINTMENTS_FOR_EVERYONE(3, "Total number of appointments for all customers");

        private final int reportId;
        private final String stringRep;

        Report(int reportId, String stringRep) {
            this.reportId = reportId;
            this.stringRep = stringRep;
        }

        @Override
        public String toString() {
            return this.stringRep;
        }

        public int getReportId() {
            return reportId;
        }
    }

    /**
     * Show report for the schedule of a contact.
     */
    private void showScheduleForContact() {
        removeAllReports();
        reportBox.getChildren().add(appointmentTable);
        try {
            ObservableList<Appointment> appointments = FXCollections.observableArrayList(appointmentDAO.getAllOfContact(contactComboBox.getValue()));
            initializeTable(appointmentTable, new FilteredList<>(appointments));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Show report for the total appointments in the database
     */
    private void showTotalAppointments() {
        removeAllReports();
        reportBox.getChildren().add(totalAppointmentsText);
        totalAppointmentsText.setText("Total Appointments: ");
        try {
            totalAppointmentsText.setText(totalAppointmentsText.getText() + appointmentDAO.getTotalNumOfAppointments());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Show appointment types and number by month.
     */
    private void showAppointmentTypeReport() {
        removeAllReports();
        reportBox.getChildren().add(appointmentTypeReportGrid);
        int row = 1;
        int column = 0;
        ArrayList<String> allTypes = null;
        try {
            allTypes = appointmentDAO.getDistinctAppointmentTypes();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Month month : Month.values()) {
            appointmentTypeReportGrid.add(new Text(month.getDisplayName(TextStyle.FULL, LocalizationEnum.INSTANCE.getCurrentLocale())), 0, row, 1, 1);
            StringBuilder types = new StringBuilder();
            StringBuilder numbers = new StringBuilder();
            for (String type : allTypes) {
                int numOfApps = 0;
                try {
                    numOfApps = appointmentDAO.getNumOfAppointmentTypeOfMonth(type, month);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if (numOfApps > 0) {
                    types.append(type);
                    types.append("\n");
                    numbers.append(numOfApps);
                    numbers.append("\n");
                }
            }
            appointmentTypeReportGrid.add(new Text(types.toString()), 1, row, 1, 1);
            appointmentTypeReportGrid.add(new Text(numbers.toString()), 2, row, 1, 1);

            row += 1;
        }
    }

    /**
     * Initialize the scene.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentDAO = new AppointmentDAO();
        initializeNodes();
        initializeClickListeners();
    }
}
