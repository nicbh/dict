import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.border.TitledBorder;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class WordCard extends JFrame {
	private JPanel jp = null;
	private JLabel jlb1 = null;
	private JLabel jlb2 = null;
	private JTextArea jta = null;
	private JTextField jtf,jtf2;
	private JButton jbt1,jbt2,jSavebt,jSendbt;
	private TitledBorder jtb = new TitledBorder("�ڴ˴��������");
	private String path = null;
	float alpha = 1.0f;
	String font = "SansSerif";
	int fontStyle = Font.PLAIN;
	int fontSize = 15;
	Color color = Color.BLUE;
	BufferedImage image = null;
	
	WordCard(String word){
		this.setSize(600,450);
		this.setResizable(false);
		this.setTitle("Dict");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		
		path = "d://temp/init.jpg";
		
		try{
			
			image = ImageIO.read(new File(path));
		}catch(IOException e){
			e.printStackTrace();
		}
		
		ImageIcon imageicon = new ImageIcon(image);
		
		jp = new JPanel(new GridLayout(6,1,5,5));
		{
			jlb2 = new JLabel();
			jlb2.setText("\u6253\u5f00");
		}
		{
			jtf = new JTextField("d://temp/");
			jtf.setSize(100, 20);
		}
		{
			jbt2 = new JButton("����ͼƬ");
		}
		{
			jta = new JTextArea();
			jta.setBorder(jtb);
		}
		{
			jbt1 = new JButton("�������");
		}
		{
			jlb1 = new JLabel();
			jlb1.setIcon(imageicon);
		}
		{
			jtf2 = new JTextField(20);
			jtf2.setText("d://temp/"+word+".jpg");
		}
		{
			jSavebt = new JButton("����ͼƬ");
			
		}
		{
			jSendbt = new JButton("����ͼƬ");
		}
		JPanel jpl1 = new JPanel();
		jpl1.add(jlb2);
		jpl1.add(jtf);
		jpl1.add(jbt2);
		
		JPanel jpl2 = new JPanel();
		jpl2.add(jtf2);
		jpl2.add(jSavebt);
		
		
		jp.add(jpl1);
		jp.add(jta);
		jp.add(jbt1);
		jp.add(jpl2);
		jp.add(jSendbt);
		
		this.add(jlb1,BorderLayout.WEST);
		this.add(jp, BorderLayout.CENTER);
	
	
		
		jbt1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String str = jta.getText();
				addWords(image, alpha, font, fontStyle, fontSize, color, str, 20, 250);
					//BufferedImage temp = ImageIO.read(new File(path));
				ImageIcon imageIcon = new ImageIcon(image);
				jlb1.setIcon(imageIcon);
								
			}
		});
		jbt2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
			//System.out.println(e.getActionCommand());
			   JFileChooser fc=new JFileChooser("d://temp/");
			   int returnval = fc.showOpenDialog(null);
			    if(returnval == JFileChooser.APPROVE_OPTION){
			       File f = fc.getSelectedFile();
			       String fileName = f.getName();
			       String filePath=fc.getSelectedFile().getAbsolutePath();
			       jtf.setText(filePath);
			       BufferedImage insert = null;
			       try{
			    	   insert = ImageIO.read(new File(filePath));
			       }catch(IOException a){
			    	   a.printStackTrace();
			       }
			       insert = resizeImage(insert,image.getWidth()-20,image.getHeight()-200);
			       mergeImage(image,insert);
			       ImageIcon imageIcon = new ImageIcon(image);
			       jlb1.setIcon(imageIcon);
			    }
			}
			
		});
	
		jSavebt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String des = jtf2.getText();
				save(des);
			}
		});
	}
	
	/*public void actionPerformed(ActionEvent e) {
		Syst
		if(e.getActionCommand() == "����ͼƬ"){
			 JFileChooser fc=new JFileChooser("d://temp/");
			 int returnval = fc.showOpenDialog(null);
			 if(returnval == JFileChooser.APPROVE_OPTION){
			    File f = fc.getSelectedFile();
			    String fileName = f.getName();
			    String filePath=fc.getSelectedFile().getAbsolutePath();
			    jtf.setText(filePath);
			    BufferedImage insert = null;
			    try{
			    	insert = ImageIO.read(new File(filePath));
			    }catch(IOException a){
			    	a.printStackTrace();
			    }
			    insert = resizeImage(insert,image.getWidth()-20,image.getHeight()-200);
			    mergeImage(image,insert);
			    ImageIcon imageIcon = new ImageIcon(image);
			    jlb1.setIcon(imageIcon);
			}
		}else if(e.getActionCommand() == "�������"){
			String str = jta.getText();
			addWords(image, alpha, font, fontStyle, fontSize, color, str, 20, 250);
				//BufferedImage temp = ImageIO.read(new File(path));
			ImageIcon imageIcon = new ImageIcon(image);
			jlb1.setIcon(imageIcon);
		}
	}
	*/
	public BufferedImage resizeImage(BufferedImage image1,int width,int height){
		BufferedImage tag = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		Graphics g = tag.getGraphics();
		g.drawImage(image1, 0, 0, width, height, null);
		g.dispose();
		return  tag;
	}
	
	public void mergeImage(BufferedImage image1,BufferedImage image2){
		Graphics g = image1.getGraphics();
		g.drawImage(image2, image1.getWidth()/2 - image2.getWidth()/2, 5, null);
		g.dispose();
	}
	
	public void addWords(BufferedImage src,float alpha,String font,int fontStyle,int fontSize,Color color,String words,int x,int y){
		Graphics2D g2d = src.createGraphics();
		g2d.drawImage(src, 0, 0, src.getWidth(),src.getHeight(),null);
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);  
	    g2d.setComposite(ac);  
	        //���������������ơ���ʽ����С   
	    g2d.setFont(new Font(font, fontStyle, fontSize));  
	    g2d.setColor(color);//����������ɫ   
	   // g2d.drawString(words, x, y); //����ˮӡ���ּ�����ʼx��y����   
	    int no=-1;
	    int count = (image.getWidth()/fontSize)*2;
	    System.out.println(image.getWidth());
	    int len = words.length();
	    int margin=(image.getWidth()%fontSize)/2;
	    for(int i=0;i<(len/count);i++){  
	    	String Midtext=words.substring(0+(count*i), count+(count*i));  
	    	g2d.drawString(Midtext, margin,y+(fontSize-2)+(i*fontSize));  
	    	no++;  
	    }
	    if(len%count!=0){  
	    	String Midtext=words.substring(count*(no+1), words.length());  
	    	//int finalmargin=(x-((len-count*(no+1))*fontSize))/2;  
	    	g2d.drawString(Midtext, margin,y+(fontSize-2)+((no+1)*fontSize));   
	    } 
	    g2d.dispose();  
	       
	} 
	
	public void save(String path){
		try{
			OutputStream os = new FileOutputStream(path);
			ImageIO.write(image,"jpg", os);
		}catch(IOException e){
			e.printStackTrace();
		}
		this.dispose();
	}
	
	public void send(){
		
	}
	
}
