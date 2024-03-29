package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.jws.soap.SOAPBinding.Use;

import org.omg.CORBA.UserException;

import com.mysql.cj.exceptions.ClosedOnExpiredPasswordException;

import model.Admin;
import util.DBConnection;

public class AttendanceDaoImpl implements AttendanceDao {
	DBConnection dbc = new DBConnection();
	private Connection conn = null;
	private PreparedStatement pst = null;

	@Override
	public boolean loginByUP(String username, String password) { //通过用户名和密码进行登录验证
		Admin admin = new Admin();
		String sql = "select * from tlogin where UserName=? and PassWord=?";
		conn = dbc.getConnection();
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, username);
			pst.setString(2, password);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				admin.setUsername(rs.getString("UserName"));
				admin.setPassword(rs.getString("PassWord"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				pst.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (admin == null) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public boolean isExit(String username) {		//判断用户名是否存在，用于登录界面ajax的异步验证
		String sql = "select UserName from tlogin";
		boolean isExit = false;
		conn = dbc.getConnection();
		try {
			pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next() && isExit == false) {
				isExit = username.equals(rs.getString("UserName"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				pst.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return isExit;
	}

	@Override
	public Admin getAdminByUP(String username, String password) {	//通过用户名和密码获得用户对象
//		System.out.println("数据库接收到的值"+username+"密码:"+password); //调试有没有查询到用户
		Admin admin = new Admin();
		String sql = "select * from tlogin where UserName=? and PassWord=?"; // 查询要用 and 不可以使用‘,’
		conn = dbc.getConnection();
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, username);
			pst.setString(2, password);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				admin.setUsername(rs.getString("UserName"));
				admin.setPassword(rs.getString("PassWord"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				pst.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//			System.out.println("数据库调试"+admin.getUsername());		//调试有没有查询到用户
		return admin;
	}

	@Override
	public boolean changePassword(String username, String password) {
		String sql = "update tlogin set password=? where username=?";
		boolean temp = false;
		conn = dbc.getConnection();
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, password);
			pst.setString(2,username);
			if(pst.execute()) {
				temp=true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				pst.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return temp;
	}


}
