package com.bank;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        
        // Retrieve form data
        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String dobString = request.getParameter("dob");
        Date dob = java.sql.Date.valueOf(dobString);
        String address = request.getParameter("address");
        String accountType = request.getParameter("accountType");

        // Database connection parameters
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String dbUsername = "system";
        String dbPassword = "admin";

        try {
            // Load Oracle JDBC Driver
            Class.forName(driver);
            
            // Establish connection
            Connection con = DriverManager.getConnection(url, dbUsername, dbPassword);

            // Prepare SQL statement
         // Prepare SQL statement
            String query = "INSERT INTO register (fullname, email, password, dob, address, account_type) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, fullname);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setDate(4, dob); // Use setDate for dob
            ps.setString(5, address);
            ps.setString(6, accountType);


            // Execute SQL query
            int count = ps.executeUpdate();

            // Process result
            if (count > 0) {
            	out.println("<div style='text-align: center; margin-top: 60px;'>");
            	out.println("<h4 style='color:green; font-size: 20px;'>Registration Successful - You can now login</h4>");
            	out.println("</div>");
                RequestDispatcher rd = request.getRequestDispatcher("login.html");
				rd.include(request, response);
                
            } else {
              	out.println("<div style='text-align: center; margin-top: 60px;'>");
                out.println("<h4 style='color:red; font-size: 20px;'>Registration Failed - Please try again</h4>");
            	out.println("</div>");
                RequestDispatcher rd=request.getRequestDispatcher("register.html");
				rd.include(request, response);
            }

            // Close connections
            ps.close();
            con.close();

        } catch (ClassNotFoundException | SQLException e) {
            out.println("<h4 style='color:red'>Error: " + e.getMessage() + "</h4>");
            e.printStackTrace(); // Log the exception for debugging
        }
    }
}
