
import java.sql.*;
import java.util.*;
import java.io.*;
import org.apache.commons.dbcp2.*;
import javax.sql.DataSource;


//数据库处理类
public class dbc {
	private PreparedStatement ps0;
	private PreparedStatement ps1;
	private PreparedStatement ps2;
	private PreparedStatement ps3;
	private PreparedStatement ps4;
	private Statement stmt;
	//在线用户列表
	private Map<Integer, String> id_name = Collections.synchronizedMap(new HashMap<Integer, String>());
//	private final String dbconn = "jdbc:mysql://localhost/dict?useSSL=true";
//	private final String dbun = "root";
//	private final String dbpw = "qwer";
	private DataSource ds;
	String queryString0 = "select id,password " + "from login " + "where username=?";
	String queryString1 = "insert into login " + "values (?,?,?)";
	String queryString2 = "select * " + "from info " + "where id=?";
	String queryString3 = "update info " + "set bing=?,youdao=?,jinshan=? " + "where id=?";
	String queryString4 = "insert into info values (?,?,?,?)";

	dbc() {
		Properties pro = new Properties();
		try {
			pro.load(new FileInputStream(new File("dbcpconfig.properties")));
			ds = BasicDataSourceFactory.createDataSource(pro);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("Initial config error!");
		}
		try
		{
//			Class.forName("com.mysql.jdbc.Driver");
//			System.out.println("Driver loaded");
//			Connection connection = DriverManager.getConnection(dbconn, dbun, dbpw);
			Connection connection = ds.getConnection();
			System.out.println("Database connected");
			stmt = connection.createStatement();

			String queryString = "select id,username from login";
			ResultSet rset = stmt.executeQuery(queryString);
			while (rset.next())
			{
				id_name.put(rset.getInt(1), rset.getString(2));
			}
			rset.close();
		} catch (Exception ex)
		{
			System.err.println(ex);
		}
	}

	public String getName(int id) {
		return id_name.get(id);
	}

//	获取id列表
	public int[] sql_id() {
		synchronized (id_name)
		{
			int[] ret = new int[id_name.size()];
			int i = 0;
			for (int id : id_name.keySet())
			{
				ret[i++] = id;
			}
			return ret;
		}
	}

//	检查用户名是否合法
	public boolean checkname(String username) {
		return id_name.containsValue(username);
	}

//	登陆检查
	public int sql_signin(String username, String password) {
		try
		{
//			Connection connection = DriverManager.getConnection(dbconn, dbun, dbpw);
			Connection connection = ds.getConnection();
			ps0 = connection.prepareStatement(queryString0);
			ps0.setString(1, username);
			ResultSet rset = ps0.executeQuery();
			if (rset.next())
			{
				if (password.equals(rset.getString(2)))
					return rset.getInt(1);
			}
			return -1;
		} catch (SQLException ex)
		{
			System.err.println(ex);
			return -1;
		}
	}

//	注册
	public int sql_signup(String username, String password) {
		try
		{
//			Connection connection = DriverManager.getConnection(dbconn, dbun, dbpw);
			Connection connection = ds.getConnection();
			ps1 = connection.prepareStatement(queryString1);
			int id = id_name.size();
			ps1.setInt(1, id);
			ps1.setString(2, username);
			ps1.setString(3, password);
			ps1.executeUpdate();
			int[] likes = { 0, 0, 0 };

			ps4 = connection.prepareStatement(queryString4);
			ps4.setInt(1, id);
			ps4.setInt(2, likes[0]);
			ps4.setInt(3, likes[1]);
			ps4.setInt(4, likes[2]);
			ps4.executeUpdate();
			id_name.put(id, username);
			return id;
		} catch (SQLException ex)
		{
			System.err.println(ex);
			return -1;
		}
	}

//	点赞列表
	public int[] sql_likes(int id) {
		try
		{
//			Connection connection = DriverManager.getConnection(dbconn, dbun, dbpw);
			Connection connection = ds.getConnection();
			ps2 = connection.prepareStatement(queryString2);
			ps2.setInt(1, id);
			ResultSet rset = ps2.executeQuery();
			if (rset.next())
			{
				int[] like = new int[3];
				like[0] = rset.getInt(2);
				like[1] = rset.getInt(3);
				like[2] = rset.getInt(4);
				return like;
			}
			return null;
		} catch (SQLException ex)
		{
			System.err.println(ex);
			return null;
		}
	}

//	点赞
	public boolean set_likes(int id, int[] likes) {
		if (likes.length != 3)
			return false;
		try
		{
//			Connection connection = DriverManager.getConnection(dbconn, dbun, dbpw);
			Connection connection = ds.getConnection();
			ps3 = connection.prepareStatement(queryString3);
			ps3.setInt(1, likes[0]);
			ps3.setInt(2, likes[1]);
			ps3.setInt(3, likes[2]);
			ps3.setInt(4, id);
			ps3.executeUpdate();
			return true;
		} catch (SQLException ex)
		{
			System.err.println(ex);
			return false;
		}
	}
}