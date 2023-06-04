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
    * 필요한 변수선언
    * */
   Connection con;
   Statement st;
   PreparedStatement ps;
   ResultSet rs;

   /**
    * 로드 연결을 위한 생성자
    * */
   public UserDefaultJTableDAO() {
      try {
         // 로드
         Class.forName("oracle.jdbc.driver.OracleDriver");
         // 연결
         con = DriverManager
               .getConnection("jdbc:oracle:thin:@172.16.20.120:1521:xe",
                     "c##scott", "tiger");

      } catch (ClassNotFoundException e) {
         System.out.println(e + "=> 로드 fail");
      } catch (SQLException e) {
         System.out.println(e + "=> 연결 fail");
      }
   }//생성자 

   /**
    * DB닫기 기능 메소드
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
    * 인수로 들어온 ID에 해당하는 레코드 검색하여 중복여부 체크하기 리턴값이 true =사용가능 , false = 중복임
    * */
    //안씀
   public boolean getIdByCheck(String id) {
      boolean result = true;

      try {
         ps = con.prepareStatement("SELECT * FROM joinDB WHERE id=?");
         ps.setString(1, id.trim());
         rs = ps.executeQuery(); //실행
         if (rs.next())
            result = false; //레코드가 존재하면 false

      } catch (SQLException e) {
         System.out.println(e + "=>  getIdByCheck fail");
      } finally {
         dbClose();
      }

      return result;

   }//getIdByCheck()

   /**
    * userlist의 모든 레코드 조회
    * */
    //콤보박스 ALL 선택후 검색 버튼 클릭 및 회원관리 창 접속시 회원정보 플로팅 메소드
   public void userSelectAll(DefaultTableModel t_model) {
      try {
         st = con.createStatement();
         rs = st.executeQuery("select * from joinDB order by id");

         // DefaultTableModel에 있는 기존 데이터 지우기
         for (int i = 0; i < t_model.getRowCount();) {
            t_model.removeRow(0);
         }

         while (rs.next()) { // 검색된 데이터를 getString을 이용해 컬럼별로 전부 가져온다.
            Object data[] = { rs.getString(1), rs.getString(2),
                  rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6) };

            t_model.addRow(data); //DefaultTableModel에 레코드 추가
         }

      } catch (SQLException e) {
         System.out.println(e + "=> userSelectAll fail");
      } finally {
         dbClose();
      }
   }//userSelectAll()

   /**
    * ID에 해당하는 레코드 삭제하기
    * */
   public int userDelete(String id) {
      int result = 0;
      try {
         ps = con.prepareStatement("delete joinDB where id = ? "); //삭제 sql문
         ps.setString(1, id.trim()); //검색된 아이디로 삭제함
         result = ps.executeUpdate();

      } catch (SQLException e) {
         System.out.println(e + "=> userDelete fail");
      }finally {
         dbClose();
      }

      return result;
   }//userDelete()

   /**
    * ID에 해당하는 레코드 수정하기
    * */
   public int userUpdate(UserJDailogGUI user) {
      int result = 0;
      String sql = "UPDATE joinDB SET pwTf=? WHERE id=?";

      try {
         ps = con.prepareStatement(sql);
         // ?의 순서대로 입력된 값 넣기
         ps.setString(1, user.password.getText());
         ps.setString(2, user.name.getText());
         ps.setString(3, user.birth.getText());
         ps.setString(4, user.email.getText());
         ps.setString(5, user.ip.getText());
         ps.setString(6, user.id.getText().trim());

         // 실행하기
         result = ps.executeUpdate(); //sql문 실행

      } catch (SQLException e) {
         System.out.println(e + "=> userUpdate fail");
      } finally {
         dbClose();
      }

      return result;
   }//userUpdate()

   /**
    * 검색단어에 해당하는 레코드 검색하기 (like연산자를 사용하여 _, %를 사용할때는 PreparedStatemnet안된다. 반드시
    * Statement객체를 이용함)
    * */
   public void getUserSearch(DefaultTableModel dt, String fieldName,
         String word) {
      //검색창에 검색어를 가지고 sql문 수행 fieldName = 콤보 데이터를 가져옴, word = 검색창에 입력된 검색어
      String sql = "SELECT * FROM joinDB WHERE " + fieldName.trim() 
            + " LIKE '%" + word.trim() + "%'";

      try {
         st = con.createStatement();
         rs = st.executeQuery(sql);

         // DefaultTableModel에 있는 기존 데이터 지우기
         for (int i = 0; i < dt.getRowCount();) {
            dt.removeRow(0);
         }

         while (rs.next()) { // 검색된 데이터를 getString을 이용해 컬럼별로 전부 가져온다.
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
   
   
}// 클래스끝