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
				while (connecting)
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
							System.out.println(name + " " + active);
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

	public static void like(int index) {// 0bing 1youdao 2jinshan
		if (index == 0 || index == 1 | index == 2)
		{
			try
			{
				int type = 3;
				Socket socket = new Socket(host, port);
				DataInputStream input = new DataInputStream(socket.getInputStream());
				DataOutputStream output = new DataOutputStream(socket.getOutputStream());
				output.writeInt(type);
				output.writeInt(index);
				int success = input.readInt();
				socket.close();
				if (success == 1)
					return;
				else
				{
					System.err.println("client.like wrong");
					return;
				}
			} catch (IOException ex)
			{
				System.err.println(ex);
			}
		} else
			System.err.println("client.like wrong");
	}

	public static void send(String s) {
		int type = 4;
	}
}
