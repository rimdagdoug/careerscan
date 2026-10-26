
package com.careerscan.careerscan.service;

import com.careerscan.careerscan.ats.AtsDetector;
import com.careerscan.careerscan.ats.AtsType;
import com.careerscan.careerscan.ats.JobSource;
import com.careerscan.careerscan.ats.greenhouse.GreenhouseJobSource;
import com.careerscan.careerscan.dto.ScanRequest;
import com.careerscan.careerscan.dto.ScanResponse;
import com.careerscan.careerscan.model.Job;
import com.careerscan.careerscan.model.RawJob;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CareerScanService {

    private final AtsDetector atsDetector;
    private final GreenhouseJobSource greenhouseJobSource;
    private final JobNormalizationService jobNormalizationService;

    public CareerScanService(
            AtsDetector atsDetector,
            GreenhouseJobSource greenhouseJobSource,
            JobNormalizationService jobNormalizationService
    ) {
        this.atsDetector = atsDetector;
        this.greenhouseJobSource = greenhouseJobSource;
        this.jobNormalizationService = jobNormalizationService;
    }

    public ScanResponse scan(ScanRequest request) {
        String url = request.url();

        AtsType atsType = atsDetector.detect(url);

        if (atsType != AtsType.GREENHOUSE) {
            return new ScanResponse(atsType.name(), List.of(), 0);
        }

        JobSource jobSource = greenhouseJobSource;

        List<RawJob> rawJobs = jobSource.fetchJobs(url);

        List<Job> jobs = rawJobs.stream()
                .map(jobNormalizationService::normalize)
                .toList();

        return new ScanResponse(
                atsType.name(),
                List.copyOf(jobs),
                jobs.size()
        );
    }
}
