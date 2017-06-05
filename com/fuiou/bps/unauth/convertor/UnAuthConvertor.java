package com.fuiou.bps.unauth.convertor;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

import com.fuiou.bps.core.constants.Constants;
import com.fuiou.bps.core.convertor.Convertor;
import com.fuiou.bps.core.data.BpsData;
import com.fuiou.bps.core.data.InterfaceData;
import com.fuiou.bps.core.utils.DateUtils;
import com.fuiou.bps.core.utils.LogWriter;
import com.fuiou.bps.core.utils.StringUtils;
import com.fuiou.bps.unauth.constants.UnAuthConstants;
import com.fuiou.bps.unauth.data.CustomerInfo;
import com.fuiou.bps.unauth.data.UnAuthReqData;
import com.fuiou.bps.unauth.data.UnAuthResData;
import com.fuiou.bps.unauth.domain.UnAuthCert;
import com.fuiou.bps.unauth.domain.UnAuthConfig;
import com.fuiou.bps.unauth.utils.UnAuthCertUtil;
import com.fuiou.bps.unauth.utils.UnAuthMapper;
import com.fuiou.bps.unauth.utils.UnAuthSignUtil;
import com.fuiou.bps.unauth.utils.UnAuthVerifyUtil;

/**
 * 银联预授权交易转换类
 * @author user
 *
 */
public class UnAuthConvertor implements Convertor {
	
	@Override
	public InterfaceData txn2Interface(BpsData txnData) {
		if(StringUtils.isEmpty(txnData.getReqDate())){
			txnData.setReqDate(DateUtils.getCurrentDate());
		}
		
		UnAuthCert cert = UnAuthCertUtil.getUnAuthCert(txnData.getDestInsCd());
		UnAuthReqData req = new UnAuthReqData(cert.getSignCertId());
		initMerInfo(req, cert.getUnConfig());
		
		if(UnAuthVerifyUtil.isQT(txnData.getTranCd())){
			txn2QT(txnData, req);
		}
		else if(UnAuthVerifyUtil.isYSQ(txnData.getTranCd())){
			txn2YSQ(txnData, req);
		}
		//签名
		req.setSignature(UnAuthSignUtil.sign(req, cert.getSignPrivateKey(), UnAuthConstants.ENCODING));
		return req;
	}
	
	@Override
	public BpsData interface2Txn(InterfaceData interfData, BpsData txnData) {
		UnAuthResData data = (UnAuthResData) interfData;
		//查询的返回origRespCode
		if(UnAuthVerifyUtil.isQT(txnData.getTranCd())){
			txnData.setRespCode(StringUtils.isNotEmpty(data.getOrigRespCode()) ? data.getOrigRespCode() : Constants.ERROR_CODE);
			txnData.setRespInfo(StringUtils.isNotEmpty(data.getOrigRespMsg()) ? data.getOrigRespMsg() : data.getRespMsg());
		}
		//预授权的返回respCode
		else if (UnAuthVerifyUtil.isYSQ(txnData.getTranCd())){
			txnData.setRespCode(data.getRespCode());
			txnData.setRespInfo(data.getRespMsg());
		}
		txnData.setRespSeqNo(StringUtils.nvl(data.getQueryId()));
		return txnData;
	}
	

	/**
	 * 设置商户相关信息
	 * @param req
	 * @param config
	 */
	private void initMerInfo(UnAuthReqData req, UnAuthConfig config) {
		req.setMerId(config.getMerId()); 
		req.setAcqInsCode(config.getAcqInsCode()); //机构号
		req.setMerName(config.getMerName());
		req.setMerCatCode(config.getMerCatCode());
		req.setMerAbbr(config.getMerAbbr());
	}

	/**
	 * 设置查询报文相关要素
	 * @param txnData
	 * @param req
	 */
	private void txn2QT(BpsData txnData, UnAuthReqData req) {
		req.setOrderId(txnData.getOldReqDate() + txnData.getOldDestSsn()); //查询订单号
		req.setTxnType(UnAuthMapper.getTxnType(txnData.getTranCd())); //交易类型
		req.setTxnTime(txnData.getOldReqDate() + DateUtils.getCurrentTime());//订单发送时间
	}

	/**
	 * 设置预授权类报文相关要素
	 * @param txnData
	 * @param req
	 */
	private void txn2YSQ(BpsData txnData, UnAuthReqData req) {
		if(UnAuthConstants.BUSI_YSQ.equals(txnData.getBusiCd())){
			req.setCurrencyCode(UnAuthConstants.CURRENCY_RMB);
			req.setAccType(UnAuthConstants.ACC_BANK); //预授权时需送银行卡类型
			req.setTxnAmt(txnData.getTranAmt());//交易金额
			req.setTxnSubType(UnAuthConstants.SUBTYPE_YSQ);//预授权交易SubType用02
			txn2EncryInfo(txnData, req);
		}
		//预授权的其他交易需要原交易流水号
		else {
			req.setOrigQryId(txnData.getOldRespSeqNo());
			req.setTxnAmt(txnData.getOldTranAmt());//交易金额需要与原始交易金额一致
		}
		req.setChannelType(UnAuthConstants.CHAL_PC); //渠道类型 PC
		req.setOrderId(txnData.getReqDate() + txnData.getDestSsn()); //订单号
		req.setTxnType(UnAuthMapper.getTxnType(txnData.getBusiCd())); //交易类型
		req.setTxnTime(txnData.getReqDate() + DateUtils.getCurrentTime());//订单发送时间
		req.setBackUrl(UnAuthConstants.BACKURL);
	}
	
	/**
	 * 设置敏感信息(机构接入不需要加密)
	 * @param txnData
	 * @param req
	 */
	private void txn2EncryInfo(BpsData txnData, UnAuthReqData req) {
		req.setCustomerInfo(getCustomerInfo(txnData));
		if(StringUtils.isNotEmpty(txnData.getDebitAccNo())){
			req.setAccNo(txnData.getDebitAccNo().substring(2));
		}
	}
	
	/**
	 * 机构接入不需要加密敏感信息
	 * @param txnData
	 * @param cert
	 * @return
	 */
	private String getCustomerInfo(BpsData txnData){
		CustomerInfo cInfo = new CustomerInfo(
				UnAuthMapper.getUnAuthCertTp(txnData.getCertType()),
				txnData.getCertNo(), txnData.getDebitAccName(),
				txnData.getMobileNo(), txnData.getCVN2(),
				convertExpired(txnData.getVaildDate()));
		String custInfo = cInfo.toKVString();
		
		if (StringUtils.isNotEmpty(custInfo)) {
			LogWriter.debug("用户信息明文:" + custInfo);
			try {
				return Base64.encodeBase64String(custInfo.getBytes(UnAuthConstants.ENCODING));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return custInfo;
	}
	
	/**
	 * 有效期转换成年月形式
	 * @param cvn2
	 * @return
	 */
	private String convertExpired(String expDate){
		if(StringUtils.isEmpty(expDate)){
			return null;
		}
		int year = Integer.parseInt(DateUtils.getCurrentDate().substring(2, 4));
		//渠道传过来的cvn2前两位小于当前年份，则视为月份
		if (Integer.parseInt(expDate.substring(0, 2)) < year) {
			return expDate.substring(2, 4) + expDate.substring(0, 2);
		}
		return expDate;
		
	}
}

