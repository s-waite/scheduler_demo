package dev.sam.scheduler.model;

public class Country {
    private int countryId;
    private String countryName;

    public Country(int countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }

    // Used in the customer form combo box
    @Override
    public String toString() {
        return this.countryName;
    }

    public int getCountryId() {
        return countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public static Country getCountryFromDivisionId(int divisionId) {
        FirstLevelDivision division = SharedData.INSTANCE.getFirstLevelDivisions().stream()
                .filter(fld -> fld.getDivisionId() == divisionId).toList().get(0);
        return SharedData.INSTANCE.getCountries().stream()
                .filter(c -> c.getCountryId() == division.getCountryId()).toList().get(0);
    }
}
