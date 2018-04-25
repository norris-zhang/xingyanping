package com.xingyanping.util;

import static com.xingyanping.util.NumberFunctions.isEQ;
import static com.xingyanping.util.PathUtil.uploadFilePath;
import static org.apache.poi.ss.usermodel.CellType.BLANK;
import static org.apache.poi.ss.usermodel.CellType.BOOLEAN;
import static org.apache.poi.ss.usermodel.CellType.FORMULA;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.xingyanping.datamodel.OriginalReport;

public class ExcelUtil {
	public static List<OriginalReport> readOriginalReportFromExcel(Long upfiId, String originalName) throws Exception {
		List<OriginalReport> list = new ArrayList<>();
		Workbook workbook = WorkbookFactory.create(new File(uploadFilePath(upfiId, originalName)));
		Sheet sheet = workbook.getSheetAt(0);
		for (Row row : sheet) {
			if (row.getRowNum() == 0) {
				continue;
			}
			OriginalReport orre = new OriginalReport();
			orre.setFromFileId(upfiId);
			for (Cell cell : row) {
				setFromCell(orre, cell);
			}
			orre.setYearMonth(yearMonthStringFromDate(orre.getReportDate()));
			orre.setUpdated(new Date());
			list.add(orre);
		}
		return list;
	}
	private static String yearMonthStringFromDate(Date date) {
		if (date == null) {
			return "";
		}
		return new SimpleDateFormat("yyyyMM").format(date);
	}
	private static void setFromCell(OriginalReport orre, Cell cell) throws ParseException {
		switch (cell.getColumnIndex()) {
			case 0:
				orre.setServerRequestIdentifier(cellDataAsString(cell));
				break;
			case 1:
				orre.setReportMobileNumber(cellDataAsString(cell));
				break;
			case 2:
				orre.setReportProvince(cellDataAsString(cell));
				break;
			case 3:
				orre.setReportDate(cellDataAsDate(cell));
				break;
			case 4:
				orre.setReportedNumber(cellDataAsString(cell));
				break;
			case 5:
				orre.setReportedProvince(cellDataAsString(cell));
				break;
			case 6:
				orre.setReportedCity(cellDataAsString(cell));
				break;
			case 7:
				orre.setServerRequestType(cellDataAsString(cell));
				break;
			case 8:
				orre.setBizPlatform(cellDataAsString(cell));
				break;
			case 9:
				orre.setReportObjectType(cellDataAsString(cell));
				break;
			case 10:
				orre.setReportContent(cellDataAsString(cell));
				break;
			case 11:
				orre.setReportSource(cellDataAsString(cell));
				break;
			default:
				break;
		}
	}
	private static Date cellDataAsDate(Cell cell) throws ParseException {
		CellType cellType = cell.getCellTypeEnum();
		if (cellType == STRING) {
			return new SimpleDateFormat("yyyy-MM-dd").parse(cell.getRichStringCellValue().getString().trim());
		} else if (cellType == NUMERIC) {
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue();
			} else {
				throw new ParseException("Number for date", 0);
			}
		} else if (cellType == BOOLEAN) {
			throw new ParseException("Boolean for date", 0);
		} else if (cellType == FORMULA) {
			throw new ParseException("Formula for date", 0);
		} else if (cellType == BLANK) {
			return null;
		} else {
			throw new ParseException("Unknown cell type", 0);
		}
	}
	private static String cellDataAsString(Cell cell) throws ParseException {
		CellType cellType = cell.getCellTypeEnum();
		if (cellType == STRING) {
			return cell.getRichStringCellValue().getString().trim();
		} else if (cellType == NUMERIC) {
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue().toString();
			} else {
				DataFormatter df = new DataFormatter();
				return df.formatCellValue(cell);
			}
		} else if (cellType == BOOLEAN) {
			return "" + cell.getBooleanCellValue();
		} else if (cellType == FORMULA) {
			return cell.getCellFormula().trim();
		} else if (cellType == BLANK) {
			return null;
		} else {
			throw new ParseException("Unknown cell type", 0);
		}
	}
	public static void writeOriginalReportToExcel(List<OriginalReport> list, OutputStream out, Long lastFileId) throws IOException {
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
		title.createCell(8);
		title.createCell(9).setCellValue(createHelper.createRichTextString("举报对象类型"));
		title.createCell(10).setCellValue(createHelper.createRichTextString("举报内容"));
		title.createCell(11).setCellValue(createHelper.createRichTextString("举报来源"));
		
		CellStyle reportDateCellStyle = wb.createCellStyle();
		reportDateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd"));
		
		CellStyle newDataStyle = wb.createCellStyle();
		newDataStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		newDataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		CellStyle newDataReportDateCellStyle = wb.createCellStyle();
		newDataReportDateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd"));
		newDataReportDateCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		newDataReportDateCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		for (OriginalReport orre : list) {
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
			row.createCell(8);
			row.createCell(9).setCellValue(createHelper.createRichTextString(orre.getReportObjectType()));
			row.createCell(10).setCellValue(createHelper.createRichTextString(orre.getReportContent()));
			row.createCell(11).setCellValue(createHelper.createRichTextString(orre.getReportSource()));
			if (isEQ(orre.getFromFileId(), lastFileId, false)) {
				for (Cell cell : row) {
					if (cell.getColumnIndex() == 3) {
						cell.setCellStyle(newDataReportDateCellStyle);
					} else {
						cell.setCellStyle(newDataStyle);
					}
				}
			}
		}
		wb.write(out);
		wb.close();
	}
}
