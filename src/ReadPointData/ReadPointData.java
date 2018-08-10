package ReadPointData;

import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;


public class ReadPointData {

	private JFrame mainFrame;
	private Container mainContentPane;
	private ReadPointDataPanel selectPanel;
	private int frameWidth = 900,frameHeight = 900;//区域大小
	ArrayList<Point> pointList;
	
	public ReadPointData(){

		initFrame();
		
		if (readPointData("SelectPoint1")) {
			addBtnToPanel(30,30);
		}
		if (readPointData("SelectPoint2")) {
			addBtnToPanel(60,60);
		}
		if (readPointData("SelectPoint3")) {
			addBtnToPanel(90,90);
		}
		
		selectPanel.repaint(); 
	}
	
	public static void main(String[] args) {
		new ReadPointData();
	}
	
	public Boolean readPointData(String name) {
		Boolean flag = false;
		File file = new File("SelectPoint.xls");

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
	
	public void addBtnToPanel(int objWidth,int objHeight) {
		for(int index =0 ;index < pointList.size(); index++) {
			JButton button = new JButton();
			button.setBounds(pointList.get(index).x, pointList.get(index).y, objWidth, objHeight);
			button.setBackground(Color.RED);
			button.setOpaque(true);
			button.setBorder(new LineBorder(Color.BLACK));
			selectPanel.add(button);

		}
	}
	
	
	public void initFrame() {
		mainFrame =new JFrame("读数据");
		mainFrame.setSize(960, 950);
		mainFrame.setLocation(10,10);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainContentPane = mainFrame.getContentPane();
		mainContentPane.setLayout(null);
		
		selectPanel = new ReadPointDataPanel();
		selectPanel.setBounds(30, 10, frameWidth, frameHeight);
		selectPanel.setLayout(null);
		
		selectPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 5));

		mainContentPane.add(selectPanel);	
		
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
	}
	
}
