package guba;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class NeuralNetworkData {

	/**
	 * 创建神经网络训练样本数据（包括经济型和网络型） 神经网络训练样本的归一化处理
	 */

	// 神经网络训练样本的归一化处理,公式为Xi=(X-Xmin)/(Xmax-Xmin)
	public void normalization(File file2) throws IOException {
		// 找出每列的最大值
		Workbook wb = null;
		WritableWorkbook wwb = null;
		try {
			wb = Workbook.getWorkbook(file2);
		} catch (BiffException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		wwb = Workbook.createWorkbook(file2, wb);// copy
		// System.out.println("file2.length:"+file2.length());
		WritableSheet ws = wwb.getSheet(0);
		Sheet sheet = wwb.getSheet(0);// 获取工作对象
		int rowNum = sheet.getRows();// 获取当前工作表的行数
		int ColumnNum = sheet.getColumns();// 获取当前工作表的列数
		System.out.println("rowNum:" + rowNum + " " + ColumnNum);
		double tagMax = 0, tagMin = 0;// 仅仅用作占位
		double edgesMax = 0, edgesMin = 0;
		double nodesMax = 0, nodesMin = 0;
		double commentsMax = 0, commentsMin = 0;
		double topicsMax = 0, topicsMin = 0;
		double publishersMax = 0, publishersMin = 0;
		double connectedComponentMax = 0, connectedComponentMin = 0;
//		double maxDiameterMax = 0, maxDiameterMin = 0;
		double degree_avgMax = 0, degree_avgMin = 0;
		double degree_stdvMax = 0, degree_stdvMin = 0;
		double component_avgMax = 0, component_avgMin = 0;
		double component_stdvMax = 0, component_stdvMin = 0;
		double priceChangeMax = 0, priceChangeMin = 0;
		double[] maxName = { edgesMax, nodesMax, commentsMax, topicsMax,
				publishersMax, connectedComponentMax,
				degree_avgMax, degree_stdvMax, component_avgMax,
				component_stdvMax, priceChangeMax, tagMax };

		double[] minName = { edgesMin, nodesMin, commentsMin, topicsMin,
				publishersMin, connectedComponentMin,
				degree_avgMin, degree_stdvMin, component_avgMin,
				component_stdvMin, priceChangeMin, tagMin };
		// 获取每列的最大值最小值
		for (int i = 0; i < ColumnNum - 2; i++) {
			Cell[] cells = sheet.getColumn(i);
			// 将cell类型转换为double类型
			double[] data = new double[cells.length];
			for (int j = 0; j < cells.length; j++) {
				data[j] = Double.parseDouble(cells[j].getContents());
			}
			maxName[i] = obtainMax(data);
			minName[i] = obtainMin(data);
		}
		// 输入数据的归一化处理
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < ColumnNum - 2; j++) {
				double cell = Double.parseDouble(sheet.getCell(j, i)
						.getContents());
				double data;
				if (maxName[j] == minName[j])
					data = 0.5;
				else
					data = (cell - minName[j]) / (maxName[j] - minName[j]);
				// 保留4位小数
				// DecimalFormat df = new DecimalFormat("#.0000");
				// String data = df.format(dataTemp);
				Label labelC = new Label(j, i, "" + data);
				try {
					// 将生成的单元格添加到工作表中
					ws.addCell(labelC);
				} catch (RowsExceededException e) {
					e.printStackTrace();
				} catch (WriteException e) {
					e.printStackTrace();
				}
			}
		}
		// 将倒数第二列的价格涨跌进行三值化
		double[] priceChan = new double[rowNum];
		for (int j = 0; j < rowNum; j++) {
			double data = Double.parseDouble(sheet.getCell(ColumnNum - 2, j)
					.getContents());
			priceChan[j] = data;
		}
		java.util.Arrays.sort(priceChan);// 将数组排序
		// sort(priceChan);
		for (int j = 0; j < rowNum; j++) {
			String data = sheet.getCell(ColumnNum - 2, j).getContents();
			Label labelC = new Label(ColumnNum - 2, j, data);
			try {
				ws.addCell(labelC);
			} catch (RowsExceededException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}
		// 将第最后一列的日期加到表格中
		for (int j = 0; j < rowNum; j++) {
			String data = sheet.getCell(ColumnNum - 1, j).getContents();
			Label labelC = new Label(ColumnNum - 1, j, data);
			try {
				ws.addCell(labelC);
			} catch (RowsExceededException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}
		try {
			wwb.write();
			wwb.close();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 关闭资源，释放内存
	}

	// 数组获取最大值
	public double obtainMax(double[] a) {
		int mylength = a.length;
		double mymax = 0; // 最大值
		for (int i = 0; i < mylength; i++) {// 循环遍历数组
			if (i == 0) {
				mymax = a[0];
			}
			double tmp = a[i];
			if (tmp > mymax) {
				mymax = tmp;
			}
		}
		return mymax;
	}

	// 数组获取最小值
	public double obtainMin(double[] a) {
		int mylength = a.length;
		double mymin = 0; // 最小值
		for (int i = 0; i < mylength; i++) {// 循环遍历数组
			if (i == 0) {
				mymin = a[0];
			}
			double tmp = a[i];
			if (tmp < mymin) {
				mymin = tmp;
			}
		}
		return mymin;
	}

	// 复制excel文件
	public void copyExcel(String file1Path, String file2Path)
			throws FileNotFoundException {
		InputStream is = null;
		OutputStream os = null;
		int len;
		byte[] buff = new byte[1024];
		try {
			is = new FileInputStream(file1Path);
			os = new FileOutputStream(file2Path);

			try {
				while ((len = is.read(buff)) != -1) {
					os.write(buff, 0, len);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			try {
				if (is != null) {
					is.close();
					os.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	// 找出三等分的两个阀值1
	public double Tierce_Q1(double[] a) {
		double tierce_Q1 = 0;
		int length = a.length;
		int position = (int) Math.floor(length / 3);
		tierce_Q1 = a[position];
		return tierce_Q1;
	}

	// 找出三等分的两个阀值1
	public double Tierce_Q2(Double[] a) {
		double tierce_Q2 = 0;
		int length = a.length;
		int position = (int) Math.floor((length / 3) * 2);
		tierce_Q2 = a[position];
		return tierce_Q2;
	}

	public void saveData(Filter filter) {
		// 日期
		List<String> date = filter.financialDate;
		// 经济型数据
//		List<Double> tradVolume = filter.tradVolume;
//		List<Double> highPrice = filter.highPrice;
//		List<Double> lowPrice = filter.lowPrice;
//		List<Double> openPrice = filter.openPrice;
//		List<Double> closePrice = filter.closePrice;
		List<Double> priceChange = filter.priceChange;
//		List<Double> amplitude = filter.amplitude;// 振幅
//		List<Double> amount = filter.amount;// 交易总金额
//		List<Double> turnRate = filter.turnRate;// 换手率

		// 网络型数据
		List<Double> edges = filter.edges;// 图的边数
		List<Double> nodes = filter.nodes; // 图的点数
		List<Double> comments = filter.comments;
		List<Double> topics = filter.topics;
		List<Double> publishers = filter.publishers;
		List<Double> connectedComponent = filter.connectedComponent;// 图的连通分量
//		List<Double> maxDiameter = filter.maxDiameter;// 最大直径
		List<Double> degree_avg = filter.degree_avg;// 节点度的平均分布
		List<Double> degree_stdv = filter.degree_stdv;// 节点度的标准差分布
		List<Double> component_avg = filter.component_avg;// 连通分量的平均分布
		List<Double> component_stdv = filter.component_stdv;// 连通分量的标准差分布

		String[] name = { "edges", "nodes", "comments", "topics", "publishers",
				"connectedComponent", " degree_avg",
				"degree_stdv", "component_avg", " component_stdv",
				"priceChange", "date" };
		List[] a = { edges, nodes, comments, topics, publishers,
				connectedComponent, degree_avg, degree_stdv,
				component_avg, component_stdv, priceChange, date };

		WritableWorkbook book = null;
		try {
			book = Workbook.createWorkbook(new File(Main.stockCode+"DataBook.xls"));
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if (book != null) {
			WritableSheet ws = book.createSheet("sheet1", 0);

			int m = 0;// 想excel中添加数据的行
			int n = 0;// 想excel中添加数据的列
			int l = name.length;
			for (int i = 0; i < l; i++) {
				m = 0;// 一个属性完后行数归零
				Iterator iterator = a[i].iterator();
				while (iterator.hasNext()) {
					Object data = iterator.next();
					Label labelC = new Label(n, m, "" + data);
					try {
						// 将生成的单元格添加到工作表中
						ws.addCell(labelC);
					} catch (RowsExceededException e) {
						e.printStackTrace();
					} catch (WriteException e) {
						e.printStackTrace();
					}
					m++;
				}
				n++;
			}
		}
		try {
			// 从内存中写入文件中
			book.write();
			// 关闭资源，释放内存
			book.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}

	}

	public void saveAsCsv(File excelfile, File csvfile) {
		InputStream is = null;
		FileWriter fWriter = null;
		jxl.Workbook rwb = null;
		try {
			fWriter = new FileWriter(csvfile);

			is = new FileInputStream(excelfile);
			rwb = Workbook.getWorkbook(is);
			Sheet rsSheet = rwb.getSheet(0);
			int rsCloumns = rsSheet.getColumns();// 获取行数
			int rsRows = rsSheet.getRows();
			System.out.println("hang lie:" + rsCloumns + "  " + rsRows);
			for (int i = 0; i < rsRows; i++) {
				String a = "";
				for (int j = 0; j < rsCloumns; j++) {
					String temp = rsSheet.getCell(j, i).getContents();
					if (j != rsCloumns - 1) {
						a = a + temp + ",";
					} else {
						a = a + temp;
					}
					// System.out.println(temp + "——");
				}
					fWriter.write(a + "\n");
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				rwb.close();
				fWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}