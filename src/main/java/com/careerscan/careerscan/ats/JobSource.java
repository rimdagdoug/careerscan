package com.careerscan.careerscan.ats;

import com.careerscan.careerscan.model.RawJob;

import java.util.List;

public interface JobSource {
    List<RawJob> fetchJobs(String url);
}
