import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.net.*;
import javax.swing.*;

// ���������������
public class loginPanel extends JPanel {
	private JTextField username = new JTextField(15);
	private JPasswordField password = new JPasswordField(15);
	private JButton signin = new JButton("��½");
	private JButton signup = new JButton("ע��");
	private JLabel label = new JLabel();
	private Font font = new Font("TimesRoman", Font.BOLD, 18);

	loginPanel() {
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
					label.setText("�û�������Ϊ��");
				else if (pwd.length() < 1)
					label.setText("���벻��Ϊ��");
				else
				{
					int rtn = Client.signin(user, pwd);
					if (rtn == 1)
						label.setText("��½�ɹ�");
					else if (rtn == 0)
						label.setText("�û����Ѵ���");
					else if (rtn == -1)
						label.setText("���Ӳ��Ϸ�����");
				}
			}
		});
		signup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String user = username.getText().toString().trim();
				String pwd = String.valueOf(password.getPassword());
				if (user.length() < 1)
					label.setText("�û�������Ϊ��");
				else if (pwd.length() < 1)
					label.setText("���벻��Ϊ��");
				else
				{
					int rtn = Client.signup(user, pwd);
					if (rtn == 1)
						label.setText("ע��ɹ�");
					else if (rtn == 0)
						label.setText("�û����Ѵ���");
					else if (rtn == -1)
						label.setText("���Ӳ��Ϸ�����");
				}
			}
		});
	}

}
