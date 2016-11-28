
import java.io.*;
import java.util.regex.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class Dict extends JFrame {
	private JTabbedPane pane = new JTabbedPane(JTabbedPane.TOP);
	private JTextField dic = new JTextField(30);
	private JCheckBox baidu = new JCheckBox("百度", true);
	private JCheckBox youdao = new JCheckBox("有道", true);
	private JCheckBox jinshan = new JCheckBox("金山", true);
	private JEditorPane[] text = new JEditorPane[3];
	// private JScrollPane[] jsp = new JScrollPane[3];
	private TitledBorder[] border = new TitledBorder[3];
	private String[] dicts = { "百度", "有道", "金山" };

	private boolean isbaidu = true;
	private boolean isyoudao = true;
	private boolean isjinshan = true;

	Dict() {
		super();
		Font font = new Font("TimesRoman", Font.BOLD, 20);
		Font font1 = new Font("TimesRoman", Font.BOLD, 15);
		Font font2 = new Font("Dialog", Font.BOLD, 15);
		Font font3 = new Font("Dialog",Font.BOLD,12);
		JPanel panel0 = new JPanel();
		JLabel input = new JLabel("请输入单词：");
		JButton bsearch = new JButton("search");
		Box box00 = Box.createHorizontalBox();
		input.setFont(font2);
		dic.setFont(font2);
		bsearch.setFont(font2);
		box00.add(input);
		box00.add(dic);
		box00.add(bsearch);
		Box box01 = Box.createHorizontalBox();
		baidu.setFont(font3);
		youdao.setFont(font3);
		jinshan.setFont(font3);
		box01.add(baidu);
		box01.add(youdao);
		box01.add(jinshan);

		border[0] = new TitledBorder(dicts[0]);
		border[0].setTitleFont(font1);
		text[0] = new JEditorPane("text/html", "");
		text[0].setBorder(border[0]);
		text[0].setEditable(false);
		text[0].setPreferredSize(new Dimension(500,200));
		JScrollPane jsp = new JScrollPane(text[0]);
		// text[0].setSize(40,5);
		border[1] = new TitledBorder(dicts[1]);
		border[1].setTitleFont(font1);
		text[1] = new JEditorPane("text/html", "");
		text[1].setBorder(border[1]);
		text[1].setEditable(false);
		text[1].setPreferredSize(new Dimension(500,200));
		// text[1].setSize(40,5);
		border[2] = new TitledBorder(dicts[2]);
		border[2].setTitleFont(font1);
		text[2] = new JEditorPane("text/html", "");
		text[2].setBorder(border[2]);
		text[2].setEditable(false);
		text[2].setPreferredSize(new Dimension(500,200));
		// text[2].setSize(40,5);
		Box box02 = Box.createVerticalBox();
		box02.add(jsp);
		box02.add(text[1]);
		box02.add(text[2]);

		Box box0 = Box.createVerticalBox();
		box0.add(box00);
		box0.add(box01);
		box0.add(box02);
		panel0.add(box0);
		pane.addTab("", null, panel0, null);
		JLabel chachi = new JLabel("   查词   ");
		chachi.setFont(font);
		pane.setTabComponentAt(0, chachi);

		JPanel panel1 = new JPanel();
		pane.addTab("", null, panel1, null);
		JLabel fanyi = new JLabel("   翻译   ");
		fanyi.setFont(font);
		pane.setTabComponentAt(1, fanyi);

		loginPanel panel2 = new loginPanel();
		pane.addTab("", null, panel2, null);
		JLabel faxian = new JLabel("   发现   ");
		faxian.setFont(font);
		pane.setTabComponentAt(2, faxian);
		add(pane, BorderLayout.CENTER);

		// 给jbt添加监听器
		bsearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = dic.getText();
				if (isbaidu)
				{
					String content = "<body>"
							+ getPageContent("http://dict.baidu.com/s?wd=" + str + "&ptype=english", 0) + "</body>";
					text[0].setText(content);
				}
				if (isjinshan)
				{
					String content = getPageContent("http://www.iciba.com/" + str, 2);
					text[2].setText(content);
				}
				if (isyoudao)
				{
					String content = getPageContent("http://youdao.com/w/eng/" + str + "/#keyfrom=dict2.index", 1);
					text[1].setText(content);
				}
			}
		});
		baidu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isbaidu = !isbaidu;
			}
		});
		youdao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isyoudao = !isyoudao;
			}
		});
		jinshan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isjinshan = !isjinshan;
			}
		});
	}

	public static void main(String[] args) {
		JFrame frame = new Dict();
		frame.setSize(700, 800);
		frame.setResizable(false);
		frame.setTitle("Dict");
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public String getPageContent(String strUrl, int i) {
		try
		{
			URL url = new URL(strUrl);
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
			int beginIx = 0;
			int endIx = 0;
			System.out.println(content);
			Pattern r = Pattern.compile("");
			if (i == 0)
			{
				r = Pattern.compile("<div class=\"en-content\">\\s*<div>[\\s\\S]*?</div>[\\s\\S]*?</div>");
			} else if (i == 1)
			{
				beginIx = content.indexOf("<span class=\"keyword\">");
				endIx = content.indexOf("网络释义");
				r = Pattern.compile("<div class=\"trans-container\">[\\s\\S]*?</div>");
			} else
			{
				beginIx = content.indexOf("<span class=\"prop\">");
				endIx = content.indexOf("<div class=\"base-bt-bar\">");
				r = Pattern.compile(
						"<ul class=\"base-list switch_part\" class=\"\">[\\s\\S]*?</ul>(\\s*<li class=\"change clearfix\">[\\s\\S]*?</li>)?");
			}
			Matcher m = r.matcher(content);
			if (m.find())
			{
				String result = m.group().replaceAll("<a[\\s\\S]*?href=\"\\S*\">", " ");
				if (i == 2)
					result = result.replaceAll("<ul[\\S\\s]*?>|</ul>>|<p>|</p>", "");
				result = result.replaceAll("<li", "<p");
				result = result.replaceAll("</li", "</p");
				result = result.replaceAll("<ul>|</ul>", "");
				//System.out.println(result);
				return result;
			} else
				return "查无此词";
		} catch (IOException e)
		{
			System.err.println(e);
			return "查无此词";
		}

	}
}
