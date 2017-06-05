package com.fuiou.bps.unkj.connector;

import com.fuiou.bps.core.connector.Connector;
import com.fuiou.bps.core.convertor.Convertor;
import com.fuiou.bps.core.data.BpsData;
import com.fuiou.bps.core.data.InterfaceData;
import com.fuiou.bps.core.exception.FUException;
import com.fuiou.bps.unkj.factory.UnkjFactory;
import com.fuiou.bps.unkj.tcp.IUnkjSend;
import com.fuiou.bps.unkj.utils.UnkjBusUtil;

public class UnkjConnector implements Connector {
	
	private IUnkjSend socketClient;
	public IUnkjSend getSocketClient() {
		return socketClient;
	}

	public void setSocketClient(IUnkjSend socketClient) {
		this.socketClient = socketClient;
	}

	@Override
	public BpsData send(BpsData bpsData) throws FUException {
		UnkjBusUtil.verifyData(bpsData);//验证请求是否合法
		Convertor convertor = UnkjFactory.createConvertor(bpsData.getTranCd());
		InterfaceData req = convertor.txn2Interface(bpsData);
		String reqStr = req.toString();
		bpsData.setBpsToInsMsg(reqStr);
		String resp = socketClient.send(reqStr);
		bpsData.setInsToBpsMsg(resp);
		//return convertor.interface2Txn(new CmbaRspData(resp.getBytes()), bpsData);
		return null;
	}
}
