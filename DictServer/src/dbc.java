import java.sql.*;
import java.util.*;

public class dbc {
	private PreparedStatement ps0;
	private PreparedStatement ps1;
	private PreparedStatement ps2;
	private PreparedStatement ps3;
	private Statement stmt;
	private Map<Integer, String> id_name = Collections.synchronizedMap(new HashMap<Integer, String>());

	dbc() {
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver loaded");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/dict", "root", "qwer");
			System.out.println("Database connected");
			String queryString0 = "select id,password " + "from login " + "where username=?";
			String queryString1 = "insert into login " + "values (?,?,?)";
			String queryString2 = "select * " + "from info " + "where id=?";
			String queryString3 = "update info " + "set bing=?,youdao=?,jinshan=? " + "where id=?";
			ps0 = connection.prepareStatement(queryString0);
			ps1 = connection.prepareStatement(queryString1);
			ps2 = connection.prepareStatement(queryString2);
			ps3 = connection.prepareStatement(queryString3);
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
			ex.printStackTrace();
		}
	}

	public String getName(int id) {
		return id_name.get(id);
	}

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

	public boolean checkname(String username) {
		return id_name.containsValue(username);
	}

	public int sql_signin(String username, String password) {
		try
		{
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
			ex.printStackTrace();
			return -1;
		}
	}

	public int sql_signup(String username, String password) {
		try
		{
			int id = id_name.size();
			ps1.setInt(1, id);
			ps1.setString(2, username);
			ps1.setString(3, password);
			ps1.executeUpdate();
			id_name.put(id, username);
			return id;
		} catch (SQLException ex)
		{
			ex.printStackTrace();
			return -1;
		}
	}
}