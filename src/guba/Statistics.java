/**
 * 
 */
package guba;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.Label;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

//import com.mysql.jdbc.Connection;

/**
 * @author Xiaoqing 创建日期：2014-2-21下午4:04:33 修改日期： 用于对预测后的数据进行好坏统计
 */
public class Statistics {

	static int N = 300;
	// 不设阈值，仅比较正负是否相同
//	public static void main(String[] args)
	 public void CompareNoYu()
	{
		int i = 0;// 统计出预测涨跌正确的天数
		int j = 0;// 统计出预测的实验总的天数
		double result = 0.0000;// 预测的正确率

		// 读取数据库中的数据
		// 驱动程序名
		String driver = "com.mysql.jdbc.Driver";
		// URL指向要访问的数据库名scutcs
		String url = "jdbc:mysql://127.0.0.1:3306/guba";
		// MySQL配置时的用户名
		String user = "root";
		// MySQL配置时的密码
		String password = "123";
		try {
			// 加载驱动程序
			Class.forName(driver);
			// 连续数据库
			Connection conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed())
				System.out
						.println("Succeeded connecting to the Database!(统计结果)");
			// statement用来执行SQL语句
			Statement statement = conn.createStatement();
			// 要执行的SQL语句
			String sql = "select testprice,realprice  from abc";
			// 结果集
			ResultSet rs = statement.executeQuery(sql);

			List<Double> testprice = new ArrayList<>();
			List<Double> realprice = new ArrayList<>();
			while (rs.next()) {
				testprice.add(Double.parseDouble(rs.getString("testprice")));
				realprice.add(Double.parseDouble(rs.getString("realprice")));
			}
			// 比较
			Iterator<Double> iterable = testprice.iterator();
			Iterator<Double> iterable2 =  realprice.iterator();
			while ((iterable.hasNext())&&(iterable2.hasNext()) ){
				double testpriceTemp = iterable.next();
				double realpricTemp = iterable2.next();
				if ((
						((testpriceTemp > 0) && (realpricTemp > 0)) 
						|| ((testpriceTemp <= 0) && (realpricTemp <= 0))
						) == true)
					i++;
			}
			j = testprice.size();
			rs.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (j != 0)
			result = (double)((double)i/(double)j);
		else
			System.out.println("the test number is 0!");
		System.out.println("测试天数为：" + j + "   预测的正确率为：" + result+"  i:"+i+"  j:"+j);
		// return result;
	}

	// public static void main(String[] args)
	public double CompareWithYu(double flag) {

		int i = 0;// 统计出预测涨跌正确的天数
		int j = 0;// 统计出预测的实验总的天数
		double result = 0.0000;// 预测的正确率

		// 读取数据库中的数据
		// 驱动程序名
		String driver = "com.mysql.jdbc.Driver";
		// URL指向要访问的数据库名scutcs
		String url = "jdbc:mysql://127.0.0.1:3306/guba";
		// MySQL配置时的用户名
		String user = "root";
		// MySQL配置时的密码
		String password = "123";
		try {
			// 加载驱动程序
			Class.forName(driver);
			// 连续数据库
			Connection conn = DriverManager.getConnection(url, user, password);
//			if (!conn.isClosed())
//				System.out.println("Succeeded connecting to the Database!(统计结果)");
			// statement用来执行SQL语句
			Statement statement = conn.createStatement();
			// 要执行的SQL语句
			String sql = "select testprice,realprice  from abc";
			// 结果集
			ResultSet rs = statement.executeQuery(sql);

			List<Double> testprice = new ArrayList<>();
			List<Double> realprice = new ArrayList<>();
			while (rs.next()) {
				testprice.add(Double.parseDouble(rs.getString("testprice")));
				realprice.add(Double.parseDouble(rs.getString("realprice")));
			}
			// 比较
			Iterator<Double> iterable = testprice.iterator();
			Iterator<Double> iterable2 =  realprice.iterator();
			while ((iterable.hasNext())&&(iterable2.hasNext()) ){
				double testpriceTemp = iterable.next();
				double realpricTemp = iterable2.next();
					if ((
					((testpriceTemp > flag) && (realpricTemp > flag))
					|| ((testpriceTemp <= -flag) && (realpricTemp <= -flag)) 
					|| (((testpriceTemp <= flag) && (testpriceTemp >= -flag)) && ((realpricTemp <= flag) && (realpricTemp >= -flag)))
					) == true)
						i++;
			}
			j = testprice.size();
			rs.close();
			conn.close();
			
			if (j != 0)
				result = (double)((double)i/(double)j);
			else
				System.out.println("the test number is 0!");
			System.out.println(" 预测的正确率为：" + result );
			
		} catch (ClassNotFoundException e) {
			System.out.println("Sorry,can`t find the Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public  void exec(){
		List<Double> result = new ArrayList<>();
		List<Double> deviation = new ArrayList<Double>();
		Statistics statistics = new Statistics();
		DecimalFormat df=new DecimalFormat("#.##");
		for (double flag =0; flag<=0.3;flag = flag + 0.02) {
				System.out.print("   flag:"+df.format(flag));
				double temp = statistics.CompareWithYu(flag);	
				result.add(temp);		
				deviation.add(flag);
		}
		resultSave(deviation,result);
	}

	private static void resultSave(List<Double> low,List<Double> result) {
		// TODO Auto-generated method stub
//		File statistic = new File(Main.statisticPath);
		WritableWorkbook wwb =null ;
		try {
			wwb = Workbook.createWorkbook(new File(Main.stockCode+"statistic.xls"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (wwb!= null) {
			WritableSheet ws = wwb.createSheet("statistic", 0);
			Iterator<Double> resultIterator = result.iterator();
			Iterator<Double> deviationIterator = low.iterator();
			int i =0;
			while((resultIterator.hasNext())&&(deviationIterator.hasNext()))
			{
				double resultTemp = resultIterator.next();
				double lowTemp = deviationIterator.next();
				Label label1 = new Label(0, i, ""+lowTemp);
				Label label2 = new Label(1,i,""+resultTemp);
				try {
					ws.addCell(label1);
					ws.addCell(label2);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				i++;
			}		
		}
		try {
			wwb.write();
			wwb.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
