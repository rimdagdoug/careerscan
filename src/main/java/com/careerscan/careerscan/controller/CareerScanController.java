
package com.careerscan.careerscan.controller;

import com.careerscan.careerscan.ats.greenhouse.GreenhouseJobSource;
import com.careerscan.careerscan.dto.ScanRequest;
import com.careerscan.careerscan.dto.ScanResponse;
import com.careerscan.careerscan.model.RawJob;
import com.careerscan.careerscan.service.CareerScanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CareerScanController {

    private final CareerScanService careerScanService;
    private final GreenhouseJobSource greenhouseJobSource;

    public CareerScanController(
            CareerScanService careerScanService,
            GreenhouseJobSource greenhouseJobSource
    ) {
        this.careerScanService = careerScanService;
        this.greenhouseJobSource = greenhouseJobSource;
    }

    @PostMapping("/scan")
    public ScanResponse scan(@RequestBody ScanRequest request) {
        return careerScanService.scan(request);
    }

    @GetMapping("/greenhouse/jobs")
    public List<RawJob> getJobs() {
        String url = "https://job-boards.eu.greenhouse.io/proton";
        return greenhouseJobSource.fetchJobs(url);
    }
}
