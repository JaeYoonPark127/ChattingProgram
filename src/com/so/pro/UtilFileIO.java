package com.so.pro;


import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class UtilFileIO {
	
	public static void main(String[] args) {
		File file = new File("./test.txt");
		saveFile(file, "�ٽ�test�Ӵ�");
		
		
		saveFile("�������̻���� ������ ��𼭳� ��Ÿ����\nȫ���� ~~~~");
		
		//���� �о����
		String str = loadFile(file);
		System.out.println("<���ϳ���>\n" + str);
		
		File filedt = new File("./20160825_173923.txt");
		System.out.println(loadFile(filedt));
	}

	
	// �������� : File�� str �����Ͽ� ���� ����
	public static boolean saveFile(File file, String str) {
		boolean result = false;
		//File�� String ����Ÿ ����
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(str);
			bw.flush();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) bw.close();
				if (fw != null) fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	//�������� : File�� JTextArea Text ����
	public static boolean saveFile(File file, JTextArea jta) {
		boolean result = false;

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		//File�� String ����Ÿ ����
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(jta.getText());
			bw.flush();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) bw.close();
				if (fw != null) fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	//�������� : File�� JTextPane Text ����
	public static boolean saveFile(File file, JTextPane jtp) {
		boolean result = false;

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		//File�� String ����Ÿ ����
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(jtp.getText());
			bw.flush();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) bw.close();
				if (fw != null) fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	//����ð��� ���ڿ�(����: yyyymmdd_hh24miss) ��ȯ
	public static String getCurrentDateTime() {
		GregorianCalendar calendar = new GregorianCalendar();
		String strDateTime = ""; 
		int year   = calendar.get(Calendar.YEAR);
		int month  = calendar.get(Calendar.MONTH) + 1;
		int day    = calendar.get(Calendar.DAY_OF_MONTH);
//		int hour   = calendar.get(Calendar.HOUR);
		int hour24 = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		String strYear  = String.valueOf(year);
		String strMonth = month < 10 ? "0" + month : String.valueOf(month);
		String strDay = day < 10 ? "0" + day : String.valueOf(day);
		String strHour = hour24 < 10 ? "0" + hour24 : String.valueOf(hour24);
		String strMinute = minute < 10 ? "0" + minute : String.valueOf(minute);
		String strSecond = second < 10 ? "0" + second : String.valueOf(second);
		
		strDateTime = strYear + strMonth + strDay + "_" + strHour + strMinute + strSecond;
		return strDateTime;
	}
	
	//����ð��� ���ڿ�(����: yyyymmdd + delimeter + hh24miss) ��ȯ
	public static String getCurrentDateTime(String delimeter) {
		GregorianCalendar calendar = new GregorianCalendar();
		String strDateTime = ""; 
		int year   = calendar.get(Calendar.YEAR);
		int month  = calendar.get(Calendar.MONTH) + 1;
		int day    = calendar.get(Calendar.DAY_OF_MONTH);
//				int hour   = calendar.get(Calendar.HOUR);
		int hour24 = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		String strYear  = String.valueOf(year);
		String strMonth = month < 10 ? "0" + month : String.valueOf(month);
		String strDay = day < 10 ? "0" + day : String.valueOf(day);
		String strHour = hour24 < 10 ? "0" + hour24 : String.valueOf(hour24);
		String strMinute = minute < 10 ? "0" + minute : String.valueOf(minute);
		String strSecond = second < 10 ? "0" + second : String.valueOf(second);
		
		strDateTime = strYear + strMonth + strDay 
				    + delimeter 
				    + strHour + strMinute + strSecond;
		return strDateTime;
	}	
	
	//�������� : JTextPane �� ����Ÿ�� yyyymmdd_hh24miss.txt �� ����
	// return : ����� ���ϸ�("./ + yyyymmdd_hh24miss.txt")
	public static String saveFile(JTextPane jtp) {
		String filename = "";
		String strFile = "./" + getCurrentDateTime() + ".txt";
		File file = new File(strFile);

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		//File�� String ����Ÿ ����
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(jtp.getText());
			bw.flush();
			filename = strFile;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) bw.close();
				if (fw != null) fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return filename;
	}
	
	//�������� : JTextArea �� ����Ÿ�� yyyymmdd_hh24miss.txt �� ����
	// return : ����� ���ϸ�("./ + yyyymmdd_hh24miss.txt")
	public static String saveFile(JTextArea jta) {
		String filename = "";
		String strFile = "./" + getCurrentDateTime() + ".txt";
		File file = new File(strFile);

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		//File�� String ����Ÿ ����
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(jta.getText());
			bw.flush();
			filename = strFile;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) bw.close();
				if (fw != null) fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return filename;
	}
	
	// �������� : ���ڿ� ����Ÿ�� yyyymmdd_hh24miss.txt �� ����
	// return : ����� ���ϸ�("./ + yyyymmdd_hh24miss.txt")
	public static String saveFile(String str) {
		String filename = "";
		String strFile = "./" + getCurrentDateTime() + ".txt";
		File file = new File(strFile);

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		//File�� String ����Ÿ ����
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(str);
			bw.flush();
			filename = strFile;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) bw.close();
				if (fw != null) fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return filename;
	}
	
	// �����о���� : File�� ����Ÿ�� �о�ͼ� String ���ڿ� ��ȯ
	public static String loadFile(File file) {
		String result = null;
		// ���Ͽ��� ���� ��������
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer(4096);
		
		try {
			br = new BufferedReader(new FileReader(file));
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str +"\n");
			}
			result = sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	// �����о���� : ���ϸ�(dir + filename)�� String���� �޾� ������ String���� ��ȯ
	// filename : ���丮 + ���ϸ� + Ȯ���� ������ ���Ե� ���ڿ�
	// return : filename�� ������ String���� return
	public static String loadFile(String filename) {
		if (filename == "") return "";
		String result = null;
		// ���Ͽ��� ���� ��������
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer(4096);
		File file = new File(filename);
		
		try {
			br = new BufferedReader(new FileReader(file));
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str + "\n");
			}
			result = sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	// ���ϴ��̾�α׸� �̿��Ͽ� ���ϸ�(���丮 + ����) ��������
	// currDir : ���ϰ˻��� ���丮 ��ġ
	// return : ���� ���̾�α׸� ���� ������ ���ϸ�(���丮 + ����)
	public static String getFilenameFromFileOpenDialog(String currDir) {
		String filename = null;
		FileDialog fd = null;
		fd = new FileDialog(fd, "���Ͽ���", FileDialog.LOAD);
		fd.setVisible(true);
		String dir = fd.getDirectory();
		String fname = fd.getFile();
		filename = dir + fname;
		return filename;
	}
}
