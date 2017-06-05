package com.fuiou.bps.unauth.domain;

import com.fuiou.bps.core.utils.ConfigReader;
import com.fuiou.bps.unauth.constants.UnAuthConstants;

public class UnAuthConfig {
	private static String SIGN_CERT_PATH = "SignCertPath";
	private static String SIGN_CERT_PWD = "SignCertPwd";
	private static String VALIDATE_CERT_DIR = "ValidateCertDir";
	private static String MERCHANT_ID = "MerId";
	private static String MERCHANT_CAT_CODE = "MerCatCode";
	private static String MERCHANT_Name = "MerName";
	private static String MERCHANT_ABBR = "MerAbbr";
	private static String ACQ_INSCODE = "AcqInsCode";
	
	
	private String fileName;
	
	private String signCertPath;
	
	private String signCertPwd;
	
	private String validateCertPath;
	
	private String validateCertDir;
	
	private String merId;
	
	private String acqInsCode;
	
	private String merCatCode; 
	
	private String merName; 
	
	private String merAbbr; 
	
	public UnAuthConfig(String destInsCd){
		this.fileName = UnAuthConstants.PRO_PREFIX + destInsCd;
	}
	
	public String getSignCertPath() {
		return signCertPath;
	}

	public String getSignCertPwd() {
		return signCertPwd;
	}

	public String getValidateCertPath() {
		return validateCertPath;
	}

	public String getValidateCertDir() {
		return validateCertDir;
	}
	
	public String getMerId() {
		return merId;
	}
	
	public String getAcqInsCode() {
		return acqInsCode;
	}

	public String getMerCatCode() {
		return merCatCode;
	}

	public String getMerName() {
		return merName;
	}

	public String getMerAbbr() {
		return merAbbr;
	}

	public void loadProperties() {
		this.signCertPath = getProperty(SIGN_CERT_PATH);
		this.signCertPwd = getProperty(SIGN_CERT_PWD);
		this.validateCertDir = getProperty(VALIDATE_CERT_DIR);
		this.merId = getProperty(MERCHANT_ID);
		this.acqInsCode = getProperty(ACQ_INSCODE);
		this.merName = getProperty(MERCHANT_Name);
		this.merCatCode = getProperty(MERCHANT_CAT_CODE);
		this.merAbbr = getProperty(MERCHANT_ABBR);
	}
	
	/**
	 * 必须配置的都配置了才认为是符合规则的配置文件
	 * @return
	 */
	public boolean isNotNull(){
		return this.signCertPath != null 
				&& this.signCertPwd != null 
				&& this.validateCertDir != null 
				&& this.merId != null;
	}
	
	private String getProperty(String key){
		try {
			return ConfigReader.getConfig(this.fileName, key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
