/**
 * 
 */
package guba;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import com.mysql.jdbc.ResultSet;

/**
 * @author Xiaoqing �������ڣ�2014-3-11����5:38:39 �޸����ڣ�
 */
public class creatDatabase {

	public  static void main(String[] args) {
		// TODO Auto-generated method stub
		String driver = "com.mysql.jdbc.Driver";
		// URLָ��Ҫ���ʵ����ݿ���scutcs
		String url = "jdbc:mysql://127.0.0.1:3306/stock";
		// MySQL����ʱ���û���
		String user = "root";
		// MySQL����ʱ������
		String password = "123";
//		String[] stockCode = { "000157", "600010", "600016", "600030" };
		Connection conn = null;
		Statement statement = null;
		String sql = null;
		try {
			Class.forName(driver); // ������������
			// �������ݿ�
			conn = DriverManager.getConnection(url, user, password);
			// statement����ִ��SQL���
			statement = conn.createStatement();
			
			for (int i = 0; i < Main.stockCodes.length; i++) {

				sql = "create table stock"+Main.stockCodes[i]
						+" (`forcastdate` DATETIME,`testDate` DATETIME, `testprice` DOUBLE, `realprice` DOUBLE, `input` VARCHAR(250), `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,  PRIMARY KEY (`id`))ENGINE = InnoDB;";
			System.out.println(sql);
				statement.execute(sql);
			}
			
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
