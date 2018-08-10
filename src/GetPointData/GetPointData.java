package GetPointData;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.filechooser.FileSystemView;


public class GetPointData {
    
    private String areaT="",logT="",keyT="";
    
	private JFrame mainFrame;
	private Container mainContentPane;
	private JPanel mainPanel;
	private SelectPanel selectPanel;
	private int frameWidth = 900,frameHeight = 900;//区域大小
	private JButton button1,button2,button3, button4;
	private Point point = new Point(0, 0); // 坐标点
	
	private ArrayList<JButton> listButton;
	
	private int numObj = 11;//目标数量
	
	String desktopPath = null;
	String filePath = "";
	String sheetName = "";
	// Excel文件sheet页的第一行
	String title[] = { "序号" ,"Point.x" , "Point.y"};
	// Excel文件每一列对应的数据
	String titleDate[] = { "num", "pointX", "pointY"};
	Data user = new Data();
	ExcelManage em;
	
	Boolean actionBool = false;//用来防止没有生成按钮就获取数据
	
	public GetPointData(){
		setFilePath();
		initFrame();
		
	}
	
	public void setFilePath() {
		String os = System.getProperty("os.name");  
		//当前用户桌面
		File desktopDir = FileSystemView.getFileSystemView() .getHomeDirectory();		
		desktopPath = desktopDir.getAbsolutePath();
		filePath = desktopPath+"/Desktop/GetPointData.xls";
		if(os.toLowerCase().startsWith("win")){  
			filePath = desktopPath+"\\GetPointData.txt";
		}
	}
	
	public void savaAllData(int num) {

		em = new ExcelManage();
		// 判断该名称的文件是否存在
		boolean fileFlag = em.fileExist(filePath);
		if (!fileFlag) {
			em.createExcel(filePath, sheetName, title);
		}
		// 判断该名称的Sheet是否存在
		boolean sheetFlag = em.sheetExist(filePath, sheetName);
		// 如果该名称的Sheet不存在，则新建一个新的Sheet
		if (!sheetFlag) {
			try {
				em.createSheet(filePath, sheetName, title);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// 写入到excel
		em.writeToExcel(filePath, sheetName, user, titleDate, num);

	}


	
	public void setTitle() {
		mainFrame.setTitle("按钮位置获取 : "+ areaT + keyT +logT);
	}
	
	public void initFrame() {
		mainFrame =new JFrame("语音目标选择实验");
		mainFrame.setSize(1350, 950);
		mainFrame.setLocation(10,10);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainContentPane = mainFrame.getContentPane();
		mainContentPane.setLayout(null);
		
		selectPanel = new SelectPanel();
		selectPanel.setBounds(30, 10, frameWidth, frameHeight);
		selectPanel.setLayout(null);
		//selectPanel.setBorder(BorderFactory.createTitledBorder("目标选择区域"));
		selectPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 5));
		
		mainPanel=new JPanel();	
		mainPanel.setSize(300,900);
		mainPanel.setBounds(950, 10, 370, 900);
		mainPanel.setLayout(null);
		//mainPanel.setBorder(BorderFactory.createTitledBorder("菜单区域"));
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY,5));
		
		button1 = new JButton("30*30");
		button1.setBounds(30, 40, 30, 30);
		button1.setBackground(Color.WHITE);
		button1.setOpaque(true);
		button1.setBorder(new LineBorder(Color.BLACK));
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sheetName = "30乘30";
				selectPanel.removeAll();
				addButton(selectPanel,numObj,30,30);
				selectPanel.repaint(); 
				actionBool=true;//可以进行获取数据了
			}
		});
		button2 = new JButton("60*60");
		button2.setBounds(30, 110, 60, 60);
		button2.setBackground(Color.WHITE);
		button2.setOpaque(true);
		button2.setBorder(new LineBorder(Color.BLACK));
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sheetName = "60乘60";
				selectPanel.removeAll();
				addButton(selectPanel,numObj,60,60);
				selectPanel.repaint(); 
				actionBool=true;
			}
		});
		button3 = new JButton("90*90");
		button3.setBounds(30, 210, 90, 90);
		button3.setBackground(Color.WHITE);
		button3.setOpaque(true);
		button3.setBorder(new LineBorder(Color.BLACK));
		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sheetName = "90乘90";
				selectPanel.removeAll();
				addButton(selectPanel,numObj,90,90);
				selectPanel.repaint(); 
				actionBool=true;
			}
		});
		button4 = new JButton("获取");
		button4.setBounds(30, 410, 120, 70);
		button4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (actionBool) {
					for(int index = 0; index < listButton.size(); index++) {
						user.setNum(String.valueOf(index));
						user.setPointX(String.valueOf(listButton.get(index).getX()));
						user.setPointY(String.valueOf(listButton.get(index).getY()));
						savaAllData(index);
					}
				}else {
					JOptionPane.showMessageDialog(new JFrame().getContentPane(), "请在生成按钮之后再获取数据！", "系统信息", JOptionPane.QUESTION_MESSAGE); 
				}
			}
		});
		
		mainPanel.add(button1);
		mainPanel.add(button2);
		mainPanel.add(button3);
		mainPanel.add(button4);
		
		mainContentPane.add(selectPanel);
		mainContentPane.add(mainPanel);
		
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
	}
	

	public void paintNine() {
		int mX = 450;
		int mY = 459;
		int distanceOfEachLayer = frameWidth/3;
		
		Graphics graphics = selectPanel.getGraphics();

		graphics.setColor(Color.BLUE);
		graphics.fillOval(mX - distanceOfEachLayer - 2, mY - distanceOfEachLayer - 2 , 4, 4);
		graphics.fillOval(mX - 2, mY - distanceOfEachLayer - 2 , 4, 4);
		graphics.fillOval(mX + distanceOfEachLayer - 2, mY - distanceOfEachLayer- 2, 4, 4);
		graphics.fillOval(mX - distanceOfEachLayer - 2, mY - 2, 4, 4);
		graphics.fillOval(mX- 2,mY- 2, 4, 4);
		graphics.fillOval(mX + distanceOfEachLayer - 2, mY - 2, 4, 4);
		graphics.fillOval(mX - distanceOfEachLayer - 2, mY + distanceOfEachLayer- 2, 4, 4);
		graphics.fillOval(mX - 2, mY + distanceOfEachLayer- 2, 4, 4);
		graphics.fillOval(mX + distanceOfEachLayer - 2, mY + distanceOfEachLayer- 2, 4, 4);
		
		graphics.drawString("1",mX - distanceOfEachLayer , mY - distanceOfEachLayer);
		graphics.drawString("2",mX , mY - distanceOfEachLayer);
		graphics.drawString("3",mX + distanceOfEachLayer , mY - distanceOfEachLayer);
		graphics.drawString("4",mX - distanceOfEachLayer , mY );
		graphics.drawString("5",mX , mY );
		graphics.drawString("6",mX + distanceOfEachLayer , mY );
		graphics.drawString("7",mX - distanceOfEachLayer , mY + distanceOfEachLayer);
		graphics.drawString("8",mX , mY + distanceOfEachLayer);
		graphics.drawString("9",mX + distanceOfEachLayer , mY + distanceOfEachLayer);
		
	}
	

	public void addButton(JPanel myJPanel,int num,int objWidth,int objHeight) {
		listButton = new ArrayList<JButton>();
		Random random = new Random();
		Rectangle rect = new Rectangle();
		int i = 0;
		boolean boolBtn = false;
		JButton button;
		while ( i < num ) { 
			button = new JButton(String.valueOf(i));
			int x = random.nextInt(frameWidth - objWidth );
			int y = random.nextInt(frameHeight - objHeight );
			button.setBounds(x,y,objWidth,objHeight );
			button.setBackground(Color.WHITE);
			button.setOpaque(true);
			button.setBorder(new LineBorder(Color.BLACK));
			if(i == 0) {
				//将随机生成的第一个按钮设置为目标按钮
				button.setBackground(Color.RED);
				myJPanel.add(button);
				listButton.add(i,button);
				i++;
			}else {
				for(int index = 0; index < listButton.size(); index++) {
					//System.out.println(index);
					int btnX = listButton.get(index).getX();
					int btnY = listButton.get(index).getY();
					rect.setBounds(btnX,btnY,objWidth,objHeight);//按钮的宽高固定则为了提高效率直接使用w,h
					//判断按钮是否重叠
					if (rect.contains(x, y)
							||rect.contains(x, y+objHeight)
							||rect.contains(x+objWidth, y)
							||rect.contains(x+objHeight, y+objWidth)
							||(btnX == x && btnY == y) //两个完全重合的矩形
							||rect.contains(x+objWidth/2, y)
							||rect.contains(x, y+objHeight/2)
							||rect.contains(x+objWidth, y+objHeight/2)
							||rect.contains(x+objWidth/2, y+objHeight/2)
							) // 判断新的按钮边界上的八个点是否被其他按钮包含 // 只要有一个点被包含则代表按钮重叠
					{
						//System.out.println("按钮重叠了，舍弃");
						boolBtn = false;
						break;
					}else {
						boolBtn = true;
					}
				}

				if (boolBtn) {
					myJPanel.add(button);
					listButton.add(i, button);
					i++;
				}
			}
		}
		
		for(int index = 0; index < listButton.size(); index++) {
			listButton.get(index).addMouseListener(new MouseInputAdapter() {
				public void mousePressed(MouseEvent e) {
					JButton button = (JButton)e.getSource();
					point = SwingUtilities.convertPoint( button, e.getPoint(), button.getParent()); // 得到当前坐标点
				}
			});
			listButton.get(index).addMouseMotionListener(new MouseInputAdapter() {
				public void mouseDragged(MouseEvent e) {
					JButton button = (JButton)e.getSource();
					Point newPoint = SwingUtilities.convertPoint(button, e.getPoint(), button.getParent()); // 转换坐标系统
					button.setLocation(button.getX() + (newPoint.x - point.x), button.getY() + (newPoint.y - point.y)); // 设置标签图片的新位置
					point = newPoint; // 更改坐标点
				}
			}); 
		}
		
	}
	
	public static void main(String[] args) {
		new GetPointData();
	}

}




