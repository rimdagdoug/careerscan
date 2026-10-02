package com.careerscan.careerscan.ats.greenhouse.dto;

import java.util.List;

public record GreenhouseApiResponse(
        List<GreenhouseApiJob> jobs
) { }
