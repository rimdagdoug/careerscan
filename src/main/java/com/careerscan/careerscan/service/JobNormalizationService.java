
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
        // 1. Priorité au type fourni par la source.
        ContractType explicitType = normalizeExplicitContractType(contractType);

        if (explicitType != ContractType.OTHER) {
            return explicitType;
        }

        // 2. Analyser le titre en priorité.
        ContractType titleType = detectContractType(title);

        if (titleType != ContractType.OTHER) {
            return titleType;
        }

        // 3. Analyser la description uniquement si nécessaire.
        return detectContractType(description);
    }

    private ContractType detectContractType(String text) {
        if (text == null || text.isBlank()) {
            return ContractType.OTHER;
        }

        String normalized = text.toLowerCase(Locale.ROOT);

        if (normalized.contains("apprenticeship")
                || normalized.contains("apprentice")
                || normalized.contains("alternance")
                || normalized.contains("apprenti")) {
            return ContractType.APPRENTICESHIP;
        }

        if (normalized.contains("internship")
                || normalized.contains("intern")
                || normalized.contains("stage")) {
            return ContractType.INTERNSHIP;
        }

        if (normalized.contains("fixed-term")
                || normalized.contains("fixed term")
                || normalized.contains("cdd")) {
            return ContractType.CDD;
        }

        if (normalized.contains("full-time")
                || normalized.contains("full time")
                || normalized.contains("permanent")
                || normalized.contains("cdi")) {
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
