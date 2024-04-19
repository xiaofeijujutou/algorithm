package com.xiaofei.algorithm.test;

import com.xiaofei.algorithm.test.BillKeyVo;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.concurrent.*;

/**
 //任务：
 //1、在main中实现定时任务调用toSend，每分钟发送一次msg_inner表中的数据
 //2、如果发送失败，幂次延迟发送，失败次数加1，下次继续重试；
 //3、每条数据都要有均等的发送机会
 //4、找到和排除程序中的所有bug
 //5、见代码中其他注释
 */
public class Test {
	//fixedThreadPool定长线程池,队列最大长度为int最大值,任务过多会压垮服务器
	//可以接收很多任务,但是如果任务一直发不出去,任务就会一直堆积;
	ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
	public void toSend(){
		Connection conn = null;
		//结果集
		ResultSet rs = null;
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		try {
			//SQL语句
			String sql="select a.syn_mid from msg_inner a where a.status = '1' and unix_timestamp(current_timestamp()) - unix_timestamp(a.deal_time) >= 5";
			conn = DBUtil.getDataSource().getConnection();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			pst1 = conn.prepareStatement("UPDATE MSG_INNER SET STATUS = '0' WHERE SYN_MID = ?");
			while (rs.next()) {
				String SYN_MID = rs.getString("SYN_MID");
				pst1.setString(1, SYN_MID);
				pst1.executeUpdate();
			}
			//关流操作要放在finally里面
			pst1.close();
			pst = null;
			rs = null;

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sql = "SELECT * FROM (SELECT A.SYN_MID,A.MSG_ID,A.CODE,A.SRC,A.DES,A.BILL_NO,A.XMLCNT,A.IN_TIME,B.IP,B.PORT,B.TOKEN FROM MSG_INNER A, MSG_ROUTER B "
					+ "WHERE A.DES = B.DES AND A.STATUS='0' AND B.IS_USED= 'Y' ORDER BY A.TO_SEND_TIME ASC) T LIMIT 0,10";
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				BillKeyVo vo = new BillKeyVo();
				vo.setSynMid(rs.getString("syn_mid"));
				vo.setTrxCode(rs.getString("code"));
				vo.setBillNo(rs.getString("bill_no"));
				vo.setSrc(rs.getString("src"));
				vo.setDes(rs.getString("des"));
				vo.setXml(rs.getString("xmlcnt"));
				vo.setIp(rs.getString("ip"));
				vo.setPort(rs.getInt("port"));
				vo.setToken(rs.getString("token"));
				vo.setInTime(sdf.format(rs.getTimestamp("in_time")));
				//调用send(),直接打包成线程池任务;
				send(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(pst1 != null){
				try {
					pst1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pst != null){
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void send(final BillKeyVo vo){
		fixedThreadPool.execute(new Runnable() {
			public void run() {
				Connection conn = null;
				PreparedStatement pst = null;
				try {
					//执行发送之前需要判断status是0还是1,最好是开启MySQL事务
					//5、发送前将数据状态置为1，表示正在有线程正在处理，发送成功就删除msg_inner表中的对应数据，
					// 发送失败就将状态置为0，表示线程处理结束，等待下一次定时任务发送
					conn = DBUtil.getDataSource().getConnection();
					//设置不自动提交
					conn.setAutoCommit(false);
					pst = conn.prepareStatement("UPDATE MSG_INNER SET STATUS = '1' ,FAIL_COUNT=FAIL_COUNT+1, DEAL_TIME = NOW(),TO_SEND_TIME=DATE_ADD(NOW(),INTERVAL power(2,FAIL_COUNT) SECOND) WHERE SYN_MID = ?");
					//TODO 判断status,如果为1就不发送;
					pst.setString(1, vo.getSynMid());
					pst.executeUpdate();
					pst.close();
					//提交事务
					conn.commit();
					String charset = "GBK";
					InetSocketAddress inetSocketAddress = new InetSocketAddress(vo.getIp(), vo.getPort());
					Socket socket = new Socket();
					socket.connect(inetSocketAddress, 5000);
					String content = vo.getXml();
					System.out.println(content);
					OutputStream output = socket.getOutputStream();
					byte[] contentBytes = content.getBytes();
					int contentLen = contentBytes.length;
					contentBytes = content.getBytes(charset);
					output.write(contentBytes);
					output.flush();
					InputStream input = socket.getInputStream();
					byte[] tmpdata = new byte[input.available()];
					int readflag = input.read(tmpdata);
					String result = new String(tmpdata,charset);
					System.out.println(result);

					pst = conn.prepareStatement("DELETE FROM MSG_INNER WHERE SYN_MID = ?");
					pst.setString(1, vo.getSynMid());
					pst.executeUpdate();
					pst.close();
				} catch (Exception e) {
					//出现异常了,设置为状态为0,表示发送失败
					e.printStackTrace();
					try {
						pst = conn.prepareStatement("UPDATE MSG_INNER SET STATUS = '0' WHERE SYN_MID = ?");
						pst.setString(1, vo.getSynMid());
						pst.executeUpdate();
						pst.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}finally{
					if(conn != null){
						try {
							conn.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}

	public static void main(String[] args) {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		Test t = new Test();
		Runnable toSend = () -> {
			t.toSend();
		};
		// 每分钟执行一次 sender
		executor.scheduleAtFixedRate(toSend, 0, 1, TimeUnit.MINUTES);

		try {
			Thread.sleep(10 * 60 * 1000); // 10 分钟
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		executor.shutdown();

	}
}
