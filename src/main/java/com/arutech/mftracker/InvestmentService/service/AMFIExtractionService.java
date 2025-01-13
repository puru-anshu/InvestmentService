package com.arutech.mftracker.InvestmentService.service;


import com.arutech.mftracker.InvestmentService.model.FundDetail;
import com.arutech.mftracker.InvestmentService.model.Return;
import com.arutech.mftracker.InvestmentService.repository.FundDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AMFIExtractionService {

    @Autowired
    private FundDetailRepository fundDetailRepository;

    @Autowired
    RestTemplate restTemplate;

    public Map<String, String> loadAMCFromCSV() {
        Map<String, String> amcMap = new HashMap<>();
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:data/amc.csv");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 2) {
                    continue;
                }
                String decodedValue = java.net.URLDecoder.decode(data[1], "UTF-8");
                amcMap.put(data[0], decodedValue);
            }
        } catch (IOException e) {
            log.warn("Error loading AMC data: {}", e.getMessage());
        }
        return amcMap;
    }


    public List<Map<String, String>> loadMutualFunds() throws IOException {

        Map<String, String> amcMap = loadAMCFromCSV();

        String navAllUrl = "https://www.amfiindia.com/spages/NAVAll.txt";
        String navALLString = restTemplate.getForObject(navAllUrl, String.class);
        StringReader navAllReader = new StringReader(navALLString);


        List<Map<String, String>> navAllData = readCSV(navAllReader, CSVFormat.DEFAULT
                .withDelimiter(';').withFirstRecordAsHeader());

        String name, type, plan, isin, schemeCode;
        String amcCode = "";
        String amcName = "";
        List<Map<String, String>> data = new ArrayList<>();
        for (Map<String, String> nav : navAllData) {
            String code = nav.get("Scheme Code");
            if (code != null && !code.endsWith("Fund")) {
                String schemeName = nav.get("Scheme Name");
                schemeCode = code;
                if (schemeName != null && !schemeName.isEmpty()) {
                    String[] values = schemeName.split("-");
                    name = values[0].trim();
                    if (values.length > 1) {
                        int length = values.length;
                        type = values[length - 1].trim();
                        plan = values[length - 2].trim();

                    } else {
                        type = "";
                        plan = "";
                    }
                    isin = nav.get("ISIN Div Reinvestment");
                    if (isin.trim().equals("-")) {
                        isin = nav.get("ISIN Div Payout/ ISIN Growth");
                    }

                    Map<String, String> mutualFundData = new HashMap<>();
                    mutualFundData.put("schemeCode", schemeCode);
                    mutualFundData.put("name", name);
                    mutualFundData.put("amcName", amcName);
                    mutualFundData.put("amcCode", amcCode);
                    mutualFundData.put("type", type);
                    mutualFundData.put("plan", plan);
                    mutualFundData.put("isin", isin);
                    data.add(mutualFundData);
                    //log.info("schemeCode: {},name: {}, amc: {},amcCode: {}, type: {}, plan: {} ,isin {}", schemeCode, name, amcName, amcCode, type, plan, isin);
                }
            } else {
                amcName = code;
                amcCode = getAmcCode(amcName, amcMap);
            }


        }

        return data;
    }

    private String getAmcCode(String amcName, Map<String, String> amcMap) {
        for (Map.Entry<String, String> entry : amcMap.entrySet()) {
            String firstWord = amcName.split(" ")[0];
            if (firstWord.equals(entry.getValue().split(" ")[0])) {
                return entry.getKey();
            }
        }
        return "";
    }

    private List<Map<String, String>> readCSV(StringReader reader, CSVFormat format) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();
        try (
                CSVParser csvParser = new CSVParser(reader, format)) {
            for (CSVRecord csvRecord : csvParser) {
                Map<String, String> row = new HashMap<>();
                csvRecord.toMap().forEach(row::put);
                data.add(row);
            }
        }
        return data;
    }


    public List<FundDetail> readFundDetailsFromCSV() throws IOException {
        List<FundDetail> fundDetails = new ArrayList<>();
        Resource resource = new ClassPathResource("data/vro_amfi_complete_data_20250104_022402.csv");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord record : csvParser) {
                FundDetail fundDetail = new FundDetail();
                String name = record.get("scheme");
                if (name == null || name.isEmpty() || name.startsWith("The underlying")) {
                    continue;
                }
                String[] values = name.split("\\|");
                String schemeName = values[0].trim();
                String planId = "";
                if (values.length > 1) {
                    planId = values[1].split("=")[1].trim();
                }
                fundDetail.setSchemeName(schemeName);
                fundDetail.setDataPlanId(planId);
//            fundDetail.setSchemeCode(record.get("Scheme Code"));
//            fundDetail.setAmcName(record.get("AMC Name"));
//            fundDetail.setAmcCode(record.get("AMC Code"));
//            fundDetail.setIsin(record.get("ISIN"));

                fundDetail.setEndType(record.get("end_type"));
                fundDetail.setCategory(record.get("category"));
                fundDetail.setPrimaryCategory(record.get("primary_category"));
                fundDetail.setBenchmark(record.get("Benchmark"));
                fundDetail.setRiskometerScheme(record.get("Riskometer-Scheme"));
                fundDetail.setRiskometerBenchmark(record.get("Riskometer-Benchmark"));
                fundDetail.setNav(record.get("Nav-Regular"));
                //fundDetail.setNavDate(record.get("NAV Date"));
                fundDetail.setDailyAum(record.get("Daily AUM").split("\\|")[0]);

                // Return Regular
                String returnRegular = record.get("Return-Regular");

                fundDetail.setReturns(getReturns(returnRegular));

                // Return Regular
                String returnDirect = record.get("Return-Direct");
                fundDetail.setReturnsDirect(getReturns(returnDirect));

                // Return Regular
                String returnBenchmark = record.get("Return-BenchMark");
                fundDetail.setReturnsBenchmark(getReturns(returnBenchmark));

                String returnBenchmarkDirect = record.get("period-return-bench-direct");
                fundDetail.setReturnsBenchmarkDirect(getReturns(returnBenchmarkDirect));


                fundDetails.add(fundDetail);

            }
        }
        return fundDetails;
    }

    private List<Return> getReturns(String returnRegular) {
        List<Return> returns = new ArrayList<>();
        //16.75|data-1y=18.08|data-3y=13.83|data-5y=16.75|data-10y=12.30|data-sl=19.21
        if (returnRegular != null) {
            String[] values = returnRegular.split("\\|");
            for (String value : values) {
                if (!value.contains("=")) continue;
                String[] returnValues = value.split("=");
                if (returnValues.length < 2) continue;
                Return return1 = new Return();
                return1.setInterval(returnValues[0].replace("data-", ""));
                return1.setReturnPercentage(returnValues[1]);
                returns.add(return1);
            }
        }
        return returns;


    }

    public void refreshFundDetails() throws IOException {
        fundDetailRepository.deleteAll();
        List<FundDetail> details = readFundDetailsFromCSV();
        log.info("Saving {} fund details", details.size());
        fundDetailRepository.saveAll(details);
    }


    public static void main(String[] args) {


        try {
            List<Map<String, String>> mutualFunds = new AMFIExtractionService().loadMutualFunds();
            //System.out.println(mutualFunds);
            System.out.println("Reading fund details");
            List<FundDetail> list = new AMFIExtractionService().readFundDetailsFromCSV();
            System.out.println("list.size() = " + list.size());
            list.stream().forEach(System.out::println);

//            list.forEach(fundDetail -> {
//                System.out.println(fundDetail.getSchemeName());
//                String name = fundDetail.getSchemeName();
//                List<Map<String, String>> mutalFunds = mutualFunds.stream().
//                        filter(row -> row.get("name").equals(name))
//                        .collect(Collectors.toList());
//                System.out.println(mutalFunds.size());
//
//                System.out.println();
//            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
