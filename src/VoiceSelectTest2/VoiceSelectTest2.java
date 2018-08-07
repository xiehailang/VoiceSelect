package VoiceSelectTest2;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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


public class VoiceSelectTest2 {
	
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
    
    private String areaT="",logT="",keyT="";
    
	private JFrame mainFrame;
	private Container mainContentPane;
	private JPanel mainPanel,selectPanel;
	private int objWidth = 40,objHeight = 40;//目标大小
	private int frameWidth = 900,frameHeight = 900;//区域大小
	private JButton button1,button2,button3,button4,button5;
	private ArrayList<Point> pointList;
	private ArrayList<JButton> listButton;

	private Point mousePoint = null;
	private Robot mainRbt;
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
			, "语音1开始", "语音1结束", "语音1结果"
			, "语音2开始", "语音2结束", "语音2结果"
			, "语音3开始", "语音3结束", "语音3结果"
			, "语音4开始", "语音4结束", "语音4结果"
			, "语音5开始", "语音5结束", "语音5结果"
			, "结束", "结果"};
	// Excel文件每一列对应的数据
	String titleDate[] = { "buttonName", "testStart", 
			"voice1Start", "voice1End", "voice1Result",
			"voice2Start", "voice2End", "voice2Result",
			"voice3Start", "voice3End", "voice3Result",
			"voice4Start", "voice4End", "voice4Result",
			"voice5Start", "voice5End", "voice5Result",
			"testEnd" , "testResult" };
	Data user = new Data();
	
	public VoiceSelectTest2(){
		client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
		setFilePath();
		initFrame();
		mainEvent();
		
	}
	
	public void setFilePath() {
		String os = System.getProperty("os.name");  
		//当前用户桌面
		File desktopDir = FileSystemView.getFileSystemView() .getHomeDirectory();		
		desktopPath = desktopDir.getAbsolutePath();
		filePath = desktopPath+"/Desktop/data1.xls";
		if(os.toLowerCase().startsWith("win")){  
			filePath = desktopPath+"\\data1.txt";
		}
	}
	
	public void savaAllData() {

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
				paint1(1);
			}else if (textResult.contains("二")||textResult.contains("2")) {
				paint1(2);
			}else if (textResult.contains("三")||textResult.contains("3")) {
				paint1(3);
			}else if (textResult.contains("四")||textResult.contains("4")) {
				paint1(4);
			}else if (textResult.contains("五")||textResult.contains("5")) {
				paint1(5);
			}else if (textResult.contains("六")||textResult.contains("6")) {
				paint1(6);
			}else if (textResult.contains("七")||textResult.contains("7")) {
				paint1(7);
			}else if (textResult.contains("八")||textResult.contains("8")) {
				paint1(8);
			}else if (textResult.contains("九")||textResult.contains("9")) {
				paint1(9);	
			}
		}else {
			logT = "语音识别出错";
			setTitle();
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
				case KeyEvent.VK_1:
					paint1(1);
					break;
				case KeyEvent.VK_2:
					paint1(2);
					break;
				case KeyEvent.VK_3:
					paint1(3);
					break;
				case KeyEvent.VK_4:
					paint1(4);
					break;
				case KeyEvent.VK_5:
					paint1(5);
					break;
				case KeyEvent.VK_6:
					paint1(6);
					break;
				case KeyEvent.VK_7:
					paint1(7);
					break;
				case KeyEvent.VK_8:
					paint1(8);
					break;
				case KeyEvent.VK_9:
					paint1(9);
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
						voiceNum++;
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
	
	// 实现模拟鼠标点击
	public void mouseClick() {
		
		Rectangle rectangle = listButton.get(0).getBounds();
		Point point = new Point(mousePoint.x, mousePoint.y);
		SwingUtilities.convertPointFromScreen(point, selectPanel);
		if (rectangle.contains(point)) {
			keyT="";
			logT = "您点中了目标！";
			user.setTestResult(true);
			setTitle();
		}else {
			keyT="";
			logT = "您进行了一次点击！";
			user.setTestResult(false);
			setTitle();
		}
	}

	
	public void setTitle() {
		mainFrame.setTitle("目标选择实验 : "+ areaT + keyT +logT);
	}
	
	
	public Boolean readPointData(String name) {
		Boolean flag = false;
		String os = System.getProperty("os.name");  
		File file = new File(desktopPath+"/Desktop/PointData.xls");
		if(os.toLowerCase().startsWith("win")){  
			file = new File(desktopPath+"\\PointData.txt");
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
		mainFrame =new JFrame("语音目标选择实验");
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
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button1")) {
					areaT="实验1； ";
					setTitle();
					voiceNum=0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("1");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
				}
			}
		});
		button2 = new JButton("2");
		button2.setBounds(80, 10, 40, 40);
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button2")) {
					areaT = "实验2； ";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("2");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
				}
			}
		});
		button3 = new JButton("3");
		button3.setBounds(130, 10, 40, 40);
		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button3")) {
					areaT = "实验3； ";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("3");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
				}
			}
		});
		button4 = new JButton("4");
		button4.setBounds(180, 10, 40, 40);
		button4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button4")) {
					areaT = "实验4； ";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("4");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
				}
			}
		});
		button5 = new JButton("5");
		button5.setBounds(30, 60, 40, 40);
		button5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (readPointData("button5")) {
					areaT = "实验5； ";
					setTitle();
					voiceNum = 0;
					addBtnToPanel();
					mainFrame.requestFocus();
					enter();
					user.setButtonName("5");
					user.setTestStart(String.valueOf(System.currentTimeMillis()));
				}
			}
		});
		
		mainPanel.add(button1);
		mainPanel.add(button2);
		mainPanel.add(button3);
		mainPanel.add(button4);
		mainPanel.add(button5);
		
		mainContentPane.add(selectPanel);
		mainContentPane.add(mainPanel);
		
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
	}
	
	public void enter() {
		mousePoint =  new Point(frameWidth/2,frameWidth/2);//计算出frame中心坐标
		SwingUtilities.convertPointToScreen(mousePoint, selectPanel);//转化成frame的坐标系
		//moveMouse(mousePoint);//移动鼠标
		layerNum = 0;
		setLayerDistance();
		paintNine();//画标记
		numOfSelect = 0;
		setTitle();
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
		new VoiceSelectTest2();
	}

}

