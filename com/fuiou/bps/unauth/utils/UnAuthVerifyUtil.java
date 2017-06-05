package com.fuiou.bps.unauth.utils;

import org.apache.commons.lang3.StringUtils;

import com.fuiou.bps.core.constants.Constants;
import com.fuiou.bps.core.data.BpsData;
import com.fuiou.bps.core.exception.FUException;
import com.fuiou.bps.unauth.constants.UnAuthConstants;

public class UnAuthVerifyUtil {

	public static void verifyBpsData(BpsData data) throws FUException{
		if(!isSupported(data.getTranCd())){
			throw new FUException(Constants.ISS_FAIL_CODE, "暂不支持此接口");
		}
		
		//验证证书相关配置
		if(UnAuthCertUtil.getUnAuthCert(data.getDestInsCd()) == null){
			throw new FUException(Constants.ISS_FAIL_CODE, "不支持的机构号");
		}
		
		if(isYSQ(data.getTranCd())){
			verifyYSQSubInfo(data.getBusiCd());
		}
		
		if(isQT(data.getTranCd())){
			verifyOldBusiTp(data.getOldBusiCd());
		}
	}
	
	/**
	 * 查询时必须传原交易类型
	 * @param data
	 * @throws FUException 
	 */
	private static void verifyOldBusiTp(String oldBusiCd) throws FUException {
		
		if (StringUtils.isEmpty(oldBusiCd)) {
			throw new FUException(Constants.ERROR_CODE, "原交易类型不能为空");
		}

		if (!isYSQSub(oldBusiCd)) {
			throw new FUException(Constants.ERROR_CODE, "原交易类型错误");
		}
	}

	/**
	 * 验证具体的预授权交易信息
	 * @param data
	 * @throws FUException 
	 */
	private static void verifyYSQSubInfo(String busiCd) throws FUException {
		if(StringUtils.isEmpty(busiCd)){
			throw new FUException(Constants.ERROR_CODE, "预授权交易类型为空");
		}
		
		if(!isYSQSub(busiCd)){
			throw new FUException(Constants.ERROR_CODE, "预授权交易类型不合法");
		}
	}

	public static boolean isSupported(String tranCd){
		return isYSQ(tranCd) || isQT(tranCd);
	}
	
	/**
	 * 判断是不是预授权类交易
	 * @param tranCd
	 * @return
	 */
	public static boolean isYSQ(String tranCd){
		return UnAuthConstants.TRANS_YSQ.equals(tranCd);
	}
	
	/**
	 * 判断是不是具体的预授权交易类型
	 * @param busiCd
	 * @return
	 */
	public static boolean isYSQSub(String busiCd){
		return UnAuthConstants.BUSI_YSQ.equals(busiCd)
				|| UnAuthConstants.BUSI_YCX.equals(busiCd)
				|| UnAuthConstants.BUSI_YWC.equals(busiCd)
				|| UnAuthConstants.BUSI_YWCX.equals(busiCd);
	}
	
	/**
	 * 判断是不是查询交易
	 * @param tranCd
	 * @return
	 */
	public static boolean isQT(String tranCd){
		return Constants.BUSI_QRYTRAN.equals(tranCd);
	}
	
}
