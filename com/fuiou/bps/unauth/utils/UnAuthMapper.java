package com.fuiou.bps.unauth.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fuiou.bps.core.constants.Constants;
import com.fuiou.bps.core.utils.ConfigReader;
import com.fuiou.bps.unauth.constants.UnAuthConstants;

public class UnAuthMapper {

	private static Map<String, String> CERT_MAP = new HashMap<String, String>();//证件类型mao
	
	private static Map<String, String> REQURL_MAP = new HashMap<String, String>();//请求地址map
	
	private static Map<String, String> TXN_TYPE_MAP = new HashMap<String, String>();//交易类型map
	
	private static String BACK_TRANS_URL = ConfigReader.getConfig(UnAuthConstants.PRO_PAHT, "BackTransUrl");
	private static String SINGLE_QUERY_URL = ConfigReader.getConfig(UnAuthConstants.PRO_PAHT, "SingleQueryUrl");
	
	private static String CERT_OTHERS = "99"; //其他
	static
	{
		CERT_MAP.put("0", "01"); //身份证
		CERT_MAP.put("1", "03"); //护照
		CERT_MAP.put("2", "02"); //军官证
		CERT_MAP.put("3", "07"); //士兵证
		CERT_MAP.put("4", "04"); //回乡证
		
		REQURL_MAP.put(UnAuthConstants.TRANS_YSQ, BACK_TRANS_URL);
		REQURL_MAP.put(Constants.BUSI_QRYTRAN, SINGLE_QUERY_URL);
		
		TXN_TYPE_MAP.put(UnAuthConstants.BUSI_YSQ, "02"); //预授权
		TXN_TYPE_MAP.put(UnAuthConstants.BUSI_YCX, "32"); //预授权撤销
		TXN_TYPE_MAP.put(UnAuthConstants.BUSI_YWC, "03"); //预授权完成
		TXN_TYPE_MAP.put(UnAuthConstants.BUSI_YWCX, "33"); //预授权完成撤销
		TXN_TYPE_MAP.put(Constants.BUSI_QRYTRAN, "00"); //查询交易
		
	}
	
	public static String getUnAuthCertTp(String fuiouCertTp) {
		String val = CERT_MAP.get(fuiouCertTp);
		if (val != null){
			return val;
		}
			
		if(StringUtils.isNotEmpty(fuiouCertTp)){
			return CERT_OTHERS;
		}
		return null;
	}
	
	/**
	 * 获取不同交易类型的请求地址,默认返回后台请求
	 * @param transCd
	 * @return
	 */
	public static String getUnAuthReqUrl(String transCd){
		if(REQURL_MAP.containsKey(transCd)){
			return REQURL_MAP.get(transCd);
		}
		return BACK_TRANS_URL;
	}
	
	public static String getTxnType(String busiCd){
		return TXN_TYPE_MAP.get(busiCd);
	}
}
