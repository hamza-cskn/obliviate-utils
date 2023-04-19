package mc.obliviate.util.database.sql;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class MySQLProvider implements SQLDriverProvider {

	private static Connection connection = null;

	private final Credentials credentials;

	public MySQLProvider(Credentials credentials) {
		this.credentials = credentials;
	}

	@Override
	public Connection getConnection() {
		return connection;
	}

	@Override
	public boolean connect() {
		connection = null;
		try {

			Class.forName("com.mysql.jdbc.Driver");
			final String URL = credentials.toLink();

			connection = DriverManager.getConnection(URL);
			if (connection == null)
				throw new RuntimeException("Could not connected to MySQL database!");
		} catch (SQLException | ClassNotFoundException | RuntimeException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void disconnect() {
		if (connection == null) return;
		try {
			connection.close();
			connection = null;
		} catch (final SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public static final class Credentials {

		private final String user;
		private final String password;
		private final String ip;
		private final String port;
		private final String database;
		private final String linkFormat;

		public Credentials(String user, String password, String ip, String port, String database, String linkFormat) {
			this.user = Objects.requireNonNull(user, "user could not be null");
			this.password = Objects.requireNonNull(password, "password could not be null");
			this.ip = Objects.requireNonNull(ip, "ip address could not be null");
			this.port = Objects.requireNonNull(port, "port could not be null");
			this.database = Objects.requireNonNull(database, "database name could not be null");
			this.linkFormat = Objects.requireNonNull(linkFormat, "link format could not be null");
		}

		public Credentials(String user, String password, String ip, String port, String database) {
			this(user,
					password,
					ip,
					port,
					database,
					"jdbc:mysql://{ip}:{port}/{database}?user={user}&password={password}"
			);
		}

		public String getUser() {
			return user;
		}

		public String getPassword() {
			return password;
		}

		public String getIp() {
			return ip;
		}

		public String getPort() {
			return port;
		}

		public String getDatabase() {
			return database;
		}

		public String getLinkFormat() {
			return linkFormat;
		}

		public String toLink() {
			return getLinkFormat()
					.replace("{ip}", ip)
					.replace("{port}", port)
					.replace("{database}", database)
					.replace("{user}", encode(user))
					.replace("{password}", encode(password));
		}

		private String encode(String str) {
			try {
				return URLEncoder.encode(str, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				System.out.println("Could not encoded: " + e.getMessage());
				return str;
			}
		}
	}
}
