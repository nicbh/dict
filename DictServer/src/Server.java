
import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.net.*;
import java.sql.*;

public class Server extends JFrame {
	private JTextArea text = new JTextArea();
	private dbc database = new dbc();

	public static void main(String[] args) {
		new Server();
	}

	Server() {
		super();
		String[] users = database.sql_username();
		for (String us : users)
			System.out.println(us);
		System.out.println(database.sql_signin("admin", "admin"));
		System.out.println(database.sql_signup("admn", "admm"));
		System.out.println(database.sql_signin("adn", "admm"));
		// System.out.println(database.sql_signup("", ""));
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
				text.append("Starting thread for client )" + clientNo + " at " + new java.util.Date() + '\n');
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
					{ // 登陆
						text.append("signin...\n");
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
					{ // 注册
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
					{// 点赞
						text.append("like...\n");
						int index = input.readInt();

					} else if (type == 4)
					{ // 发送消息

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

	private class dbc {
		private PreparedStatement ps0;
		private PreparedStatement ps1;
		private PreparedStatement ps2;
		private PreparedStatement ps3;
		private Statement stmt;

		dbc() {
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
				System.out.println("Driver loaded");
				Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/dict", "root", "qwer");
				System.out.println("Database connected");
				String queryString0 = "select password " + "from login " + "where username=?";
				String queryString1 = "insert into login " + "values (?,?)";
				String queryString2 = "select * " + "from info " + "where username=?";
				String queryString3 = "update info " + "set bing=?,youdao=?,jinshan=? " + "where username=?";
				ps0 = connection.prepareStatement(queryString0);
				ps1 = connection.prepareStatement(queryString1);
				ps2 = connection.prepareStatement(queryString2);
				ps3 = connection.prepareStatement(queryString3);
				stmt = connection.createStatement();
			} catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		public String[] sql_username() {
			try
			{
				String queryString = "select * from login";
				ResultSet rset = stmt.executeQuery(queryString);
				StringBuilder strbld = new StringBuilder("");
				while (rset.next())
				{
					strbld.append(rset.getString(1) + " ");
				}
				return strbld.toString().trim().split(" ");
			} catch (SQLException ex)
			{
				ex.printStackTrace();
				return null;
			}
		}

		public boolean sql_signin(String username, String password) {
			try
			{
				ps0.setString(1, username);
				ResultSet rset = ps0.executeQuery();
				if (rset.next())
				{
					if (password.equals(rset.getString(1)))
						return true;
				}
				return false;
			} catch (SQLException ex)
			{
				ex.printStackTrace();
				return false;
			}
		}

		public boolean sql_signup(String username, String password) {
			try
			{
				ps1.setString(1, username);
				ps1.setString(2, password);
				ps1.executeUpdate();
				return true;
			} catch (SQLException ex)
			{
				ex.printStackTrace();
				return false;
			}
		}
	}
}
