package com.aurionpro.controller;

import com.aurionpro.dao.QuestionDao;
import com.aurionpro.model.Question;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet("/QuizServlet")
public class QuizServlet extends HttpServlet {
    private final QuestionDao questionDao = new QuestionDao();
    private final int pageSize = 2; 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        if (username == null) {
            response.sendRedirect("quiz_login.html");
            return;
        }

        List<Question> questions = (List<Question>) session.getAttribute("questions");
        Map<Integer, String> answers = (Map<Integer, String>) session.getAttribute("answers");
        Integer currentIndex = (Integer) session.getAttribute("currentIndex");

        if (questions == null) {
            String difficulty = request.getParameter("difficulty");
            Integer subjectId = request.getParameter("subjectId") != null ? Integer.parseInt(request.getParameter("subjectId")) : null;
            if (subjectId == null || difficulty == null) {
                response.sendRedirect("TestSelectionServlet");
                return;
            }
            questions = questionDao.getQuestionsBySubjectAndDifficulty(subjectId, difficulty);
            session.setAttribute("questions", questions);
            currentIndex = 0;
            session.setAttribute("currentIndex", currentIndex);
            answers = new HashMap<>();
            session.setAttribute("answers", answers);
            session.setAttribute("subjectId", subjectId);
            session.setAttribute("difficulty", difficulty);
        }

        if (answers == null) answers = new HashMap<>();

        
        for (int i = currentIndex; i < Math.min(currentIndex + pageSize, questions.size()); i++) {
            String ans = request.getParameter("answer_" + questions.get(i).getId());
            if (ans != null) {
                answers.put(questions.get(i).getId(), ans);
            }
        }
        session.setAttribute("answers", answers);

        String action = request.getParameter("action");
        if ("Next".equals(action)) {
            currentIndex += pageSize;
            session.setAttribute("currentIndex", currentIndex);
        } else if ("Submit".equals(action)) {
            response.sendRedirect("SubmitQuizServlet");
            return;
        }

        if (currentIndex >= questions.size()) {
            response.sendRedirect("SubmitQuizServlet");
            return;
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><head><title>Quiz</title>");
        out.println("<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css'></head>");
        out.println("<body class='bg-light'><div class='container mt-5'>");
        out.println("<div class='card shadow-sm'><div class='card-header bg-primary text-white'><h4>Quiz</h4></div>");
        out.println("<div class='card-body'><form method='post' action='QuizServlet'>");

        for (int i = currentIndex; i < Math.min(currentIndex + pageSize, questions.size()); i++) {
            Question q = questions.get(i);
            String group = "answer_" + q.getId();
            String selectedValue = answers.get(q.getId());
            out.println("<div class='mb-4'><h5>Q" + (i + 1) + ". " + safe(q.getQuestionText()) + "</h5>");
            out.println(renderOption(group, q.getOption1(), 1, true, selectedValue));
            out.println(renderOption(group, q.getOption2(), 2, false, selectedValue));
            out.println(renderOption(group, q.getOption3(), 3, false, selectedValue));
            out.println(renderOption(group, q.getOption4(), 4, false, selectedValue));
            out.println("</div>");
        }

        if (currentIndex + pageSize >= questions.size()) {
            out.println("<button type='submit' name='action' value='Submit' class='btn btn-success'>Submit</button>");
        } else {
            out.println("<button type='submit' name='action' value='Next' class='btn btn-success'>Next</button>");
        }

        out.println("</form></div></div></div></body></html>");
    }

    private String safe(String s) {
        return s == null ? "" : s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

   
    private String renderOption(String group, String val, int idx, boolean required, String selectedValue) {
        boolean checked = val != null && val.equals(selectedValue);
        return "<div class='form-check'>"
                + "<input class='form-check-input' type='radio' name='" + group + "' id='" + group + "_" + idx
                + "' value='" + safe(val) + "' " + (required ? "required" : "") + (checked ? " checked" : "") + ">"
                + "<label class='form-check-label' for='" + group + "_" + idx + "'>" + safe(val) + "</label>"
                + "</div>";
    }
}
