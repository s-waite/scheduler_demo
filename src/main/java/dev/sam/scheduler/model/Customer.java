package dev.sam.scheduler.model;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

public class Customer {
    private Integer id;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private OffsetDateTime creationDate;
    private String createdBy;
    private OffsetDateTime lastUpdatedDate;
    private String lastUpdatedBy;
    private Integer divisionId;

    public Customer(Integer id, String name, String address, String postalCode, String phone, OffsetDateTime creationDate, String createdBy, OffsetDateTime lastUpdatedDate, String lastUpdatedBy, Integer divisionId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.creationDate = creationDate;
        this.createdBy = createdBy;
        this.lastUpdatedDate = lastUpdatedDate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionId = divisionId;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public OffsetDateTime getCreationDate() {
        return creationDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public OffsetDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public Integer getDivisionId() {
        return divisionId;
    }
}
