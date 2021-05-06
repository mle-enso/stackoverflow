package de.mle.stackoverflow.java;

public class Java14Features {
    static boolean isWorkday(String day){
        return switch (day) {
            case "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY" -> true;
            case "SATURDAY", "SUNDAY" -> false;
            default -> throw new IllegalArgumentException("What's a " + day);
        };
    }
}