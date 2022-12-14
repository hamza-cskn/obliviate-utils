package mc.obliviate.util.placeholder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlaceholderUtil {

    private final Set<InternalPlaceholder> placeholders = new HashSet<>();

    /**
     * Registers new placeholder (key, value relation) to
     * the placeholder util.
     *
     * @param key pseudo string, search string - ex: {player-name}
     * @param value result of placeholder, replace string, - ex: Mr_Obliviate
     * @return
     */
    public PlaceholderUtil add(final String key, final String value) {
        if (key == null) throw new IllegalArgumentException("key cannot be null");
        placeholders.add(new InternalPlaceholder(key, value));
        return this;
    }

    public Set<InternalPlaceholder> getPlaceholders() {
        return placeholders;
    }

    /**
     * Adds all placeholders and their values of param placeholder util
     * to this placeholder util.
     *
     * @param placeholderUtil
     */
    public void merge(PlaceholderUtil placeholderUtil) {
        if (placeholderUtil == null) return;
        this.placeholders.addAll(new ArrayList<>(placeholderUtil.placeholders));
    }

    /**
     * Parses placeholders of all String
     * values of list
     *
     * @param texts texts to be parsed
     */
    public List<String> apply(final Iterable<String> texts) {
        if (texts == null) return null;
        final List<String> result = new ArrayList<>();
        for (final String text : texts) {
            result.add(apply(text));
        }
        return result;
    }

    /**
     * Parses placeholders of String
     * value
     *
     * @param text text to be parsed
     */
    public String apply(String text) {
        if (text == null) return null;
        for (final InternalPlaceholder placeholder : placeholders) {
            text = text.replace(placeholder.getPlaceholder(), placeholder.getValue());
        }
        return text;
    }

    public static String apply(String text, PlaceholderUtil placeholderUtil) {
        if (placeholderUtil == null) return text;
        return placeholderUtil.apply(text);
    }

    public static List<String> apply(final Iterable<String> texts, PlaceholderUtil placeholderUtil) {
        if (placeholderUtil == null) {
            if (texts instanceof List) return (List<String>) texts;

            final List<String> list = new ArrayList<>();
            for (String text : texts) list.add(text);
            return list;
        }
        return placeholderUtil.apply(texts);
    }


}
