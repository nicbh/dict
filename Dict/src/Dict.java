
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class Dict extends JFrame {
	private JTabbedPane pane = new JTabbedPane(JTabbedPane.TOP);
	private JTextField dic = new JTextField("sdvsdvgykgkdsvsdrrgdrghddvsdv");
	private JCheckBox baidu = new JCheckBox("�ٶ�");
	private JCheckBox youdao = new JCheckBox("�е�");
	private JCheckBox jinshan = new JCheckBox("��ɽ");
	private JTextArea[] text = new JTextArea[3];
	private TitledBorder[] border = new TitledBorder[3];

	Dict() {
		super();
		JPanel panel0 = new JPanel();
		JLabel input = new JLabel("�����뵥��");
		JButton bsearch = new JButton("search");
		panel0.add(input);
		panel0.add(dic);
		panel0.add(bsearch);
		JPanel panel00=new JPanel();
		panel00.add(baidu);
		panel00.add(youdao);
		panel00.add(jinshan);
		panel0.add(panel00);
		
		border[0]=new TitledBorder("�ٶ�");
		text[0]=new JTextArea("asfafasfassdvsdvgykgkdsdrhdrhdrhdghdhvsdrrgdrghddvsdvfasf");
		text[0].setBorder(border[0]);
		border[1]=new TitledBorder("�е�");
		text[1]=new JTextArea("sdfsdvsdvssdvsdvgykgkdsvsdrrgdrghddvsdvdfsdvsd");
		text[1].setBorder(border[1]);
		border[2]=new TitledBorder("��ɽ");
		text[2]=new JTextArea("afafafafafasdvsdvgykgkdsv\nsdrrgdrghddvsdvfafaffe");
		text[2].setBorder(border[2]);
		panel0.add(text[0]);
		panel0.add(text[1]);
		panel0.add(text[2]);
		
		
		JPanel panel1 = new JPanel();
		pane.addTab("", null, panel0, null);
		JLabel chachi = new JLabel("   ���   ");
		chachi.setFont(new Font("TimesRoman",Font.BOLD,20));
		pane.setTabComponentAt(0, chachi);
		pane.addTab("", null, panel1, null);
		JLabel fanyi = new JLabel("   ����   ");
		fanyi.setFont(new Font("TimesRoman",Font.BOLD,20));
		pane.setTabComponentAt(1, fanyi);
		

		JPanel panel2 = new JPanel();
		pane.addTab("", null, panel2, null);
		JLabel faxian = new JLabel("   ����   ");
		faxian.setFont(new Font("TimesRoman",Font.BOLD,20));
		pane.setTabComponentAt(2, faxian);
		add(pane);
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
