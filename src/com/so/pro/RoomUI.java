package com.so.pro;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.JScrollPane;

public class RoomUI extends JFrame implements ActionListener {

   
   EightClient client;
   Room room;
   User user;
   WaitRoomUI wait;
   
   JTextPane chatArea;
   JTextField chatField;
   JList uList;
   DefaultListModel model;
   
    ImageIcon icon,icon2;
    Style s;
    Style def;
    StyledDocument doc;
    int roomnum;
    
    JScrollPane scrollPane;
    String str;
    StringTokenizer st;
    
    Statement stmt = null;
    ResultSet rs = null;
    String url = "jdbc:oracle:thin:@172.16.20.120:1521:xe"; // 오라클 포트번호1521/@이후에는 IP주소
    String sql = null;
    Properties info = null;
    Connection cnn = null;
 
   public RoomUI(EightClient client, Room room) {
      this.client = client;
      this.room = room;
      this.roomnum = room.getRoomNum();
      setTitle("Octopus ChatRoom : " + room.toProtocol());
      icon = new ImageIcon("C:\\Users\\PC\\Desktop\\back.jpg");
      this.setIconImage(icon.getImage());//타이틀바에 이미지넣기
      initialize();
   }
   
   // 베경 이미지 설정

   private void initialize() {
      setBounds(100, 100, 502, 481);
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      getContentPane().setLayout(null);
      setResizable(false);

      // 채팅 패널
      final JPanel panel = new JPanel();
      panel.setBounds(12, 10, 300, 358);
      getContentPane().add(panel);
      panel.setLayout(new BorderLayout(0, 0));

      JScrollPane scrollPane = new JScrollPane();
      
      //scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      //scrollPane.getVerticalScrollBar().setUnitIncrement(16);
   
      panel.add(scrollPane, BorderLayout.CENTER);

      chatArea = new JTextPane();
      chatArea.setBackground(Color.WHITE);
      
      doc = chatArea.getStyledDocument();
      
      chatArea.setEditable(false); // 수정불가
      scrollPane.setViewportView(chatArea); // 화면 보임
      
      
      Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
         
      Style s = doc.addStyle("blue",def);
      StyleConstants.setForeground(s, Color.blue);
       s = doc.addStyle("red",def);
       StyleConstants.setForeground(s,Color.red);
       s=doc.addStyle("black",def);
       StyleConstants.setForeground(s,Color.black);
       s=doc.addStyle("green",def);
       StyleConstants.setForeground(s,Color.green);
       s=doc.addStyle("gray",def);
       StyleConstants.setForeground(s,Color.gray);
       s=doc.addStyle("right",def);
       StyleConstants.setAlignment(s,StyleConstants.ALIGN_RIGHT);
       s=doc.addStyle("left",def);
       StyleConstants.setAlignment(s,StyleConstants.ALIGN_LEFT);
       s=doc.addStyle("center",def);
       StyleConstants.setAlignment(s,StyleConstants.ALIGN_CENTER);
       
   

      JPanel panel1 = new JPanel();
      // 글쓰는 패널
      panel1.setBounds(12, 378, 300, 34);
      getContentPane().add(panel1);
      panel1.setLayout(new BorderLayout(0, 0));

      chatField = new JTextField();
      panel1.add(chatField);
      chatField.setColumns(10);
      chatField.addKeyListener(new KeyAdapter() {
         // 엔터 버튼 이벤트
         @Override
         public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
               msgSummit();
            }
         }

      });
      

      // 참여자 패널
      JPanel panel2 = new JPanel();
      // 참여자 패널
      panel2.setBounds(324, 10, 150, 325);
      getContentPane().add(panel2);
      panel2.setLayout(new BorderLayout(0, 0));

      JScrollPane scrollPane1 = new JScrollPane();
      panel2.add(scrollPane1, BorderLayout.CENTER);

      uList = new JList(new DefaultListModel());
      model = (DefaultListModel) uList.getModel();
      scrollPane1.setViewportView(uList);

      // send button
      JButton roomSendBtn = new JButton("보내기");
      roomSendBtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            msgSummit();
            chatField.requestFocus();
         }
      });
      roomSendBtn.setBounds(324, 378, 75, 34);
      getContentPane().add(roomSendBtn);

      // exit button
      JButton roomExitBtn = new JButton("나가기");
      roomExitBtn.addMouseListener(new MouseAdapter() {      // 나가기 버튼
         @Override
         public void mouseClicked(MouseEvent e) {
            int ans = JOptionPane.showConfirmDialog(panel, "방을 삭제 하시겠습니까?", "삭제확인", JOptionPane.OK_CANCEL_OPTION);

            if (ans == 0) { // 삭제
               try {
                  client.getUser().getDos().writeUTF(User.GETOUT_ROOM + "/" + room.getRoomNum());
                  for (int i = 0; i < client.getUser().getRoomArray().size(); i++) {
                     if (client.getUser().getRoomArray().get(i).getRoomNum() == room.getRoomNum()) {
                        client.getUser().getRoomArray().remove(i);
                     }
                  }
                  setVisible(false);
               } catch (IOException e1) {
                  e1.printStackTrace();
               }
            } else { // 그냥 나가기
               setVisible(false);
            }
         }
      });
      roomExitBtn.setBounds(400, 378, 75, 34);
      getContentPane().add(roomExitBtn);
      
      JButton colorBT = new JButton("색 상");
      colorBT.setBounds(323, 345, 75, 23);
       getContentPane().add(colorBT);
       colorBT.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent arg0) {
                //주석처리 코드는 채팅 글자 또는 채팅 배경 중 하나만 변경됨
                Color c1 = JColorChooser.showDialog(RoomUI.this, "배경 색상 선택", Color.CYAN);               
                chatArea.setBackground(c1);// -> 채팅 배경 색
                new ColorChoose(chatArea);
             }
          });
       
       //패널4 추가하여 배경을 나타냄
      icon2 = new ImageIcon("C:\\Users\\PC\\Desktop\\back.jpg");
       
      JPanel panel4 = new JPanel() {
          public void paintComponent(Graphics g) {
              //Approach 1: Dispaly image at at full size
              g.drawImage(icon2.getImage(), 0, 0, null);
              // Approach 2: Scale image to size of component
              Dimension d = getSize();
              // g.drawImage(icon.getImage(), 0, 0, d.width, d.height, null);
              // Approach 3: Fix the image position in the scroll pane
              // Point p = scrollPane.getViewport().getViewPosition();
              // g.drawImage(icon.getImage(), p.x, p.y, null);
              setOpaque(false); //그림을 표시하게 설정,투명하게 조절
              super.paintComponent(g);
          }
      };
  
  
      panel4.setBounds(0, 0, 486, 419); // 로그인 비밀번호 나타내기
      getContentPane().add(panel4); // 로그인 화면 나타내기
      panel4.setLayout(null);
      
      
      //이뫼티콘
      JButton btnNewButton = new JButton("New");
      btnNewButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
         }
      });
      
      
      btnNewButton.setBounds(400, 345, 74, 23);
      panel4.add(btnNewButton);

      JMenuBar menuBar = new JMenuBar();
      setJMenuBar(menuBar);
      setVisible(true);
      
      //////////////////////////////////////
      // 채팅내용 저장 및 가져오기 메뉴
      JMenu mnuSaveChat = new JMenu("채팅저장");
      mnuSaveChat.addActionListener(this);
      menuBar.add(mnuSaveChat);
      
      JMenuItem mitSaveChatToFile = new JMenuItem("파일로저장");
      mitSaveChatToFile.addActionListener(this);
      mnuSaveChat.add(mitSaveChatToFile);
      
      JMenuItem dbSaveChatToFile = new JMenuItem("DB로 저장");
      dbSaveChatToFile.addActionListener(this);
      mnuSaveChat.add(dbSaveChatToFile);
      
      JMenu mnuLoadChat = new JMenu("저장채팅확인");
      mnuLoadChat.addActionListener(this);
      menuBar.add(mnuLoadChat);
      JMenuItem mitLoadChatFromFile = new JMenuItem("파일열기");
      mitLoadChatFromFile.addActionListener(this);
      mnuLoadChat.add(mitLoadChatFromFile);
      
      ///////////////////////////////////////////////
      
      chatField.requestFocus();
      this.addWindowListener(new WindowAdapter() {   // 윈도우 나가기
         @Override
         public void windowClosing(WindowEvent e) {
            try {
               client.getUser().getDos().writeUTF(User.GETOUT_ROOM + "/" + room.getRoomNum());
               for (int i = 0; i < client.getUser().getRoomArray().size(); i++) {
                  if (client.getUser().getRoomArray().get(i).getRoomNum() == room.getRoomNum()) {
                     client.getUser().getRoomArray().remove(i);
                  }
               }
            } catch (IOException e1) {
               e1.printStackTrace();
            }
         }
      });
   }

   private void msgSummit() {
      String string = chatField.getText();
      if (!string.equals("")) {
         try {
            // 채팅방에 메시지 보냄
            client.getDos()
                  .writeUTF(User.ECHO02 + "/" + room.getRoomNum() + "/" + client.getUser().toString() + string);
            chatField.setText("");
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }
   
   //현재시간을 문자열(형태: yyyymmdd_hh24miss) 반환
   public static String getCurrentDateTime() {
      GregorianCalendar calendar = new GregorianCalendar();
      String strDateTime = ""; 
      int year   = calendar.get(Calendar.YEAR);
      int month  = calendar.get(Calendar.MONTH) + 1;
      int day    = calendar.get(Calendar.DAY_OF_MONTH);
//      int hour   = calendar.get(Calendar.HOUR);
      int hour24 = calendar.get(Calendar.HOUR_OF_DAY);
      int minute = calendar.get(Calendar.MINUTE);
      int second = calendar.get(Calendar.SECOND);
      String strYear  = String.valueOf(year);
      String strMonth = month < 10 ? "0" + month : String.valueOf(month);
      String strDay = day < 10 ? "0" + day : String.valueOf(day);
      String strHour = hour24 < 10 ? "0" + hour24 : String.valueOf(hour24);
      String strMinute = minute < 10 ? "0" + minute : String.valueOf(minute);
      String strSecond = second < 10 ? "0" + second : String.valueOf(second);
      
      strDateTime = strYear+ "년" + strMonth + "월" + strDay + "일" + "_" + strHour + strMinute + strSecond;
      return strDateTime;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      switch (e.getActionCommand()) {
         case "파일로저장":
            String filename = UtilFileIO.saveFile(chatArea);
            JOptionPane.showMessageDialog(chatArea.getParent(), 
                  "채팅내용을 파일명(" + filename + ")으로 저장하였습니다", 
                  "채팅백업", JOptionPane.INFORMATION_MESSAGE);
            break;
         case "DB로 저장":
             try {
                 Class.forName("oracle.jdbc.driver.OracleDriver"); // 알아서 conn으로 연결
                 info = new Properties();
                 info.setProperty("user", "c##scott");
                 info.setProperty("password", "tiger");
                 cnn = DriverManager.getConnection(url, info); // 연결할 정보를 가지고있는 드라이버매니저를 던진다
                 stmt = cnn.createStatement();

                 LocalDate now = LocalDate.now(); // 포맷 정의         
                 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd"); // 포맷 적용
                 String formatedNow = now.format(formatter);

                 sql = "insert into textdb(date_1,text_1) values" + "(" +"'"+formatedNow+"'" + ","  +"'" + chatArea.getText() +"'"+")";   
                 stmt.executeUpdate(sql);

                 
                 JOptionPane.showMessageDialog(chatArea.getParent(), 
                         "채팅내용을 DB로 저장하였습니다", 
                         "DB백업", JOptionPane.INFORMATION_MESSAGE);
              } catch (Exception ee) {
                 System.out.println("문제있음");
              }
           break;
         case "파일열기":
            filename = UtilFileIO.getFilenameFromFileOpenDialog("./");
            String text = UtilFileIO.loadFile(filename);
            TextViewUI textview = new TextViewUI(text);
            break;
         }
   }
}
   