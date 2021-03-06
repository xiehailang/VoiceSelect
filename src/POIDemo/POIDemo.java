package POIDemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

public class POIDemo {

	public static void main(String[] args)  throws IOException{
	    String filePath = "数据汇总.xls";
	    String sheetName = "测试";
	    //Excel文件易车sheet页的第一行 
	    String title[] = {"日期", "城市","新发布车源数"};
	    //Excel文件易车每一列对应的数据
	    String titleDate[] = {"date", "city","newPublish"};
	        
	    ExcelManage em = new ExcelManage();  
	    //判断该名称的文件是否存在  
	    boolean fileFlag = em.fileExist(filePath);        
	    if(!fileFlag){
	       em.createExcel(filePath,sheetName,title);
	    }  
	    //判断该名称的Sheet是否存在  
	    boolean sheetFlag = em.sheetExist(filePath,sheetName);
	    //如果该名称的Sheet不存在，则新建一个新的Sheet
	    if(!sheetFlag){
	       try {
	           em.createSheet(filePath,sheetName,title);
	       } catch (FileNotFoundException e) {
	           e.printStackTrace();
	       } catch (IOException e) {
	           e.printStackTrace();
	       }
	    }          
	    YiCheData user = new YiCheData();  
	    user.setDate("206-12-21");  
	    user.setCity("北京");  
	    user.setNewPublish("5");  
	    //写入到excel 
	    em.writeToExcel(filePath,sheetName,user,titleDate);  
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

class YiCheData {
    private String date;  
    private String city;  
    private String newPublish;  
   
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getCity() {
    	return city;
    }
    public void setCity(String city) {
    	this.city = city;
    }
    public String getNewPublish() {
    	return newPublish;
    }
    public void setNewPublish(String newPublish) {
    	this.newPublish = newPublish;
    }
}

class ExcelManage {
    private HSSFWorkbook workbook = null;  
    
    /** 
     * 判断文件是否存在
     * @param filePath  文件路径 
     * @return 
     */  
    public boolean fileExist(String filePath){  
         boolean flag = false;  
         File file = new File(filePath);  
         flag = file.exists();  
         return flag;  
    }  
    
    /** 
     * 判断文件的sheet是否存在
     * @param filePath   文件路径 
     * @param sheetName  表格索引名 
     * @return 
     */  
    public boolean sheetExist(String filePath,String sheetName){  
         boolean flag = false;  
         File file = new File(filePath);  
         if(file.exists()){    //文件存在  
            //创建workbook  
             try {  
                workbook = new HSSFWorkbook(new FileInputStream(file));  
                //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)  
                HSSFSheet sheet = workbook.getSheet(sheetName);    
                if(sheet!=null)  
                    flag = true;  
            } catch (Exception e) {  
                e.printStackTrace();  
            }                 
         }else{    //文件不存在  
             flag = false;  
         }            
         return flag;  
    }
    /** 
     * 创建新Sheet并写入第一行数据
     * @param filePath  excel的路径 
     * @param sheetName 要创建的表格索引 
     * @param titleRow excel的第一行即表格头 
     * @throws IOException 
     * @throws FileNotFoundException 
     */  
    public void createSheet(String filePath,String sheetName,String titleRow[]) throws FileNotFoundException, IOException{ 
        FileOutputStream out = null;         
        File excel = new File(filePath);  // 读取文件
        FileInputStream in = new FileInputStream(excel); // 转换为流
        workbook = new HSSFWorkbook(in); // 加载excel的 工作目录       
                          
        workbook.createSheet(sheetName); // 添加一个新的sheet  
        //添加表头  
        Row row = workbook.getSheet(sheetName).createRow(0);    //创建第一行            
        try {              
            for(int i = 0;i < titleRow.length;i++){  
                Cell cell = row.createCell(i);  
                cell.setCellValue(titleRow[i]);  
            } 
            out = new FileOutputStream(filePath);  
            workbook.write(out);
       }catch (Exception e) {  
           e.printStackTrace();  
       }finally {    
           try {    
               out.close();    
           } catch (IOException e) {    
               e.printStackTrace();  
           }    
       }             
    }
    /** 
     * 创建新excel. 
     * @param filePath  excel的路径 
     * @param sheetName 要创建的表格索引 
     * @param titleRow excel的第一行即表格头 
     */  
    public void createExcel(String filePath,String sheetName,String titleRow[]){  
        //创建workbook  
        workbook = new HSSFWorkbook();  
        //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)  
        workbook.createSheet(sheetName);    
        //新建文件  
        FileOutputStream out = null;  
        try {  
            //添加表头  
            Row row = workbook.getSheet(sheetName).createRow(0);    //创建第一行    
            for(int i = 0;i < titleRow.length;i++){  
                Cell cell = row.createCell(i);  
                cell.setCellValue(titleRow[i]);  
            }               
            out = new FileOutputStream(filePath);  
            workbook.write(out);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {    
            try {    
                out.close();    
            } catch (IOException e) {    
                e.printStackTrace();  
            }    
        }    
    }  
    /** 
     * 删除文件. 
     * @param filePath  文件路径 
     */  
    public boolean deleteExcel(String filePath){  
        boolean flag = false;  
        File file = new File(filePath);  
        // 判断目录或文件是否存在    
        if (!file.exists()) {  
            return flag;    
        } else {    
            // 判断是否为文件    
            if (file.isFile()) {  // 为文件时调用删除文件方法    
                file.delete();  
                flag = true;  
            }   
        }  
        return flag;  
    }  
    /** 
     * 往excel中写入. 
     * @param filePath    文件路径 
     * @param sheetName  表格索引 
     * @param object 
     */  
    public void writeToExcel(String filePath,String sheetName, Object object,String titleRow[]){  
        //创建workbook  
        File file = new File(filePath);  
        try {  
            workbook = new HSSFWorkbook(new FileInputStream(file));  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        FileOutputStream out = null;  
        HSSFSheet sheet = workbook.getSheet(sheetName);  
        // 获取表格的总行数  
        int rowCount = sheet.getLastRowNum() + 1; // 需要加一  
        try {  
            Row row = sheet.createRow(rowCount);     //最新要添加的一行  
            //通过反射获得object的字段,对应表头插入  
            // 获取该对象的class对象  
            Class<? extends Object> class_ = object.getClass();              
            
            for(int i = 0;i < titleRow.length;i++){    
                String title = titleRow[i];
                String UTitle = Character.toUpperCase(title.charAt(0))+ title.substring(1, title.length()); // 使其首字母大写;  
                String methodName  = "get"+UTitle;  
                Method method = class_.getDeclaredMethod(methodName); // 设置要执行的方法  
                String data = method.invoke(object).toString(); // 执行该get方法,即要插入的数据  
                Cell cell = row.createCell(i);  
                cell.setCellValue(data);  
            }           
            out = new FileOutputStream(filePath);  
            workbook.write(out);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {    
            try {    
                out.close();    
            } catch (IOException e) {    
                e.printStackTrace();  
            }    
        }    
    }  
}