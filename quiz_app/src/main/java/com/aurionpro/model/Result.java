package com.aurionpro.model;

public class Result {
	private String username;
	private String subjectName;
	private int score;

	public Result(String username, String subjectName, int score) {
		this.username = username;
		this.subjectName = subjectName;
		this.score = score;
	}

	public String getUsername() {
		return username;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public int getScore() {
		return score;
	}
}
