package crawl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class StartGather {
	
	public static void main(String args[]) throws ParseException {
	
		CrawlTime.init();
		UglyDB.initDB();
		// UglyDB.loadUglyDB();

		// if (args.length == 0) {
		// System.out.println("usage: java -jar StartGather 股票名字 股票代码 抓取主题数");
		// System.out
		// .println("example: java -jar StartGather 恒邦股份 002237 1000");
		// } else {
		Guba myGuba = new Guba();
		// Stock stock = new Stock(args[0], args[1]);
		 Stock stock = new Stock("包钢股份", "600010");
		//Stock stock = new Stock("浦发银行", "600000");
//		 Stock stock = new Stock("中联重科", "000157");
		// Stock stock = new Stock("沧州大化", "600230");
		//Stock stock = new Stock("广汇能源", "600256");
		//Stock stock = new Stock("广州药业", "600332");

		stock.getUri(myGuba);
		//stock.getRecentTopics(Integer.parseInt(args[2]));
		
		while(true){
			//里面的参数代表抓多少条数据
			stock.getRecentTopics();
			Date da = new Date();
			System.out.println(da.getHours()+":"+da.getMinutes());
			UglyDB.saveUglyDB();
			UglyDB.clear();
			Date da1 = new Date();
			System.out.println(da1.getHours()+":"+da1.getMinutes());
			try {
				Thread.sleep(1000*60*60*24);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			CrawlTime.tomorrow();
		}
		
		
	}
}
