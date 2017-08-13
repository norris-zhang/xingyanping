package com.xingyanping.util;

import java.io.File;

public class PathUtil {
	public static String uploadFilePath(Long upfiId, String originalName) {
		String path = Config.get("upload.file.base") + File.separator + (upfiId / 1000);
		File pathFile = new File(path);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		return path + File.separator + upfiId + extractExt(originalName);
	}

	private static String extractExt(String originalName) {
		return originalName.substring(originalName.lastIndexOf("."));
	}
	public static void main(String[] args) {
		System.out.println(extractExt("abc.xls"));
	}
}
