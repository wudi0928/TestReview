package com.fuiou.bps.unauth.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import com.fuiou.bps.core.utils.LogWriter;
import com.fuiou.bps.unauth.utils.UnAuthCerFilter;

public class UnAuthCert {
	private static String SIGN_TYPE = "PKCS12";
	private KeyStore signKeyStore = null;
	private PrivateKey signPrivateKey = null;
	private  X509Certificate validateCert = null;
	private  Map<String, X509Certificate> certMap = new HashMap<String, X509Certificate>();
	private  UnAuthConfig unConfig =null;
	private String signCertId = null;
	
	private UnAuthCert(){
	}
	
	public static UnAuthCert getInstance(){
		return new UnAuthCert();
	}
	
	public void initConfig(String destInsCd) {
		unConfig = new UnAuthConfig(destInsCd);
		unConfig.loadProperties();
		//机构号对应文件配置类
		LogWriter.debug("加载机构配置：destInsCd="+destInsCd);

		//如果有相关的配置再进行初始化操作,否则初始化没有意义
		if(unConfig.isNotNull()){
			initSignCert();
			initValidateCertFromDir();
		}
	}

	public  void initSignCert() {
		LogWriter.debug("加载签名证书开始");
		this.signKeyStore = getKeyInfo(unConfig.getSignCertPath(), unConfig.getSignCertPwd());
		this.signCertId = initSignCertId();
		this.signPrivateKey = initSignPrivateKey();
		LogWriter.debug("[" + unConfig.getSignCertPath() + "][serialNumber=" + signCertId + "]");
		LogWriter.debug("加载签名证书结束");
	}
	
	private String initSignCertId() {
		try {
			@SuppressWarnings("rawtypes")
			Enumeration aliasenum = signKeyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements())
				keyAlias = (String) aliasenum.nextElement();

			X509Certificate cert = (X509Certificate) signKeyStore
					.getCertificate(keyAlias);
			return cert.getSerialNumber().toString();
		} catch (Exception e) {
			LogWriter.debug("获取签名证书的序列号失败" + e.getMessage());
		}
		return "";
	}
	
	private PrivateKey initSignPrivateKey(){
		try {
			Enumeration<String> aliasenum = signKeyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			PrivateKey privateKey = (PrivateKey) signKeyStore.getKey(keyAlias,
					this.unConfig.getSignCertPwd().toCharArray());
			return privateKey;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void initValidateCertFromDir() {
		File[] files;
		int i;
		LogWriter.debug("从目录中加载验证签名证书开始.");
		certMap.clear();
		String filePath = unConfig.getValidateCertDir();
		if ((null == filePath) || ("".equals(filePath))) {
			LogWriter.debug("验证签名证书路径配置为空.");
			return;
		}

		CertificateFactory cf = null;
		FileInputStream in = null;
		try {
			cf = CertificateFactory.getInstance("X.509");
			File fileDir = new File(filePath);
			files = fileDir.listFiles(new UnAuthCerFilter());
			for (i = 0; i < files.length; ++i) {
				File file = files[i];
				in = new FileInputStream(file.getAbsolutePath());
				validateCert = (X509Certificate) cf.generateCertificate(in);
				certMap.put(validateCert.getSerialNumber().toString(),
						validateCert);
				LogWriter.debug("[" + file.getAbsolutePath()
						+ "][serialNumber="
						+ validateCert.getSerialNumber().toString() + "]");
			}
		} catch (CertificateException e) {
			LogWriter.debug("验证签名证书加载失败" + e.getMessage());
		} catch (FileNotFoundException e) {
			LogWriter.debug("验证签名证书加载失败,证书文件不存在" + e.getMessage());
		} finally {
			if (null != in)
				try {
					in.close();
				} catch (IOException e) {
					LogWriter.debug(e.toString());
				}
		}
		LogWriter.debug("从目录中加载验证签名证书结束.");
	}
	
	/**
	 * 通过certId获取验签证书Map中对应证书PublicKey
	 * 
	 * @param certId 证书物理序号
	 * @return 通过证书编号获取到的公钥
	 */
	public PublicKey getValidatePublicKey(String certId) {
		X509Certificate cf = null;
		if (certMap.containsKey(certId)) {
			// 存在certId对应的证书对象
			cf = certMap.get(certId);
			return cf.getPublicKey();
		} 
		return null;
	}
		
	public PrivateKey getSignPrivateKey(){
		return this.signPrivateKey;
	}
	
	public String getSignCertId(){
		return signCertId;
	}
	
	public UnAuthConfig getUnConfig(){
		return this.unConfig;
	}
	
	public boolean isAvailable(){
		return this.unConfig != null && this.unConfig.isNotNull();
	}

	private KeyStore getKeyInfo(String pfxkeyfile, String keypwd) {
		return getKeyInfo(pfxkeyfile, keypwd, SIGN_TYPE);
	}
	
	private KeyStore getKeyInfo(String pfxkeyfile, String keypwd, String type) {
		try {
			//Security.addProvider(bouncyCastleProvider);
			KeyStore ks = KeyStore.getInstance(type);
			//KeyStore ks = KeyStore.getInstance(type);
			FileInputStream fis = new FileInputStream(pfxkeyfile);
			char[] nPassword = null;
			nPassword = ((null == keypwd) || ("".equals(keypwd.trim()))) ? null
					: keypwd.toCharArray();
			ks.load(fis, nPassword);
			fis.close();
			return ks;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
