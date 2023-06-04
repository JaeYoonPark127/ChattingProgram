package com.so.pro;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DBRevise implements MouseListener {

	String id = null;
	String pw, name, birth;

	JFrame frame;
	JPanel logPanel;
	JPanel logPanel1;
	JPanel logPanel2;
	JPanel logPanel3;
	JTextField idTf, pwTf, nameTf, birthTf = null;
	JButton okBtn;

	MsgBox msgbox = new MsgBox();

	Statement stmt = null;
	ResultSet rs = null;
	String url = "jdbc:oracle:thin:@172.16.20.120:1521:xe"; // ����Ŭ ��Ʈ��ȣ1521/@���Ŀ��� IP�ּ�
	String sql = null;
	String sql2 = null;
	Properties info = null;
	Connection cnn = null;

	// id�� �޾ƿͼ� �װ��� ������ pw/name/barth ������ ����
	void myInfo(String id) {
		this.id = id;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver"); // �˾Ƽ� conn���� ����
			info = new Properties();
			info.setProperty("user", "c##scott");
			info.setProperty("password", "tiger");
			cnn = DriverManager.getConnection(url, info); // ������ ������ �������ִ� ����̹��Ŵ����� ������
			stmt = cnn.createStatement();

			sql = "select * from joinDB where id='" + id + "'";
			rs = stmt.executeQuery(sql);

			while (rs.next() == true) { // ��������
				pw = rs.getString(2);
				name = rs.getString(3);
				birth = rs.getString(4);
			}
		} catch (Exception ee) {
			System.out.println("��������");
			ee.printStackTrace();
		}

		frame = new JFrame("ȸ������");
		logPanel = new JPanel();
		logPanel1 = new JPanel(new GridLayout(4, 1));
		logPanel2 = new JPanel(new GridLayout(4, 1));
		logPanel3 = new JPanel();

		JLabel idLabel = new JLabel(" I   D   ", JLabel.CENTER);
		JLabel pwLabel = new JLabel(" P  W  ", JLabel.CENTER);
		JLabel nameLabel = new JLabel("�� ��", JLabel.CENTER);
		JLabel baLabel = new JLabel("�� �� �� �� ", JLabel.CENTER);
		logPanel1.add(idLabel);
		logPanel1.add(pwLabel);
		logPanel1.add(nameLabel);
		logPanel1.add(baLabel);

		idTf = new JTextField(20);
		idTf.setText(id);
		idTf.setEditable(false);
		pwTf = new JTextField(20);
		pwTf.setText(pw);
		nameTf = new JTextField(20);
		nameTf.setText(name);
		birthTf = new JTextField(20);
		birthTf.setText(birth);
		logPanel2.add(idTf);
		logPanel2.add(pwTf);
		logPanel2.add(nameTf);
		logPanel2.add(birthTf);

		frame.add(logPanel, BorderLayout.NORTH);
		frame.add(logPanel1, BorderLayout.WEST);
		frame.add(logPanel2, BorderLayout.CENTER);
		frame.add(logPanel3, BorderLayout.EAST);

		JPanel logPanel4 = new JPanel();
		JLabel askLabel = new JLabel("�����Ͻðڽ��ϱ�?");
		okBtn = new JButton("Ȯ��");
		JButton cancleBtn = new JButton("���");
		okBtn.addMouseListener(this); 		// addMouseListener�̺�Ʈ
		logPanel4.add(askLabel);
		logPanel4.add(okBtn);
		logPanel4.add(cancleBtn);
		frame.add(logPanel4, BorderLayout.SOUTH);

		// ��� ��ư
		cancleBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.dispose();
				dbClose();
			}
		});

		frame.setBounds(450, 250, 350, 200);
		frame.setResizable(false);
		frame.setVisible(true);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		try {
			// Ȯ�� ��ư
			if (e.getSource().equals(okBtn)) {
				if ((idTf.getText().isEmpty()) == true || (pwTf.getText().isEmpty()) == true
						|| (nameTf.getText().isEmpty()) || (birthTf.getText().isEmpty())) {
					msgbox.messageBox(logPanel3, "����ִ� ĭ�� �����մϴ�.");
				} else if ((birthTf.getText().length()) != 6) {
					msgbox.messageBox(logPanel3, "������� ������ �߸��Ǿ����ϴ�."); // �ƴѰ��
				} else {
					sql = "update joinDB set pw='" + pwTf.getText() + "',name='" + nameTf.getText() + "',birth='"
							+ birthTf.getText() + "' where id='" + id + "'";
					System.out.println(sql);
					stmt.executeUpdate(sql);
					msgbox.messageBox(logPanel3, "���� �Ǽ̽��ϴ�.");
					frame.dispose(); // â �ݱ�
					dbClose();
				}
			}

		} catch (Exception ee) {
			System.out.println("��������");
			ee.printStackTrace();
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	public void dbClose() {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (cnn != null)
				cnn.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
