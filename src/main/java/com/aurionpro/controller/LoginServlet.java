package com.aurionpro.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.aurionpro.service.UserService;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("quiz_login.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (userService.login(username, password)) {
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            response.sendRedirect("TestSelectionServlet");
        } else {
            out.println("<!DOCTYPE html><html><head><title>Login Failed</title>");
            out.println("<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css'></head>");
            out.println("<body class='container mt-5'>");
            out.println("<div class='alert alert-danger'><h4>Login Failed</h4>");
            out.println("<p>Invalid username or password. Please try again.</p></div>");
            out.println("<a href='quiz_login.html' class='btn btn-secondary'>Back to Login</a>");
            out.println("</body></html>");
        }
    }
}
