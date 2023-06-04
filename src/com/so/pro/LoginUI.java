package com.so.pro;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginUI extends JFrame implements TextListener {

	boolean confirm = false;
	TextField idText;
	TextField pwText;
	JButton loginBtn, signUpBtn;
	JButton ipBtn;
	EightClient client;

	DBJoin jdb;
	JScrollPane scrollPane;
    ImageIcon icon;
    LoginUI login;
    JPanel panel;
	
	// ������
	public LoginUI(EightClient eigClient) {
		
		setTitle("�α���");
		ServerAddress sd = new ServerAddress(this);
		this.client = eigClient;
		loginUIInitialize();
	}

	// �޼���
	private void loginUIInitialize() {
		
		icon = new ImageIcon("C:\\Users\\PC\\Desktop\\back1.png"); 
        //��� Panel ������ �������������� ����      
        JPanel panel = new JPanel() {
            public void paintComponent(Graphics g) {
                g.drawImage(icon.getImage(), 0, 0, null);            
                setOpaque(false); //�׸��� ǥ���ϰ� ����,�����ϰ� ����
                super.paintComponent(g);
            }
        };
		
		setBounds(100, 100, 335, 218); // â ũ�� ����
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		setResizable(false);

		
		
		panel.setBounds(0, 0, 348, 181); // �α��� ��й�ȣ ��Ÿ����
		getContentPane().add(panel); // �α��� ȭ�� ��Ÿ����
		panel.setLayout(null);

		JLabel jbNewLabel1 = new JLabel("���̵�");
		jbNewLabel1.setBounds(60, 55, 57, 15); // "���̵�" ��ġ
		panel.add(jbNewLabel1);

		JLabel jbNewLabel2 = new JLabel("��й�ȣ");
		jbNewLabel2.setBounds(60, 86, 57, 15);
		panel.add(jbNewLabel2);

		idText = new TextField();
		idText.setBounds(129, 52, 116, 21);
		panel.add(idText);
		idText.setColumns(10);

		pwText = new TextField();
		pwText.addKeyListener(new KeyAdapter() {

			// �α��� ���� ��ư
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					msgSummit();
				}
			}

		});
		pwText.setBounds(129, 83, 116, 21);
		panel.add(pwText);
		pwText.setColumns(10);
		
	    pwText.selectAll();   //��й�ȣ�� �Էµ� ��� ���ڿ� ���ð���
	    pwText.setEchoChar('*'); //��й�ȣ ���ڸ� '*'�� �ٌ���

		idText.addTextListener(this);
		pwText.addTextListener(this);

		loginBtn = new JButton("�α���");
		loginBtn.setEnabled(false);
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});

		loginBtn.addMouseListener(new MouseAdapter() {
			// Ŭ���� ���� �޼���� �Ѿ��.
			@Override
			public void mouseClicked(MouseEvent e) {
				if (loginBtn.isEnabled() == true) {
					msgSummit();
				}
			}
		});

		loginBtn.setBounds(50, 111, 97, 23);
		panel.add(loginBtn);

		signUpBtn = new JButton("ȸ������");
		signUpBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		signUpBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// ȸ������
				jdb = new DBJoin();
				jdb.JoinDBPanel();
			}
		});
		signUpBtn.setBounds(149, 111, 97, 23);
		panel.add(signUpBtn);

		JLabel jbNewLabe3 = new JLabel("���� ������");
		jbNewLabe3.setBounds(12, 10, 78, 15);
		panel.add(jbNewLabe3);

		ipBtn = new JButton("");
		ipBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ServerAddress sd = new ServerAddress(LoginUI.this);
				setVisible(false); // true�̸� â�� �׳� ������.
			}
		});
		ipBtn.setBounds(93, 6, 97, 23);
		panel.add(ipBtn);
	}

	// �޽��� ���� �޼���
	private void msgSummit() {
		new Thread(new Runnable() {
			public void run() {

				// ���ϻ���(�α��� ������ �ȵȴ�)
				if (client.serverAccess()) {
					try {
						// �α�������(���̵�+�н�����) ����
						client.getDos().writeUTF(User.LOGIN + "/" + idText.getText() + "/" + pwText.getText());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			} // run() start
		}).start();
	} // msgSummit() end

	@Override
	public void textValueChanged(TextEvent e) {
		if (idText.getText().equals("") || pwText.getText().equals("")) {
			loginBtn.setEnabled(false);
		} else {
			loginBtn.setEnabled(true);
		}
	}
	

}
