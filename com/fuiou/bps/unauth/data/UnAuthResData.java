package com.fuiou.bps.unauth.data;

import java.util.Map;
import com.fuiou.bps.core.data.InterfaceData;
import com.fuiou.bps.core.exception.FUException;
import com.fuiou.bps.unauth.utils.UnAuthConvertUtil;

public class UnAuthResData implements InterfaceData {
	
	private UnAuthResData(){}
	
	//private String orderId; // 订单号
	
	//private String origQryId; // 原始交易流水号
	
	private String queryId;//银行返回流水号
	
	private String respCode;//应答代码
	
	private String respMsg;//应答信息
	
	/**
	 * 查询响应
	 */
	//private String traceNo; //系统跟踪号
	
	private String origRespCode; //原交易应答码
	
	private String origRespMsg; //原交易应答信息
	
	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespMsg() {
		return respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

	public String getOrigRespCode() {
		return origRespCode;
	}

	public void setOrigRespCode(String origRespCode) {
		this.origRespCode = origRespCode;
	}

	public String getOrigRespMsg() {
		return origRespMsg;
	}

	public void setOrigRespMsg(String origRespMsg) {
		this.origRespMsg = origRespMsg;
	}
	
	public static UnAuthResData fromResMap(Map<String,String> result){
		return UnAuthConvertUtil.convertMap2Obj(result, new UnAuthResData());
	}
	
	//不用的应答字段不转为对象属性,需要用的时候加入即可
/*	private String version;
	
	private String encoding;
	
	private String certId; //证书ID
	
	private String signature; //签名
	
	private String acqInsCode; //上送机构号
	
	private String signMethod; //签名方法 01(表示采用RSA ）
	
	private String txnType;//交易类型
	
	private String txnSubType;//交易子类型
	
	private String bizType; //产品类型
	
	private String accessType; //接入类型 商户接入固定填0，不需修改
	
	private String merId; //商户代码

	private String txnTime; // 订单发送时间
	
	private String accNo; //帐号
	
	private String txnAmt; //交易金额 分
	
	private String currencyCode; // 交易币种

	private String reqReserved;//请求方保留域
	
	private String reserved; //保留域
	private String payCardType;//支付卡类型
	
	private String payType; //支付方式
	
	private String tn; //银联订单号
	
	private String traceTime; //交易传输时间
	
	private String settleDate; //清算日期
	
	private String settleCurrencyCode; //清算币种
	
	private String settleAmt; //清算金额
	
	private String exchangeRate; //清算汇率
	
	private String exchangeDate; //兑换日期
	
	private String payCardNo; //支付卡标识
	
	private String payCardIssueName; //支付卡名称
	
	private String cardTransData; //有卡交易信息域
	
	private String issuerIdentifyMode; //发卡机构识别模式
	
	private String bindId; //委托关系标识号
*/

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
	
	
}
