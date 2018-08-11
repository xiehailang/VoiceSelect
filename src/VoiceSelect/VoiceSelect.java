package VoiceSelect;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFName;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.speech.AipSpeech;


public class VoiceSelect {
	
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
    private boolean voiveFlag = true;
    private long recStartTime;
    private long recEndTime;
    
    private String testT="方法一； ",areaT="",logT="",keyT="";
    
	private JFrame mainFrame;
	private Container mainContentPane;
	private JPanel mainPanel,selectPanel;
	private int objWidth = 30,objHeight = 30;//目标大小
	private int frameWidth = 900,frameHeight = 900;//区域大小
	private JButton button1,button2,button3,button4,button5,button6,button7,button8,button9;
	private JButton button10,button11,button12,button13,button14,button15,button16,button17,button18;
	private JButton button19,button20,button21,button22,button23,button24,button25,button26,button27;
	private JButton button28,button29,button30,button31,button44,button46,button47,button48,button39;
	private JButton button41,button51,button42,button43,button40,button53,button54,button45,button38;
	private JButton button49,button50,button52,button32,button33,button34,button35,button36,button37;
	private JButton buttonA,buttonB,buttonC;
	private ArrayList<Point> pointList;
	private ArrayList<JButton> listButton;
	private ArrayList<JButton> selectListButton;
	private int selectListButtonNum = 0;
	
	private int testFlag = 1;

	private Point mousePoint = null;
	private int layerNum = 5 ;
	private int distanceOfEachLayer = 0;
	private int numOfSelect;//操作步数
	private long startTime;
	private long endTime;
	private int voiceNum=0;

	String desktopPath=null;
	String filePath = "data.xls";
	String sheetName = "1";
	// Excel文件sheet页的第一行
	String title[] = { "序号", "开始"
			, "语音1开始", "语音1结束", "语音1结果", "语音1时长"
			, "语音2开始", "语音2结束", "语音2结果", "语音2时长"
			, "语音3开始", "语音3结束", "语音3结果", "语音3时长"
			, "语音4开始", "语音4结束", "语音4结果", "语音4时长"
			, "语音5开始", "语音5结束", "语音5结果", "语音5时长"
			, "结束", "结果","步数"};
	// Excel文件每一列对应的数据
	String titleDate[] = { "buttonName", "testStart", 
			"voice1Start", "voice1End", "voice1Result","voice1Time",
			"voice2Start", "voice2End", "voice2Result","voice2Time",
			"voice3Start", "voice3End", "voice3Result","voice3Time",
			"voice4Start", "voice4End", "voice4Result","voice4Time",
			"voice5Start", "voice5End", "voice5Result","voice5Time",
			"testEnd" , "testResult","numOfSelect"};
	Data user = new Data();
	private boolean voiceNumFlag = true;
	
	public VoiceSelect(){
		client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
		setFilePath();
		initFrame();
		mainEvent();
		
	}
	
	public void setFilePath() {
		String os = System.getProperty("os.name");  
		//当前用户桌面
		File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();		
		desktopPath = desktopDir.getAbsolutePath();
		filePath = desktopPath+"/Desktop/TestDataX.xls";
		if(os.toLowerCase().startsWith("win")){  
			filePath = desktopPath+"\\TestDataX.xls";
		}
	}
	
	public void savaAllData() {

		
		switch (testFlag) {
		case 1:
			sheetName = "方法一";
			break;
		case 2:
			sheetName = "方法二";
			break;
		case 3:
			sheetName = "方法三";
			break;
		}
		
		ExcelManage em = new ExcelManage();
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
		em.writeToExcel(filePath, sheetName, user, titleDate);

	}
	
	// 语音命令的分配
	public void textAnalyze(String textResult) {
		if ( textResult != null) {
			logT = "识别结果："+textResult;
			setTitle();
			if (textResult.contains("一")||textResult.contains("1")) {
				switchPaint(1);
				voiceNumFlag =true;
			}else if (textResult.contains("二")||textResult.contains("2")) {
				switchPaint(2);
				voiceNumFlag =true;
			}else if (textResult.contains("三")||textResult.contains("3")) {
				switchPaint(3);
				voiceNumFlag =true;
			}else if (textResult.contains("四")||textResult.contains("4")) {
				switchPaint(4);
				voiceNumFlag =true;
			}else if (textResult.contains("五")||textResult.contains("5")) {
				switchPaint(5);
				voiceNumFlag =true;
			}else if (textResult.contains("六")||textResult.contains("6")) {
				switchPaint(6);
				voiceNumFlag =true;
			}else if (textResult.contains("七")||textResult.contains("7")) {
				switchPaint(7);
				voiceNumFlag =true;
			}else if (textResult.contains("八")||textResult.contains("8")) {
				switchPaint(8);
				voiceNumFlag =true;
			}else if (textResult.contains("九")||textResult.contains("9")) {
				switchPaint(9);	
				voiceNumFlag =true;
			}else if (textResult.contains("上")) {
				keyOri(3);
				voiceNumFlag =true;
			}else if (textResult.contains("下")) {
				keyOri(4);
				voiceNumFlag =true;
			}else if (textResult.contains("左")) {
				keyOri(1);
				voiceNumFlag =true;
			}else if (textResult.contains("右")) {
				keyOri(2);
				voiceNumFlag =true;
			}
		}else {
			logT = "语音识别出错";
			setTitle();
		}
	}
	
	// 上下左右键盘的操作函数
	public void keyOri(int orientation) {
		if (testFlag == 3) {
			mouseMoveBtn(orientation);
			paintFlag();
			numOfSelect++;
			if (numOfSelect == 1) {
				startTime = System.currentTimeMillis();
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
	}
	
	// 判断按钮位置，移动鼠标指针到按钮中心  
	public void mouseMoveBtn(int orientation) {
		Point myPoint = new Point(mousePoint.x,mousePoint.y);
		SwingUtilities.convertPointFromScreen(myPoint, selectPanel);
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
			SwingUtilities.convertPointToScreen(point, selectPanel);
			logT = "移动到：("+point.x+","+point.y+")";
			mousePoint = point;
			
		}
	}
	
	
	
	public void switchPaint(int keynum) {
		switch (testFlag) {
		case 1:
			paint1(keynum);
			break;
		case 2:
			paint2(keynum);
			break;
		case 3:
			paint3(keynum);
			break;
		}
	}
		
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
				Point myPoint = new Point(mousePoint.x,mousePoint.y);
				SwingUtilities.convertPointFromScreen(myPoint, selectPanel);
				Graphics graphics = selectPanel.getGraphics();
				selectPanel.paint(graphics);
				graphics.setColor(Color.BLUE);
				graphics.fillOval(myPoint.x- 2,myPoint.y- 2, 6, 6);

			}
		}
		keyT = "您选择了:" + keyNum + "号; ";
		setTitle();
	}
	
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
				
				// 需要将distanceOfEachLayer再减一层才能只包含最后一个按钮
				selectButtonTest();
			}
		}
		keyT = "您选择了:" + keyNum + "号; ";
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
				Point myPoint = new Point(mousePoint.x,mousePoint.y);
				SwingUtilities.convertPointFromScreen(myPoint, selectPanel);
				// 让panel上的图形消失
				Graphics graphics = selectPanel.getGraphics();
				selectPanel.paint(graphics);
				graphics.setColor(Color.RED);
				int mX = myPoint.x;
				int mY = myPoint.y;
				graphics.drawLine(mX,mY,mX,mY-1000);
				graphics.drawLine(mX,mY,mX,mY+1000);
				graphics.drawLine(mX,mY,mX-1000,mY);
				graphics.drawLine(mX,mY,mX+1000,mY);
				
			}
		}
		keyT = "您选择了:" + keyNum + "号; ";
		setTitle();
	}
	
	// 改bug加入的函数：解决distanceOfEachLayer不够小的问题
	public void selectButtonTest() {
		
		// 让panel上的图形消失
		Graphics graphics = selectPanel.getGraphics();
		selectPanel.paint(graphics);
		
		distanceOfEachLayer = frameWidth/3/3/3/3;
		// 得出选中的区域
		Point myPoint = new Point(mousePoint.x,mousePoint.y);
		SwingUtilities.convertPointFromScreen(myPoint, selectPanel);
		int mX = myPoint.x;
		int mY = myPoint.y;
		Rectangle rect = new Rectangle();
		rect.setBounds(mX - distanceOfEachLayer*3/2, mY - distanceOfEachLayer*3/2, distanceOfEachLayer*3, distanceOfEachLayer*3);
		
		// 筛选出区域中的按钮
		for (int index=1 ;index < listButton.size() ;index++ ) {
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
		
		graphics.setColor(Color.BLUE);
		//外边框
		graphics.drawLine(mX - distanceOfEachLayer*3/2, mY - distanceOfEachLayer*3/2, mX + distanceOfEachLayer*3/2, mY - distanceOfEachLayer*3/2);
		graphics.drawLine(mX - distanceOfEachLayer*3/2, mY - distanceOfEachLayer*3/2, mX - distanceOfEachLayer*3/2, mY + distanceOfEachLayer*3/2);
		graphics.drawLine(mX + distanceOfEachLayer*3/2, mY + distanceOfEachLayer*3/2, mX + distanceOfEachLayer*3/2, mY - distanceOfEachLayer*3/2);
		graphics.drawLine(mX + distanceOfEachLayer*3/2, mY + distanceOfEachLayer*3/2, mX - distanceOfEachLayer*3/2, mY + distanceOfEachLayer*3/2);

	}
	
	
	
	// 计算下一级鼠标位置
	public void mouseMoveToNext(int keyNumber) {
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
		//moveMouse(point);//移动鼠标
		mousePoint = point;
		
	}
	
	public void mainEvent() {
		mainFrame.requestFocus();
		mainFrame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				switch (e.getKeyCode()) {
				case KeyEvent.VK_SPACE:
					if (selectListButtonNum<54) {
						selectListButton.get(selectListButtonNum).doClick();
					}
					break;
				case KeyEvent.VK_1:
					switchPaint(1);
					break;
				case KeyEvent.VK_2:
					switchPaint(2);
					break;
				case KeyEvent.VK_3:
					switchPaint(3);
					break;
				case KeyEvent.VK_4:
					switchPaint(4);
					break;
				case KeyEvent.VK_5:
					switchPaint(5);
					break;
				case KeyEvent.VK_6:
					switchPaint(6);
					break;
				case KeyEvent.VK_7:
					switchPaint(7);
					break;
				case KeyEvent.VK_8:
					switchPaint(8);
					break;
				case KeyEvent.VK_9:
					switchPaint(9);
					break;
				case KeyEvent.VK_LEFT:
					keyOri(1);
					break;
				case KeyEvent.VK_RIGHT:
					keyOri(2);
					break;
				case KeyEvent.VK_UP:
					keyOri(3);
					break;
				case KeyEvent.VK_DOWN:
					keyOri(4);
					break;
				case KeyEvent.VK_ENTER:
					if (mousePoint!=null) {
						user.setTestEnd(String.valueOf(System.currentTimeMillis()));
						mouseClick();
						savaAllData();
					}
					break;
				case KeyEvent.VK_Z:
					if (voiveFlag) {
						keyT = "正在录音; ";
						logT = "";
						setTitle();
						recStartTime = System.currentTimeMillis();
						if (voiceNumFlag) {
							voiceNum++;
							voiceNumFlag=false;
						}
						setVoiceStartTime(voiceNum);
						capture(); // 调用录音的方法
						voiveFlag = false;
					}
					break;
				case KeyEvent.VK_X:
					if (!stopflag) {
						// 停止录音
						stopflag = true;
						// 调用保存录音的方法
						save();
						recEndTime = System.currentTimeMillis();
						setVoiceEndTime(voiceNum);
						voiveFlag = true;
						keyT = "语音识别结束,耗时:"+(recEndTime - recStartTime)+" 毫秒！;  " ;
						logT = "识别结果为："+textResult;
						setTitle();
						setVoiceTime(voiceNum,String.valueOf(recEndTime - recStartTime));
						setVoiceResult(voiceNum,textResult);
						break;
					}
				}
			}
		});
	}
	
	public void setVoiceStartTime(int num) {
		switch (num) {
		case 1:
			user.setVoice1Start(String.valueOf(System.currentTimeMillis()));
			break;
		case 2:
			user.setVoice2Start(String.valueOf(System.currentTimeMillis()));
			break;
		case 3:
			user.setVoice3Start(String.valueOf(System.currentTimeMillis()));
			break;
		case 4:
			user.setVoice4Start(String.valueOf(System.currentTimeMillis()));
			break;
		case 5:
			user.setVoice5Start(String.valueOf(System.currentTimeMillis()));
			break;
		}
	}
	public void setVoiceEndTime(int num) {
		switch (num) {
		case 1:
			user.setVoice1End(String.valueOf(System.currentTimeMillis()));
			break;
		case 2:
			user.setVoice2End(String.valueOf(System.currentTimeMillis()));
			break;
		case 3:
			user.setVoice3End(String.valueOf(System.currentTimeMillis()));
			break;
		case 4:
			user.setVoice4End(String.valueOf(System.currentTimeMillis()));
			break;
		case 5:
			user.setVoice5End(String.valueOf(System.currentTimeMillis()));
			break;
		}
	}
	public void setVoiceResult(int num,String result) {
		switch (num) {
		case 1:
			user.setVoice1Result(result);
			break;
		case 2:
			user.setVoice2Result(result);
			break;
		case 3:
			user.setVoice3Result(result);
			break;
		case 4:
			user.setVoice4Result(result);
			break;
		case 5:
			user.setVoice5Result(result);
			break;
		}
	}
	
	public void setVoiceTime(int num,String time) {
		switch (num) {
		case 1:
			user.setVoice1Time(time);
			break;
		case 2:
			user.setVoice2Time(time);
			break;
		case 3:
			user.setVoice3Time(time);
			break;
		case 4:
			user.setVoice4Time(time);
			break;
		case 5:
			user.setVoice5Time(time);
			break;
		}
	}
	
	// 实现模拟鼠标点击
	public void mouseClick() {
		if (testFlag == 2) {
			boolean test = true;
			for (int index=1 ;index < listButton.size() ;index++ ) {
				if (listButton.get(index).getBackground( )== Color.LIGHT_GRAY) {
					test = false;
				}
			}
			
			if (test) {
				keyT="";
				logT = "您点中了目标！";
				user.setTestResult(true);
				user.setNumOfSelect(String.valueOf(numOfSelect));
				setTitle();
			}else {
				keyT="";
				logT = "您进行了一次点击！";
				user.setTestResult(false);
				user.setNumOfSelect(String.valueOf(numOfSelect));
				setTitle();
			}
		}else {
			Rectangle rectangle = listButton.get(0).getBounds();
			Point point = new Point(mousePoint.x, mousePoint.y);
			SwingUtilities.convertPointFromScreen(point, selectPanel);
			if (rectangle.contains(point)) {
				keyT="";
				logT = "您点中了目标！";
				user.setTestResult(true);
				user.setNumOfSelect(String.valueOf(numOfSelect));
				setTitle();
			}else {
				keyT="";
				logT = "您进行了一次点击！";
				user.setTestResult(false);
				user.setNumOfSelect(String.valueOf(numOfSelect));
				setTitle();
			}
		}
	}

	
	public void setTitle() {
		mainFrame.setTitle("目标选择实验 : "+ testT + areaT + keyT +logT);
	}
	
	
	public Boolean readPointData(String name) {
		Boolean flag = false;
		String os = System.getProperty("os.name");  
		File file = new File(desktopPath+"/Desktop/PointData.xls");
		if(os.toLowerCase().startsWith("win")){  
			file = new File(desktopPath+"\\PointDatat.txt");
		}
		pointList = new ArrayList<Point>();
        try {
            //1.读取Excel的对象
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(file));
            //2.Excel工作薄对象
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
            //3.Excel工作表对象
            HSSFSheet hssfSheet = hssfWorkbook.getSheet(name);
            if (hssfSheet!=null) {
                //总行数
                int rowLength = hssfSheet.getLastRowNum()+1;
     
                for (int i = 0; i < rowLength; i++) {
                    //获取Excel工作表的行对象
                    HSSFRow hssfRow = hssfSheet.getRow(i);
                    
                    int x=0,y=0;
                    //获取指定单元格
                    HSSFCell hssfCell1 = hssfRow.getCell(0);
                    HSSFCell hssfCell2 = hssfRow.getCell(1);
                    
                    //Excel数据Cell有不同的类型，当我们试图从一个数字类型的Cell读取出一个字符串时就有可能报异常：
                    //Cannot get a STRING value from a NUMERIC cell
                    //将所有的需要读的Cell表格设置为String格式
                    if (hssfCell1 != null) {
                        hssfCell1.setCellType(CellType.STRING);
                    }
                    if (hssfCell2 != null) {
                        hssfCell2.setCellType(CellType.STRING);
                    }
                    x = Integer.parseInt(hssfCell1.getStringCellValue());
                    y = Integer.parseInt(hssfCell2.getStringCellValue());
                    pointList.add(i, new Point(x, y));
                }
                flag = true;
			}else {
				JOptionPane.showMessageDialog(new JFrame().getContentPane(), "不存在"+name+"，请检查PointData文件！", "系统信息", JOptionPane.QUESTION_MESSAGE); 
			}

        } catch (IOException e) {
        	JOptionPane.showMessageDialog(new JFrame().getContentPane(), e, "系统信息", JOptionPane.QUESTION_MESSAGE); 
        }
        return flag;
	}
	
	public void initFrame() {
		selectListButton = new ArrayList<JButton>();
		mainFrame =new JFrame();
		mainFrame.setSize(1350, 950);
		mainFrame.setLocation(10,10);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainContentPane = mainFrame.getContentPane();
		mainContentPane.setLayout(null);
		
		selectPanel = new JPanel();
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
		
		button1 = new JButton("1");
		button1.setBounds(30, 10, 40, 40);
		button1.setBackground(Color.WHITE);
		button1.setOpaque(true);
		button1.setBorder(new LineBorder(Color.BLACK));
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button1")) {
					selectListButtonNum=1;
					objWidth=30;
					objHeight=30;
					areaT="实验1； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum=0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("1");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button1.setBackground(Color.GRAY);
				}
			}
		});
		button2 = new JButton("2");
		button2.setBounds(80, 10, 40, 40);
		button2.setBackground(Color.WHITE);
		button2.setOpaque(true);
		button2.setBorder(new LineBorder(Color.BLACK));
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button3")) {
					selectListButtonNum=2;
					objWidth=30;
					objHeight=30;
					areaT = "实验2； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("2");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button2.setBackground(Color.GRAY);
				}
			}
		});
		button3 = new JButton("3");
		button3.setBounds(130, 10, 40, 40);
		button3.setBackground(Color.WHITE);
		button3.setOpaque(true);
		button3.setBorder(new LineBorder(Color.BLACK));
		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button5")) {
					selectListButtonNum=3;
					objWidth=30;
					objHeight=30;
					areaT = "实验3； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("3");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button3.setBackground(Color.GRAY);
				}
			}
		});
		button4 = new JButton("4");
		button4.setBounds(180, 10, 40, 40);
		button4.setBackground(Color.WHITE);
		button4.setOpaque(true);
		button4.setBorder(new LineBorder(Color.BLACK));
		button4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button7")) {
					selectListButtonNum=4;
					objWidth=60;
					objHeight=60;
					areaT = "实验4； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("4");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button4.setBackground(Color.GRAY);
				}
			}
		});
		button5 = new JButton("5");
		button5.setBounds(30, 60, 40, 40);
		button5.setBackground(Color.WHITE);
		button5.setOpaque(true);
		button5.setBorder(new LineBorder(Color.BLACK));
		button5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button9")) {
					selectListButtonNum=5;
					objWidth=60;
					objHeight=60;
					areaT = "实验5； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("5");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button5.setBackground(Color.GRAY);
				}
			}
		});
		button6 = new JButton("6");
		button6.setBounds(80, 60, 40, 40);
		button6.setBackground(Color.WHITE);
		button6.setOpaque(true);
		button6.setBorder(new LineBorder(Color.BLACK));
		button6.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button11")) {
					selectListButtonNum=6;
					objWidth=60;
					objHeight=60;
					areaT = "实验6； ";
					logT="实验开始";
					setTitle();
					keyT="";
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("6");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button6.setBackground(Color.GRAY);
				}
			}
		});
		button7 = new JButton("7");
		button7.setBounds(130, 60, 40, 40);
		button7.setBackground(Color.WHITE);
		button7.setOpaque(true);
		button7.setBorder(new LineBorder(Color.BLACK));
		button7.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button13")) {
					selectListButtonNum=7;
					objWidth=90;
					objHeight=90;
					areaT = "实验7； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("7");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button7.setBackground(Color.GRAY);
				}
			}
		});
		button8 = new JButton("8");
		button8.setBounds(180, 60, 40, 40);
		button8.setBackground(Color.WHITE);
		button8.setOpaque(true);
		button8.setBorder(new LineBorder(Color.BLACK));
		button8.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button15")) {
					selectListButtonNum=8;
					objWidth=90;
					objHeight=90;
					areaT = "实验8； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("8");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button8.setBackground(Color.GRAY);
				}
			}
		});
		button9 = new JButton("9");
		button9.setBounds(30, 110, 40, 40);
		button9.setBackground(Color.WHITE);
		button9.setOpaque(true);
		button9.setBorder(new LineBorder(Color.BLACK));
		button9.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button17")) {
					selectListButtonNum=9;
					objWidth=90;
					objHeight=90;
					areaT = "实验9； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("9");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button9.setBackground(Color.GRAY);
				}
			}
		});
		
		
		button10 = new JButton("10");
		button10.setBounds(80, 110, 40, 40);
		button10.setBackground(Color.WHITE);
		button10.setOpaque(true);
		button10.setBorder(new LineBorder(Color.BLACK));
		button10.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button2")) {
					selectListButtonNum=10;
					objWidth=30;
					objHeight=30;
					areaT="实验10； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum=0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("10");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button10.setBackground(Color.GRAY);
				}
			}
		});
		button11 = new JButton("11");
		button11.setBounds(130, 110, 40, 40);
		button11.setBackground(Color.WHITE);
		button11.setOpaque(true);
		button11.setBorder(new LineBorder(Color.BLACK));
		button11.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button4")) {
					selectListButtonNum=11;
					objWidth=30;
					objHeight=30;
					areaT = "实验11； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("11");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button11.setBackground(Color.GRAY);
				}
			}
		});
		button12 = new JButton("12");
		button12.setBounds(180, 110, 40, 40);
		button12.setBackground(Color.WHITE);
		button12.setOpaque(true);
		button12.setBorder(new LineBorder(Color.BLACK));
		button12.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button6")) {
					selectListButtonNum=12;
					objWidth=30;
					objHeight=30;
					areaT = "实验12； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("12");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button12.setBackground(Color.GRAY);
				}
			}
		});
		button13 = new JButton("13");
		button13.setBounds(30, 160, 40, 40);
		button13.setBackground(Color.WHITE);
		button13.setOpaque(true);
		button13.setBorder(new LineBorder(Color.BLACK));
		button13.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button8")) {
					selectListButtonNum=13;
					objWidth=60;
					objHeight=60;
					areaT = "实验13； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("13");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button13.setBackground(Color.GRAY);
				}
			}
		});
		button14 = new JButton("14");
		button14.setBounds(80, 160, 40, 40);
		button14.setBackground(Color.WHITE);
		button14.setOpaque(true);
		button14.setBorder(new LineBorder(Color.BLACK));
		button14.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button10")) {
					selectListButtonNum=14;
					objWidth=60;
					objHeight=60;
					areaT = "实验14； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("14");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button14.setBackground(Color.GRAY);
				}
			}
		});
		button15 = new JButton("15");
		button15.setBounds(130, 160, 40, 40);
		button15.setBackground(Color.WHITE);
		button15.setOpaque(true);
		button15.setBorder(new LineBorder(Color.BLACK));
		button15.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button12")) {
					selectListButtonNum=15;
					objWidth=60;
					objHeight=60;
					areaT = "实验15； ";
					logT="实验开始";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("15");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button15.setBackground(Color.GRAY);
				}
			}
		});
		button16 = new JButton("16");
		button16.setBounds(180, 160, 40, 40);
		button16.setBackground(Color.WHITE);
		button16.setOpaque(true);
		button16.setBorder(new LineBorder(Color.BLACK));
		button16.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button14")) {
					selectListButtonNum=16;
					objWidth=90;
					objHeight=90;
					areaT = "实验16； ";
					logT="实验开始";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("16");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button16.setBackground(Color.GRAY);
				}
			}
		});
		button17 = new JButton("17");
		button17.setBounds(30, 210, 40, 40);
		button17.setBackground(Color.WHITE);
		button17.setOpaque(true);
		button17.setBorder(new LineBorder(Color.BLACK));
		button17.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button16")) {
					selectListButtonNum=17;
					objWidth=90;
					objHeight=90;
					areaT = "实验17； ";
					logT="实验开始";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("17");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button17.setBackground(Color.GRAY);
				}
			}
		});
		button18 = new JButton("18");
		button18.setBounds(80, 210, 40, 40);
		button18.setBackground(Color.WHITE);
		button18.setOpaque(true);
		button18.setBorder(new LineBorder(Color.BLACK));
		button18.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button18")) {
					selectListButtonNum=18;
					objWidth=90;
					objHeight=90;
					areaT = "实验18； ";
					logT="实验开始";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("18");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button18.setBackground(Color.GRAY);
				}
			}
		});
		
		
		button19 = new JButton("19");
		button19.setBounds(130, 210, 40, 40);
		button19.setBackground(Color.WHITE);
		button19.setOpaque(true);
		button19.setBorder(new LineBorder(Color.BLACK));
		button19.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button17")) {
					selectListButtonNum=19;
					objWidth=90;
					objHeight=90;
					areaT="实验19； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum=0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("19");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button19.setBackground(Color.GRAY);
				}
			}
		});
		button20 = new JButton("20");
		button20.setBounds(180, 210, 40, 40);
		button20.setBackground(Color.WHITE);
		button20.setOpaque(true);
		button20.setBorder(new LineBorder(Color.BLACK));
		button20.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button15")) {
					selectListButtonNum=20;
					objWidth=90;
					objHeight=90;
					areaT = "实验20； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("20");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button20.setBackground(Color.GRAY);
				}
			}
		});
		button21 = new JButton("21");
		button21.setBounds(30, 260, 40, 40);
		button21.setBackground(Color.WHITE);
		button21.setOpaque(true);
		button21.setBorder(new LineBorder(Color.BLACK));
		button21.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button13")) {
					selectListButtonNum=21;
					objWidth=90;
					objHeight=90;
					areaT = "实验21； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("21");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button21.setBackground(Color.GRAY);
				}
			}
		});
		button22 = new JButton("22");
		button22.setBounds(80, 260, 40, 40);
		button22.setBackground(Color.WHITE);
		button22.setOpaque(true);
		button22.setBorder(new LineBorder(Color.BLACK));
		button22.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button11")) {
					selectListButtonNum=22;
					objWidth=60;
					objHeight=60;
					areaT = "实验22； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("22");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button22.setBackground(Color.GRAY);
				}
			}
		});
		button23 = new JButton("23");
		button23.setBounds(130, 260, 40, 40);
		button23.setBackground(Color.WHITE);
		button23.setOpaque(true);
		button23.setBorder(new LineBorder(Color.BLACK));
		button23.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button9")) {
					selectListButtonNum=23;
					objWidth=60;
					objHeight=60;
					areaT = "实验23； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("23");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button23.setBackground(Color.GRAY);
				}
			}
		});
		button24 = new JButton("24");
		button24.setBounds(180, 260, 40, 40);
		button24.setBackground(Color.WHITE);
		button24.setOpaque(true);
		button24.setBorder(new LineBorder(Color.BLACK));
		button24.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button7")) {
					selectListButtonNum=24;
					objWidth=60;
					objHeight=60;
					areaT = "实验24； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("24");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button24.setBackground(Color.GRAY);
				}
			}
		});
		button25 = new JButton("25");
		button25.setBounds(30, 310, 40, 40);
		button25.setBackground(Color.WHITE);
		button25.setOpaque(true);
		button25.setBorder(new LineBorder(Color.BLACK));
		button25.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button5")) {
					selectListButtonNum=25;
					objWidth=30;
					objHeight=30;
					areaT = "实验25； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("25");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button25.setBackground(Color.GRAY);
				}
			}
		});
		button26 = new JButton("26");
		button26.setBounds(80, 310, 40, 40);
		button26.setBackground(Color.WHITE);
		button26.setOpaque(true);
		button26.setBorder(new LineBorder(Color.BLACK));
		button26.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button3")) {
					selectListButtonNum=26;
					objWidth=30;
					objHeight=30;
					areaT = "实验26； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("26");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button26.setBackground(Color.GRAY);
				}
			}
		});
		button27 = new JButton("27");
		button27.setBounds(130, 310, 40, 40);
		button27.setBackground(Color.WHITE);
		button27.setOpaque(true);
		button27.setBorder(new LineBorder(Color.BLACK));
		button27.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button1")) {
					selectListButtonNum=27;
					objWidth=30;
					objHeight=30;
					areaT = "实验27； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("27");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button27.setBackground(Color.GRAY);
				}
			}
		});
		button28 = new JButton("28");
		button28.setBounds(180, 310, 40, 40);
		button28.setBackground(Color.WHITE);
		button28.setOpaque(true);
		button28.setBorder(new LineBorder(Color.BLACK));
		button28.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button18")) {
					selectListButtonNum=28;
					objWidth=90;
					objHeight=90;
					areaT = "实验28； ";
					logT="实验开始";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("28");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button28.setBackground(Color.GRAY);
				}
			}
		});
		button29 = new JButton("29");
		button29.setBounds(30, 360, 40, 40);
		button29.setBackground(Color.WHITE);
		button29.setOpaque(true);
		button29.setBorder(new LineBorder(Color.BLACK));
		button29.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button16")) {
					selectListButtonNum=29;
					objWidth=90;
					objHeight=90;
					areaT = "实验29； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("29");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button29.setBackground(Color.GRAY);
				}
			}
		});
		
		button30 = new JButton("30");
		button30.setBounds(80, 360, 40, 40);
		button30.setBackground(Color.WHITE);
		button30.setOpaque(true);
		button30.setBorder(new LineBorder(Color.BLACK));
		button30.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button14")) {
					selectListButtonNum=30;
					objWidth=90;
					objHeight=90;
					areaT="实验30； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum=0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("30");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button30.setBackground(Color.GRAY);
				}
			}
		});
		button31 = new JButton("31");
		button31.setBounds(130, 360, 40, 40);
		button31.setBackground(Color.WHITE);
		button31.setOpaque(true);
		button31.setBorder(new LineBorder(Color.BLACK));
		button31.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button12")) {
					selectListButtonNum=31;
					objWidth=60;
					objHeight=60;
					areaT = "实验31； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("31");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button31.setBackground(Color.GRAY);
				}
			}
		});
		button32 = new JButton("32");
		button32.setBounds(180, 360, 40, 40);
		button32.setBackground(Color.WHITE);
		button32.setOpaque(true);
		button32.setBorder(new LineBorder(Color.BLACK));
		button32.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button10")) {
					selectListButtonNum=32;
					objWidth=60;
					objHeight=60;
					areaT = "实验32； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("32");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button32.setBackground(Color.GRAY);
				}
			}
		});
		button33 = new JButton("33");
		button33.setBounds(30, 410, 40, 40);
		button33.setBackground(Color.WHITE);
		button33.setOpaque(true);
		button33.setBorder(new LineBorder(Color.BLACK));
		button33.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button8")) {
					selectListButtonNum=33;
					objWidth=60;
					objHeight=60;
					areaT = "实验33； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("33");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button33.setBackground(Color.GRAY);
				}
			}
		});
		button34 = new JButton("34");
		button34.setBounds(80, 410, 40, 40);
		button34.setBackground(Color.WHITE);
		button34.setOpaque(true);
		button34.setBorder(new LineBorder(Color.BLACK));
		button34.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button6")) {
					selectListButtonNum=34;
					objWidth=30;
					objHeight=30;
					areaT = "实验34； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("34");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button34.setBackground(Color.GRAY);
				}
			}
		});
		button35 = new JButton("35");
		button35.setBounds(130, 410, 40, 40);
		button35.setBackground(Color.WHITE);
		button35.setOpaque(true);
		button35.setBorder(new LineBorder(Color.BLACK));
		button35.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button4")) {
					selectListButtonNum=35;
					objWidth=30;
					objHeight=30;
					areaT = "实验35； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("35");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button35.setBackground(Color.GRAY);
				}
			}
		});
		button36 = new JButton("36");
		button36.setBounds(180, 410, 40, 40);
		button36.setBackground(Color.WHITE);
		button36.setOpaque(true);
		button36.setBorder(new LineBorder(Color.BLACK));
		button36.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button2")) {
					selectListButtonNum=36;
					objWidth=30;
					objHeight=30;
					areaT = "实验36； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("36");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button36.setBackground(Color.GRAY);
				}
			}
		});
		button37 = new JButton("37");
		button37.setBounds(30, 460, 40, 40);
		button37.setBackground(Color.WHITE);
		button37.setOpaque(true);
		button37.setBorder(new LineBorder(Color.BLACK));
		button37.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button11")) {
					selectListButtonNum=37;
					objWidth=60;
					objHeight=60;
					areaT = "实验37； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("37");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button37.setBackground(Color.GRAY);
				}
			}
		});
		button38 = new JButton("38");
		button38.setBounds(80, 460, 40, 40);
		button38.setBackground(Color.WHITE);
		button38.setOpaque(true);
		button38.setBorder(new LineBorder(Color.BLACK));
		button38.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button18")) {
					selectListButtonNum=38;
					objWidth=90;
					objHeight=90;
					areaT = "实验38； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("38");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button38.setBackground(Color.GRAY);
				}
			}
		});
		
		
		button39 = new JButton("39");
		button39.setBounds(130, 460, 40, 40);
		button39.setBackground(Color.WHITE);
		button39.setOpaque(true);
		button39.setBorder(new LineBorder(Color.BLACK));
		button39.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button1")) {
					selectListButtonNum=39;
					objWidth=30;
					objHeight=30;
					areaT="实验39； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum=0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("39");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button39.setBackground(Color.GRAY);
				}
			}
		});
		button40 = new JButton("40");
		button40.setBounds(180, 460, 40, 40);
		button40.setBackground(Color.WHITE);
		button40.setOpaque(true);
		button40.setBorder(new LineBorder(Color.BLACK));
		button40.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button2")) {
					selectListButtonNum=40;
					objWidth=30;
					objHeight=30;
					areaT = "实验40； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("40");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button40.setBackground(Color.GRAY);
				}
			}
		});
		button41 = new JButton("41");
		button41.setBounds(30, 510, 40, 40);
		button41.setBackground(Color.WHITE);
		button41.setOpaque(true);
		button41.setBorder(new LineBorder(Color.BLACK));
		button41.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button3")) {
					selectListButtonNum=41;
					objWidth=30;
					objHeight=30;
					areaT = "实验41； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("41");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button41.setBackground(Color.GRAY);
				}
			}
		});
		button42 = new JButton("42");
		button42.setBounds(80, 510, 40, 40);
		button42.setBackground(Color.WHITE);
		button42.setOpaque(true);
		button42.setBorder(new LineBorder(Color.BLACK));
		button42.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button4")) {
					selectListButtonNum=42;
					objWidth=30;
					objHeight=30;
					areaT = "实验42； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("42");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button42.setBackground(Color.GRAY);
				}
			}
		});
		button43 = new JButton("43");
		button43.setBounds(130, 510, 40, 40);
		button43.setBackground(Color.WHITE);
		button43.setOpaque(true);
		button43.setBorder(new LineBorder(Color.BLACK));
		button43.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button5")) {
					selectListButtonNum=43;
					objWidth=30;
					objHeight=30;
					areaT = "实验43； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("43");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button43.setBackground(Color.GRAY);
				}
			}
		});
		button44 = new JButton("44");
		button44.setBounds(180, 510, 40, 40);
		button44.setBackground(Color.WHITE);
		button44.setOpaque(true);
		button44.setBorder(new LineBorder(Color.BLACK));
		button44.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button6")) {
					selectListButtonNum=44;
					objWidth=30;
					objHeight=30;
					areaT = "实验44； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("44");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button44.setBackground(Color.GRAY);
				}
			}
		});
		button45 = new JButton("45");
		button45.setBounds(30, 560, 40, 40);
		button45.setBackground(Color.WHITE);
		button45.setOpaque(true);
		button45.setBorder(new LineBorder(Color.BLACK));
		button45.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button7")) {
					selectListButtonNum=45;
					objWidth=60;
					objHeight=60;
					areaT = "实验45； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("45");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button45.setBackground(Color.GRAY);
				}
			}
		});
		button46 = new JButton("46");
		button46.setBounds(80, 560, 40, 40);
		button46.setBackground(Color.WHITE);
		button46.setOpaque(true);
		button46.setBorder(new LineBorder(Color.BLACK));
		button46.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button8")) {
					selectListButtonNum=46;
					objWidth=60;
					objHeight=60;
					areaT = "实验46； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("46");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button46.setBackground(Color.GRAY);
				}
			}
		});
		button47 = new JButton("47");
		button47.setBounds(130, 560, 40, 40);
		button47.setBackground(Color.WHITE);
		button47.setOpaque(true);
		button47.setBorder(new LineBorder(Color.BLACK));
		button47.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button9")) {
					selectListButtonNum=47;
					objWidth=60;
					objHeight=60;
					areaT = "实验47； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("47");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button47.setBackground(Color.GRAY);
				}
			}
		});
		
		
		button48 = new JButton("48");
		button48.setBounds(180, 560, 40, 40);
		button48.setBackground(Color.WHITE);
		button48.setOpaque(true);
		button48.setBorder(new LineBorder(Color.BLACK));
		button48.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button10")) {
					selectListButtonNum=48;
					objWidth=60;
					objHeight=60;
					areaT="实验48； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum=0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("48");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button48.setBackground(Color.GRAY);
				}
			}
		});
		button49 = new JButton("49");
		button49.setBounds(30, 610, 40, 40);
		button49.setBackground(Color.WHITE);
		button49.setOpaque(true);
		button49.setBorder(new LineBorder(Color.BLACK));
		button49.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button12")) {
					selectListButtonNum=49;
					objWidth=60;
					objHeight=60;
					areaT = "实验49； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("49");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button49.setBackground(Color.GRAY);
				}
			}
		});
		button50 = new JButton("50");
		button50.setBounds(80, 610, 40, 40);
		button50.setBackground(Color.WHITE);
		button50.setOpaque(true);
		button50.setBorder(new LineBorder(Color.BLACK));
		button50.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button13")) {
					selectListButtonNum=50;
					objWidth=90;
					objHeight=90;
					areaT = "实验50； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("50");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button50.setBackground(Color.GRAY);
				}
			}
		});
		button51 = new JButton("51");
		button51.setBounds(130, 610, 40, 40);
		button51.setBackground(Color.WHITE);
		button51.setOpaque(true);
		button51.setBorder(new LineBorder(Color.BLACK));
		button51.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button14")) {
					selectListButtonNum=51;
					objWidth=90;
					objHeight=90;
					areaT = "实验51； ";
					keyT="";
					logT="实验开始";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("51");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button51.setBackground(Color.GRAY);
				}
			}
		});
		button52 = new JButton("52");
		button52.setBounds(180, 610, 40, 40);
		button52.setBackground(Color.WHITE);
		button52.setOpaque(true);
		button52.setBorder(new LineBorder(Color.BLACK));
		button52.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button15")) {
					selectListButtonNum=52;
					objWidth=90;
					objHeight=90;
					areaT = "实验52； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("52");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button52.setBackground(Color.GRAY);
				}
			}
		});
		button53 = new JButton("53");
		button53.setBounds(30, 660, 40, 40);
		button53.setBackground(Color.WHITE);
		button53.setOpaque(true);
		button53.setBorder(new LineBorder(Color.BLACK));
		button53.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button16")) {
					selectListButtonNum=53;
					objWidth=90;
					objHeight=90;
					areaT = "实验53； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("53");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button53.setBackground(Color.GRAY);
				}
			}
		});
		button54 = new JButton("54");
		button54.setBounds(80, 660, 40, 40);
		button54.setBackground(Color.WHITE);
		button54.setOpaque(true);
		button54.setBorder(new LineBorder(Color.BLACK));
		button54.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button17")) {
					selectListButtonNum=54;
					objWidth=90;
					objHeight=90;
					areaT = "实验54； ";
					logT="实验开始";
					keyT="";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("54");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
					button54.setBackground(Color.GRAY);
				}
			}
		});
		
		
		
		mainPanel.add(button1);
		mainPanel.add(button2);
		mainPanel.add(button3);
		mainPanel.add(button4);
		mainPanel.add(button5);
		mainPanel.add(button6);
		mainPanel.add(button7);
		mainPanel.add(button8);
		mainPanel.add(button9);
		
		mainPanel.add(button10);
		mainPanel.add(button11);
		mainPanel.add(button12);
		mainPanel.add(button13);
		mainPanel.add(button14);
		mainPanel.add(button15);
		mainPanel.add(button16);
		mainPanel.add(button17);
		mainPanel.add(button18);
		
		mainPanel.add(button19);
		mainPanel.add(button20);
		mainPanel.add(button21);
		mainPanel.add(button22);
		mainPanel.add(button23);
		mainPanel.add(button24);
		mainPanel.add(button25);
		mainPanel.add(button26);
		mainPanel.add(button27);
		
		mainPanel.add(button28);
		mainPanel.add(button29);
		mainPanel.add(button30);
		mainPanel.add(button31);
		mainPanel.add(button32);
		mainPanel.add(button33);
		mainPanel.add(button34);
		mainPanel.add(button35);
		mainPanel.add(button36);
		
		mainPanel.add(button37);
		mainPanel.add(button38);
		mainPanel.add(button39);
		mainPanel.add(button40);
		mainPanel.add(button41);
		mainPanel.add(button42);
		mainPanel.add(button43);
		mainPanel.add(button44);
		mainPanel.add(button45);
		
		mainPanel.add(button46);
		mainPanel.add(button47);
		mainPanel.add(button48);
		mainPanel.add(button49);
		mainPanel.add(button50);
		mainPanel.add(button51);
		mainPanel.add(button52);
		mainPanel.add(button53);
		mainPanel.add(button54);
		
		
		buttonA = new JButton("方法一");
		buttonA.setBounds(240, 60, 100, 40);
		buttonA.setBackground(Color.GRAY);
		buttonA.setOpaque(true);
		buttonA.setBorder(new LineBorder(Color.BLACK));
		buttonA.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				testFlag = 1;
				selectListButtonNum=0;
				testT="方法一； ";areaT="";keyT="";logT="";
				setTitle();
				
				selectPanel.removeAll();
				selectPanel.repaint(); 
				
				buttonA.setBackground(Color.GRAY);
				buttonB.setBackground(Color.WHITE);
				buttonC.setBackground(Color.WHITE);
				
				button1.setBackground(Color.WHITE);
				button2.setBackground(Color.WHITE);
				button3.setBackground(Color.WHITE);
				button4.setBackground(Color.WHITE);
				button5.setBackground(Color.WHITE);
				button6.setBackground(Color.WHITE);
				button7.setBackground(Color.WHITE);
				button8.setBackground(Color.WHITE);
				button9.setBackground(Color.WHITE);
				button10.setBackground(Color.WHITE);
				button11.setBackground(Color.WHITE);
				button12.setBackground(Color.WHITE);
				button13.setBackground(Color.WHITE);
				button14.setBackground(Color.WHITE);
				button15.setBackground(Color.WHITE);
				button16.setBackground(Color.WHITE);
				button17.setBackground(Color.WHITE);
				button18.setBackground(Color.WHITE);
				button19.setBackground(Color.WHITE);
				button20.setBackground(Color.WHITE);
				button21.setBackground(Color.WHITE);
				button22.setBackground(Color.WHITE);
				button23.setBackground(Color.WHITE);
				button24.setBackground(Color.WHITE);
				button25.setBackground(Color.WHITE);
				button26.setBackground(Color.WHITE);
				button27.setBackground(Color.WHITE);
				button28.setBackground(Color.WHITE);
				button29.setBackground(Color.WHITE);
				button30.setBackground(Color.WHITE);
				button31.setBackground(Color.WHITE);
				button32.setBackground(Color.WHITE);
				button33.setBackground(Color.WHITE);
				button34.setBackground(Color.WHITE);
				button35.setBackground(Color.WHITE);
				button36.setBackground(Color.WHITE);
				button37.setBackground(Color.WHITE);
				button38.setBackground(Color.WHITE);
				button39.setBackground(Color.WHITE);
				button40.setBackground(Color.WHITE);
				button41.setBackground(Color.WHITE);
				button42.setBackground(Color.WHITE);
				button43.setBackground(Color.WHITE);
				button44.setBackground(Color.WHITE);
				button45.setBackground(Color.WHITE);
				button46.setBackground(Color.WHITE);
				button47.setBackground(Color.WHITE);
				button48.setBackground(Color.WHITE);
				button49.setBackground(Color.WHITE);
				button50.setBackground(Color.WHITE);
				button51.setBackground(Color.WHITE);
				button52.setBackground(Color.WHITE);
				button53.setBackground(Color.WHITE);
				button54.setBackground(Color.WHITE);
				mainFrame.requestFocus();
			}
		});
		buttonB = new JButton("方法二");
		buttonB.setBounds(240, 120, 100, 40);
		buttonB.setBackground(Color.WHITE);
		buttonB.setOpaque(true);
		buttonB.setBorder(new LineBorder(Color.BLACK));
		buttonB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				testFlag = 2;
				selectListButtonNum=0;
				selectPanel.removeAll();
				selectPanel.repaint(); 
				testT="方法二； ";areaT="";keyT="";logT="";
				setTitle();
				
				buttonA.setBackground(Color.WHITE);
				buttonB.setBackground(Color.GRAY);
				buttonC.setBackground(Color.WHITE);
				
				button1.setBackground(Color.WHITE);
				button2.setBackground(Color.WHITE);
				button3.setBackground(Color.WHITE);
				button4.setBackground(Color.WHITE);
				button5.setBackground(Color.WHITE);
				button6.setBackground(Color.WHITE);
				button7.setBackground(Color.WHITE);
				button8.setBackground(Color.WHITE);
				button9.setBackground(Color.WHITE);
				button10.setBackground(Color.WHITE);
				button11.setBackground(Color.WHITE);
				button12.setBackground(Color.WHITE);
				button13.setBackground(Color.WHITE);
				button14.setBackground(Color.WHITE);
				button15.setBackground(Color.WHITE);
				button16.setBackground(Color.WHITE);
				button17.setBackground(Color.WHITE);
				button18.setBackground(Color.WHITE);
				button19.setBackground(Color.WHITE);
				button20.setBackground(Color.WHITE);
				button21.setBackground(Color.WHITE);
				button22.setBackground(Color.WHITE);
				button23.setBackground(Color.WHITE);
				button24.setBackground(Color.WHITE);
				button25.setBackground(Color.WHITE);
				button26.setBackground(Color.WHITE);
				button27.setBackground(Color.WHITE);
				button28.setBackground(Color.WHITE);
				button29.setBackground(Color.WHITE);
				button30.setBackground(Color.WHITE);
				button31.setBackground(Color.WHITE);
				button32.setBackground(Color.WHITE);
				button33.setBackground(Color.WHITE);
				button34.setBackground(Color.WHITE);
				button35.setBackground(Color.WHITE);
				button36.setBackground(Color.WHITE);
				button37.setBackground(Color.WHITE);
				button38.setBackground(Color.WHITE);
				button39.setBackground(Color.WHITE);
				button40.setBackground(Color.WHITE);
				button41.setBackground(Color.WHITE);
				button42.setBackground(Color.WHITE);
				button43.setBackground(Color.WHITE);
				button44.setBackground(Color.WHITE);
				button45.setBackground(Color.WHITE);
				button46.setBackground(Color.WHITE);
				button47.setBackground(Color.WHITE);
				button48.setBackground(Color.WHITE);
				button49.setBackground(Color.WHITE);
				button50.setBackground(Color.WHITE);
				button51.setBackground(Color.WHITE);
				button52.setBackground(Color.WHITE);
				button53.setBackground(Color.WHITE);
				button54.setBackground(Color.WHITE);
				mainFrame.requestFocus();
			}
		});
		buttonC = new JButton("方法三");
		buttonC.setBounds(240, 180, 100, 40);
		buttonC.setBackground(Color.WHITE);
		buttonC.setOpaque(true);
		buttonC.setBorder(new LineBorder(Color.BLACK));
		buttonC.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				testFlag = 3;
				selectListButtonNum = 0;
				selectPanel.removeAll();
				selectPanel.repaint(); 
				testT="方法三； ";areaT="";keyT="";logT="";
				setTitle();
				buttonA.setBackground(Color.WHITE);
				buttonB.setBackground(Color.WHITE);
				buttonC.setBackground(Color.GRAY);
				
				button1.setBackground(Color.WHITE);
				button2.setBackground(Color.WHITE);
				button3.setBackground(Color.WHITE);
				button4.setBackground(Color.WHITE);
				button5.setBackground(Color.WHITE);
				button6.setBackground(Color.WHITE);
				button7.setBackground(Color.WHITE);
				button8.setBackground(Color.WHITE);
				button9.setBackground(Color.WHITE);
				button10.setBackground(Color.WHITE);
				button11.setBackground(Color.WHITE);
				button12.setBackground(Color.WHITE);
				button13.setBackground(Color.WHITE);
				button14.setBackground(Color.WHITE);
				button15.setBackground(Color.WHITE);
				button16.setBackground(Color.WHITE);
				button17.setBackground(Color.WHITE);
				button18.setBackground(Color.WHITE);
				button19.setBackground(Color.WHITE);
				button20.setBackground(Color.WHITE);
				button21.setBackground(Color.WHITE);
				button22.setBackground(Color.WHITE);
				button23.setBackground(Color.WHITE);
				button24.setBackground(Color.WHITE);
				button25.setBackground(Color.WHITE);
				button26.setBackground(Color.WHITE);
				button27.setBackground(Color.WHITE);
				button28.setBackground(Color.WHITE);
				button29.setBackground(Color.WHITE);
				button30.setBackground(Color.WHITE);
				button31.setBackground(Color.WHITE);
				button32.setBackground(Color.WHITE);
				button33.setBackground(Color.WHITE);
				button34.setBackground(Color.WHITE);
				button35.setBackground(Color.WHITE);
				button36.setBackground(Color.WHITE);
				button37.setBackground(Color.WHITE);
				button38.setBackground(Color.WHITE);
				button39.setBackground(Color.WHITE);
				button40.setBackground(Color.WHITE);
				button41.setBackground(Color.WHITE);
				button42.setBackground(Color.WHITE);
				button43.setBackground(Color.WHITE);
				button44.setBackground(Color.WHITE);
				button45.setBackground(Color.WHITE);
				button46.setBackground(Color.WHITE);
				button47.setBackground(Color.WHITE);
				button48.setBackground(Color.WHITE);
				button49.setBackground(Color.WHITE);
				button50.setBackground(Color.WHITE);
				button51.setBackground(Color.WHITE);
				button52.setBackground(Color.WHITE);
				button53.setBackground(Color.WHITE);
				button54.setBackground(Color.WHITE);
				mainFrame.requestFocus();
			}
		});
		
		mainPanel.add(buttonA);
		mainPanel.add(buttonB);
		mainPanel.add(buttonC);
		
		String text = "使用说明：\n    第一步，选择方法（默认为方法一）；\n    第二步，选择实验号码（1-54）；\n    第三步，根据所选方法进行目标选择。\n当所选方法实验全部测完时请重复第一步";
		JTextArea textPane = new JTextArea(text);
		textPane.setBounds(30, 730, 310, 150);
		textPane.setOpaque(false);
		textPane.setEditable(false);
		mainPanel.add(textPane);
		
		mainContentPane.add(selectPanel);
		mainContentPane.add(mainPanel);
		
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
		
		testT="方法一； ";
		setTitle();
		
		selectListButton.add(0,button1);
		selectListButton.add(1,button2);
		selectListButton.add(2,button3);
		selectListButton.add(3,button4);
		selectListButton.add(4,button5);
		selectListButton.add(5,button6);
		selectListButton.add(6,button7);
		selectListButton.add(7,button8);
		selectListButton.add(8,button9);
		selectListButton.add(9,button10);
		selectListButton.add(10,button11);
		selectListButton.add(11,button12);
		selectListButton.add(12,button13);
		selectListButton.add(13,button14);
		selectListButton.add(14,button15);
		selectListButton.add(15,button16);
		selectListButton.add(16,button17);
		selectListButton.add(17,button18);
		selectListButton.add(18,button19);
		selectListButton.add(19,button20);
		selectListButton.add(20,button21);
		selectListButton.add(21,button22);
		selectListButton.add(22,button23);
		selectListButton.add(23,button24);
		selectListButton.add(24,button25);
		selectListButton.add(25,button26);
		selectListButton.add(26,button27);
		selectListButton.add(27,button28);
		selectListButton.add(28,button29);
		selectListButton.add(29,button30);
		selectListButton.add(30,button31);
		selectListButton.add(31,button32);
		selectListButton.add(32,button33);
		selectListButton.add(33,button34);
		selectListButton.add(34,button35);
		selectListButton.add(35,button36);
		selectListButton.add(36,button37);
		selectListButton.add(37,button38);
		selectListButton.add(38,button39);
		selectListButton.add(39,button40);
		selectListButton.add(40,button41);
		selectListButton.add(41,button42);
		selectListButton.add(42,button43);
		selectListButton.add(43,button44);
		selectListButton.add(44,button45);
		selectListButton.add(45,button46);
		selectListButton.add(46,button47);
		selectListButton.add(47,button48);
		selectListButton.add(48,button49);
		selectListButton.add(49,button50);
		selectListButton.add(50,button51);
		selectListButton.add(51,button52);
		selectListButton.add(52,button53);
		selectListButton.add(53,button54);

		
	}
	
	public void enter() {
		mousePoint =  new Point(frameWidth/2,frameWidth/2);//计算出frame中心坐标
		SwingUtilities.convertPointToScreen(mousePoint, selectPanel);//转化成frame的坐标系

		layerNum = 0;
		setLayerDistance();
		
		switch (testFlag) {
		case 1:
			paintNine();//画标记
			break;
		case 2:
			paintNine2();//画标记
			selectButton();
			break;
		case 3:
			paintFlag();//画标记
			break;
		}
		
		numOfSelect = 0;
		setTitle();
	}
	
	
	public void paintFlag() {
		Point myPoint = new Point(mousePoint.x,mousePoint.y);
		SwingUtilities.convertPointFromScreen(myPoint, selectPanel);
		int mX = myPoint.x;
		int mY = myPoint.y;
		Graphics graphics = selectPanel.getGraphics();
		selectPanel.paint(graphics);
		
		if(layerNum<3) {
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

		graphics.setColor(Color.RED);
		graphics.drawLine(mX,mY,mX,mY-1000);
		graphics.drawLine(mX,mY,mX,mY+1000);
		graphics.drawLine(mX,mY,mX-1000,mY);
		graphics.drawLine(mX,mY,mX+1000,mY);
		
	}
	
	// 实现选中区域内的按钮
	public void selectButton() {
		// 得出选中的区域
		Point myPoint = new Point(mousePoint.x,mousePoint.y);
		SwingUtilities.convertPointFromScreen(myPoint, selectPanel);
		int mX = myPoint.x;
		int mY = myPoint.y;
		Rectangle rect = new Rectangle();
		rect.setBounds(mX - distanceOfEachLayer*3/2, mY - distanceOfEachLayer*3/2, distanceOfEachLayer*3, distanceOfEachLayer*3);
		
		// 筛选出区域中的按钮
		for (int index=1 ;index < listButton.size() ;index++ ) {
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
	
	// 依据mousePoint和distanceOfEachLayer画出九个区域的中心点（每个区域带边框）
	public void paintNine2() {
		Point myPoint = new Point(mousePoint.x,mousePoint.y);
		SwingUtilities.convertPointFromScreen(myPoint, selectPanel);
		int mX = myPoint.x;
		int mY = myPoint.y;
		Graphics graphics = selectPanel.getGraphics();
		selectPanel.paint(graphics);
		
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
	
	
	// 计算每层(0-3)深度所需的距离
	public void setLayerDistance() {
		switch (layerNum) {
		case 0:
			distanceOfEachLayer = frameWidth/3;
			break;
		case 1:
			distanceOfEachLayer = frameWidth/3/3;
			break;
		case 2:
			distanceOfEachLayer = frameWidth/3/3/3;
			break;
		}
	}
	
	// 依据mousePoint和distanceOfEachLayer画出九个区域的中心点
	public void paintNine() {
		Point myPoint = new Point(mousePoint.x,mousePoint.y);
		SwingUtilities.convertPointFromScreen(myPoint, selectPanel);
		int mX = myPoint.x;
		int mY = myPoint.y;
		Graphics graphics = selectPanel.getGraphics();
		selectPanel.paint(graphics);
		
		int size = 6;
		graphics.setColor(Color.BLUE);
		graphics.fillOval(mX- 2,mY- 2, size, size);
		graphics.drawString("5",mX , mY );
		
		graphics.setColor(Color.BLACK);
		graphics.fillOval(mX - distanceOfEachLayer - 2, mY - distanceOfEachLayer - 2 , size, size);
		graphics.fillOval(mX - 2, mY - distanceOfEachLayer - 2 , size, size);
		graphics.fillOval(mX + distanceOfEachLayer - 2, mY - distanceOfEachLayer- 2, size, size);
		graphics.fillOval(mX - distanceOfEachLayer - 2, mY - 2, size,size);
		graphics.fillOval(mX + distanceOfEachLayer - 2, mY - 2, size, size);
		graphics.fillOval(mX - distanceOfEachLayer - 2, mY + distanceOfEachLayer- 2, size, size);
		graphics.fillOval(mX - 2, mY + distanceOfEachLayer- 2, size, size);
		graphics.fillOval(mX + distanceOfEachLayer - 2, mY + distanceOfEachLayer- 2, size, size);
		
		graphics.drawString("1",mX - distanceOfEachLayer , mY - distanceOfEachLayer);
		graphics.drawString("2",mX , mY - distanceOfEachLayer);
		graphics.drawString("3",mX + distanceOfEachLayer , mY - distanceOfEachLayer);
		graphics.drawString("4",mX - distanceOfEachLayer , mY );
		graphics.drawString("6",mX + distanceOfEachLayer , mY );
		graphics.drawString("7",mX - distanceOfEachLayer , mY + distanceOfEachLayer);
		graphics.drawString("8",mX , mY + distanceOfEachLayer);
		graphics.drawString("9",mX + distanceOfEachLayer , mY + distanceOfEachLayer);
		
	}
	
	
	public void addBtnToPanel() {
		selectPanel.removeAll();
		listButton = new ArrayList<JButton>();
		for(int index =0 ;index < pointList.size(); index++) {
			JButton button = new JButton();
			button.setBounds(pointList.get(index).x, pointList.get(index).y, objWidth, objHeight);
			button.setBackground(Color.WHITE);
			button.setOpaque(true);
			button.setBorder(new LineBorder(Color.BLACK));
			selectPanel.add(button);
			listButton.add(index,button);
		}
		
		listButton.get(0).setBackground(Color.RED);
		listButton.get(0).setOpaque(true);
		listButton.get(0).setBorder(new LineBorder(Color.BLACK));
		
		//selectPanel.repaint(); 
	}
	
	
	// 从麦克风捕捉声音数据
	public void capture() {
		try {
			af = getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);
			// TargetDataLine是声音的输入(麦克风),而SourceDataLine是输出(音响,耳机).
			td = (TargetDataLine) (AudioSystem.getLine(info));

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

	// 对音频数据进行处理
	public void save() {
		af = getAudioFormat();
		byte audioData[] = baos.toByteArray();
		bais = new ByteArrayInputStream(audioData);
		ais = new AudioInputStream(bais, af, audioData.length / af.getFrameSize());
		ByteArrayOutputStream myData = new ByteArrayOutputStream();
		try {
			AudioSystem.write(ais, AudioFileFormat.Type.WAVE, myData);
			byte[] myAudioData = myData.toByteArray();
			textResult = rec(myAudioData);
			textAnalyze(textResult);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bais != null) {
					bais.close();
				}
				if (ais != null) {
					ais.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	// 保存音频数据
	class Record implements Runnable {
		// 定义存放录音的字节数组,作为缓冲区
		byte bts[] = new byte[10000];

		// 将字节数组包装到流里，最终存入到baos中
		public void run() {
			baos = new ByteArrayOutputStream();
			try {
				stopflag = false;
				while (stopflag != true) {
					// 当停止录音没按下时，该线程一直执行
					int cnt = td.read(bts, 0, bts.length);
					if (cnt > 0) {
						baos.write(bts, 0, cnt);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (baos != null) {
						baos.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
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
			result = jsonArray.getString(0);
		}

		return result;
	}

	// 设置音频格式
	public AudioFormat getAudioFormat() {
		// 采样率是每秒播放和录制的样本数
		float sampleRate = 16000F;
		int sampleSizeInBits = 16;
		// 8,16
		int channels = 1;
		// 单声道为1，立体声为2
		boolean signed = true;
		// true,false
		boolean bigEndian = true;
		// true,false
		// 构造具有线性 PCM 编码和给定参数的 AudioFormat
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}
	
	public static void main(String[] args) {
		new VoiceSelect();
	}

}

