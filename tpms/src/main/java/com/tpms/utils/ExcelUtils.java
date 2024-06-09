package com.tpms.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.tpms.entity.ResourcePoolHistory;

public class ExcelUtils {

	
	//Check File is of Excel Type or not

public static boolean checkExcelFormat(MultipartFile file) {
	
	String contentType=file.getContentType();
	
	Optional<String> cont = Optional.ofNullable(contentType);
	
	if(cont.equals(Optional.ofNullable("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))||cont.equals(Optional.ofNullable("application/vnd.ms-excel"))) {
		return true;
	}else {
		return false;
	}
}

//Convert Excel to List of Employee
@SuppressWarnings("resource")
public static List<ResourcePoolHistory> convertExceltoListofEmployee(InputStream is, LocalDate allocationDate){
	
	List<ResourcePoolHistory> excelResourceList = new ArrayList<>();

	try {

		XSSFWorkbook workbook = new XSSFWorkbook(is);

		XSSFSheet sheet = workbook.getSheetAt(0);

		int rowNumber = 0;

		Iterator<Row> iterator = sheet.iterator();
		while (iterator.hasNext()) {

			Row row = iterator.next();
			if (rowNumber == 0) {
				rowNumber++;
				continue;
			}

			Iterator<Cell> cells = row.iterator();

			int cid = 0;

			ResourcePoolHistory resourcePoolHistory = new ResourcePoolHistory();

			while (cells.hasNext()) {
				Cell cell = cells.next();
				String cellValue = cellToString(cell);

				switch (cid) {
				case 1:
					resourcePoolHistory.setResourceCode(cellValue);
					break;
				case 2:
					resourcePoolHistory.setResourceName(cellValue);
					break;
				case 3:
					resourcePoolHistory.setDesignation(cellValue);
					break;
				case 4:
					resourcePoolHistory.setPlatform(cellValue);
					break;
				case 5:
					resourcePoolHistory.setEmail(cellValue);
					break;
				case 6:
					resourcePoolHistory.setPhoneNo(cellValue);
					break;
				case 7:
					resourcePoolHistory.setLocation(cellValue);
					break;
				case 8:
					resourcePoolHistory.setEngagementPlan(cellValue);
					break;
				case 9:
					resourcePoolHistory.setExperience(cellValue);
					break;
				default:
					break;
				}
				cid++;
				resourcePoolHistory.setDeletedFlag((byte) 0);
				resourcePoolHistory.setAllocationDate(allocationDate);

			}

			excelResourceList.add(resourcePoolHistory);

		}

	} catch (Exception e) {
		e.printStackTrace();
	}
	return excelResourceList;
}


//Convert Excel to List of Employee For Validation
@SuppressWarnings("resource")
public static List<ResourcePoolHistory> convertExceltoListofEmployeeForValidation(InputStream is/* , LocalDate allocationDate */){
	
	List<ResourcePoolHistory> excelResourceList = new ArrayList<>();

	try {

		XSSFWorkbook workbook = new XSSFWorkbook(is);
		XSSFSheet sheet = workbook.getSheetAt(0);

		int rowNumber = 0;

		Iterator<Row> iterator = sheet.iterator();
		while (iterator.hasNext()) {

			Row row = iterator.next();
			if (rowNumber == 0) {
				rowNumber++;
				continue;
			}

			Iterator<Cell> cells = row.iterator();

			int cid = 0;

			ResourcePoolHistory resourcePoolHistory = new ResourcePoolHistory();

			while (cells.hasNext()) {
				Cell cell = cells.next();
				String cellValue = cellToString(cell);

				switch (cid) {
				case 1:
					resourcePoolHistory.setResourceCode(cellValue);
					break;
				case 2:
					resourcePoolHistory.setResourceName(cellValue);
					break;
				case 3:
					resourcePoolHistory.setDesignation(cellValue);
					break;
				case 4:
					resourcePoolHistory.setPlatform(cellValue);
					break;
				case 5:
					resourcePoolHistory.setEmail(cellValue);
					break;
				case 6:
					resourcePoolHistory.setPhoneNo(cellValue);
					break;
				case 7:
					resourcePoolHistory.setLocation(cellValue);
					break;
				case 8:
					resourcePoolHistory.setEngagementPlan(cellValue);
					break;
				case 9:
					resourcePoolHistory.setExperience(cellValue);
					break;
				default:
					break;
				}
				cid++;
				resourcePoolHistory.setDeletedFlag((byte) 0);

			}

			excelResourceList.add(resourcePoolHistory);

		}

	} catch (Exception e) {
		e.printStackTrace();
	}
	return excelResourceList;
}


@SuppressWarnings("resource")
public static String checkExcelinproperorder(InputStream is){
	
	String response = "";
	List<String> excelColumns = new ArrayList<>();
	List<String> staticColoums = new ArrayList<>();
	
	Collections.addAll(staticColoums, "Sl No","Employee Code","Employee Name","Designation","Technology","Email","Phone","Location","Engagement Plan","Exp.");
	
	try {
		
		XSSFWorkbook workbook = new XSSFWorkbook(is);
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		Iterator<Cell> cellIterator = sheet.getRow(0).cellIterator();
		
		while (cellIterator.hasNext())
		{
			excelColumns.add(cellToString(cellIterator.next()));
		}
		
		response = excelColumns.equals(staticColoums) ? "1" : "2" ;
		
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return response;
}


//Check For Phone Duplicacy Validation

@SuppressWarnings("resource")
public static String checkExcelphoneDuplicacy(MultipartFile file) throws IOException{
	String phone=null;
	List<ResourcePoolHistory> resourcepoolexcel = ExcelUtils.convertExceltoListofEmployeeForValidation(file.getInputStream());
	List<String> phoneno = new ArrayList<>();
	try {
		

		for(int i=0;i<resourcepoolexcel.size();i++) {
			phone=resourcepoolexcel.get(i).getPhoneNo();
			if(!phoneno.contains(resourcepoolexcel.get(i).getPhoneNo())) {
			phoneno.add(resourcepoolexcel.get(i).getPhoneNo());
		
			}
			else {
				break;
			}
		}
		
	}

	 catch (IndexOutOfBoundsException e) {
		e.printStackTrace();
	}
	
	if(resourcepoolexcel.size()==phoneno.size()) {
	 return "Uniqueness";	
	}
	return phone;
}

//Check For Email Duplicacy Validation
@SuppressWarnings("resource")
public static String checkExcelEmailDuplicacy(MultipartFile file)  throws IOException{
	String email=null;
	List<ResourcePoolHistory> resourcepoolexcel = ExcelUtils.convertExceltoListofEmployeeForValidation(file.getInputStream());
	List<String> emailid = new ArrayList<>();
	try {
		
		for(int i=0;i<resourcepoolexcel.size();i++) {
			email=resourcepoolexcel.get(i).getEmail();
			if(!emailid.contains(email)) {
				emailid.add(resourcepoolexcel.get(i).getEmail());}
			else {
				break;
			}
		}
	}

	catch (IndexOutOfBoundsException e) {

		e.printStackTrace();
	}
	if(resourcepoolexcel.size()==emailid.size()) {
		 return "Uniqueness";	
		}
	
	return email;
}


//Check For Resource Code ID Duplicacy Validation
@SuppressWarnings("resource")
public static String checkExcelresourceidDuplicacy(MultipartFile file)  throws IOException{
	String resourcecode=null;
	List<ResourcePoolHistory> resourcepoolexcel = ExcelUtils.convertExceltoListofEmployeeForValidation(file.getInputStream());
	List<String> resourceCodelist = new ArrayList<>();
	try {
		
		for(int i=0;i<resourcepoolexcel.size();i++) {
			resourcecode=resourcepoolexcel.get(i).getResourceCode();
			if(!resourceCodelist.contains(resourcecode)) {
				resourceCodelist.add(resourcepoolexcel.get(i).getResourceCode());}
			else {
				break;
			}
		}
	}

	catch (IndexOutOfBoundsException e) {

		e.printStackTrace();
	}
	if(resourcepoolexcel.size()==resourceCodelist.size()) {
		 return "Uniqueness";	
		}
	
	return resourcecode;
}

//Check For Both Email and Phone Number Duplicacy in Excel
@SuppressWarnings("resource")
public static Map<String, List<String>> checkExcelphoneEmailDuplicacy(MultipartFile file) throws IOException {

	List<ResourcePoolHistory> resourcepoolexcel = ExcelUtils
			.convertExceltoListofEmployeeForValidation(file.getInputStream());

	List<String> getAllphoneno = resourcepoolexcel.stream().map((r) -> r.getPhoneNo()).collect(Collectors.toList());

	Set<String> duplicatephoneno = getAllphoneno.stream().filter(i -> Collections.frequency(getAllphoneno, i) > 1)
			.collect(Collectors.toSet());

	List<String> getAllemailId = resourcepoolexcel.stream().map((r) -> r.getEmail()).collect(Collectors.toList());

	Set<String> duplicatemailId = getAllemailId.stream().filter(i -> Collections.frequency(getAllemailId, i) > 1)
			.collect(Collectors.toSet());

	List<String> duplicateListphoneno = duplicatephoneno.stream().collect(Collectors.toList());
	List<String> duplicateListemail = duplicatemailId.stream().collect(Collectors.toList());
	Map<String, List<String>> map = new HashedMap<>();

	try {

		map.put("Phone", duplicateListphoneno);
		map.put("Email", duplicateListemail);

	}

	catch (IndexOutOfBoundsException e) {

		e.printStackTrace();
	}

	return map;
}

private static String cellToString(Cell cell) {
    switch (cell.getCellType()) {
        case STRING:
            return cell.getStringCellValue();
        case NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toString();
            } else {
                double numericValue = cell.getNumericCellValue();
                if (String.valueOf(numericValue).contains("E")) {
                  
                    DecimalFormat df = new DecimalFormat("0");
                    return df.format(numericValue);
                } else {
                   
                    return String.valueOf(numericValue);
                }
            }
        case BOOLEAN:
            return String.valueOf(cell.getBooleanCellValue());
        case FORMULA:
            return cell.getCellFormula();
        case BLANK:
            return "NA"; 
        default:
            return "NA"; 
    }
}

}




