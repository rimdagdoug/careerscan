package com.careerscan.careerscan.controller;

import com.careerscan.careerscan.dto.ScanRequest;
import com.careerscan.careerscan.dto.ScanResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CareerScanController {

    @PostMapping("/scan")
    public ScanResponse scan(@RequestBody ScanRequest request){
        return new ScanResponse("UNKNOWN", List.of(),0);
    }
}
