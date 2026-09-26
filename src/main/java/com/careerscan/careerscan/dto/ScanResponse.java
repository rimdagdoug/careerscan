package com.careerscan.careerscan.dto;

import java.util.List;

public record ScanResponse(
        String ats,
        List<Object> jobs,
        int total
) { }
