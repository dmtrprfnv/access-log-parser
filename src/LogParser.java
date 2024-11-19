import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {
    String ipAddress, date, method, httpVersion, statusCode, contentLength, referrer, userAgent;

    public LogParser(String logRow) {
        Pattern pattern = Pattern.compile("^(?<ip>\\d{1,3}(?:\\.\\d{1,3}){3})( - - | )\\[(?<date>[^\\]]+)\\] \"(?<method>[A-Z]+) (?<url>[^\\s]+) HTTP\\/(?<httpVersion>[\\d.]+)\" (?<statusCode>\\d{3}) (?<contentLength>\\d+) \"(?<referrer>[^\"]*)\" \"(?<userAgent>[^\"]*)\"$");
        Matcher matcher = pattern.matcher(logRow);
        matcher.matches();

        this.ipAddress = matcher.group("ip");
        this.date = matcher.group("date");
        this.method = matcher.group("method");
        this.httpVersion = matcher.group("httpVersion");
        this.statusCode = matcher.group("statusCode");
        this.contentLength = matcher.group("contentLength");
        this.referrer = matcher.group("referrer");
        this.userAgent = matcher.group("userAgent");
    }

    @Override
    public String toString() {
        return "LogParser{" +
                "ipAddress='" + ipAddress + '\'' +
                ", date='" + date + '\'' +
                ", method='" + method + '\'' +
                ", httpVersion='" + httpVersion + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", contentLength='" + contentLength + '\'' +
                ", referrer='" + referrer + '\'' +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }
}
