package com.bank;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DepositServlet")
public class DepositServlet extends HttpServlet {
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
			String query = "update BankAccount set balance=balance+? where Accnum=?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, amt);
			ps.setInt(2, Accnum);
		        int count = ps.executeUpdate();
		        if (count > 0) {
		           out.println("<h2>Money Deposited!</h2>");
		           RequestDispatcher rd = request.getRequestDispatcher("profile.html");
				   rd.forward(request, response);
		        } 
	        else {
	            System.out.println("<h2>Money not deposited </h2>");
	            RequestDispatcher rd = request.getRequestDispatcher("deposit.html");
				rd.include(request, response);
	        }  
	        
	       
	}catch(Exception e) {
		out.println("<h2>"+e.getMessage()+"</h2>");
	}

	}

}
