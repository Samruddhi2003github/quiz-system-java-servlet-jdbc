package com.aurionpro.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.aurionpro.database.Database;
import com.aurionpro.model.Question;

public class QuestionDao {
	public List<Question> getAllQuestions() {
		List<Question> list = new ArrayList<>();
		String sql = "SELECT * FROM questions";
		try (Connection conn = Database.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql)) {
			while (rs.next()) {
				Question q = new Question();
				q.setId(rs.getInt("id"));
				q.setQuestionText(rs.getString("question_text"));
				q.setOption1(rs.getString("option1"));
				q.setOption2(rs.getString("option2"));
				q.setOption3(rs.getString("option3"));
				q.setOption4(rs.getString("option4"));
				q.setCorrectOption(rs.getString("correct_option"));
				list.add(q);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Question> getQuestionsBySubject(int subjectId) {
		List<Question> list = new ArrayList<>();
		String sql = "SELECT * FROM questions WHERE subject_id = ?";
		try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, subjectId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Question q = new Question();
				q.setId(rs.getInt("question_id"));
				q.setQuestionText(rs.getString("question_text"));
				q.setOption1(rs.getString("option1"));
				q.setOption2(rs.getString("option2"));
				q.setOption3(rs.getString("option3"));
				q.setOption4(rs.getString("option4"));
				q.setCorrectOption(rs.getString("correct_option"));
				list.add(q);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Question> getQuestionsBySubjectAndDifficulty(int subjectId, String difficulty) {
		List<Question> list = new ArrayList<>();
		String sql = "SELECT * FROM questions WHERE subject_id=? AND difficulty=?";
		try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, subjectId);
			ps.setString(2, difficulty);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Question q = new Question();
				q.setId(rs.getInt("question_id"));
				q.setQuestionText(rs.getString("question_text"));
				q.setOption1(rs.getString("option1"));
				q.setOption2(rs.getString("option2"));
				q.setOption3(rs.getString("option3"));
				q.setOption4(rs.getString("option4"));
				q.setCorrectOption(rs.getString("correct_option"));
				list.add(q);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
