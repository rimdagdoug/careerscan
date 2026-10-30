
package com.careerscan.careerscan.service;

import com.careerscan.careerscan.ats.AtsDetector;
import com.careerscan.careerscan.ats.AtsType;
import com.careerscan.careerscan.ats.greenhouse.GreenhouseJobSource;
import com.careerscan.careerscan.dto.ScanRequest;
import com.careerscan.careerscan.dto.ScanResponse;
import com.careerscan.careerscan.model.RawJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CareerScanServiceTest {

    private AtsDetector atsDetector;
    private GreenhouseJobSource greenhouseJobSource;
    private JobNormalizationService normalizationService;
    private CareerScanService careerScanService;

    @BeforeEach
    void setUp() {
        atsDetector = mock(AtsDetector.class);
        greenhouseJobSource = mock(GreenhouseJobSource.class);
        normalizationService = new JobNormalizationService();

        careerScanService = new CareerScanService(
                atsDetector,
                greenhouseJobSource,
                normalizationService
        );
    }

    @Test
    void shouldReturnNormalizedJobsForGreenhouseUrl() {
        String url = "https://job-boards.eu.greenhouse.io/proton";

        RawJob rawJob = new RawJob(
                "Java Developer",
                "Proton",
                "Paris",
                "full-time",
                "Java development",
                "https://example.com/job/1",
                "2026-10-20"
        );

        when(atsDetector.detect(url)).thenReturn(AtsType.GREENHOUSE);
        when(greenhouseJobSource.fetchJobs(url)).thenReturn(List.of(rawJob));

        ScanResponse response = careerScanService.scan(new ScanRequest(url));

        assertEquals("GREENHOUSE", response.ats());
        assertEquals(1, response.total());
        assertEquals(1, response.jobs().size());

        verify(greenhouseJobSource).fetchJobs(url);
    }

    @Test
    void shouldReturnEmptyListForUnsupportedAts() {
        String url = "https://example.com/jobs";

        when(atsDetector.detect(url)).thenReturn(AtsType.UNKNOWN);

        ScanResponse response = careerScanService.scan(new ScanRequest(url));

        assertEquals("UNKNOWN", response.ats());
        assertEquals(0, response.total());
        assertTrue(response.jobs().isEmpty());

        verifyNoInteractions(greenhouseJobSource);
    }

    @Test
    void shouldReturnZeroWhenNoJobsAreFound() {
        String url = "https://job-boards.eu.greenhouse.io/proton";

        when(atsDetector.detect(url)).thenReturn(AtsType.GREENHOUSE);
        when(greenhouseJobSource.fetchJobs(url)).thenReturn(List.of());

        ScanResponse response = careerScanService.scan(new ScanRequest(url));

        assertEquals("GREENHOUSE", response.ats());
        assertEquals(0, response.total());
        assertTrue(response.jobs().isEmpty());
    }
}
