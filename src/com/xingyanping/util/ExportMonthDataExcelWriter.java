package com.xingyanping.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

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
		XSSFWorkbook wb = new XSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		Sheet sheet = wb.createSheet("不良信息资料查询");
		
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
		
		for (OriginalReport orre : orreList) {
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
		wb.write(out);
		wb.close();
	}
	
}
