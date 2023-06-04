package com.so.pro;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ServerAddress extends JFrame {

	JButton confirmBtn;
	JTextField ipText;
	LoginUI loginUI;
    ImageIcon icon;

	public ServerAddress(LoginUI loginUI) {
		this.loginUI = loginUI;
		initialize();
	}



	private void initialize() {
		
		icon = new ImageIcon("icon2.png");
		this.setIconImage(icon.getImage());//Ÿ��Ʋ�ٿ� �̹����ֱ�
		
		setTitle("���� ������ �ּ� �Է�");
		setBounds(100, 100, 306, 95);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(12, 10, 266, 37);
		getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0)); // �ؽ�Ʈ ũ��

		ipText = new JTextField();
		ipText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					loginUI.ipBtn.setText(ipText.getText());
					loginUI.setVisible(true);
					dispose();
					loginUI.idText.requestFocus();
				}
			}
		});
		ipText.setText("172.16.20.120");
		panel.add(ipText, BorderLayout.CENTER);
		ipText.setColumns(10);

		confirmBtn = new JButton("Ȯ��");
		confirmBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loginUI.ipBtn.setText(ipText.getText());
				loginUI.setVisible(true);
				dispose();
				loginUI.idText.requestFocus();
			}
		});
		panel.add(confirmBtn, BorderLayout.EAST);
		setVisible(true);
	}

}