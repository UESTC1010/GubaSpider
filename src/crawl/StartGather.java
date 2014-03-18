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
		// System.out.println("usage: java -jar StartGather ��Ʊ���� ��Ʊ���� ץȡ������");
		// System.out
		// .println("example: java -jar StartGather ���ɷ� 002237 1000");
		// } else {
		Guba myGuba = new Guba();
		// Stock stock = new Stock(args[0], args[1]);
		 Stock stock = new Stock("���ֹɷ�", "600010");
		//Stock stock = new Stock("�ַ�����", "600000");
//		 Stock stock = new Stock("�����ؿ�", "000157");
		// Stock stock = new Stock("���ݴ�", "600230");
		//Stock stock = new Stock("�����Դ", "600256");
		//Stock stock = new Stock("����ҩҵ", "600332");

		stock.getUri(myGuba);
		//stock.getRecentTopics(Integer.parseInt(args[2]));
		
		while(true){
			//����Ĳ�������ץ����������
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
