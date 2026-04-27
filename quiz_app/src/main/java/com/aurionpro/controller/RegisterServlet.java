package com.aurionpro.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.aurionpro.service.UserService;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("quiz_register.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (userService.register(username, password)) {
            response.sendRedirect("quiz_login.html?register=success");
        } else {
            response.setContentType("text/html");
            response.getWriter().println("<!DOCTYPE html><html><body>");
            response.getWriter().println("<div class='alert alert-danger'>Registration failed. Try a different username.</div>");
            response.getWriter().println("<a href='quiz_register.html'>Back to Register</a>");
            response.getWriter().println("</body></html>");
        }
    }
}
