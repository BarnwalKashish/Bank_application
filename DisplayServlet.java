package com.bank;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DisplayServlet")
public class DisplayServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");

        // Database connection parameters
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String username = "system";
        String password = "admin";

        // User input
        int account_number = Integer.parseInt(request.getParameter("accountNumber"));

        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, username, password);
            String query = "SELECT * FROM bankaccount WHERE account_number=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, account_number);
            ResultSet rs = ps.executeQuery();

            out.println("<h2 style='color:red'>Account Details are: </h2>");
            if (rs.next()) {
                out.println("<h2>Account number: " + rs.getInt("account_number") + "</h2>");
                out.println("<h2>Name: " + rs.getString("account_holder") + "</h2>");
                out.println("<h2>Balance: " + rs.getInt("balance") + "</h2>");
            } else {
                out.println("<h2>Account details not found</h2>");
                RequestDispatcher rd = request.getRequestDispatcher("display.html");
                rd.include(request, response);
            }

            con.close();
        } catch (Exception e) {
            out.println("<h2>Error: " + e.getMessage() + "</h2>");
        }
    }
}
