/**
 * 
 */
package guba;

/**
 * @author Xiaoqing
 * 创建日期：2014-3-11下午9:54:21
 * 修改日期：
 */
public class urlTest {

	public String inputFilePathString ="F:\\课件\\123.txt";
	
	public void print(){
		//String inputFileName_train = this.getClass().getResource(inputFilePathString).getFile();
		System.out.println(this.getClass());
		System.out.println(urlTest.class.getClassLoader().getResource("F:\\课件\\123.txt"));
		//System.out.println("inputFileName_train:" + inputFileName_train);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		urlTest test = new urlTest();
		test.print();
	}

}
