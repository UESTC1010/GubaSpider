package guba;

import java.util.Date;

/*网络型特征数据*/
public class NetworkFeature {

	Date startDate;
	Date endDate;
	
	//graph features:
	double edges;//图的边数
	double nodes;//图的点数
	double comments;
	double topics;
	double publishers;
	double connectedComponent;//连通分量
	double maxDiameter;//最大直径
	
	double component_avg;//连通分量的的平均分布
	double component_stdv;//连通分量的的标准差分布
//	double component_quartiles_Q1;//四分位数
//	double component_quartiles_Q2;
//	double component_quartiles_Q3;
//	double component_skewness;//偏度
//	double component_kurtosis;//峰度
	
	double degree_avg;//节点度的平均分布
	double degree_stdv;//节点度的标准差分布
//	double degree_quartiles_Q1;//四分位数
//	double degree_quartiles_Q2;
//	double degree_quartiles_Q3;
//	double degree_skewness;//偏度
//	double degree_kurtosis;//峰度	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public double getEdges() {
		return edges;
	}
	public void setEdges(double edges) {
		this.edges = edges;
	}
	public double getNodes() {
		return nodes;
	}
	public void setNodes(double nodes) {
		this.nodes = nodes;
	}
	public double getComments() {
		return comments;
	}
	public void setComments(double comments) {
		this.comments = comments;
	}
	public double getTopics() {
		return topics;
	}
	public void setTopics(double topics) {
		this.topics = topics;
	}
	public double getPublishers() {
		return publishers;
	}
	public void setPublishers(double publishers) {
		this.publishers = publishers;
	}
	public double getConnectedComponent() {
		return connectedComponent;
	}
	public void setConnectedComponent(double connectedComponent) {
		this.connectedComponent = connectedComponent;
	}
	public double getMaxDiameter() {
		return maxDiameter;
	}
	public void setMaxDiameter(double maxDiameter) {
		this.maxDiameter = maxDiameter;
	}
	public double getComponent_avg() {
		return component_avg;
	}
	public void setComponent_avg(double component_avg) {
		this.component_avg = component_avg;
	}
	public double getComponent_stdv() {
		return component_stdv;
	}
	public void setComponent_stdv(double component_stdv) {
		this.component_stdv = component_stdv;
	}
	public double getDegree_avg() {
		return degree_avg;
	}
	public void setDegree_avg(double degree_avg) {
		this.degree_avg = degree_avg;
	}
	public double getDegree_stdv() {
		return degree_stdv;
	}
	public void setDegree_stdv(double degree_stdv) {
		this.degree_stdv = degree_stdv;
	}
	
	
}
