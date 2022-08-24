package mc.obliviate.util.placeholder;

import java.util.Objects;

public class InternalPlaceholder {

	private static final String DEFAULT_VALUE = "";
	private final String placeholder;
	private final String value;

	protected InternalPlaceholder(final String placeholder, final String value) {
		this.placeholder = placeholder;
		this.value = value;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public String getValue() {
		if (value == null) return DEFAULT_VALUE;
		return value;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (object == null || getClass() != object.getClass()) return false;
		InternalPlaceholder that = (InternalPlaceholder) object;
		return Objects.equals(placeholder, that.placeholder) && Objects.equals(value, that.value);
	}
}
