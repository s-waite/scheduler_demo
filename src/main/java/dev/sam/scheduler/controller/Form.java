package dev.sam.scheduler.controller;

import java.sql.SQLException;
import java.util.ArrayList;

public abstract class Form {
    enum ValidationCode {
        NAME_ERR,
        ADDRESS_ERR,
        POSTAL_ERR,
        PHONE_ERR,
        COUNTRY_ERR,
        FIRST_DIV_ERR,
        EMPTY_FIELDS_ERR,
        USER_ID_NOT_FOUND_ERR, CUSTOMER_ID_NOT_FOUND_ERR, START_DATE_BEFORE_END_ERR, TIME_ERR, OUTSIDE_BUSINESS_HOURS_ERR, START_TIME_OUTSIDE_HRS_ERR, END_TIME_OUTSIDE_HRS_ERR, CONFLICTING_APP_ERR, OK
    }

    abstract ArrayList<ValidationCode> validateForm();

    abstract void saveForm() throws SQLException;
}
