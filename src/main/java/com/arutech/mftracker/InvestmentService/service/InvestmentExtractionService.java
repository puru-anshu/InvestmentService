package com.arutech.mftracker.InvestmentService.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.arutech.mftracker.InvestmentService.model.Investment;
import com.arutech.mftracker.InvestmentService.model.Portfolio;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class InvestmentExtractionService {

    public List<Investment> extractInvestmentsFromExcel(String userId, MultipartFile file) {
        List<Investment> investments = new ArrayList<>();


        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheet("Transaction Details");

            if (null == sheet) {
                log.warn("could not read sheet");
                return investments;
            }

            boolean foundRow = false;
            int rowNum = 0;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                String schemeName = "Scheme Name";

                Row row = sheet.getRow(i);
                if (row == null)
                    continue;
                String col1 = row.getCell(0).getStringCellValue();
                if (!foundRow && col1.equals(schemeName))
                    foundRow = true;
                if (!foundRow)
                    continue;
                if (rowNum++ == 0)
                    continue;


                Investment investment = new Investment();
                investment.setUserId(userId);
                investment.setSchemeName(row.getCell(0).getStringCellValue());
                investment.setTransactionDescription(row.getCell(1).getStringCellValue());
                investment.setDate(row.getCell(2).getStringCellValue());
                investment.setNav(cellValue(row.getCell(3)));
                investment.setUnits(row.getCell(4).getStringCellValue());
                investment.setAmount(row.getCell(5).getStringCellValue());

                investments.add(investment);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error processing the Excel file", e);
        }

        return investments;
    }


    public List<Portfolio> extractPortfolioFromExcel(String userId, MultipartFile file) {
        List<Portfolio> investments = new ArrayList<>();


        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            if (null == sheet) {
                log.warn("could not read sheet");
                return investments;
            }

            boolean foundRow = false;
            int rowNum = 0;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                String schemeName = "Scheme Name";

                Row row = sheet.getRow(i);
                if (row == null)
                    continue;
                String col1 = cellValue(row.getCell(0));
                if (!foundRow && col1.equals(schemeName))
                    foundRow = true;
                if (!foundRow)
                    continue;
                if (rowNum++ == 0)
                    continue;
                if (row.getCell(0) == null)
                    break;
                log.info(" processing " + rowNum);
                Portfolio investment = new Portfolio();
                investment.setUserId(userId);
                investment.setSchemeName(row.getCell(0).getStringCellValue());
                investment.setAmcName(row.getCell(1).getStringCellValue());
                log.info(investment.getAmcName());
                investment.setCategory(row.getCell(2).getStringCellValue());
                investment.setFolioNumber(cellValue(row.getCell(3)));
                String units =  cellValue(row.getCell(7));
                int unit = 0;
                double investedValue = 0d;
                try {
                    double d = Double.parseDouble(units);
                    unit = (int)d;
                    investedValue = Double.parseDouble(cellValue(row.getCell(5)));
                }catch (Exception ignored) {}
                investment.setUnits(unit);
                investment.setInvestedValue(investedValue);
                investments.add(investment);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error processing the Excel file", e);
        }

        return investments;
    }

    private String cellValue(Cell cell) {
        if (cell == null)
            return "";
        CellType type = cell.getCellType();
        switch (type) {
            case STRING -> {
                return cell.getStringCellValue();
            }
            case NUMERIC -> {
                return "" + cell.getNumericCellValue();
            }
            default -> {
                return cell.getRichStringCellValue().getString();
            }

        }
    }
}