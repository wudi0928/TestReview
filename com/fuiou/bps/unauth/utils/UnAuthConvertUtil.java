package com.fuiou.bps.unauth.utils;

import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.fuiou.bps.core.utils.StringUtils;
import com.fuiou.bps.unauth.constants.UnAuthConstants;

/**
 * 转换数据的工具类
 * @author Administrator
 *
 */
public class UnAuthConvertUtil {
	/**
	 * 将Obj中的数据转换成按照Key的ascii码排序后的key1=value1&key2=value2的形式 不包含签名域signature
	 * 
	 * @param data
	 *            待拼接的Bean对象
	 * @return 拼接好后的字符串
	 */
	public static <T> String convertObj2SortStr(T data) {
		TreeMap<String, String> tree = convertObj2TreeMap(data);
		return convertMap2Str(tree);
	}
	
	/**
	 * 将Obj中的数据转换成TreeMap
	 * 
	 * @param data
	 *            待转换的对象
	 * @return 转换后的TreeMap
	 */
	public static <T> TreeMap<String, String> convertObj2TreeMap(T data) {
		TreeMap<String, String> tree = new TreeMap<String, String>();
		Field[] fields = data.getClass().getDeclaredFields();
		Object valueObj = null;
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				valueObj = field.get(data);
				if (valueObj != null && StringUtils.isNotEmpty(valueObj.toString())) {
					tree.put(field.getName(), valueObj.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tree;
	}
	
	/**
	 * 将Obj中的数据转换成key1=value1&key2=value2的形式 
	 * 
	 * @param data
	 *            待拼接的Bean对象
	 * @return 拼接好后的字符串
	 */
	public static <T> String convertObj2Str(T data) {
		StringBuilder sb = new StringBuilder();
		Field[] fields = data.getClass().getDeclaredFields();
		Object valueObj = null;
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				valueObj = field.get(data);
				if (valueObj != null 
						&& StringUtils.isNotEmpty(valueObj.toString())) {
					sb.append(field.getName()).append(UnAuthConstants.EQUAL)
					.append(valueObj.toString())
					.append(UnAuthConstants.AMPERSAND);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(sb.length() > 0){
			return sb.substring(0, sb.length() - 1);
		}
		return null;
	}
	
	/**
	 * 将Obj中的数据转换成URL请求参数字符串
	 * 
	 * @param data
	 *            待拼接的Bean对象
	 * @return 拼接好后的字符串
	 */
	public static <T> String convertObj2ReqStr(T data, String encode) {
		StringBuilder sb = new StringBuilder();
		Field[] fields = data.getClass().getDeclaredFields();
		Object valueObj = null;
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				valueObj = field.get(data);
				if (valueObj != null){
					sb.append(field.getName()).append(UnAuthConstants.EQUAL)
					.append(URLEncoder.encode(valueObj.toString(), encode))
					.append(UnAuthConstants.AMPERSAND);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.substring(0, sb.length() - 1);
	}
	
	/**
	 * 除去signature参数外其他参数组成key=value字符串
	 * @param data
	 * @return
	 */
	public static String convertMap2Str(Map<String, String> data){
		StringBuilder sb = new StringBuilder();
		for(Entry<String,String> entry : data.entrySet()){
			if("signature".equals(entry.getKey()))
				continue;
			
			sb.append(entry.getKey()).append(UnAuthConstants.EQUAL)
			.append(entry.getValue()).append(UnAuthConstants.AMPERSAND);
		}
		return sb.substring(0, sb.length() - 1);
	}
	
	/**
	 * 将key=value字符串转为对象
	 * 
	 * @param data
	 *            待拼接的Bean对象
	 * @return 拼接好后的字符串
	 * @throws  
	 * @throws Exception 
	 * @throws  
	 * @throws Exception 
	 */
	public static <T> T convertMap2Obj(Map<String, String> resMap, T data) {
		Field[] fields = data.getClass().getDeclaredFields();
		if(resMap != null && resMap.size() > 0){
			//反射给对象属性赋值
			String value = null;
			for (Field field : fields) {
				field.setAccessible(true);
					value = resMap.get(field.getName());
					if (value != null){
						try {
							field.set(data, value);
						} catch (Exception e) {
							e.printStackTrace();
						} 
					}
			}
		}
		return data;
	}
	
	/**
	 * 将形如key=value&key=value的字符串转换为相应的Map对象
	 * 
	 * @param result
	 * @return
	 */
	public static Map<String, String> convertResultStringToMap(String result) {
		Map<String, String> map = null;
		if (StringUtils.isNotEmpty(result)) {
			if (result.startsWith("{") && result.endsWith("}")) {
				result = result.substring(1, result.length() - 1);
			}
			map = parseQString(result);
		}
		return map;
	}
	
	private static Map<String, String> parseQString(String str)
 {

		Map<String, String> map = new TreeMap<String, String>();
		int len = str.length();
		StringBuilder temp = new StringBuilder();
		char curChar;
		String key = null;
		boolean isKey = true;
		boolean isOpen = false;//值里有嵌套
		char openName = 0;
		if(len>0){
			for (int i = 0; i < len; i++) {// 遍历整个带解析的字符串
				curChar = str.charAt(i);// 取当前字符
				if (isKey) {// 如果当前生成的是key
					
					if (curChar == '=') {// 如果读取到=分隔符 
						key = temp.toString();
						temp.setLength(0);
						isKey = false;
					} else {
						temp.append(curChar);
					}
				} else  {// 如果当前生成的是value
					if(isOpen){
						if(curChar == openName){
							isOpen = false;
						}
						
					}else{//如果没开启嵌套
						if(curChar == '{'){//如果碰到，就开启嵌套
							isOpen = true;
							openName ='}';
						}
						if(curChar == '['){
							isOpen = true;
							openName =']';
						}
					}
					if (curChar == '&' && !isOpen) {// 如果读取到&分割符,同时这个分割符不是值域，这时将map里添加
						putKeyValueToMap(temp, isKey, key, map);
						temp.setLength(0);
						isKey = true;
					}else{
						temp.append(curChar);
					}
				}
				
			}
			putKeyValueToMap(temp, isKey, key, map);
		}
		return map;
	}

	private static void putKeyValueToMap(StringBuilder temp, boolean isKey,
			String key, Map<String, String> map) {
		if (isKey) {
			key = temp.toString();
			if (key.length() == 0) {
				throw new RuntimeException("QString format illegal");
			}
			map.put(key, "");
		} else {
			if (key.length() == 0) {
				throw new RuntimeException("QString format illegal");
			}
			map.put(key, temp.toString());
		}
	}
	
	
	
}
