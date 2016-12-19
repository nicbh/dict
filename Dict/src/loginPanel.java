import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.xml.parsers.DocumentBuilder;

import org.xml.sax.DocumentHandler;

// ���������������
public class loginPanel extends JPanel {
	private JTextField username = new JTextField(15);
	private JPasswordField password = new JPasswordField(15);
	private JButton signin = new JButton("��½");
	private JButton signup = new JButton("ע��");
	private JLabel label = new JLabel();
	private Font font = new Font("TimesRoman", Font.BOLD, 18);
	private Dict dict;

	private JList<String> users = new JList<String>();
	private JTextPane accept = new JTextPane();
	private HashMap<String, StringBuffer> docs = new HashMap<String, StringBuffer>();
	private HashMap<String, Boolean> msg = new HashMap<String, Boolean>();
	private JTextArea send = new JTextArea();
	public String userName = null;
	private int lIndex = 0;
	private String indexname = null;
	private SimpleAttributeSet attr1 = new SimpleAttributeSet();
	private SimpleAttributeSet attr2 = new SimpleAttributeSet();
	private JLabel lname = new JLabel();
	private final String msgu = "\u25CF";

	public Client client = new Client(this);
	public boolean login = false;

	private logindlg lgn = new logindlg();

	loginPanel(Dict dic) {
		dict = dic;
		Box box20 = Box.createVerticalBox();
		JPanel panel20 = new JPanel();
		JLabel label1 = new JLabel("�û���");
		label1.setFont(font);
		panel20.add(label1);
		panel20.add(username);
		JPanel panel21 = new JPanel();
		JLabel label2 = new JLabel("����");
		label2.setFont(font);
		panel21.add(label2);
		panel21.add(password);
		box20.add(panel20);
		box20.add(panel21);
		Box box21 = Box.createHorizontalBox();
		signin.setFont(font);
		signup.setFont(font);
		box21.add(signin);
		box21.add(signup);
		Box box22 = Box.createVerticalBox();

		box22.add(box20);// , BorderLayout.CENTER);
		box22.add(box21);// , BorderLayout.SOUTH);
		label.setFont(font);
		Box box23 = Box.createVerticalBox();
		box23.add(box22);
		box23.add(label);
		lgn.add(box23);

		signin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String user = username.getText().toString().trim();
				String pwd = String.valueOf(password.getPassword());
				if (user.length() < 1)
					JOptionPane.showMessageDialog(null, "�û�������Ϊ��", "��½����", JOptionPane.ERROR_MESSAGE);
				else if (pwd.length() < 1)
					JOptionPane.showMessageDialog(null, "���벻��Ϊ��", "��½����", JOptionPane.ERROR_MESSAGE);
				else
				{
					int rtn = client.signin(user, pwd);
					if (rtn == 1)
					{
						JOptionPane.showMessageDialog(null, "��½�ɹ���", "�ɹ�", JOptionPane.INFORMATION_MESSAGE);
						lgn.dispose();
						dict.setEnabled(true);
						dict.requestFocus();
						login = true;
					} else if (rtn == 0)
						JOptionPane.showMessageDialog(null, "�û������������", "��½����", JOptionPane.ERROR_MESSAGE);
					else if (rtn == -1)
						JOptionPane.showMessageDialog(null, "���Ӳ��Ϸ�����", "��½����", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		signup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String user = username.getText().toString().trim();
				String pwd = String.valueOf(password.getPassword());
				if (user.length() < 1)
					JOptionPane.showMessageDialog(null, "�û�������Ϊ��", "����", JOptionPane.ERROR_MESSAGE);
				else if (user.replaceAll("\\s", "") != user)
					JOptionPane.showMessageDialog(null, "�û��������пհ׷�", "����", JOptionPane.ERROR_MESSAGE);
				else if (!user.matches("\\w+"))
					JOptionPane.showMessageDialog(null, "�û���ֻ������ĸ���ֺ��»������", "����", JOptionPane.ERROR_MESSAGE);
				else if (pwd.length() < 1)
					JOptionPane.showMessageDialog(null, "���벻��Ϊ��", "����", JOptionPane.ERROR_MESSAGE);
				else
				{
					int rtn = client.signup(user, pwd);
					if (rtn == 1)
					{
						JOptionPane.showMessageDialog(null, "ע��ɹ���", "�ɹ�", JOptionPane.INFORMATION_MESSAGE);
						lgn.dispose();
						dict.setEnabled(true);
						dict.requestFocus();
						login = true;
					} else if (rtn == 0)
						JOptionPane.showMessageDialog(null, "�û����Ѵ���", "����", JOptionPane.WARNING_MESSAGE);
					else if (rtn == -1)
						JOptionPane.showMessageDialog(null, "���Ӳ��Ϸ�����", "����", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		Font f0 = new Font("timesroman", Font.BOLD, 18);
		Font f1 = new Font("Dialog", Font.BOLD, 15);
		Box b0 = Box.createVerticalBox();
		Box b1 = Box.createHorizontalBox();
		JButton sendtext = new JButton("����");
		JButton sendpic = new JButton("����ͼƬ");
		sendtext.setFont(f1);
		sendpic.setFont(f1);
		sendtext.setPreferredSize(new Dimension(90, 30));
		sendpic.setPreferredSize(new Dimension(100, 30));
		b1.add(sendpic);
		b1.add(Box.createHorizontalGlue());
		b1.add(sendtext);
		b0.add(new JScrollPane(send));
		b0.add(b1);
		// accept.setLineWrap(true);
		send.setFont(f0);
		send.setLineWrap(true);
		accept.setOpaque(false);
		accept.setEditable(false);
		Box b2 = Box.createVerticalBox();
		Box b3 = Box.createHorizontalBox();
		lname.setFont(f0);
		b3.add(lname);
		b3.add(Box.createHorizontalGlue());
		b2.add(b3);
		b2.add(new JScrollPane(accept));
		JSplitPane vspane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, b2, b0);
		vspane.setDividerLocation(530);
		vspane.setContinuousLayout(true);

		users.setFont(f0);
		users.setOpaque(false);
		((JComponent) users.getCellRenderer()).setOpaque(false);
		// users.setBackground(new Color(0, 0, 0, 0));
		// DefaultListModel<String> dlm = new DefaultListModel<String>();
		// dlm.addElement("awdad\u263A");
		// dlm.addElement("awdac\u25CB");
		// users.setModel(dlm);
		// indexname = "awdad";
		// lname.setText("awdad\u263A");

		users.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		users.setSelectedIndex(lIndex);
		JSplitPane hspane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(users), vspane);
		hspane.setDividerLocation(170);
		hspane.setPreferredSize(new Dimension(680, 720));
		hspane.setContinuousLayout(true);
		hspane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		add(hspane);

		users.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				try
				{
					if (e.getValueIsAdjusting())
					{

						// Document doc = accept.getDocument();
						// String content = doc.getText(0, doc.getLength());
						// docs.put(indexname, new StringBuffer(content));

						//
					} else if (!e.getValueIsAdjusting())
					{
						int index = users.getSelectedIndex();
						if (index < 0)
							index = lIndex;
						users.setSelectedIndex(index);
						setIndex(index);
						// if (index < 0)
						// return;
						// if (index != lIndex)
						// {
						// lIndex = index;
						// indexname = users.getSelectedValue();
						// if (indexname == null)
						// return;
						// Pattern p = Pattern.compile("\\w+");
						// Matcher m = p.matcher(indexname);
						// if (!m.find())
						// return;
						// indexname = m.group();
						// // indexname = indexname.substring(0,
						// // indexname.length() - 1);
						// accept.setText("");
						// Document doc = accept.getDocument();
						// lname.setText(users.getSelectedValue());
						// if (!docs.containsKey(indexname))
						// {
						// docs.put(indexname, new StringBuffer());
						// }
						// doc.insertString(0, docs.get(indexname).toString(),
						// attr1);
						// }
						// if (msg.containsKey(indexname) && msg.get(indexname)
						// == true)
						// {
						// msg.put(indexname, false);
						// refresh();
						// lname.setText(users.getSelectedValue());
						// }
					}
				} catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
		StyleConstants.setFontSize(attr1, 18);
		StyleConstants.setFontFamily(attr1, "timesroman");
		StyleConstants.setItalic(attr1, true);
		StyleConstants.setFontSize(attr2, 17);
		StyleConstants.setFontFamily(attr2, "timesroman");
		StyleConstants.setItalic(attr2, false);
		sendpic.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		sendtext.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String sendText = send.getText();
				while (sendText.length() > 0 && sendText.endsWith("\n"))
					sendText = sendText.substring(0, sendText.length() - 1);
				if (sendText.length() == 0)
					return;
				Date date = new Date();
				SimpleDateFormat form = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				try
				{
					Document doc = accept.getDocument();

					String str1 = userName + " " + form.format(date) + "\n";
					str1 = str1 + "        " + sendText + "\n";
					doc.insertString(doc.getLength(), str1, attr1);
					client.sendText(users.getSelectedIndex(), str1);
					StringBuffer sb = docs.get(indexname);
					String content = sb.toString();
					docs.put(indexname, new StringBuffer(content + str1));
					send.setText("");
				} catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});
	}

	public void accText(String id, String str) {
		try
		{
			if (docs.containsKey(id))
				docs.get(id).append(str);
			else
			{
				docs.put(id, new StringBuffer(str));
			}

			System.out.println("point::"+indexname+" "+id);
			if (!indexname.equals(id))
				msg.put(id, true);
//			setIndex(lIndex);
			refresh();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void accPic() {

	}

	public void refresh() {
		DefaultListModel<String> dlm = new DefaultListModel<String>();
		synchronized (client.userlist)
		{
			for (String s : client.userlist)
			{
				String un = s.substring(0, s.length() - 1);
				if (indexname == null)
				{
					indexname = un;
				}
				if (s.substring(0, s.length() - 1).equals(indexname))
					lIndex = dlm.getSize();
				if (msg.containsKey(un) && msg.get(un) == true)
					s = msgu + s;
				dlm.addElement(s);

				if (!docs.containsKey(un))
				{
					docs.put(un, new StringBuffer("\n"));
				}
			}
		}
		users.setModel(dlm);
		// users.setSelectedIndex(lIndex);
		// indexname = users.getSelectedValue();
		// if (indexname != null)
		// indexname = indexname.substring(0, indexname.length() - 1);
		// }

	}

	public void click() {
		if (!login)
		{
			username.setText("");
			password.setText("");
			lgn.setVisible(true);
			// lgn.setAlwaysOnTop(true);
		} else
		{
			setIndex(lIndex);

			// users.setSelectedIndex(lIndex);
			// accept.setText("");
			// Document doc = accept.getDocument();
			// lname.setText(users.getSelectedValue());
			// try
			// {
			// if (docs.containsKey(indexname))
			// doc.insertString(0, docs.get(indexname).toString(), attr1);
			// } catch (Exception ex)
			// {
			// ex.printStackTrace();
			// }
		}
	}

	private void setIndex(int index) {
		try
		{
			if (lIndex != users.getSelectedIndex())
			{
				lIndex = index;
				users.setSelectedIndex(index);
				indexname = users.getSelectedValue();
				System.out.println(indexname);
				Pattern p = Pattern.compile("\\w+");
				Matcher m = p.matcher(indexname);
				// if (!m.find())
				// return;
				System.out.println(m.find());
				indexname = m.group();
				if (msg.containsKey(indexname) && msg.get(indexname) == true)
				{
					msg.put(indexname, false);
				}
				refresh();
				index = lIndex;
				users.setSelectedIndex(index);
			}
			accept.setText("");
			Document doc = accept.getDocument();
			lname.setText(users.getSelectedValue());
			// if (!docs.containsKey(indexname))
			// {
			// docs.put(indexname, new StringBuffer());
			// }
			doc.insertString(0, docs.get(indexname).toString(), attr1);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private class logindlg extends JFrame {
		logindlg() {
			setSize(300, 150);
			setResizable(false);
			setTitle("��½");
			setLocationRelativeTo(null);
			setDefaultCloseOperation(lgn.DISPOSE_ON_CLOSE);
			this.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosing(WindowEvent e) {
					// TODO Auto-generated method stub
					dict.setEnabled(true);
					dict.requestFocus();
				}
			});
			this.addWindowFocusListener(new WindowFocusListener() {

				@Override
				public void windowLostFocus(WindowEvent e) {
					// TODO Auto-generated method stub
					lgn.requestFocus();
					;
				}

				@Override
				public void windowGainedFocus(WindowEvent e) {
					// TODO Auto-generated method stub

				}
			});
		}
	}
}
