
import java.io.*;
import java.util.regex.*;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	private JLabel hint = new JLabel(" ");
	private JCheckBox bing = new JCheckBox("必应", true);
	private JCheckBox youdao = new JCheckBox("有道", true);
	private JCheckBox jinshan = new JCheckBox("金山", true);
	private JEditorPane[] text = new JEditorPane[3];
	// private JScrollPane[] jsp = new JScrollPane[3];
	private TitledBorder[] border = new TitledBorder[3];
	private String[] dicts = { "必应", "有道", "金山" };
	private int[] order = { 2, 0, 1 };
	private JScrollPane[] jsp = new JScrollPane[3];

	private boolean[] is = new boolean[3];
	private boolean isbing = true;
	private boolean isyoudao = true;
	private boolean isjinshan = true;

	Dict() {
		super();
		is[0] = true;
		is[1] = true;
		is[2] = true;
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
		box01.add(bing);
		box01.add(youdao);
		box01.add(jinshan);

		border[0] = new TitledBorder(dicts[order[0]]);
		border[0].setTitleFont(font1);
		text[0] = new JEditorPane("text/html", "");
		text[0].setBorder(border[0]);
		text[0].setEditable(false);
		jsp[0] = new JScrollPane(text[0]);
		jsp[0].setPreferredSize(new Dimension(500, 200));
		// text[0].setSize(40,5);
		border[1] = new TitledBorder(dicts[order[1]]);
		border[1].setTitleFont(font1);
		text[1] = new JEditorPane("text/html", "");
		text[1].setBorder(border[1]);
		text[1].setEditable(false);
		jsp[1] = new JScrollPane(text[1]);
		jsp[1].setPreferredSize(new Dimension(500, 200));
		// text[1].setSize(40,5);
		border[2] = new TitledBorder(dicts[order[2]]);
		border[2].setTitleFont(font1);
		text[2] = new JEditorPane("text/html", "");
		text[2].setBorder(border[2]);
		text[2].setEditable(false);
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

	void getDict() {
		String str = dic.getText().trim();
		// if (!str.matches("[1-9a-zA-Z,.;'\":<>{}].*"))
		// hint.setText("wrong");
		// else
		{
			hint.setText(" ");
			int bing = 0, youdao = 0, jinshan = 0;
			for (int i = 0; i < order.length; i++)
			{
				if (order[i] == 0)
					bing = i;
				if (order[i] == 1)
					youdao = i;
				if (order[i] == 2)
					jinshan = i;
			}
			if (isbing)
			{
				text[bing].setText("载入中...");
				new getPage("http://cn.bing.com/dict/search?q=" + str, 0, text[bing]).execute();
			}
			if (isyoudao)
			{
				text[youdao].setText("载入中...");
				new getPage("http://youdao.com/w/" + str + "/#keyfrom=dict2.index", 1, text[youdao]).execute();
			}
			if (isjinshan)
			{
				text[jinshan].setText("载入中...");
				new getPage("http://www.iciba.com/" + str, 2, text[jinshan]).execute();
			}
		}
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

}
