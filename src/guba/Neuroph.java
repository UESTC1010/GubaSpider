/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package org.neuroph.contrib.samples.timeseries;
package guba;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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


/**
 * 
 * @author zoran
 */
public class Neuroph implements LearningEventListener {
	NeuralNetwork neuralNet;
	DataSet trainingSet;
	DataSet testingSet;

	 public static void main(String[] args) {
	 Neuroph neuroph = new Neuroph();
	 neuroph.train("data/600010TrainSet.csv");
	}

	
	public void train(String inputFilePath) {
//		inputFilePath = inputFilePath.substring(inputFilePath.length() - 18,
//				inputFilePath.length());
//		System.out.println("path:" + inputFilePath + " "
//				+ inputFilePath.length());
//		String inputFileName_train = Neuroph.class.getResource(inputFilePath).getFile();
//		System.out.println("inputFileName_train:" + inputFileName_train);
//		String inputFileName_test = Neuroph.class.getResource(inputFilePath).getFile();
		// create MultiLayerPerceptron neural network
		neuralNet = new MultiLayerPerceptron(TransferFunctionType.TANH, 10, 16,
				1);
		MomentumBackpropagation learningRule = (MomentumBackpropagation) neuralNet
				.getLearningRule();
		learningRule.setLearningRate(0.2);
		learningRule.setMomentum(0.5);
		learningRule.setMaxError(0.8);
		// learningRule.addObserver(this);
		learningRule.addListener(this);

		// create training set from file
		trainingSet = DataSet.createFromFile(inputFilePath, 10, 1, ",");
		testingSet = DataSet.createFromFile(inputFilePath, 10, 1, ",");
		// train the network with training set
		neuralNet.learn(trainingSet);

		// add observer here

		System.out.println("Done training.");

	}

	/**
	 * Prints network output for the each element from the specified training
	 * set.
	 * 
	 * @param neuralNet
	 *            neural network
	 * @param trainingSet
	 *            training set
	 */
	public void testNeuralNetwork() {
		System.out.println("Testing network...");

		// 连接mysql数据库
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/stock";
//		System.out.println("url:"+url);
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
		// 神经网络训练
		List<Double> desireOutputs = new LinkedList<>();
		List<Double> outputs = new LinkedList<>();
		System.out.println("testingSet:"+testingSet.size());
		for (DataSetRow testingElement : testingSet.getRows()) {
			neuralNet.setInput(testingElement.getInput());
			neuralNet.calculate();
			double[] networkOutput = neuralNet.getOutput();

			String inputString = Arrays.toString(testingElement.getInput());
			double desireoutput = testingElement.getDesiredOutput()[0];// 因为我们这里只有一个输出，所以就取了第一个元素
			double output = networkOutput[0];

			System.out.print("Input: "
					+ Arrays.toString(testingElement.getInput()));
			System.out.print(" desireOutput: "
					+ Arrays.toString(testingElement.getDesiredOutput()));
			System.out.println(" Output: " + Arrays.toString(networkOutput));
			desireOutputs.add(testingElement.getDesiredOutput()[0]);
			outputs.add(networkOutput[0]);
			// 将结果存入mysql数据库中
			Date testDate = new Date();
			java.sql.Timestamp tesTimes = new java.sql.Timestamp(
					testDate.getTime());
			java.sql.Timestamp forcastTimes = new java.sql.Timestamp(
					calendar.getTime().getTime());
//			Date tesTimes = new java.sql.Date(new java.util.Date().getTime());
			boolean result = false;
			try {
				ps = (PreparedStatement) conn
						.prepareStatement("insert into stock"+  Main.stockCode+"(forcastdate,testDate,testprice,realprice,input) values(?,?,?,?,?)");		
				System.out.println("times:"+forcastTimes);
				ps.setTimestamp(1, forcastTimes);
				ps.setTimestamp(2, tesTimes);
				ps.setDouble(3, output);
				ps.setDouble(4, desireoutput);
				ps.setString(5, inputString);
				result = ps.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (result == true) {
				System.out.println("insert success");
			}
			calendar.add(calendar.DATE, 1);
			// 对于预测日期去掉周末停盘的日子
			int dayForWeek = 0;
			if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
				dayForWeek = 7;
			} else {
				dayForWeek = calendar.get(calendar.DAY_OF_WEEK) - 1;
			}
			if (dayForWeek == 6) {
				calendar.add(Calendar.DATE, 2);
				System.out.println("xingqi : "+calendar.getTime().toString());
			}
			if (dayForWeek == 7) {
				calendar.add(Calendar.DATE, 1);
				System.out.println("xingqi : "+calendar.getTime().toString());
			}
		}
		try {
			conn.close();
			statement.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 存储训练结果至TrainResult.csv
		// String file2Path = Main.BasicPath + "TrainSet.xls";
		// String file3Path = Main.BasicPath + "TrainResult.xls";
		File trainResult = new File(Main.trainResultPath);
		NeuralNetworkData neuralNetworkData = new NeuralNetworkData();
		try {
			neuralNetworkData.copyExcel(Main.trainFilePath,
					Main.trainResultPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// double[] desireOutput = trainingElement.getDesiredOutput();
		// double[] outputs = networkOutput;
		Workbook rwb;
		try {
			rwb = Workbook.getWorkbook(trainResult);
			WritableWorkbook wwb = Workbook.createWorkbook(trainResult, rwb);// copy
			WritableSheet ws = wwb.getSheet(0);

			for (int i = 0; i < outputs.size(); i++) {
				double output = outputs.get(i);
				Label label = new Label(13, i, "" + output);// 将预测结果添加到最后一列
				try {
					ws.addCell(label);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			wwb.write();
			try {
				wwb.close();
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// @Override
	// public void update(Observable arg0, Object arg1) {
	// SupervisedLearning rule = (SupervisedLearning)arg0;
	// System.out.println( "Training, Network Epoch " +
	// rule.getCurrentIteration() + ", Error:" + rule.getTotalNetworkError());
	// }

	@Override
	public void handleLearningEvent(LearningEvent event) {
		SupervisedLearning rule = (SupervisedLearning) event.getSource();
		System.out.println("Training, Network Epoch "
				+ rule.getCurrentIteration() + ", Error:"
				+ rule.getTotalNetworkError());
	}

}
