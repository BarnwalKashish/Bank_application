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

/**
 * Servlet implementation class WithdrawServlet
 */
@WebServlet("/WithdrawServlet")
public class WithdrawServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		int Accnum = Integer.parseInt(request.getParameter("num"));
		int amt= Integer.parseInt(request.getParameter("amt"));
		try {
			Class.forName(driver);
			Connection con = DriverManager.getConnection(url,"system","admin");
	        String query1 = "select balance from BankAccount where Accnum=?";
	        PreparedStatement ps1 = con.prepareStatement(query1);
	        ps1.setInt(1, Accnum);
	        ResultSet rs = ps1.executeQuery();
	        if (rs.next()) {
	            int balance = rs.getInt(3);
	            if (amt <= balance) {
	                String query2 = "update BankAccount set balance=balance-? where Accnum=?";
	                PreparedStatement ps2 = con.prepareStatement(query2);
	                ps2.setInt(1, Accnum);
	                ps2.setInt(2, amt);
	                int count = ps2.executeUpdate();
	                if (count > 0) {
	                    out.println("<h2 style='color:red'>Money withdrawn</h2>");
	                    RequestDispatcher rd = request.getRequestDispatcher("profile.html");
	 				   rd.forward(request, response);
	                } else {
	                	System.out.println("<h2 style='color:red'>Money not Withdrawn</h2>");
	    	            RequestDispatcher rd = request.getRequestDispatcher("deposit.html");
	    				rd.include(request, response);
	                }
	            } else {
	               out.println("<h2 style='color:red'> Low Balance!!! </h2>");
	            }
	        } else {
	            out.println("<h2 style='color:red'>Error!</h2>");
	        }
    }catch(Exception e) {
		out.println("<h2>"+e.getMessage()+"</h2>");
	}
	}

}
