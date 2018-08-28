package biz.movia.utils;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.TimeZone;

public class TimeZoneUtils {
    /* Returns null if the timezoneID is invalid */
    public static TimeZone getTimeZone(String timezoneID) {

        final String[] availableTimezoneIDs = TimeZone.getAvailableIDs();

        if ( ! Arrays.asList(availableTimezoneIDs).contains(timezoneID) ) {

            // Unknown timezone ID, maybe a fixed offset timezone id?

            if (timezoneID.equals("Z") ||
                    timezoneID.startsWith("+") || timezoneID.startsWith("-") ||
                    timezoneID.startsWith("UTC") || timezoneID.startsWith("UT") || timezoneID.startsWith("GMT")
                    ) {
                try {
                    return TimeZone.getTimeZone(ZoneId.of(timezoneID));
                } catch (DateTimeException e) {
                    // Invalid fixed-offset timezone id
                    return null;
                }
            } else
                // Not even a fixed offset timezone id
                return null;

        } else
            return TimeZone.getTimeZone(timezoneID);

    }
}
