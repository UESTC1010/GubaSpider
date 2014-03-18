package guba;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Filter {

	// a,b,c 分别存储经济数据的交易量，一天中的最高价格、最低价格
	List<String> financialDate = new LinkedList<String>();
	List<Double> tradVolume = new LinkedList<Double>();// 交易量
	List<Double> highPrice = new LinkedList<Double>();// 一天中的最高价格
	List<Double> lowPrice = new LinkedList<Double>();// 一天中的最第价格
	List<Double> openPrice = new LinkedList<Double>();// 一天的开盘价格
	List<Double> closePrice = new LinkedList<Double>();// 一天的收盘价格
	List<Double> priceChange = new LinkedList<Double>();// 一天价格的涨幅（）
	List<Double> amplitude = new LinkedList<Double>();// 振幅
	List<Double> amount = new LinkedList<Double>();// 交易总金额
	List<Double> turnRate = new LinkedList<Double>();// 换手率

	// d，e分别存储网络数据构成图的边数，点数
	List<Double> edges = new LinkedList<Double>();// 图的边数
	List<Double> nodes = new LinkedList<Double>(); // 图的点数
	List<Double> comments = new LinkedList<Double>();
	List<Double> topics = new LinkedList<Double>();
	List<Double> publishers = new LinkedList<Double>();
	List<Double> connectedComponent = new LinkedList<Double>();// 图的连通分量
	List<Double> maxDiameter = new LinkedList<Double>();// 最大直径

	List<Double> degree_avg = new LinkedList<Double>();// 节点度的平均分布
	List<Double> degree_stdv = new LinkedList<Double>();// 节点度的标准差分布
	// List<Double> degree_quartiles_Q1 = new LinkedList<>();//四分位数
	// List<Double> degree_quartiles_Q2 = new LinkedList<>();
	// List<Double> degree_quartiles_Q3 = new LinkedList<>();
	// List<Double> degree_skewness = new LinkedList<>();//偏度
	// List<Double> degree_kurtosis = new LinkedList<>();//峰度

	List<Double> component_avg = new LinkedList<Double>();// 连通分量的平均分布
	List<Double> component_stdv = new LinkedList<Double>();// 连通分量的标准差分布
	// List<Double> component_quartiles_Q1 = new LinkedList<>();//四分位数
	// List<Double> component_quartiles_Q2 = new LinkedList<>();
	// List<Double> component_quartiles_Q3 = new LinkedList<>();
	// List<Double> component_skewness = new LinkedList<>();//偏度
	// List<Double> component_kurtosis = new LinkedList<>();//峰度

	List[] a = { tradVolume, highPrice, lowPrice, openPrice, closePrice,
			priceChange, amplitude, amount, turnRate };// 该这里的话记得改下面对应的name

	static String[] a_name = { "tradVolume", "highPrice", "lowPrice",
			"openPrice", "closePrice", "priceChange", "amplitude", "amount",
			"turnRate " };

	List[] b = { edges, nodes, comments, topics, publishers,
			connectedComponent, maxDiameter, degree_avg, degree_stdv,
			component_avg, component_stdv };// 该这里的话记得改下面对应的name

	static String[] b_name = { "edges", "nodes", "comments", "topics",
			"publishers", "connectedComponent", " maxDiameter", " degree_avg",
			"degree_stdv", "component_avg", " component_stdv"};

	public void data(List<FinancialFeature> financialList,
			List<NetFeature> netList, int t) {
		int M = financialList.size();
		int N = netList.size();
		if (t >= 0) {
			for (int i = t; i < M; i++) {
				if (financialList.get(i) != null) {// 非停盘的日子
					DateFormat df = new SimpleDateFormat("yyy-MM-dd-EE");
					financialDate.add(df.format(financialList.get(i).date));
//					tradVolume.add(financialList.get(i).tradVolume);
//					highPrice.add(financialList.get(i).highprice);
//					lowPrice.add(financialList.get(i).lowprice);
//					openPrice.add(financialList.get(i).openPrice);
//					closePrice.add(financialList.get(i).closePrice);
					priceChange.add(financialList.get(i).priceChange);
//					amplitude.add(financialList.get(i).amplitude);
//					amount.add(financialList.get(i).amount);
//					turnRate.add(financialList.get(i).turnRate);
				}
			}
			for (int i = t; i < N; i++) {
				if (financialList.get(i) != null) {// 非停盘的日子才获取对应的网络数据
					edges.add(netList.get(i - t).edges);
					nodes.add(netList.get(i - t).nodes);
					comments.add(netList.get(i - t).comments);
					publishers.add(netList.get(i - t).publishers);
					topics.add(netList.get(i - t).topics);
					connectedComponent
							.add(netList.get(i - t).connectedComponent);
//					maxDiameter.add(netList.get(i - t).maxDiameter);
					degree_avg.add(netList.get(i - t).degree_avg);
					degree_stdv.add(netList.get(i - t).degree_stdv);
					// degree_quartiles_Q1.add(netList.get(i-t).degree_quartiles_Q1);
					// degree_quartiles_Q2.add(netList.get(i-t).degree_quartiles_Q2);
					// degree_quartiles_Q3.add(netList.get(i-t).degree_quartiles_Q3);
					// degree_skewness.add(netList.get(i-t).degree_skewness);
					// degree_kurtosis.add(netList.get(i-t).degree_kurtosis);

					component_avg.add(netList.get(i - t).component_avg);
					component_stdv.add(netList.get(i - t).component_stdv);
					// component_quartiles_Q1.add(netList.get(i-t).component_quartiles_Q1);
					// component_quartiles_Q2.add(netList.get(i-t).component_quartiles_Q2);
					// component_quartiles_Q3.add(netList.get(i-t).component_quartiles_Q3);
					// component_skewness.add(netList.get(i-t).component_skewness);
					// component_kurtosis.add(netList.get(i-t).component_kurtosis);
				}
			}
		} else {
			for (int i = 0; i < M + t; i++) {
				if (financialList.get(i) != null) {// 非停盘的日子
					DateFormat df = new SimpleDateFormat("yyy-MM-dd-EE");
					financialDate.add(df.format(financialList.get(i).date));
//					tradVolume.add(financialList.get(i).tradVolume);
//					highPrice.add(financialList.get(i).highprice);
//					lowPrice.add(financialList.get(i).lowprice);
//					openPrice.add(financialList.get(i).openPrice);
//					closePrice.add(financialList.get(i).closePrice);
					priceChange.add(financialList.get(i).priceChange);
//					amplitude.add(financialList.get(i).amplitude);
//					amount.add(financialList.get(i).amount);
//					turnRate.add(financialList.get(i).turnRate);
				}
			}

			for (int i = 0; i < N + t; i++) {
				if (financialList.get(i) != null) {// 非停盘的日子才获取对应的网络数据
					edges.add(netList.get(i - t).edges);
					nodes.add(netList.get(i - t).nodes);
					comments.add(netList.get(i - t).comments);
					publishers.add(netList.get(i - t).publishers);
					topics.add(netList.get(i - t).topics);
					connectedComponent
							.add(netList.get(i - t).connectedComponent);
//					maxDiameter.add(netList.get(i - t).maxDiameter);
					degree_avg.add(netList.get(i - t).degree_avg);
					degree_stdv.add(netList.get(i - t).degree_stdv);
					// degree_quartiles_Q1.add(netList.get(i-t).degree_quartiles_Q1);
					// degree_quartiles_Q2.add(netList.get(i-t).degree_quartiles_Q2);
					// degree_quartiles_Q3.add(netList.get(i-t).degree_quartiles_Q3);
					// degree_skewness.add(netList.get(i-t).degree_skewness);
					// degree_kurtosis.add(netList.get(i-t).degree_kurtosis);

					component_avg.add(netList.get(i - t).component_avg);
					component_stdv.add(netList.get(i - t).component_stdv);
					// component_quartiles_Q1.add(netList.get(i-t).component_quartiles_Q1);
					// component_quartiles_Q2.add(netList.get(i-t).component_quartiles_Q2);
					// component_quartiles_Q3.add(netList.get(i-t).component_quartiles_Q3);
					// component_skewness.add(netList.get(i-t).component_skewness);
					// component_kurtosis.add(netList.get(i-t).component_kurtosis);
				}
			}
		}
	}
}
