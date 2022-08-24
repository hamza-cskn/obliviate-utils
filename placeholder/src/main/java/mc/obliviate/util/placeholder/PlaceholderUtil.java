package mc.obliviate.util.placeholder;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderUtil {

	private final List<InternalPlaceholder> placeholders = new ArrayList<>();

	/**
	 *
	 * Registers new placeholder (key, value relation) to
	 * the placeholder util.
	 *
	 * @param key pseudo string, search string - ex: {player-name}
	 * @param value result of placeholder, replace string, - ex: Mr_Obliviate
	 * @return
	 */
	public PlaceholderUtil add(final String key, final String value) {
		placeholders.add(new InternalPlaceholder(key, value));
		return this;
	}

	public List<InternalPlaceholder> getPlaceholders() {
		return placeholders;
	}

	/**
	 * Adds all placeholders and their values of param placeholder util
	 * to this placeholder util.
	 * @param placeholderUtil
	 */
	public void merge(PlaceholderUtil placeholderUtil) {
		this.placeholders.addAll(new ArrayList<>(placeholderUtil.placeholders));
	}

	/**
	 * Parses placeholders of all String
	 * values of list
	 *
	 * @param texts
	 */
	public List<String> apply(final Iterable<String> texts) {
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
	 * @param text
	 */
	public String apply(String text) {
		for (final InternalPlaceholder placeholder : placeholders) {
			text = text.replace(placeholder.getPlaceholder(), placeholder.getValue());
		}
		return text;
	}


}
