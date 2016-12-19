
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.net.*;

public class Server extends JFrame {
	private JTextArea text = new JTextArea();
	private dbc database = new dbc();
	private Map<Integer, Integer> users = Collections.synchronizedMap(new HashMap<Integer, Integer>());
	private HashMap<String, String> sendt = new HashMap<String, String>();
	private HashMap<String, String> sendp = new HashMap<String, String>();

	public static void main(String[] args) {
		new Server();
	}

	Server() {
		super();
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
		int[] ids = database.sql_id();
		for (int id : ids)
		{
			users.put(id, 0);
			System.out.println(database.getName(id) + " " + id);
		}
		// System.out.println(database.sql_signin("admin", "admin"));
		// System.out.println(database.sql_signin("admn", "admm"));
		// System.out.println(database.sql_signin("adn", "admm"));
		try
		{
			ServerSocket serverSocket = new ServerSocket(7795);
			text.append("Start to work\n");
			int clientNo = 1;
			ExecutorService executor = Executors.newCachedThreadPool();
			while (true)
			{
				Socket socket = serverSocket.accept();
				text.append("Starting thread for client )" + clientNo + " at " + new java.util.Date() + '\n');
				InetAddress inetAddress = socket.getInetAddress();
				text.append("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + '\n');
				text.append("Client " + clientNo + "'s IP Addresss is " + inetAddress.getHostAddress() + '\n');

				executor.execute(new HandleAClient(socket, clientNo));
				clientNo++;
			}
		} catch (IOException ex)
		{
			System.err.println(ex);
		}
	}

	private class HandleAClient implements Runnable {
		private Socket socket;
		private String No;
		private User user;
		private final int askcode = 12368;
		private final int likecode = 11111;
		private final int textcode = 97653;
		private final int piccode = 86432;
		DataInputStream input;
		DataOutputStream output;
		int[] likes = null;

		public HandleAClient(Socket socket, int clientNo) {
			this.socket = socket;
			No = "Client " + clientNo + " ";
		}

		public void run() {
			try
			{
				if (start())
				{
					users.put(user.getId(), 1);
					likes = user.getLikes();
					for (int i = 0; i < likes.length; i++)
						output.writeInt(likes[i]);
					int length = users.size();
					output.writeInt(length);
					for (Entry<Integer, Integer> entry : users.entrySet())
					{
						output.writeUTF(database.getName(entry.getKey()));
						output.writeInt(entry.getValue());
					}
					while (true)
					{
						try
						{
							int response = input.readInt();
							if (response == askcode)
							{
								String name = user.getName();
								boolean send = false;
								synchronized (sendt)
								{
									if (sendt.containsKey(name) && sendt.get(name) != null)
									{
										send = true;
										String content = sendt.get(name);
										String nm = content.substring(0, content.indexOf(' '));
										output.writeInt(textcode);
										output.writeUTF(nm);
										output.writeUTF(content);
										int success = input.readInt();
										if (success == textcode)
										{
											sendt.put(name, null);
											synchronized (text)
											{
												text.append(No + "sendtext success: " + name + "; " + content);
											}
										}
									}
								}
								if (!send)
									output.writeInt(askcode);
								length = users.size();
								output.writeInt(length);
								for (Entry<Integer, Integer> entry : users.entrySet())
								{
									output.writeUTF(database.getName(entry.getKey()));
									output.writeInt(entry.getValue());
								}
								// synchronized (text)
								// {
								// text.append(No + "online");
								// }

							} else if (response == likecode)
							{
								likes[0] = input.readInt();
								likes[1] = input.readInt();
								likes[2] = input.readInt();
								synchronized (text)
								{
									text.append(No + "update likes: " + likes[0] + " " + likes[1] + " " + likes[2]);
								}
								if (user.setLikes(likes) == true)
									output.writeInt(likecode);
								else
									output.writeInt(likecode + 1);
							} else if (response == textcode)
							{
								String name, content;
								name = input.readUTF();
								content = input.readUTF();
								if (sendt.containsKey(name) && sendt.get(name) != null)
									content = sendt.get(name) + content;
								synchronized (sendt)
								{
									sendt.put(name, content);
								}
								output.writeInt(textcode);
								synchronized (text)
								{
									text.append(No + "sendtext: " + name + "; " + content);
								}
							} else if (response == piccode)
							{
								String name = input.readUTF();
								int len = input.readInt();
								String filename = name + len + new Date().getTime() % 50000 + ".jpg";
								FileOutputStream fos = new FileOutputStream(new File(filename));
								byte[] inputbyte = new byte[1024];
								int lengthh = 1000;
								if (len < lengthh)
									lengthh = len;
								while (len > 0)
								{
									input.read(inputbyte, 0, lengthh);
									len -= lengthh;
									fos.write(inputbyte, 0, lengthh);
									fos.flush();
									if (len < lengthh)
										lengthh = len;
								}
								fos.close();
								String content = filename;
								if (sendp.containsKey(name) && sendp.get(name) != null)
									content = sendp.get(name) + "|" + filename;
								synchronized (sendp)
								{
									sendp.put(name, content);
								}
								output.writeInt(piccode);
								synchronized (text)
								{
									text.append(No + "sendpic: " + name + "; " + filename);
								}
							}
						} catch (IOException ex)
						{
							// ex.printStackTrace();
							break;
						}
					}
					users.put(user.getId(), 0);
				}
				socket.close();
			} catch (IOException ex)
			{
				ex.printStackTrace();
			}
			synchronized (text)
			{
				text.append(No + "closed\n");
			}
		}

		private boolean start() {
			try
			{
				input = new DataInputStream(socket.getInputStream());
				output = new DataOutputStream(socket.getOutputStream());
				int type = input.readInt();
				if (type == 1)
				{ // µÇÂ½
					synchronized (text)
					{
						text.append(No + "signin...\n");
					}
					String username = input.readUTF();
					String password = input.readUTF();
					user = new User(username, password);
					boolean success = user.confirm();
					synchronized (text)
					{
						text.append(No + user.toString() + " signin " + success + "\n");
					}
					int rtn = (success) ? 1 : 0;
					output.writeInt(rtn);
					if (success)
						return true;
					else
						return false;
				} else if (type == 2)
				{ // ×¢²á
					synchronized (text)
					{
						text.append(No + "signup...\n");
					}
					String username = input.readUTF();
					String password = input.readUTF();
					user = new User(username, password);
					boolean success = user.confirmName();
					synchronized (text)
					{
						text.append(No + user.toString() + " signup " + success + "\n");
					}
					int rtn = (success) ? 1 : 0;
					output.writeInt(rtn);
					if (success)
						return true;
					else
						return false;
				} else
					return false;
			} catch (IOException ex)
			{
				ex.printStackTrace();
				return false;
			}
		}
	}

	public class User {
		private int id;
		private String username;
		private String password;

		User(String username, String password) {
			this.username = username;
			this.password = password;
		}

		public boolean confirmName() {
			if (database.checkname(username))
				return false;
			else
			{
				id = database.sql_signup(username, password);
				return true;
			}
		}

		public boolean confirm() {
			id = database.sql_signin(username, password);
			if (id == -1)
				return false;
			else
				return true;
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return username;
		}

		public String toString() {
			String rt = "id:" + id + "\t" + "username:" + username + "\t" + "password:" + password;
			return rt;
		}

		public int[] getLikes() {
			int[] likes = database.sql_likes(id);
			// if (likes == null)
			// {
			// int[] like = { 0, 0, 0 };
			// database.set_likes(id, like);
			// return like;
			// }
			if (likes.length != 3)
				return null;
			else
				return likes;
		}

		public boolean setLikes(int[] likes) {
			return database.set_likes(id, likes);
		}
	}
}
