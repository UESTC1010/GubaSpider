/**
 * 
 */
package guba;

import java.util.Date;

/**
 * @author Xiaoqing
 * �������ڣ�2014-1-2����5:20:07
 * �޸����ڣ�
 */
//���ڴ洢�����ݿ��еĽ��
public class ResultBean {

	String stockCode ;//Ԥ��Ĺ�Ʊ����
	Date date;//Ԥ������ڣ�result��Ӧ�ģ�
	Float priceResult;//�۸��ǵ���Ԥ����
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
