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
}
