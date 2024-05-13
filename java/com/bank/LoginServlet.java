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

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
        
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String fullname = request.getParameter("username");
        String password = request.getParameter("password");
        
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url,"system","admin");
            String query = "select * from register where email=? and password=?"; // Corrected column names
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, fullname);
            ps.setString(2, password);
                
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                // Redirect to another servlet or JSP for post-login logic
//                RequestDispatcher rd=request.getRequestDispatcher("SuccessServlet");
//                rd.forward(request, response);
            	out.println("<div style='text-align: center; margin-top: 60px;'>");
            	out.println("<h4 style='color:green; font-size: 20px;'>Registration Successful - You can now login</h4>");
            	out.println("</div>");
                RequestDispatcher rd = request.getRequestDispatcher("home.html");
				rd.include(request, response);
            } else {
                out.println("<div style='text-align: center; margin-top: 60px;'>");
                out.println("<h4 style='color:red; font-size: 20px;'>Login Failed/Try Again</h4>");
                out.println("</div>");
                RequestDispatcher rd=request.getRequestDispatcher("login.html");
                rd.include(request, response);
            }
        } catch(Exception e) {
            out.println("<h4 style='color:red'>"+e.getMessage()+"</h4>");
            e.printStackTrace();
        }
    }
}
