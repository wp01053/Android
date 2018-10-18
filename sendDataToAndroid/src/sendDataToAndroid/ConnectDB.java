package sendDataToAndroid;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectDB {
	// 싱글톤 패턴으로 사용 하기위 한 코드들
	private static ConnectDB instance = new ConnectDB();

	public static ConnectDB getInstance() {
		return instance;
	}

	public ConnectDB() {

	}

	String jdbcUrl = "jdbc:oracle:thin:@192.168.0.90:1521:xe";
	String userId = "scott";
	String userPw = "tiger";
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private PreparedStatement pstmt2 = null;
	private ResultSet rs = null;
	private String sql = "";
	private String sql2 = "";
	String returns = "";
	String returns2 = "";

	public String logindb(String id, String pwd) {
		try {
			System.out.println("3333333333333");

			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("66666666666666");

			conn = DriverManager.getConnection(jdbcUrl, userId, userPw);
			System.out.println("7777777777777");

			sql = "select id,pwd from oc22table where id=? and pwd=?";
			System.out.println("로그인디비 들어감");

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pwd);
			rs = pstmt.executeQuery();
			System.out.println("커리문 들어갔다가 나옴");

			if (rs.next()) {

				if (rs.getString("id").equals(id) && rs.getString("pwd").equals(pwd)) {
					System.out.println("아이디 비교중");

					returns2 = "true";// 로그인 가능
					System.out.println("로그인성공");

				} else {
					returns2 = "false"; // 로그인 실패
					System.out.println("로그인 실패");

				}
			} else {
				returns2 = "noId"; // 아이디 또는 비밀번호 존재 X
				System.out.println("회원정보없음");

			}

		} catch (Exception e) {

		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
		}
		return returns2;
	}

	// 데이터베이스와 통신하기 위한 코드가 들어있는 메서드
	public String joindb(String id, String pwd) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(jdbcUrl, userId, userPw);
			sql = "select id from oc22table where id=?";
			System.out.println("11111111111111111");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getString("id").equals(id)) { // 이미 아이디가 있는 경우
					returns = "id";
					System.out.println("2222222222222");

				}
			} else { // 입력한 아이디가 없는 경우
				sql2 = "insert into oc22table values(?,?)";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, id);
				pstmt2.setString(2, pwd);
				pstmt2.executeUpdate();

				returns = "ok";
				System.out.println("111111123123123123123123123");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
			if (pstmt2 != null)
				try {
					pstmt2.close();
				} catch (SQLException ex) {
				}
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
		}
		return returns;
	}
}
