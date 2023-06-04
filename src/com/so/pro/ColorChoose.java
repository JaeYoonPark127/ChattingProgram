package com.so.pro;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;



import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;    

class ColorChoose extends JDialog {


   private static final long serialVersionUID = 1L;
   JComponent targetComponent; //Component와 Container 클래스를 상속받아 setBackground, setForeground를 사용하게됨
    JColorChooser colorChooser; //색상을 선택할 수 있는 색상 선택 창
    JButton backgroundButton; //선택한 색을 배경색으로 적용하기 위한 버튼
    JButton foregroundButton; //선택한 색을 채팅색으로 적용하기 위한 버튼
    JButton okButton; //색상 적용 버튼

    public ColorChoose(JComponent targetComponent) {

        this.targetComponent = targetComponent;

        colorChooser = new JColorChooser(); //색상 선택 창 객체 생성

        ButtonActionListener listener = new ButtonActionListener(); //버튼 액션 객체 생성

        backgroundButton = new JButton("채팅 배경색");
        backgroundButton.addActionListener(listener); //배경색 버튼 클릭 시 버튼 액션 호출

        foregroundButton = new JButton("채팅 글자색");
        foregroundButton.addActionListener(listener); //글자색 버튼 클릭 시 이용한 버튼 액션 호출

        okButton = new JButton("확인");
        okButton.addActionListener(listener); //확인 버튼 클릭 시

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(backgroundButton); //색상 선택 창에 버튼 추가
        buttonPanel.add(foregroundButton);
        buttonPanel.add(okButton);

        getContentPane().add(colorChooser, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.PAGE_END);

        pack();
        setModal(true);
        setLocationRelativeTo(targetComponent);

    }

    private class ButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(backgroundButton)) { //배경색 선택 버튼 클릭시
                targetComponent.setBackground(colorChooser.getColor());
            } else if (e.getSource().equals(foregroundButton)) { //글자색 선택 버튼 클릭시
                targetComponent.setForeground(colorChooser.getColor());
            } else if (e.getSource().equals(okButton)) { //확인 버튼 클릭시
                dispose();
            }
        }
    }

}