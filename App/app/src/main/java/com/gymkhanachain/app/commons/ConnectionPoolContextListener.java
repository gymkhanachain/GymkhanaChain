package com.gymkhanachain.app.commons;

import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class ConnectionPoolContextListener {

    private static final String DB_NAME = "GYMKHANACHAIN";
    private static final String INSTANCE_CONNECTION_NAME = "gymkhanachain:us-central1:myinstance";
    private static final String MYSQL_USER_NAME = "root";
    private static final String MYSQL_USER_PASSWORD = "gymkhanachain mola mazo ";
    private static final String GOOGLE_APPLICATION_CREDENTIALS="key.json";
    private static final String URL = "jdbc:mysql://google/" + DB_NAME +
            "?cloudSqlInstance=" + INSTANCE_CONNECTION_NAME +
            "&socketFactory=com.google.cloud.sql.mysql.SocketFactory" +
            "&useSSL=false" +
            "&user=" + MYSQL_USER_NAME +
            "&password=" + MYSQL_USER_PASSWORD;

    private void createConnection() throws IOException {
        URL url = new URL(URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        StringBuilder sb = new StringBuilder();
    }

    private void createGymkhana(DataSource pool) throws SQLException {
        // Safely attempt to create the table schema.
        try (Connection conn = pool.getConnection()) {
            PreparedStatement createTableStatement = conn.prepareStatement(
                    ""
            );
            createTableStatement.execute();
        }
    }
}
