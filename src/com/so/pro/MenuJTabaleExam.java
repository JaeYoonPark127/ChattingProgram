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
    JMenu m = new JMenu("����");
    JMenuItem insert = new JMenuItem("����");
    JMenuItem update = new JMenuItem("����");
    JMenuItem delete = new JMenuItem("����");
    JMenuItem quit = new JMenuItem("����");
    JMenuBar mb = new JMenuBar();
 
    String[] name = { "id", "password", "name",  "birth" ,"email" , "zipcode"};
 
    DefaultTableModel dt = new DefaultTableModel(name, 0);
    JTable jt = new JTable(dt);
    JScrollPane jsp = new JScrollPane(jt);
    /*
     * South ������ �߰��� Componet��
     */
    JPanel p = new JPanel();
    //�޺��ڽ��� �߰��� �̸��� String �迭�� ���� �� �ʱ�ȭ
    String[] comboName = { "  ALL  ", "  ID  ", " PASSWORD ", " NAME " , " BIRTH ", " EMAIL "," ZIPCODE "};
 
    //�޺��ڽ��� �̸� �迭�� ����
    JComboBox combo = new JComboBox(comboName);
    JTextField searchField = new JTextField(20);
    JButton search = new JButton("�˻�");
 
    UserDefaultJTableDAO dao = new UserDefaultJTableDAO();
    
    public static JLabel imageLabel;
    private final JLabel lblNewLabel = new JLabel("ȸ�� ����");
 
    /**
     * ȭ�鱸�� �� �̺�Ʈ���
     */
    public MenuJTabaleExam() {
       
        super("ȸ���������α׷�");
        jt.setAutoCreateRowSorter(true);//���̺� ���� ������
        
        //�޴��������� �޴��� �߰�
        m.add(insert);
        m.add(update);
        m.add(delete);
        m.add(quit);
        //�޴��� �޴��ٿ� �߰�
        mb.add(m);
       
        //�����쿡 �޴��� ����
        setJMenuBar(mb);
 
        imageLabel = new JLabel("������ġ");
        imageLabel.setPreferredSize(new Dimension(100, 100));
        p.setPreferredSize(new Dimension(100, 110));
        // South����
        p.setBackground(Color.gray);
        
        p.add(lblNewLabel);
        
        p.add(imageLabel);
        p.add(combo);
        p.add(searchField);
        p.add(search);
 
        getContentPane().add(jsp, "Center"); //ȸ������ ���̺� �г� ����
        getContentPane().add(p, "South"); //�˻� �г� ����
 
        setSize(1400, 900);
        setVisible(true);
 
        //����â�� ���������� ��
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // �̺�Ʈ���
        insert.addActionListener(this);
        update.addActionListener(this);
        delete.addActionListener(this);
        quit.addActionListener(this);
        search.addActionListener(this);
 
        // ��緹�ڵ带 �˻��Ͽ� DefaultTableModle�� �ø���
        dao.userSelectAll(dt);
       
        //ù���� ����.
        if (dt.getRowCount() > 0)
            jt.setRowSelectionInterval(0, 0);
        
        setLocationRelativeTo(null);
        
        jt.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseClicked(MouseEvent e) {
    			if( jt.getSelectedRow() >= 0) {
    				//System.out.println(jt.getSelectedRow()); ȸ������ ��� ���̺��� �� ���� ���� �� �� ����
    				//����ڵ��� 1�� 1������ �����Ѵٰ� �� �� ������ JTable�� 0�� 0������ �����Ѵٴ� ���� �� �� ����
    				String id = jt.getValueAt(jt.getSelectedRow(), 0).toString();
    				
    			}
    		}
    	});
 
    }// �����ڳ�
 
    /**
     * main�޼ҵ� �ۼ�
     */

    /**
     * ����/����/����/�˻������ ����ϴ� �޼ҵ�
     * */   
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == insert) {// ���� �޴������� Ŭ��
        	DBJoin add = new DBJoin();
        	add.JoinDBPanel();
        } else if (e.getSource() == update) {// ���� �޴������� Ŭ��
            new UserJDailogGUI(this, "����");
 
        } else if (e.getSource() == delete) {// ���� �޴������� Ŭ��
            // ���� Jtable�� ���õ� ��� ���� ���� ���´�.
            int row = jt.getSelectedRow();
            System.out.println("������ : " + row);
 
            Object obj = jt.getValueAt(row, 0);// �� ���� �ش��ϴ� value
            System.out.println("�� : " + obj);
 
            if (dao.userDelete(obj.toString()) > 0) {
                UserJDailogGUI.messageBox(this, "ȸ���� �����Ǿ����ϴ�.");
               
                //����Ʈ ����
                dao.userSelectAll(dt);
                if (dt.getRowCount() > 0)
                    jt.setRowSelectionInterval(0, 0);
 
            } else {
                UserJDailogGUI.messageBox(this, "ȸ���� �������� �ʾҽ��ϴ�.");
            }
 
        } else if (e.getSource() == quit) {// ���� �޴������� Ŭ��
        	dispose(); 
        } else if (e.getSource() == search) {// �˻� ��ư Ŭ��
            // JComboBox�� ���õ� value ��������
            String fieldName = combo.getSelectedItem().toString();
            System.out.println("�ʵ�� " + fieldName);
 
            if (fieldName.trim().equals("ALL")) {// ��ü�˻�
                dao.userSelectAll(dt);
                if (dt.getRowCount() > 0)
                    jt.setRowSelectionInterval(0, 0);
            } else {
                if (searchField.getText().trim().equals("")) {
                    UserJDailogGUI.messageBox(this, "�˻��ܾ �Է����ּ���!");
                    searchField.requestFocus();
                } else {// �˻�� �Է��������
                    dao.getUserSearch(dt, fieldName, searchField.getText());
                    if (dt.getRowCount() > 0)
                        jt.setRowSelectionInterval(0, 0);
                }
            }
        }
 
    }//actionPerformed()----------
}