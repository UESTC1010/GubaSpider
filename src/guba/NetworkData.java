package guba;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NetworkData {

	public LinkedList<NetworkFeature> obtainNetData(Date startDate,int TEST_DAYS) {

		Date endDate = null;
		LinkedList<NetworkFeature> netFeatureList = new LinkedList<NetworkFeature>();
		for (int i = 0; i < TEST_DAYS; i++) {
			endDate = (Date) startDate.clone();
			endDate.setDate(endDate.getDate() + 1);//将当前时间后置一天
			GubaGraphic myGubaGraphic = new GubaGraphic();
			myGubaGraphic.establishGraphic(startDate, endDate);
		
			NetworkFeature net = new NetworkFeature();
			net.edges = myGubaGraphic.edges;
			net.startDate = myGubaGraphic.startDate;
			net.endDate = myGubaGraphic.endDate;
			net.nodes = myGubaGraphic.nodes;
			net.comments= myGubaGraphic.comments;
			net.publishers = myGubaGraphic.publishers;
			net.topics = myGubaGraphic.topics;
			GraphDeal graphDeal = new GraphDeal();
			net.connectedComponent = graphDeal.ConnectedComponent(myGubaGraphic);
			net.maxDiameter = graphDeal.flody(myGubaGraphic);

			Map<GraphicNode, Integer> nodeMap = graphDeal.degree(myGubaGraphic);
			net.degree_avg = graphDeal.degree_avg(nodeMap);
			net.degree_stdv=graphDeal.degree_stdv(nodeMap);
			
			List<Integer> component = graphDeal.componentList(myGubaGraphic);
			net.component_avg = graphDeal.component_avg(component);
			net.component_stdv = graphDeal.component_stdv(component);
			
			netFeatureList.add(net);
			
			startDate = endDate;
		}
		return netFeatureList;
	}
}
