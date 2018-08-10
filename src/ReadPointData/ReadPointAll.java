package ReadPointData;

import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;

public class ReadPointAll {

	private JFrame mainFrame;
	private Container mainContentPane;
	private JPanel mainPanel;
	private ReadPointDataPanel selectPanel;
	private JButton button1,button2,button3,button4,button5,button6,button7,button8,button9;
	private JButton button10,button11,button12,button13,button14,button15,button16,button17,button18;
	
	private int objWidth = 30,objHeight = 30;//目标大小
	private int frameWidth = 900,frameHeight = 900;//区域大小
	
	ArrayList<Point> pointList;
	
	public ReadPointAll() {
		initFrame();
	}
	
	public static void main(String[] args) {
		new ReadPointAll();
	}

	public Boolean readPointData(String name) {
		Boolean flag = false;
		File file = new File("PointData.xls");

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

	public void addBtnToPanel() {
		selectPanel.removeAll();

		for(int index =0 ;index < pointList.size(); index++) {
			JButton button = new JButton();
			button.setBounds(pointList.get(index).x, pointList.get(index).y, objWidth, objHeight);
			button.setBackground(Color.WHITE);
			button.setOpaque(true);
			button.setBorder(new LineBorder(Color.BLACK));
			selectPanel.add(button);
			if (index == 0) {
				button.setBackground(Color.RED);
			}
		}
		
		selectPanel.repaint(); 
	}
	public void initFrame() {
		mainFrame =new JFrame("语音目标");
		mainFrame.setSize(1350, 950);
		mainFrame.setLocation(10,10);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainContentPane = mainFrame.getContentPane();
		mainContentPane.setLayout(null);
		
		selectPanel = new ReadPointDataPanel();
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
					objWidth=30;
					objHeight=30;
					
					addBtnToPanel();
					
					button1.setBackground(Color.GREEN);
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
				if (readPointData("button2")) {
					objWidth=30;
					objHeight=30;

					addBtnToPanel();
					mainFrame.requestFocus();

					button2.setBackground(Color.GREEN);
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
				if (readPointData("button3")) {
					objWidth=30;
					objHeight=30;

					addBtnToPanel();
					mainFrame.requestFocus();

					button3.setBackground(Color.GREEN);
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
				if (readPointData("button4")) {
					objWidth=30;
					objHeight=30;

					addBtnToPanel();
					mainFrame.requestFocus();

					button4.setBackground(Color.GREEN);
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
				if (readPointData("button5")) {
					objWidth=30;
					objHeight=30;

					addBtnToPanel();
					mainFrame.requestFocus();

					button5.setBackground(Color.GREEN);
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
				if (readPointData("button6")) {
					objWidth=30;
					objHeight=30;

					addBtnToPanel();
					mainFrame.requestFocus();

					button6.setBackground(Color.GREEN);
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
				if (readPointData("button7")) {
					objWidth=60;
					objHeight=60;

					addBtnToPanel();
					mainFrame.requestFocus();

					button7.setBackground(Color.GREEN);
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
				if (readPointData("button8")) {
					objWidth=60;
					objHeight=60;

					addBtnToPanel();
					mainFrame.requestFocus();

					button8.setBackground(Color.GREEN);
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
				if (readPointData("button9")) {
					objWidth=60;
					objHeight=60;

					addBtnToPanel();
					mainFrame.requestFocus();

					button9.setBackground(Color.GREEN);
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
				if (readPointData("button10")) {
					objWidth=60;
					objHeight=60;

					addBtnToPanel();
					mainFrame.requestFocus();

					button10.setBackground(Color.GREEN);
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
				if (readPointData("button11")) {
					objWidth=60;
					objHeight=60;

					addBtnToPanel();
					mainFrame.requestFocus();

					button11.setBackground(Color.GREEN);
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
				if (readPointData("button12")) {
					objWidth=60;
					objHeight=60;

					addBtnToPanel();
					mainFrame.requestFocus();

					button12.setBackground(Color.GREEN);
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
				if (readPointData("button13")) {
					objWidth=90;
					objHeight=90;

					addBtnToPanel();
					mainFrame.requestFocus();

					button13.setBackground(Color.GREEN);
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
				if (readPointData("button14")) {
					objWidth=90;
					objHeight=90;


					addBtnToPanel();

					button14.setBackground(Color.GREEN);
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
				if (readPointData("button15")) {
					objWidth=90;
					objHeight=90;

					addBtnToPanel();
			
					button15.setBackground(Color.GREEN);
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
				if (readPointData("button16")) {
					objWidth=90;
					objHeight=90;

					addBtnToPanel();
					mainFrame.requestFocus();

					button16.setBackground(Color.GREEN);
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
				if (readPointData("button17")) {
					objWidth=90;
					objHeight=90;

					addBtnToPanel();

					button17.setBackground(Color.GREEN);
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
					objWidth=90;
					objHeight=90;

					addBtnToPanel();

					button18.setBackground(Color.GREEN);
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
		
		mainContentPane.add(selectPanel);
		mainContentPane.add(mainPanel);
		
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
	}
	
	
	
}
