package VoiceSelect;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class VoiceSelect {

	private JFrame mainFrame;
	private Container mainContentPane;
	private JPanel mainPanel,selectPanel;
	private int objWidth = 90,objHeight = 90;//目标大小
	private int frameWidth = 900,frameHeight = 900;//界面大小
	
	public VoiceSelect(){
		initFrame();
	}
	
	public void initFrame() {
		mainFrame =new JFrame("目标选择");
		mainFrame.setSize(950, 950);
		mainFrame.setLocation(10,10);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainContentPane = mainFrame.getContentPane();
		mainContentPane.setLayout(null);
		mainPanel=new JPanel();	
		mainPanel.setSize(frameWidth, frameHeight);
		mainPanel.setLocation(22, 5);
		mainPanel.setLayout(null);
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		
		mainContentPane.add(mainPanel);
		mainFrame.setVisible(true);
		mainFrame.setResizable(false);

	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new VoiceSelect();
	}

}
