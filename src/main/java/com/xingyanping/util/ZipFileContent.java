package com.xingyanping.util;

import java.util.ArrayList;
import java.util.List;

public class ZipFileContent {
	private List<ZipFileEntry> entryList = new ArrayList<>();

	public List<ZipFileEntry> getEntryList() {
		return entryList;
	}

	public void setEntryList(List<ZipFileEntry> entryList) {
		this.entryList = entryList;
	}
	
}
