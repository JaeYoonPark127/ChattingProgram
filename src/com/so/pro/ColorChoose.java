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
   JComponent targetComponent; //Component�� Container Ŭ������ ��ӹ޾� setBackground, setForeground�� ����ϰԵ�
    JColorChooser colorChooser; //������ ������ �� �ִ� ���� ���� â
    JButton backgroundButton; //������ ���� �������� �����ϱ� ���� ��ư
    JButton foregroundButton; //������ ���� ä�û����� �����ϱ� ���� ��ư
    JButton okButton; //���� ���� ��ư

    public ColorChoose(JComponent targetComponent) {

        this.targetComponent = targetComponent;

        colorChooser = new JColorChooser(); //���� ���� â ��ü ����

        ButtonActionListener listener = new ButtonActionListener(); //��ư �׼� ��ü ����

        backgroundButton = new JButton("ä�� ����");
        backgroundButton.addActionListener(listener); //���� ��ư Ŭ�� �� ��ư �׼� ȣ��

        foregroundButton = new JButton("ä�� ���ڻ�");
        foregroundButton.addActionListener(listener); //���ڻ� ��ư Ŭ�� �� �̿��� ��ư �׼� ȣ��

        okButton = new JButton("Ȯ��");
        okButton.addActionListener(listener); //Ȯ�� ��ư Ŭ�� ��

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(backgroundButton); //���� ���� â�� ��ư �߰�
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
            if (e.getSource().equals(backgroundButton)) { //���� ���� ��ư Ŭ����
                targetComponent.setBackground(colorChooser.getColor());
            } else if (e.getSource().equals(foregroundButton)) { //���ڻ� ���� ��ư Ŭ����
                targetComponent.setForeground(colorChooser.getColor());
            } else if (e.getSource().equals(okButton)) { //Ȯ�� ��ư Ŭ����
                dispose();
            }
        }
    }

}