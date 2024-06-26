package ru.otus.kudaiberdieva.homework09;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class BaseCalculatorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    double a = Double.parseDouble(request.getParameter("a"));
    double b = Double.parseDouble(request.getParameter("b"));
    String result = calculate(a, b);
    writeResponse(response, getServletInfo(), result);
    }
    protected void writeResponse(HttpServletResponse resp, String title, String result) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.printf("<html><body><h1>%s</h1><p>%s</p></body></html>", title, result);
        out.close();
    }

    protected abstract String calculate(double a, double b);
}
