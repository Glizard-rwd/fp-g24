package org.claimsystem.g24fp.model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static final String URL = "jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:5432/postgres?user=postgres.vwabpbkixqotqpxraopx&password=Minh@Thang123";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(URL);
            if (conn == null) {
                System.out.println("Failed to make connection!");
            } else {
                System.out.println("Connection established!");
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return conn;
    }

}
