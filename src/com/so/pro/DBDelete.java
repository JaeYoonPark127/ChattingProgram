package com.so.pro;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

public class DBDelete {
	MsgBox msgbox = new MsgBox();

	String id = null;

	Statement stmt = null;
	ResultSet rs = null;
	String url = "jdbc:oracle:thin:@172.16.20.120:1521:xe"; // ����Ŭ ��Ʈ��ȣ1521/@���Ŀ��� IP�ּ�
	String sql = null;
	String sql2 = null;
	Properties info = null;
	Connection cnn = null;

	// id�� �޾ƿͼ� �װ��� ������ pw/name/birth ����
	int InfoDel(String id) {
		int result = 0;
		this.id = id;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); // �˾Ƽ� ����..conn��
			info = new Properties();
			info.setProperty("user", "c##scott");
			info.setProperty("password", "tiger");
			cnn = DriverManager.getConnection(url, info); // ������ ������ �������ִ� ����̹��Ŵ����� ������
			stmt = cnn.createStatement();

			sql = "delete from joinDB where id='" + id + "'";
			stmt.executeUpdate(sql);

			sql = "select * from joinDB where id='" + id + "'";
			rs = stmt.executeQuery(sql);
			if (rs.next() == true) { // ��������
				result = 0; // ����
			} else {
				result = 1; // ����
			}
		} catch (Exception ee) {
			System.out.println("��������");
			ee.printStackTrace();
		}

		return result;
	}

}
