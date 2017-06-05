package com.fuiou.bps.unauth.data;

import org.apache.commons.lang.StringUtils;

import com.fuiou.bps.unauth.utils.UnAuthConvertUtil;

public class CustomerInfo {

	private String certifTp; //证件类型
	
	private String certifId; //证件号码
	
	private String customerNm; //姓名
	
	private String phoneNo; //手机号
	
	private String cvn2; 
	
	private String expired;//有效期

	public CustomerInfo(String certifTp, String certifId, String customerNm,
			String phoneNo, String cvn2, String expired) {
		this.certifTp = certifTp;
		this.certifId = certifId;
		this.customerNm = customerNm;
		this.phoneNo = phoneNo;
		this.cvn2 = cvn2;
		this.expired = expired;
	}

	public String getCertifTp() {
		return certifTp;
	}

	public void setCertifTp(String certifTp) {
		this.certifTp = certifTp;
	}

	public String getCertifId() {
		return certifId;
	}

	public void setCertifId(String certifId) {
		this.certifId = certifId;
	}

	public String getCustomerNm() {
		return customerNm;
	}

	public void setCustomerNm(String customerNm) {
		this.customerNm = customerNm;
	}
	
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getCvn2() {
		return cvn2;
	}

	public void setCvn2(String cvn2) {
		this.cvn2 = cvn2;
	}

	public String getExpired() {
		return expired;
	}

	public void setExpired(String expired) {
		this.expired = expired;
	}

	/**
	 * 返回key=value格式字符串
	 * @return
	 */
	public String toKVString(){
		String res =  UnAuthConvertUtil.convertObj2Str(this);
		if(StringUtils.isNotEmpty(res)){
			return "{" + res + "}";
		}
		return res;
	}
}
