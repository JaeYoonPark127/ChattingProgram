package com.so.pro;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.*;
public class JDBCPostMain extends JFrame implements ActionListener{
	DBJoin DB;
    JTextField tf;
    JLabel la;
    DefaultTableModel model;
    JTable table;
    JButton button1;
    JButton button2;
    static Object addrNum;
    public JDBCPostMain() {
        tf=new JTextField(15);
        la=new JLabel("�Է�");
        button1 = new JButton("�˻�");
        button2 = new JButton("�����ȣ Ȯ��");
        String[] col= {"�����ȣ","���θ�","��/��/����"};
        String[][] row=new String[0][3];
        DB = new DBJoin();
        model=new DefaultTableModel(row,col) {
           public boolean isCellEditable(int i,int c) {
              return false;
           }
        };
        table=new JTable(model);
        JScrollPane js=new JScrollPane(table);   //���̺�
        JPanel p=new JPanel();   // �Է�â
        p.add(la);
        p.add(tf); 
        p.add(button1);
        p.add(button2);
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(2).setPreferredWidth(300);
        
        
        add("North",p);
        add("Center",js);   //���̺�
        
        setSize(700, 500);
        setVisible(true);
        table.addMouseListener(new addrMouseListener());
        button1.addActionListener(this);
        button2.addActionListener(this);
        System.out.println(addrNum);
    }
    public static void main(String[] args) {
        new JDBCPostMain();
        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if(e.getSource()==button1) {
            String dong=tf.getText();
           
            int[] n = new int[2];
            List<String> list = new ArrayList<String>();
            if(dong.length()<1)//�Է��� �ȵ� ���
            {
                JOptionPane.showMessageDialog(this, "��/��/���� �Է��ϼ���");
                return;
            }
            //ó��
            for(int i=model.getRowCount()-1;i>=0;i--) {
                model.removeRow(i);
            }
            new JDBCTest();
            JDBCTest.find(dong,1,50,list,n);
            
            // ���
            for(int i = 0; i<list.size();i+=3) {
                String[] data= {list.get(i), list.get(i+1),list.get(i+2)};
                model.addRow(data);
            }
            
        }
        if(e.getSource()==button2) {
           if(table.getSelectedRow() == -1)
              return;
           else
           {
              
              addrNum = model.getValueAt(table.getSelectedRow(),0);
              Object doro = model.getValueAt(table.getSelectedRow(),1);
              DB.ipTf.setText((String) addrNum);
              DB.ip_2Tf.setText((String)doro);
              setVisible(false);
              
           }
        }
    }
    class addrMouseListener implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e) {
        	if(e.getClickCount()==2){
        	//setVisible(false);
        		addrNum = model.getValueAt(table.getSelectedRow(),0);
                Object doro = model.getValueAt(table.getSelectedRow(),1);
                DB.ipTf.setText((String) addrNum);
                DB.ip_2Tf.setText((String)doro);
                setVisible(false);
 
        	}
        }

    	@Override
    	public void mousePressed(MouseEvent e) {
    		// TODO Auto-generated method stub
    		
    	}

    	@Override
    	public void mouseReleased(MouseEvent e) {
    		// TODO Auto-generated method stub
    		
    	}

    	@Override
    	public void mouseEntered(MouseEvent e) {
    		// TODO Auto-generated method stub
    		
    	}

    	@Override
    	public void mouseExited(MouseEvent e) {
    		// TODO Auto-generated method stub
    		
    	}
    	
        }
}