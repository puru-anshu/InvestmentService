package com.arutech.mftracker.InvestmentService.service;

import com.arutech.mftracker.InvestmentService.model.MFInstrument;
import com.arutech.mftracker.InvestmentService.repository.InstrumentRepository;
import jakarta.annotation.PostConstruct;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
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

        String url = "https://api.kite.trade/mf/instruments";
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
            instruments.add(instrument);
        }
        instrumentRepository.saveAll(instruments);
    }

    private Double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
