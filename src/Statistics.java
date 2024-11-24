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
    HashSet<String> urlSuccessSet, urlNotFoundSet;
    HashMap<String, Integer> osStatistics, browserStatistics;

    public Statistics() {
        totalTraffic = 0;
        minTime = LocalDateTime.parse("26/Sep/2100:10:18:06 +0300", DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));
        maxTime = LocalDateTime.parse("26/Sep/1980:10:18:06 +0300", DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));
        urlSuccessSet = new HashSet<>();
        urlNotFoundSet = new HashSet<>();
        osStatistics = new HashMap<>();
        browserStatistics = new HashMap<>();
    }

    void addEntry (LogEntry entry) {
        totalTraffic += entry.responseSize;
        if (entry.date.isAfter(maxTime)) maxTime = entry.date;
        if (entry.date.isBefore(minTime)) minTime = entry.date;

        if (entry.responseCode == 200) {
            urlSuccessSet.add(entry.path);
        }

        if (entry.responseCode == 404) {
            urlNotFoundSet.add(entry.path);
        }

        if (osStatistics.containsKey(entry.userAgent.operationSystem)) {
            int osCount = osStatistics.get(entry.userAgent.operationSystem) + 1;
            osStatistics.put(entry.userAgent.operationSystem, osCount);
        } else {
            osStatistics.put(entry.userAgent.operationSystem, 1);
        }

        if (browserStatistics.containsKey(entry.userAgent.browser)) {
            int browserCount = browserStatistics.get(entry.userAgent.browser) + 1;
            browserStatistics.put(entry.userAgent.browser, browserCount);
        } else {
            browserStatistics.put(entry.userAgent.browser, 1);
        }
    }

    public HashSet<String> getUrlSuccessSet() {
        return urlSuccessSet;
    }

    public HashSet<String> getUrlNotFoundSet() {
        return urlNotFoundSet;
    }

    public Map<String, Integer> getOsStatistics() {
        return osStatistics;
    }

    public Map<String, Integer> getBrowserStatistics() {
        return browserStatistics;
    }

    public Map<String, Double> getOsShares() {
        Map<String, Double> osShares = new HashMap<>();
        int totalEntries = osStatistics.values().stream().mapToInt(Integer::intValue).sum();

        if (totalEntries == 0) {
            return osShares;
        }

        for (Map.Entry<String, Integer> entry : osStatistics.entrySet()) {
            String os = entry.getKey();
            int osCount = entry.getValue();
            double osShare = (double) osCount / totalEntries;
            osShares.put(os, osShare);
        }

        return osShares;
    }

    public Map<String, Double> getBrowserShares() {
        Map<String, Double> browserShares = new HashMap<>();
        int totalEntries = browserStatistics.values().stream().mapToInt(Integer::intValue).sum();
        if (totalEntries == 0) {
            return browserShares;
        }

        for (Map.Entry<String, Integer> entry : browserStatistics.entrySet()) {
            String browser = entry.getKey();
            int browserCount = entry.getValue();
            double browserShare = (double) browserStatistics.get(browser) / totalEntries;
            browserShares.put(browser, browserShare);
        }

        return browserShares;
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
