package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
/**
 * JDBC的通用类
 * @author Administrator
 */
public class JdbcTemplate {
	
	private static String driver;
	private static String url;
	private static String usename;
	private static String password;
	private Connection conn=null;
	private ResultSet rs=null;
	private PreparedStatement ps=null;
	private Statement st=null;
	
	/**
	 * 读取数据库的配置文件
	 * 注册驱动
	 */
	static{
		//使用输入流读取配置文件
		InputStream in=
		  JdbcTemplate.class.getClassLoader().getResourceAsStream("param.properties");
		Properties pro=new Properties();
		//将注册驱动放在静态程序块中，因为它只需要被执行一次，并且不需要被修改
		try {
			pro.load(in);
			driver=pro.getProperty("mysql-driver");
			url=pro.getProperty("mysql-url");
			usename=pro.getProperty("mysql-usename");
			password=pro.getProperty("mysql-password");
			
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("注册驱动失败！！！");
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 建立与数据库的链接
	 */
	private void getConnection(){
		try {
			conn=DriverManager.getConnection(url,usename,password);
			//将自动提交改为手动提交
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("建立链接失败！！！");
		}
	}
	
	/**
	 * 建立预状态通道
	 * @param sql
	 */
	private void preparedStatement(String sql){
		getConnection();
		try {
			ps=conn.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("建立预状态通道失败！！！");
		}
	}
	
	/**
	 * 建立状态通道
	 */
	private void statement(){
		getConnection();
		try {
			st=conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("建立状态通道失败！！！");
		}
	}
	
	/**
	 * 基于状态通道的批处理
	 */
	public void executeBatch (List<String> sqls){
		statement();
		try{
			for (int i = 0; i < sqls.size(); i++) {
				st.addBatch(sqls.get(i));
			}
			st.executeBatch();
			myCommit();
		}catch(Exception e){
			e.printStackTrace();
			myRollBack();
		}
	}
	
	/**
	 * 在状态通道下操作数据的增删改
	 * @param sql
	 * @return
	 */
	public boolean updateDate(String sql){
		statement();
		boolean flag=false;
		try {
			int result=st.executeUpdate(sql);
			if(result>0){
				flag=true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 在状态通道下查询数据
	 * @param sql
	 * @return
	 */
	public ResultSet query(String sql){
		statement();
		try {
			rs=st.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	/**
	 * 对外界公开这个方法，使用预状态通道
	 * 要求传入sql语句，和一个存放数据库中值的数组
	 * 可以对数据进行增、删、改的操作
	 * @param  insert into、  delete、  update
	 * @return 返回布尔类型的值用于判断是否成功
	 */
	public boolean updateDate(String sql, String param[]) {
		preparedStatement(sql);
		boolean isok=false;
		try {
			bandle(param);
			int result=ps.executeUpdate();
			if(result>0){
				isok=true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("预状态通道下执行操作语句失败！！！");
		}
		return isok;
	}
	
	/**
	 * 绑定参数
	 * @param param
	 * @throws SQLException 
	 */
	private void bandle(String param[]) throws SQLException {
			if (param != null&&param.length>0) {
				for (int i = 0; i < param.length; i++) {
					ps.setString(i + 1, param[i]);
				}
			}
	}
	/**
	 * 预状态通道下执行查询语句
	 * @param sql
	 * @param param
	 * @return 将查询到的结果集返回，丢给调用的地方去操作
	 */
	public ResultSet query(String sql,String param[]){
		preparedStatement(sql);
		try {
			bandle(param);
			rs=ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("预状态通道下查询失败！！！");
		}
		return rs;
	}
	
	/**
	 * 手动提交数据
	 */
	public void myCommit(){
		try {
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 数据回滚
	 */
	public void myRollBack(){
		try {
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 对外界提供，关闭各种连接关系
	 */
	public void closeRes(){
		try {
			if(rs!=null)
				rs.close();
			if(ps!=null)
				ps.close();
			if(st!=null)
				st.close();
			if(conn!=null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
