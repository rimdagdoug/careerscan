
package com.careerscan.careerscan.service;

import com.careerscan.careerscan.model.ContractType;
import com.careerscan.careerscan.model.RawJob;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JobNormalizationServiceTest {

    private final JobNormalizationService service =
            new JobNormalizationService();

    @Test
    void shouldDetectApprenticeshipFromTitle() {
        RawJob rawJob = new RawJob(
                "Développeuse Java en alternance",
                "Example",
                "Lyon",
                null,
                "Nous recherchons une développeuse.",
                "https://example.com/job",
                null
        );

        assertEquals(
                ContractType.APPRENTICESHIP,
                service.normalize(rawJob).contractType()
        );
    }

    @Test
    void shouldDetectInternshipFromTitle() {
        RawJob rawJob = new RawJob(
                "Java Developer Internship",
                "Example",
                "Lyon",
                null,
                "Join our team.",
                "https://example.com/job",
                null
        );

        assertEquals(
                ContractType.INTERNSHIP,
                service.normalize(rawJob).contractType()
        );
    }

    @Test
    void shouldDetectCdiFromContractType() {
        RawJob rawJob = new RawJob(
                "Java Developer",
                "Example",
                "Lyon",
                "permanent",
                "Develop applications.",
                "https://example.com/job",
                null
        );

        assertEquals(
                ContractType.CDI,
                service.normalize(rawJob).contractType()
        );
    }

    @Test
    void shouldReturnOtherWhenContractTypeIsUnknown() {
        RawJob rawJob = new RawJob(
                "Software Developer",
                "Example",
                "Lyon",
                null,
                "Develop applications.",
                "https://example.com/job",
                null
        );

        assertEquals(
                ContractType.OTHER,
                service.normalize(rawJob).contractType()
        );
    }


    @Test
    void shouldPrioritizeTitleOverDescription() {
        RawJob rawJob = new RawJob(
                "Développeur Java CDI",
                "Example",
                "Lyon",
                null,
                "We are looking for an internship candidate.",
                "https://example.com/job",
                null
        );

        assertEquals(
                ContractType.CDI,
                service.normalize(rawJob).contractType()
        );
    }

    @Test
    void shouldDetectApprenticeshipFromDescriptionWhenTitleIsNeutral() {
        RawJob rawJob = new RawJob(
                "Java Developer",
                "Example",
                "Lyon",
                null,
                "This position is available as an apprenticeship.",
                "https://example.com/job",
                null
        );

        assertEquals(
                ContractType.APPRENTICESHIP,
                service.normalize(rawJob).contractType()
        );
    }

}
