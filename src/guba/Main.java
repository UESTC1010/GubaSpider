package guba;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

import jxl.Workbook;
import jxl.demo.Write;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class Main {

	/**
	 * @param args
	 */
	static String stockCode;

	static String START_DATE = "2014-01-01 00:00:00";
	static int TEST_DAY = 4;

	static String BasicPath;
	static String basicDataFilePath ;
	static String normalizedFilePath ;
	static String trainFilePath ;
	static String trainCsvFilePath ;
	static String trainResultPath ;
	static String statisticPath ;
	static String testPath ;
	static String[] stockCodes = { "600010", "600030","600179", "600186"};
	public static void main(String[] args) {
//		String[] stockCodes = { "600010", "600030" };
		for (int i = 0; i < stockCodes.length; i++) {
			updateInfo(stockCodes[i]);
			exec();
			System.out.println("over...");
		}
	}

	public static void updateInfo(String newStockCode) {
		stockCode = newStockCode;
		BasicPath = "D:/Users/duxq/workspace/stockPrediction" + "/";
		basicDataFilePath = BasicPath + stockCode + "DataBook.xls";
		normalizedFilePath = BasicPath + stockCode + "normalized.xls";
		trainFilePath = BasicPath + stockCode + "TrainSet.xls";
		trainCsvFilePath = BasicPath  + "src/guba/data/"+ stockCode + "TrainSet.csv";
		trainResultPath = BasicPath + stockCode + "TrainResult.xls";
		statisticPath = BasicPath+ stockCode  + "statistic.xls";
		testPath = basicDataFilePath+ stockCode  + "test.xls";
	}

	public static void exec() {
		
		StockData myStockData = new StockData();
		myStockData.getDataFromXml(Main.stockCode+"test.xls");
		// myStockData.getDataFromXml(testPath);

		FinancialData fff = new FinancialData();
		NetData nnn = new NetData();

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = null;
		try {
			startDate = df.parse(START_DATE);
			// endDate = df.parse("2012-10-07 00:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Date start = (Date) startDate.clone();
		LinkedList<FinancialFeature> financialList = fff.obtainFaniacialData(
				start, TEST_DAY);
		LinkedList<NetFeature> netFeatureList = nnn.obtainNetData(start,
				TEST_DAY);
		Filter filter = new Filter();
		filter.data((List<FinancialFeature>) financialList.clone(),
				(List<NetFeature>) netFeatureList.clone(), 1);// 过滤掉停盘的天
		NeuralNetworkData startAnalysis = new NeuralNetworkData();
		startAnalysis.saveData(filter);

		File basicDataFile = new File(basicDataFilePath);
		System.out.println("basicDataFile.length:" + basicDataFile.length());

		File normalizedFile = new File(normalizedFilePath);

		File trainFile = new File(trainFilePath);

		File trainCsvFile = new File(trainCsvFilePath);

		NeuralNetworkData networkData = new NeuralNetworkData();
		try {
			networkData.copyExcel(basicDataFilePath, normalizedFilePath);
			System.out.println("normalizedFile.length:"
					+ normalizedFile.length());
			networkData.normalization(normalizedFile);// 归一化处理
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 将归一化的数据中的日期去掉，并存为TrainSet.csv供训练使用
		try {
			networkData.copyExcel(normalizedFilePath, trainFilePath);
			System.out.println("trainFile.length:" + trainFile.length());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Workbook wb = null;
		WritableWorkbook wwb = null;
		try {
			wb = Workbook.getWorkbook(trainFile);
			wwb = Workbook.createWorkbook(trainFile, wb);// copy
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		WritableSheet ws = wwb.getSheet(0);
		WritableSheet sheet = wwb.getSheet(0);// 获取工作对象
		sheet.removeColumn(sheet.getColumns() - 1);
		try {
			wwb.write();
			wwb.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		startAnalysis.saveAsCsv(trainFile, trainCsvFile);// 将训练集存为csv格式

		// 第三步、神经网络的训练与测试
		Neuroph tts = new Neuroph();
		tts.train(trainCsvFilePath);
		tts.testNeuralNetwork();
		
		//第四步 统计
		List<Double> result = new ArrayList<>();
		List<Double> deviation = new ArrayList<Double>();
		Statistics statistics = new Statistics();
		DecimalFormat dff=new DecimalFormat("#.##");
		for (double flag =0; flag<=0.3;flag = flag + 0.02) {
				System.out.print("   flag:"+df.format(flag));
				double temp = statistics.CompareWithYu(flag);	
				result.add(temp);		
				deviation.add(flag);
		}
		Statistics statistics2 = new Statistics();
		statistics2.exec();
	}
}
