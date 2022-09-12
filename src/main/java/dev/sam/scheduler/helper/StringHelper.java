package dev.sam.scheduler.helper;

public class StringHelper {
    /**
     * Create SQL statement item
     * @param inputString The input string
     * @param noComma Whether there should be a comma at the end
     * @return The statement
     */
    public static String toStatementItem(String inputString, boolean... noComma) {
        if (noComma.length > 0 && noComma[0]) {
            return "\"" + inputString + "\"";
        } else {
            return "\"" + inputString + "\",";
        }
    }
}
