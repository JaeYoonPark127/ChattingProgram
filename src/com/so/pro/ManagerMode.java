package com.so.pro;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;


import java.awt.Color;
import java.awt.Component;

public class ManagerMode extends JFrame {//������ �α��� â

	private JPanel contentPane;
	private JTextField tfID;
	private JPasswordField tfPassword;
	private JButton loginBtn, exitBtn;
	public JFrame frame;

	//�̹��� ������ �߰�
		ImageIcon sign_bg1; //���� �̹���
		Image mod_bg1; // �̹���������(Imageicon)�� �̹���(Image)�� ��ȯ
		Image mod_bg2; // �̹��� ũ�� ����
		ImageIcon sign_bg2; // ���� �̹���(Image)�� �̹���������(Imageicon)���� �纯ȯ

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManagerMode frame = new ManagerMode();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ManagerMode() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(400, 300);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setForeground(new Color(0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setTitle("ȸ������"); // Ÿ��Ʋ �̸�
		
		JLabel lblLogin = new JLabel("���̵�");
		lblLogin.setBounds(41, 52, 69, 35);
		contentPane.add(lblLogin);
		
		JLabel lblPassword = new JLabel("��й�ȣ");
		lblPassword.setBounds(41, 103, 69, 35);
		contentPane.add(lblPassword);
		
		tfID = new JTextField();
		tfID.setBounds(157, 52, 176, 35);
		contentPane.add(tfID);
		tfID.setColumns(10);
				
		tfPassword = new JPasswordField();
		tfPassword.setEchoChar('*');
		tfPassword.setColumns(10);
		tfPassword.setBounds(157, 103, 176, 35);
		contentPane.add(tfPassword);
		
		loginBtn = new JButton("�α���");
		loginBtn.setBounds(229, 154, 104, 29);
		contentPane.add(loginBtn);
		
		exitBtn = new JButton("���");
		exitBtn.setBounds(108, 154, 106, 29);
		contentPane.add(exitBtn);
		
		setVisible(true);

		
		//�α��� �׼�
		loginBtn.addActionListener(new ActionListener() {					
					@Override
					public void actionPerformed(ActionEvent e) {
						String ID = tfID.getText();
						String password = tfPassword.getText();
						if(ID.equals("ppo0603") && password.equals("aceg2453886@")) { //�ӽ÷� ������ �α��� üũ ���
							//�α��� ���� �޽���
							JOptionPane.showMessageDialog(null, "�α��� ����");
							//ȸ�� ���� ����Ʈ ȭ�� �̵��� ���ÿ� username ���ǰ����� �ѱ�.
							//ChatClientObject ��ü�� �������� ä�ù����� �����Ų��.
					        new MenuJTabaleExam();
							//MemberListFrame mlf = new MemberListFrame(username);
							//���� ȭ�� ����
							dispose();
						}else {
							JOptionPane.showMessageDialog(null, "�α��� ����");
						}
						
					}
		});
		
		//����â ��� ��ư
		exitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}			
		});

	}
}