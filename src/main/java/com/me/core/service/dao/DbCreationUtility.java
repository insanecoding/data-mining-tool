package com.me.core.service.dao;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

@Slf4j
public class DbCreationUtility {

    public static void createDbIfAbsent() throws IOException, SQLException {
        String[] elems = readProperties();
        String url = elems[0];
        String user = elems[1];
        String password = elems[2];

        createIfAbsent(url, user, password);
    }

    /**
     * reads property-file for production use
     * parses url, username and password
     */
    private static String[] readProperties() throws IOException {
        String url;
        String user;
        String password;

        try (InputStream in = DbCreationUtility.class
                .getClassLoader()
                .getResourceAsStream("application-prod.properties")
        ) {
            Properties prop = new Properties();
            prop.load(in);

            user = prop.getProperty("spring.datasource.username");
            password = prop.getProperty("spring.datasource.password");
            url = prop.getProperty("spring.datasource.url");
        }

        return new String[]{url, user, password};
    }

    /**
     * Db name is case sensitive!!!
     */
    private static boolean createIfAbsent(String url, String user,
                                          String password) throws SQLException {
        boolean result = false;
        int idxOfLastSlash = url.lastIndexOf("/");
        String host = url.substring(0, idxOfLastSlash + 1);
        String db = url.substring(idxOfLastSlash + 1);

        if (!dbExists(url, user, password)) {
            try (Connection c = DriverManager.getConnection(host, user, password);
                 Statement stmt = c.createStatement()) {
                stmt.executeUpdate("create database " + '"' + db + '"');
                result = true;
                log.debug("database {} was created", db);
            }
        } else {
            log.debug("database {} exists", db);
        }
        return result;
    }

    /**
     * database name is case sensitive !!!!!
     */
    private static boolean dbExists(String url, String user, String password) {
        boolean result = false;
        String databaseName = url.substring(url.lastIndexOf("/") + 1);

        try (Connection c = DriverManager.getConnection(url, user, password);
             ResultSet rs = c.getMetaData().getCatalogs()) {
            while (rs.next()) {
                if (rs.getString(1).equals(databaseName)) {
                    result = true;
                }
            }
        } catch (SQLException ignored) {
        }
        return result;
    }
}
