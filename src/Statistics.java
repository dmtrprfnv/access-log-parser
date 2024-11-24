import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Statistics {
    double totalTraffic;
    LocalDateTime minTime, maxTime;

    public Statistics() {
        totalTraffic = 0;
        minTime = LocalDateTime.parse("26/Sep/2100:10:18:06 +0300", DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));
        maxTime = LocalDateTime.parse("26/Sep/1980:10:18:06 +0300", DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));

    }

    void addEntry (LogEntry entry) {
        totalTraffic += entry.responseSize;
        if (entry.date.isAfter(maxTime)) maxTime = entry.date;
        if (entry.date.isBefore(minTime)) minTime = entry.date;
    }

    double getTrafficRate () {
        Duration duration = Duration.between(minTime, maxTime);
        return totalTraffic / duration.toHours();
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "totalTraffic=" + totalTraffic +
                ", minTime=" + minTime +
                ", maxTime=" + maxTime +
                '}';
    }
}
