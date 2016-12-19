import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JOptionPane;

//���罻����̬������
public class Client {
	private static final int port = 7795;
	private static final String host = "localhost";
	private final int askcode = 12368;
	private final int textcode = 97653;
	private final int piccode = 86432;
	Socket socket;
	DataInputStream input;
	DataOutputStream output;
	boolean connecting = false;
	public ArrayList<String> userlist = new ArrayList<String>();
	private TreeMap<String, Integer> userstate = new TreeMap<String, Integer>();
	private final String isonline = "\u263A";
	private final String isoffline = "\u25CB";
	private loginPanel lPanel;
	private String username;

	Client(loginPanel lpn) {
		lPanel = lpn;
	}

	private void connect() {
		try
		{
			socket = new Socket(host, port);
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException ex)
		{
			// ex.printStackTrace();
		}
	}

	public class clientConnect implements Runnable {
		public void run() {
			try
			{
				int[] like = Dict.likes;
				like[0] = input.readInt();
				like[1] = input.readInt();
				like[2] = input.readInt();
				System.out.printf("%d %d %d\n", like[0], like[1], like[2]);
				while (connecting)
				{
					boolean refresh = false;
					synchronized (output)
					{
						output.writeInt(askcode);
						int res = input.readInt();
						if (res == textcode)
						{
							String name, content;
							name = input.readUTF();
							content = input.readUTF();
							lPanel.accText(name, content);
							output.writeInt(textcode);
						}
						if (res == askcode)
						{
							int length = input.readInt();
							for (int i = 0; i < length; i++)
							{
								String name = input.readUTF();
								int active = input.readInt();
								if (userstate.get(name) == null || userstate.get(name) != active)
								{
									userstate.put(name, active);
									refresh = true;
								}
							}
						}
					}
					if (refresh)
					{
						synchronized (userlist)
						{
							Set<Map.Entry<String, Integer>> entrySet = userstate.entrySet();
							userlist.clear();
							for (Map.Entry<String, Integer> entry : entrySet)
							{
								if (entry.getValue() == 1 && !entry.getKey().equals(username))
									userlist.add(entry.getKey() + isonline);
							}
							for (Map.Entry<String, Integer> entry : entrySet)
							{
								if (entry.getValue() == 0 && !entry.getKey().equals(username))
									userlist.add(entry.getKey() + isoffline);
							}
							System.out.println(userlist);
						}
						lPanel.refresh();
					}
					Thread.sleep(1000);
				}
			} catch (Exception ex)
			{
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "�����ж�", "����", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	private void start() {
		connecting = true;
		lPanel.userName = username;
		new Thread(new clientConnect()).start();
	}

	public int signin(String username, String password) {
		connect();
		int type = 1;
		try
		{
			this.username = username;
			output.writeInt(type);
			output.writeUTF(username);
			output.writeUTF(password);
			int success = input.readInt();
			if (success == 1)
				start();
			return success;
		} catch (Exception ex)
		{
			ex.printStackTrace();
			return -1;
		}
	}

	public int signup(String username, String password) {
		connect();
		int type = 2;
		try
		{
			this.username = username;
			output.writeInt(type);
			output.writeUTF(username);
			output.writeUTF(password);
			int success = input.readInt();
			if (success == 1)
				start();
			return success;
		} catch (Exception ex)
		{
			ex.printStackTrace();
			return -1;
		}
	}

	public boolean like() {// 0bing 1youdao 2jinshan
		if (!connecting)
			return true;
		int[] like = Dict.likes;
		if (like.length == 3)
		{
			int type = 11111;
			try
			{
				int success = 0;
				synchronized (output)
				{
					output.writeInt(type);
					for (int i = 0; i != like.length; i++)
						output.writeInt(like[i]);
					success = input.readInt();
					System.out.println("success " + success);
				}
				if (success == type)
					return true;
				else
				{
					System.err.println("client.like wrong");
					return false;
				}
			} catch (IOException ex)
			{
				ex.printStackTrace();
				return false;
			}
		} else
		{
			System.err.println("client.like wrong " + like.length);
			return false;
		}
	}

	public void sendText(int index, String s) {
		int type = textcode;
		try
		{
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					int success = 97654;
					try
					{
						while (success != 97653)
						{
							System.out.println(success);
							synchronized (output)
							{
								output.writeInt(type);
								String name = userlist.get(index);
								name = name.substring(0, name.length() - 1);
								output.writeUTF(name);
								output.writeUTF(s);
								success = input.readInt();
							}
							Thread.sleep(100);
						}
					} catch (Exception ex)
					{
						System.err.println("client.sendText wrong");
						System.err.println(ex);
					}
				}
			}).start();
		} catch (Exception ex)
		{
			System.err.println("client.sendText wrong");
			System.err.println(ex);
		}
	}
}
