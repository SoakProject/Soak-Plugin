package org.soak.plugin.utils.log;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomLoggerFormat extends Formatter {

    public static Map<String, String> LEVEL_COLOURS;

    static {
        LEVEL_COLOURS = Map.of(
                System.Logger.Level.ERROR.getName(), AnsiEscapeCodes.RED,
                System.Logger.Level.WARNING.getName(), AnsiEscapeCodes.YELLOW,
                System.Logger.Level.DEBUG.getName(), AnsiEscapeCodes.CYAN,
                System.Logger.Level.INFO.getName(), AnsiEscapeCodes.GREEN
        );
    }

    private String formatInt(int value) {
        if (value >= 10) {
            return String.valueOf(value);
        }
        return "0" + value;
    }

    @Override
    public String format(LogRecord record) {
        var time = LocalTime.ofInstant(record.getInstant(), ZoneId.systemDefault());

        String levelColour = LEVEL_COLOURS.getOrDefault(record.getLevel().getName(), AnsiEscapeCodes.RESET);
        return "[" +
                formatInt(time.getHour()) +
                ":" +
                formatInt(time.getMinute()) +
                ":" +
                formatInt(time.getSecond()) +
                "] [" +
                Thread.currentThread().getName() +
                "/" +
                levelColour +
                record.getLevel().getName() +
                AnsiEscapeCodes.RESET +
                "] [" +
                record.getLoggerName() +
                "]: " +
                (record.getLevel().getName().equalsIgnoreCase("info") ? "" : levelColour) +
                record.getMessage() +
                AnsiEscapeCodes.RESET +
                "\n";
    }
}
