package NinePointSelect;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileSystemView;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.speech.AipSpeech;


public class NinePointSelect {
	
	private JFrame mainFrame;
	private Container mainContentPane;
	private JPanel mainPanel;
	private Rectangle mainFrameBounds;//用于保存当前JFrame所在的屏幕位置
	private int mainFrameWidth,mainFrameHeight,mainFrameX,mainFrameY;
	private Insets aInsets ;//主界面的修饰栏
	private int frameBottom,frameTop,frameLeft,frameRight;//主界面的边框大小
	private ArrayList<JButton> listButton;
	private int objWidth = 40,objHeight = 40;//目标大小
	private Point mousePoint;
	private Point mousePointTemp;
	private Robot mainRbt;
	private int layerNum = 5 ;
	private int distanceOfEachLayer = 0;
	private int numOfSelect;//操作步数
	private long startTime;
	private long endTime;
	private int modeFlag = 0;
	private String modeT="",areaT="",keyT="",logT="";
	private int voiceModeFlag=0;
	private boolean cancelFlag = false;
	private MyActionListener listener;
	
	public static final String APP_ID = "11453107";
    public static final String API_KEY = "pLu1heTGawjYDxfQCjGn5wGy";
    public static final String SECRET_KEY = "pkg4NvXeOozXrxaMiYXlHfhr6rNMVI6P";
	
    //定义录音格式  
    AudioFormat af = null;  
    //定义目标数据行,可以从中读取音频数据,该 TargetDataLine 接口提供从目标数据行的缓冲区读取所捕获数据的方法。  
    TargetDataLine td = null;  
    //定义源数据行,源数据行是可以写入数据的数据行。它充当其混频器的源。应用程序将音频字节写入源数据行，这样可处理字节缓冲并将它们传递给混频器。  
    SourceDataLine sd = null;  
    //定义字节数组输入输出流
    ByteArrayInputStream bais = null;
    ByteArrayOutputStream baos = null;
    //定义音频输入流
    AudioInputStream ais = null;
    //定义停止录音的标志，来控制录音线程的运行
    Boolean stopflag = false;
    AipSpeech client;
    private String textResult;
    
	public NinePointSelect() {
		client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
		mousePointTemp = new Point();
		initFrame();
		myEvent();
	}
	
	// 将数据保存进文档
	public void dateSave(String data) {
		String os = System.getProperty("os.name");  
		FileWriter fw = null;
		try {
			//当前用户桌面
			File desktopDir = FileSystemView.getFileSystemView() .getHomeDirectory();
			String desktopPath = desktopDir.getAbsolutePath();
			// 如果文件存在，则追加内容；如果文件不存在，则创建文件
			File f = new File(desktopPath+"/data.txt");;
			if(os.toLowerCase().startsWith("win")){  
				f = new File(desktopPath+"\\data.txt");
			}
			if (!(f.exists())) {
				f.createNewFile();
			}
			fw = new FileWriter(f, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(fw);
		pw.println(data);
		pw.flush();
		try {
			fw.flush();
			pw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 载入界面
	public void initFrame() {
		mainFrame =new JFrame();
		mainFrame.setSize(900, 900);
		mainFrame.setLocation(0, 0);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainContentPane = mainFrame.getContentPane();
		mainPanel=new JPanel();	
		mainPanel.setLayout(null);
		mainContentPane.add(mainPanel);
		mainFrame.setVisible(true);
		mainFrame.setResizable(false);
		
		aInsets = mainFrame.getInsets();
		frameBottom = aInsets.bottom;
		frameLeft = aInsets.left;
		frameRight = aInsets.right;
		frameTop = aInsets.top;
		
		mainFrameBounds = mainFrame.getBounds();
		mainFrameX = mainFrameBounds.x;
		mainFrameY = mainFrameBounds.y;
		mainFrameWidth = mainFrameBounds.width;
		mainFrameHeight = mainFrameBounds.height;
		
	}
	
	public void setTitle() {
		mainFrame.setTitle("目标选择 : "+ modeT + areaT + keyT + logT);
	}
   
	//界面重载函数
	public void winReInit() {
		logT = "请选择模式 : a、s、d";
		setTitle();
		
		mainPanel.removeAll();
		System.out.println("00000000000000000000");
		addMyJPanel(mainPanel);
		mainPanel.repaint();
		System.out.println("111111111111111111111");

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
		dateSave("******** 实验重置！"+df.format(System.currentTimeMillis())+"开始记录 ********");
	}
    
	public void addButtonClick() {
		listener = new MyActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				super.actionPerformed(e);
				System.out.println("123");
				endTime = System.currentTimeMillis();
				logT ="选中目标按钮所花时间为:"+(endTime - startTime)+" 毫秒！花了"+numOfSelect+"步！";
				dateSave(modeT+logT);
				setTitle();
				mainFrame.requestFocus();
			}
		};
		System.out.println("22222222222222222222");
		listButton.get(0).addActionListener(listener);
	}
	
	// 将按钮添加进区域  参数：myJPanel  需要添加按钮的区域  ；返回一个ArrayList<JButton>
	public void addMyJPanel(JPanel myJPanel) {
		listButton = new ArrayList<JButton>();
		Random random = new Random();
		Rectangle rect = new Rectangle();
		int i = 0;
		boolean boolBtn = false;
		JButton button;
		int num = 10;
		while ( i < num ) { // i 用于控制添加的按钮个数
			button = new JButton();
			int x = random.nextInt(mainFrameWidth-frameLeft-frameRight - objWidth );
			int y = random.nextInt(mainFrameHeight- frameTop - frameBottom - objHeight );
			button.setBounds(
					x,//根据容器的宽度产生随机x坐标，再减去按钮的宽度
					y,//根据容器的高度产生随机y坐标，再减去按钮的高度
					objWidth,
					objHeight );
			button.setFont(new Font("宋体", Font.BOLD, 10));
			button.setBackground(Color.WHITE);
			//根据需求将按钮对象存进ArrayList
			if(i == 0) {
				//将随机生成的第一个按钮设置为目标按钮
				button.setText("O");
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
			listButton.get(index).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					JButton button = (JButton)e.getSource();
					button.setBackground(Color.GREEN);
					super.mouseEntered(e);
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					JButton button = (JButton)e.getSource();
					button.setBackground(Color.WHITE);
					super.mouseExited(e);
				}
			});
			listButton.get(index).addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					mainFrame.requestFocus();
				}
			});
		}
		
	}
	
	// 依据mousePoint和distanceOfEachLayer画出九个区域的中心点
	public void paintNine() {
		Point myPoint = new Point(mousePoint.x,mousePoint.y);
		SwingUtilities.convertPointFromScreen(myPoint, mainPanel);
		int mX = myPoint.x;
		int mY = myPoint.y;
		Graphics graphics = mainPanel.getGraphics();
		mainPanel.paint(graphics);
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
	
	// 依据mousePoint和distanceOfEachLayer画出九个区域的中心点（每个区域带边框）
	public void paintNine2() {
		Point myPoint = new Point(mousePoint.x,mousePoint.y);
		SwingUtilities.convertPointFromScreen(myPoint, mainPanel);
		int mX = myPoint.x;
		int mY = myPoint.y;
		Graphics graphics = mainPanel.getGraphics();
		mainPanel.paint(graphics);
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
		
		//内 竖线
		graphics.drawLine(mX - distanceOfEachLayer*3/2, mY - distanceOfEachLayer/2, mX + distanceOfEachLayer*3/2, mY - distanceOfEachLayer/2);
		graphics.drawLine(mX - distanceOfEachLayer*3/2, mY + distanceOfEachLayer/2, mX + distanceOfEachLayer*3/2, mY + distanceOfEachLayer/2);
		graphics.drawLine(mX - distanceOfEachLayer/2, mY - distanceOfEachLayer*3/2, mX - distanceOfEachLayer/2, mY + distanceOfEachLayer*3/2);
		graphics.drawLine(mX + distanceOfEachLayer/2, mY - distanceOfEachLayer*3/2, mX + distanceOfEachLayer/2, mY + distanceOfEachLayer*3/2);
		//外边框
		graphics.drawLine(mX - distanceOfEachLayer*3/2, mY - distanceOfEachLayer*3/2, mX + distanceOfEachLayer*3/2, mY - distanceOfEachLayer*3/2);
		graphics.drawLine(mX - distanceOfEachLayer*3/2, mY - distanceOfEachLayer*3/2, mX - distanceOfEachLayer*3/2, mY + distanceOfEachLayer*3/2);
		graphics.drawLine(mX + distanceOfEachLayer*3/2, mY + distanceOfEachLayer*3/2, mX + distanceOfEachLayer*3/2, mY - distanceOfEachLayer*3/2);
		graphics.drawLine(mX + distanceOfEachLayer*3/2, mY + distanceOfEachLayer*3/2, mX - distanceOfEachLayer*3/2, mY + distanceOfEachLayer*3/2);
		
	}
	
	// 将鼠标移动到点
	public void moveMouse(Point pointInScreen) {
		try {
			mainRbt = new Robot();
			mainRbt.mouseMove(pointInScreen.x,pointInScreen.y);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		logT = "鼠标移动到：("+pointInScreen.x+","+pointInScreen.y+")";
	}
	
	// 计算每层(0-3)深度所需的距离
	public void setLayerDistance() {
		switch (layerNum) {
		case 0:
			distanceOfEachLayer = mainFrameWidth/3;
			break;
		case 1:
			distanceOfEachLayer = mainFrameWidth/3/3;
			break;
		case 2:
			distanceOfEachLayer = mainFrameWidth/3/3/3;
			break;
		}
	}
	
	// 设置深度(0-3)循环
	public void setLayerNum() {
		layerNum++;
		if (layerNum > 2) {
			layerNum = 0;
			mousePointTemp = mousePoint;
			mousePoint =  new Point((mainFrameWidth - frameLeft - frameRight)/2,mainFrameHeight/2);//计算出frame中心坐标
			SwingUtilities.convertPointToScreen(mousePoint, mainPanel);//转化成frame的坐标系
		}
	}
	
	// 计算下一级鼠标位置
	public void mouseMoveToNext(int keyNumber) {
		mousePointTemp = mousePoint;
		Point point = new Point();
		int mX = mousePoint.x, mY = mousePoint.y;
		switch (keyNumber) {
		case 1:
			point.setLocation( mX - distanceOfEachLayer, mY - distanceOfEachLayer);
			break;
		case 2:
			point.setLocation( mX , mY - distanceOfEachLayer);
			break;
		case 3:
			point.setLocation( mX + distanceOfEachLayer , mY - distanceOfEachLayer);
			break;
		case 4:
			point.setLocation( mX - distanceOfEachLayer, mY);
			break;
		case 5:
			point.setLocation( mX , mY);
			break;
		case 6:
			point.setLocation( mX + distanceOfEachLayer, mY);
			break;
		case 7:
			point.setLocation( mX - distanceOfEachLayer, mY + distanceOfEachLayer);
			break;
		case 8:
			point.setLocation( mX , mY + distanceOfEachLayer);
			break;
		case 9:
			point.setLocation( mX + distanceOfEachLayer, mY + distanceOfEachLayer);
			break;
		}
		moveMouse(point);//移动鼠标
		mousePoint = point;
		
	}
	
	// 事件监听
	public void myEvent() {
		
		// 窗口事件的监听：窗口关闭、打开、最小化
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				super.windowOpened(e);
				winReInit();
				addButtonClick();
			}
		});
		
		// 窗体大小改变、移动、隐藏和显示的监听
		mainFrame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				mainFrameBounds = mainFrame.getBounds();
				mainFrameX = mainFrameBounds.x;
				mainFrameY = mainFrameBounds.y;
				mainFrameWidth = mainFrameBounds.width;
				mainFrameHeight = mainFrameBounds.height;
			}
		});
		
		mainFrame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				super.keyPressed(e);
				switch (e.getKeyCode()) {
				case KeyEvent.VK_SPACE:
					listButton.get(0).removeActionListener(listener);
					winReInit();
					addButtonClick();
					System.out.println("space");
					break;
				case KeyEvent.VK_1:
					selectMode(1);
					cancelFlag = true;
					break;
				case KeyEvent.VK_2:
					selectMode(2);
					cancelFlag = true;
					break;
				case KeyEvent.VK_3:
					selectMode(3);
					cancelFlag = true;
					break;
				case KeyEvent.VK_4:
					selectMode(4);
					cancelFlag = true;
					break;
				case KeyEvent.VK_5:
					selectMode(5);
					cancelFlag = true;
					break;
				case KeyEvent.VK_6:
					selectMode(6);
					cancelFlag = true;
					break;
				case KeyEvent.VK_7:
					selectMode(7);
					cancelFlag = true;
					break;
				case KeyEvent.VK_8:
					selectMode(8);
					cancelFlag = true;
					break;
				case KeyEvent.VK_9:
					selectMode(9);
					cancelFlag = true;
					break;
				case KeyEvent.VK_LEFT:
					keyOri(1);
					cancelFlag = true;
					break;
				case KeyEvent.VK_RIGHT:
					keyOri(2);
					cancelFlag = true;
					break;
				case KeyEvent.VK_UP:
					keyOri(3);
					cancelFlag = true;
					break;
				case KeyEvent.VK_DOWN:
					keyOri(4);
					cancelFlag = true;
					break;
				case KeyEvent.VK_ENTER:
					mouseClick();
					if (modeFlag == 2) {
						mode2MouseClick();
					}
					break;
				case KeyEvent.VK_A:
					System.out.println("a");
					mode1Enter();
					break;
				case KeyEvent.VK_S:
					System.out.println("s");
					mode2Enter();
					break;
				case KeyEvent.VK_D:
					System.out.println("d");
					mode3Enter();
					break;
				case KeyEvent.VK_Z:
					keyT = "您按下了Z键; ";
					logT = "正在录音";
					setTitle();
					capture(); // 调用录音的方法
					break;
				case KeyEvent.VK_X:
					keyT = "您按下了X键; ";
					logT = "开始语音识别";
					setTitle();
					// 停止录音
					stopflag = true;
					// 调用保存录音的方法
					save();
					break;
				case KeyEvent.VK_Q:
					cancelStep(cancelFlag);
					break;
				}
			}
		});
		
		// 全局的键盘监听事件
//		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
//			public void eventDispatched(AWTEvent event) {
//				switch (((KeyEvent) event).getID()) {
//				case KeyEvent.KEY_PRESSED:
//					switch (((KeyEvent) event).getKeyCode()) {
//					case KeyEvent.VK_SPACE:
//						listButton.get(0).removeActionListener(listener);
//						winReInit();
//						addButtonClick();
//						System.out.println("space");
//						break;
//					case KeyEvent.VK_1:
//						selectMode(1);cancelFlag = true;
//						break;
//					case KeyEvent.VK_2:
//						selectMode(2);cancelFlag = true;
//						break;
//					case KeyEvent.VK_3:
//						selectMode(3);cancelFlag = true;
//						break;
//					case KeyEvent.VK_4:
//						selectMode(4);cancelFlag = true;
//						break;
//					case KeyEvent.VK_5:
//						selectMode(5);cancelFlag = true;
//						break;
//					case KeyEvent.VK_6:
//						selectMode(6);cancelFlag = true;
//						break;
//					case KeyEvent.VK_7:
//						selectMode(7);cancelFlag = true;
//						break;
//					case KeyEvent.VK_8:
//						selectMode(8);cancelFlag = true;
//						break;
//					case KeyEvent.VK_9:
//						selectMode(9);cancelFlag = true;
//						break;
//					case KeyEvent.VK_LEFT:
//						keyOri(1);cancelFlag = true;
//						break;
//					case KeyEvent.VK_RIGHT:
//						keyOri(2);cancelFlag = true;
//						break;
//					case KeyEvent.VK_UP:
//						keyOri(3);cancelFlag = true;
//						break;
//					case KeyEvent.VK_DOWN:
//						keyOri(4);cancelFlag = true;
//						break;
//					case KeyEvent.VK_ENTER:
//						mouseClick();
//						if (modeFlag == 2) {
//							mode2MouseClick();
//						}
//						break;
//					case KeyEvent.VK_A:
//						System.out.println("a");
//						mode1Enter(); 
//						break;
//					case KeyEvent.VK_S:
//						System.out.println("s");
//						mode2Enter();
//						break;
//					case KeyEvent.VK_D:
//						System.out.println("d");
//						mode3Enter();
//						break;
//					case KeyEvent.VK_Z:
//						keyT = "您按下了Z键; ";
//						logT = "正在录音";
//						setTitle();
//	    	            capture(); //调用录音的方法 
//						break;
//					case KeyEvent.VK_X:
//						keyT = "您按下了X键; ";
//						logT = "开始语音识别";
//						setTitle();
//	    	            //停止录音  
//						stopflag = true;   
//	    	            //调用保存录音的方法  
//	    	            save();
//						break;
//					case KeyEvent.VK_Q:
//						cancelStep(cancelFlag);
//						break;
//					}
//					break;
//				}
//			}
//		}, AWTEvent.KEY_EVENT_MASK);
	}
	
	// 返回上一步 只能执行一次
	public void cancelStep(boolean flag) {
		switch (modeFlag) {
		case 1:
			if (flag) {
				mousePoint = mousePointTemp;
				moveMouse(mousePoint);
				numOfSelect++;//步数+1
				layerNum--;//层数-1
				setLayerDistance();
				paintNine();
				logT = "返回上一步";
				setTitle();
				cancelFlag = false;
			}
			break;
		case 2:
			if (flag) {
				mousePoint = mousePointTemp;
				moveMouse(mousePoint);
				numOfSelect++;//步数+1
				layerNum--;//层数-1
				setLayerDistance();
				paintNine2();
				selectButton();
				logT = "返回上一步";
				setTitle();
				cancelFlag = false;
			}
			break;
		case 3:
			if (flag) {
				mousePoint = mousePointTemp;
				moveMouse(mousePoint);
				numOfSelect++;//步数+1
				if (layerNum != 0) {
					layerNum--;//层数-1
				}
				setLayerDistance();
				paintFlag();
				logT = "返回上一步";
				setTitle();
				cancelFlag = false;
			}
			break;
		}
	}
	
	// 语音命令的分配
	public void textAnalyze(String textResult) {
		if ( textResult != null) {
			System.out.println("程序正常运行,识别结果为："+textResult);
			if (textResult.contains("一")||textResult.contains("1")) {
				selectMode(1);
			}else if (textResult.contains("二")||textResult.contains("2")) {
				selectMode(2);
			}else if (textResult.contains("三")||textResult.contains("3")) {
				selectMode(3);
			}else if (textResult.contains("四")||textResult.contains("4")) {
				selectMode(4);
			}else if (textResult.contains("五")||textResult.contains("5")) {
				selectMode(5);
			}else if (textResult.contains("六")||textResult.contains("6")) {
				selectMode(6);
			}else if (textResult.contains("七")||textResult.contains("7")) {
				selectMode(7);
			}else if (textResult.contains("八")||textResult.contains("8")) {
				selectMode(8);
			}else if (textResult.contains("九")||textResult.contains("9")) {
				selectMode(9);
			}else if (textResult.contains("上")) {
				keyOri(3);
			}else if (textResult.contains("下")) {
				keyOri(4);
			}else if (textResult.contains("左")) {
				keyOri(1);
			}else if (textResult.contains("右")) {
				keyOri(2);
			}else if (textResult.contains("点")) {
				mouseClick();
				mode2MouseClick();
			}else if (textResult.contains("重")) {
				winReInit();
			}else if (textResult.contains("换")) {
				voiceModeFlag++;
				if (voiceModeFlag == 1) {
					mode1Enter(); 
				}else if (voiceModeFlag == 2) {
					mode2Enter(); 
				}else if (voiceModeFlag == 3) {
					mode3Enter(); 
				}else {
					voiceModeFlag = 1;
					mode1Enter(); 
				}
			}
		}
	}
	
	// 对音频数据进行处理  
    public void save()  
    {  
        af = getAudioFormat();  
        byte audioData[] = baos.toByteArray();  
        bais = new ByteArrayInputStream(audioData);  
        ais = new AudioInputStream(bais,af, audioData.length / af.getFrameSize());  
        ByteArrayOutputStream myData = new ByteArrayOutputStream();
        try {
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, myData); 
            byte[] myAudioData = myData.toByteArray();
            textResult = rec(myAudioData);
            logT = textResult;
            setTitle();
            textAnalyze(textResult);
        }
        catch (Exception e) {  
            e.printStackTrace();  
        }
        finally{    
            try {   
                if(bais != null)  
                {  
                    bais.close();  
                }   
                if(ais != null)  
                {  
                    ais.close();          
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            }         
        }
        
    }

    // 设置音频格式  
    public AudioFormat getAudioFormat()   
    {        
      //采样率是每秒播放和录制的样本数  
      float sampleRate = 16000F;
      int sampleSizeInBits = 16;  
      // 8,16  
      int channels = 1;  
      // 单声道为1，立体声为2  
      boolean signed = true;  
      // true,false  
      boolean bigEndian = true;  
      // true,false  
      //构造具有线性 PCM 编码和给定参数的 AudioFormat
      return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,bigEndian);  
    }
    
    // 保存音频数据
	class Record implements Runnable  
    {  
    	//定义存放录音的字节数组,作为缓冲区  
        byte bts[] = new byte[10000];  
        //将字节数组包装到流里，最终存入到baos中   
        public void run() {   
            baos = new ByteArrayOutputStream();       
            try {  
                stopflag = false;  
                while(stopflag != true)  
                {  
                    //当停止录音没按下时，该线程一直执行
                    int cnt = td.read(bts, 0, bts.length);  
                    if(cnt > 0)  
                    {  
                        baos.write(bts, 0, cnt);  
                    }  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            }finally{  
                try {  
                    if(baos != null)  
                    {  
                        baos.close();  
                    }     
                } catch (Exception e) {  
                    e.printStackTrace();  
                }finally{  
                    td.close();      
                }  
            }  
        }  
          
    }
	
    // 音频数据上传返回语音识别结果
    public String rec(byte[] myData) {
    	String result = null;
        
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("dev_pid", 1536);
        JSONObject res = client.asr(myData, "wav", 16000, options);
        
        if (res.has("result")) {
        	JSONArray jsonArray = res.getJSONArray("result");
        	result =jsonArray.getString(0);
		}else {
			mainFrame.setTitle("没听清");
		}
        
        return result;
	}
    
	// 从麦克风捕捉声音数据
	public void capture()  
    {  
        try {   
            af = getAudioFormat();  
            DataLine.Info info = new DataLine.Info(TargetDataLine.class,af);  
            //TargetDataLine是声音的输入(麦克风),而SourceDataLine是输出(音响,耳机).
            td = (TargetDataLine)(AudioSystem.getLine(info));  
             
            td.open(af);   
            td.start();  

            Record record = new Record();  
            Thread t1 = new Thread(record);  
            t1.start();  

        } catch (Exception ex) {  
            ex.printStackTrace();  
            return;  
        }
    }  
	
	// 上下左右键盘的操作函数
	public void keyOri(int orientation) {
		if (modeFlag == 3) {
			mouseMoveBtn(orientation);
			paintFlag();
			numOfSelect++;
			if (numOfSelect == 1) {
				startTime = System.currentTimeMillis();
			}
		}
		switch (orientation) {
		case 1:
			keyT = "您按下了左键; ";
			break;
		case 2:
			keyT = "您按下了右键; ";
			break;
		case 3:
			keyT = "您按下了上键; ";
			break;
		case 4:
			keyT = "您按下了下键; ";
			break;
		}
		setTitle();
	}
	
	// 区域选择时加入的：只有单个按钮被区域包含 就能按回车点击
	public void mode2MouseClick() {
		int num = 0,select = 1000;
		for (int index=0 ;index < listButton.size() ;index++ ) {
			if (listButton.get(index).getBackground()== Color.LIGHT_GRAY) {
				num++;
				select = index;
				
			}
		}
		if (num == 1 && select!=1000) {
			listButton.get(select).doClick();
		}
	}
	
	// 按键的模式选择入口
	public void selectMode(int num) {
		switch (modeFlag) {
		case 1:
			paint1(num);
			break;
		case 2:
			paint2(num);
			break;
		case 3:
			paint3(num);
			break;
		}
	}
	
	// 画九宫格
	public void paint1(int keyNum) {
		if (layerNum < 3) {
			mouseMoveToNext(keyNum);
			numOfSelect++;
			if (numOfSelect == 1) {
				startTime = System.currentTimeMillis();
			}
			if (layerNum < 2) {
				layerNum++;
				//进行下一级操作的准备：设置层次、距离，画出预选点
				setLayerDistance();
				paintNine();
			}else {
				layerNum++;
				// 让panel上的图形消失
				Graphics graphics = mainPanel.getGraphics();
				mainPanel.paint(graphics);
			}
		}
		areaT = "您选择了:" + keyNum + "号; ";
		setTitle();
	}
	
	// 画九宫格，带 改变区域包含的按钮颜色
	public void paint2(int keyNum) {
		if (layerNum < 3) {
			mouseMoveToNext(keyNum);
			numOfSelect++;
			if (numOfSelect == 1) {
				startTime = System.currentTimeMillis();
			}
			if (layerNum < 2) {
				layerNum++;
				//进行下一级操作的准备：设置层次、距离，画出预选点
				setLayerDistance();
				paintNine2();
				selectButton();
			}else {
				layerNum++;
				// 让panel上的图形消失
				Graphics graphics = mainPanel.getGraphics();
				mainPanel.paint(graphics);
				
				// 需要将distanceOfEachLayer再减一层才能只包含最后一个按钮
				selectButtonTest();
			}
		}
		areaT = "您选择了:" + keyNum + "号; ";
		setTitle();
	}
	
	// 画九宫格加画指示
	public void paint3(int keyNum) {
		if (layerNum < 3) {
			mouseMoveToNext(keyNum);
			numOfSelect++;
			if (numOfSelect == 1) {
				startTime = System.currentTimeMillis();
			}
			if (layerNum < 2) {
				layerNum++;
				//进行下一级操作的准备：设置层次、距离，画出预选点
				setLayerDistance();
				paintFlag();
			}else {
				layerNum++;
				// 让panel上的图形消失
				Graphics graphics = mainPanel.getGraphics();
				mainPanel.paint(graphics);
			}
		}
		areaT = "您选择了:" + keyNum + "号; ";
		setTitle();
	}
	
	// 模式3流程入口
	public void mode3Enter(){
		for (int index=0 ;index < listButton.size() ;index++ ) {
			listButton.get(index).setBackground(Color.WHITE);
		}
		modeFlag = 3;
		modeT = "跳跃式选择; ";
		mousePoint =  new Point((mainFrameWidth - frameLeft - frameRight)/2,mainFrameHeight/2);//计算出frame中心坐标
		SwingUtilities.convertPointToScreen(mousePoint, mainPanel);//转化成frame的坐标系
		moveMouse(mousePoint);//移动鼠标
		layerNum = 0;
		setLayerDistance();
		numOfSelect = 0;
		paintFlag();
		setTitle();
		mousePointTemp = mousePoint;
	}
	// 模式2流程入口
	public void mode2Enter() {
		for (int index=0 ;index < listButton.size() ;index++ ) {
			listButton.get(index).setBackground(Color.WHITE);
		}
		modeFlag = 2;
		modeT = "九宫格面选; ";
		mousePoint =  new Point((mainFrameWidth - frameLeft - frameRight)/2,mainFrameHeight/2);//计算出frame中心坐标
		SwingUtilities.convertPointToScreen(mousePoint, mainPanel);//转化成frame的坐标系
		moveMouse(mousePoint);//移动鼠标
		
		layerNum = 0;
		setLayerDistance();
		paintNine2();//画标记
		selectButton();
		numOfSelect = 0;
		setTitle();
		mousePointTemp = mousePoint;
	}
	// 模式1流程入口
	public void mode1Enter() {
		for (int index=0 ;index < listButton.size() ;index++ ) {
			listButton.get(index).setBackground(Color.WHITE);
		}
		modeFlag = 1;
		modeT = "九宫格点选; ";
		mousePoint =  new Point((mainFrameWidth - frameLeft - frameRight)/2,mainFrameHeight/2);//计算出frame中心坐标
		SwingUtilities.convertPointToScreen(mousePoint, mainPanel);//转化成frame的坐标系
		moveMouse(mousePoint);//移动鼠标
		layerNum = 0;
		setLayerDistance();
		paintNine();//画标记
		numOfSelect = 0;
		setTitle();
		mousePointTemp = mousePoint;
	}
	
	// 实现选中区域内的按钮
	public void selectButton() {
		// 得出选中的区域
		Point myPoint = new Point(mousePoint.x,mousePoint.y);
		SwingUtilities.convertPointFromScreen(myPoint, mainPanel);
		int mX = myPoint.x;
		int mY = myPoint.y;
		Rectangle rect = new Rectangle();
		rect.setBounds(mX - distanceOfEachLayer*3/2, mY - distanceOfEachLayer*3/2, distanceOfEachLayer*3, distanceOfEachLayer*3);
		
		// 筛选出区域中的按钮
		for (int index=0 ;index < listButton.size() ;index++ ) {
			Rectangle rectButton = listButton.get(index).getBounds();
			Point one = new Point(rectButton.x, rectButton.y);
			Point two = new Point(rectButton.x + rectButton.width, rectButton.y);
			Point three = new Point(rectButton.x, rectButton.y + rectButton.height);
			Point four = new Point(rectButton.x + rectButton.width, rectButton.y + rectButton.height);
			if (rect.contains(one)||rect.contains(two)||rect.contains(three)||rect.contains(four)) {
				listButton.get(index).setBackground(Color.LIGHT_GRAY);
			}else {
				listButton.get(index).setBackground(Color.WHITE);
			}
		}
		
	}
	
	// 改bug加入的函数：解决distanceOfEachLayer不够小的问题
	public void selectButtonTest() {
		
		distanceOfEachLayer = mainFrameWidth/3/3/3/3;
		// 得出选中的区域
		Point myPoint = new Point(mousePoint.x,mousePoint.y);
		SwingUtilities.convertPointFromScreen(myPoint, mainPanel);
		int mX = myPoint.x;
		int mY = myPoint.y;
		Rectangle rect = new Rectangle();
		rect.setBounds(mX - distanceOfEachLayer*3/2, mY - distanceOfEachLayer*3/2, distanceOfEachLayer*3, distanceOfEachLayer*3);
		
		// 筛选出区域中的按钮
		for (int index=0 ;index < listButton.size() ;index++ ) {
			Rectangle rectButton = listButton.get(index).getBounds();
			Point one = new Point(rectButton.x, rectButton.y);
			Point two = new Point(rectButton.x + rectButton.width, rectButton.y);
			Point three = new Point(rectButton.x, rectButton.y + rectButton.height);
			Point four = new Point(rectButton.x + rectButton.width, rectButton.y + rectButton.height);
			if (rect.contains(one)||rect.contains(two)||rect.contains(three)||rect.contains(four)) {
				listButton.get(index).setBackground(Color.LIGHT_GRAY);
			}else {
				listButton.get(index).setBackground(Color.WHITE);
			}
		}
		
	}
	
	// 跳跃模式 画箭头 画九宫格 将功能集合在一起是避免mainPanel.paint(graphics);清空之前所画图像
	public void paintFlag() {
		Point myPoint = new Point(mousePoint.x,mousePoint.y);
		SwingUtilities.convertPointFromScreen(myPoint, mainPanel);
		int mX = myPoint.x;
		int mY = myPoint.y;
		Graphics graphics = mainPanel.getGraphics();
		mainPanel.paint(graphics);
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
		
		graphics.setColor(Color.RED);

		graphics.drawLine(mX,mY,mX,mY-1000);
		graphics.drawLine(mX,mY,mX,mY+1000);
		graphics.drawLine(mX,mY,mX-1000,mY);
		graphics.drawLine(mX,mY,mX+1000,mY);
		
		abc(myPoint.x,myPoint.y);//改变按钮背景颜色
		//paintJT(graphics,myPoint.x,myPoint.y);//画箭头
		
	}
	
	// 使对应的按钮变色
	public void abc(int mouseInAreaX,int mouseInAreaY) {
		for (int index = 0; index < listButton.size(); index++) {
			listButton.get(index).setBackground(Color.WHITE);
		}
		Rectangle rect;
		int rectX,rectY,rectH,rectW;
		int distance;
		int cpn=1000;
		int obj1=100,obj2=100,obj3=100,obj4=100;
		for (int a = 0; a < listButton.size(); a++) {
			rect = listButton.get(a).getBounds();
			rectX = rect.x;
			rectY = rect.y;
			rectW = rect.width;
			rectH = rect.height;
			if( ( rectX + rectW/2 ) < mouseInAreaX) {
				if( rectY > (mouseInAreaY - rectH - rectH/2) && rectY < (mouseInAreaY + rectH/2) ) {
					distance = mouseInAreaX - rectX;
					if (distance < cpn) {
						cpn = distance;
						obj1 = a;
					}
				}
			}	
		}
		for (int b = 0; b < listButton.size(); b++) {
			rect = listButton.get(b).getBounds();
			rectX = rect.x;
			rectY = rect.y;
			rectW = rect.width;
			rectH = rect.height;
			if( ( rectX + rectW/2 ) > mouseInAreaX) {
				if( rectY > (mouseInAreaY - rectH - rectH/2) && rectY < (mouseInAreaY + rectH/2) ) {
					distance = rectX - mouseInAreaX;
					if (distance < cpn) {
						cpn = distance;
						obj2 = b;
					}
				}
			}	
		}
		for (int c = 0; c < listButton.size(); c++) {
			rect = listButton.get(c).getBounds();
			rectX = rect.x;
			rectY = rect.y;
			rectW = rect.width;
			rectH = rect.height;
			if( ( rectY + rectH/2 ) < mouseInAreaY) {
				if (rectX > (mouseInAreaX - rectW -rectW/2) && rectX < (mouseInAreaX + rectW/2)) {
					distance = mouseInAreaY - rectY;
					if (distance < cpn) {
						cpn = distance;
						obj3 = c;
					}
				}
			}	
		}
		for (int d = 0; d < listButton.size(); d++) {
			rect = listButton.get(d).getBounds();
			rectX = rect.x;
			rectY = rect.y;
			rectW = rect.width;
			rectH = rect.height;
			if( ( rectY + rectH/2 ) > mouseInAreaY) {
				if (rectX > (mouseInAreaX - rectW -rectW/2) && rectX < (mouseInAreaX + rectW/2)) {
					distance = rectY - mouseInAreaY;
					if (distance < cpn) {
						cpn = distance;
						obj4 = d;
					}
				}
			}
		}
		if (obj1 != 100) {
			listButton.get(obj1).setBackground(Color.YELLOW);
		}
		if (obj2 != 100) {
			listButton.get(obj2).setBackground(Color.YELLOW);
		}
		if (obj3 != 100) {
			listButton.get(obj3).setBackground(Color.YELLOW);
		}
		if (obj4 != 100) {
			listButton.get(obj4).setBackground(Color.YELLOW);
		}
	}
	// 实现打印箭头标记
	public void paintJT(Graphics graphics,int mouseInAreaX,int mouseInAreaY) {
		Rectangle rect;
		int rectX,rectY,rectH,rectW;
		int distance;
		int cpn=1000;
		int obj1=100,obj2=100,obj3=100,obj4=100;
		
		for (int a = 0; a < listButton.size(); a++) {
			rect = listButton.get(a).getBounds();
			rectX = rect.x;
			rectY = rect.y;
			rectW = rect.width;
			rectH = rect.height;
			if( ( rectX + rectW/2 ) < mouseInAreaX) {
				if( rectY > (mouseInAreaY - rectH - rectH/2) && rectY < (mouseInAreaY + rectH/2) ) {
					distance = mouseInAreaX - rectX;
					if (distance < cpn) {
						cpn = distance;
						obj1 = a;
						System.out.println("1");
					}
				}
			}	
		}
		for (int b = 0; b < listButton.size(); b++) {
			rect = listButton.get(b).getBounds();
			rectX = rect.x;
			rectY = rect.y;
			rectW = rect.width;
			rectH = rect.height;
			if( ( rectX + rectW/2 ) > mouseInAreaX) {
				if( rectY > (mouseInAreaY - rectH - rectH/2) && rectY < (mouseInAreaY + rectH/2) ) {
					distance = rectX - mouseInAreaX;
					if (distance < cpn) {
						cpn = distance;
						obj2 = b;
						System.out.println("2");
					}
				}
			}	
		}
		for (int c = 0; c < listButton.size(); c++) {
			rect = listButton.get(c).getBounds();
			rectX = rect.x;
			rectY = rect.y;
			rectW = rect.width;
			rectH = rect.height;
			if( ( rectY + rectH/2 ) < mouseInAreaY) {
				if (rectX > (mouseInAreaX - rectW -rectW/2) && rectX < (mouseInAreaX + rectW/2)) {
					distance = mouseInAreaY - rectY;
					if (distance < cpn) {
						cpn = distance;
						obj3 = c;
						System.out.println("3");
					}
				}
			}	
		}
		for (int d = 0; d < listButton.size(); d++) {
			rect = listButton.get(d).getBounds();
			rectX = rect.x;
			rectY = rect.y;
			rectW = rect.width;
			rectH = rect.height;
			if( ( rectY + rectH/2 ) > mouseInAreaY) {
				if (rectX > (mouseInAreaX - rectW -rectW/2) && rectX < (mouseInAreaX + rectW/2)) {
					distance = rectY - mouseInAreaY;
					if (distance < cpn) {
						cpn = distance;
						obj4 = d;
						System.out.println("4");
					}
				}
			}
		}
		if (obj1 != 100) {
			rect = listButton.get(obj1).getBounds();
			
			//左 箭头
			graphics.drawLine(rect.x + rect.width/4, rect.y + rect.height/2, rect.x + rect.width*3/4, rect.y + rect.height/2);
			graphics.drawLine(rect.x + rect.width/4, rect.y + rect.height/2, rect.x + rect.width/3, rect.y + rect.height/3);
			graphics.drawLine(rect.x + rect.width/4, rect.y + rect.height/2, rect.x + rect.width/3, rect.y + rect.height*2/3);
			//point1.setLocation(rect.x + rect.width / 2,rect.y + rect.height / 2);
			//graphics.fillOval(point1.x, point1.y, 5,5);
			System.out.println("打印 左 箭头");
		}
		if (obj2 != 100) {
			rect = listButton.get(obj2).getBounds();
			
			//右 箭头
			graphics.drawLine(rect.x + rect.width*3/4, rect.y + rect.height/2, rect.x + rect.width/4, rect.y + rect.height/2);
			graphics.drawLine(rect.x + rect.width*3/4, rect.y + rect.height/2, rect.x + rect.width*2/3, rect.y + rect.height/3);
			graphics.drawLine(rect.x + rect.width*3/4, rect.y + rect.height/2, rect.x + rect.width*2/3, rect.y + rect.height*2/3);
			//point2.setLocation(rect.x + rect.width / 2,rect.y + rect.height / 2);
			//graphics.fillOval(point2.x, point2.y, 5,5);
			System.out.println("打印 右 箭头");
		}
		if (obj3 != 100) {
			//上 箭头
			rect = listButton.get(obj3).getBounds();
			
			graphics.drawLine(rect.x + rect.width / 2, rect.y + rect.height/4, rect.x + rect.width / 2, rect.y + rect.height*3/4);
			graphics.drawLine(rect.x + rect.width / 2, rect.y + rect.height/4, rect.x + rect.width / 3, rect.y + rect.height/3);
			graphics.drawLine(rect.x + rect.width / 2, rect.y + rect.height/4, rect.x + rect.width*2/3, rect.y + rect.height/3);
			//point3.setLocation(rect.x + rect.width / 2,rect.y + rect.height / 2);
			//graphics.fillOval(point3.x, point3.y, 5,5);
			System.out.println("打印 上 箭头");
		}
		if (obj4 != 100) {
			rect = listButton.get(obj4).getBounds();
			
			//下 箭头
			graphics.drawLine(rect.x + rect.width / 2, rect.y + rect.height*3/4, rect.x + rect.width / 2, rect.y + rect.height/4);
			graphics.drawLine(rect.x + rect.width / 2, rect.y + rect.height*3/4, rect.x + rect.width / 3, rect.y + rect.height*2/3);
			graphics.drawLine(rect.x + rect.width / 2, rect.y + rect.height*3/4, rect.x + rect.width*2/3, rect.y + rect.height*2/3);
			//point4.setLocation(rect.x + rect.width / 2,rect.y + rect.height / 2);
			//graphics.fillOval(point4.x, point4.y, 5,5);
			System.out.println("打印 下 箭头");
		}
	}
	
	// 判断按钮位置，移动鼠标指针到按钮中心  
	public void mouseMoveBtn(int orientation) {
		Point myPoint = new Point(mousePoint.x,mousePoint.y);
		SwingUtilities.convertPointFromScreen(myPoint, mainPanel);
		int mouseInAreaX = myPoint.x;
		int mouseInAreaY = myPoint.y;
		Rectangle rect;
		int rectX,rectY,rectH,rectW;
		int distance,cpn=1000,obj = 100;
		for (int index=0 ;index < listButton.size() ;index++ ) {
			rect = listButton.get(index).getBounds();
			rectX = rect.x;
			rectY = rect.y;
			rectW = rect.width;
			rectH = rect.height;
			switch (orientation) {
			case 1:
				if( ( rectX + rectW/2 ) < mouseInAreaX) {
					if( rectY > (mouseInAreaY - rectH - rectH/2) && rectY < (mouseInAreaY + rectH/2) ) {
						distance = mouseInAreaX - rectX;
						if (distance < cpn) {
							cpn = distance;
							obj = index;
						}
					}
				}
				break;
			case 2:
				if( ( rectX + rectW/2 ) > mouseInAreaX) {
					if( rectY > (mouseInAreaY - rectH - rectH/2) && rectY < (mouseInAreaY + rectH/2) ) {
						distance = rectX - mouseInAreaX;
						if (distance < cpn) {
							cpn = distance;
							obj = index;
						}
					}
				}
				break;
			case 3:
				if( ( rectY + rectH/2 ) < mouseInAreaY) {
					if (rectX > (mouseInAreaX - rectW -rectW/2) && rectX < (mouseInAreaX + rectW/2)) {
						distance = mouseInAreaY - rectY;
						if (distance < cpn) {
							cpn = distance;
							obj = index;
						}
					}
				}
				break;
			case 4:
				if( ( rectY + rectH/2 ) > mouseInAreaY) {
					if (rectX > (mouseInAreaX - rectW -rectW/2) && rectX < (mouseInAreaX + rectW/2)) {
						distance = rectY - mouseInAreaY;
						if (distance < cpn) {
							cpn = distance;
							obj = index;
						}
					}
				}
				break;
			}
		}
		
		if (cpn != 1000 && obj != 100) {
			//计算返回的鼠标位置（在屏幕中的位置）
			rect = listButton.get(obj).getBounds();
			rectX = rect.x;
			rectY = rect.y;
			rectW = rect.width;
			rectH = rect.height;
			Point point = new Point(rectX + rectW/2,rectY + rectH/2);
			SwingUtilities.convertPointToScreen(point, mainPanel);
			logT = "鼠标移动到：("+point.x+","+point.y+")";
			moveMouse(point);
			mousePointTemp = mousePoint;
			mousePoint = point;
			
		}
	}
	
	// 实现模拟鼠标点击
	public void mouseClick() {
		try {
			mainRbt =new Robot();
			//模拟鼠标按下左键
			mainRbt.mousePress(InputEvent.BUTTON1_MASK);
			//模拟鼠标松开左键
			mainRbt.mouseRelease(InputEvent.BUTTON1_MASK);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logT = "鼠标进行了一次点击";
	}
	
	
	public static void main(String[] args) {
		new NinePointSelect();
	}

}

class MyActionListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	
}