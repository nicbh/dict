
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
//jg
public class Dict extends JFrame {
	private JTabbedPane pane = new JTabbedPane(JTabbedPane.TOP);
	private JTextField dic = new JTextField(30);
	private JCheckBox baidu = new JCheckBox("百度");
	private JCheckBox youdao = new JCheckBox("有道");
	private JCheckBox jinshan = new JCheckBox("金山");
	private JTextArea[] text = new JTextArea[3];
	private TitledBorder[] border = new TitledBorder[3];
	private String[] dicts = {"百度","有道","金山"};

	Dict() {
		super();
		JPanel panel0 = new JPanel();
		JLabel input = new JLabel("请输入单词");
		JButton bsearch = new JButton("search");
		Box box00=Box.createHorizontalBox();
		box00.add(input);
		box00.add(dic);
		box00.add(bsearch);
		Box box01=Box.createHorizontalBox();
		box01.add(baidu);
		box01.add(youdao);
		box01.add(jinshan);
		
		border[0]=new TitledBorder(dicts[0]);
		text[0]=new JTextArea();
		text[0].setBorder(border[0]);
		text[0].setEditable(false);
		border[1]=new TitledBorder(dicts[1]);
		text[1]=new JTextArea();
		text[1].setBorder(border[1]);
		text[1].setEditable(false);
		border[2]=new TitledBorder(dicts[2]);
		text[2]=new JTextArea();
		text[2].setBorder(border[2]);
		text[2].setEditable(false);
		Box box02=Box.createVerticalBox();
		box02.add(text[0]);
		box02.add(text[1]);
		box02.add(text[2]);
		
		Box box0=Box.createVerticalBox();
		box0.add(box00);
		box0.add(box01);
		box0.add(box02);
		panel0.add(box0);
		pane.addTab("", null, panel0, null);
		JLabel chachi = new JLabel("   查词   ");
		chachi.setFont(new Font("TimesRoman",Font.BOLD,20));
		pane.setTabComponentAt(0, chachi);
		
		JPanel panel1 = new JPanel();
		pane.addTab("", null, panel1, null);
		JLabel fanyi = new JLabel("   翻译   ");
		fanyi.setFont(new Font("TimesRoman",Font.BOLD,20));
		pane.setTabComponentAt(1, fanyi);
		
		loginPanel panel2=new loginPanel();
		pane.addTab("", null, panel2, null);
		JLabel faxian = new JLabel("   发现   ");
		faxian.setFont(new Font("TimesRoman",Font.BOLD,20));
		pane.setTabComponentAt(2, faxian);
		add(pane,BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		JFrame frame = new Dict();
		frame.setSize(500, 500);
		frame.setResizable(false);
		frame.setTitle("Dict");
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
