import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.*;
import javax.swing.text.*;

/* 第三个面板的面板类
 * 在这个类中定义了登录注册对话框
 * 
 */
public class loginPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1468905174708747189L;
	private JTextField username = new JTextField(15);
	private JPasswordField password = new JPasswordField(15);
	private JButton signin = new JButton("登陆");
	private JButton signup = new JButton("注册");
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
		JLabel label1 = new JLabel("用户名");
		label1.setFont(font);
		panel20.add(label1);
		panel20.add(username);
		JPanel panel21 = new JPanel();
		JLabel label2 = new JLabel("密码");
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

		box22.add(box20);
		box22.add(box21);
		label.setFont(font);
		Box box23 = Box.createVerticalBox();
		box23.add(box22);
		box23.add(label);
		lgn.add(box23);

		// 登陆消息处理
		signin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String user = username.getText().toString().trim();
				String pwd = String.valueOf(password.getPassword());
				if (user.length() < 1)
					JOptionPane.showMessageDialog(null, "用户名不能为空", "登陆错误", JOptionPane.ERROR_MESSAGE);
				else if (pwd.length() < 1)
					JOptionPane.showMessageDialog(null, "密码不能为空", "登陆错误", JOptionPane.ERROR_MESSAGE);
				else
				{
					int rtn = client.signin(user, pwd);
					if (rtn == 1)
					{
						JOptionPane.showMessageDialog(null, "登陆成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
						lgn.dispose();
						dict.setEnabled(true);
						dict.requestFocus();
						login = true;
					} else if (rtn == 0)
						JOptionPane.showMessageDialog(null, "用户名或密码错误", "登陆错误", JOptionPane.ERROR_MESSAGE);
					else if (rtn == -1)
						JOptionPane.showMessageDialog(null, "连接不上服务器", "登陆错误", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		// 注册消息处理
		signup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String user = username.getText().toString().trim();
				String pwd = String.valueOf(password.getPassword());
				if (user.length() < 1)
					JOptionPane.showMessageDialog(null, "用户名不能为空", "错误", JOptionPane.ERROR_MESSAGE);
				else if (user.replaceAll("\\s", "") != user)
					JOptionPane.showMessageDialog(null, "用户名不能有空白符", "错误", JOptionPane.ERROR_MESSAGE);
				else if (!user.matches("\\w+"))
					JOptionPane.showMessageDialog(null, "用户名只能由字母数字和下划线组成", "错误", JOptionPane.ERROR_MESSAGE);
				else if (pwd.length() < 1)
					JOptionPane.showMessageDialog(null, "密码不能为空", "错误", JOptionPane.ERROR_MESSAGE);
				else
				{
					int rtn = client.signup(user, pwd);
					if (rtn == 1)
					{
						JOptionPane.showMessageDialog(null, "注册成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
						lgn.dispose();
						dict.setEnabled(true);
						dict.requestFocus();
						login = true;
					} else if (rtn == 0)
						JOptionPane.showMessageDialog(null, "用户名已存在", "错误", JOptionPane.WARNING_MESSAGE);
					else if (rtn == -1)
						JOptionPane.showMessageDialog(null, "连接不上服务器", "错误", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		Font f0 = new Font("timesroman", Font.BOLD, 18);
		Font f1 = new Font("Dialog", Font.BOLD, 15);
		Box b0 = Box.createVerticalBox();
		Box b1 = Box.createHorizontalBox();
		JButton sendtext = new JButton("发送");
		JButton sendpic = new JButton("发送图片");
		sendtext.setFont(f1);
		sendpic.setFont(f1);
		sendtext.setPreferredSize(new Dimension(90, 30));
		sendpic.setPreferredSize(new Dimension(100, 30));
		b1.add(sendpic);
		b1.add(Box.createHorizontalGlue());
		b1.add(sendtext);
		b0.add(new JScrollPane(send));
		b0.add(b1);
//		accept.setLineWrap(true);
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

		// 列表切换重载
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
		// 发送图片
		sendpic.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser fc = new JFileChooser(WordCard.pdir);
				int returnval = fc.showOpenDialog(null);
				if (returnval == JFileChooser.APPROVE_OPTION)
				{
					File f = fc.getSelectedFile();
					String fileName = f.getName();
//					String regex = ".+\\.(JPG|jpg|bmp|BMP|gif|GIF|WBMP|png|PNG|wbmp|jpeg|JPEG)";
//					if (!fileName.matches(regex))
//					{
//						JOptionPane.showMessageDialog(null, "不支持的图片格式", "错误", JOptionPane.ERROR_MESSAGE);
//						return;
//					}
//					String filePath = fc.getSelectedFile().getAbsolutePath();
					try
					{
						FileInputStream in = new FileInputStream(f);
						int size = in.available();
						if (size > 1024 * 1024 * 50)
						{
							JOptionPane.showMessageDialog(null, "不支持的图片大小", "错误", JOptionPane.ERROR_MESSAGE);
							in.close();
							return;
						}
						String picxxx = fileName.substring(fileName.lastIndexOf('.'));
						client.sendpic(indexname + "$" + picxxx, in);
						Thread.sleep(100);

						Date date = new Date();
						SimpleDateFormat form = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						Document doc = accept.getDocument();
						String str1 = userName + " " + form.format(date) + "\n";
						str1 = str1 + String.format("向%s发送了图片%s\n", indexname, fileName);
						doc.insertString(doc.getLength(), str1, attr2);
						StringBuffer sb = docs.get(indexname);
						String content = sb.toString();
						docs.put(indexname, new StringBuffer(content + str1));
					} catch (Exception ex)
					{
						ex.printStackTrace();
					}

				}
			}
		});

		// 发送文字
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

	// 接收文字
	public void accText(String id, String str) {
		try
		{
			if (docs.containsKey(id))
				docs.get(id).append(str);
			else
			{
				docs.put(id, new StringBuffer(str));
			}

			System.out.println("point::" + indexname + " " + id);
			if (!indexname.equals(id))
				msg.put(id, true);
			refresh();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	// 接收图片转为接受文字处理
	// public void accPic() {
	//
	// }

	// 刷新列表
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
	}

	// panel显示
	public void click() {
		if (!login)
		{
			username.setText("");
			password.setText("");
			lgn.setVisible(true);
		} else
		{
			setIndex(lIndex);
		}
	}

	// 切换重载
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

	/*
	 * logindlg 对话框类，当未登录是点击“发现”会弹出登录对话框
	 */
	private class logindlg extends JFrame {
		/**
		 * 
		 */
		private static final long serialVersionUID = 716671922175637837L;

		logindlg() {
			setSize(300, 150);
			setResizable(false);
			setTitle("登陆");
			setLocationRelativeTo(null);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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
