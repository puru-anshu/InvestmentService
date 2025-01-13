package com.arutech.mftracker.InvestmentService.service;

import com.arutech.mftracker.InvestmentService.model.Portfolio;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
public class CASInvestmentExtractionService {



    public List<Portfolio> extractPortfolioFromExcel(String userId, MultipartFile file) {
        List<Portfolio> portfolios = new ArrayList<>();


        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            if (null == sheet) {
                log.warn("could not read sheet");
                return portfolios;
            }
            StringWriter stringWriter = new StringWriter();
            CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT.withRecordSeparator("\n"));
            Iterator<Row> iterator = sheet.iterator();
            int count = 0;
            boolean toContinue = false;
            while (iterator.hasNext()) {
                Row row = iterator.next();

                if (!toContinue && row.getCell(0).getCellType() == CellType.STRING && row.getCell(0).getStringCellValue().equals("Scheme Name")) {
                    toContinue = true;
                }
                if (!toContinue)
                    continue;
                String[] rowData = new String[row.getPhysicalNumberOfCells()];
                for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case STRING:
                                rowData[i] = cell.getStringCellValue();
                                break;
                            case NUMERIC:
                                rowData[i] = String.valueOf(cell.getNumericCellValue());
                                break;
                            case BOOLEAN:
                                rowData[i] = String.valueOf(cell.getBooleanCellValue());
                                break;
                            default:
                                rowData[i] = ""; // Handle other types (e.g., formulas, errors, etc.)
                        }
                    } else {
                        rowData[i] = ""; // Handle empty cells
                    }
                }
//                if(count++ == 0)
//                    continue;
                csvPrinter.printRecord((Object[]) rowData);

            }
            csvPrinter.flush();
            log.info(stringWriter.toString());
            CSVParser parser = new CSVParser(new StringReader(stringWriter.toString()),
                    CSVFormat.DEFAULT.withFirstRecordAsHeader());
            for (CSVRecord csvRecord : parser) {
                Portfolio portfolio = new Portfolio();
                portfolio.setUserId(userId);
                //Scheme Name, AMC Name, Category, Folio No., Invested Value, Current Value, Returns, Units
                portfolio.setSchemeName(csvRecord.get("Scheme Name"));
                if (portfolio.getSchemeName().isEmpty())
                    continue;
                portfolio.setAmcName(csvRecord.get("AMC Name"));
                portfolio.setCategory(csvRecord.get("Category"));
                portfolio.setFolioNumber(csvRecord.get("Folio No."));
                portfolio.setInvestedValue(Double.parseDouble(csvRecord.get("Invested Value")));
                portfolio.setUnits(Double.parseDouble(csvRecord.get("Units")));
                portfolio.setCurrentValue(Double.parseDouble(csvRecord.get("Current Value")));
//                portfolio.setLastUpdateDate(csvRecord.get("NAVDate"));
//                portfolio.setAnnualizedXIRR(Double.parseDouble(csvRecord.get("Annualised XIRR")));

                if (portfolio.getUnits() > 0)
                    portfolios.add(portfolio);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error processing the Excel file", e);
        }

        return portfolios;
    }


}