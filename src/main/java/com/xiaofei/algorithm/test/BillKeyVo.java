package com.xiaofei.algorithm.test;

import java.io.Serializable;

public class BillKeyVo implements Serializable {
	private static final long serialVersionUID = 1705952838514049009L;

	private String synMid = null;

	private String trxCode = null;

	private String billNo = null;

	private String src = null;

	private String des = null;

	private String xml = null;

	private String ip = null;
	private int port;
	private String token = null;

	private String inTime = null;

	private String type = null;

	private int failCount = 0;

	private int status = 0;
	private String des_name;

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj == null) {
			result = false;
		} else if (this == obj) {
			result = true;
		} else if ((obj instanceof BillKeyVo)) {
			BillKeyVo target = (BillKeyVo) obj;
			String syn = target.getSynMid();
			if ((syn != null) && (syn.equals(this.synMid))) {
				result = true;
			}
		}

		return result;
	}

	public int hashCode() {
		return this.synMid.hashCode();
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("synMid=" + this.synMid + ",");
		buffer.append("trxCode=" + this.trxCode + ",");
		buffer.append("billNo=" + this.billNo + ",");
		buffer.append("src=" + this.src + ",");
		buffer.append("des=" + this.des + ",");
		buffer.append("ip=" + this.ip + ",");
		buffer.append("port=" + this.port + ",");

		return buffer.toString().substring(0, buffer.toString().length());
	}

	public String getDes_name() {
		return this.des_name;
	}

	public void setDes_name(String des_name) {
		this.des_name = des_name;
	}

	public int getFailCount() {
		return this.failCount;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSynMid() {
		return this.synMid;
	}

	public void setSynMid(String synMid) {
		this.synMid = synMid;
	}

	public String getTrxCode() {
		return this.trxCode;
	}

	public void setTrxCode(String trxCode) {
		this.trxCode = trxCode;
	}

	public String getBillNo() {
		return this.billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getSrc() {
		return this.src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getDes() {
		return this.des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getXml() {
		return this.xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getInTime() {
		return this.inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
}