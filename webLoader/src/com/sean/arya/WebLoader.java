package com.sean.arya;

import java.awt.BorderLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 网站全站下载UI
 * @author Sean
 * @since 2017-07-01
 */
public class WebLoader extends JFrame{
	private static final long serialVersionUID = 1L; 
	//Labels
	private JLabel baseLabel=new JLabel("Base  Site ");
	private JLabel pageLabel=new JLabel("Start Page");
	private JLabel patternLabel=new JLabel("Pattern      ");
	private JLabel storgeLabel=new JLabel("Storge       ");
	//Fields
	private JTextField base=new JTextField();
	private JTextField page=new JTextField();
	private JTextField pattern=new JTextField();
	private JTextField storge=new JTextField();
	//Buttons
	private JButton storgeButton=new JButton("Choose");
	private JButton startButton=new JButton("Start Download");
	public String getBaseText() {
		return baseText;
	}
	public String getPageText() {
		return pageText;
	}
	public String getPatternText() {
		return patternText;
	}
	public String getStorgeText() {
		return storgeText;
	}
	private String baseText=null;
	private String pageText=null;
	private String patternText=null;
	private String storgeText=null;
	public WebLoader(){
		//布局
		setTitle("WebLoader");
		//Panel 存放组件组合
		JPanel basePanel=new JPanel(new BorderLayout());//baseLabel+base
		JPanel pagePanel=new JPanel(new BorderLayout());//pageLabel+page
		JPanel patternPanel=new JPanel(new BorderLayout());//patternLabel+pattern
		JPanel storgePanel =new JPanel(new BorderLayout());//storgeLabel+storge+storgeButton
		JPanel northPanel=new JPanel(new BorderLayout());//basePanel+pagePanel
		JPanel southPanel=new JPanel(new BorderLayout());//storgePanel+startButton
		JPanel contentPanel=new JPanel(new BorderLayout());//All
		basePanel.add(baseLabel,BorderLayout.WEST);
		basePanel.add(base);
		pagePanel.add(pageLabel,BorderLayout.WEST);
		pagePanel.add(page);
		patternPanel.add(patternLabel,BorderLayout.WEST);
		patternPanel.add(pattern);
		storgePanel.add(storgeLabel,BorderLayout.WEST);
		storgePanel.add(storgeButton,BorderLayout.EAST);
		storgePanel.add(storge);
		northPanel.add(basePanel,BorderLayout.NORTH);
		northPanel.add(pagePanel);
		southPanel.add(storgePanel);
		southPanel.add(startButton,BorderLayout.SOUTH);
		contentPanel.add(northPanel,BorderLayout.NORTH);
		contentPanel.add(patternPanel);
		contentPanel.add(southPanel,BorderLayout.SOUTH);
		this.getContentPane().add(contentPanel);
		
		//监听
		storge.addFocusListener(new FocusAdapter() {

			public void focusLost(FocusEvent e) {
				File f=new File(((JTextField)e.getComponent()).getText());
				if(f.exists() && !f.isDirectory()){
					JOptionPane.showMessageDialog(WebLoader.this, "Error storge");
				}
				((JTextField)e.getComponent()).setText("");
			}
		});
		storgeButton.addMouseListener(new MouseAdapter() {
			JFileChooser choose=null;
			@Override
			public void mouseClicked(MouseEvent e) {
				choose=new JFileChooser();
				choose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int val=choose.showOpenDialog(WebLoader.this);
				if(val==JFileChooser.APPROVE_OPTION){
					storge.setText(choose.getSelectedFile().getAbsolutePath());
				}
			}
			
		});
		
		startButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				baseText=base.getText();
				pageText=page.getText();
				patternText=pattern.getText();
				storgeText=storge.getText();
				if((baseText==null || baseText.trim().equals("")) && (pageText==null || pageText.trim().equals(""))){
					JOptionPane.showMessageDialog(WebLoader.this, "Input base site and start page!");
					return;
				}
				if((baseText==null || baseText.trim().equals(""))){
					baseText=pageText;
				}
				if((pageText==null || pageText.trim().equals(""))){
					pageText=baseText;
				}
				if(patternText==null || patternText.trim().equals("")){
					patternText="";
				}
				
				if(storgeText==null || storgeText.trim().equals("")){
					JOptionPane.showMessageDialog(WebLoader.this, "Input or Choose storge directory");
					return;
				}
				new WebLoaderController(WebLoader.this).start();
			}
		});
				
		//设置
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(750, 150);
		this.setResizable(false);
		this.setVisible(true);
		this.setVisible(true);
	}
	public static void main(String[] args){
		new WebLoader();
	}

}
