/**
 * 
 */
package guba;

/**
 * @author Xiaoqing
 * �������ڣ�2014-3-11����9:54:21
 * �޸����ڣ�
 */
public class urlTest {

	public String inputFilePathString ="F:\\�μ�\\123.txt";
	
	public void print(){
		//String inputFileName_train = this.getClass().getResource(inputFilePathString).getFile();
		System.out.println(this.getClass());
		System.out.println(urlTest.class.getClassLoader().getResource("F:\\�μ�\\123.txt"));
		//System.out.println("inputFileName_train:" + inputFileName_train);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		urlTest test = new urlTest();
		test.print();
	}

}
