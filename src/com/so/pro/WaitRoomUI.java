package com.so.pro;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.EventHandler;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

public class WaitRoomUI extends JFrame implements ActionListener {

   MsgBox msgbox = new MsgBox();
   String temp,id,name;
   
   int lastRoomNum = 100;
   JButton makeRoomBtn, getInRoomBtn, whisperBtn, sendBtn;
   JTree userTree;
   JList roomList;
   JTextField chatField;
   JTextArea waitRoomArea;
   JLabel lbid, lbnick, lbname;
   JTextField lbip;

   EightClient client;
   ArrayList<User> userArray; // ����� ��� �迭
   String currentSelectedTreeNode;
   DefaultListModel model;
   DefaultMutableTreeNode level1;      
   DefaultMutableTreeNode level2;   
   DefaultMutableTreeNode level3;   
   
   JScrollPane scrollPane;
    ImageIcon icon1,icon2;
    LoginUI login;
    
    Statement stmt = null;
    ResultSet rs = null;
    String url = "jdbc:oracle:thin:@172.16.20.120:1521:xe"; // ����Ŭ ��Ʈ��ȣ1521/@���Ŀ��� IP�ּ�
    String sql = null;
    Properties info = null;
    Connection cnn = null;

   public WaitRoomUI(EightClient eigClient) {
      setTitle("Octopus Chatting");
      userArray = new ArrayList<User>();
      client = eigClient;
      initialize();
   }

   private void initialize() {
      

      
/*
      icon1 = new ImageIcon("C:\\Users\\rnrnr\\������Ʈ\\image\\univer_image.jpg");
      this.setIconImage(icon1.getImage());//Ÿ��Ʋ�ٿ� �̹����ֱ�
      */
      setBounds(100, 100, 527, 528);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setResizable(false);

      JMenuBar menuBar = new JMenuBar();
      setJMenuBar(menuBar);

      JMenu basicMenus = new JMenu("����");
      basicMenus.addActionListener(this);
      menuBar.add(basicMenus);

      JMenuItem mitSaveChatToFile = new JMenuItem("���Ϸ�����");
      mitSaveChatToFile.addActionListener(this);
      basicMenus.add(mitSaveChatToFile);
      
      JMenuItem dbSaveChatToFile = new JMenuItem("DB�� ����");
      dbSaveChatToFile.addActionListener(this);
      basicMenus.add(dbSaveChatToFile);
      
      JMenuItem mitLoadChatFromFile = new JMenuItem("���Ͽ���");
      mitLoadChatFromFile.addActionListener(this);
      basicMenus.add(mitLoadChatFromFile);
      
      JMenuItem exitItem = new JMenuItem("����");
      exitItem.addActionListener(this);
      basicMenus.add(exitItem);

      JMenu updndel = new JMenu("����/Ż��");
      updndel.addActionListener(this);
      menuBar.add(updndel);

      JMenuItem changeInfo = new JMenuItem("ȸ������ ����");
      changeInfo.addActionListener(this);
      updndel.add(changeInfo);
      
      JMenuItem withdrawMem = new JMenuItem("ȸ�� Ż��");
      withdrawMem.addActionListener(this);
      updndel.add(withdrawMem);
      
      JMenu helpMenus = new JMenu("����");
      helpMenus.addActionListener(this);
      menuBar.add(helpMenus);

      JMenuItem proInfoItem = new JMenuItem("���α׷� ����");
      proInfoItem.addActionListener(this);
      helpMenus.add(proInfoItem);
      getContentPane().setLayout(null);

      JPanel room = new JPanel();
      room.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "ä �� ��", TitledBorder.CENTER, TitledBorder.TOP, null, null));
      room.setBounds(250, 10, 239, 215);
      getContentPane().add(room);
      room.setLayout(new BorderLayout(0, 0));

      JScrollPane scrollPane = new JScrollPane();
      room.add(scrollPane, BorderLayout.CENTER);

      // ����Ʈ ��ü�� �� ����
      roomList = new JList(new DefaultListModel());
      roomList.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            int i = roomList.getFirstVisibleIndex();
            // System.out.println(">>>>>>>>>>>" + i);
            if (i != -1) {
               // ä�ù� ��� �� �ϳ��� ������ ���,
               // ������ ���� ���ȣ�� ����
               String temp = (String) roomList.getSelectedValue();
               if(temp.equals(null)){
                  return;
               }

               try {
                  client.getUser().getDos().writeUTF(User.UPDATE_SELECTEDROOM_USERLIST + "/" + temp.substring(0, 3));
               } catch (IOException e1) {
                  e1.printStackTrace();
               }
            }
         }
      });
      model = (DefaultListModel) roomList.getModel();
      scrollPane.setViewportView(roomList);

      JPanel panel2 = new JPanel();
      room.add(panel2, BorderLayout.SOUTH);

      makeRoomBtn = new JButton("�� �����");
      makeRoomBtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
         }
      });
      makeRoomBtn.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent arg0) {
            // �游��� ��ư Ŭ��
            createRoom();
         }
      });
      panel2.setLayout(new GridLayout(0, 2, 0, 0));
      panel2.add(makeRoomBtn);

      getInRoomBtn = new JButton("�� �����ϱ�");
      getInRoomBtn.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent arg0) {
            // �� ����
            getIn();
         }
      });
      panel2.add(getInRoomBtn);
      level1 = new DefaultMutableTreeNode("������");
      level2 = new DefaultMutableTreeNode();
      level3 = new DefaultMutableTreeNode("����");
      level1.add(level3);

      DefaultTreeModel model = new DefaultTreeModel(level1);

      JPanel waitroom = new JPanel();
      
      waitroom.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "�� �� ��",   TitledBorder.CENTER, TitledBorder.TOP, null, Color.DARK_GRAY));
      waitroom.setBounds(250, 235, 239, 185);
      getContentPane().add(waitroom);
      waitroom.setLayout(new BorderLayout(0, 0));

      JPanel panel = new JPanel();
      waitroom.add(panel, BorderLayout.SOUTH);
      panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

      JScrollPane scrollPane4 = new JScrollPane();
      panel.add(scrollPane4);

      chatField = new JTextField();

      chatField.addKeyListener(new KeyAdapter() {
         @Override
         public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
               msgSummit();
            }
         }

      });
      scrollPane4.setViewportView(chatField);
      chatField.setColumns(10);

      sendBtn = new JButton("������");
      sendBtn.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            msgSummit();
            chatField.requestFocus();
         }
      });
      panel.add(sendBtn);

      JScrollPane scrollPane2 = new JScrollPane();
      waitroom.add(scrollPane2, BorderLayout.CENTER);

      waitRoomArea = new JTextArea();
      waitRoomArea.setEditable(false);
      scrollPane2.setViewportView(waitRoomArea);
      
      //�г�4 �߰��Ͽ� ����� ��Ÿ��
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
             setOpaque(false); //�׸��� ǥ���ϰ� ����,�����ϰ� ����
             super.paintComponent(g);
         }
     };
 
 
     panel4.setBounds(0, 0, 700, 500); // �α��� ��й�ȣ ��Ÿ����
     getContentPane().add(panel4); // �α��� ȭ�� ��Ÿ����
     panel4.setLayout(null);
     
           JPanel user = new JPanel();
           user.setBounds(12, 10, 228, 409);
           panel4.add(user);
           user.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),   "����� ���", TitledBorder.CENTER,   TitledBorder.TOP, null, null));
           user.setLayout(new BorderLayout(0, 0));
           
                 JScrollPane scrollPane1 = new JScrollPane();
                 user.add(scrollPane1, BorderLayout.CENTER);
                 
                       // ����ڸ��, Ʈ������
                       userTree = new JTree(); 
                       userTree.addTreeSelectionListener(new TreeSelectionListener() {
                          @Override
                          public void valueChanged(TreeSelectionEvent e) {
                             currentSelectedTreeNode = e.getPath().getLastPathComponent().toString();
                          }
                       });
                       
                             DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
                             renderer.setLeafIcon(new ImageIcon("C:\\Users\\PC\\Desktop\\profile.jpeg")); // user(������)
                             renderer.setClosedIcon(new ImageIcon("C:\\Users\\PC\\Desktop\\mark.png")); // wait(����)
                             renderer.setOpenIcon(new ImageIcon("C:\\Users\\PC\\Desktop\\mark.png")); // open(��
                             
                                   userTree.setCellRenderer(renderer);
                                   userTree.setEditable(false);
                                   userTree.setModel(model);
                                   
                                         scrollPane1.setViewportView(userTree);
                                         
                                               JPanel panel1 = new JPanel();
                                               user.add(panel1, BorderLayout.SOUTH);
                                               panel1.setLayout(new GridLayout(1, 0, 0, 0));
                                               
                                                     whisperBtn = new JButton("�ӼӸ�");
                                                     
                                                     
                                                     whisperBtn.addActionListener(this);
                                                     panel1.add(whisperBtn);
                                                     /*
                                                     JButton btnNewButton = new JButton("1 �� 1 ä�ù� ����");
                                                     btnNewButton.addActionListener(new ActionListener() {
                                                        public void actionPerformed(ActionEvent e) {
                                                        }
                                                     });
                                                     
                                                    
                                                     
                                                     btnNewButton.setBounds(12, 429, 209, 23);
                                                     panel4.add(btnNewButton);
                                                     */
      JPanel info = new JPanel();
      lbid = new JLabel("-");
      info.add(lbid);
      lbname = new JLabel("-");
      info.add(lbname);
      lbnick = new JLabel("-");
      info.add(lbnick);
      lbip = new JTextField();
      lbip.setEditable(false);
      info.add(lbip);
      lbip.setColumns(10);

      chatField.requestFocus();
      setVisible(true);
      chatField.requestFocus();

      this.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            exit01();
         }
      });
      
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      id = client.user.getId();
      switch (e.getActionCommand()) {
      case "�ӼӸ�":
         // �г��� �����ϰ� ���̵� ����
         StringTokenizer token = new StringTokenizer(currentSelectedTreeNode, "("); // ��ū ����
         temp = token.nextToken(); // ��ū���� �и��� ��Ʈ��
         temp = token.nextToken();
         id = "/" + temp.substring(0, temp.length() - 1) + " ";
         chatField.setText(id);
         chatField.requestFocus();
         break;
      // �޴�1 ���� �޴�
      case "ȸ������ ����":
         DBRevise reDB = new DBRevise();
         reDB.myInfo(id);
         break;
      case "ȸ�� Ż��":
         DBDelete delDB = new DBDelete();

         int ans = JOptionPane.showConfirmDialog(this, "���� Ż�� �Ͻðڽ��ϱ�?", "Ż��Ȯ��", JOptionPane.OK_CANCEL_OPTION);

         if (ans == 0) {
            int i = 0;
            i = delDB.InfoDel(id);
            if (i == 0) {
               // msgbox.messageBox(this, "Ż��� ������..:)");
            } else {
               msgbox.messageBox(this, "Ż�� �����Ͽ����ϴ�..:(");
               exit01();
            }
         }
         break;
      case "����":
         int ans1 = JOptionPane.showConfirmDialog(this, "���� ���� �Ͻðڽ��ϱ�?", "����Ȯ��", JOptionPane.OK_CANCEL_OPTION);
         if (ans1 == 0) {
            // System.exit(0); // ���� ����
            try {
               client.getUser().getDos().writeUTF(User.LOGOUT);
            } catch (IOException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
         }
         break;
      case "���Ϸ�����":
         String filename = UtilFileIO.saveFile(waitRoomArea);
         JOptionPane.showMessageDialog(waitRoomArea.getParent(), "ä�ó����� ���ϸ�(" + filename + ")���� �����Ͽ����ϴ�", "ä�ù��", JOptionPane.INFORMATION_MESSAGE);
         break;
         
      case "DB�� ����":
          try {
              Class.forName("oracle.jdbc.driver.OracleDriver"); // �˾Ƽ� conn���� ����
              info = new Properties();
              info.setProperty("user", "c##scott");
              info.setProperty("password", "tiger");
              cnn = DriverManager.getConnection(url, info); // ������ ������ �������ִ� ����̹��Ŵ����� ������
              stmt = cnn.createStatement();

              LocalDate now = LocalDate.now(); // ���� ����         
              DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd"); // ���� ����
              String formatedNow = now.format(formatter);

              sql = "insert into waittextdb(date_1,text_1) values" + "(" +"'"+formatedNow+"'" + ","  +"'" + waitRoomArea.getText() +"'"+")";   
              stmt.executeUpdate(sql);

              
              JOptionPane.showMessageDialog(waitRoomArea.getParent(), 
                      "ä�ó����� DB�� �����Ͽ����ϴ�", 
                      "DB���", JOptionPane.INFORMATION_MESSAGE);
           } catch (Exception ee) {
              System.out.println("��������");
           }
          break;
          
      case "���Ͽ���":
         filename = UtilFileIO.getFilenameFromFileOpenDialog("./");
         if (filename == "") break;
         String text = UtilFileIO.loadFile(filename);
         TextViewUI textview = new TextViewUI(text);
         break;
      case "���α׷� ����":
         maker();
         break;
      }

   }
   
   private void msgSummit() {
      String string = chatField.getText();// �޽�������

      if (!string.equals("")) {
         if (string.substring(0, 1).equals("/")) {
            
            StringTokenizer token = new StringTokenizer(string, " "); // ��ū ����
            String id = token.nextToken(); // ��ū���� �и��� ��Ʈ��
            String msg = token.nextToken();
            
            try {
               client.getDos().writeUTF(User.WHISPER + id + "/" + msg);
               waitRoomArea.append(id + "�Կ��� �ӼӸ� : " + msg + "\n");
            } catch (IOException e) {
               e.printStackTrace();
            }
            chatField.setText("");
         } else {

            try {
               // ���ǿ� �޽��� ����
               client.getDos().writeUTF(User.ECHO01 + "/" + string);
            } catch (IOException e) {
               e.printStackTrace();
            }
            chatField.setText("");
         }
      }
   }

   private void exit01() {
      try {
         client.getUser().getDos().writeUTF(User.LOGOUT);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private void createRoom() {
      String roomname = JOptionPane.showInputDialog("    ��ȭ�� �̸� �Է�");////////////
      if(roomname==null) {   // ��� ��ư
         
      } else {
         Room newRoom = new Room(roomname);   // �� ��ü ����
         newRoom.setRoomNum(lastRoomNum);
         newRoom.setrUI(new RoomUI(client, newRoom));
         
         // Ŭ���̾�Ʈ�� ������ �� ��Ͽ� �߰�
         client.getUser().getRoomArray().add(newRoom);
         
         try {
            client.getDos().writeUTF(User.CREATE_ROOM + "/" + newRoom.toProtocol());
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   
   }

   private void getIn() {
      // ������ �� ����
      String selectedRoom = (String) roomList.getSelectedValue();
      StringTokenizer token = new StringTokenizer(selectedRoom, "/"); // ��ū ����
      String rNum = token.nextToken();
      String rName = token.nextToken();

      Room theRoom = new Room(rName); // �� ��ü ����
      theRoom.setRoomNum(Integer.parseInt(rNum)); // ���ȣ ����
      theRoom.setrUI(new RoomUI(client, theRoom)); // UI

      // Ŭ���̾�Ʈ�� ������ �� ��Ͽ� �߰�
      client.getUser().getRoomArray().add(theRoom);

      try {
         client.getDos().writeUTF(User.GETIN_ROOM + "/" + theRoom.getRoomNum());
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void maker() {
      JDialog maker = new JDialog();
      Maker m = new Maker();
      maker.setTitle("���α׷� ����");
      maker.getContentPane().add(m);
      maker.setSize(400, 170);
      maker.setVisible(true);
      maker.setLocation(400, 350);
      maker.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
   }
}

class Maker extends JPanel {
   public Maker() {
      super();
      initialize();
   }

   private void initialize() {
      this.setLayout(new GridLayout(3, 1));

      JLabel j1 = new JLabel("       ���α׷� ������ : �Ѻ� Class6 5��      ");
      JLabel j2 = new JLabel("       ������ ��� : Octopus Chatting      ");
      JLabel j3 = new JLabel("       ���α׷� ���� : 1.6.9 v  ( 2016 . 8 . 31 )      ");

      this.add(j1);
      this.add(j2);
      this.add(j3);
   }
}