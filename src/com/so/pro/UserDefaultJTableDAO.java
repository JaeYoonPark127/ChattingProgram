package com.so.pro;

import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
//UserDefaultJTableDAO.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class UserDefaultJTableDAO {
   public static String id;    
   
   /**
    * �ʿ��� ��������
    * */
   Connection con;
   Statement st;
   PreparedStatement ps;
   ResultSet rs;

   /**
    * �ε� ������ ���� ������
    * */
   public UserDefaultJTableDAO() {
      try {
         // �ε�
         Class.forName("oracle.jdbc.driver.OracleDriver");
         // ����
         con = DriverManager
               .getConnection("jdbc:oracle:thin:@172.16.20.120:1521:xe",
                     "c##scott", "tiger");

      } catch (ClassNotFoundException e) {
         System.out.println(e + "=> �ε� fail");
      } catch (SQLException e) {
         System.out.println(e + "=> ���� fail");
      }
   }//������ 

   /**
    * DB�ݱ� ��� �޼ҵ�
    * */
   public void dbClose() {
      try {
         if (rs != null) rs.close();
         if (st != null) st.close();
         if (ps != null) ps.close();
      } catch (Exception e) {
         System.out.println(e + "=> dbClose fail");
      }
   }//dbClose() ---

   /**
    * �μ��� ���� ID�� �ش��ϴ� ���ڵ� �˻��Ͽ� �ߺ����� üũ�ϱ� ���ϰ��� true =��밡�� , false = �ߺ���
    * */
    //�Ⱦ�
   public boolean getIdByCheck(String id) {
      boolean result = true;

      try {
         ps = con.prepareStatement("SELECT * FROM joinDB WHERE id=?");
         ps.setString(1, id.trim());
         rs = ps.executeQuery(); //����
         if (rs.next())
            result = false; //���ڵ尡 �����ϸ� false

      } catch (SQLException e) {
         System.out.println(e + "=>  getIdByCheck fail");
      } finally {
         dbClose();
      }

      return result;

   }//getIdByCheck()

   /**
    * userlist�� ��� ���ڵ� ��ȸ
    * */
    //�޺��ڽ� ALL ������ �˻� ��ư Ŭ�� �� ȸ������ â ���ӽ� ȸ������ �÷��� �޼ҵ�
   public void userSelectAll(DefaultTableModel t_model) {
      try {
         st = con.createStatement();
         rs = st.executeQuery("select * from joinDB order by id");

         // DefaultTableModel�� �ִ� ���� ������ �����
         for (int i = 0; i < t_model.getRowCount();) {
            t_model.removeRow(0);
         }

         while (rs.next()) { // �˻��� �����͸� getString�� �̿��� �÷����� ���� �����´�.
            Object data[] = { rs.getString(1), rs.getString(2),
                  rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6) };

            t_model.addRow(data); //DefaultTableModel�� ���ڵ� �߰�
         }

      } catch (SQLException e) {
         System.out.println(e + "=> userSelectAll fail");
      } finally {
         dbClose();
      }
   }//userSelectAll()

   /**
    * ID�� �ش��ϴ� ���ڵ� �����ϱ�
    * */
   public int userDelete(String id) {
      int result = 0;
      try {
         ps = con.prepareStatement("delete joinDB where id = ? "); //���� sql��
         ps.setString(1, id.trim()); //�˻��� ���̵�� ������
         result = ps.executeUpdate();

      } catch (SQLException e) {
         System.out.println(e + "=> userDelete fail");
      }finally {
         dbClose();
      }

      return result;
   }//userDelete()

   /**
    * ID�� �ش��ϴ� ���ڵ� �����ϱ�
    * */
   public int userUpdate(UserJDailogGUI user) {
      int result = 0;
      String sql = "UPDATE joinDB SET pwTf=? WHERE id=?";

      try {
         ps = con.prepareStatement(sql);
         // ?�� ������� �Էµ� �� �ֱ�
         ps.setString(1, user.password.getText());
         ps.setString(2, user.name.getText());
         ps.setString(3, user.birth.getText());
         ps.setString(4, user.email.getText());
         ps.setString(5, user.ip.getText());
         ps.setString(6, user.id.getText().trim());

         // �����ϱ�
         result = ps.executeUpdate(); //sql�� ����

      } catch (SQLException e) {
         System.out.println(e + "=> userUpdate fail");
      } finally {
         dbClose();
      }

      return result;
   }//userUpdate()

   /**
    * �˻��ܾ �ش��ϴ� ���ڵ� �˻��ϱ� (like�����ڸ� ����Ͽ� _, %�� ����Ҷ��� PreparedStatemnet�ȵȴ�. �ݵ��
    * Statement��ü�� �̿���)
    * */
   public void getUserSearch(DefaultTableModel dt, String fieldName,
         String word) {
      //�˻�â�� �˻�� ������ sql�� ���� fieldName = �޺� �����͸� ������, word = �˻�â�� �Էµ� �˻���
      String sql = "SELECT * FROM joinDB WHERE " + fieldName.trim() 
            + " LIKE '%" + word.trim() + "%'";

      try {
         st = con.createStatement();
         rs = st.executeQuery(sql);

         // DefaultTableModel�� �ִ� ���� ������ �����
         for (int i = 0; i < dt.getRowCount();) {
            dt.removeRow(0);
         }

         while (rs.next()) { // �˻��� �����͸� getString�� �̿��� �÷����� ���� �����´�.
            Object data[] = { rs.getString(1), rs.getString(2), rs.getString(3),
                  rs.getString(4), rs.getString(5), rs.getString(6) };

            dt.addRow(data);
         }

      } catch (SQLException e) {
         System.out.println(e + "=> getUserSearch fail");
      } finally {
         dbClose();
      }

   }//getUserSearch()
   
   
}// Ŭ������