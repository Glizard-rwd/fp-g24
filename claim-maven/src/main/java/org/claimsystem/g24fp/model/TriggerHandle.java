package org.claimsystem.g24fp.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TriggerHandle {

    private static Connection conn = DBConnection.getConnection();

    public static void recordAction(String current_user, String description) {
        try {
            String sql = "INSERT INTO history (\"current_user\", description) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, current_user);
            pstmt.setString(2, description);
            pstmt.executeUpdate();
            System.out.println("Action recorded!");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static ObservableList<History> loadHistory () {
        ObservableList<History> historyList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM history";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                String id = rs.getString("id");
                String current_user = rs.getString("current_user");
                String created_at = rs.getString("created_at");
                String description = rs.getString("description");
                historyList.add(new History(id, current_user, created_at, description));
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return historyList;
    }
}

