package com.careerscan.careerscan.model;

public record RawJob(
        String title,
                     String company,
                     String location,
                     String contractType,
                     String description,
                     String url,
                     String publishedAt
) { }
