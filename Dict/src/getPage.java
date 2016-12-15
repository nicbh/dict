import java.io.*;
import java.net.*;
import java.util.regex.*;
import javax.swing.*;

public class getPage extends SwingWorker<String, Object> {
	private String Url;
	private int index;
	private JEditorPane r;

	getPage(String strUrl, int i, JEditorPane result) {
		Url = strUrl;
		index = i;
		r = result;
	}

	protected void done() {
		try
		{
			r.setText(get());
			System.out.println(index + " complete");
		} catch (Exception ex)
		{
			r.setText("连接不到在线词典");
		}
	}

	protected String doInBackground() {
		String content = getPageContent(Url, index);
		System.out.println(index + " gets content");
		return content;
	}

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