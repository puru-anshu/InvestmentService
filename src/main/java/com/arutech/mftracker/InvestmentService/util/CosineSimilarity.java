package com.arutech.mftracker.InvestmentService.util;

import java.util.HashMap;
import java.util.Map;

public class CosineSimilarity {


    public static double cosineSimilarity(String text1, String text2) {
        String firstword1 = text1.split("\\s+")[0].toLowerCase();
        String firstword2 = text2.split("\\s+")[0].toLowerCase();
        if (!firstword1.equals(firstword2))
            return 0.0;

        Map<String, Integer> freq1 = getTermFrequency(text1.toLowerCase());
        Map<String, Integer> freq2 = getTermFrequency(text2.toLowerCase());

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (String key : freq1.keySet()) {
            dotProduct += freq1.get(key) * freq2.getOrDefault(key, 0);
            norm1 += Math.pow(freq1.get(key), 2);
        }

        for (String key : freq2.keySet()) {
            norm2 += Math.pow(freq2.get(key), 2);
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    public static double positionalWeightedSimilarity(String sentence1, String sentence2) {
        String[] tokens1 = sentence1.split("\\s+");
        String[] tokens2 = sentence2.split("\\s+");

        Map<String, Integer> freq1 = getTermFrequency(tokens1);
        Map<String, Integer> freq2 = getTermFrequency(tokens2);

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        int maxLength = Math.max(tokens1.length, tokens2.length);

        for (int i = 0; i < maxLength; i++) {
            String token1 = i < tokens1.length ? tokens1[i] : "";
            String token2 = i < tokens2.length ? tokens2[i] : "";
            if(i==0 && !token1.equals(token2)) return  0;

            int weight = maxLength - i; // Higher weight for earlier positions

            if (freq1.containsKey(token1) && freq2.containsKey(token2) && token1.equals(token2)) {
                dotProduct += freq1.get(token1) * freq2.get(token2) * weight;
            }

            norm1 += freq1.getOrDefault(token1, 0) * weight;
            norm2 += freq2.getOrDefault(token2, 0) * weight;
        }

        norm1 = Math.sqrt(norm1);
        norm2 = Math.sqrt(norm2);

        return dotProduct / (norm1 * norm2);
    }

    private static Map<String, Integer> getTermFrequency(String text) {
        Map<String, Integer> termFrequency = new HashMap<>();
        String[] tokens = text.split("\\s+");
        for (String token : tokens) {
            termFrequency.put(token, termFrequency.getOrDefault(token, 0) + 1);
        }
        return termFrequency;
    }

    private static Map<String, Integer> getTermFrequency(String[] tokens) {
        Map<String, Integer> termFrequency = new HashMap<>();
        for (String token : tokens) {
            termFrequency.put(token, termFrequency.getOrDefault(token, 0) + 1);
        }
        return termFrequency;
    }
}

