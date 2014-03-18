package crawl;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class PageHandle {
	public String downloadpage(String url){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpHost proxy = new HttpHost("60.190.138.151", 80, "http");
		
		HttpGet httpget = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.custom()
									.setProxy(proxy)
									.setConnectTimeout(30000)
									.setConnectionRequestTimeout(30000)
									.setSocketTimeout(30000)
									.setExpectContinueEnabled(true).build();
		httpget.setConfig(requestConfig);
		
		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
			@Override
			public String handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				int statuscode = response.getStatusLine().getStatusCode();
				if(HttpStatus.SC_OK == statuscode ){
					HttpEntity entity = response.getEntity();
					return entity != null? EntityUtils.toString(entity):null;
				}else {
                    throw new ClientProtocolException("Unexpected response status: " + statuscode);
                } 
			}
		};
		String responseBody = null;
		try {
			responseBody = httpclient.execute(httpget, responseHandler);
			//HandlerBody(responseBody);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			httpget.releaseConnection();
		}
		
		return responseBody;
	}
	
	public Date HandlerBody(String res){
		//do something about the context
		
		//String regex1 = "<a href=\"/shop/\\d{7}\" class=\"BL\" title=\"(.*?)\""; 
		//System.out.println(res);
		String regex1 = "发表时间：(.*?)<";
		Pattern pattern = Pattern.compile(regex1);
		Matcher matcher = pattern.matcher(res);
		//FileWriter fw = new FileWriter("D://foods.txt",true);
		
		while(matcher.find()){
			//System.out.println(matcher.group(1));
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");			
			try {
				Date publishtime = format.parse(matcher.group(1));
				return publishtime;
			} catch (ParseException e) {
				System.out.println("解析发帖时间错误");
				e.printStackTrace();
			}
			//fw.write(matcher.group(1)+"  \n");
			//fw.flush();
			
		}
		//fw.close();
		return null;
	}   
	public Date Handlerlastpage(String res){
		//do something about the context
		
		//String regex1 = "<a href=\"/shop/\\d{7}\" class=\"BL\" title=\"(.*?)\""; 
		//System.out.println(res);
		//System.out.println("---------------------------------------------");
		String regex1 = "zwlitxb\">(.*?)<";
		Pattern pattern = Pattern.compile(regex1);
		Matcher matcher = pattern.matcher(res);
		LinkedList<String> a = new LinkedList<String>();
		while(matcher.find()){	
			a.add(matcher.group(1));
		}
		if(!a.isEmpty())
		{
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");			
			try {
				Date updateDate = format.parse(a.getLast());
				return updateDate;
			} catch (ParseException e) {
				System.out.println("解析发帖时间错误");
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public static void main(String args[]){
		PageHandle commentpage = new PageHandle();
		String htmlcode1 = commentpage.downloadpage("http://guba.eastmoney.com/news,000157,90193439_16.html");
		Date tempTime1 = commentpage.Handlerlastpage(htmlcode1);
		System.out.println(tempTime1);
	}
}
