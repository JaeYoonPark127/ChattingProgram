package com.so.pro;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;



public class DBJoin implements MouseListener {
   JFrame frame;
   JPanel logPanel;
   JPanel logPanel1;
   JPanel logPanel2;
   JPanel logPanel3;
   public static JTextField idTf, pwTf,pwAa, nameTf, birthTf,e_mailTf, ipTf,ip_2Tf=null;
   JLabel imge = new JLabel();
   JButton joinBtn, checkBt, checkpass,checkaddress,plod;
   ImageIcon icon;
   String Image_path,pro;
   MsgBox msgbox = new MsgBox();
   
   private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$"; 
   private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
   
   public ImageIcon ResizeImage(String ImagePath){
		ImageIcon MyImage = new ImageIcon(ImagePath);
		Image img = MyImage.getImage();
		Image newImg = img.getScaledInstance(imge.getWidth(),imge.getHeight(),Image.SCALE_SMOOTH);
		ImageIcon image = new ImageIcon(newImg);
		return image;
		
	}


   void JoinDBPanel() {
      
      frame = new JFrame("회원가입");
      logPanel = new JPanel();
      logPanel1 = new JPanel(new GridLayout(12, 1));
      logPanel2 = new JPanel(new GridLayout(12, 1));
      logPanel3 = new JPanel();

      icon = new ImageIcon("icon2.png");
      frame.setIconImage(icon.getImage());//타이틀바에 이미지넣기
      
    
      JLabel idLabel = new JLabel(" I D ", JLabel.CENTER);
      JLabel pwLabel = new JLabel(" P W ", JLabel.CENTER);
      JLabel pwA = new JLabel(" P W 확 인", JLabel.CENTER);
      JLabel nameLabel = new JLabel("이 름", JLabel.CENTER);
      JLabel baLabel = new JLabel("생 년 월 일 ", JLabel.CENTER);
      JLabel e_mainTf = new JLabel("E-mail", JLabel.CENTER);
      JLabel ip = new JLabel("주 소", JLabel.CENTER);
      JLabel profile = new JLabel("프 로 필", JLabel.CENTER);
      logPanel1.add(idLabel);
      logPanel1.add(pwLabel);
      logPanel1.add(pwA);
      logPanel1.add(nameLabel);
      logPanel1.add(baLabel);
      logPanel1.add(e_mainTf);
      logPanel1.add(ip);
      logPanel1.add(profile);
      
     
    
      
      idTf = new JTextField(20);
      frame.add(idTf);
      idTf.setBounds(70,22,190,25);
      idTf.addMouseListener(this);
      pwTf = new JTextField(20);
      frame.add(pwTf);
      pwTf.setBounds(70,59,190,25);
      pwTf.addMouseListener(this);
      pwAa = new JTextField(20);
      frame.add(pwAa);
      pwAa.setBounds(70,97,190,25);
      pwAa.addMouseListener(this);
      nameTf = new JTextField(20);
      frame.add(nameTf);
      nameTf.setBounds(70,135,190,25);
      nameTf.addMouseListener(this);
      birthTf = new JTextField("ex)901231", 20);
      frame.add(birthTf);
      birthTf.setBounds(70,174,190,25);
      birthTf.addMouseListener(this);
      e_mailTf = new JTextField("ex)...@naver.com",20);
      frame.add(e_mailTf);
      e_mailTf.setBounds(70,212,190,25);
      e_mailTf.addMouseListener(this);
      ipTf = new JTextField(20);
      frame.add(ipTf);
      ipTf.setBounds(70,245,90,25);
      ipTf.addMouseListener(this);
      ip_2Tf = new JTextField(20);
      frame.add(ip_2Tf);
      ip_2Tf.setBounds(70,270,190,25);
      ip_2Tf.addMouseListener(this);
      
      
      
      
      imge.setHorizontalAlignment(SwingConstants.CENTER);
      imge.setText("이미지를 업로드 해주세요.");
      frame.add(imge);
      imge.setBounds(85,300,200,140);
      imge.setVisible(true);
      
      
   
     
      
      checkBt = new JButton("ID 확인");
      frame.add(checkBt);
      checkBt.setBounds(265, 20, 95, 30); // 20,5,95,30  x축,y축,세로,가로
      checkBt.addMouseListener(this); // addMouseListener이벤트
      
      checkpass = new JButton("PW 확인");
      frame.add(checkpass);
      checkpass.setBounds(265,58, 95, 30); // 20,5,95,30
      checkpass.addMouseListener(this);
      
      checkaddress = new JButton("주소 검색");
      frame.add(checkaddress);
      checkaddress.setBounds(265, 255, 95, 30); // 20,5,95,30
      checkaddress.addMouseListener(this);
      
      plod = new JButton("이 미 지");
      frame.add(plod);
      plod.setBounds(80,450,211,23);
      plod.addMouseListener(this);
      
      
      /*원래 코드 
            checkBt = new JButton("ID Check");
            logPanel3.add(checkBt, BorderLayout.NORTH);
            checkBt.addMouseListener(this); // addMouseListener이벤트
      
            checkpass = new JButton("비밀번호확인");
            logPanel3.add(checkpass,BorderLayout.NORTH);
            checkpass.addMouseListener(this);
       */
      
      frame.add(logPanel, BorderLayout.NORTH);
      frame.add(logPanel1, BorderLayout.WEST);
      frame.add(logPanel2, BorderLayout.CENTER);
      frame.add(logPanel3, BorderLayout.EAST);

      JPanel logPanel4 = new JPanel();
      JLabel askLabel = new JLabel("가입하시겠습니까?");
      joinBtn = new JButton("가입");
      // joinBtn.setEnabled(false);
      JButton cancleBtn = new JButton("취소");
      joinBtn.addMouseListener(this); // addMouseListener이벤트
      logPanel4.add(askLabel);
      logPanel4.add(joinBtn);
      logPanel4.add(cancleBtn);
      frame.add(logPanel4, BorderLayout.SOUTH);

      // if((idTf.getText().isEmpty())==true ||
      // (pwTf.getText().isEmpty())==true){ 
      // joinBtn.setEnabled(true);
      // }

      // 취소 버튼
      cancleBtn.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            frame.dispose();
            dbClose();
         }
      });
      
    
      frame.setBounds(500, 100, 400,550);//frame.setBounds(450,250,500,350);
      frame.setResizable(false);
      frame.setVisible(true);
      //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 살리면 로그인창도 함께
      // 사라진다

   }// JoinDBPanel() end
      // ///////////////////////////////////////////////////////////////////////////////////////////////////////////

 
Statement stmt = null;
   ResultSet rs = null;
   String url = "jdbc:oracle:thin:@172.16.20.120:1521:xe"; // 오라클 포트번호1521/@이후에는 IP주소
   String sql = null;
   Properties info = null;
   Connection cnn = null;
   
   

   @Override
   public void mouseClicked(MouseEvent e) {
	   
	   try { 
	    	
	    	  if(e.getSource().equals(plod)) {
	    		  
	    		  JFileChooser file = new JFileChooser();
	    		  file.setCurrentDirectory(new File(System.getProperty("user.home")));
	  			  FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images","jpg","gif","png");
	  			  file.setFileFilter(filter);
	  			  file.setMultiSelectionEnabled(false);  //다중 선택 불가
	  			  int result = file.showSaveDialog(null);
	  			  if(result == JFileChooser.APPROVE_OPTION) {
	  				  File selectedFile = file.getSelectedFile();
	  				  String path = selectedFile.getAbsolutePath();
	  				  imge.setIcon(ResizeImage(path));
	  				  Image_path=path;
	  			
	  				  
	  			
	  			  }
	  			  else if(result==JFileChooser.CANCEL_OPTION) {
	  				  System.out.println("사진 선택 안됨");
	  			  }
	  		 	}

      // TextField 클릭시 예시지워주기
      if (e.getSource().equals(idTf)) {
         idTf.setText("");
      } else if (e.getSource().equals(pwTf)) {
         pwTf.setText("");
      } else if (e.getSource().equals(nameTf)) {
         nameTf.setText("");
      } else if (e.getSource().equals(birthTf)) {
         birthTf.setText("");
      } else if (e.getSource().equals(e_mailTf)) {
        e_mailTf.setText("");
      } else if (e.getSource().equals(ipTf)) {
         ipTf.setText(""); 
      } else if (e.getSource().equals(pwAa)) {
          pwAa.setText(""); 
      }
      
      checkaddress.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              new JDBCPostMain();
              setVisible(false); // 창 안보이게 하기 
          }
          
      private void setVisible(boolean b) {
         // TODO Auto-generated method stub
         
      }
      });
      
     
     
     
   
      
    	  
         Class.forName("oracle.jdbc.driver.OracleDriver"); // 알아서 들어간다..conn로
         info = new Properties();
         info.setProperty("user", "c##scott");
         info.setProperty("password", "tiger");
         cnn = DriverManager.getConnection(url, info); // 연결할 정보를 가지고있는 드라이버매니저를 던진다
         stmt = cnn.createStatement();

         // 테이블이 생성
         /*
          * sql =
          * "create table joinDB(
        *  id varchar2(20) primary key,
        *  pw varchar2(20) not null,
        *  name, varchar2(30).
        *  birth number(6),
        *  e_mail varchar(30),
        *  ip varchar(100))"
          * ; stmt.execute(sql); System.out.println("테이블생성완료");
          */

         // id 체크버튼
         if (e.getSource().equals(checkBt)) {
            sql = "select * from joinDB where id='" + idTf.getText() + "'";
            rs = stmt.executeQuery(sql);

            if (rs.next() == true || (idTf.getText().isEmpty()) == true) { // 이미 id가 존재하면
               msgbox.messageBox(logPanel3, "이미 존재하는 ID입니다.");
            } else { //없으면 ㄱㄱ
               msgbox.messageBox(logPanel3, "사용 가능한 ID 입니다.");
            }
         }
         //비밀번호 확인
         if (e.getSource().equals(checkpass)) {
            if(pwTf.getText().equals(pwAa.getText()) ) {
               msgbox.messageBox(logPanel3, "비밀번호가 일치합니다 !");
            }else if(!pwTf.getText().equals(pwAa.getText()) ) {
               msgbox.messageBox(logPanel3, "비밀번호가 다릅니다.");
             
            }
            
         }
         
        //주소 확인
         
         
         // 가입 버튼
         if (e.getSource().equals(joinBtn)) {
            sql = "select * from joinDB where id='" + idTf.getText() + "'";

            rs = stmt.executeQuery(sql);

            if (rs.next() == true) {
               msgbox.messageBox(logPanel3, "ID Check가 필요합니다.");

            } else if (!pwTf.getText().equals(pwAa.getText())) {
                msgbox.messageBox(logPanel3, "비밀번호 확인이 필요합니다.");
            } else if ((idTf.getText().isEmpty()) == true || (pwTf.getText().isEmpty()) == true
                  || (nameTf.getText().isEmpty()) || (birthTf.getText().isEmpty())) { //id or pw 가 비어 있을 경우
               msgbox.messageBox(logPanel3, "비어있는 칸이 존재합니다.");
            } else if ((birthTf.getText().length()) != 6) {
               msgbox.messageBox(logPanel3, "생년월일 서식이 잘못되었습니다."); //생년월일 예시에 안맞는 경우
            } else {

               sql = "insert into joinDB values ('" + idTf.getText() + "','" + pwTf.getText() + "','"
                     + nameTf.getText() + "','" + birthTf.getText() + "','" + e_mailTf.getText() + "','" 
                     + ip_2Tf.getText() + "')"; //DB 테이블 삽입
               stmt.executeUpdate(sql);
               msgbox.messageBox(logPanel3, "축하합니다.가입 되셨습니다.");
               frame.dispose(); // 창 종료
               dbClose();
            }
         }
      } 
      
      catch (Exception ee) {
         System.out.println("문제있음");
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

}// class end