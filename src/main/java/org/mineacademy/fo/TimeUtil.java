package org.mineacademy.fo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.mineacademy.fo.exception.FoException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.fo.model.SimpleReplacer;

/**
 * Utility class for calculating time from ticks and back.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeUtil {

	/**
	 * The date format in dd.MM.yyy HH:mm:ss
	 */
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	/**
	 * The date format in dd.MM.yyy HH:mm
	 */
	private static final DateFormat DATE_FORMAT_SHORT = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    private static String SECONDS_SYMBOL = "seconds", SECOND_SYMBOL = "second";
    private static String MINUTES_SYMBOL = "minutes ",MINUTE_SYMBOL = "minute";

    // ------------------------------------------------------------------------------------------------------------
	// Current time
	// ------------------------------------------------------------------------------------------------------------

	/**
	 * Seconds elapsed since January the 1st, 1970
	 *
	 * @return System.currentTimeMillis / 1000
	 */
	public static long currentTimeSeconds() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * Ticks elapsed since January the 1st, 1970
	 *
	 * @return System.currentTimeMillis / 50
	 */
	public static long currentTimeTicks() {
		return System.currentTimeMillis() / 50;
	}

	// ------------------------------------------------------------------------------------------------------------
	// Formatting
	// ------------------------------------------------------------------------------------------------------------

	/**
	 * Return the current date formatted as DAY.MONTH.YEAR HOUR:MINUTES:SECONDS
	 *
	 * @return
	 */
	public static String getFormattedDate() {
		return getFormattedDate(System.currentTimeMillis());
	}

	/**
	 * Return the given date in millis formatted as
	 * DAY.MONTH.YEAR HOUR:MINUTES:SECONDS
	 *
	 * @param time
	 * @return
	 */
	public static String getFormattedDate(final long time) {
		return DATE_FORMAT.format(time);
	}

	/**
	 * Return the current date formatted as DAY.MONTH.YEAR HOUR:MINUTES
	 *
	 * @return
	 */
	public static String getFormattedDateShort() {
		return DATE_FORMAT_SHORT.format(System.currentTimeMillis());
	}

	/**
	 * Return the given date in millis formatted as
	 * DAY.MONTH.YEAR HOUR:MINUTES
	 *
	 * @param time
	 * @return
	 */
	public static String getFormattedDateShort(final long time) {
		return DATE_FORMAT_SHORT.format(time);
	}

	// ------------------------------------------------------------------------------------------------------------
	// Converting
	// ------------------------------------------------------------------------------------------------------------

	/**
	 * Converts the time from a human readable format like "10 minutes"
	 * to seconds.
	 *
	 * @param humanReadableTime the human readable time format: {time} {period}
	 * 		  	   example: 5 seconds, 10 ticks, 7 minutes, 12 hours etc..
	 *
	 * @return the converted human time to seconds
	 */
	public static long toTicks(final String humanReadableTime) {
		Valid.checkNotNull(humanReadableTime, "Time is null");

		long seconds = 0L;

		final String[] split = humanReadableTime.split(" ");

		for (int i = 1; i < split.length; i++) {
			final String sub = split[i].toLowerCase();
			int multiplier = 0; // e.g 2 hours = 2
			long unit = 0; // e.g hours = 3600
			boolean isTicks = false;

			try {
				multiplier = Integer.parseInt(split[i - 1]);
			} catch (final NumberFormatException e) {
				continue;
			}

			// attempt to match the unit time
			if (sub.startsWith("tick"))
				isTicks = true;

			else if (sub.startsWith("second"))
				unit = 1;

			else if (sub.startsWith("minute"))
				unit = 60;

			else if (sub.startsWith("hour"))
				unit = 3600;

			else if (sub.startsWith("day"))
				unit = 86400;

			else if (sub.startsWith("week"))
				unit = 604800;

			else if (sub.startsWith("month"))
				unit = 2629743;

			else if (sub.startsWith("year"))
				unit = 31556926;
			else
				throw new FoException("Must define date type! Example: '1 second' (Got '" + sub + "')");

			seconds += multiplier * (isTicks ? 1 : unit * 20);
		}

		return seconds;
	}

	/**
	 * Formats the given time from seconds into the following format:
	 *
	 * "1 hour 50 minutes 10 seconds" or similar, or less
	 *
	 * @param seconds
	 * @return
	 */
	public static String formatTimeGeneric(final long seconds) {
		final long second = seconds % 60;
		long minute = seconds / 60;
		String hourMsg = "";

		if (minute >= 60) {
			final long hour = seconds / 60 / 60;
			minute %= 60;

			hourMsg = hour + (hour == 1 ? " hour" : " hours") + " ";
		}

		return hourMsg + (minute != 0 ? minute : "") + (minute > 0 ? (minute == 1 ? " minute" : " minutes") + " " : "") + Long.parseLong(String.valueOf(second)) + (Long.parseLong(String.valueOf(second)) == 1 ? " second" : " seconds");

	}

	/**
	 * Format time in "X days Y hours Z minutes Å seconds"
	 *
	 * @param seconds
	 * @return
	 */
	public static String formatTimeDays(final long seconds) {
		final long minutes = seconds / 60;
		final long hours = minutes / 60;
		final long days = hours / 24;

		return days + " days " + hours % 24 + " hours " + minutes % 60 + " minutes " + seconds % 60 + " seconds";
	}

	/**
	 * Format the time in seconds, for example: 10d 5h 10m 20s or just 5m 10s
	 * If the seconds are zero, an output 0s is given
	 *
	 * @param seconds
	 * @return
	 */
	public static String formatTimeShort(long seconds) {
		long minutes = seconds / 60;
		long hours = minutes / 60;
		final long days = hours / 24;

		hours = hours % 24;
		minutes = minutes % 60;
		seconds = seconds % 60;

		return (days > 0 ? days + "d " : "") + (hours > 0 ? hours + "h " : "") + (minutes > 0 ? minutes + "m " : "") + seconds + "s";
	}


    public static SimpleReplacer getTimeReplacer(String string, int seconds) {
        return SimpleReplacer.from(string)
                .replace("{seconds}", seconds)
                .replace("{time}", formatTime(seconds))
                .replace("{fancy-time}", formatTimeFancy(seconds));
    }

    public static String replacePlaceholders(String string, int seconds) {
        return getTimeReplacer(string, seconds).getMessages();
    }

    public static String formatTime(int duration) {
        String f = "0";
        if (duration < 0) {
            duration = 0;
        }
        if (duration >= 3600 && duration < 7200) {
            duration = duration - 3600;
            int ms = duration / 60;
            int ss = duration % 60;
            String m = (ms < 10 ? "0" : "") + ms;
            String s = (ss < 10 ? "0" : "") + ss;
            f = "1:" + m + ":" + s;
        } else if (duration >= 7200 && duration < 10800) {
            duration = duration - 7200;
            int ms = duration / 60;
            int ss = duration % 60;
            String m = (ms < 10 ? "0" : "") + ms;
            String s = (ss < 10 ? "0" : "") + ss;
            f = "2:" + m + ":" + s;
        } else if (duration >= 10800 && duration < 14400) {
            duration = duration - 10800;
            int ms = duration / 60;
            int ss = duration % 60;
            String m = (ms < 10 ? "0" : "") + ms;
            String s = (ss < 10 ? "0" : "") + ss;
            f = "3:" + m + ":" + s;
        } else {
            int ms = duration / 60;
            int ss = duration % 60;
            String m = (ms < 10 ? "0" : "") + ms;
            String s = (ss < 10 ? "0" : "") + ss;
            f = m + ":" + s;
        }

        return f;
    }

    public static int parseToSeconds(String hoursDuration) {
        int[] array = getIntArr(hoursDuration.split(":"));

        if (array.length > 3)
            throw new RuntimeException("Error while converting duration, this can only convert max to hours.");

        int totalSeconds = 0;
        int multiple = 1;

        for (int i = array.length - 1; i >= 0; i--) {
            totalSeconds += (array[i] * multiple);

            multiple *= 60;
        }

        if (totalSeconds <= 0)
            throw new RuntimeException("Error while converting duration, result < 0 after converted");

        return totalSeconds;
    }

    public static String formatTimeFancy(int time) {
        StringBuilder builder = new StringBuilder();

        int minutes = time / 60;
        int seconds = time % 60;

        String secondWord = seconds > 1 ? SECONDS_SYMBOL : SECOND_SYMBOL;
        String minuteWord = minutes > 1 ? MINUTES_SYMBOL : MINUTE_SYMBOL;

        if (minutes == 0)
            builder.append(seconds)
                    .append(secondWord);
        else {
            builder.append(minutes).append(minuteWord);

            if (seconds != 0)
                builder.append(seconds).append(secondWord);
        }

        return builder.toString();
    }

    private static int[] getIntArr(String[] stringArr) {
        int[] intArr = new int[stringArr.length];

        for (int i = 0; i < stringArr.length; i++)
            intArr[i] = Integer.valueOf(stringArr[i]);

        return intArr;
    }
}
