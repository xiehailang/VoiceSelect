package VoiceSelect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;


public class ExcelManage {
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