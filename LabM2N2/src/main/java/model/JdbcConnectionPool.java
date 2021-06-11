package model;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;


public class JdbcConnectionPool {
    private static JdbcConnectionPool instance = null;

    private final int size;
    private final DataSource dataSource;
    private final LinkedList<Connection> freeConnections;

    public static JdbcConnectionPool getInstance() throws SQLException {
        if (instance == null) {
            instance = new JdbcConnectionPool(Integer.parseInt(System.getenv("CONNECTION_POOL_SIZE")));
        }

        return instance;
    }

    private JdbcConnectionPool(int size) throws SQLException {
        this.dataSource = this.getDataSource();
        this.size = size;

        this.freeConnections = new LinkedList<>();
        for (int i = 0; i < this.getSize(); ++i) {
            this.freeConnections.add(this.createConnection());
        }

    }

    private DataSource getDataSource() {
        var pgDataSource = new PGSimpleDataSource();

        pgDataSource.setUrl(String.format(
                "jdbc:postgresql://%s/%s",
                System.getenv("POSTGRES_HOST"), System.getenv("POSTGRES_DATABASE")
        ));
        pgDataSource.setUser(System.getenv("POSTGRES_USER"));
        pgDataSource.setPassword(System.getenv("POSTGRES_PASSWORD"));

        return pgDataSource;
    }

    private Connection createConnection() throws SQLException {
        Connection connection = this.dataSource.getConnection();
        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class[]{Connection.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("close")) {
                        if (connection.isClosed()) {
                            throw new IllegalStateException("Connection has already been closed");
                        }

                        synchronized (this) {
                            if (this.freeConnections.size() < this.getSize()) {
                                this.freeConnections.add((Connection) proxy);
                            }
                            else {
                                connection.close();
                            }
                        }
                        return null;
                    }

                    return method.invoke(connection, args);
                }
        );
    }

    public synchronized Connection getConnection() throws SQLException {
        return this.freeConnections.isEmpty() ? this.createConnection() : this.freeConnections.pop();
    }

    private int getSize() {
        return this.size;
    }
}