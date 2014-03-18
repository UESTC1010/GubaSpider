/**
 * 
 */
package guba;


/**
 * @author Xiaoqing
 * 创建日期：2013-11-20下午2:34:52
 * 修改日期：
 */
public class GraphicEdge {
	EdgeType type;

	GraphicNode node1;
	GraphicNode node2;
	enum EdgeType {
		c2c, t2c, p2c, p2t
	}
	public GraphicEdge(GraphicNode node1, GraphicNode node2, EdgeType type) {
		this.type = type;
		this.node1 =node1;
		this.node2 =node2;
	}
	
		
}
