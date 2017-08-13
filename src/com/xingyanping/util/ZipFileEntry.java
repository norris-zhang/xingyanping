package com.xingyanping.util;

public class ZipFileEntry {
	public String path;
	public byte[] data;
	public ZipFileEntry(String path, byte[] data) {
		this.path = path;
		this.data = data;
	}
}
