import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.net.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

// 第三个面板的面板类
public class loginPanel extends JPanel {
	private JTextField username = new JTextField(15);
	private JPasswordField password = new JPasswordField(15);
	private JButton signin = new JButton("登陆");
	private JButton signup = new JButton("注册");
	private JLabel label = new JLabel();
	private Font font = new Font("TimesRoman", Font.BOLD, 18);
	private Dict dict;

	private JList<String> users = new JList<String>();
	private JTextArea accept = new JTextArea();
	private JTextArea send = new JTextArea();

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
					JOptionPane.showMessageDialog(null, "用户名不能为空", "登陆错误", JOptionPane.ERROR_MESSAGE);
				else if (pwd.length() < 1)
					JOptionPane.showMessageDialog(null, "密码不能为空", "登陆错误", JOptionPane.ERROR_MESSAGE);
				else
				{
					int rtn = client.signin(user, pwd);
					if (rtn == 1)
					{
						label.setText("登陆成功");
						login = true;
					} else if (rtn == 0)
						JOptionPane.showMessageDialog(null, "用户名或密码错误", "登陆错误", JOptionPane.ERROR_MESSAGE);
					else if (rtn == -1)
						JOptionPane.showMessageDialog(null, "连接不上服务器", "登陆错误", JOptionPane.WARNING_MESSAGE);
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
					JOptionPane.showMessageDialog(null, "用户名不能为空", "错误", JOptionPane.ERROR_MESSAGE);
				else if (pwd.length() < 1)
					JOptionPane.showMessageDialog(null, "密码不能为空", "错误", JOptionPane.ERROR_MESSAGE);
				else
				{
					int rtn = client.signup(user, pwd);
					if (rtn == 1)
					{
						label.setText("注册成功");
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
		accept.setOpaque(false);
		b1.add(sendpic);
		b1.add(Box.createHorizontalGlue());
		b1.add(sendtext);
		b0.add(new JScrollPane(send));
		b0.add(b1);
		accept.setEditable(false);
		accept.setLineWrap(true);
		send.setLineWrap(true);
		JSplitPane vspane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(accept), b0);
		vspane.setDividerLocation(530);
		vspane.setContinuousLayout(true);

		users.setFont(f0);
		users.setOpaque(false);
		((JComponent)users.getCellRenderer()).setOpaque(false);
		//users.setBackground(new Color(0, 0, 0, 0));
		DefaultListModel<String> dlm = new DefaultListModel<String>();
		dlm.addElement("awdad\u263A");
		dlm.addElement("awdad\u25CB");
		users.setModel(dlm);
		users.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JSplitPane hspane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(users), vspane);
		hspane.setDividerLocation(200);
		hspane.setPreferredSize(new Dimension(680, 720));
		hspane.setContinuousLayout(true);
		hspane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		add(hspane);
	}

	public void refresh() {
		DefaultListModel<String> dlm = new DefaultListModel<String>();
		for (String s : client.userlist)
			dlm.addElement(s);
		users.setModel(dlm);
	}

	public void click() {
		username.setText("");
		password.setText("");
		lgn.setVisible(true);
		// lgn.setAlwaysOnTop(true);
	}

	private class logindlg extends JFrame {
		logindlg() {
			setSize(300, 150);
			setResizable(false);
			setTitle("登陆");
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
