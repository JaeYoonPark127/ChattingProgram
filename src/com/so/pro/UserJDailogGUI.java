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

	JLabel lable_Id = new JLabel("아이디");
	JLabel lable_Password=new JLabel("비밀번호");
	JLabel lable_Name =new JLabel("이름");
	JLabel label_birth = new JLabel("생일");
	JLabel lable_email =new JLabel("이메일");
	JLabel label_ip = new JLabel("주소");

	


	JTextField id=new JTextField();
	JTextField password=new JTextField();
	JTextField name=new JTextField();
	JTextField birth=new JTextField();
	JTextField email=new JTextField();
	JTextField ip=new JTextField();

	

	JButton confirm;
	JButton reset=new JButton("취소");

   MenuJTabaleExam me;

   JPanel Pwcheck = new JPanel(new BorderLayout());
   JButton PwBtn = new JButton("비밀번호확인");
   

  
   JButton ImageBtn = new JButton("프로필검색");
   
   UserDefaultJTableDAO dao =new UserDefaultJTableDAO();
   

	public UserJDailogGUI(MenuJTabaleExam me, String index){
		super(me,"회원수정");
		this.me=me;
		if(index.equals("가입")){
			confirm=new JButton(index);
		}else{
			confirm=new JButton("수정");	
			
			//text박스에 선택된 레코드의 정보 넣기
			int row = me.jt.getSelectedRow();//선택된 행
			id.setText( me.jt.getValueAt(row, 0).toString() );
			password.setText( me.jt.getValueAt(row, 1).toString() );
			name.setText( me.jt.getValueAt(row, 2).toString() );
			birth.setText( me.jt.getValueAt(row, 3).toString() );
			email.setText( me.jt.getValueAt(row, 4).toString() );
			ip.setText( me.jt.getValueAt(row, 5).toString() );
			
			
			//id text박스 비활성
			id.setEditable(false);
		}
		
		
		//Label추가부분
		pw.add(lable_Id);//아이디
		pw.add(lable_Password);//비밀번호
		pw.add(lable_Name);//이름
		pw.add(label_birth);//생일
		pw.add(lable_email);//이메일
		pw.add(label_ip);//우편번호
	
	
		//비밀번호 패널에 입력창 및 버튼 삽입
		Pwcheck.add(password,"Center");
		Pwcheck.add(PwBtn,"East");


		
		//TextField 추가
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

		//우측상단 x버튼 누르면 수정창만 나가짐
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); 
		
		//이벤트등록
        confirm.addActionListener(this); //가입/수정 이벤트등록
        reset.addActionListener(this); //취소 이벤트등록
        PwBtn.addActionListener(this);// ID중복체크 이벤트 등록
        setLocationRelativeTo(null);
    	ImageBtn.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			System.out.println("프로필 사진 검색 버튼 클릭."); //이미지 검색 버튼 클릭시 출력
    			JFileChooser chooser = new JFileChooser(); //파일 탐색창 생성
    			int returnVal = chooser.showOpenDialog(UserJDailogGUI.this); //파일 탐색창 출력
    			  				
    		}  
       });
	}//생성자끝
    
	/**
	 * 수정 기능에 대한 부분
	 * */
	@Override
	public void actionPerformed(ActionEvent e) {
	   String btnLabel =e.getActionCommand();//이벤트주체 대한 Label 가져오기
	    
	   if(btnLabel.equals("수정")){
		    if( dao.userUpdate(this) > 0){
		    	messageBox(this, "수정완료되었습니다.");
		    	dispose();
		    	dao.userSelectAll(me.dt);
		    	if(me.dt.getRowCount() > 0 ) me.jt.setRowSelectionInterval(0, 0);
		    	
		    }else{
		    	messageBox(this, "수정되지 않았습니다.");
		    }   
	   }else if(btnLabel.equals("취소")){
		   dispose();
		   
	   }else if(btnLabel.equals("비밀번호확인")){
			DBJoin mdao = new DBJoin();				
			if(password.getText().trim().equals("")) { // 빈 입력창 상태로 버튼 클릭시
				JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요.");
				password.requestFocus(); // 비밀번호 입력창으로 포커스 이동
			}else { // 비밀번호 입력 후 버튼 클릭시
				/*if(mdao.getPwPatternCheck(password.getText())) { // 우선적으로 비밀번호 규칙 확인
					JOptionPane.showMessageDialog(null, "최소 9~15글자, 대문자 1개, 소문자 1개, 특수문자 1개로 구성해야됩니다.");*/
					password.setText(""); // 비밀번호 입력창에 입력한 정보 지움
					password.requestFocus(); // 비밀번호 입력창에 포커스를 옮김 
				}/*
				else if(mdao.pwTf.getText()((password.getText())) { // 규칙 확인 이후 중복 확인
					JOptionPane.showMessageDialog(null, "사용 가능한 비밀번호입니다.");
				}/*else {
					JOptionPane.showMessageDialog(null, "해당 비밀번호는 중복된 비밀번호입니다.");
					password.setText(""); // 비밀번호 입력창에 입력한 정보 지움
					password.requestFocus(); // 비밀번호 입력창에 포커스 옮김
				}*/
				
			}
	   }		
	//actionPerformed끝

	
	/**
	 * 메시지박스 띄워주는 메소드 작성
	 * */
	public static void messageBox(Object obj , String message){
		JOptionPane.showMessageDialog( (Component)obj , message);
	}

}//클래스끝
