package Log4jDemo;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class Log4jDemo {

	private static Logger logger = Logger.getLogger(Log4jDemo.class);  // 获取日志记录器
	
	public static void main(String[] args) {

		BasicConfigurator.configure(); // 自动快速地使用缺省Log4j环境。  
		//PropertyConfigurator.configure( String configFilename); //读取使用Java的特性文件编写的配置文件。  
		//DOMConfigurator.configure(String filename ); //读取XML形式的配置文件。

		// 记录error级别的信息  
        logger.error("This is error message.");  
		// 记录warn级别的信息  
		logger.warn ("This is warn message.") ;  
        // 记录debug级别的信息  
        logger.debug("This is debug message.");  
        // 记录info级别的信息  
        logger.info("This is info message.");  
        
	}

}
