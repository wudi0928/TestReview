package com.fuiou.bps.unauth.constants;

public class UnAuthConstants {
	
	public static final String PRO_PAHT = "unauth/unauth";
	public static final String PRO_PREFIX = "unauth/";
	
	public static final String TRANS_YSQ = "YSQ";
	public static final String BUSI_YSQ = "YSQ"; //预授权
	public static final String BUSI_YCX = "YCX"; //预授权撤销
	public static final String BUSI_YWC = "YWC"; //预授权完成
	public static final String BUSI_YWCX = "YWCX"; //预授权完成撤销

	public static final String SIGNMETHOD = "01"; //01：RSA
	
	public static final String ENCODING = "UTF-8"; //编码方式 UTF-8
	
	public static final String VERSION = "5.0.0";
	
	public static final String CHAL_PC = "07";//PC
	
	public static final String SUBTYPE_DEFAULT = "00";//默认交易子类型
	public static final String SUBTYPE_YSQ = "02";//预授权交易子类型
	
	public static final String BIZTYPE_GATE = "001001";//订购
	
	public static final String MER_ORG = "1";//机构接入
	
	public static final String BACKURL = "http://www.specialUrl.com";//不需要通知的默认地址
	
	public static final String CURRENCY_RMB = "156";//人民币
	
	public static final String ACC_BANK = "01";//银行卡类型 01
	
	/** memeber variable: colon. */
	public final static String COLON = ",";

	/** memeber variable: AMPERSAND. */
	public static final String AMPERSAND = "&";
	
	public static final String EQUAL = "=";
}
