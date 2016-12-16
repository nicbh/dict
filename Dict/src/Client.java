import java.io.*;
import java.net.*;
import java.util.*;

//网络交互静态方法类
public class Client {
	private static final int port = 7795;
	private static final String host = "localhost";
	private final int askcode = 12368;
	Socket socket;
	DataInputStream input;
	DataOutputStream output;
	boolean connecting = false;

	Client() {
	}

	private void connect() {
		try
		{
			socket = new Socket(host, port);
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException ex)
		{
			ex.printStackTrace();
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
					synchronized (output)
					{
						output.writeInt(askcode);
						int res = input.readInt();
						if (res == askcode)
						{
							int length = input.readInt();
							for (int i = 0; i < length; i++)
							{
								String name = input.readUTF();
								int active = input.readInt();
								// System.out.println(name + " " + active);
							}
						}
					}
					Thread.sleep(1000);
				}
			} catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	private void start() {
		connecting = true;
		new Thread(new clientConnect()).start();
	}

	public int signin(String username, String password) {
		connect();
		int type = 1;
		try
		{
			output.writeInt(type);
			output.writeUTF(username);
			output.writeUTF(password);
			int success = input.readInt();
			if (success == 1)
				start();
			return success;
		} catch (Exception ex)
		{
			System.err.println(ex);
			return -1;
		}
	}

	public int signup(String username, String password) {
		connect();
		int type = 2;
		try
		{
			output.writeInt(type);
			output.writeUTF(username);
			output.writeUTF(password);
			int success = input.readInt();
			if (success == 1)
				start();
			return success;
		} catch (Exception ex)
		{
			System.err.println(ex);
			return -1;
		}
	}

	public boolean like() {// 0bing 1youdao 2jinshan
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
				System.err.println(ex);
				return false;
			}
		} else
		{
			System.err.println("client.like wrong " + like.length);
			return false;
		}
	}

	public static void send(String s) {
		int type = 4;
	}
}
