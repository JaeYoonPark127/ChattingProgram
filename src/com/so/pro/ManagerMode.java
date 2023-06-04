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

public class ManagerMode extends JFrame {//관리자 로그인 창

	private JPanel contentPane;
	private JTextField tfID;
	private JPasswordField tfPassword;
	private JButton loginBtn, exitBtn;
	public JFrame frame;

	//이미지 아이콘 추가
		ImageIcon sign_bg1; //원본 이미지
		Image mod_bg1; // 이미지아이콘(Imageicon)을 이미지(Image)로 변환
		Image mod_bg2; // 이미지 크기 조정
		ImageIcon sign_bg2; // 조정 이미지(Image)를 이미지아이콘(Imageicon)으로 재변환

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
		setTitle("회원관리"); // 타이틀 이름
		
		JLabel lblLogin = new JLabel("아이디");
		lblLogin.setBounds(41, 52, 69, 35);
		contentPane.add(lblLogin);
		
		JLabel lblPassword = new JLabel("비밀번호");
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
		
		loginBtn = new JButton("로그인");
		loginBtn.setBounds(229, 154, 104, 29);
		contentPane.add(loginBtn);
		
		exitBtn = new JButton("취소");
		exitBtn.setBounds(108, 154, 106, 29);
		contentPane.add(exitBtn);
		
		setVisible(true);

		
		//로그인 액션
		loginBtn.addActionListener(new ActionListener() {					
					@Override
					public void actionPerformed(ActionEvent e) {
						String ID = tfID.getText();
						String password = tfPassword.getText();
						if(ID.equals("ppo0603") && password.equals("aceg2453886@")) { //임시로 구현한 로그인 체크 기능
							//로그인 성공 메시지
							JOptionPane.showMessageDialog(null, "로그인 성공");
							//회원 정보 리스트 화면 이동과 동시에 username 세션값으로 넘김.
							//ChatClientObject 객체를 생성시켜 채팅방으로 연결시킨다.
					        new MenuJTabaleExam();
							//MemberListFrame mlf = new MemberListFrame(username);
							//현재 화면 종료
							dispose();
						}else {
							JOptionPane.showMessageDialog(null, "로그인 실패");
						}
						
					}
		});
		
		//관리창 취소 버튼
		exitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}			
		});

	}
}