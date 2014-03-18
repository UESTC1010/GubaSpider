/**
 * 
 */
package guba;

import java.util.Date;

/**
 * @author Xiaoqing
 * 创建日期：2014-1-2下午5:20:07
 * 修改日期：
 */
//用于存储在数据库中的结果
public class ResultBean {

	String stockCode ;//预测的股票代码
	Date date;//预测的日期（result对应的）
	Float priceResult;//价格涨跌的预测结果
	public String getStockCode() {
		return stockCode;
	}
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Float getPriceResult() {
		return priceResult;
	}
	public void setPriceResult(Float priceResult) {
		this.priceResult = priceResult;
	}
	
}
