package com.careerscan.careerscan.ats.greenhouse;

import com.careerscan.careerscan.ats.JobSource;
import com.careerscan.careerscan.ats.greenhouse.dto.GreenhouseApiResponse;
import com.careerscan.careerscan.model.RawJob;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;

@Component
public class GreenhouseJobSource implements JobSource {

    private final RestClient restClient;

    public GreenhouseJobSource(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    private String extractBoardToken(String url){
        URI uri = URI.create(url);

        String path = uri.getPath();

        if (path == null || path.isBlank() || path.equals("/")) {
            throw new IllegalArgumentException(
                    "Invalid Greenhouse URL"
            );
        }
        return path.substring(1).split("/")[0];
    };

    @Override
    public List<RawJob> fetchJobs(String url) {

        // 1. Extraire le token Greenhouse
        String boardToken = extractBoardToken(url);

        // 2. Appeler l'API Greenhouse
        GreenhouseApiResponse response = restClient.get()
                .uri(
                        "https://boards-api.greenhouse.io/v1/boards/{boardToken}/jobs?content=true",
                        boardToken
                )
                .retrieve()
                .body(GreenhouseApiResponse.class);

        // 3. Vérifier la réponse
        if (response == null || response.jobs() == null) {
            return List.of();
        }

        // 4. Convertir les offres Greenhouse en RawJob
        return response.jobs().stream()
                .map(job -> new RawJob(
                        job.title(),
                        job.companyName(),
                        job.location() != null
                                ? job.location().name()
                                : null,
                        null,
                        job.content(),
                        job.absoluteUrl(),
                        job.updatedAt()
                ))
                .toList();
    }
}
