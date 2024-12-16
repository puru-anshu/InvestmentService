package com.arutech.mftracker.InvestmentService;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

import org.apache.commons.csv.*;


public class MergeFiles {
    public static void main(String[] args) {
        String instrumentsFile = "data/instruments.csv";
        String navAllFile = "data/NAVAll.txt";
        String outputFile = "data/merged_output.csv";

        try {
            List<Map<String, String>> instrumentsData = readCSV(instrumentsFile, CSVFormat.DEFAULT.withFirstRecordAsHeader());
            System.out.println("instrumentsData = " + instrumentsData.size());
            List<Map<String, String>> navAllData = readCSV(navAllFile, CSVFormat.DEFAULT
                    .withDelimiter(';').withFirstRecordAsHeader());
            System.out.println("navAllData = " + navAllData.size());


            navAllData.stream().filter(row -> row.get("Scheme Code") != null).forEach(System.out::println);



            List<Map<String, String>> mergedData = mergeData(instrumentsData, navAllData);
            System.out.println("mergedData = " + mergedData.size());

            writeCSV(outputFile, mergedData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Map<String, String>> readCSV(String filePath,CSVFormat format) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             CSVParser csvParser = new CSVParser(reader, format)) {
            for (CSVRecord csvRecord : csvParser) {
                Map<String, String> row = new HashMap<>();
                csvRecord.toMap().forEach(row::put);
                data.add(row);
            }
        }
        return data;
    }

    private static List<Map<String, String>> mergeData(List<Map<String, String>> instrumentsData, List<Map<String, String>> navAllData) {

        List<Map<String, String>> mergedData = new ArrayList<>();
        for (Map<String, String> instrument : instrumentsData) {
            String tradingsymbol = instrument.get("tradingsymbol");
            for(Map<String, String> nav : navAllData) {
                String isin = nav.get("ISIN Div Payout/ ISIN Growth");
                String isinDivPayout = nav.get("ISIN Div Payout/ ISIN Growth");
                if (tradingsymbol.equals(isinDivPayout) || tradingsymbol.equals(isin)) {
                    Map<String, String> mergedRow = new HashMap<>(instrument);
                    mergedRow.put("scheme_code", nav.get("Scheme Code"));
                    mergedData.add(mergedRow);
                    break;
                }
            }

        }
        return mergedData;
    }

    private static void writeCSV(String filePath, List<Map<String, String>> data) throws IOException {
        if (data.isEmpty()) {
            return;
        }

        try (Writer writer = Files.newBufferedWriter(Paths.get(filePath));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(data.get(0).keySet().toArray(new String[0])))) {
            for (Map<String, String> row : data) {
                csvPrinter.printRecord(row.values());
            }
        }
    }
}
