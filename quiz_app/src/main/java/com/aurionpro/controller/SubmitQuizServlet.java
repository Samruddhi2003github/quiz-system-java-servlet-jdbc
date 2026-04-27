package com.aurionpro.controller;

import com.aurionpro.dao.ResultDao;
import com.aurionpro.dao.SubjectDao;
import com.aurionpro.model.Question;
import com.aurionpro.model.Subject;
import com.aurionpro.model.Result;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet("/SubmitQuizServlet")
public class SubmitQuizServlet extends HttpServlet {
    private final ResultDao resultDao = new ResultDao();
    private final SubjectDao subjectDao = new SubjectDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("quiz_login.html");
            return;
        }

        @SuppressWarnings("unchecked")
        List<Question> questions = (List<Question>) session.getAttribute("questions");
        String username = (String) session.getAttribute("username");
        Integer subjectIdObj = (Integer) session.getAttribute("subjectId");
        Map<Integer, String> answers = (Map<Integer, String>) session.getAttribute("answers");

        if (questions == null || username == null || subjectIdObj == null || answers == null) {
            response.sendRedirect("quiz_login.html");
            return;
        }

        int score = 0;
        for (Question q : questions) {
            String selected = answers.get(q.getId());
            String correct = q.getCorrectOption();
            if (selected != null && correct != null && selected.trim().equalsIgnoreCase(correct.trim())) {
                score++;
            }
        }

        int total = questions.size();
        String subjectName = subjectDao.getAllSubjects().stream()
                .filter(s -> s.getSubjectId() == subjectIdObj)
                .map(Subject::getSubjectName)
                .findFirst()
                .orElse("Unknown");

        // Save current quiz result
        resultDao.saveResult(username, subjectName, score);

        // Fetch all results
        List<Result> allResults = resultDao.getResultsByUsername(username);

        // Keep only latest result per subject
        Map<String, Result> latestPerSubject = new LinkedHashMap<>();
        for (Result r : allResults) {
            latestPerSubject.put(r.getSubjectName(), r); // overwrites older, keeps latest
        }

        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html><head><title>Quiz Result</title>");
        out.println("<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css'></head>");
        out.println("<body class='bg-light'>");
        out.println("<div class='container mt-5'>");
        out.println("<div class='card shadow-sm p-4'>");
        out.println("<h3 class='mb-3 text-center'>Quiz Result</h3>");
        out.println("<p><b>User:</b> " + escape(username) + "</p>");
        out.println("<p><b>Subject:</b> " + escape(subjectName) + "</p>");
        out.println("<p class='fs-4'><b>Score:</b> " + score + " / " + total + "</p>");
        out.println("<h5 class='mt-4'>Your Latest Results:</h5>");
        out.println("<table class='table table-bordered'>");
        out.println("<thead class='table-dark'><tr><th>Subject</th><th>Score</th></tr></thead>");
        out.println("<tbody>");
        for (Result r : latestPerSubject.values()) {
            String rowClass = r.getSubjectName().equals(subjectName) ? "table-success" : "";
            out.println("<tr class='" + rowClass + "'><td>" + escape(r.getSubjectName()) + "</td><td>" + r.getScore() + "</td></tr>");
        }
        out.println("</tbody></table>");
        out.println("<div class='mt-4 text-center'>");
        out.println("<a class='btn btn-outline-primary me-2' href='TestSelectionServlet'>Take another quiz</a>");
        out.println("<a class='btn btn-outline-danger' href='quiz_login.html'>Logout</a>");
        out.println("</div></div></div></body></html>");

        // Clear session quiz data
        session.removeAttribute("questions");
        session.removeAttribute("answers");
        session.removeAttribute("currentIndex");
        session.removeAttribute("subjectId");
        session.removeAttribute("difficulty");
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("&", "&amp;")
                                 .replace("<", "&lt;")
                                 .replace(">", "&gt;")
                                 .replace("\"", "&quot;");
    }
}
