import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

public class Statistics {
    double totalTraffic;
    LocalDateTime minTime, maxTime;
    HashSet<String> urlSuccessSet;
    HashMap<String, Integer> osStatistics;

    public Statistics() {
        totalTraffic = 0;
        minTime = LocalDateTime.parse("26/Sep/2100:10:18:06 +0300", DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));
        maxTime = LocalDateTime.parse("26/Sep/1980:10:18:06 +0300", DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));
        urlSuccessSet = new HashSet<>();
        osStatistics = new HashMap<>();
    }

    void addEntry (LogEntry entry) {
        totalTraffic += entry.responseSize;
        if (entry.date.isAfter(maxTime)) maxTime = entry.date;
        if (entry.date.isBefore(minTime)) minTime = entry.date;

        if (entry.responseCode == 200) {
            urlSuccessSet.add(entry.path);
        }

        if (osStatistics.containsKey(entry.userAgent.operationSystem)) {
            int count = osStatistics.get(entry.userAgent.operationSystem) + 1;
            osStatistics.put(entry.userAgent.operationSystem, count);
        } else {
            osStatistics.put(entry.userAgent.operationSystem, 1);
        }
    }

    public HashSet<String> getUrlSuccessSet() {
        return urlSuccessSet;
    }

    public Map<String, Integer> getOsStatistics() {
        return osStatistics;
    }

    public Map<String, Double> getOsShares() {
        Map<String, Double> osShares = new HashMap<>();
        int totalEntries = osStatistics.values().stream().mapToInt(Integer::intValue).sum();

        if (totalEntries == 0) {
            return osShares;
        }

        for (Map.Entry<String, Integer> entry : osStatistics.entrySet()) {
            String os = entry.getKey();
            int count = entry.getValue();
            double share = (double) count / totalEntries;
            osShares.put(os, share);
        }

        return osShares;
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
