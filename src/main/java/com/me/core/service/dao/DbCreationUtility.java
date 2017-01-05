package com.me.core.service.dao;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

@Slf4j
public class DbCreationUtility {

    /**
     * database name is case sensitive !!!!!
     */
    public static boolean dbExists(String url, String user, String password) {
        boolean result = false;
        String databaseName = url.substring(url.lastIndexOf("/") + 1);

        try (Connection c = DriverManager.getConnection(url, user, password);
             ResultSet rs = c.getMetaData().getCatalogs()) {
            while (rs.next()) {
                if (rs.getString(1).equals(databaseName)) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            log.error("{} : {}. Full trace: {}", e.getClass(), e.getClass(), e);
        }
        return result;
    }


    public static boolean createDbIfAbsent() {
        String[] elems = readProperties();
        String url = elems[0];
        String user = elems[1];
        String password = elems[2];

        return createIfAbsent(url, user, password);
    }

    private static String[] readProperties() {
        String url = "";
        String user = "";
        String password = "";
        String currentDir = System.getProperty("user.dir");

        try (InputStream in = new FileInputStream(currentDir +
                "/src/main/resources/app.properties")) {
            Properties prop = new Properties();
            prop.load(in);

            user = prop.getProperty("app.username");
            password = prop.getProperty("app.password");
            url = prop.getProperty("app.jdbcUrl");
        } catch (IOException e) {
            log.error("exception: {}, {}", e.getClass(), e.getMessage());
        }

        return new String[]{url, user, password};
    }

    /**
     * Db name is case sensitive!!!
     */
    private static boolean createIfAbsent(String url, String user, String password) {
        boolean result = false;
        int idxOfLastSlash = url.lastIndexOf("/");
        String host = url.substring(0, idxOfLastSlash + 1);
        String db = url.substring(idxOfLastSlash + 1);

        if (!dbExists(url, user, password)) {

            try (Connection c = DriverManager.getConnection(host, user, password);
                 Statement stmt = c.createStatement()) {
                stmt.executeUpdate("create database " + '"' + db + '"');
                result = true;
                log.debug("database {} created", url);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            log.debug("database {} already exists, skipped", url);
        }
        return result;
    }


    public static boolean checkConnection(String url, String user, String password) {
        try {
            DriverManager.getConnection(url, user, password);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
