import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAgent {
    final String userAgent, operationSystem, browser;

    public UserAgent(String agent) {

        Pattern patternOS = Pattern.compile("\\(.*?\\)");
        Matcher matcherOS = patternOS.matcher(agent);

        if (matcherOS.find()
                && (matcherOS.group(0).split(";")[0].trim().equals("(Windows")
                || matcherOS.group(0).split(";")[0].trim().equals("(Macintosh")
                || matcherOS.group(0).split(";")[0].trim().equals("(Linux"))) {
            this.operationSystem = matcherOS.group(0).split(";")[0].replace("(","").split(" ")[0];
        }
        else {
            this.operationSystem = null;
        }

        Pattern patternBrowserFirefox = Pattern.compile(".*(?:Firefox).*");
        Matcher matcherBrowserFirefox = patternBrowserFirefox.matcher(agent);

        Pattern patternBrowserCommon = Pattern.compile(".*(KHTML).*(Chrome).*(Safari).*");
        Matcher matcherBrowserCommon = patternBrowserCommon.matcher(agent);

        Pattern patternBrowserEdge = Pattern.compile(".*(?:Edge).*");
        Matcher matcherBrowserEdge = patternBrowserEdge.matcher(agent);

        Pattern patternBrowserSafari = Pattern.compile(".*(?:Mobile).*");
        Matcher matcherBrowserSafari = patternBrowserSafari.matcher(agent);

        Pattern patternBrowserOpera = Pattern.compile(".*(?:OPR|Presto).*");
        Matcher matcherBrowserOpera = patternBrowserOpera.matcher(agent);

        if (matcherBrowserFirefox.find()) {this.browser = "Firefox";}
        else if (matcherBrowserCommon.find()) {
            if (matcherBrowserEdge.find()) {this.browser = "Edge";}
            else if (matcherBrowserSafari.find()) {this.browser = "Safari";}
            else if (matcherBrowserOpera.find()) {this.browser = "Opera";}
            else {this.browser = "Chrome";}
        }
        else {this.browser = "Unknown";}
        this.userAgent = agent;
    }

    public String getOperationSystem() {
        return operationSystem;
    }

    public String getBrowser() {
        return browser;
    }

    public String getUserAgent() {
        return userAgent;
    }

    @Override
    public String toString() {
        return "UserAgent{" +
                "userAgent='" + userAgent + '\'' +
                ", operationSystem='" + operationSystem + '\'' +
                ", browser='" + browser + '\'' +
                '}';
    }
}
