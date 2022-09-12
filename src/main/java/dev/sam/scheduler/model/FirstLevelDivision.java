package dev.sam.scheduler.model;

/**
 * The first level division database entity
 */
public class FirstLevelDivision {
    private int divisionId;
    private String division;
    private int countryId;


    public FirstLevelDivision(int divisionId, String division, int countryId) {
        this.divisionId = divisionId;
        this.division = division;
        this.countryId = countryId;
    }

    @Override
    public String toString() {
       return this.getDivision();
    }

    public int getDivisionId() {
        return divisionId;
    }

    public String getDivision() {
        return division;
    }

    public int getCountryId() {
        return countryId;
    }

    public static FirstLevelDivision getDivisionFromId(int divisionId) {
        return SharedData.INSTANCE.getFirstLevelDivisions().stream()
                .filter(fld -> fld.getDivisionId() == divisionId).toList().get(0);
    }

}
