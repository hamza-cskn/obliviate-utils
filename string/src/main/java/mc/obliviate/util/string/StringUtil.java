package mc.obliviate.util.string;

import mc.obliviate.util.versiondetection.ServerVersionController;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    private static final Pattern HEX_PATTERN = Pattern.compile("<#[a-fA-F\\d]{6}>");

    public static List<String> listReplace(final List<String> stringList, final String search, final String replace) {
        if (stringList == null) return null;
        final List<String> result = new ArrayList<>();
        for (String str : stringList) {
            result.add(str.replace(search, replace));
        }
        return result;
    }

    public static String parseColor(String string) {
        if (string == null) return null;
        string = ChatColor.translateAlternateColorCodes('&', string);
        if (ServerVersionController.isServerVersionAtLeast(ServerVersionController.V1_16)) {
            Matcher matcher = HEX_PATTERN.matcher(string);
            while (matcher.find()) {
                final ChatColor hexColor = ChatColor.of(matcher.group().substring(1, matcher.group().length() - 1));
                final String before = string.substring(0, matcher.start());
                final String after = string.substring(matcher.end());
                string = before + hexColor + after;
                matcher = HEX_PATTERN.matcher(string);
            }
        }
        return string;
    }

    public static List<String> parseColor(final List<String> stringList) {
        if (stringList == null) return null;
        final List<String> result = new ArrayList<>();
        for (String str : stringList) {
            result.add(parseColor(str));
        }
        return result;
    }

    public static int getPercentage(final double total, final double progress) {
        try {
            return (int) (progress / (total / 100d));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static String getProgressBar(final int completed, final int total, final String got, final String missing) {
        final StringBuilder points = new StringBuilder();
        for (int i = 0; i + 1 <= total; i++) {
            if (i >= completed) {
                points.append(missing);
            } else {
                points.append(got);
            }
        }
        return points.toString();
    }

    public static double getFirstDigits(final double number, final int digitAmount) {
        final double multiple = Math.pow(10, digitAmount);
        return (((int) (number * multiple)) / multiple);
    }
}
