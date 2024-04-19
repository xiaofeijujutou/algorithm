package com.xiaofei.algorithm.test;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

public class DBUtil {

	private static BasicDataSource dataSource = null;

	static{
		Properties p=new Properties();
		try {
			p.load(DBUtil.class.getClassLoader().getResourceAsStream("db.properties"));
			System.out.println(p);
			dataSource = (BasicDataSource) BasicDataSourceFactory
					.createDataSource(p);
		} catch (Exception e) {
			System.out.println("创建数据源失败,失败原因："+e);
		}
	}

	private static void init(){
		Properties p=new Properties();
		try {
			p.load(DBUtil.class.getClassLoader().getResourceAsStream("db.properties"));
//			p.load(new FileInputStream("db.properties"));
			dataSource = (BasicDataSource) BasicDataSourceFactory
					.createDataSource(p);
		} catch (Exception e) {
			System.out.println("创建数据源失败,失败原因："+e);
		}
	}

	/**
	 * 获取连接
	 * @return
	 */
	public static synchronized Connection getConnection(){
		Connection conn=null;
		try {
			conn=getDataSource().getConnection();
		} catch (SQLException e) {
			conn=null;
			System.out.println("获取连接失败，失败原因："+e);
		}
		return conn;
	}

	/**
	 * 获取数据源
	 * @return
	 */
	public static DataSource getDataSource(){
		if(dataSource==null)init();
		return dataSource;
	}

	/**
	 * 关闭连接
	 * @param conn
	 */
	public static void closeConnection(Connection conn){
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("数据库连接关闭失败，失败原因："+e);
			}
		}
	}

	/**
	 * 关闭数据源
	 * @param ds
	 */
	public static void shutDownDataSource(DataSource ds){
		BasicDataSource bds = (BasicDataSource) ds;
		if(bds!=null){
			try {
				bds.close();
			} catch (SQLException e) {
				System.out.println("关闭数据源失败，失败原因："+e);
			}
		}
	}

	public static void main(String[] args) {
		Connection conn=DBUtil.getConnection();
		System.out.println(conn);
	}
}
