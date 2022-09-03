package dev.sam.scheduler.helper;

public class StringHelper {
    public static String toStatementItem(String inputString, boolean... noComma) {
        if (noComma.length > 0 && noComma[0]) {
            return "\"" + inputString + "\"";
        } else {
            return "\"" + inputString + "\",";
        }
    }
}
