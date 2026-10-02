package com.careerscan.careerscan.ats.greenhouse;

import com.careerscan.careerscan.ats.greenhouse.dto.GreenhouseApiJob;
import com.careerscan.careerscan.ats.greenhouse.dto.GreenhouseApiResponse;
import com.careerscan.careerscan.ats.greenhouse.dto.GreenhouseLocation;
import com.careerscan.careerscan.model.RawJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class GreenhouseJobSourceTest {

    private RestClient restClient;
    private RestClient.Builder builder;
    private GreenhouseJobSource greenhouseJobSource;

    @BeforeEach
    void setUp() {
        builder = mock(RestClient.Builder.class);
        restClient = mock(RestClient.class, Answers.RETURNS_DEEP_STUBS);

        when(builder.build()).thenReturn(restClient);

        greenhouseJobSource = new GreenhouseJobSource(builder);
    }

    @Test
    void shouldFetchAndMapGreenhouseJobs() {

        GreenhouseLocation location =
                new GreenhouseLocation("Lyon, France");

        GreenhouseApiJob job = new GreenhouseApiJob(
                123L,
                "Développeuse Java",
                location,
                "https://example.com/jobs/123",
                "2026-09-30T10:00:00Z",
                "Example Company",
                "<p>Développement Java Spring Boot</p>"
        );

        GreenhouseApiResponse response =
                new GreenhouseApiResponse(List.of(job));

        when(restClient.get()
                .uri(
                        anyString(),
                        anyString()
                )
                .retrieve()
                .body(GreenhouseApiResponse.class))
                .thenReturn(response);

        List<RawJob> jobs =
                greenhouseJobSource.fetchJobs(
                        "https://boards.greenhouse.io/example"
                );

        assertEquals(1, jobs.size());

        RawJob rawJob = jobs.get(0);

        assertEquals("Développeuse Java", rawJob.title());
        assertEquals("Example Company", rawJob.company());
        assertEquals("Lyon, France", rawJob.location());
        assertNull(rawJob.contractType());
        assertEquals(
                "<p>Développement Java Spring Boot</p>",
                rawJob.description()
        );
        assertEquals(
                "https://example.com/jobs/123",
                rawJob.url()
        );
        assertEquals(
                "2026-09-30T10:00:00Z",
                rawJob.publishedAt()
        );
    }

    @Test
    void shouldReturnEmptyListWhenResponseIsNull() {

        when(restClient.get()
                .uri(
                        anyString(),
                        anyString()
                )
                .retrieve()
                .body(GreenhouseApiResponse.class))
                .thenReturn(null);

        List<RawJob> jobs =
                greenhouseJobSource.fetchJobs(
                        "https://boards.greenhouse.io/example"
                );

        assertTrue(jobs.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenJobsAreNull() {

        GreenhouseApiResponse response =
                new GreenhouseApiResponse(null);

        when(restClient.get()
                .uri(
                        anyString(),
                        anyString()
                )
                .retrieve()
                .body(GreenhouseApiResponse.class))
                .thenReturn(response);

        List<RawJob> jobs =
                greenhouseJobSource.fetchJobs(
                        "https://boards.greenhouse.io/example"
                );

        assertTrue(jobs.isEmpty());
    }

    @Test
    void shouldRejectInvalidGreenhouseUrl() {

        assertThrows(
                IllegalArgumentException.class,
                () -> greenhouseJobSource.fetchJobs(
                        "https://boards.greenhouse.io/"
                )
        );
    }
}