package spms.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/member/update")
public class MemberUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			ServletContext sc = this.getServletContext();
			Class.forName(sc.getInitParameter("driver"));
			con = DriverManager.getConnection(sc.getInitParameter("url"),
											  sc.getInitParameter("username"),
											  sc.getInitParameter("password"));
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT MNO,EMAIL,MNAME,CRE_DATE from MEMBERS"
								   + " WHERE MNO=" + request.getParameter("no"));
			rs.next();
			
			response.setContentType("text/html; charset=UTF-8");
			
			PrintWriter out = response.getWriter();
			
			out.println("<html><head><title>회원 정보</title></head>");
			out.println("<body><h1>회원 정보</h1>");
			out.println("<form action='update' method='post'>");
			out.println("번호: <input type='text' name='no' value='" 
						+ request.getParameter("no") +  "'readonly><br>");
			out.println("이름: *<input type='text' name='name'" 
						+ " value='" + rs.getString("MNAME") + "'><br>");
			out.println("이메일: <input type='text' name='email'" 
						+ " value='" + rs.getString("EMAIL") + "'><br>");
			out.println("가입일: "
						+ rs.getDate("CRE_DATE") + "<br>");
			out.println("<input type='submit' value='저장'>");
			out.println("<input type='button' value='취소'"
						+ " onClick='location.href=\"list\"' >");
			out.println("</form>");
			out.println("</body></html>");
			
		} catch (Exception e) {
			throw new ServletException(e);
		} finally {
			try{ if(rs != null) rs.close();} catch(Exception e) {}
			try{ if(stmt != null) stmt.close();}catch(Exception e) {}
			try{ if(con != null) con.close();}catch(Exception e) {}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		Connection con = null;
		PreparedStatement stmt = null;
		
		try {
			ServletContext sc = this.getServletContext();
			Class.forName(sc.getInitParameter("driver"));
			con = DriverManager.getConnection(sc.getInitParameter("url"),
											  sc.getInitParameter("username"),
											  sc.getInitParameter("password"));
			stmt = con.prepareStatement(
					"UPDATE MEMBERS SET EMAIL=?, MNAME=?, MOD_DATE=now()"
					+ " WHERE MNO=?");
			
			stmt.setString(1, request.getParameter("email"));
			stmt.setString(2, request.getParameter("name"));
			stmt.setInt(3, Integer.parseInt(request.getParameter("no")));
			
			stmt.executeUpdate();
			
			response.sendRedirect("list");		
			
			
		} catch (Exception e) {
			throw new ServletException(e);
		} finally {
			try{ if(stmt != null) stmt.close();}catch(Exception e) {}
			try{ if(con != null) con.close();}catch(Exception e) {}
		}		
	}
}
