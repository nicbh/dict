import java.io.*;
import java.net.*;
import java.util.*;

//网络交互静态方法类
public class Client {
	private static int port = 7795;
	private static String host = "localhost";

	public static int signin(String username, String password) {
		int type = 1;
		try
		{
			Socket socket = new Socket(host, port);
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			output.writeInt(type);
			output.writeUTF(username);
			output.writeUTF(password);
			int success = input.readInt();
			socket.close();
			return success;
		} catch (IOException ex)
		{
			System.err.println(ex);
			return -1;
		}
	}

	public static int signup(String username, String password) {
		int type = 2;
		try
		{
			Socket socket = new Socket(host, port);
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			output.writeInt(type);
			output.writeUTF(username);
			output.writeUTF(password);
			int success = input.readInt();
			socket.close();
			return success;
		} catch (IOException ex)
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
