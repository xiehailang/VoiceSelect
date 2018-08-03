package POIDemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

public class POIDemo {

	public static void main(String[] args)  throws IOException{
		POIDemo myPoiDemo = new POIDemo();
		myPoiDemo.create();
		myPoiDemo.read();
	}

	public void create() throws IOException {
	
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("sheet name");

		// 设置第一行单元格宽度
		sheet.setColumnWidth(0, 100 * 256);

		// 创建第一行
		HSSFRow row0 = sheet.createRow(0);

		// 设置第一行单元格高度
		row0.setHeight((short) 400);

		// 创建第一行第一列单元格
		HSSFCell cell0_1 = row0.createCell(0);
		// 设置单元格的值
		cell0_1.setCellValue("hello!");

		// 改变字体样式，步骤
		// 1.设置字体,红色
		HSSFFont hssfFont = wb.createFont();
		hssfFont.setColor(HSSFFont.COLOR_RED);

		// 2.设置样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFont(hssfFont);
		// 设置居中
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		// 3.单元格使用样式，设置第一行第一列单元格样式
		cell0_1.setCellStyle(cellStyle);

		// 生成excel文件
		FileOutputStream fileOut = new FileOutputStream("test1.xls");
		wb.write(fileOut);
		fileOut.close();
	}

    public void read() {
        File file = new File("test1.xls");
        if (!file.exists())
            System.out.println("文件不存在");
        try {
            //1.读取Excel的对象
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(file));
            //2.Excel工作薄对象
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
            //3.Excel工作表对象
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
            //总行数
            int rowLength = hssfSheet.getLastRowNum()+1;
            //4.得到Excel工作表的行
            HSSFRow hssfRow = hssfSheet.getRow(0);
            //总列数
            int colLength = hssfRow.getLastCellNum();
            //得到Excel指定单元格中的内容
            HSSFCell hssfCell = hssfRow.getCell(0);
            //得到单元格样式
            CellStyle cellStyle = hssfCell.getCellStyle();
 
            for (int i = 0; i < rowLength; i++) {
                //获取Excel工作表的行
                HSSFRow hssfRow1 = hssfSheet.getRow(i);
                for (int j = 0; j < colLength; j++) {
                    //获取指定单元格
                    HSSFCell hssfCell1 = hssfRow1.getCell(j);
 
                    //Excel数据Cell有不同的类型，当我们试图从一个数字类型的Cell读取出一个字符串时就有可能报异常：
                    //Cannot get a STRING value from a NUMERIC cell
                    //将所有的需要读的Cell表格设置为String格式
                    if (hssfCell1 != null) {
                        hssfCell1.setCellType(CellType.STRING);
                    }
 
                    //获取每一列中的值
                    System.out.print(hssfCell1.getStringCellValue() + "\t");
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
