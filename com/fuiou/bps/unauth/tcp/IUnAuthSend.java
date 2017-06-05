package com.fuiou.bps.unauth.tcp;

import com.fuiou.bps.core.exception.FUException;

public interface IUnAuthSend {
	public String send(String reqData, String url) throws FUException;
}
