package ThreeSelect;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
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
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.filechooser.FileSystemView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.events.MutationEvent;

import com.baidu.aip.speech.AipSpeech;

public class ThreeSelect {
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
    
	private JFrame mainFrame;
	private Container mainContentPane;
	private JPanel mainPanel;
	private ArrayList<JButton> listButton;
	private int objWidth = 40,objHeight = 40;//目标大小
	private int frameWidth = 900,frameHeight = 900;//目标大小
	
	private Point mousePoint;
	private Robot mainRbt;
	private int layerNum = 5 ;
	private int distanceOfEachLayer = 0;
	private int numOfSelect;//操作步数
	private long startTime;
	private long endTime;
	
	private String areaT="",logT="",keyT="";
	
	private int n = 0;//第n次点击
	
	public ThreeSelect(){
		client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
		initFrame();
		mainEvent();
		
		logT="按 space 开始进行实验";
		setTitle();
	}

	//界面重载函数
	public void winReInit() {
		mainPanel.removeAll();
		addMyJPanel(mainPanel);
		//mainPanel.repaint(); 
		
		mousePoint =  new Point(frameWidth/2,frameWidth/2);//计算出frame中心坐标
		SwingUtilities.convertPointToScreen(mousePoint, mainPanel);//转化成frame的坐标系
		moveMouse(mousePoint);//移动鼠标
		layerNum = 0;
		setLayerDistance();
		paintFlag();//画标记
		numOfSelect = 0;
		
		setTitle();
	}
	
	// 上下左右键盘的操作函数
	public void keyOri(int orientation) {

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
			mousePoint = point;
			
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
	
	public void setTitle() {
		mainFrame.setTitle("跳跃式 : "+ keyT + areaT + logT);
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
	
	public void addMyJPanel(JPanel myJPanel) {
		listButton = new ArrayList<JButton>();
		Random random = new Random();
		Rectangle rect = new Rectangle();
		int i = 0;
		boolean boolBtn = false;
		JButton button;
		int num = 10; //添加的按钮个数
		while ( i < num ) { 
			button = new JButton();
			int x = random.nextInt(frameWidth - objWidth );
			int y = random.nextInt(frameHeight - objHeight );
			button.setBounds(x,y,objWidth,objHeight );
			button.setBackground(Color.WHITE);
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
			listButton.get(index).addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					mainFrame.requestFocus();
				}
			});
		}
		
		listButton.get(0).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				endTime = System.currentTimeMillis();
				logT ="耗时:"+(endTime - startTime)+" 毫秒！步数："+numOfSelect;
				dateSave(logT);
				setTitle();
				mainFrame.requestFocus();
			}
		});
	}
	
	// 载入界面
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
		mainContentPane.add(mainPanel,BorderLayout.CENTER);
		mainFrame.setVisible(true);
		mainFrame.setResizable(false);
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
		moveMouse(point);//移动鼠标
		mousePoint = point;
		
	}
	
	// 画九宫格加画指示
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
	
	public void mainEvent() {
		mainFrame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				super.keyPressed(e);
				switch (e.getKeyCode()) {
				case KeyEvent.VK_SPACE:
					winReInit();
					break;
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
					mouseClick();
					break;
				case KeyEvent.VK_Z:
					if (voiveFlag) {
						areaT = "正在录音; ";
						setTitle();
						recStartTime = System.currentTimeMillis();
						capture(); // 调用录音的方法
						voiveFlag = false;
					}
					break;
				case KeyEvent.VK_X:
					// 停止录音
					stopflag = true;
					// 调用保存录音的方法
					save();
					voiveFlag = true;
					recEndTime = System.currentTimeMillis();
					areaT = "语音识别结束,耗时:"+(recEndTime - recStartTime)+" 毫秒！ ";
					setTitle();
					dateSave(areaT+"识别结果:"+textResult);
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
				}
			}
		});
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
			File f = new File(desktopPath+"/data3.txt");;
			if(os.toLowerCase().startsWith("win")){  
				f = new File(desktopPath+"\\data3.txt");
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
	
	// 实现模拟鼠标点击
	public void mouseClick() {
		n++;
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
		areaT="";
		logT = "------------ 第"+n+"次点击 ------------";
		setTitle();
		dateSave(logT);
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
			}else if (textResult.contains("上")) {
				keyOri(3);
			}else if (textResult.contains("下")) {
				keyOri(4);
			}else if (textResult.contains("左")) {
				keyOri(1);
			}else if (textResult.contains("右")) {
				keyOri(2);
			}
		}else {
			logT = "语音识别出错";
			setTitle();
		}
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
		}
        
        return result;
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ThreeSelect();
	}

}
