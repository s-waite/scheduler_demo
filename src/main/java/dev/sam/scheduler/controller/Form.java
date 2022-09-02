package dev.sam.scheduler.controller;

import java.sql.SQLException;
import java.util.ArrayList;

public interface Form {
    enum ValidationCode {
        NAME_ERR,
        ADDRESS_ERR,
        POSTAL_ERR,
        PHONE_ERR,
        COUNTRY_ERR,
        FIRST_DIV_ERR,
        OK
    }

    ArrayList<ValidationCode> validateForm();

    void saveForm() throws SQLException;


}
