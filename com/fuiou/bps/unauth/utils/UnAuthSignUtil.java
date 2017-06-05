package com.fuiou.bps.unauth.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import com.fuiou.bps.core.utils.LogWriter;


/**
 * 签名工具类
 * @author Administrator
 *
 */
public class UnAuthSignUtil {
	private static String SHA1_RSA = "SHA1withRSA";
	/**
	 * 签名
	 * 
	 * @param data
	 * @param keyStorePath
	 * @param keyStorePassword
	 * @return
	 * @throws Exception
	 */
	public static <T> String sign(T data, PrivateKey privateKey,String encode)  {
		String signStr = UnAuthConvertUtil.convertObj2SortStr(data);
		LogWriter.debug("待签名请求报文串:[" + signStr + "]");
		try {
			//Security.addProvider(bouncyCastleProvider);
			Signature signature = Signature.getInstance(SHA1_RSA);
			signature.initSign(privateKey);
			// 通过SHA1进行摘要并转16进制
			signature.update(sha1X16(signStr, encode));
			return Base64.encodeBase64String(signature.sign());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean checkSign(Map<String, String> data, String destInsCd, String encoding)  {
		PublicKey pubKey = UnAuthCertUtil.getUnAuthCert(destInsCd).getValidatePublicKey(data.get("certId"));
		try {
			Signature st = Signature.getInstance(SHA1_RSA);
			st.initVerify(pubKey);
			st.update(sha1X16(UnAuthConvertUtil.convertMap2Str(data), encoding));
			return st.verify(Base64.decodeBase64(data.get("signature").getBytes(encoding)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	private static String bytes2HexStr(byte[] bytes) {
		StringBuilder sha1Str = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
				sha1Str.append("0").append(
						Integer.toHexString(0xFF & bytes[i]));
			} else {
				sha1Str.append(Integer.toHexString(0xFF & bytes[i]));
			}
		}
		return sha1Str.toString();
	}
	
	private static byte[] sha1(byte[] data) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			md.reset();
			md.update(data);
			return md.digest();
		} catch (Exception e) {
			LogWriter.debug("SHA1计算失败");
			e.printStackTrace();
			return null;
		}
	}
	
	private static byte[] sha1X16(String data, String encoding) throws UnsupportedEncodingException {
		byte[] bytes = sha1(data.getBytes(encoding));
		return bytes2HexStr(bytes).getBytes(encoding);
	}
	
	
}
