import java.io.*;
import java.net.*;
import java.util.regex.*;
import javax.swing.*;


/*
 * �̳���SwingWoker<String,Object>
 * ������ҳ����ȡ��Ҫʱ�䣬�����½�һ���߳�������
 */
public class getPage extends SwingWorker<String, Object> {
	private String Url;
	private int index;
	private JEditorPane rst;
	private String[] ctt;

	/*
	 * ��getPage���еı�������ֵ
	 * @param strURL ��ҳ��ȡ�ĵ�ַ
	 * @param i ������ʶ����JEditorPane
	 * @param result ��������HTML�ı���Ϣ
	 * @param content �洢HTML�ı���Ϣ
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
			ctt[index] = "���Ӳ������ߴʵ�";
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
	 * ��ȡ��ҳ��Ϣ��������i����ʶ����Ӧ���е�����ɽ���е�һ��
	 * @param strURL �� ��ҳ��ַ
	 * @param i  i����ʶ����Ӧ���е�����ɽ���е�һ��
	 * @return ����ץȡ�����������
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
					result = result.replaceAll("����", "�������壺");
				if (i == 2)
				{
					result = result.replaceAll("<ul[\\S\\s]*?>|</ul>>|<p>|</p>", "");
					result = result.replaceAll("����", "���Σ�");
				}
				result = result.replaceAll("<li", "<p");
				result = result.replaceAll("</li", "</p");
				result = result.replaceAll("<ul>|</ul>", "");
				// System.out.println(result);
				return result;
			} else
				return "���޴˴�";
		} catch (IOException e)
		{
			System.err.println(e);
			return "���Ӳ������ߴʵ�";
		}

	}
}