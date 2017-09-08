package com.xingyanping.util;

import java.io.IOException;
import java.io.OutputStream;
import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.xingyanping.datamodel.OriginalReport;

public class ExportMonthDataExcelWriter {
	private List<OriginalReport> orreList;
	public ExportMonthDataExcelWriter(List<OriginalReport> orreList) {
		this.orreList = orreList;
	}
	public void write(OutputStream out) throws IOException {
		LinkedHashMap<String, List<OriginalReport>> map = typeMap();
		
		XSSFWorkbook wb = new XSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		
		for (String key : map.keySet()) {
			createSheet(wb, createHelper, key, map.get(key));
		}
		
		wb.write(out);
		wb.close();
	}
	private void createSheet(XSSFWorkbook wb, CreationHelper createHelper, String key, List<OriginalReport> categorizedList) {
		Sheet sheet = wb.createSheet(key);
		
		int rowNum = 0;
		
		Row title = sheet.createRow(rowNum++);
		title.createCell(0).setCellValue(createHelper.createRichTextString("服务请求标识"));
		title.createCell(1).setCellValue(createHelper.createRichTextString("举报手机号码"));
		title.createCell(2).setCellValue(createHelper.createRichTextString("举报受理省"));
		title.createCell(3).setCellValue(createHelper.createRichTextString("用户举报时间"));
		title.createCell(4).setCellValue(createHelper.createRichTextString("被举报号码/网站地址"));
		title.createCell(5).setCellValue(createHelper.createRichTextString("被举报号码归属省"));
		title.createCell(6).setCellValue(createHelper.createRichTextString("归属地市"));
		title.createCell(7).setCellValue(createHelper.createRichTextString("服务请求类别"));
		title.createCell(8).setCellValue(createHelper.createRichTextString("业务平台名称"));
		title.createCell(9).setCellValue(createHelper.createRichTextString("举报对象类型"));
		title.createCell(10).setCellValue(createHelper.createRichTextString("举报内容"));
		title.createCell(11).setCellValue(createHelper.createRichTextString("下发内容"));
		title.createCell(12).setCellValue(createHelper.createRichTextString("投诉类别"));
		title.createCell(13).setCellValue(createHelper.createRichTextString("所属客户"));
		title.createCell(14).setCellValue(createHelper.createRichTextString("简称"));
		
		CellStyle reportDateCellStyle = wb.createCellStyle();
		reportDateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd"));
		
		for (OriginalReport orre : categorizedList) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(createHelper.createRichTextString(orre.getServerRequestIdentifier()));
			row.createCell(1).setCellValue(createHelper.createRichTextString(orre.getReportMobileNumber()));
			row.createCell(2).setCellValue(createHelper.createRichTextString(orre.getReportProvince()));
			Cell cell3 = row.createCell(3);
			cell3.setCellValue(orre.getReportDate());
			cell3.setCellStyle(reportDateCellStyle);
			row.createCell(4).setCellValue(createHelper.createRichTextString(orre.getReportedNumber()));
			row.createCell(5).setCellValue(createHelper.createRichTextString(orre.getReportedProvince()));
			row.createCell(6).setCellValue(createHelper.createRichTextString(orre.getReportedCity()));
			row.createCell(7).setCellValue(createHelper.createRichTextString(orre.getServerRequestType()));
			row.createCell(8).setCellValue(createHelper.createRichTextString(orre.getBizPlatform()));
			row.createCell(9).setCellValue(createHelper.createRichTextString(orre.getReportObjectType()));
			row.createCell(10).setCellValue(createHelper.createRichTextString(orre.getReportContent()));
			row.createCell(11).setCellValue(createHelper.createRichTextString(orre.getDistContent()));
			row.createCell(12).setCellValue(createHelper.createRichTextString(orre.getComplaintType()));
			row.createCell(13).setCellValue(createHelper.createRichTextString(orre.getMatchesClientPortRelationship().getClient()));
			row.createCell(14).setCellValue(createHelper.createRichTextString(orre.getMatchesClientPortRelationship().getCompanyShortName()));
		}
	}
	private LinkedHashMap<String, List<OriginalReport>> typeMap() {
		LinkedHashMap<String, List<OriginalReport>> map = new LinkedHashMap<>();
		map.put("A", new ArrayList<>());
		map.put("B", new ArrayList<>());
		map.put("C", new ArrayList<>());
		map.put("D", new ArrayList<>());
		map.put("E", new ArrayList<>());
		map.put("未分类", new ArrayList<>());
		
		for (OriginalReport orre : orreList) {
			map.get(orre.getComplaintType()).add(orre);
		}
		
		final Collator collator = Collator.getInstance(Locale.SIMPLIFIED_CHINESE);
		for (String key : map.keySet()) {
			map.get(key).sort((o1, o2) -> {
				Date reportDate1 = o1.getReportDate();
				Date reportDate2 = o2.getReportDate();
				if (!reportDate1.equals(reportDate2)) {
					return reportDate1.compareTo(reportDate2);
				}
				CollationKey collationKey1 = collator.getCollationKey(o1.getMatchesClientPortRelationship().getClient());
				CollationKey collationKey2 = collator.getCollationKey(o2.getMatchesClientPortRelationship().getClient());
				return collationKey1.compareTo(collationKey2);
			});
		}
		return map;
	}
	
}
