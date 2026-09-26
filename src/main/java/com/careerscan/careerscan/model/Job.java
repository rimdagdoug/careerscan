package com.careerscan.careerscan.model;

public record Job  (String title,
        String company,
        String location,
        ContractType contractType,
        String description,
        String url,
        String publishedAt,
        String source) {
}
