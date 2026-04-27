package com.aurionpro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.aurionpro.database.Database;
import com.aurionpro.model.Result;

public class ResultDao {
    public boolean saveResult(String username, String subject, int score) {
        String sql = "INSERT INTO results (username, subject_name, score) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, subject);
            ps.setInt(3, score);

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<Result> getResultsByUsername(String username) {
        List<Result> results = new ArrayList<>();
        String sql = "SELECT * FROM results WHERE username = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(new Result(
                    rs.getString("username"),
                    rs.getString("subject_name"),
                    rs.getInt("score")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return results;
    }

}
