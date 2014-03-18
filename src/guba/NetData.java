package guba;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NetData {

//	static String START_DATE = "2012-06-01";
//	static int TEST_DAYS = 150;

	public LinkedList<NetFeature> obtainNetData(Date startDate,int TEST_DAYS) {
		Date endDate = null;

		LinkedList<NetFeature> netFeatureList = new LinkedList<NetFeature>();
		
//		startDate = (Date) endDate.clone();
		for (int i = 0; i < TEST_DAYS; i++) {
			endDate = (Date) startDate.clone();
			endDate.setDate(endDate.getDate() + 1);//将当前时间后置一天
			GubaGraphic myGubaGraphic = new GubaGraphic();
			myGubaGraphic.establishGraphic(startDate, endDate);
		
			NetFeature net = new NetFeature();
			net.edges = myGubaGraphic.edges;
			net.startDate = myGubaGraphic.startDate;
			net.endDate = myGubaGraphic.endDate;
			net.nodes = myGubaGraphic.nodes;
			net.comments= myGubaGraphic.comments;
			net.publishers = myGubaGraphic.publishers;
			net.topics = myGubaGraphic.topics;
			GraphDeal graphDeal = new GraphDeal();
//			graphDeal.dawMap(myGubaGraphic);
			net.connectedComponent = graphDeal.ConnectedComponent(myGubaGraphic);
//			net.maxDiameter = graphDeal.flody(myGubaGraphic);

			Map<GraphicNode, Integer> nodeMap = graphDeal.degree(myGubaGraphic);
			net.degree_avg = graphDeal.degree_avg(nodeMap);
			net.degree_stdv=graphDeal.degree_stdv(nodeMap);
//			System.out.print(graphDeal.degree_stdv(nodeMap)+" ");
//			net.degree_quartiles_Q1=graphDeal.degree_quartiles_Q1(nodeMap);
//			net.degree_quartiles_Q2=graphDeal.degree_quartiles_Q2(nodeMap);
//			net.degree_quartiles_Q3=graphDeal.degree_quartiles_Q3(nodeMap);
//			net.degree_skewness = graphDeal.degree_skewness(nodeMap);
//			net.degree_kurtosis = graphDeal.degree_kurtosis(nodeMap);
			
			List<Integer> component = graphDeal.componentList(myGubaGraphic);
			net.component_avg = graphDeal.component_avg(component);
			net.component_stdv = graphDeal.component_stdv(component);
//			net.component_quartiles_Q1 = graphDeal.component_quartiles_Q1(component);
//			net.component_quartiles_Q2 = graphDeal.component_quartiles_Q2(component);
//			net.component_quartiles_Q3 = graphDeal.component_quartiles_Q3(component);
//			net.component_skewness = graphDeal.component_skewness(component);
//			net.component_kurtosis = graphDeal.component_kurtosis(component);
			
			netFeatureList.add(net);
			
			startDate = endDate;
		}
		return netFeatureList;
	}
}
