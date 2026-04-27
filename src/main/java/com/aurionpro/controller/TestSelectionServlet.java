package com.aurionpro.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.aurionpro.dao.SubjectDao;
import com.aurionpro.model.Subject;

@WebServlet("/TestSelectionServlet")
public class TestSelectionServlet extends HttpServlet {
    private SubjectDao subjectDao = new SubjectDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("quiz_login.html");
            return;
        }
        List<Subject> subjects = subjectDao.getAllSubjects();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><head><title>Select Subject</title>");
        out.println("<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css'></head>");
        out.println("<body class='container mt-5'>");
        out.println("<h2>Select a Subject & Difficulty</h2>");
        if (subjects == null || subjects.isEmpty()) {
            out.println("<div class='alert alert-danger'>No subjects available. Contact admin.</div>");
        } else {
            out.println("<form method='post' action='QuizServlet'>");
            out.println("<div class='mb-3'><label class='form-label'>Subject</label>");
            out.println("<select name='subjectId' class='form-select'>");
            for (Subject s : subjects) {
                out.println("<option value='" + s.getSubjectId() + "'>" + s.getSubjectName() + "</option>");
            }
            out.println("</select></div>");
            out.println("<div class='mb-3'><label class='form-label'>Difficulty</label>");
            out.println("<select name='difficulty' class='form-select'>");
            out.println("<option value='easy'>Easy</option>");
            out.println("<option value='medium'>Medium</option>");
            out.println("<option value='hard'>Hard</option>");
            out.println("</select></div>");
            out.println("<button type='submit' class='btn btn-primary'>Start Quiz</button>");
            out.println("</form>");
        }
        out.println("</body></html>");
    }
}
