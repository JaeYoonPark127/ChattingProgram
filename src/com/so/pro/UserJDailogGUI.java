package com.so.pro;

import java.awt.BorderLayout;

//UserJDailogGUI.java

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


public class UserJDailogGUI extends JDialog implements ActionListener{
	
	JPanel pw=new JPanel(new GridLayout(10,1));
	JPanel pc=new JPanel(new GridLayout(10,1));
	JPanel ps=new JPanel();

	JLabel lable_Id = new JLabel("���̵�");
	JLabel lable_Password=new JLabel("��й�ȣ");
	JLabel lable_Name =new JLabel("�̸�");
	JLabel label_birth = new JLabel("����");
	JLabel lable_email =new JLabel("�̸���");
	JLabel label_ip = new JLabel("�ּ�");

	


	JTextField id=new JTextField();
	JTextField password=new JTextField();
	JTextField name=new JTextField();
	JTextField birth=new JTextField();
	JTextField email=new JTextField();
	JTextField ip=new JTextField();

	

	JButton confirm;
	JButton reset=new JButton("���");

   MenuJTabaleExam me;

   JPanel Pwcheck = new JPanel(new BorderLayout());
   JButton PwBtn = new JButton("��й�ȣȮ��");
   

  
   JButton ImageBtn = new JButton("�����ʰ˻�");
   
   UserDefaultJTableDAO dao =new UserDefaultJTableDAO();
   

	public UserJDailogGUI(MenuJTabaleExam me, String index){
		super(me,"ȸ������");
		this.me=me;
		if(index.equals("����")){
			confirm=new JButton(index);
		}else{
			confirm=new JButton("����");	
			
			//text�ڽ��� ���õ� ���ڵ��� ���� �ֱ�
			int row = me.jt.getSelectedRow();//���õ� ��
			id.setText( me.jt.getValueAt(row, 0).toString() );
			password.setText( me.jt.getValueAt(row, 1).toString() );
			name.setText( me.jt.getValueAt(row, 2).toString() );
			birth.setText( me.jt.getValueAt(row, 3).toString() );
			email.setText( me.jt.getValueAt(row, 4).toString() );
			ip.setText( me.jt.getValueAt(row, 5).toString() );
			
			
			//id text�ڽ� ��Ȱ��
			id.setEditable(false);
		}
		
		
		//Label�߰��κ�
		pw.add(lable_Id);//���̵�
		pw.add(lable_Password);//��й�ȣ
		pw.add(lable_Name);//�̸�
		pw.add(label_birth);//����
		pw.add(lable_email);//�̸���
		pw.add(label_ip);//�����ȣ
	
	
		//��й�ȣ �гο� �Է�â �� ��ư ����
		Pwcheck.add(password,"Center");
		Pwcheck.add(PwBtn,"East");


		
		//TextField �߰�
		pc.add(id);
		pc.add(Pwcheck);
		pc.add(name);
		pc.add(birth);
		pc.add(email);
		pc.add(ip);

	
	

		
		
		
		ps.add(confirm); 
		ps.add(reset);
	
		getContentPane().add(pw,"West"); 
		getContentPane().add(pc,"Center");
		getContentPane().add(ps,"South");
		
		setSize(435,800);
		setVisible(true);

		//������� x��ư ������ ����â�� ������
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); 
		
		//�̺�Ʈ���
        confirm.addActionListener(this); //����/���� �̺�Ʈ���
        reset.addActionListener(this); //��� �̺�Ʈ���
        PwBtn.addActionListener(this);// ID�ߺ�üũ �̺�Ʈ ���
        setLocationRelativeTo(null);
    	ImageBtn.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			System.out.println("������ ���� �˻� ��ư Ŭ��."); //�̹��� �˻� ��ư Ŭ���� ���
    			JFileChooser chooser = new JFileChooser(); //���� Ž��â ����
    			int returnVal = chooser.showOpenDialog(UserJDailogGUI.this); //���� Ž��â ���
    			  				
    		}  
       });
	}//�����ڳ�
    
	/**
	 * ���� ��ɿ� ���� �κ�
	 * */
	@Override
	public void actionPerformed(ActionEvent e) {
	   String btnLabel =e.getActionCommand();//�̺�Ʈ��ü ���� Label ��������
	    
	   if(btnLabel.equals("����")){
		    if( dao.userUpdate(this) > 0){
		    	messageBox(this, "�����Ϸ�Ǿ����ϴ�.");
		    	dispose();
		    	dao.userSelectAll(me.dt);
		    	if(me.dt.getRowCount() > 0 ) me.jt.setRowSelectionInterval(0, 0);
		    	
		    }else{
		    	messageBox(this, "�������� �ʾҽ��ϴ�.");
		    }   
	   }else if(btnLabel.equals("���")){
		   dispose();
		   
	   }else if(btnLabel.equals("��й�ȣȮ��")){
			DBJoin mdao = new DBJoin();				
			if(password.getText().trim().equals("")) { // �� �Է�â ���·� ��ư Ŭ����
				JOptionPane.showMessageDialog(null, "��й�ȣ�� �Է����ּ���.");
				password.requestFocus(); // ��й�ȣ �Է�â���� ��Ŀ�� �̵�
			}else { // ��й�ȣ �Է� �� ��ư Ŭ����
				/*if(mdao.getPwPatternCheck(password.getText())) { // �켱������ ��й�ȣ ��Ģ Ȯ��
					JOptionPane.showMessageDialog(null, "�ּ� 9~15����, �빮�� 1��, �ҹ��� 1��, Ư������ 1���� �����ؾߵ˴ϴ�.");*/
					password.setText(""); // ��й�ȣ �Է�â�� �Է��� ���� ����
					password.requestFocus(); // ��й�ȣ �Է�â�� ��Ŀ���� �ű� 
				}/*
				else if(mdao.pwTf.getText()((password.getText())) { // ��Ģ Ȯ�� ���� �ߺ� Ȯ��
					JOptionPane.showMessageDialog(null, "��� ������ ��й�ȣ�Դϴ�.");
				}/*else {
					JOptionPane.showMessageDialog(null, "�ش� ��й�ȣ�� �ߺ��� ��й�ȣ�Դϴ�.");
					password.setText(""); // ��й�ȣ �Է�â�� �Է��� ���� ����
					password.requestFocus(); // ��й�ȣ �Է�â�� ��Ŀ�� �ű�
				}*/
				
			}
	   }		
	//actionPerformed��

	
	/**
	 * �޽����ڽ� ����ִ� �޼ҵ� �ۼ�
	 * */
	public static void messageBox(Object obj , String message){
		JOptionPane.showMessageDialog( (Component)obj , message);
	}

}//Ŭ������
