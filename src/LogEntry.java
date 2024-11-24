import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {
    final String ipAddr, path, referer;
    final UserAgent userAgent;
    final LocalDateTime date;
    final HttpMethod method;
    final Integer responseCode, responseSize;

    public LogEntry(String logRow) {

        Pattern pattern = Pattern.compile("^(?<ip>\\d{1,3}(?:\\.\\d{1,3}){3})( - - | )\\[(?<date>[^\\]]+)\\] \"(?<method>[A-Z]+) (?<url>[^\\s]+) HTTP\\/(?<httpVersion>[\\d.]+)\" (?<statusCode>\\d{3}) (?<contentLength>\\d+) \"(?<referrer>[^\"]*)\" \"(?<userAgent>[^\"]*)\"$");
        Matcher matcher = pattern.matcher(logRow);
        matcher.matches();


        this.ipAddr = matcher.group("ip");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
        this.date = LocalDateTime.parse(matcher.group("date"), formatter);
        this.method = HttpMethod.valueOf(matcher.group("method"));

        this.path = matcher.group("url");

        this.responseCode = Integer.parseInt(matcher.group("statusCode"));
        this.responseSize = Integer.parseInt(matcher.group("contentLength"));
        this.referer = matcher.group("referrer");
        this.userAgent = new UserAgent(matcher.group("userAgent"));
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public String getPath() {
        return path;
    }

    public String getReferer() {
        return referer;
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public Integer getResponseSize() {
        return responseSize;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "ipAddr='" + ipAddr + '\'' +
                ", path='" + path + '\'' +
                ", referer='" + referer + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", date=" + date +
                ", method=" + method +
                ", responseCode=" + responseCode +
                ", responseSize=" + responseSize +
                '}';
    }
}

enum HttpMethod {
    GET, POST, PUT, DELETE;
}