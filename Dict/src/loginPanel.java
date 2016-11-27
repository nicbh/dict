import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.net.*;
import javax.swing.*;

// 第三个面板的面板类
public class loginPanel extends JPanel {
	private JTextField username = new JTextField(15);
	private JPasswordField password = new JPasswordField(15);
	private JButton signin = new JButton("登陆");
	private JButton signup = new JButton("注册");
	private JLabel label = new JLabel();
	private Font font = new Font("TimesRoman", Font.BOLD, 18);

	loginPanel() {
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

		box22.add(box20);//, BorderLayout.CENTER);
		box22.add(box21);//, BorderLayout.SOUTH);
		label.setFont(font);
		Box box23 = Box.createVerticalBox();
		box23.add(box22);
		box23.add(label);
		add(box23);

		signin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String user = username.getText().toString().trim();
				String pwd = String.valueOf(password.getPassword());
				if (user.length() < 1)
					label.setText("用户名不能为空");
				else if (pwd.length() < 1)
					label.setText("密码不能为空");
				else
				{
					int rtn = Client.signin(user, pwd);
					if (rtn == 1)
						label.setText("登陆成功");
					else if (rtn == 0)
						label.setText("用户名已存在");
					else if (rtn == -1)
						label.setText("连接不上服务器");
				}
			}
		});
		signup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String user = username.getText().toString().trim();
				String pwd = String.valueOf(password.getPassword());
				if (user.length() < 1)
					label.setText("用户名不能为空");
				else if (pwd.length() < 1)
					label.setText("密码不能为空");
				else
				{
					int rtn = Client.signup(user, pwd);
					if (rtn == 1)
						label.setText("注册成功");
					else if (rtn == 0)
						label.setText("用户名已存在");
					else if (rtn == -1)
						label.setText("连接不上服务器");
				}
			}
		});
	}

}
