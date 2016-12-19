
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.*;

import javax.imageio.*;
import javax.swing.border.TitledBorder;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class WordCard extends JFrame {
	private JPanel jp = null;
	private JLabel jlb1 = null;
	private JLabel jlb2 = null;
	private JTextArea jta = null;
	private JTextField jtf, jtf2;
	private JButton jbt1, jbt2, jSavebt, jCancelbt;
	private TitledBorder jtb = new TitledBorder("在此处添加文字");
	private String path = null;
	private float alpha = 1.0f;
	private String font = "SansSerif";
	private int fontStyle = Font.PLAIN;
	private int fontSize = 15;
	private Color color = Color.BLUE;
	private BufferedImage image = null;
	private Box box = null;
	private BufferedImage wordImage = null;
	public static final String pdir = System.getProperty("user.dir");

	WordCard(String word, BufferedImage wordimage) {
//		System.out.println(pdir);
		this.setSize(600, 450);
		this.setResizable(false);
		this.setTitle("Dict");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);

		path = "init.jpg";

		try
		{

			image = ImageIO.read(new File(path));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		wordImage = resizeImage(wordimage, image.getWidth() - 15, image.getHeight() - 290);
		addWordImage(wordImage);
		ImageIcon imageicon = new ImageIcon(image);

		// jp = new JPanel(new GridLayout(6,1,5,5));
		{
			box = Box.createVerticalBox();
		}
		{
			jlb2 = new JLabel();
			jlb2.setText("\u6253\u5f00");
		}
		{
			jtf = new JTextField(15);
			jtf.setText(pdir);
			// jtf.setSize(100, 20);
		}
		{
			jbt2 = new JButton("插入图片");
		}
		{
			jta = new JTextArea();
			jta.setBorder(jtb);
			jta.setPreferredSize(new Dimension(90, 30));
		}
		{
			jbt1 = new JButton("添加文字");
		}
		{
			jlb1 = new JLabel();
			jlb1.setIcon(imageicon);
		}
		{
			jtf2 = new JTextField(20);
			jtf2.setText(word + ".jpg");
		}
		{
			jSavebt = new JButton("保存图片");

		}
		{
			jCancelbt = new JButton("取消修改");
		}

		JPanel jpl1 = new JPanel();
		jpl1.add(jlb2);
		jpl1.add(jtf);
		jpl1.add(jbt2);

		JPanel jpl2 = new JPanel();
		jpl2.add(jtf2);
		jpl2.add(jSavebt);

		Box b1 = Box.createHorizontalBox();
		b1.add(jbt1);
		b1.add(jCancelbt);

		box.add(jpl1);
		box.add(jpl2);
		box.add(jta);
		box.add(b1);

		// jp.add(jSendbt);

		this.add(jlb1, BorderLayout.WEST);
		this.add(box, BorderLayout.CENTER);

		jbt1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = jta.getText();
				addWords(image, alpha, font, fontStyle, fontSize, color, str, 20, 230);
				// BufferedImage temp = ImageIO.read(new File(path));
				ImageIcon imageIcon = new ImageIcon(image);
				jlb1.setIcon(imageIcon);

			}
		});
		jbt2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// System.out.println(e.getActionCommand());
				JFileChooser fc = new JFileChooser(pdir);
				int returnval = fc.showOpenDialog(null);
				if (returnval == JFileChooser.APPROVE_OPTION)
				{
					File f = fc.getSelectedFile();
					String fileName = f.getName();
					String filePath = fc.getSelectedFile().getAbsolutePath();
					jtf.setText(filePath);
					BufferedImage insert = null;
					try
					{
						insert = ImageIO.read(new File(filePath));
					} catch (IOException a)
					{
						a.printStackTrace();
					}
					insert = resizeImage(insert, image.getWidth() - 20, image.getHeight() - 200);
					mergeImage(image, insert);
					ImageIcon imageIcon = new ImageIcon(image);
					jlb1.setIcon(imageIcon);
				}
			}

		});

		jSavebt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String des = jtf2.getText();
				save(des);
			}
		});

		jCancelbt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try
				{

					image = ImageIO.read(new File(path));
				} catch (IOException e1)
				{
					e1.printStackTrace();

				}
				addWordImage(wordimage);
				ImageIcon imageIcon = new ImageIcon(image);
				jlb1.setIcon(imageIcon);
			}

		});
	}

	/*
	 * public void actionPerformed(ActionEvent e) { Syst if(e.getActionCommand()
	 * == "插入图片"){ JFileChooser fc=new JFileChooser("d://temp/"); int returnval
	 * = fc.showOpenDialog(null); if(returnval == JFileChooser.APPROVE_OPTION){
	 * File f = fc.getSelectedFile(); String fileName = f.getName(); String
	 * filePath=fc.getSelectedFile().getAbsolutePath(); jtf.setText(filePath);
	 * BufferedImage insert = null; try{ insert = ImageIO.read(new
	 * File(filePath)); }catch(IOException a){ a.printStackTrace(); } insert =
	 * resizeImage(insert,image.getWidth()-20,image.getHeight()-200);
	 * mergeImage(image,insert); ImageIcon imageIcon = new ImageIcon(image);
	 * jlb1.setIcon(imageIcon); } }else if(e.getActionCommand() == "添加文字"){
	 * String str = jta.getText(); addWords(image, alpha, font, fontStyle,
	 * fontSize, color, str, 20, 250); //BufferedImage temp = ImageIO.read(new
	 * File(path)); ImageIcon imageIcon = new ImageIcon(image);
	 * jlb1.setIcon(imageIcon); } }
	 */
	public BufferedImage resizeImage(BufferedImage image1, int width, int height) {
//		System.out.println(image1.getWidth() + "+" + image1.getHeight());
		BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = tag.getGraphics();
		g.drawImage(image1, 0, 0, width, height, null);
//		System.out.println(tag.getWidth() + "+" + tag.getHeight());
		g.dispose();
		return tag;
	}

	public void addWordImage(BufferedImage image1) {
		if (image1 == null)
			return;
		Graphics g = image.getGraphics();
		g.drawImage(image1, image.getWidth() / 2 - image1.getWidth() / 2, 250, null);
		g.dispose();
	}

	public void mergeImage(BufferedImage image1, BufferedImage image2) {
		Graphics g = image1.getGraphics();
		g.drawImage(image2, image1.getWidth() / 2 - image2.getWidth() / 2, 5, null);
		g.dispose();
	}

	public void addWords(BufferedImage src, float alpha, String font, int fontStyle, int fontSize, Color color,
			String words, int x, int y) {
		Graphics2D g2d = src.createGraphics();
		g2d.drawImage(src, 0, 0, src.getWidth(), src.getHeight(), null);
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		g2d.setComposite(ac);
		// 设置文字字体名称、样式、大小
		g2d.setFont(new Font(font, fontStyle, fontSize));
		g2d.setColor(color);// 设置字体颜色
		// g2d.drawString(words, x, y); //输入水印文字及其起始x、y坐标
		int no = -1;
		int count = (image.getWidth() / fontSize) * 2;
//		System.out.println(image.getWidth());
		int len = words.length();
		int margin = (image.getWidth() % fontSize) / 2;
		for (int i = 0; i < (len / count); i++)
		{
			String Midtext = words.substring(0 + (count * i), count + (count * i));
			g2d.drawString(Midtext, margin, y + (fontSize - 2) + (i * fontSize));
			no++;
		}
		if (len % count != 0)
		{
			String Midtext = words.substring(count * (no + 1), words.length());
			// int finalmargin=(x-((len-count*(no+1))*fontSize))/2;
			g2d.drawString(Midtext, margin, y + (fontSize - 2) + ((no + 1) * fontSize));
		}
		g2d.dispose();

	}

	public void save(String path) {
		try
		{
			OutputStream os = new FileOutputStream(path);
			ImageIO.write(image, "jpg", os);
			os.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		this.dispose();
	}

	public void send() {

	}

}
