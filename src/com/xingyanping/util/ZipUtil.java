package com.xingyanping.util;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
	public static byte[] zip(ZipFileContent zipFileContent) throws IOException {
		ByteArrayOutputStream bsout = new ByteArrayOutputStream();
		
		ZipOutputStream out = new ZipOutputStream(bsout);
		
		for (ZipFileEntry entry : zipFileContent.getEntryList()) {
			ZipEntry e = new ZipEntry(entry.path);
			out.putNextEntry(e);
			out.write(entry.data);
			out.closeEntry();
		}
		out.close();
		
		return bsout.toByteArray();
	}
	public static void main(String[] args) throws IOException {
//		ByteArrayOutputStream bsout = new ByteArrayOutputStream();
		FileOutputStream bsout = new FileOutputStream("/Users/norris/Documents/temp/a.zip");
		ZipOutputStream out = new ZipOutputStream(bsout);
		ZipEntry e = new ZipEntry("folder/abc.txt");
		out.putNextEntry(e);
		
		byte[] data = "abc".getBytes();
		out.write(data, 0, data.length);
		out.closeEntry();
		
		out.close();
		bsout.close();
	}
}
