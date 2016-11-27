
import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.net.*;

public class Server extends JFrame {
	private JTextArea text = new JTextArea();

	public static void main(String[] args) {
		new Server();
	}

	Server() {
		super();
		// JPanel panel = new JPanel();
		// panel.add(text);
		add(new JScrollPane(text), BorderLayout.CENTER);// panel);
		setSize(400, 300);
		setResizable(false);
		setTitle("Server");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		try
		{
			ServerSocket serverSocket = new ServerSocket(7795);
			text.append("Start to work\n");
			int clientNo = 1;
			while (true)
			{
				Socket socket = serverSocket.accept();
				text.append("Starting thread for client )" + clientNo + " at " + new Date() + '\n');
				InetAddress inetAddress = socket.getInetAddress();
				text.append("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + '\n');
				text.append("Client " + clientNo + "'s IP Addresss is " + inetAddress.getHostAddress() + '\n');

				new Thread(new HandleAClient(socket)).start();
				clientNo++;
			}
		} catch (IOException ex)
		{
			System.err.println(ex);
		}
	}

	private class HandleAClient implements Runnable {
		private Socket socket;

		public HandleAClient(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try
			{
				// DataInputStream input = new
				// DataInputStream(socket.getInputStream());
				// DataOutputStream output=new
				// DataOutputStream(socket.getOutputStream());
				DataInputStream input = new DataInputStream(socket.getInputStream());
				DataOutputStream output = new DataOutputStream(socket.getOutputStream());
				while (true)
				{
					int type = input.readInt();
					if (type == 1)
					{ // µÇÂ½
						String username = input.readUTF();
						String password = input.readUTF();
						SignObject so = new SignObject(username, password);
						boolean success = so.confirm();
						text.append(so.toString() + " signin " + success + "\n");
						int rtn = (success) ? 1 : 0;
						output.writeInt(rtn);
						if (success)
						{

						}
					} else if (type == 2)
					{ // ×¢²á
						text.append("signup...\n");
						String username = input.readUTF();
						String password = input.readUTF();
						SignObject so = new SignObject(username, password);
						boolean success = so.confirmName();
						text.append(so.toString() + " signup " + success + "\n");
						int rtn = (success) ? 1 : 0;
						output.writeInt(rtn);
						if (success)
						{

						}
					} else if (type == 3)
					{ // ·¢ËÍÏûÏ¢

					}
				}
			} catch (IOException ex)
			{
				System.err.println(ex);
			}
			text.append("socket closed\n");
		}
	}

	private class SignObject implements Serializable {
		private String username;
		private String password;

		public SignObject(String name, String pwd) {
			username = name;
			password = pwd;
		}

		public boolean confirmName() {
			return true;
		}

		public boolean confirm() {
			return true;
		}

		public String toString() {
			String rt = "username:" + username + "\t" + "password:" + password;
			return rt;
		}
	}
}
