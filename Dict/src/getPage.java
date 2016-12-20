import java.io.*;
import java.net.*;
import java.util.regex.*;
import javax.swing.*;


/*
 * 继承了SwingWoker<String,Object>
 * 由于网页的爬取需要时间，所以新建一个线程来处理
 */
public class getPage extends SwingWorker<String, Object> {
	private String Url;
	private int index;
	private JEditorPane rst;
	private String[] ctt;

	/*
	 * 给getPage类中的变量赋初值
	 * @param strURL 网页爬取的地址
	 * @param i 用来标识三个JEditorPane
	 * @param result 用来接收HTML文本信息
	 * @param content 存储HTML文本信息
	 */
	getPage(String strUrl, int i, JEditorPane result, String[] content) {
		Url = strUrl;
		index = i;
		rst = result;
		ctt = content;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.SwingWorker#done()
	 */
	protected void done() {
		try
		{
			ctt[index] = get();
			rst.setText(ctt[index]);
			System.out.println(index + " complete");
		} catch (Exception ex)
		{
			ctt[index] = "连接不到在线词典";
			rst.setText(ctt[index]);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	protected String doInBackground() {
		String content = getPageContent(Url, index);
		System.out.println(index + " gets content");
		return content;
	}

	/*
	 * 获取网页信息，其中用i来标识“必应，有道，金山”中的一个
	 * @param strURL ： 网页地址
	 * @param i  i来标识“必应，有道，金山”中的一个
	 * @return 返回抓取后整理的内容
	 */
	public String getPageContent(String strUrl, int i) {
		try
		{
			URL url = new URL(strUrl);

			System.out.println(index + " starts");
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			InputStreamReader isr = new InputStreamReader(httpConn.getInputStream(), "utf-8");
			BufferedReader br = new BufferedReader(isr);

			String str;
			StringBuilder content = new StringBuilder();
			while ((str = br.readLine()) != null)
			{
				content.append(str);
				// if(i==2)
				// System.out.println(str);
			}
			System.out.println(index + " gets page");
			// System.out.println(content);
			Pattern r = Pattern.compile("");
			if (i == 0)
			{
				r = Pattern.compile("<ul><li><span class=\"pos[\\S\\s]*?</ul>");
				// r = Pattern.compile("<div
				// class=\"en-content\">\\s*<div>[\\s\\S]*?</div>[\\s\\S]*?</div>");
			} else if (i == 1)
			{
				r = Pattern.compile("<div class=\"trans-container\">[\\s\\S]*?</div>");
			} else
			{
				r = Pattern.compile(
						"<ul class=\"base-list switch_part\" class=\"\">[\\s\\S]*?</ul>(\\s*<li class=\"change clearfix\">[\\s\\S]*?</li>)?");
			}
			Matcher m = r.matcher(content);
			if (m.find())
			{
				// System.out.println(m.group());
				String result = m.group().replaceAll("<a[\\s\\S]*?href=\"\\S*\"[\\s\\S]*?>", " ");
				if (i == 0)
					result = result.replaceAll("网络", "网络释义：");
				if (i == 2)
				{
					result = result.replaceAll("<ul[\\S\\s]*?>|</ul>>|<p>|</p>", "");
					result = result.replaceAll("变形", "变形：");
				}
				result = result.replaceAll("<li", "<p");
				result = result.replaceAll("</li", "</p");
				result = result.replaceAll("<ul>|</ul>", "");
				// System.out.println(result);
				return result;
			} else
				return "查无此词";
		} catch (IOException e)
		{
			System.err.println(e);
			return "连接不到在线词典";
		}

	}
}