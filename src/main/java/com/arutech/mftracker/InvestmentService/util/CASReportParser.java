package com.arutech.mftracker.InvestmentService.util;


import com.arutech.mftracker.InvestmentService.dto.cas.EcasReport;
import com.arutech.mftracker.InvestmentService.service.EcasParserService;
import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.*;
import technology.tabula.extractors.BasicExtractionAlgorithm;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class CASReportParser {


    public EcasReport parsePdfReport(FileInputStream pdfFile, String password) throws IOException {
        EcasReport report = new EcasReport();

        try (PDDocument document = PDDocument.load(pdfFile, password)) {
            ObjectExtractor extractor = new ObjectExtractor(document);
            BasicExtractionAlgorithm sea = new BasicExtractionAlgorithm();

            List<String> data = new ArrayList<>();
            // Process each page
            for (int i = 1; i <= document.getNumberOfPages(); i++) {
                Page page = extractor.extract(i);
                List<Table> tables = sea.extract(page);

                for (Table table : tables) {
                    int colCount = table.getColCount();
                    if (colCount < 7) continue;
                    table.getRows().stream().forEach(row -> {
                        StringBuffer buffer = new StringBuffer();
                        row.stream().forEach(cell -> {
                            buffer.append(cell.getText());
                        });
                        data.add(buffer.toString());
                    });
                }
            }
//            data.stream().forEach(System.out::println);
            return new EcasParserService().parseEcasReport(data.stream().collect(Collectors.joining("\n")));

        }

    }


    public static void main(String[] args) {
        // Path to the password-protected PDF file
        String filePath = "/Users/anshumanpurushottam/Downloads/CAS_11102024-09012025_CP178911441_09012025091823810.pdf";

        // Password for the protected PDF file
        String password = "pwd123";
        try (FileInputStream fis = new FileInputStream(filePath)) {
            PDDocument document = PDDocument.load(fis, password);
            SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
            PageIterator pi = new ObjectExtractor(document).extract();
            List<String> data = new ArrayList<>();
            while (pi.hasNext()) {
                // iterate over the pages of the document
                Page page = pi.next();
                List<Table> table = sea.extract(page);
                // iterate over the tables of the page
                for (Table tables : table) {
                    List<List<RectangularTextContainer>> rows = tables.getRows();
                    // iterate over the rows of the table
                    for (List<RectangularTextContainer> cells : rows) {
                        // print all column-cells of the row plus linefeed
                        for (RectangularTextContainer content : cells) {
                            // Note: Cell.getText() uses \r to concat text chunks
                            String text = content.getText().replace("\r", " ");
//                            System.out.print(text + "|");
                            data.add(text);
                        }
//                        System.out.println();
                    }
                }
            }

            data.stream().forEach(str -> System.out.println(str));
            EcasReport report = new EcasParserService().parseEcasReport(data.stream().collect(Collectors.joining("\n")));
            System.out.println("report = " + report);


        } catch (IOException e) {
            e.printStackTrace();
        }

//        EcasReport report = null;
//        try {
//            report = new CASReportParser().parsePdfReport(new FileInputStream(filePath), password);
//            System.out.println("report.getSchemes().size() = " + report.getSchemes().size());
//            report.getSchemes().stream().forEach(System.out::println);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


    }
}
