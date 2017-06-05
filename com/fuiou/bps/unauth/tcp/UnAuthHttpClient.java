package com.fuiou.bps.unauth.tcp;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import com.fuiou.bps.core.exception.FUException;
import com.fuiou.bps.core.utils.LogWriter;

/**
 * Description: HTTP通信工具类
 */
public class UnAuthHttpClient implements IUnAuthSend {

	private String charset;

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	@Override
	public String send(String data, String url) throws FUException {
		try {
			LogWriter.debug(this, "发送UnAuth url=" + url + "\n,body===" + data);

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			StringEntity reqEntity = new StringEntity(data, this.charset);
			reqEntity.setContentType("application/x-www-form-urlencoded");
			httppost.setEntity(reqEntity);
			HttpResponse httpresponse = httpclient.execute(httppost);
			
			LogWriter.debug("发送UnAuth返回应答码:" + httpresponse.getStatusLine().getStatusCode());
			String result  = EntityUtils.toString(httpresponse.getEntity()); 

			LogWriter.debug("发送UnAuth返回信息:" + result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new FUException("200001","目标方连接超时");
		}
	}
}
