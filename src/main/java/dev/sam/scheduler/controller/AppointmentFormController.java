package dev.sam.scheduler.controller;

import dev.sam.scheduler.dao.AppointmentDAO;
import dev.sam.scheduler.dao.ContactDAO;
import dev.sam.scheduler.helper.DateAndTimeHelper;
import dev.sam.scheduler.model.Appointment;
import dev.sam.scheduler.model.Contact;
import dev.sam.scheduler.model.SharedData;
import dev.sam.scheduler.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AppointmentFormController extends Form implements Initializable, Controller {
    @FXML
    private TextField appIdInput;

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<Contact> contactComboBox;

    @FXML
    private TextField customerIdInput;

    @FXML
    private TextField descInput;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TextField endTimeInput;

    @FXML
    private TextField locInput;

    @FXML
    private Button saveButton;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private TextField startTimeInput;

    @FXML
    private TextField titleInput;

    @FXML
    private TextField typeInput;

    @FXML
    private TextField userIdInput;

    ArrayList<Contact> allContacts = new ArrayList<>();

    Appointment activeAppointment;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        activeAppointment = SharedData.INSTANCE.getActiveAppointment();
        AppointmentDAO appointmentDAO = new AppointmentDAO();

        if (activeAppointment == null) {
            try {
                int maxId = appointmentDAO.getMaxAppointmentId();
                appIdInput.setText(String.valueOf(maxId + 1));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        userIdInput.setText(String.valueOf(SharedData.INSTANCE.getActiveUser().getUserId()));

        initializeNodes();
        initializeClickListeners();
    }

    @Override
    public void initializeNodes() {
        ContactDAO contactDAO = new ContactDAO();
        try {
            allContacts = (ArrayList<Contact>) contactDAO.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Contact contact : allContacts) {
            contactComboBox.getItems().add(contact);
        }
    }

    @Override
    public void initializeClickListeners() {
        saveButton.setOnAction(actionEvent -> {
            try {
                saveForm();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    ArrayList<ValidationCode> validateForm() {
        return null;
    }

    @Override
    void saveForm() throws SQLException {
        User activeUser = SharedData.INSTANCE.getActiveUser();
        int appId = Integer.parseInt(appIdInput.getText().strip());
        String title = titleInput.getText().strip();
        String description = descInput.getText().strip();
        String location = locInput.getText().strip();
        String type = typeInput.getText().strip();
        OffsetDateTime startDate = DateAndTimeHelper.localDateAndTimeToUTC(startDatePicker.getValue(), LocalTime.parse(startTimeInput.getText()));
        OffsetDateTime endDate = DateAndTimeHelper.localDateAndTimeToUTC(endDatePicker.getValue(), LocalTime.parse(endTimeInput.getText()));
        OffsetDateTime creationDate = OffsetDateTime.now(ZoneOffset.UTC);
        String createdBy = activeUser.getUserName();
        OffsetDateTime modifyDate = OffsetDateTime.now(ZoneOffset.UTC);
        String modifiedBy = activeUser.getUserName();
        int customerId = Integer.parseInt(customerIdInput.getText());
        int userId = Integer.parseInt(userIdInput.getText());
        int contactId = contactComboBox.getValue().getId();

        if (SharedData.INSTANCE.getActiveAppointment() == null) {
            new Appointment(
                    appId,
                    title,
                    description,
                    location,
                    type,
                    startDate,
                    endDate,
                    creationDate,
                    createdBy,
                    modifyDate,
                    modifiedBy,
                    customerId,
                    userId,
                    contactId
            );
        } else {
            activeAppointment.setTitle(title);
            activeAppointment.setDescription(description);
            activeAppointment.setLocation(location);
            activeAppointment.setType(type);
            activeAppointment.setStartDateTime(startDate);
            activeAppointment.setEndDateTime(endDate);
            activeAppointment.setLastUpdatedDate(modifyDate);
            activeAppointment.setLastUpdatedBy(modifiedBy);
            activeAppointment.setCustomerId(customerId);
            activeAppointment.setCustomerId(userId);
            activeAppointment.setContactId(contactId);
        }
    }
}