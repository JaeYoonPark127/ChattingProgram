package com.so.pro;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class DBLogin {

	String id = null;
	String pw = null;

	Statement stmt = null;
	ResultSet rs = null;
	String url = "jdbc:oracle:thin:@172.16.20.120:1521:xe"; // ����Ŭ ��Ʈ��ȣ1521/@���Ŀ��� IP�ּ�
	String sql = null;
	Properties info = null;
	Connection cnn = null;

	int checkIDPW(String id, String pw) {
		this.id = id;
		this.pw = pw;
		int result = 1;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); // �˾Ƽ� ����..conn��
			info = new Properties();
			info.setProperty("user", "c##scott");
			info.setProperty("password", "tiger");
			cnn = DriverManager.getConnection(url, info); // ������ ������ �������ִ� ����̹��Ŵ����� ������
			stmt = cnn.createStatement();

			sql = "select * from joinDB where id='" + id + "'";
			rs = stmt.executeQuery(sql); // �о���°Ŷ� �ٸ��� ����	//����Ÿ���� ResultSet

			if (rs.next() == false || (id.isEmpty()) == true) { // id�� ����x
				result = 1;
			} else {
				sql = "select * from (select * from joinDB where id='" + id + "')";
				rs = stmt.executeQuery(sql);
				while (rs.next() == true) { 		// ��������
					if (rs.getString(2).equals(pw)) { // pw�� ������ ��
						result = 0; 		// ������ �α��� ����
					} else {				// ���̵�°��� pw�� �ٸ����
						result = 1;
					}
				}
			}
		} catch (Exception ee) {
			System.out.println("��������");
			ee.printStackTrace();
		}
		return result;
	}
}
