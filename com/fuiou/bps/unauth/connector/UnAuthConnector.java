package com.fuiou.bps.unauth.connector;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.fuiou.bps.core.connector.Connector;
import com.fuiou.bps.core.constants.Constants;
import com.fuiou.bps.core.convertor.Convertor;
import com.fuiou.bps.core.data.BpsData;
import com.fuiou.bps.core.data.InterfaceData;
import com.fuiou.bps.core.exception.FUException;
import com.fuiou.bps.core.utils.ConfigReader;
import com.fuiou.bps.core.utils.LogWriter;
import com.fuiou.bps.unauth.constants.UnAuthConstants;
import com.fuiou.bps.unauth.data.UnAuthResData;
import com.fuiou.bps.unauth.tcp.IUnAuthSend;
import com.fuiou.bps.unauth.utils.UnAuthConvertUtil;
import com.fuiou.bps.unauth.utils.UnAuthMapper;
import com.fuiou.bps.unauth.utils.UnAuthSignUtil;
import com.fuiou.bps.unauth.utils.UnAuthVerifyUtil;



/**
 * 银联全渠道预授权接口
 * @author user
 *
 */
public class UnAuthConnector implements Connector {
	
	private static String STATE_SUCCESS = "00"; //成功
	private static String STATE_TIMEOUT = "03"; //交易通讯超时，请发起查询交易
	private static String STATE_UNCLEAR = "04"; //交易状态未明，请查询对账结果
	private static String STATE_PROCESSING = "05"; //交易已受理，请稍后查询交易结果
	
	private static String QUERY_SLEEP = "QuerySleepSeconds";
	private static int QUERY_TIMES = 6;
	
	private int sleepSeconds = Integer.parseInt(ConfigReader.getConfig(
			UnAuthConstants.PRO_PAHT, QUERY_SLEEP));
	
	private IUnAuthSend httpClient;
	
	private Convertor convertor;
	
	public void setHttpClient(IUnAuthSend httpClient) {
		this.httpClient = httpClient;
	}

	public void setConvertor(Convertor convertor) {
		this.convertor = convertor;
	}

	@Override
	public BpsData send(BpsData bpsData) throws FUException {
		// 校验数据
		UnAuthVerifyUtil.verifyBpsData(bpsData);
		// 发送报文到银行
		InterfaceData interfaceData = convertor.txn2Interface(bpsData);
		String req = interfaceData.toString();
		bpsData.setBpsToInsMsg(req);
		UnAuthResData data;
		data = sendUnAuth(req, bpsData,
				UnAuthMapper.getUnAuthReqUrl(bpsData.getTranCd()));
		if(needQuery(data.getRespCode(), bpsData.getTranCd())){
			data = reSendUnAuth(constructQT(bpsData));
		}
		return convertor.interface2Txn(data, bpsData);
	}
	
	/**
	 * 发送http请求并进行验签转换
	 * @param data
	 * @param bpsData
	 * @return
	 * @throws FUException
	 */
	private UnAuthResData sendUnAuth(String data, BpsData bpsData, String url) throws FUException{
		String result = httpClient.send(data, url);
		bpsData.setInsToBpsMsg(result);
		if(StringUtils.isEmpty(result)){
			throw new FUException(Constants.ERROR_CODE,"未获取到返回报文");
		}
		Map<String, String> resMap = UnAuthConvertUtil.convertResultStringToMap(result);
		if(!UnAuthSignUtil.checkSign(resMap, bpsData.getDestInsCd(), UnAuthConstants.ENCODING)){
			throw new FUException(Constants.ERROR_CODE,"验签失败");
		}
		return UnAuthResData.fromResMap(resMap);
	}
	
	/**
	 * 是否需要发起查询接口
	 * 
	 * @param resCode
	 *            银联返回的应答码
	 * @param transCd
	 *            交易的类型
	 * @return 预授权类交易需要用查询接口判断交易准确性
	 */
	private boolean needQuery(String resCode, String tranCd) {
		return UnAuthVerifyUtil.isYSQ(tranCd)
				&& (STATE_SUCCESS.equals(resCode) || isUnclearStatus(resCode));
	}
	  
	/**
	 * 
	 * @param resCode
	 *            银联的状态码
	 * @return 判断是不是需要查询的状态
	 */
	private boolean isUnclearStatus(String resCode) {
		return STATE_TIMEOUT.equals(resCode) || STATE_UNCLEAR.equals(resCode)
				|| STATE_PROCESSING.equals(resCode);
	}
	  
	private UnAuthResData reSendUnAuth(BpsData bpsData) throws FUException {
		String data = convertor.txn2Interface(bpsData).toString();
		String url = UnAuthMapper.getUnAuthReqUrl(bpsData.getTranCd());
		UnAuthResData res = null;
		for (int i = 1; i <= QUERY_TIMES; i++) {
			try {
				LogWriter.debug("第" + i + "次查询原交易状态");
				res = sendUnAuth(data, bpsData, url);
				//明确成功或者失败状态直接返回结果
				if (STATE_SUCCESS.equals(res.getOrigRespCode()) || !isUnclearStatus(res.getOrigRespCode())) {
					return res;
				}
				TimeUnit.MILLISECONDS.sleep(sleepSeconds);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (FUException e) {
				e.printStackTrace();
				if(i == QUERY_TIMES){
					throw e;
				}
			}
		}
		return res;
	}
		
	private BpsData constructQT(BpsData bpsData) {
		bpsData.setTranCd(Constants.BUSI_QRYTRAN);
		bpsData.setOldDestSsn(bpsData.getDestSsn());
		bpsData.setOldReqDate(bpsData.getReqDate());
		return bpsData;
	}
	  
	
}
