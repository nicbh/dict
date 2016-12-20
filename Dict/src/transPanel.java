import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.regex.*;
import org.apache.commons.lang3.*;
import javax.script.*;
import javax.swing.*;

/*
 * 翻译对话框，定义了翻译选项中的界面
 * 以及相关的翻译功能
 */
public class transPanel extends JPanel {
	private JTextArea text = new JTextArea();
	private JTextArea trans = new JTextArea();
	private int type = 0;

	transPanel() {
		Box box = Box.createVerticalBox();
		Font font = new Font("Dialog", Font.BOLD, 15);
		Font font1 = new Font("TimesRoman", Font.BOLD, 10);
		Font font2 = new Font("TimesRoman", Font.PLAIN, 15);
		JButton button = new JButton("    翻译     ");
		text.setFont(font);
		text.setLineWrap(true);
		button.setFont(font2);
		// button.setContentAreaFilled(false);
		trans.setFont(font);
		trans.setLineWrap(true);
		trans.setEditable(false);
		trans.setOpaque(false);
		JScrollPane jsp1 = new JScrollPane(text);
		JScrollPane jsp2 = new JScrollPane(trans);

		jsp1.setPreferredSize(new Dimension(600, 310));
		button.setPreferredSize(new Dimension(100, 30));
		jsp2.setPreferredSize(new Dimension(600, 310));

		box.add(Box.createVerticalStrut(15));
		box.add(jsp1);
		box.add(Box.createVerticalStrut(10));

		Box box1 = Box.createHorizontalBox();
		String[] transFlags = { "英转中", "中转英", "英转繁", "中转繁", "英转日", "转成英文", "转成中文" };
		JComboBox<String> jcb = new JComboBox<String>(transFlags);
		jcb.setPreferredSize(new Dimension(100, 30));
		box1.add(Box.createHorizontalGlue());
		box1.add(jcb);
		box1.add(Box.createHorizontalStrut(10));
		box1.add(button);
		box.add(box1);

		box.add(Box.createVerticalStrut(10));
		box.add(jsp2);

		JLabel lb = new JLabel("此翻译由google翻译提供");
		lb.setFont(font1);
		Box box2 = Box.createHorizontalBox();
		box2.add(Box.createHorizontalGlue());
		box2.add(lb);
		box.add(box2);

		this.add(box);

		//对组合框设置监听事件
		jcb.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				type = jcb.getSelectedIndex();
			}
		});
		
		//翻译的按键监听事件
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						try
						{
							String srs = null, des = null;
							switch (type) {
							case 0:
								srs = "en";
								des = "zh-CN";
								break;
							case 1:
								srs = "zh-CN";
								des = "en";
								break;
							case 2:
								srs = "en";
								des = "zh-TW";
								break;
							case 3:
								srs = "zh-CN";
								des = "zh-TW";
								break;
							case 4:
								srs = "en";
								des = "ja";
								break;
							case 5:
								srs = "auto";
								des = "en";
								break;
							case 6:
								srs = "auto";
								des = "zh-CN";
								break;
							}
							String contents = text.getText();
							String tkk = tk(contents);
							contents = URLEncoder.encode(contents, "UTF-8");
							URL url = new URL(
									"http://translate.google.cn/translate_a/single?client=t&sl=" + srs + "&tl=" + des
											+ "&hl=zh-CN&dt=at&dt=bd&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=ss&dt=t&ie=UTF-8&oe=UTF-8&clearbtn=1&otf=1&pc=1&srcrom=0&ssel=0&tsel=0&kc=2&tk="
											+ tkk + "&q=" + contents);
							// #auto/en/"
							// +
							// "please");
							HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
							String browser = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0";
							// String browser = "Mozilla/4.0 (Windows; U;MSIE
							// 6.0;
							// Windows NT 6.1; SV1; .NET CLR 2.0.50727)";
							httpConn.setRequestProperty("User-Agent", browser);
							// httpConn.setDoInput(true);
							// httpConn.setDoOutput(true);
							// httpConn.setRequestMethod("POST");

							// httpConn.connect();
							// PrintWriter pw = new PrintWriter(new
							// BufferedOutputStream(httpConn.getOutputStream()));
							// pw.write("hl=zh-CN&");
							// pw.write("ie=UTF-8&");
							// pw.write("langpair=en%7Czh-CN&");
							// pw.write("sl=en");
							// pw.write("hl=zh-CN");
							// pw.write("text=" + "please");

							InputStreamReader isr = new InputStreamReader(httpConn.getInputStream(), "utf-8");
							BufferedReader br = new BufferedReader(isr);

							String str;
							StringBuilder content = new StringBuilder();
							while ((str = br.readLine()) != null)
							{
								content.append(str + "\n");
							}
							Pattern r = Pattern.compile("\\[\".*?,,,[0-9]\\]");
							Matcher m = r.matcher(content);
							StringBuilder content1 = new StringBuilder();
							while (m.find())
							{
								content1.append(m.group());
								// System.out.println(m.group());
							}
							r = Pattern.compile("\\[\".*?\"");
							m = r.matcher(content1);
							StringBuilder content2 = new StringBuilder();
							while (m.find())
							{
								String temp = m.group().replaceAll("\\[\"|\"", "");
								temp = StringEscapeUtils.unescapeJava(temp);
								content2.append(temp);
								// System.out.println(temp);
							}
							// System.out.println(content2 + "\n");
							trans.setText(content2.toString());
						} catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}).start();
			}
		});
	}

	/*
	 * 处理google加密码
	 */
	private String tk(String val) throws Exception {
		String script = "function tk(a) {"
				+ "var TKK = ((function() {var a = 561666268;var b = 1526272306;return 406398 + '.' + (a + b); })());\n"
				+ "function b(a, b) { for (var d = 0; d < b.length - 2; d += 3) { var c = b.charAt(d + 2), c = 'a' <= c ? c.charCodeAt(0) - 87 : Number(c), c = '+' == b.charAt(d + 1) ? a >>> c : a << c; a = '+' == b.charAt(d) ? a + c & 4294967295 : a ^ c } return a }\n"
				+ "for (var e = TKK.split('.'), h = Number(e[0]) || 0, g = [], d = 0, f = 0; f < a.length; f++) {"
				+ "var c = a.charCodeAt(f);"
				+ "128 > c ? g[d++] = c : (2048 > c ? g[d++] = c >> 6 | 192 : (55296 == (c & 64512) && f + 1 < a.length && 56320 == (a.charCodeAt(f + 1) & 64512) ? (c = 65536 + ((c & 1023) << 10) + (a.charCodeAt(++f) & 1023), g[d++] = c >> 18 | 240, g[d++] = c >> 12 & 63 | 128) : g[d++] = c >> 12 | 224, g[d++] = c >> 6 & 63 | 128), g[d++] = c & 63 | 128)"
				+ "}" + "a = h;" + "for (d = 0; d < g.length; d++) a += g[d], a = b(a, '+-a^+6');"
				+ "a = b(a, '+-3^+b+-f');" + "a ^= Number(e[1]) || 0;" + "0 > a && (a = (a & 2147483647) + 2147483648);"
				+ "a %= 1E6;" + "return a.toString() + '.' + (a ^ h)\n" + "}";

		ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
		engine.eval(script);
		Invocable inv = (Invocable) engine;
		return (String) inv.invokeFunction("tk", val);
	}
}
