package com.arutech.mftracker.InvestmentService.service;

import com.arutech.mftracker.InvestmentService.model.MFInstrument;
import com.arutech.mftracker.InvestmentService.repository.InstrumentRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MFInstrumentService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private InstrumentRepository instrumentRepository;

    @PostConstruct
    public void init() {
        try {
            fetchAndStoreInstruments();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void fetchAndStoreInstruments() throws IOException {

        instrumentRepository.deleteAll();


        String navAllUrl = "https://www.amfiindia.com/spages/NAVAll.txt";
        String navALLString = restTemplate.getForObject(navAllUrl, String.class);
        StringReader navAllReader = new StringReader(navALLString);
        List<Map<String, String>> navAllData = readCSV(navAllReader, CSVFormat.DEFAULT
                .withDelimiter(';').withFirstRecordAsHeader());


        final String url = "https://api.kite.trade/mf/instruments";
        String csvData = restTemplate.getForObject(url, String.class);
        StringReader stringReader = new StringReader(csvData);
        CSVParser csvParser = new CSVParser(stringReader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

        List<MFInstrument> instruments = new ArrayList<>();
        for (CSVRecord csvRecord : csvParser) {
            MFInstrument instrument = new MFInstrument();
            instrument.setTradingsymbol(csvRecord.get("tradingsymbol"));
            instrument.setName(csvRecord.get("name"));
            instrument.setAmc(csvRecord.get("amc"));
            instrument.setSchemeType(csvRecord.get("scheme_type"));
            instrument.setPlan(csvRecord.get("plan"));
            instrument.setLastPriceDate(csvRecord.get("last_price_date"));
            instrument.setLastPrice(parseDouble(csvRecord.get("last_price")));
            instrument.setSchemeCode(getSchemeCode(navAllData, instrument.getTradingsymbol()));
            instruments.add(instrument);
            if (null == instrument.getSchemeCode()) {
                log.info(instrument.getTradingsymbol() + " == " + instrument.getName() + "----"
                        + navALLString.indexOf(instrument.getTradingsymbol())); ;

            }
        }
        instrumentRepository.saveAll(instruments);
    }

    private String getSchemeCode(List<Map<String, String>> navAllData, String tradingsymbol) {
        for (Map<String, String> nav : navAllData) {
            String isin = nav.get("ISIN Div Payout/ ISIN Growth");
            String isinDivPayout = nav.get("ISIN Div Reinvestment");
            if (tradingsymbol.equals(isinDivPayout) || tradingsymbol.equals(isin)) {
                return nav.get("Scheme Code");
            }
        }
        return null;
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


    private Double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
