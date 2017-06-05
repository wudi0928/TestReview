package com.fuiou.bps.unauth.data;

import com.fuiou.bps.core.data.InterfaceData;
import com.fuiou.bps.core.exception.FUException;
import com.fuiou.bps.unauth.constants.UnAuthConstants;
import com.fuiou.bps.unauth.utils.UnAuthConvertUtil;

/**
 * 银联数据转化类
 */
public class UnAuthReqData implements InterfaceData {	
	
	
	public UnAuthReqData(){}
	
	public UnAuthReqData(String certId) {
		this.certId = certId;
	}

	//----------请求--------------
	private String version = UnAuthConstants.VERSION;
	
	private String encoding = UnAuthConstants.ENCODING;
	
	private String certId; //证书ID
	
	private String signMethod = UnAuthConstants.SIGNMETHOD; //签名方法 01(表示采用RSA ）
	
	private String txnType;//交易类型
	
	private String txnSubType = UnAuthConstants.SUBTYPE_DEFAULT;//交易子类型
	
	private String bizType = UnAuthConstants.BIZTYPE_GATE; //产品类型
	
	private String channelType; //渠道类型 07-PC，08-手机
	
	private String backUrl; //后台通知地址 无需通知上送http://
	
	private String accessType = UnAuthConstants.MER_ORG; //接入类型 1-机构接入
	
	private String merId; //商户代码
	
	private String acqInsCode; //机构接入送机构号
	
	private String merCatCode; //机构接入送商户对应的 Mcc码
	
	private String merName; //商户名称
	
	private String merAbbr; //商户简称
	
	//二级商户信息 商户类型为平台类商户接入时必须上送
/*	private String subMerId; //二级商户代码
	private String subMerName; // 二级商户名称 
	private String subMerAbbr; // 二级商户简称
*/	
	private String orderId; // 订单号
	
	private String origQryId; // 原始交易流水号 填入原消费交易的queryId
	
	private String txnTime; // 订单发送时间
	
	private String accType; //帐号类型 01-银行卡 02-存折
	
	private String accNo; //帐号
	
	private String txnAmt; //交易金额 分
	
	private String currencyCode; // 交易币种

	private String customerInfo; //银行卡验证信息及身份信息;
	
	private String signature; //签名
	/*
	private String termId ; //终端号
	private String reqReserved;//请求方保留域
	private String reserved; //保留域
	private String riskRateInfo; //风险信息域
	private String instalTransInfo; //分期付款信息域
	private String issInsCode; //发卡机构代码
	private String userMac; //终端信息域
	private String customerIp; //持卡人IP
	private String bindId; //绑定标识号
	private String payCardType; //支付卡类型
	private String encryptCertId; //加密证书ID
	private String cardTransData;//有卡交易信息域
	private String orderDesc; //订单描述  移动支付上送
*/	
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getCertId() {
		return certId;
	}

	public void setCertId(String certId) {
		this.certId = certId;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignMethod() {
		return signMethod;
	}

	public void setSignMethod(String signMethod) {
		this.signMethod = signMethod;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getTxnSubType() {
		return txnSubType;
	}

	public void setTxnSubType(String txnSubType) {
		this.txnSubType = txnSubType;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrigQryId() {
		return origQryId;
	}

	public void setOrigQryId(String origQryId) {
		this.origQryId = origQryId;
	}

	public String getTxnTime() {
		return txnTime;
	}

	public void setTxnTime(String txnTime) {
		this.txnTime = txnTime;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getTxnAmt() {
		return txnAmt;
	}

	public void setTxnAmt(String txnAmt) {
		this.txnAmt = txnAmt;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCustomerInfo() {
		return customerInfo;
	}

	public void setCustomerInfo(String customerInfo) {
		this.customerInfo = customerInfo;
	}
	
	public String getAcqInsCode() {
		return acqInsCode;
	}

	public void setAcqInsCode(String acqInsCode) {
		this.acqInsCode = acqInsCode;
	}

	public String getMerCatCode() {
		return merCatCode;
	}

	public void setMerCatCode(String merCatCode) {
		this.merCatCode = merCatCode;
	}

	public String getMerName() {
		return merName;
	}

	public void setMerName(String merName) {
		this.merName = merName;
	}

	public String getMerAbbr() {
		return merAbbr;
	}

	public void setMerAbbr(String merAbbr) {
		this.merAbbr = merAbbr;
	}

	@Override
	public byte[] toBytes() throws FUException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] toBytes(String desKey) throws FUException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
			return UnAuthConvertUtil.convertObj2ReqStr(this, UnAuthConstants.ENCODING);
	}
}
