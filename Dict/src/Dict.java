
import javax.swing.*;
import javax.swing.event.*;

import gui.ava.html.image.generator.HtmlImageGenerator;

import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.*;

public class Dict extends JFrame {
	private Dict dict;
	private JTabbedPane pane = new JTabbedPane(JTabbedPane.TOP);
	private int index = 0;
	private JTextField dic = new JTextField(30);
	private JLabel hint = new JLabel(" ");
	private JCheckBox bing = new JCheckBox("必应", true);
	private JCheckBox youdao = new JCheckBox("有道", true);
	private JCheckBox jinshan = new JCheckBox("金山", true);
	private JEditorPane[] text = new JEditorPane[3];
	// private JScrollPane[] jsp = new JScrollPane[3];
	private TitledBorder[] border = new TitledBorder[3];
	private String[] dicts = { "必应", "有道", "金山" };
	private int[] order = { 0, 1, 2 };
	private JScrollPane[] jsp = new JScrollPane[3];
	private loginPanel lPanel;

	public static int[] likes = { 0, 0, 0 }; // like count
	private boolean[] like = { false, false, false }; // like flag
	private String[] content = new String[3];
	private boolean[] likeable = { false, false, false };

	private final String full = "\u2665";// "\u2764";
	private final String empty = "\u2661";

	private boolean isbing = true;
	private boolean isyoudao = true;
	private boolean isjinshan = true;

	private BufferedImage image = null;

	Dict() {
		super();
		lPanel = new loginPanel(this);
		Font font = new Font("TimesRoman", Font.BOLD, 20);
		Font font1 = new Font("TimesRoman", Font.BOLD, 15);
		Font font2 = new Font("Dialog", Font.BOLD, 15);
		Font font3 = new Font("Dialog", Font.BOLD, 12);
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
		bing.setFont(font3);
		youdao.setFont(font3);
		jinshan.setFont(font3);
		box01.add(Box.createHorizontalGlue());
		box01.add(bing);
		box01.add(youdao);
		box01.add(jinshan);
		box01.add(Box.createHorizontalGlue());

		border[0] = new TitledBorder(dicts[order[0]]);
		border[0].setTitleFont(font1);
		text[0] = new JEditorPane("text/html", "");
		text[0].setBorder(border[0]);
		text[0].setEditable(false);
		text[0].setOpaque(false);
		jsp[0] = new JScrollPane(text[0]);
		jsp[0].setOpaque(false);
		jsp[0].getViewport().setOpaque(false);
		jsp[0].setPreferredSize(new Dimension(500, 200));
		// text[0].setSize(40,5);
		border[1] = new TitledBorder(dicts[order[1]]);
		border[1].setTitleFont(font1);
		text[1] = new JEditorPane("text/html", "");
		text[1].setBorder(border[1]);
		text[1].setEditable(false);
		text[1].setOpaque(false);
		jsp[1] = new JScrollPane(text[1]);
		jsp[1].setPreferredSize(new Dimension(500, 200));
		// text[1].setSize(40,5);
		border[2] = new TitledBorder(dicts[order[2]]);
		border[2].setTitleFont(font1);
		text[2] = new JEditorPane("text/html", "");
		text[2].setBorder(border[2]);
		text[2].setEditable(false);
		text[2].setOpaque(false);
		jsp[2] = new JScrollPane(text[2]);
		jsp[2].setPreferredSize(new Dimension(500, 200));
		// text[2].setSize(40,5);
		Box box02 = Box.createVerticalBox();
		box02.add(jsp[0]);
		box02.add(jsp[1]);
		box02.add(jsp[2]);

		Box box0 = Box.createVerticalBox();
		box0.add(box00);
		hint.setFont(font3);
		hint.setForeground(Color.red);
		box0.add(hint);
		box0.add(box01);
		box0.add(box02);
		panel0.add(box0);
		pane.addTab("", null, panel0, null);
		JLabel chachi = new JLabel("   查词   ");
		chachi.setFont(font);
		pane.setTabComponentAt(0, chachi);

		JPanel panel1 = new transPanel();
		pane.addTab("", null, panel1, null);
		JLabel fanyi = new JLabel("   翻译   ");
		fanyi.setFont(font);
		pane.setTabComponentAt(1, fanyi);

		pane.addTab("", null, lPanel, null);
		JLabel faxian = new JLabel("   发现   ");
		faxian.setFont(font);
		pane.setTabComponentAt(2, faxian);
		add(pane, BorderLayout.CENTER);

		pane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				int sindex = pane.getSelectedIndex();
				System.out.println(sindex);
				if (sindex == 2)
				{
					if (!lPanel.login)
					{
						pane.setSelectedIndex(index);
						dict.setEnabled(false);
					}
					lPanel.click();
					if (lPanel.login)
					{
						pane.setSelectedIndex(sindex);
					}
				} else
					index = sindex;
			}
		});
		// 给jbt添加监听器
		bsearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				getDict();
			}
		});
		dic.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				getDict();
			}
		});

		bing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isbing = !isbing;
				likeable[0] = isbing;
			}
		});
		youdao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isyoudao = !isyoudao;
				likeable[1] = isyoudao;
			}
		});
		jinshan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isjinshan = !isjinshan;
				likeable[2] = isjinshan;
			}
		});
		text[0].addMouseListener(new mouse(0));
		text[1].addMouseListener(new mouse(1));
		text[2].addMouseListener(new mouse(2));
	}

	private class mouse extends MouseAdapter {
		private int index;

		mouse(int i) {
			index = i;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			int type = e.getButton();// 得到按下的鼠标键
			if (type == MouseEvent.BUTTON1)// 判断是鼠标左键按下
			{
				int idx = order[index];
				if (likeable[idx])
				{
					// boolean swap = false;
					like[idx] = !like[idx];
					if (like[idx] == true)
					{
						likes[idx]++;
						if (!lPanel.client.like())
						{
							likes[idx]--;
							like[idx] = !like[idx];
						}
						// swap = putOrder();
						// idx = order[index];
					} else
					{
						likes[idx]--;
						if (!lPanel.client.like())
						{
							likes[idx]++;
							like[idx] = !like[idx];
						}
						// swap = putOrder();
						// idx = order[index];
					}

					String title = dicts[idx];
					if (like[idx])
					{
						title = title + full;
					} else
					{
						title = title + empty;
					}
					border[index].setTitle(title);
					text[index].setText(text[index].getText());
					// if (swap)
					// {
					// for (int i = 0; i < order.length; i++)
					// {
					// int ii = order[i];
					// title = dicts[ii];
					// if (likeable[ii])
					// {
					// if (like[ii])
					// {
					// title = title + full;
					// } else
					// {
					// title = title + empty;
					// }
					// }
					// border[i].setTitle(title);
					// text[i].setText(content[ii]);
					// }
					// }
					System.out.printf("%d %d %d\n", likes[0], likes[1], likes[2]);
				}
			} else if (type == MouseEvent.BUTTON3)
			{// 判断是鼠标右键按下
				if (likeable[index])
				{
					if (content[index] != "载入中..." && content[index] != "查无此词" && content[index] != "连接不到在线词典")
					{
						HtmlImageGenerator ig = new HtmlImageGenerator();
						ig.loadHtml("<h2><strong>" + dic.getText().trim() + "      ————" + dicts[order[index]]
								+ "词典</strong></h2>" + content[index]);
						image = ig.getBufferedImage();
						// "<h2><strong>" + "apple" + "<></h2>" + "<p>有道</p>"

					}
					new WordCard(dic.getText().trim(), image);
				}
			}
		}
	}

	void getDict() {
		String str = dic.getText().trim();
		if (str.length() == 0 || str.matches("\\W|\\d|_"))
			hint.setText("请输入正确的格式：字母或汉字");
		else
		{
			try
			{
				str = URLEncoder.encode(str, "UTF-8");
			} catch (Exception ex)
			{
				ex.printStackTrace();
			}
			hint.setText(" ");
			putOrder();
			int bing = 0, youdao = 0, jinshan = 0;
			for (int i = 0; i < order.length; i++)
			{
				if (order[i] == 0)
					bing = i;
				if (order[i] == 1)
					youdao = i;
				if (order[i] == 2)
					jinshan = i;
				like[i] = false;
			}
			if (isbing)
			{
				likeable[0] = true;
				border[bing].setTitle(dicts[0] + empty);
				content[0] = "载入中...";
				text[bing].setText(content[0]);
				new getPage("http://cn.bing.com/dict/search?q=" + str, 0, text[bing], content).execute();
				/*
				 * HtmlImageGenerator ig = new HtmlImageGenerator();
				 * ig.loadHtml(content[0]); image = ig.getBufferedImage();
				 */
			} else
			{
				likeable[0] = false;
				border[bing].setTitle(dicts[0]);
				content[0] = "未选择复选框";
				text[bing].setText(content[0]);
			}
			if (isyoudao)
			{
				likeable[1] = true;
				border[youdao].setTitle(dicts[1] + empty);
				content[1] = "载入中...";
				text[youdao].setText(content[1]);
				new getPage("http://youdao.com/w/" + str + "/#keyfrom=dict2.index", 1, text[youdao], content).execute();
				/*
				 * HtmlImageGenerator ig = new HtmlImageGenerator();
				 * ig.loadHtml(content[1]); image = ig.getBufferedImage();
				 */
			} else
			{
				likeable[1] = false;
				border[youdao].setTitle(dicts[1]);
				content[1] = "未选择复选框";
				text[youdao].setText(content[1]);
			}
			if (isjinshan)
			{
				likeable[2] = true;
				border[jinshan].setTitle(dicts[2] + empty);
				content[1] = "载入中...";
				text[jinshan].setText(content[1]);
				new getPage("http://www.iciba.com/" + str, 2, text[jinshan], content).execute();
				/*
				 * HtmlImageGenerator ig = new HtmlImageGenerator();
				 * ig.loadHtml(content[2]); image = ig.getBufferedImage();
				 */
			} else
			{
				likeable[2] = false;
				border[jinshan].setTitle(dicts[2]);
				content[2] = "未选择复选框";
				text[jinshan].setText(content[2]);
			}
		}
	}

	public boolean putOrder() {
		int[] likess = likes.clone();
		int[] orders = { 0, 1, 2 };
		for (int i = 0; i < likess.length; i++)
		{
			for (int j = 0; j < likess.length - 1; j++)
			{
				if (likess[j] < likess[j + 1])
				{
					int t = likess[j];
					likess[j] = likess[j + 1];
					likess[j + 1] = t;
					t = orders[j];
					orders[j] = orders[j + 1];
					orders[j + 1] = t;
				}
			}
		}
		boolean swap = !order.equals(orders);
		order = orders.clone();
		return swap;
	}

	public void setWindow(Dict dic) {
		dict = dic;
	}

	public static void main(String[] args) {
		Dict frame = new Dict();
		frame.setWindow(frame);
		frame.setSize(700, 800);
		frame.setResizable(false);
		frame.setTitle("Dict");
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
