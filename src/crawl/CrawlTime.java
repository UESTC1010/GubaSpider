package crawl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class CrawlTime {
	private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static Date INITIAL_TIME = null;
	public static Date START_TIME   = null;
	public static Date END_TIME     = null;
	public static String path = "log.txt";
	
	public static void init() throws ParseException{
		INITIAL_TIME  = format.parse("2013-06-01 00:00:00");
		String lasttime = gettime(path);
		if(lasttime != null)
			START_TIME = format.parse(lasttime);
		else
			START_TIME = format.parse("2013-10-01 00:00:00");
		END_TIME   = new Date();
		writefile(path, format.format(END_TIME));
	}
	public static void tomorrow(){
		START_TIME = END_TIME;		
		END_TIME = new Date();
		writefile(path, format.format(END_TIME));
	} 
	
	public static String gettime(String pathname){
		File getstring = new File(pathname);
		Scanner sc = null;
		if(!getstring.exists()){
			try {
				getstring.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			sc = new Scanner(new FileReader(getstring));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
		String line=null;
		while((sc.hasNextLine()&&(line=sc.nextLine())!=null)){
		    if(!sc.hasNextLine())
		    {
		    	System.out.println(line);
		    	return line;
		    }
		    
		}
		sc.close();
		return null;
	}
	public static void writefile(String pathname, String endtime){
		File log = new File(pathname);
		try {
			FileWriter fileWriter=new FileWriter(log,true);
			fileWriter.write(endtime+"\r\n");
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
