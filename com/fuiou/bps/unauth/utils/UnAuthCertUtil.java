package com.fuiou.bps.unauth.utils;

import java.util.HashMap;

import com.fuiou.bps.core.utils.ConfigReader;
import com.fuiou.bps.unauth.constants.UnAuthConstants;
import com.fuiou.bps.unauth.domain.UnAuthCert;

/**
 * 配置相关的工具类
 */
public class UnAuthCertUtil {

	private static HashMap<String, UnAuthCert> CERT_MAP = new HashMap<String, UnAuthCert>();

	private static String DESTINSCDS_KEY = "UnDestInsCds";
	
	/**
	 * 初始化根据配置加载证书相关信息
	 */
	static{
		initCertInfoByConf(ConfigReader.getConfig(UnAuthConstants.PRO_PAHT, DESTINSCDS_KEY));
	}
	
	/**
	 * 根据不同的机构号加载对应配置文件信息
	 * @param destInsCd
	 */
	private static void loadCertInfo(String destInsCd) {
		UnAuthCert unAuthCert = UnAuthCert.getInstance();
		unAuthCert.initConfig(destInsCd);
		if(unAuthCert.isAvailable()){
			CERT_MAP.put(destInsCd, unAuthCert);
		}
	}

	/**
	 * 根据机构号获取相关配置信息
	 * @param destInsCd
	 * @return
	 */
	public static UnAuthCert getUnAuthCert(String destInsCd){
		if(!CERT_MAP.containsKey(destInsCd)){
			loadCertInfo(destInsCd);
		}
		return CERT_MAP.get(destInsCd);
	}
	
	private static void initCertInfoByConf(String configCds){
		String[] insCdsArr = configCds.split(UnAuthConstants.COLON);
		for(String insCd : insCdsArr){
			loadCertInfo(insCd);
		}
	}
}
