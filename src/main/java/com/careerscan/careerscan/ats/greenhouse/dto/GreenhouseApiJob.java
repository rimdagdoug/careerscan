package com.careerscan.careerscan.ats.greenhouse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GreenhouseApiJob(
        Long id,

        String title,

        GreenhouseLocation location,

        @JsonProperty("absolute_url")
        String absoluteUrl,

        @JsonProperty("updated_at")
        String updatedAt,

        @JsonProperty("company_name")
        String companyName,

        String content
) { }