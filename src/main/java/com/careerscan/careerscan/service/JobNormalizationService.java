package com.careerscan.careerscan.service;

import com.careerscan.careerscan.model.ContractType;
import com.careerscan.careerscan.model.Job;
import com.careerscan.careerscan.model.RawJob;

import java.util.Locale;

public class JobNormalizationService {
    public Job normalize(RawJob rawJob){
        ContractType contractType = normalizeContractType(rawJob.contractType());
        return new Job(
                rawJob.title(),
                rawJob.company(),
                rawJob.location(),
                contractType,
                rawJob.description(),
                rawJob.url(),
                rawJob.publishedAt(),
                "UNKNOWN"
        );
    }
    private ContractType normalizeContractType(String contractType){
        if(contractType == null || contractType.isBlank()){
            return ContractType.OTHER;
        }
        return switch (contractType.trim().toLowerCase(Locale.ROOT)){
            case "full-time", "full time", "permanent","cdi" ->
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
