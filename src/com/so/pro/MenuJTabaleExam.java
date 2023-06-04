package com.so.pro;
//MenuJTabaleExam.java

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;



import javax.swing.JLabel;
 
 
public class MenuJTabaleExam extends JFrame implements ActionListener {
    JMenu m = new JMenu("관리");
    JMenuItem insert = new JMenuItem("가입");
    JMenuItem update = new JMenuItem("수정");
    JMenuItem delete = new JMenuItem("삭제");
    JMenuItem quit = new JMenuItem("종료");
    JMenuBar mb = new JMenuBar();
 
    String[] name = { "id", "password", "name",  "birth" ,"email" , "zipcode"};
 
    DefaultTableModel dt = new DefaultTableModel(name, 0);
    JTable jt = new JTable(dt);
    JScrollPane jsp = new JScrollPane(jt);
    /*
     * South 영역에 추가할 Componet들
     */
    JPanel p = new JPanel();
    //콤보박스에 추가할 이름을 String 배열로 선언 및 초기화
    String[] comboName = { "  ALL  ", "  ID  ", " PASSWORD ", " NAME " , " BIRTH ", " EMAIL "," ZIPCODE "};
 
    //콤보박스에 이름 배열을 삽입
    JComboBox combo = new JComboBox(comboName);
    JTextField searchField = new JTextField(20);
    JButton search = new JButton("검색");
 
    UserDefaultJTableDAO dao = new UserDefaultJTableDAO();
    
    public static JLabel imageLabel;
    private final JLabel lblNewLabel = new JLabel("회원 사진");
 
    /**
     * 화면구성 및 이벤트등록
     */
    public MenuJTabaleExam() {
       
        super("회원관리프로그램");
        jt.setAutoCreateRowSorter(true);//테이블 정렬 아이콘
        
        //메뉴아이템을 메뉴에 추가
        m.add(insert);
        m.add(update);
        m.add(delete);
        m.add(quit);
        //메뉴를 메뉴바에 추가
        mb.add(m);
       
        //윈도우에 메뉴바 세팅
        setJMenuBar(mb);
 
        imageLabel = new JLabel("사진위치");
        imageLabel.setPreferredSize(new Dimension(100, 100));
        p.setPreferredSize(new Dimension(100, 110));
        // South영역
        p.setBackground(Color.gray);
        
        p.add(lblNewLabel);
        
        p.add(imageLabel);
        p.add(combo);
        p.add(searchField);
        p.add(search);
 
        getContentPane().add(jsp, "Center"); //회원정보 테이블 패널 삽입
        getContentPane().add(p, "South"); //검색 패널 삽입
 
        setSize(1400, 900);
        setVisible(true);
 
        //관리창만 나가지도록 함
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // 이벤트등록
        insert.addActionListener(this);
        update.addActionListener(this);
        delete.addActionListener(this);
        quit.addActionListener(this);
        search.addActionListener(this);
 
        // 모든레코드를 검색하여 DefaultTableModle에 올리기
        dao.userSelectAll(dt);
       
        //첫번행 선택.
        if (dt.getRowCount() > 0)
            jt.setRowSelectionInterval(0, 0);
        
        setLocationRelativeTo(null);
        
        jt.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseClicked(MouseEvent e) {
    			if( jt.getSelectedRow() >= 0) {
    				//System.out.println(jt.getSelectedRow()); 회원정보 출력 테이블의 각 행의 값을 알 수 있음
    				//사용자들은 1행 1열부터 시작한다고 할 수 있지만 JTable은 0행 0열부터 시작한다는 것을 알 수 있음
    				String id = jt.getValueAt(jt.getSelectedRow(), 0).toString();
    				
    			}
    		}
    	});
 
    }// 생성자끝
 
    /**
     * main메소드 작성
     */

    /**
     * 가입/수정/삭제/검색기능을 담당하는 메소드
     * */   
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == insert) {// 가입 메뉴아이템 클릭
        	DBJoin add = new DBJoin();
        	add.JoinDBPanel();
        } else if (e.getSource() == update) {// 수정 메뉴아이템 클릭
            new UserJDailogGUI(this, "수정");
 
        } else if (e.getSource() == delete) {// 삭제 메뉴아이템 클릭
            // 현재 Jtable의 선택된 행과 열의 값을 얻어온다.
            int row = jt.getSelectedRow();
            System.out.println("선택행 : " + row);
 
            Object obj = jt.getValueAt(row, 0);// 행 열에 해당하는 value
            System.out.println("값 : " + obj);
 
            if (dao.userDelete(obj.toString()) > 0) {
                UserJDailogGUI.messageBox(this, "회원이 삭제되었습니다.");
               
                //리스트 갱신
                dao.userSelectAll(dt);
                if (dt.getRowCount() > 0)
                    jt.setRowSelectionInterval(0, 0);
 
            } else {
                UserJDailogGUI.messageBox(this, "회원이 삭제되지 않았습니다.");
            }
 
        } else if (e.getSource() == quit) {// 종료 메뉴아이템 클릭
        	dispose(); 
        } else if (e.getSource() == search) {// 검색 버튼 클릭
            // JComboBox에 선택된 value 가져오기
            String fieldName = combo.getSelectedItem().toString();
            System.out.println("필드명 " + fieldName);
 
            if (fieldName.trim().equals("ALL")) {// 전체검색
                dao.userSelectAll(dt);
                if (dt.getRowCount() > 0)
                    jt.setRowSelectionInterval(0, 0);
            } else {
                if (searchField.getText().trim().equals("")) {
                    UserJDailogGUI.messageBox(this, "검색단어를 입력해주세요!");
                    searchField.requestFocus();
                } else {// 검색어를 입력했을경우
                    dao.getUserSearch(dt, fieldName, searchField.getText());
                    if (dt.getRowCount() > 0)
                        jt.setRowSelectionInterval(0, 0);
                }
            }
        }
 
    }//actionPerformed()----------
}