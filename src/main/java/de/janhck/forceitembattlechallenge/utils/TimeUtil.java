package de.janhck.forceitembattlechallenge.utils;

public class TimeUtil {

    public static String formatSeconds(int timeInSeconds) {
        int seconds = timeInSeconds % 60;
        int minutes = (timeInSeconds / 60) % 60;
        int hours = timeInSeconds / 60 / 60;
        return hours + "h " + minutes + "m " + seconds + "s";
    }

}
