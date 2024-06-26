package ru.otus.kudaiberdieva.homework09;


import javax.servlet.annotation.WebServlet;

@WebServlet(name = "SubtractServlet", urlPatterns = "/subtract")
public class SubtractServlet extends BaseCalculatorServlet {

    @Override
    protected String calculate(double a, double b) {
        double result = a - b;
        return String.format("%f - %f = %f", a, b, result);
    }
    @Override
    public String getServletInfo() {
        return "Subtraction Result";
    }
}
