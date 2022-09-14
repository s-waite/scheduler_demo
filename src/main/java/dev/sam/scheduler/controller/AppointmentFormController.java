package dev.sam.scheduler.controller;

import dev.sam.scheduler.dao.AppointmentDAO;
import dev.sam.scheduler.dao.ContactDAO;
import dev.sam.scheduler.dao.CustomerDAO;
import dev.sam.scheduler.dao.UserDAO;
import dev.sam.scheduler.helper.DateAndTimeHelper;
import dev.sam.scheduler.model.Appointment;
import dev.sam.scheduler.model.Contact;
import dev.sam.scheduler.model.SharedData;
import dev.sam.scheduler.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The controller for the appointment form
 */
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

    /**
     * Set up the form.
     * @param url
     * @param resourceBundle
     */
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
        } else {
            try {
                loadAppointmentIntoFields(activeAppointment);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        userIdInput.setText(String.valueOf(SharedData.INSTANCE.getActiveUser().getUserId()));

        initializeNodes();
        initializeClickListeners();
    }

    /**
     * Initialize the nodes in the scene.
     */
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

    /**
     * If load the information from an appointment into the form fields.
     * @param appointment The appointment to load.
     * @throws SQLException
     */
    public void loadAppointmentIntoFields(Appointment appointment) throws SQLException {
        ContactDAO contactDAO = new ContactDAO();
        appIdInput.setText(String.valueOf(appointment.getId()));
        titleInput.setText(appointment.getTitle());
        descInput.setText(appointment.getDescription());
        locInput.setText(appointment.getLocation());
        contactComboBox.setValue(contactDAO.getContactFromId(appointment.getContactId()));
        typeInput.setText(appointment.getType());
        startDatePicker.setValue(DateAndTimeHelper.offsetDateTimeToLocalDate(appointment.getStartDateTime()));
        startTimeInput.setText(DateAndTimeHelper.offsetDateTimeToLocalTimeStr(appointment.getStartDateTime()));
        endDatePicker.setValue(DateAndTimeHelper.offsetDateTimeToLocalDate(appointment.getEndDateTime()));
        endTimeInput.setText(DateAndTimeHelper.offsetDateTimeToLocalTimeStr(appointment.getEndDateTime()));
        customerIdInput.setText(String.valueOf(appointment.getCustomerId()));
        userIdInput.setText(String.valueOf(appointment.getUserId()));
    }

    /**
     * Set up click listeners for the scene
     * LAMBDA: The click listeners use lambda expressions because it is clearer and less verbose than creating a class for one action
     * LAMBDA: The for each method on the form errors uses a lambda instead of having to write out the anonymous inner class
     */
    @Override
    public void initializeClickListeners() {
        cancelButton.setOnAction(actionEvent -> {
            SharedData.INSTANCE.setActiveAppointment(null);
            Stage stage = (Stage) userIdInput.getScene().getWindow();
            stage.close();
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
                    case EMPTY_FIELDS_ERR -> formErrors.add("Empty fields");
                    case USER_ID_NOT_FOUND_ERR -> formErrors.add("User ID not found");
                    case CUSTOMER_ID_NOT_FOUND_ERR -> formErrors.add("Customer ID not found");
                    case START_TIME_OUTSIDE_HRS_ERR -> formErrors.add("Start time outside business hours");
                    case END_TIME_OUTSIDE_HRS_ERR -> formErrors.add("End time outside business hours");
                    case TIME_ERR -> formErrors.add("Please enter valid times");
                    case START_DATE_BEFORE_END_ERR -> formErrors.add("Start date is before end date");
                    case CONFLICTING_APP_ERR -> formErrors.add("Customer has conflicting appointments");
                    case USER_CONFLICTING_APP_ERR -> formErrors.add("User has conflicting appointments");
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

    }

    /**
     * Validate that the form inputs are correct.
     * @return
     */
    @Override
    ArrayList<ValidationCode> validateForm() {
        ArrayList<ValidationCode> returnCodes = new ArrayList<>();

        if (titleInput.getText().isBlank()
                || descInput.getText().isBlank()
                || locInput.getText().isBlank()
                || contactComboBox.getValue() == null
                || typeInput.getText().isBlank()
                || userIdInput.getText().isBlank()
                || customerIdInput.getText().isBlank()
                || startDatePicker.getValue() == null
                || startTimeInput.getText().isBlank()
                || endDatePicker.getValue() == null
                || endTimeInput.getText().isBlank()) {
            returnCodes.add(ValidationCode.EMPTY_FIELDS_ERR);
            return returnCodes;
        }
        CustomerDAO customerDAO = new CustomerDAO();
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        UserDAO userDAO = new UserDAO();

        // if updating, ignore the appointment we are updating
        try {
            // Check to see if user id exist
            if (!userDAO.userIdExists(Integer.parseInt(userIdInput.getText()))) {
                returnCodes.add(ValidationCode.USER_ID_NOT_FOUND_ERR);
            } else {
                Boolean isConflict = false;
                // If id does exist make sure the appointment times do not overlap with other appointments of this user
                List<Appointment> userApps = appointmentDAO.getAllFromUserId(Integer.parseInt(userIdInput.getText()));
                for (Appointment appointment : userApps) {
                    ZonedDateTime  startLocal = ZonedDateTime.from(appointment.getStartDateTime()).withZoneSameInstant(ZoneId.systemDefault());
                    ZonedDateTime  endLocal = ZonedDateTime.from(appointment.getEndDateTime()).withZoneSameInstant(ZoneId.systemDefault());
                    ZonedDateTime startInpLocal = LocalDateTime.parse(startDatePicker.getValue() + " " + startTimeInput.getText(), DateAndTimeHelper.formatter).atZone(ZoneOffset.systemDefault());
                    ZonedDateTime endInpLocal = LocalDateTime.parse(endDatePicker.getValue() + " " + endTimeInput.getText(), DateAndTimeHelper.formatter).atZone(ZoneOffset.systemDefault());
                    if (appointment.getId() == Integer.parseInt(appIdInput.getText())) {
                        if ((startInpLocal.isAfter(startLocal) || startInpLocal.equals(startLocal)) && (endInpLocal.isBefore(endLocal) || endInpLocal.isEqual(endLocal))) {
                            break;
                        }
                    }

                    if ((startInpLocal.isAfter(startLocal) || startInpLocal.isEqual(startLocal)) && startInpLocal.isBefore(endLocal)) {
                        isConflict = true;
                    }

                    if ((endInpLocal.isAfter(startLocal) || endInpLocal.isEqual(startLocal)) && endInpLocal.isBefore(endLocal)) {
                        isConflict = true;
                    }

                    if ((endInpLocal.isEqual(endLocal))) {
                        isConflict = true;
                    }

                    if (isConflict) {
                        returnCodes.add(ValidationCode.USER_CONFLICTING_APP_ERR);
                        break;
                    }
                }
            }
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }

        // if updating, ignore the appointment we are updating
        try {
            // Check to see if user id exist
            if (!customerDAO.customerIdExists(Integer.parseInt(customerIdInput.getText()))) {
                returnCodes.add(ValidationCode.CUSTOMER_ID_NOT_FOUND_ERR);
            } else {
                Boolean isConflict = false;
                // If id does exist make sure the appointment times do not overlap with other appointments of this user
                List<Appointment> userApps = appointmentDAO.getAllFromCustomerId(Integer.parseInt(customerIdInput.getText()));
                for (Appointment appointment : userApps) {
                    ZonedDateTime  startLocal = ZonedDateTime.from(appointment.getStartDateTime()).withZoneSameInstant(ZoneId.systemDefault());
                    ZonedDateTime  endLocal = ZonedDateTime.from(appointment.getEndDateTime()).withZoneSameInstant(ZoneId.systemDefault());
                    ZonedDateTime startInpLocal = LocalDateTime.parse(startDatePicker.getValue() + " " + startTimeInput.getText(), DateAndTimeHelper.formatter).atZone(ZoneOffset.systemDefault());
                    ZonedDateTime endInpLocal = LocalDateTime.parse(endDatePicker.getValue() + " " + endTimeInput.getText(), DateAndTimeHelper.formatter).atZone(ZoneOffset.systemDefault());
                    if (appointment.getId() == Integer.parseInt(appIdInput.getText())) {
                        if ((startInpLocal.isAfter(startLocal) || startInpLocal.equals(startLocal)) && (endInpLocal.isBefore(endLocal) || endInpLocal.isEqual(endLocal))) {
                            break;
                        }
                    }

                    if ((startInpLocal.isAfter(startLocal) || startInpLocal.isEqual(startLocal)) && startInpLocal.isBefore(endLocal)) {
                        isConflict = true;
                    }

                    if ((endInpLocal.isAfter(startLocal) || endInpLocal.isEqual(startLocal)) && endInpLocal.isBefore(endLocal)) {
                        isConflict = true;
                    }

                    if ((endInpLocal.isEqual(endLocal))) {
                        isConflict = true;
                    }

                    if (isConflict) {
                        returnCodes.add(ValidationCode.CONFLICTING_APP_ERR);
                        break;
                    }
                }
            }
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }


        try {
            ZonedDateTime startDateTime = LocalDateTime.parse(startDatePicker.getValue() + " " + startTimeInput.getText(), DateAndTimeHelper.formatter).atZone(ZoneId.systemDefault());
            ZonedDateTime endDateTime = LocalDateTime.parse(endDatePicker.getValue() + " " + endTimeInput.getText(), DateAndTimeHelper.formatter).atZone(ZoneId.systemDefault());
            ZonedDateTime startDateTimeEST = startDateTime.withZoneSameInstant(ZoneId.of("US/Eastern"));
            System.out.println(startDateTimeEST);
            ZonedDateTime acceptableStartTime = startDateTimeEST.withHour(8).withMinute(0).withSecond(0);
            ZonedDateTime endDateTimeEST = endDateTime.withZoneSameInstant(ZoneId.of("US/Eastern"));
            ZonedDateTime acceptableEndTime = endDateTimeEST.withHour(20).withMinute(0).withSecond(0);
            if (startDateTime.isAfter(endDateTime)) {
                returnCodes.add(ValidationCode.START_DATE_BEFORE_END_ERR);
            }

            if (startDateTimeEST.getHour() < acceptableStartTime.getHour() || startDateTimeEST.getHour() > acceptableEndTime.getHour()) {
                returnCodes.add(ValidationCode.START_TIME_OUTSIDE_HRS_ERR);
            }

            if (endDateTimeEST.getHour() < acceptableStartTime.getHour() || endDateTimeEST.getHour() > acceptableEndTime.getHour()) {
                returnCodes.add(ValidationCode.END_TIME_OUTSIDE_HRS_ERR);
            }
        } catch (
                Exception e) {
            returnCodes.add(ValidationCode.TIME_ERR);
        }


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
    void saveForm() throws SQLException {
        AppointmentDAO appointmentDAO = new AppointmentDAO();
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
            System.out.println("save");
            appointmentDAO.insert(new Appointment(
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
            ));

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
            activeAppointment.setUserId(userId);
            activeAppointment.setContactId(contactId);
            appointmentDAO.update(activeAppointment);
        }

        AppointmentTableController appointmentTableController = SharedData.INSTANCE.getAppointmentTableController();
        appointmentTableController.refreshTable(true);

        // Active customer is not null since are finished editing it
        SharedData.INSTANCE.setActiveAppointment(null);

        Stage stage = (Stage) typeInput.getScene().getWindow();
        stage.close();
        appointmentTableController.onThisTabSelected();
    }
}