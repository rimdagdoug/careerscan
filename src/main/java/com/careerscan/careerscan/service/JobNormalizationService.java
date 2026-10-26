
package com.careerscan.careerscan.service;

import com.careerscan.careerscan.model.ContractType;
import com.careerscan.careerscan.model.Job;
import com.careerscan.careerscan.model.RawJob;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class JobNormalizationService {

    public Job normalize(RawJob rawJob) {
        ContractType contractType = normalizeContractType(
                rawJob.contractType(),
                rawJob.title(),
                rawJob.description()
        );

        return new Job(
                rawJob.title(),
                rawJob.company(),
                rawJob.location(),
                contractType,
                rawJob.description(),
                rawJob.url(),
                rawJob.publishedAt(),
                "GREENHOUSE"
        );
    }

    private ContractType normalizeContractType(
            String contractType,
            String title,
            String description
    ) {
        // 1. Priorité au type de contrat fourni par la source.
        ContractType normalizedType = normalizeExplicitContractType(contractType);

        if (normalizedType != ContractType.OTHER) {
            return normalizedType;
        }

        // 2. Si le type est absent, rechercher des indices dans le titre
        // et la description.
        String text = ((title == null ? "" : title) + " "
                + (description == null ? "" : description))
                .toLowerCase(Locale.ROOT);

        if (text.contains("apprenticeship")
                || text.contains("apprentice")
                || text.contains("alternance")
                || text.contains("apprenti")) {
            return ContractType.APPRENTICESHIP;
        }

        if (text.contains("internship")
                || text.contains("intern")
                || text.contains("stage")) {
            return ContractType.INTERNSHIP;
        }

        if (text.contains("fixed-term")
                || text.contains("fixed term")
                || text.contains("cdd")) {
            return ContractType.CDD;
        }

        if (text.contains("full-time")
                || text.contains("full time")
                || text.contains("permanent")
                || text.contains("cdi")) {
            return ContractType.CDI;
        }

        return ContractType.OTHER;
    }

    private ContractType normalizeExplicitContractType(String contractType) {
        if (contractType == null || contractType.isBlank()) {
            return ContractType.OTHER;
        }

        return switch (contractType.trim().toLowerCase(Locale.ROOT)) {
            case "full-time", "full time", "permanent", "cdi" ->
                    ContractType.CDI;
            case "fixed-term", "fixed term", "cdd" ->
                    ContractType.CDD;
            case "internship", "intern", "stage" ->
                    ContractType.INTERNSHIP;
            case "apprenticeship", "apprentice", "alternance" ->
                    ContractType.APPRENTICESHIP;
            default -> ContractType.OTHER;
        };
    }
}
