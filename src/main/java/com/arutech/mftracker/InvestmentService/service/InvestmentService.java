package com.arutech.mftracker.InvestmentService.service;

import com.arutech.mftracker.InvestmentService.model.Investment;
import com.arutech.mftracker.InvestmentService.repository.InvestmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
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
public class InvestmentService {

    @Autowired
    private InvestmentRepository investmentRepository;

    public Investment addInvestment(String userId, Investment investment) {
        return investmentRepository.save(investment);
    }

    public List<Investment> getUserInvestments(String userId) {
        return investmentRepository.findByUserId(userId);
    }

    public Investment updateInvestment(String userId, String investmentId, Investment investment) {
        return investmentRepository.save(investment);
    }

    public void deleteInvestment(String userId, String investmentId) {
        Investment investment = investmentRepository.findByUserIdAndInvestmentId(userId, investmentId);
        if (null != investment)
            investmentRepository.delete(investment);
    }

    public void saveInvestments(String userId, List<Investment> investments) {
        investmentRepository.saveAll(investments);
    }


    public List<Investment> extractInvestmentsFromExcel(String userId, MultipartFile file) {
        List<Investment> investments = new ArrayList<>();


        try (InputStream is = file.getInputStream(); Workbook workbook = new HSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            if (null == sheet) {
                log.warn("could not read sheet");
                return investments;
            }
            StringWriter stringWriter = new StringWriter();
            CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT.withRecordSeparator("\n"));
            Iterator<Row> iterator = sheet.iterator();
            int count = 0;
            while (iterator.hasNext()) {
                Row row = iterator.next();
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
                // Write the row to the CSVPrinter
//                if(count++ > 0)
                csvPrinter.printRecord((Object[]) rowData);

            }
            csvPrinter.flush();
            log.info(stringWriter.toString());
            CSVParser parser = new CSVParser(new StringReader(stringWriter.toString()),
                    CSVFormat.DEFAULT.withFirstRecordAsHeader());
            for (CSVRecord csvRecord : parser) {
                log.info("{}", csvRecord);
                Investment investment = new Investment();
                investment.setUserId(userId);
                investment.setSchemeName(csvRecord.get("SCHEME_NAME"));
                if (investment.getSchemeName().isEmpty())
                    continue;
                investment.setAmcName(csvRecord.get("MF_NAME"));
                investment.setFundType(csvRecord.get("Type"));
                investment.setFolioNumber(csvRecord.get("FOLIO_NUMBER"));
                investment.setTradeDate(csvRecord.get("TRADE_DATE"));
                investment.setTransactionType(csvRecord.get("TRANSACTION_TYPE"));
                investment.setInvestorName(csvRecord.get("INVESTOR_NAME"));
                investment.setPAN(csvRecord.get("PAN"));
                investment.setProductCode(csvRecord.get("PRODUCT_CODE"));
                if (null != csvRecord.get("AMOUNT") && !csvRecord.get("AMOUNT").isEmpty())
                    investment.setAmount(Double.parseDouble(csvRecord.get("AMOUNT")));
                if (null != csvRecord.get("UNITS") && !csvRecord.get("UNITS").isEmpty())
                    investment.setUnits(Double.parseDouble(csvRecord.get("UNITS")));
                if (null != csvRecord.get("PRICE") && !csvRecord.get("PRICE").isEmpty())
                    investment.setPrice(Double.parseDouble(csvRecord.get("PRICE")));
                investment.setBroker(csvRecord.get("BROKER"));

                investments.add(investment);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error processing the Excel file", e);
        }

        return investments;
    }


}