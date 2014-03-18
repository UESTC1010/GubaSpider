package guba;

//用来存储一段时间的经济数据特征

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class FinancialData {

//	static String START_DATE = "2012-06-01";
//	static int TEST_DAYS = 150;

	// 构建从当前日期开始向前推TEST_DAYS天内的经济型数据列表
	public LinkedList<FinancialFeature> obtainFaniacialData(Date startDate,int TEST_DAYS) {
		Date endDate = null;

		StockData myStockData = new StockData();
		myStockData.getDataFromXml(Main.stockCode+"test.xls");

		LinkedList<FinancialFeature> financialList = new LinkedList<FinancialFeature>();
//		startDate = (Date) endDate.clone();
		
		for (int i = 0; i <TEST_DAYS; i++) {
			endDate = (Date) startDate.clone();
			endDate.setDate(endDate.getDate() + 1);//将当前时间后置一天

			StockOneDayData myDate = myStockData.daylyData.get(startDate
					.toString());

			FinancialFeature financial = new FinancialFeature();
			if (myDate == null) {
				financialList.add(null);
//				System.out.println("FinancialData 54th"+financial.date);
			} else {
				financial.date = myDate.date;
//				financial.tradVolume = myDate.tradingVolume;
//				financial.highprice = myDate.highprice;
//				financial.lowprice = myDate.lowprice;
//				financial.openPrice = myDate.openPrice;
//				financial.closePrice = myDate.closePrice;
				financial.priceChange = myDate.priceChange;
//				financial.amount = myDate.amount;
//				financial.amplitude = myDate.amplitude;
//				financial.turnRate = myDate.turnRate;
				financialList.add(financial);
			}
			startDate = endDate;
		}
		return financialList;
	}
}
