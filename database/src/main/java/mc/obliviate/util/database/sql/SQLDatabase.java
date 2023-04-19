package mc.obliviate.util.database.sql;

import mc.obliviate.util.database.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Defines all SQL databases
 * provides serialize/deserialize and update/query command generator methods.
 *
 * @param <T> the object of database
 */
public abstract class SQLDatabase<T> implements Database<T> {

	private final SQLDriverProvider provider;

	protected SQLDatabase(SQLDriverProvider provider) {
		this.provider = provider;
	}

	/* column, value */
	public abstract List<ColumnValues> serialize(T object);

	/**
	 * @return null, if result set has no next position(when {@code ResulSet#next()} returns false). otherwise is non-null.
	 */
	public abstract T deserialize(ResultSet object) throws SQLException;

	@Override
	public void save(T object) {
		if (object == null)
			throw new NullPointerException("object could not save because it was null!");

		final String id = getId(object);
		final List<ColumnValues> columns = serialize(object);
		final boolean exist = exist(queryString(id));

		if (exist) {
			update(SQLDatabase.getUpdateCommand(getTable(), getIdColumn(), id, columns));
		} else {
			update(SQLDatabase.getInsertCommand(getTable(), columns));
		}
	}

	@Override
	public T load(Object id) {
		if (id == null)
			throw new NullPointerException("id cannot be null");

		ResultSet rs = query(queryString(id));
		try {
			return deserialize(rs);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<T> loadAll() {
		ResultSet rs = query("SELECT * FROM " + getTable());
		List<T> result = new ArrayList<>();
		try {
			while (true) {
				T obj = deserialize(rs);
				if (obj == null) return result;
				result.add(obj);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void update(List<String> sqls) {
		sqls.forEach(this::update);
	}

	public void update(String sql) {
		try {
			System.out.println(sql);
			Statement statement = provider.getConnection().createStatement();
			statement.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void query(List<String> sqls) {
		sqls.forEach(this::query);
	}

	public ResultSet query(String sql) {
		try {
			System.out.println(sql);
			Statement statement = provider.getConnection().createStatement();
			return statement.executeQuery(sql);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private boolean exist(String sql) {
		ResultSet rs = query(sql);
		try {
			return rs.next();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public String queryString(Object id) {
		if (id == null)
			throw new NullPointerException("id cannot be null");
		return "SELECT * FROM " + getTable() + " WHERE " + getIdColumn() + "='" + id + "'";
	}

	public abstract String getId(T object);

	public abstract String getIdColumn();

	public abstract String getTable();

	public static List<String> getUpdateCommand(final String table, final String where, final Object whereValue, final List<ColumnValues> columns) {
		return columns.stream().map(col -> getUpdateCommand(table, where, whereValue, col)).collect(Collectors.toList());
	}

	public static String getUpdateCommand(final String table, final String where, final Object whereValue, final ColumnValues columns) {
		final StringBuilder stringBuilder = (new StringBuilder("UPDATE ")).append(table).append(" SET ");
		int i = 0;
		for (String str : columns.getColumnValues().keySet()) {
			i++;
			stringBuilder.append(str).append(" = ").append("'").append(columns.getColumnValues().get(str)).append("'");
			if (i != columns.getColumnValues().size()) stringBuilder.append(", ");
		}
		stringBuilder.append(" WHERE ").append(where).append(" = '").append(whereValue.toString()).append("'");

		return stringBuilder.toString();
	}

	public static List<String> getInsertCommand(final String table, final List<ColumnValues> columns) {
		return columns.stream().map(col -> getInsertCommand(table, col)).collect(Collectors.toList());
	}

	public static String getInsertCommand(final String table, final ColumnValues columns) {
		final StringBuilder stringBuilder = (new StringBuilder("INSERT INTO ")).append(table).append(" VALUES(");
		int i = 0;
		for (Object obj : columns.getColumnValues().values()) {
			i++;
			if (obj instanceof Number)
				stringBuilder.append(obj);
			else
				stringBuilder.append("'").append(obj).append("'");
			if (i != columns.getColumnValues().size()) stringBuilder.append(", ");
		}
		stringBuilder.append(")");

		return stringBuilder.toString();
	}

	public SQLDriverProvider getProvider() {
		return provider;
	}
}
