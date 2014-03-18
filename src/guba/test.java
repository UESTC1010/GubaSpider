/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package org.neuroph.contrib.samples.timeseries;
package guba;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PseudoColumnUsage;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.core.learning.DataSet;
import org.neuroph.core.learning.DataSetRow;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

/**
 * 
 * @author zoran
 */
public class test  {
	public static void main(String[] args) {
		System.out.println("Testing network...");

		// 连接mysql数据库
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/guba";
		String user = "root";
		String password = "123";
		Connection conn = null;
		java.sql.Statement statement = null;
		PreparedStatement ps = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
			statement = conn.createStatement();
			if (!conn.isClosed()) {
				System.out.println("Succeeded connecting to the Database!");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		// 开始时间
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = null;
		try {
			startDate = df.parse(Main.START_DATE);
			// endDate = df.parse("2012-10-07 00:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.DATE, 1);

		Date testDate = new Date();

		String inputString = "dfhashj";
		double desireoutput = 0.4455;// 因为我们这里只有一个输出，所以就去了第一个元素
		double output = 0.544;

		// 将结果存入mysql数据库中

		java.sql.Timestamp forcastTimestamp = new java.sql.Timestamp(calendar
				.getTime().getTime());
		java.sql.Timestamp tesTimestamp = new java.sql.Timestamp(
				testDate.getTime());
		// String sql =
		// "insert into 000157(date,testDate,testprice,realprice,input) values("
		// + forcastTimestamp
		// + ","
		// + tesTimestamp
		// + ","
		// + output
		// + ","
		// + desireoutput + "," + inputString + ")";
		boolean result = false;
		try {
			ps = (PreparedStatement) conn
					.prepareStatement("insert into abc(forcastdate,testDate,testprice,realprice,input) values(?,?,?,?,?)");
			ps.setTimestamp(1, forcastTimestamp);
			ps.setTimestamp(2, tesTimestamp);
			ps.setDouble(3, output);
			ps.setDouble(4, desireoutput);
			ps.setString(5, inputString);
			System.out.println(ps.toString());
			// ps = (PreparedStatement) conn.prepareStatement(sql);
			result = ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (result == false) {
			System.out.println("insert fail");
		}
		calendar.add(calendar.DATE, 1);
		try {
			conn.close();
			statement.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
