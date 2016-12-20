
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.net.*;


/*
 * 服务器类，继承了JFrame
 */
public class Server extends JFrame {
	// 服务器监控
	private JTextArea text = new JTextArea();
	private dbc database = new dbc();
	// 用户在线情况表
	private Map<Integer, Integer> users = Collections.synchronizedMap(new HashMap<Integer, Integer>());
	// 消息缓存列表
	private HashMap<String, String> sendt = new HashMap<String, String>();
	// 图片缓存列表
	private HashMap<String, String> sendp = new HashMap<String, String>();

	/*
	 * 服务器程序运行接口
	 */
	public static void main(String[] args) {
		new Server();
	}

	/*
	 * 服务器类的初始化信息，并且一直结束来自客户端的请求
	 */
	Server() {
		super();
		add(new JScrollPane(text), BorderLayout.CENTER);
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
		// 如果有用户登录或者注册，新建一个线程处理用户交互
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


	/*
	 * 内部类：主要是当有个客户端与服务器建立连接时，服务器新建一个线程来处理客户端的请求
	 * 里面处理了来自客户端的各种请求，比如登录，注册
	 */
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
					// 发送点赞列表
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
								// 发生消息
								synchronized (sendt)
								{
									if (sendt.containsKey(name) && sendt.get(name) != null)
									{
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
												text.append(No + "sendtext success: " + name + "; " + content + "\n");
											}
										}
									}
								}
								// 发送图片
								synchronized (sendp)
								{
									if (sendp.containsKey(name) && sendp.get(name) != null)
									{
										String files = sendp.get(name);
										String[] filename = files.split("\\|");
										output.writeInt(piccode);
										output.writeInt(filename.length);
										sendp.put(name, null);

										for (int i = 0; i < filename.length; i++)
										{
											String fname = filename[i];
											String picxxx = fname.substring(fname.lastIndexOf('.'));
											String sendname = fname.substring(0, fname.indexOf('$'));
											output.writeUTF(sendname + "$" + picxxx);
											String llen = fname.substring(0, fname.lastIndexOf('$'));
											llen = llen.substring(llen.lastIndexOf('$') + 1);
											try
											{
												FileInputStream in = new FileInputStream(new File(fname));
												int flength = in.available();
												if (flength != Integer.parseInt(llen))
												{
													in.close();
													throw new IOException();
												}
												output.writeInt(flength);

												byte[] outputbyte = new byte[1024];
												int flen = 1024;
												if (flength < flen)
													flen = flength;
												while (flength > 0)
												{
													in.read(outputbyte, 0, flen);
													output.write(outputbyte, 0, flen);
													output.flush();
													flength -= flen;
													if (flength < flen)
														flen = flength;
												}
												in.close();
												int success = piccode + 1;
												success = input.readInt();
												if (success != piccode)
												{
													synchronized (text)
													{
														text.append(No + "send failed: " + fname + "\n");
													}
												} else
												{
													synchronized (text)
													{
														text.append(No + "send success: " + fname + "\n");
													}
												}
											} catch (IOException ex)
											{
												System.err.println(ex);
												synchronized (text)
												{
													text.append(No + "file error: " + fname + "\n");
												}
												output.writeInt(0);
											}
										}
									}
								}
								// 发送点赞列表
								output.writeInt(askcode);
								length = users.size();
								output.writeInt(length);
								for (Entry<Integer, Integer> entry : users.entrySet())
								{
									output.writeUTF(database.getName(entry.getKey()));
									output.writeInt(entry.getValue());
								}

							} else if (response == likecode)
							{// 点赞处理
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
							{//接受消息
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
							{//接受图片
								String name = input.readUTF();
								int inddd = name.lastIndexOf('$');
								String picxxx = name.substring(inddd + 1);
								name = name.substring(0, inddd);
								int len = input.readInt();
								String filename = user.getName() + "$" + name + "$" + len + "$"
										+ new Date().getTime() % 100000 + picxxx;
								FileOutputStream fos = new FileOutputStream(new File(filename));
								byte[] inputbyte = new byte[1024];
								int lengthh = 1024;
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
									text.append(No + "sendpic: " + name + "; " + filename + "\n");
								}
							}
						} catch (SocketException ex)
						{
							// System.err.println(ex);
							break;
						}
					}
					users.put(user.getId(), 0);
				}
				socket.close();
			} catch (IOException ex)
			{
				System.err.println(ex);
			}
			users.put(user.getId(), 0);
			synchronized (text)
			{
				text.append(No + "closed\n");
			}
		}

		/*
		 * 
		 */
		private boolean start() {
			try
			{
				input = new DataInputStream(socket.getInputStream());
				output = new DataOutputStream(socket.getOutputStream());
				int type = input.readInt();
				if (type == 1)
				{ // 登陆
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
				{ // 注册
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
				System.err.println(ex);
				return false;
			}
		}
	}

//	用户信息内部类
	public class User {
		private int id;
		private String username;
		private String password;

		User(String username, String password) {
			this.username = username;
			this.password = password;
		}

//		注册
		public boolean confirmName() {
			if (database.checkname(username))
				return false;
			else
			{
				id = database.sql_signup(username, password);
				return true;
			}
		}

//		登陆
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

//		获取点赞列表
		public int[] getLikes() {
			int[] likes = database.sql_likes(id);
			if (likes.length != 3)
				return null;
			else
				return likes;
		}

//		点赞
		public boolean setLikes(int[] likes) {
			return database.set_likes(id, likes);
		}
	}
}
