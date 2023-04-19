package mc.obliviate.util.database.sql;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ColumnValues {

	private Map<String, Object> columnValues = new HashMap<>();

	public void add(String column, Object obj) {
		columnValues.put(column, obj);
	}

	public Map<String, Object> getColumnValues() {
		return Collections.unmodifiableMap(columnValues);
	}
}
