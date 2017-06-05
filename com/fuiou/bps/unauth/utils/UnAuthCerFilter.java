package com.fuiou.bps.unauth.utils;

import java.io.File;
import java.io.FilenameFilter;

public class UnAuthCerFilter implements FilenameFilter {

	public boolean isCer(String name) {
		return (name.toLowerCase().endsWith(".cer"));
	}

	public boolean accept(File dir, String name) {
		return isCer(name);
	}

}
