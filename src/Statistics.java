import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Statistics {
    private double totalTraffic;
    private int totalVisits, totalErrorRequests;
    private LocalDateTime minTime, maxTime;
    private HashSet<String> urlSuccessSet, urlNotFoundSet, uniqueUsers, uniqueDomains;
    private HashMap<String, Integer> osStatistics, browserStatistics, userVisits;
    private HashMap<Integer, Integer> visitsPerSecond;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = LocalDateTime.parse("26/Sep/2100:10:18:06 +0300", DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));
        this.maxTime = LocalDateTime.parse("26/Sep/1980:10:18:06 +0300", DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH));
        this.urlSuccessSet = new HashSet<>();
        this.urlNotFoundSet = new HashSet<>();
        this.osStatistics = new HashMap<>();
        this.browserStatistics = new HashMap<>();
        this.totalVisits = 0;
        this.totalErrorRequests = 0;
        this.uniqueUsers = new HashSet<>();
        this.visitsPerSecond = new HashMap<>();
        this.uniqueDomains = new HashSet<>();
        this.userVisits = new HashMap<>();
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

        if (!entry.userAgent.isBot()) {
            totalVisits++;
            uniqueUsers.add(entry.ipAddr);

            int seconds = entry.date.getSecond();
            if (visitsPerSecond.containsKey(seconds)) {
                visitsPerSecond.put(seconds, visitsPerSecond.get(seconds) + 1);
            } else {
                visitsPerSecond.put(seconds, 1);
            }

            if (userVisits.containsKey(entry.ipAddr)) {
                userVisits.put(entry.ipAddr, userVisits.get(entry.ipAddr) + 1);
            } else {
                userVisits.put(entry.ipAddr, 1);
            }
        }

        if (entry.referer != null && !entry.referer.isEmpty()) {
            String referrerDomain = extractDomain(entry.referer);
            uniqueDomains.add(referrerDomain);
        }

        if (entry.responseCode >= 400) {
            totalErrorRequests++;
        }

    }

    private String extractDomain(String url) {
        String regex = "^(?:https?:\\/\\/)?([^\\/]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    public Set<String> getUniqueDomains() {
        return uniqueDomains;
    }

    public int getPeakTraffic() {
        return visitsPerSecond.values().stream().mapToInt(Integer::intValue).max().orElse(0);
    }

    public int getPeakVisits() {
        return userVisits.values().stream().mapToInt(Integer::intValue).max().orElse(0);
    }

    public double averageVisitsPerHour() {
        Duration duration = Duration.between(minTime, maxTime);
        return duration.toHours() == 0 ? 0 : (double) totalVisits / duration.toHours();
    }

    public double averageErrorRequestsPerHour() {
        Duration duration = Duration.between(minTime, maxTime);
        return duration.toHours() == 0 ? 0 : (double) totalErrorRequests / duration.toHours();
    }

    public double averageVisitsPerUser () {
        return uniqueUsers.isEmpty() ? 0 : (double) totalVisits / uniqueUsers.size();
    }

    public HashMap<Integer, Integer> getVisitsPerSecond() {
        return visitsPerSecond;
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
