package com.arutech.mftracker.InvestmentService.service;

import com.arutech.mftracker.InvestmentService.dto.cas.EcasReport;
import com.arutech.mftracker.InvestmentService.dto.cas.InvestorDetails;
import com.arutech.mftracker.InvestmentService.dto.cas.MutualFundScheme;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EcasParserService {
    private static final String REGEX_PATTERN = "^(\\S+)\\s*(\\S+)\\s*([^\\d]+)([\\d,.]+)\\s*([\\d,.]+)\\s*(\\d{2}-[A-Za-z]{3}-\\d{4})\\s*([\\d,.]+)\\s*([\\d,.]+)?\\s*(\\w+)\\s*(.*)$";


    public EcasReport parseEcasReport(String reportContent) {
        EcasReport report = new EcasReport();

        // Parse report date
        Pattern datePattern = Pattern.compile("As on (\\d{2}-[A-Za-z]{3}-\\d{4})");
        Matcher dateMatcher = datePattern.matcher(reportContent);
        if (dateMatcher.find()) {
            report.setReportDate(dateMatcher.group(1));
        }

        // Parse investor details
        InvestorDetails investor = new InvestorDetails();
        Pattern emailPattern = Pattern.compile("Email Id: (.+)");
        Matcher emailMatcher = emailPattern.matcher(reportContent);
        if (emailMatcher.find()) {
            String email = emailMatcher.group(1);
            int idx = email.indexOf(".com");
            if (idx != -1) {
                email = email.substring(0, idx + 4);
            }
            investor.setEmailId(email);
        }

        Pattern namePattern = Pattern.compile("Name: (.+)");
        Matcher nameMatcher = namePattern.matcher(reportContent);
        if (nameMatcher.find()) {
            investor.setName(nameMatcher.group(1));
        }

        // Parse phone numbers
        Pattern phonePattern = Pattern.compile("Phone Res: (\\d+)");
        Matcher phoneMatcher = phonePattern.matcher(reportContent);
        if (phoneMatcher.find()) {
            investor.setPhoneRes(phoneMatcher.group(1));
        }

        Pattern mobilePattern = Pattern.compile("Mobile: \\+(\\d+)");
        Matcher mobileMatcher = mobilePattern.matcher(reportContent);
        if (mobileMatcher.find()) {
            investor.setMobile(mobileMatcher.group(1));
        }

        report.setInvestorDetails(investor);

        // Parse mutual fund schemes
        List<MutualFundScheme> schemes = new ArrayList<>();
        Pattern schemePattern = Pattern.compile(REGEX_PATTERN);
//        String pattern = "^" +
//                // Folio number (allows digits, forward slash)
//                "([\\d/]+)" +
//                // ISIN (12 character alphanumeric)
//                "\\s*(INF\\w{9})" +
//                // Scheme name (includes alphanumeric, spaces, hyphens, dots)
//                "\\s*([\\w\\s\\-\\.]+?)" +
//                // Cost value (amount with optional decimals and commas)
//                "\\s*(\\d+(?:,\\d+)*(?:\\.\\d+)?)" +
//                // Unit balance (amount with optional decimals and commas)
//                "\\s*(\\d+(?:,\\d+)*(?:\\.\\d+)?)" +
//                // Nav date (date in DD-MMM-YYYY format)
//                "\\s*(\\d{2}-[A-Za-z]{3}-\\d{4})" +
//                // Nav value (amount with optional decimals)
//                "\\s*(\\d+(?:\\.\\d+)?)" +
//                // Market value (amount with optional decimals and commas)
//                "\\s*(\\d+(?:,\\d+)*(?:\\.\\d+)?)" +
//                // Registrar (KFINTECH or CAMS)
//                "\\s*(KFINTECH|CAMS)" +
//                "$";
//        Pattern schemePattern = Pattern.compile(
//                pattern
//        );

        Matcher schemeMatcher = schemePattern.matcher(reportContent);
        for (String row : reportContent.split("\n")) {
            schemeMatcher = schemePattern.matcher(row);
            if (schemeMatcher.find() == false) continue;
            MutualFundScheme scheme = new MutualFundScheme();
            System.out.println(schemeMatcher.group(4));
            scheme.setFolioNo(schemeMatcher.group(1));
            scheme.setIsin(schemeMatcher.group(2));
            scheme.setSchemeName(schemeMatcher.group(3).trim());
            scheme.setCostValue(Double.parseDouble(schemeMatcher.group(4).replaceAll(",", "")));
            scheme.setUnitBalance(Double.parseDouble(schemeMatcher.group(5).replaceAll(",", "")));
            scheme.setNavDate(schemeMatcher.group(6));
            scheme.setNav(Double.parseDouble(schemeMatcher.group(7).replaceAll(",", "")));
            scheme.setMarketValue(Double.parseDouble(schemeMatcher.group(8).replaceAll(",", "")));
            scheme.setRegistrar(schemeMatcher.group(9));
            schemes.add(scheme);
        }

        report.setSchemes(schemes);
        return report;
    }
}
