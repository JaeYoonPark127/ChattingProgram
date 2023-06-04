package com.so.pro;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JScrollPane;

public class EightClient implements Runnable {

	static int PORT = 5555; // ������Ʈ��ȣ
	static String IP = "172.16.20.120"; // �����������ּ�
	Socket socket; // ����
	User user; // �����
	
	RoomUI roomui;
	LoginUI login;
	WaitRoomUI waitRoom;
	DataInputStream dis = null;
	DataOutputStream dos = null;
	boolean ready = false;
	StringTokenizer st;
	
	JTextPane chatArea = new JTextPane();
	StyledDocument doc = chatArea.getStyledDocument();
	Style s;
	Statement stmt =null;
	ResultSet rs =null;
	Connection cnn = null;
	static Properties info = null;

	EightClient() {
        login = new LoginUI(this);
		
		Thread thread = new Thread(this);
		thread.start();
	}

	public static void main(String[] args) {
		System.out.println("Client start...1");
		new EightClient();
		
	} // main end
	
	

	@Override
	public void run() {
		//
		// ���� ��� ����
		//
		while (!ready) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// ����ڰ� ��ü ���� �� �����Ǽ���
		user = new User(dis, dos);
		user.setIP(socket.getInetAddress().getHostAddress());

		// �޽��� ����
		while (true) {
			try {
				String receivedMsg = dis.readUTF(); // �޽��� �ޱ�(���)
				dataParsing(receivedMsg); // �޽��� �ؼ�
			} catch (IOException e) {
				e.printStackTrace();
				try {
					user.getDis().close();
					user.getDos().close();
					socket.close();
					break;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

		alarmMsg("�������α׷��� ���� ����Ǿ����ϴ�.");
		// ä�����α׷� ����
		waitRoom.dispose();
	}

	public boolean serverAccess() {
		if (!ready) {
			// ������ ������ �̷������ ���� ��쿡�� ����
			// ó�� ����ÿ��� ����
			socket = null;
			IP = login.ipBtn.getText();
			try {
				// ��������
				InetSocketAddress inetSockAddr = new InetSocketAddress(InetAddress.getByName(IP), PORT);
				socket = new Socket();

				// ������ �ּҷ� ���� �õ� (3�ʵ���)
				socket.connect(inetSockAddr, 3000);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// ������ �Ǹ� ����
			if (socket.isBound()) {
				// �Է�, ��� ��Ʈ�� ����
				try {
					dis = new DataInputStream(socket.getInputStream());
					dos = new DataOutputStream(socket.getOutputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
				ready = true;
			}
		}
		return ready;
	}

	// �����͸� ����
	public synchronized void dataParsing(String data) {
		StringTokenizer token = new StringTokenizer(data, "/"); // ��ū ����
		String protocol = token.nextToken(); // ��ū���� �и��� ��Ʈ��
		String id, pw, rNum, nick, rName, msg, result;
		System.out.println("���� ������ : " + data);

		switch (protocol) {
		case User.LOGIN: // �α���
			// ����ڰ� �Է���(������) ���̵�� �н�����
			result = token.nextToken();
			if (result.equals("OK")) {
				alarmMsg("�α��ο� �����߽��ϴ�!");
				nick = token.nextToken();
				login(nick);
			} else {
				msg = token.nextToken();
				alarmMsg(msg);
			}
			break;
		case User.LOGOUT:
			logout();
			break;
		case User.MEMBERSHIP: // ȸ������ ����
			result = token.nextToken();
			break;
		case User.UPDATE_USERLIST: // ���� ����� ���
			userList(token);
			break;
		case User.UPDATE_ROOM_USERLIST: // ä�ù� ����� ���
			// ���ȣ�б�
			rNum = token.nextToken();
			userList(rNum, token);
			break;
		case User.UPDATE_SELECTEDROOM_USERLIST: // ���ǿ��� ������ ä�ù��� ����� ���
			selectedRoomUserList(token);
			break;
		case User.UPDATE_ROOMLIST: // �� ���
			roomList(token);
			break;
		case User.ECHO01: // ���� ����
			msg = token.nextToken();
			echoMsg(msg);
			break;
		case User.ECHO02: // ä�ù� ����
			rNum = token.nextToken();
			msg = token.nextToken();
			echoMsgToRoom(rNum, msg);
			break;
		case User.WHISPER: // �ӼӸ�
			id = token.nextToken();
			nick = token.nextToken();
			msg = token.nextToken();
			whisper(id, nick, msg);
			break;
		}
	}

	private void logout() {
		try {
			alarmMsg("ä�� ���α׷��� �����մϴ�!");
			waitRoom.dispose();
			user.getDis().close();
			user.getDos().close();
			socket.close();
			waitRoom = null;
			user = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ä�ù� ���� ����� ����Ʈ
	private void userList(String rNum, StringTokenizer token) {
		for (int i = 0; i < user.getRoomArray().size(); i++) {
			if (Integer.parseInt(rNum) == user.getRoomArray().get(i).getRoomNum()) {

				// ������ ����Ʈ�� ���� ��� ������
				if (user.getRoomArray().get(i).getrUI().model != null)
					user.getRoomArray().get(i).getrUI().model.removeAllElements();

				while (token.hasMoreTokens()) {
					// ���̵�� �г����� �о ���� ��ü �ϳ��� ����
					String id = token.nextToken();
					String nick = token.nextToken();
					User tempUser = new User(id,nick);

					user.getRoomArray().get(i).getrUI().model.addElement(tempUser.toString());
				}
			}
		}
	}

	// ������ ä�ù��� ����� ����Ʈ
	private void selectedRoomUserList(StringTokenizer token) {
		// �����κ��� ��������Ʈ(ä�ù�)�� ������Ʈ�϶�� ������ ����

		if (!waitRoom.level2.isLeaf()) {
			// ������尡 �ƴϰ�, ���ϵ尡 �ִٸ� ��� ����
			waitRoom.level2.removeAllChildren();
		}
		while (token.hasMoreTokens()) {
			// ���̵�� �г����� �о ���� ��ü �ϳ��� ����
			String id = token.nextToken();
			String nick = token.nextToken();
			User tempUser = new User(id, nick);

			// ä�ù� ����ڳ�忡 �߰�
			waitRoom.level2.add(new DefaultMutableTreeNode(tempUser.toString()));
		}
		waitRoom.userTree.updateUI();
	}

	// ���� ����� ����Ʈ
	private void userList(StringTokenizer token) {
		// �����κ��� ��������Ʈ(����)�� ������Ʈ�϶�� ������ ����

		if (waitRoom == null) {
			return;
		}

		if (!waitRoom.level3.isLeaf()) {
			// ������尡 �ƴϰ�, ���ϵ尡 �ִٸ� ��� ����
			waitRoom.level3.removeAllChildren();
		}
		while (token.hasMoreTokens()) {
			// ���̵�� �г����� �о ���� ��ü �ϳ��� ����
			String id = token.nextToken();
			String nick = token.nextToken();
			User tempUser = new User(id, nick);

			for (int i = 0; i < waitRoom.userArray.size(); i++) {
				if (tempUser.getId().equals(waitRoom.userArray.get(i))) {
				}
				if (i == waitRoom.userArray.size()) {
					// �迭�� ������ ������ �߰�����
					waitRoom.userArray.add(tempUser);
				}
			}
			// ���� ����ڳ�忡 �߰�
			waitRoom.level3.add(new DefaultMutableTreeNode(tempUser.toString()));
		}
		waitRoom.userTree.updateUI();
	}

	// �����κ��� �渮��Ʈ�� ������Ʈ�϶�� ������ ����
	private void roomList(StringTokenizer token) {
		String rNum, rName;
		Room room = new Room();

		// ������ ����Ʈ�� ���� ��� ������
		if (waitRoom.model != null) {
			waitRoom.model.removeAllElements();
		}

		while (token.hasMoreTokens()) {
			rNum = token.nextToken();
			rName = token.nextToken();
			int num = Integer.parseInt(rNum);

			// ��Ʈ��ѹ��� ������Ʈ (�ִ밪+1)
			if (num >= waitRoom.lastRoomNum) {
				waitRoom.lastRoomNum = num + 1;
			}
			room.setRoomNum(num);
			room.setRoomName(rName);

			waitRoom.model.addElement(room.toProtocol());
		}
	}

	private void alarmMsg(String string) {
		int i = JOptionPane.showConfirmDialog(null, string, "�޽���", JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
		// Ȯ�� ������ ����
		if (i == 0) { }
	}

	private void login(String nick) {
		// �α������� ������
		user.setId(login.idText.getText());
		user.setNickName(nick);

		// �α���â �ݰ� ����â ����
		login.dispose();
		waitRoom = new WaitRoomUI(EightClient.this);
		waitRoom.lbid.setText(user.getId());
		waitRoom.lbip.setText(user.getIP());
		waitRoom.lbnick.setText(user.getNickName());
	}

	private void whisper(String id, String nick, String msg) {
		waitRoom.waitRoomArea.append("(" + id + ")���� �ӼӸ� : " + msg + "\n");
	}

	private void echoMsg(String msg) {
		// Ŀ�� ��ġ ����
		if (waitRoom != null) {
			waitRoom.waitRoomArea.setCaretPosition(waitRoom.waitRoomArea.getText().length());
			waitRoom.waitRoomArea.append(msg + "\n");
		}
	}

	private void echoMsgToRoom(String rNum, String msg) {
		for (int i = 0; i < user.getRoomArray().size(); i++) {
			if (Integer.parseInt(rNum) == user.getRoomArray().get(i).getRoomNum()) {
				// ����� -> ��迭 -> ������ -> �ؽ�Ʈ�����
				// Ŀ�� ��ġ ����
				
				try {
	               StringTokenizer token = new StringTokenizer(msg,"()");
	               String idl = token.nextToken();
	               String nick = token.nextToken();
	               String mmsg = token.nextToken();

	               if(idl.equals(user.getNickName())) {
	            	   if("���� �����ϼ̽��ϴ�.".equals(mmsg)) {
	            		   user.getRoomArray().get(i).getrUI().doc.setLogicalStyle(user.getRoomArray().get(i).getrUI().doc.getLength(),
	            				   user.getRoomArray().get(i).getrUI().doc.getStyle("center"));
	 	                  user.getRoomArray().get(i).getrUI().doc.insertString(user.getRoomArray().get(i).getrUI().doc.getLength(),
	 	                		  idl+mmsg +" \n", user.getRoomArray().get(i).getrUI().doc.getStyle("black"));
	            		   
	            	   }
	            	   else {
	            		   
		            		  
	 	                  user.getRoomArray().get(i).getrUI().doc.setLogicalStyle(user.getRoomArray().get(i).getrUI().doc.getLength(),
	 	                		  user.getRoomArray().get(i).getrUI().doc.getStyle("right"));
	 	                  user.getRoomArray().get(i).getrUI().doc.insertString(user.getRoomArray().get(i).getrUI().doc.getLength(),
	 	                		  idl+":"+mmsg +" \n", user.getRoomArray().get(i).getrUI().doc.getStyle("green"));
	            	   }}
	            	   else {
	            		   if("���� �����ϼ̽��ϴ�.".equals(mmsg)) {
		            		   user.getRoomArray().get(i).getrUI().doc.setLogicalStyle(user.getRoomArray().get(i).getrUI().doc.getLength(),
		            				   user.getRoomArray().get(i).getrUI().doc.getStyle("center"));
		 	                  user.getRoomArray().get(i).getrUI().doc.insertString(user.getRoomArray().get(i).getrUI().doc.getLength(),
		 	                		  idl+mmsg +" \n", user.getRoomArray().get(i).getrUI().doc.getStyle("black"));
		            		   
		            	   }
	            	   
	            	   else if("���� �����ϼ̽��ϴ�.".equals(mmsg)) {
	            		   user.getRoomArray().get(i).getrUI().doc.setLogicalStyle(user.getRoomArray().get(i).getrUI().doc.getLength(),
	            				   user.getRoomArray().get(i).getrUI().doc.getStyle("center"));
	 	                  user.getRoomArray().get(i).getrUI().doc.insertString(user.getRoomArray().get(i).getrUI().doc.getLength(),
	 	                		  idl+mmsg +" \n", user.getRoomArray().get(i).getrUI().doc.getStyle("red"));
	            		   
	            	   }else {
	            	  
	               
	               
	            	   user.getRoomArray().get(i).getrUI().doc.setLogicalStyle(user.getRoomArray().get(i).getrUI().doc.getLength(),
	            			   user.getRoomArray().get(i).getrUI().doc.getStyle("left"));
	            	   user.getRoomArray().get(i).getrUI().doc.insertString(user.getRoomArray().get(i).getrUI().doc.getLength(),
	            			   idl+":"+mmsg +" \n", user.getRoomArray().get(i).getrUI().doc.getStyle("blue"));
	               
	              // user.getRoomArray().get(i).getrUI().scrollPane.getVerticalScrollBar().setValue(user.getRoomArray().get(i).getrUI().scrollPane.getVerticalScrollBar().getMaximum());
				}}}catch(Exception e) {
	               	System.out.println(e);
	               }
	            /*   
				user.getRoomArray().get(i).getrUI().chatArea.setCaretPosition(user.getRoomArray().get(i).getrUI().chatArea.getText().length());
	            // ����
	            user.getRoomArray().get(i).getrUI().chatArea.append(msg + "\n");
	            */

	}}
		}
	
	/*private String getText(String nickName) {
		return null;
	}
*/

	// getter, setter
	public static int getPORT() {
		return PORT;
	}

	public static void setPORT(int pORT) {
		PORT = pORT;
	}

	public static String getIP() {
		return IP;
	}

	public static void setIP(String iP) {
		IP = iP;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LoginUI getLogin() {
		return login;
	}

	public void setLogin(LoginUI login) {
		this.login = login;
	}

	public WaitRoomUI getRestRoom() {
		return waitRoom;
	}

	public void setRestRoom(WaitRoomUI restRoom) {
		this.waitRoom = restRoom;
	}

	public DataInputStream getDis() {
		return dis;
	}

	public void setDis(DataInputStream dis) {
		this.dis = dis;
	}

	public DataOutputStream getDos() {
		return dos;
	}

	public void setDos(DataOutputStream dos) {
		this.dos = dos;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

}