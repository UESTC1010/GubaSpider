package guba;

import java.util.Date;

/*网络型特征数据*/
public class NetFeature {

	Date startDate;
	Date endDate;
	
	//graph features:
	double edges;//图的边数
	double nodes;//图的点数
	double comments;
	double topics;
	double publishers;
	double connectedComponent;//连通分量
//	double maxDiameter;//最大直径
	
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
}
