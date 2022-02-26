package spms.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/member/delete")
public class MemberDeleteServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
		
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = null;
		Statement stmt = null;
		
		try {
			
			ServletContext sc = this.getServletContext();
			Class.forName(sc.getInitParameter("driver"));
			con = DriverManager.getConnection(sc.getInitParameter("url"),
											  sc.getInitParameter("username"),
											  sc.getInitParameter("password"));
			stmt = con.createStatement();
			
			stmt.executeUpdate("DELETE FROM MEMBERS WHERE MNO="
								   + request.getParameter("no"));
			
			response.sendRedirect("list");			
			
		} catch (Exception e) {
			throw new ServletException(e);
		} finally {
			try{stmt.close();}catch(Exception e) {}
			try{con.close();}catch(Exception e) {}
		}
	}
	

}
