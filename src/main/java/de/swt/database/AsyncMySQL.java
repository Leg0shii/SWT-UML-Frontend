package de.swt.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 *
 * @author Sandro Hahn (xShadoow)
 * @version 0.2.1
 * @category MySQL Connection Utils
 *
 */
public class AsyncMySQL {

    private ExecutorService executor;
    private MySQL sql;

    /**
     * Called when a new AsyncMySQL object is created.
     * Tries to establish connection immediately.
     * @param host ip address of mysql server
     * @param port port of mysql server (usually 3306)
     * @param user username (usually "root")
     * @param password password of username
     * @param database de.swt.database to connect to
     */
    public AsyncMySQL(String host, int port, String user, String password, String database) throws SQLException, ClassNotFoundException {

        sql = new MySQL(host, port, user, password, database);
        executor = Executors.newCachedThreadPool();

    }

    /**
     * Executes a mysql update asynchronous using a PreparedStatement object.
     * @param statement sql update as PreparedStatement object
     */
    public void update(PreparedStatement statement) {
        executor.execute(() -> sql.queryUpdate(statement));
    }

    /**
     * Executes a mysql update asynchronous using a String object.
     * String argument gets converted to a PreparedStatement object automatically.
     * @param statement sql update as a String object
     */
    public void update(String statement) {
        executor.execute(() -> sql.queryUpdate(statement));
    }

    /**
     * Can be used to insert a new record into the de.swt.database asynchronous if "auto increment"
     * is turned on for the "ID" column.
     * The returned integer in the consumer is the id that got generated.
     * @param insert sql insert query as a String
     * @param consumer consumer object that returns the generated id
     */
    public void insert(String insert, Consumer<Integer> consumer) {

        executor.execute(() -> {

            try {

                String[] returnId = {"ID"};
                PreparedStatement statement = sql.conn.prepareStatement(insert, returnId);
                statement.executeUpdate();
                ResultSet rs = statement.getGeneratedKeys();

                if (rs.next()) {

                    Integer integer = rs.getInt(1);
                    consumer.accept(integer);

                }

                rs.close();

            } catch(SQLException ignored) { }

        });

    }

    /**
     * Executes a sql query asynchronous using a PreparedStatement object.
     * The consumer object returns the ResultSet of the query.
     * @param statement sql query as PreparedStatement object
     * @param consumer consumer object that returns the ResultSet of the query
     */
    public void query(PreparedStatement statement, Consumer<ResultSet> consumer) {
        executor.execute(() -> {
            ResultSet result = sql.query(statement);
            consumer.accept(result);
        });
    }

    /**
     * Executes a sql query asynchronous using a String object.
     * String object gets converted to a PreparedStatement automatically.
     * The consumer object returns the ResultSet of the query.
     * @param statement sql query as a String object
     */
    public ResultSet query(String statement) {
        return sql.query(statement);
    }

    /**
     * Used to convert a String object query/update to a PreparedStatement object
     * @param query sql query/update as a String object
     * @return the query as a PreparedStatement object
     */
    public PreparedStatement prepare(String query) {

        try {

            return sql.getConnection().prepareStatement(query);

        } catch(SQLException ignored) { ignored.printStackTrace(); }

        return null;
    }

    /**
     * Getter for inner mysql class
     * @return the inner mysql class
     */
    public MySQL getMySQL() {
        return sql;
    }

    public static class MySQL {

        private String host;
        private String user;
        private String password;
        private String database;
        private int port;

        private Connection conn;

        public MySQL(String host, int port, String user, String password, String database) throws SQLException, ClassNotFoundException {

            this.host = host;
            this.port = port;
            this.user = user;
            this.password = password;
            this.database = database;

            this.openConnection();

        }

        /**
         * Called when AsyncMySQL object is created. Establishes mysql connection
         * using the given login credentials.
         * @return connection object
         * @return null if no connection was made
         */
        public void openConnection() throws SQLException, ClassNotFoundException {

            if (conn != null && !conn.isClosed()) {
                return;
            }

            synchronized (this) {
                if (conn != null && !conn.isClosed()) {
                    return;
                }
                //Class.forName("com.mysql.jdbc.Driver");
                this.conn = DriverManager.getConnection("jdbc:mysql://" + this.host+ ":" + this.port + "/" + this.database + "?useSSL=false&autoReconnect=true&serverTimezone=UTC", this.user, this.password);
            }

        }

        /**
         * Call this function when the plugin gets disabled.
         */
        public void closeConnection() {

            try {

                this.conn.close();

            } catch (SQLException ignored) { ignored.printStackTrace();} finally {

                this.conn = null;

            }

        }

        /**
         * Checks if connection is established.
         * @return true if connected to mysql server
         * @return false if not connected to mysql server
         */
        public boolean isConnected() {

            try {

                if((this.conn == null) || (!this.conn.isValid(10)) || (this.conn.isClosed())) {

                    return false;
                } else {

                    return true;
                }

            } catch (SQLException ignored) { ignored.printStackTrace(); }

            return false;
        }

        /**
         * Checks if connection is established. If reconnect is true it will try
         * to reconnect if connection isnt established.
         * @param reconnect whether it should try to reconnect
         * @return true if connected to mysql server
         * @return false if not connected to mysql server
         */
        public boolean isConnected(boolean reconnect) {

            try {

                if((this.conn == null) || (!this.conn.isValid(10)) || (this.conn.isClosed())) {

                    reconnect();

                    return false;
                } else {

                    return true;
                }

            } catch (SQLException | ClassNotFoundException ignored) { ignored.printStackTrace(); }

            return false;
        }

        /**
         * Call this to reconnect to the mysql server.
         * @return true if successfully reconnected or already connected
         * @return false if failed to reconnect
         */
        public boolean reconnect() throws SQLException, ClassNotFoundException {

            if(!isConnected()) {

                openConnection();
                return isConnected();

            } else return true;

        }

        /**
         * Getter for Connection object
         * @return connection object
         */
        public Connection getConnection() {
            return this.conn;
        }

        /**
         * Executes a mysql update SYNCHRONOUS (Use with caution!) using a String object.
         * String argument gets converted to a PreparedStatement object automatically.
         * @param query sql update as a String object
         */
        public void queryUpdate(String query) {

            try(PreparedStatement statement = conn.prepareStatement(query)) {

                queryUpdate(statement);

            } catch(SQLException ignored) { ignored.printStackTrace(); }

        }

        /**
         * Executes a mysql update SYNCHRONOUS (Use with caution!) using a PreparedStatement object.
         * @param statement sql update as a PreparedStatement object
         */
        public void queryUpdate(PreparedStatement statement) {

            if(!isConnected()) {
                return;
            }

            try {

                statement.executeUpdate();

            } catch (SQLException ignored) { ignored.printStackTrace(); }

        }

        /**
         * Executes a sql query SYNCHRONOUS (Use with caution!) using a String object.
         * String object gets converted to a PreparedStatement automatically.
         * @param query sql query as a String object
         * @return the ResultSet of the query
         */
        public ResultSet query(String query) {

            try {

                return query(conn.prepareStatement(query));

            } catch(SQLException ignored) { ignored.printStackTrace(); }

            return null;
        }

        /**
         * Executes a sql query SYNCHRONOUS (Use with caution!) using a PreparedStatement object.
         * @param statement sql query as a PreparedStatement object
         * @return the ResultSet of the query
         */
        public ResultSet query(PreparedStatement statement) {

            if(!isConnected()) {
                return null;
            }

            try {

                return statement.executeQuery();

            } catch(SQLException ignored) { ignored.printStackTrace(); }

            return null;
        }

    }

}
