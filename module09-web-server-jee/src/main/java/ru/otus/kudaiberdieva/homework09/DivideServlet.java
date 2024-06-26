package ru.otus.kudaiberdieva.homework09;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "DivideServlet", urlPatterns = "/div")
public class DivideServlet extends BaseCalculatorServlet{

    @Override
    protected String calculate(double a, double b) {
        Double result = a/b;
        return String.format("%f / %f = %f", a, b, result);
    }

    @Override
    public String getServletInfo() {
        return "Division Result";
    }
}
