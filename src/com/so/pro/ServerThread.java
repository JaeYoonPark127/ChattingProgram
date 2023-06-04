package com.so.pro;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JTextArea;

public class ServerThread implements Runnable {

	MsgBox msgbox = new MsgBox();

	ArrayList<User> userArray; // ������ ������ ����ڵ�
	ArrayList<Room> roomArray; // ������ ������� ä�ù��
	User user; // ���� ������� �����(������ ������) �����
	JTextArea jta;
	boolean onLine = true;

	private DataOutputStream thisUser;

	ServerThread(JTextArea jta, User person, ArrayList<User> userArray, ArrayList<Room> roomArray) {
		
		this.roomArray = roomArray;
		this.userArray = userArray;
		this.userArray.add(person); // �迭�� ����� �߰�
		this.user = person;
		this.jta = jta;
		this.thisUser = person.getDos();
	}

	@Override
	public void run() {
		DataInputStream dis = user.getDis(); // �Է� ��Ʈ�� ���

		while (onLine) {
			try {
				String receivedMsg = dis.readUTF(); // �޽��� �ޱ�(���)
				dataParsing(receivedMsg); // �޽��� �ؼ�
				jta.append("���� : �޽��� ���� -" + receivedMsg + "\n");
				jta.setCaretPosition(jta.getText().length());
			} catch (IOException e) {
				try {
					user.getDis().close();
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					jta.append("���� : ����������-�б� ����\n");
					break;
				}
			}
		}
	}

	// �����͸� ����
	public synchronized void dataParsing(String data) {
		StringTokenizer token = new StringTokenizer(data, "/"); // ��ū ����
		String protocol = token.nextToken(); // ��ū���� �и��� ��Ʈ���� ���ڷ�
		String id, pw, rNum, rName, msg;
		System.out.println("������ ���� ������ : " + data);

		switch (protocol) {
		case User.LOGIN: // �α���
			// ����ڰ� �Է���(������) ���̵�� �н�����
			id = token.nextToken();
			pw = token.nextToken();
			login(id, pw);
			break;
		case User.LOGOUT: // �α׾ƿ�
			logout();
			break;
		case User.MEMBERSHIP: // ȸ������
			id = token.nextToken();
			pw = token.nextToken();
			break;
		case User.UPDATE_USERLIST: // ���� ����� ���
			userList(thisUser);
			break;
		case User.UPDATE_ROOM_USERLIST: // ä�ù� ����� ���
			// ���ȣ�б�
			rNum = token.nextToken();
			userList(rNum, thisUser);
			break;
		case User.UPDATE_SELECTEDROOM_USERLIST: // ���ǿ��� ������ ä�ù��� ����� ���
			// ���ȣ�б�
			rNum = token.nextToken();
			selectedRoomUserList(rNum, thisUser);
			break;
		case User.UPDATE_ROOMLIST: // �� ���
			roomList(thisUser);
			break;
		case User.CREATE_ROOM: // �游���
			rNum = token.nextToken();
			rName = token.nextToken();
			createRoom(rNum, rName);
			break;
		case User.GETIN_ROOM: // �� ����
			rNum = token.nextToken();
			getInRoom(rNum);
			break;
		case User.GETOUT_ROOM: // �� ������
			rNum = token.nextToken();
			getOutRoom(rNum);
			break;
		case User.ECHO01: // ���� ����
			msg = token.nextToken();
			echoMsg(User.ECHO01 + "/" + user.toString() + msg);
			break;
		case User.ECHO02: // ä�ù� ����
			rNum = token.nextToken();
			msg = token.nextToken();
			echoMsg(rNum, msg);
			break;
		case User.WHISPER: // �ӼӸ�
			id = token.nextToken();
			msg = token.nextToken();
			whisper(id, msg);
			break;
		}
	}

	private void getOutRoom(String rNum) {
		for (int i = 0; i < roomArray.size(); i++) {
			if (Integer.parseInt(rNum) == roomArray.get(i).getRoomNum()) {
				// �濡�� ������
				// ä�ù��� ��������Ʈ���� ����� ����
				for (int j = 0; j < roomArray.get(i).getUserArray().size(); j++) {
					if (user.getId().equals(roomArray.get(i).getUserArray().get(j).getId())) {
						roomArray.get(i).getUserArray().remove(j);
					}
				}

				// ������� �渮��Ʈ���� ���� ����
				for (int j = 0; j < user.getRoomArray().size(); j++) {
					if (Integer.parseInt(rNum) == user.getRoomArray().get(j).getRoomNum()) {
						user.getRoomArray().remove(j);
					}
				}
				echoMsg(roomArray.get(i), user.toString() + "���� �����ϼ̽��ϴ�.");
				userList(rNum);

				if (roomArray.get(i).getUserArray().size() <= 0) {
					roomArray.remove(i);
					roomList();
				}
			}
		}
	}

	private void getInRoom(String rNum) {
		for (int i = 0; i < roomArray.size(); i++) {
			if (Integer.parseInt(rNum) == roomArray.get(i).getRoomNum()) {
				// �� ��ü�� �ִ� ���, �濡 ������߰�
				roomArray.get(i).getUserArray().add(user);
				// ����� ��ü�� �� �߰�
				user.getRoomArray().add(roomArray.get(i));
				echoMsg(roomArray.get(i), user.toString() + "���� �����ϼ̽��ϴ�.");
				userList(rNum);
			}
		}
	}

	private void createRoom(String rNum, String rName) {
		Room rm = new Room(rName); // ������ �������� ä�ù� ����
		rm.setMaker(user); // ���� ����
		rm.setRoomNum(Integer.parseInt(rNum)); // ���ȣ ����

		rm.getUserArray().add(user); // ä�ù濡 ����(����) �߰�
		roomArray.add(rm); // �븮��Ʈ�� ���� ä�ù� �߰�
		user.getRoomArray().add(rm); // ����� ��ü�� ������ ä�ù��� ����

		echoMsg(User.ECHO01 + "/" + user.toString() + "���� " + rm.getRoomNum() + "�� ä�ù��� �����ϼ̽��ϴ�.");
		echoMsg(rm, user.toString() + "���� �����ϼ̽��ϴ�.");
		roomList();
		userList(rNum, thisUser);
		jta.append("���� : " + userArray.toString() + "�� ä�ù����\n");
	}

	private void whisper(String id, String msg) {
		for (int i = 0; i < userArray.size(); i++) {
			if (id.equals(userArray.get(i).getId())) {
				// �ӼӸ� ��븦 ã����
				try {
					userArray.get(i).getDos().writeUTF(User.WHISPER + "/" + user.toProtocol() + "/" + msg);
					jta.append("���� : �ӼӸ����� : " + user.toString() + "�� " + userArray.get(i).toString() + "����" + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// ���� ����
	private void echoMsg(String msg) {
		for (int i = 0; i < userArray.size(); i++) {
			try {
				userArray.get(i).getDos().writeUTF(msg);
				jta.append(user.toString() + " - " + msg + "\n");
			} catch (IOException e) {
				e.printStackTrace();
				jta.append("���� : ���� ����\n");
			}
		}
	}

	// �� ���� (�� ��ȣ�� �ƴ� ���)
	private void echoMsg(String rNum, String msg) {
		for (int i = 0; i < roomArray.size(); i++) {
			if (Integer.parseInt(rNum) == roomArray.get(i).getRoomNum()) {
				echoMsg(roomArray.get(i), msg);
			}
		}
	}

	// �� ���� (�水ü�� �ִ� ���)
	private void echoMsg(Room room, String msg) {
		for (int i = 0; i < room.getUserArray().size(); i++) {
			try {
				// �濡 ������ �����鿡�� ���� �޽��� ����
				room.getUserArray().get(i).getDos().writeUTF(User.ECHO02 + "/" + room.getRoomNum() + "/" + msg);
				jta.append("���� : �޽������� : " + msg + "\n");
			} catch (IOException e) {
				e.printStackTrace();
				jta.append("���� : ���� ����\n");
			}
		}
	}

	private void login(String id, String pw) {
		StringBuffer str = new StringBuffer();
		try {

			DBLogin logdb = new DBLogin();
			int result = logdb.checkIDPW(id, pw);

			if (result == 0) { // result�� 0�̸� ����
				for (int i = 0; i < userArray.size(); i++) {
					if (id.equals(userArray.get(i).getId())) {
						try {
							System.out.println("������");
							thisUser.writeUTF(User.LOGIN + "/fail/�̹� ���� ���Դϴ�.");
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
				}
				// �α��� OK
				user.setId(id);
				user.setPw(pw);
				user.setNickName(id);
				thisUser.writeUTF(User.LOGIN + "/OK/" + user.getNickName());
				this.user.setOnline(true);

				// ���ǿ� ����
				echoMsg(User.ECHO01 + "/" + user.toString() + "���� �����ϼ̽��ϴ�.");
				jta.append(id + " : ���� �����ϼ̽��ϴ�.\n");

				roomList(thisUser);
				for (int i = 0; i < userArray.size(); i++) {
					userList(userArray.get(i).getDos());
				}

				jta.append("���� : DB �б� : " + id);
			} else { // result�� 1�̸� ����
				thisUser.writeUTF(User.LOGIN + "/fail/���̵�� ��й�ȣ�� Ȯ���� �ּ���!");
			}

		} catch (Exception e) {
			try {
				thisUser.writeUTF(User.LOGIN + "/fail/���̵� �������� �ʽ��ϴ�!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			jta.append("���� : DB �б�\n");
			return;
		}

	}

	private void logout() {
		System.out.println("�α׾ƿ� ����!");

		// ������������ �ٲ�
		user.setOnline(false);
		// ����ڹ迭���� ����
		for (int i = 0; i < userArray.size(); i++) {
			if (user.getId().equals(userArray.get(i).getId())) {
				System.out.println(userArray.get(i).getId() + "������.");
				userArray.remove(i);
			}
		}
		// room Ŭ������ ��������� ����ڹ迭���� ����
		for (int i = 0; i < roomArray.size(); i++) {
			for (int j = 0; j < roomArray.get(i).getUserArray().size(); j++) {
				if (user.getId().equals(roomArray.get(i).getUserArray().get(j).getId())) {
					roomArray.get(i).getUserArray().remove(j);
				}
			}
		}
		echoMsg(User.ECHO01 + "/" + user.toString() + "���� �����ϼ̽��ϴ�.");

		for (int i = 0; i < userArray.size(); i++) {
			userList(userArray.get(i).getDos());
		}

		jta.append(user.getId() + " : ���� �����ϼ̽��ϴ�.\n");

		try {
			user.getDos().writeUTF(User.LOGOUT);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			user.getDis().close();
			user.getDos().close();
			user = null;
			jta.append("���� : ��Ʈ�� �ݱ�\n");
		} catch (IOException e) {
			e.printStackTrace();
			jta.append("���� : ��Ʈ�� �ݱ�\n");
		}
	}

	// ����� ����Ʈ (������ ä�ù�)
	public void selectedRoomUserList(String rNum, DataOutputStream target) {
		String ul = "";

		for (int i = 0; i < roomArray.size(); i++) {
			if (Integer.parseInt(rNum) == roomArray.get(i).getRoomNum()) {
				for (int j = 0; j < roomArray.get(i).getUserArray().size(); j++) {
					// ä�ù濡 ���ӵǾ� �ִ� �������� ���̵�+�г���
					ul += "/" + roomArray.get(i).getUserArray().get(j).toProtocol();
				}
			}
		}
		try {
			// ������ ����
			target.writeUTF(User.UPDATE_SELECTEDROOM_USERLIST + ul);
			jta.append("���� : ���(�����)-" + ul + "\n");
		} catch (IOException e) {
			jta.append("���� : ���(�����) ���� ����\n");
		}
	}

	// ����� ����Ʈ (����)
	public String userList(DataOutputStream target) {
		String ul = "";

		for (int i = 0; i < userArray.size(); i++) {
			// ���ӵǾ� �ִ� �������� ���̵�+�г���
			ul += "/" + userArray.get(i).toProtocol();
		}

		try {
			// ������ ����
			target.writeUTF(User.UPDATE_USERLIST + ul);
			jta.append("���� : ���(�����)-" + ul + "\n");
		} catch (IOException e) {
			jta.append("���� : ���(�����) ���� ����\n");
		}
		return ul;
	}

	// ����� ����Ʈ (ä�ù� ����)
	public void userList(String rNum, DataOutputStream target) {
		String ul = "/" + rNum;

		for (int i = 0; i < roomArray.size(); i++) {
			if (Integer.parseInt(rNum) == roomArray.get(i).getRoomNum()) {
				for (int j = 0; j < roomArray.get(i).getUserArray().size(); j++) {
					// ä�ù濡 ���ӵǾ� �ִ� �������� ���̵�+�г���
					ul += "/" + roomArray.get(i).getUserArray().get(j).toProtocol();
				}
			}
		}
		try {
			// ������ ����
			target.writeUTF(User.UPDATE_ROOM_USERLIST + ul);
			jta.append("���� : ���(�����)-" + ul + "\n");
		} catch (IOException e) {
			jta.append("���� : ���(�����) ���� ����\n");
		}
	}

	// ����� ����Ʈ (ä�ù� ���� ��� ����ڵ鿡�� ����)
	public void userList(String rNum) {
		String ul = "/" + rNum;
		Room temp = null;
		for (int i = 0; i < roomArray.size(); i++) {
			if (Integer.parseInt(rNum) == roomArray.get(i).getRoomNum()) {
				temp = roomArray.get(i);
				for (int j = 0; j < roomArray.get(i).getUserArray().size(); j++) {
					// ä�ù濡 ���ӵǾ� �ִ� �������� ���̵�+�г���
					ul += "/" + roomArray.get(i).getUserArray().get(j).toProtocol();
				}
			}
		}
		for (int i = 0; i < temp.getUserArray().size(); i++) {
			try {
				// ������ ����
				temp.getUserArray().get(i).getDos().writeUTF(User.UPDATE_ROOM_USERLIST + ul);
				jta.append("���� : ���(�����)-" + ul + "\n");
			} catch (IOException e) {
				jta.append("���� : ���(�����) ���� ����\n");
			}
		}
	}

	// ä�� �渮��Ʈ
	public void roomList(DataOutputStream target) {
		String rl = "";

		for (int i = 0; i < roomArray.size(); i++) {
			// ������� ä�ù���� ����
			rl += "/" + roomArray.get(i).toProtocol();
		}

		jta.append("test\n");

		try {
			// ������ ����
			target.writeUTF(User.UPDATE_ROOMLIST + rl);
			jta.append("���� : ���(��)-" + rl + "\n");
		} catch (IOException e) {
			jta.append("���� : ���(��) ���� ����\n");
		}
	}

	// ä�� �渮��Ʈ
	public void roomList() {
		String rl = "";

		for (int i = 0; i < roomArray.size(); i++) {
			// ������� ä�ù���� ����
			rl += "/" + roomArray.get(i).toProtocol();
		}

		jta.append("test\n");

		for (int i = 0; i < userArray.size(); i++) {

			try {
				// ������ ����
				userArray.get(i).getDos().writeUTF(User.UPDATE_ROOMLIST + rl);
				jta.append("���� : ���(��)-" + rl + "\n");
			} catch (IOException e) {
				jta.append("���� : ���(��) ���� ����\n");
			}
		}
	}
}