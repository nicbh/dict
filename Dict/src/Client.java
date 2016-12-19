import java.io.*;
import java.net.*;
import java.util.*;
import java.security.MessageDigest;

//网络交互静态方法类
public class Client {
	private static final int port = 7795;
	private static final String host = "localhost";
	private final int askcode = 12368;
	Socket socket;
	DataInputStream input;
	DataOutputStream output;
	boolean connecting = false;
	public ArrayList<String> userlist = new ArrayList<String>();
	private TreeMap<String, Integer> userstate = new TreeMap<String, Integer>();
	private final String isonline = "\u263A";
	private final String isoffline = "\u25CB";
	private loginPanel lPanel;

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
								// System.out.println(name + " " + active);
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
								if (entry.getValue() == 1)
									userlist.add(entry.getKey() + isonline);
							}
							for (Map.Entry<String, Integer> entry : entrySet)
							{
								if (entry.getValue() == 0)
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
			output.writeUTF(md5Encode(password));
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
			output.writeUTF(md5Encode(password));
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
	
	/*public int sentCard(String toUser,String path){
		connect();
		byte[] 
		int type = 4;
		try{
			File file = new File(path);
			
		}
		return 1;
	}*/

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
	
	
	/*** 
	 * MD5加密 生成32位md5码
	 * @param 待加密字符串
	 * @return 返回32位md5码
	 */
	public static String md5Encode(String inStr) throws Exception {
	    MessageDigest md5 = null;
	    try {
	        md5 = MessageDigest.getInstance("MD5");
	    } catch (Exception e) {
	        System.out.println(e.toString());
	        e.printStackTrace();
	        return "";
	    }

	    byte[] byteArray = inStr.getBytes("UTF-8");
	    byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
	    for (int i = 0; i < md5Bytes.length; i++) {
	        int val = ((int) md5Bytes[i]) & 0xff;
	        if (val < 16) {
	            hexValue.append("0");
	        }
	        hexValue.append(Integer.toHexString(val));
	    }
	    return hexValue.toString();
    }
	
}
