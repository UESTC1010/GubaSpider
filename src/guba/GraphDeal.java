package guba;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

/*
 * 计算图的连通分量ConnectedComponent()
 * 计算图的最大直径MaxDiameter()....
 */
public class GraphDeal {

	static String START_DATE = "2012-07-01";
	static int TEST_DAYS = 90;
	static int M = Integer.MAX_VALUE;
    static int THRESHOLD = 8000;
	public static int MAXSUM(int a, int b) {
		return (a != M && b != M) ? (a + b) : M;
	}

	// Set<Integer> connectedComponent = new HashSet<Integer>();
	private Stack<GraphicNode> theStack = new Stack<GraphicNode>();// 定义一个栈，搜索时用

	// 计算连通分量
	public long ConnectedComponent(GubaGraphic myGubaGraphic) {

		Map<GraphicNode, Boolean> nodeflag = new HashMap<GraphicNode, Boolean>();
		nodeflag = setToMap(myGubaGraphic.nodeSet);
		Iterator iterator = nodeflag.entrySet().iterator();

		long count = 0;

		while (iterator.hasNext()) {
			Entry<GraphicNode, Boolean> e = (Entry<GraphicNode, Boolean>) iterator
					.next();
			if (e.getValue() == false) {
				DFS(e.getKey(), nodeflag, myGubaGraphic.edgeSet);
				count++;
			}
		}
		return count;
	}

	// 最短路径Floyd算法计算最大直径（任意两点之间最短路径中最长的即为最大直径）
	public long flody(GubaGraphic mygGubaGraphic) {
		Set<GraphicNode> nodeSet = mygGubaGraphic.nodeSet;
		Set<GraphicEdge> edgeSet = mygGubaGraphic.edgeSet;
		Integer[][] dist = mapToMatrix(nodeSet, edgeSet);
		int length = dist.length;
		for (int k = 0; k < length; k++) {
			for (int i = 0; i < length; i++) {
				for (int j = 0; j < length; j++) {
					if (dist[i][j] > MAXSUM(dist[i][k], dist[k][j])) {
						dist[i][j] = MAXSUM(dist[i][k], dist[k][j]);
					}
				}
			}
		}
		int MaxDiameter = findMaxDiameterFromMatrix(dist);
		return MaxDiameter;
	}
	
	//连通分量的平均值COMPONENT AVG
	public double component_avg(List<Integer> component)
	{
		double component_avg=0;
		int length = component.size();
		long sum = 0;
		Iterator<Integer> iterator = component.iterator();
		while (iterator.hasNext()) {
			int i = iterator.next();
			sum = sum+i;		
		}
		if(length!=0){
		component_avg= sum/length;		
		}
		else{
			component_avg = 0;
		}
		return component_avg;
	}
	
	//连通分量的标准差COMPONENT STDV
	public double component_stdv(List<Integer> component)
	{
		double component_stdv =0;
		double component_avg = component_avg(component);
		double sum =0;
		Iterator<Integer> iterator = component.iterator();
		while(iterator.hasNext())
		{
			long i = iterator.next();
			sum = sum + (i-component_avg)*(i-component_avg);
		}
		if(component.size()!= 0){
			component_stdv = Math.sqrt(sum/(component.size()));
		}
		else{
			component_stdv = 0;
		}
		return component_stdv;
	}
	
	//连通分量的四分位数——Q1：COMPONENT QUARTILES
	public double component_quartiles_Q1(List<Integer> component)
	{
		double component_quartiles_Q1 = 0;
		int[] compo = componentArray(component);//连通子图节点数并从小到大排序的数组
		int length = compo.length;
		
		double position;
		position = (length + 1) / 4;
		component_quartiles_Q1 = compo[(int) Math.floor(position)-1]
				+ (position - Math.floor(position))
				* (compo[(int) Math.ceil(position)-1]-compo[(int) Math.floor(position)-1]);
		return component_quartiles_Q1;	
	}
	
	//连通分量的四分位数——Q2：COMPONENT QUARTILES
	public double component_quartiles_Q2(List<Integer> component)
	{
		double component_quartiles_Q2 = 0;
		int[] compo = componentArray(component);//连通子图节点数并从小到大排序的数组
		int length = compo.length;
		
		double position;
		position = 2*(length + 1) / 4;
		component_quartiles_Q2 = compo[(int) Math.floor(position)-1]
				+ (position - Math.floor(position))
				* (compo[(int) Math.ceil(position)-1]-compo[(int) Math.floor(position) -1]);
		return component_quartiles_Q2;	
	}
	 
	//连通分量的四分位数——Q3：COMPONENT QUARTILES
	public double component_quartiles_Q3(List<Integer> component)
	{
//		System.out.println("hhha"+component.size());
		double component_quartiles_Q3 = 0;
		int[] compo = componentArray(component);//连通子图节点数并从小到大排序的数组
		int length = compo.length;
		
		double position;
		position = 3*(length + 1) / 4;
		try{
		    component_quartiles_Q3 = compo[ (int) Math.floor(position)-1] + (position - Math.floor(position)) * (compo[(int) Math.ceil(position)-1]-compo[(int) Math.floor(position)-1]);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return component_quartiles_Q3;	
	}

	//连通分量的偏度 COMPONENT SKEWNESS
	public double component_skewness(List<Integer> component)
	{
		double skewness= 0;
		double avg = component_avg(component);
		double stdv = component_stdv(component);
		int[] com = componentArray(component);
		int length = com.length;
		double sum = 0;
		for(int i =0;i<length-1;i++)
		{
			sum = sum + Math.pow(com[i]-avg, 3);
		}
		try {
			skewness = length*sum/((length-1)*(length-2)*Math.pow(stdv, 3));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return skewness;
	}
	
	//连通分量的峰度 COMPONENT KURTOSIS
	public double component_kurtosis(List<Integer> component)
	{
		double kurtosis = 0;
		double avg = component_avg(component);
		double stdv = component_stdv(component);
		double sum = 0;
		int[] com = componentArray(component);
		int l = com.length;
		for (int i = 0; i < l - 1; i++) {
			sum = sum + Math.pow(com[i] - avg, 4);
		}
		try{
			System.out.println("GraphDeal 213th l:"+l);
			System.out.println("GraphDeal 214th s:"+stdv);
		kurtosis = l * (l + 1) * sum
				/ ((l - 1) * (l - 2) * (l - 3) * Math.pow(stdv, 4)) - 3
				* (l - 1) * (l - 1) / ((l - 2) * (l - 3));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return kurtosis;
	}
	
	//节点度的平均值 DEGREE AVG
 	public double degree_avg(Map<GraphicNode, Integer> nodeMap)
	{
		double avg=0;
		long sum =0;
		Iterator iterator = nodeMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<GraphicNode, Integer> entry = (Entry<GraphicNode, Integer>) iterator.next();
			int i = entry.getValue();
			sum = sum+i;
		}
		long m = nodeMap.size();
		if(m==0)
		{
			avg = 0;
		}
		else {
			avg = sum / m;
		} 
		return avg;
	}
		
	//节点度的标准差 DEGREE STDV
	public double degree_stdv(Map<GraphicNode, Integer> nodeMap)
	{
		double stdv=0;
		double avg = degree_avg(nodeMap);
		double sum = 0;
		Iterator iterator = nodeMap.entrySet().iterator();
		while(iterator.hasNext())
		{
			Entry<GraphicNode, Integer> entry = (Entry<GraphicNode, Integer>) iterator.next();
			int i = entry.getValue();
			sum = (i -avg )*(i -avg);
		}
		if(nodeMap.size()!=0){
			stdv = Math.sqrt(sum/(nodeMap.size()));
		}
		else
		{
			stdv=0;
		}
		return stdv;
	}
	
	//节点度的四分位数——Q1：DEGREE QUARTILES
	public double degree_quartiles_Q1(Map<GraphicNode, Integer> nodeMap) {
		double quartiles_Q1 = 0;
		int[] degree = degreeArray(nodeMap);
		int length = degree.length;

		double position;// Q1的位置
		position = (length + 1) / 4;
		quartiles_Q1 = degree[(int) Math.floor(position)-1]
				+ (position - Math.floor(position))
				* (degree[(int) Math.ceil(position)-1]-degree[(int) Math.floor(position)-1]);
		return quartiles_Q1;
	}
	
	//节点度的四分位数——Q2：DEGREE QUARTILES
	public double degree_quartiles_Q2(Map<GraphicNode, Integer> nodeMap) {
		double quartiles_Q2 = 0;
		int[] degree = degreeArray(nodeMap);
		int length = degree.length;

		double position;// Q1的位置
		position = 2*(length + 1) / 4;
		quartiles_Q2 = degree[(int) Math.floor(position)-1]
				+ (position - Math.floor(position))
				* (degree[(int) Math.ceil(position)-1]-degree[(int) Math.floor(position)-1]);
		return quartiles_Q2;
	}
	
	//节点度的四分位数——Q3：DEGREE QUARTILES
	public double degree_quartiles_Q3(Map<GraphicNode, Integer> nodeMap) {
		double quartiles_Q3 = 0;
		int[] degree = degreeArray(nodeMap);
		int length = degree.length;

		double position;// Q1的位置
		position = 3*(length + 1) / 4;
		quartiles_Q3 = degree[(int) Math.floor(position)-1]
				+ (position - Math.floor(position))
				* (degree[(int) Math.ceil(position)-1]-degree[(int) Math.floor(position)-1]);
		return quartiles_Q3;
	}
	
	//节点度的偏度 DEGREE SKEWNESS
	public double degree_skewness(Map<GraphicNode, Integer> nodeMap)
	{
		double skewness=0;
		double avg = degree_avg(nodeMap);//平均值
		double stdv = degree_stdv(nodeMap);//标准差
		int length = nodeMap.size();
		double sum = 0;
		int[] degree = degreeArray(nodeMap);
		for(int i =0;i<length-1;i++)
		{
			sum = sum + Math.pow(degree[i]-avg, 3);
		}
		try {
			skewness = length*sum/((length-1)*(length-2)*Math.pow(stdv, 3));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}		
		return skewness;
	}
	
	//节点的峰度DEGREE KURTOSIS
	public double degree_kurtosis(Map<GraphicNode, Integer> nodeMap) {
		double kurtosis = 0;
		double avg = degree_avg(nodeMap);
		double stdv = degree_stdv(nodeMap);
		double sum = 0;
		int[] degree = degreeArray(nodeMap);
		int l = degree.length;
		for (int i = 0; i < l - 1; i++) {
			sum = sum + Math.pow(degree[i] - avg, 4);
		}
		kurtosis = l * (l + 1) * sum
				/ ((l - 1) * (l - 2) * (l - 3) * Math.pow(stdv, 4)) - 3
				* (l - 1) * (l - 1) / ((l - 2) * (l - 3));
		return kurtosis;
	}
	

 	//给节点加上标志位，并初始化为false
	public Map<GraphicNode, Boolean> setToMap(Set<GraphicNode> nodeSet) {
		Iterator<GraphicNode> iterator = nodeSet.iterator();
		Map<GraphicNode, Boolean> nodeflag = new HashMap<GraphicNode, Boolean>();
		while (iterator.hasNext()) {
			GraphicNode temp = iterator.next();
			boolean flag = false;
			nodeflag.put(temp, flag);
		}
		return nodeflag;
	}

	// 给node加上序号
	public Map<GraphicNode, Integer> nodeAddMap(Set<GraphicNode> nodeSet) {
		Iterator<GraphicNode> iterator = nodeSet.iterator();
		Map<GraphicNode, Integer> nodeMap = new HashMap<GraphicNode, Integer>();
		for (int i = 0; iterator.hasNext(); i++) {
			GraphicNode temp = iterator.next();
			int flag = i;
			nodeMap.put(temp, flag);
		}
		return nodeMap;
	}

	//深度优先搜索,并返回该子图的节点数
	public int DFS(GraphicNode v, Map<GraphicNode, Boolean> nodeflag,
			Set<GraphicEdge> edgeset) { // 深度优先搜索

		int count =1;//该子图的节点数
		nodeflag.put(v, true);// 设置标志位为true

		theStack.push(v);// 压栈

		while (!theStack.isEmpty()) {
			// 查看栈顶元素是否有邻接点
			GraphicNode linkV = getAdjUnvisitedVertex(nodeflag, edgeset,
					theStack.peek());// peek()方法
			// 查看栈顶对象而不移除它
			if (linkV == null) {
				theStack.pop();// 没有邻接点就出栈
			} else {
				// DFS(linkV, nodeflag, edgeset);
				count++;
				nodeflag.put(linkV, true);
				theStack.push(linkV);
			}
		}
		return count;
	}

	//找到一个点的邻接点
	public GraphicNode getAdjUnvisitedVertex(
			Map<GraphicNode, Boolean> nodeflag, Set<GraphicEdge> edgeset,
			GraphicNode v) {
		Iterator<GraphicEdge> iterator = edgeset.iterator();

		GraphicNode graphicNode = null;// 返回的邻接点

		while (iterator.hasNext()) {
			GraphicEdge graphicedge = (GraphicEdge) iterator.next();
			if ((graphicedge.node1 == v) && (graphicedge.node2 != null)
					&& (nodeflag.get(graphicedge.node2) == false)) {
				graphicNode = graphicedge.node2;
				break;
			} else if ((graphicedge.node2 == v) && (graphicedge.node1 != null)
					&& (nodeflag.get(graphicedge.node1) == false)) {
				graphicNode = graphicedge.node1;
				break;
			}
		}
		return graphicNode;
	}

	//将图转化成一个矩阵，赋边的权值为1
	public Integer[][] mapToMatrix(Set<GraphicNode> nodeSet,
			Set<GraphicEdge> edgeSet) {
		// 将图转化成一个矩阵，赋边的权值为1
		Map<GraphicNode, Integer> nodeMap = nodeAddMap(nodeSet);
		int length = nodeSet.size();
		Integer[][] dist = new Integer[length][length];

		// 给数组赋初值，全为最大M
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (i != j)
					dist[i][j] = M;
				else
					dist[i][j] = 0;

			}

		}
		Iterator<GraphicEdge> iterator = edgeSet.iterator();
		while (iterator.hasNext()) {
			GraphicEdge edge = iterator.next();
			GraphicNode node1 = edge.node1;
			GraphicNode node2 = edge.node2;
			int i = 0;
			int j = 0;
			Iterator iterator2 = nodeMap.entrySet().iterator();
			while (iterator2.hasNext()) {
				Entry<GraphicNode, Integer> entry = (Entry<GraphicNode, Integer>) iterator2
						.next();
				if (entry.getKey() == node1)
					i = entry.getValue();
				if (entry.getKey() == node2)
					j = entry.getValue();
			}
			dist[i][j] = 1;
			dist[j][i] = 1;
		}

		return dist;

	}

	//从矩阵中找到最短路径中最长的
	public  int findMaxDiameterFromMatrix(Integer[][] dist) {
		int MaxDiameter = 0;
		int length = dist.length;
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if ((dist[i][j] > MaxDiameter) && (dist[i][j] != M))
					MaxDiameter = dist[i][j];
			}
		}
		return MaxDiameter;
	}

	//计算一个图中点的度，并用图来存储，key为点，value为度的值
	public Map<GraphicNode, Integer>  degree(GubaGraphic myGubaGraphic)
	{
		List<Integer> degreeList = new LinkedList<Integer>();
		Set<GraphicNode> nodeSet = myGubaGraphic.nodeSet;
		Set<GraphicEdge> edgeSet = myGubaGraphic.edgeSet;
		
		//给节点的度赋初值，并用map来存储
		 Iterator<GraphicNode> nodeIterator = nodeSet.iterator();
		 Map<GraphicNode, Integer> nodeMap = new HashMap<GraphicNode, Integer>();
		 while(nodeIterator.hasNext())
		 {
			 GraphicNode node =nodeIterator.next();
			 nodeMap.put(node, 0);
		 }
		 
		 Iterator<GraphicEdge> edgeIterator = edgeSet.iterator();
		 while(edgeIterator.hasNext())
		 {
			 GraphicEdge edge = edgeIterator.next();
			 GraphicNode node1=edge.node1;
			 GraphicNode node2 = edge.node2;
			 
			 Iterator it = nodeMap.entrySet().iterator();
			 while (it.hasNext()) {
				 Entry<GraphicNode, Integer> entry = (Entry<GraphicNode, Integer>) it.next();
				 if(entry.getKey()==node1)
				 {
					 int i = entry.getValue();
					 nodeMap.put(node1, i+1);
				 }
				if(entry.getKey()==node2)
				{
					int j=entry.getValue();
					nodeMap.put(node2, j+1);
				}
			}
		 }				 		 
		return nodeMap;
	}
	
	//将点的度进行从小到大的排序，并用数组存储
	public int[] degreeArray(Map<GraphicNode, Integer> nodeMap)
	{
		int length = nodeMap.size();
		int[] node =new int[length];
		int j=0;
		Iterator iterator = nodeMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<GraphicNode, Integer> entry = (Entry<GraphicNode, Integer>) iterator.next();
			int i = entry.getValue();
			node[j] = i;
			j++;		
		}
		//对node[]进行从小到大的排序
		sort(node);
		return node;
	}
	
	//将用list存储的连通子图的点数 进行从小到大的排序，并返回数组
	public int[] componentArray(List<Integer> component)
	{
		int  length = component.size();
		int[] count = new int[length];
		int j =0;
		Iterator<Integer> iterator = component.iterator();
		while(iterator.hasNext())
		{
			int i = iterator.next();
			count[j] = i;
			j++;
		}
		sort(count);
		return count;
	}
	
	//对数组进行排序
    public  void sort(int[] data) {
        int[] temp=new int[data.length];
        mergeSort(data,temp,0,data.length-1);
    }
    private  void mergeSort(int[] data, int[] temp, int l, int r) {
        int i, j, k;
        int mid = (l + r) / 2; 


        if (l == r)
            return;
        if ((mid - l) >= THRESHOLD)
            mergeSort(data, temp, l, mid);
        else
            insertSort(data, l, mid - l + 1);
        if ((r - mid) > THRESHOLD)
            mergeSort(data, temp, mid + 1, r);
        else
            insertSort(data, mid + 1, r - mid);

        for (i = l; i <= mid; i++) {
            temp[i] = data[i];
        } 

        for (j = 1; j <= r - mid; j++) {
            temp[r - j + 1] = data[j + mid];
        }
        int a = temp[l];
        int b = temp[r];
        for (i = l, j = r, k = l; k <= r; k++) {
            if (a < b) {
                data[k] = temp[i++];
                a = temp[i];
            } else {
                data[k] = temp[j--];
                b = temp[j]; 

            }
        }
    }
    private  void insertSort(int[] data, int start, int len) {
        for(int i=start+1;i<start+len;i++){
            for(int j=i;(j>start) && data[j]<data[j-1];j--){
                swap(data,j,j-1);
            }
        }
    } 
    public static void swap(int[] data, int i, int j) { 
        int temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }	

    //得到一个图的各个连通子图的节点数
    public  List<Integer> componentList(GubaGraphic myGubaGraphic)
    {
		Map<GraphicNode, Boolean> nodeflag = new HashMap<GraphicNode, Boolean>();
		nodeflag = setToMap(myGubaGraphic.nodeSet);
		Iterator iterator = nodeflag.entrySet().iterator();

		List<Integer> component = new LinkedList<Integer>();
		int i =0;
		while (iterator.hasNext()) {
			Entry<GraphicNode, Boolean> e = (Entry<GraphicNode, Boolean>) iterator
					.next();
			if (e.getValue() == false) {
				i=DFS(e.getKey(), nodeflag, myGubaGraphic.edgeSet);
				component.add(i);
			}
		}
		return component;
    }

    // 打印矩阵
	public  void printDist(Integer[][] dist) {
		for (int i = 0; i < dist.length; i++) {
			for (int j = 0; j < dist[i].length; j++) {
				System.out.println("dist[" + i + "][" + j + "]:" + dist[i][j]
						+ " ");
			}
		}
	}

	// 连通分量计算的测试代码
	// public static void main(String args[]) {
	//
	// Publisher publisher1 = new Publisher();
	// publisher1.name = "11";
	// publisher1.commentAmount = 1;
	// publisher1.topicAmount = 1;
	//
	// Publisher publisher2 = new Publisher();
	// publisher2.name = "22";
	// publisher2.commentAmount = 2;
	// publisher2.topicAmount = 2;
	//
	// Publisher publisher3 = new Publisher();
	// publisher3.name = "33";
	// publisher3.commentAmount = 3;
	// publisher3.topicAmount = 3;
	//
	// Publisher publisher4 = new Publisher();
	// publisher4.name = "44";
	// publisher4.commentAmount = 4;
	// publisher4.topicAmount = 4;
	//
	// Publisher publisher5 = new Publisher();
	// publisher5.name = "55";
	// publisher5.commentAmount = 5;
	// publisher5.topicAmount = 5;
	//
	// Publisher publisher6 = new Publisher();
	// publisher6.name = "6";
	// publisher6.commentAmount = 6;
	// publisher6.topicAmount = 6;
	//
	// Map<GraphicNode, Boolean> nodeflag = new HashMap<GraphicNode, Boolean>();
	// nodeflag.put(publisher1, false);
	// nodeflag.put(publisher2, false);
	// nodeflag.put(publisher3, false);
	// nodeflag.put(publisher4, false);
	// nodeflag.put(publisher5, false);
	// nodeflag.put(publisher6, false);
	//
	// Set<GraphicNode> nodeset = new HashSet<>();
	// nodeset.add(publisher6);
	// nodeset.add(publisher5);
	// nodeset.add(publisher4);
	// nodeset.add(publisher3);
	// nodeset.add(publisher2);
	// nodeset.add(publisher1);
	//
	// Set<GraphicEdge> edgeSet = new HashSet<GraphicEdge>();
	// edgeSet.add(new GraphicEdge(publisher1, publisher2, EdgeType.c2c));
	// edgeSet.add(new GraphicEdge(publisher2, publisher3, EdgeType.c2c));
	// edgeSet.add(new GraphicEdge(publisher3, publisher5, EdgeType.p2c));
	// edgeSet.add(new GraphicEdge(publisher4, publisher5, EdgeType.p2c));
	//
	// Iterator iterator = nodeflag.entrySet().iterator();
	//
	// int count = 0;
	//
	// while (iterator.hasNext()) {
	// Map.Entry<GraphicNode, Boolean> e = (Map.Entry<GraphicNode, Boolean>)
	// iterator
	// .next();
	// if ((e.getValue() == false)) {
	// count++;
	// DFS(e.getKey(), nodeflag, edgeSet);
	//
	// }
	// }
	// System.out.println("该天的连通分量count:" + count);
	// // System.out.println("最大直径为："+flody(nodeset, edgeSet));
	//
	// }

}