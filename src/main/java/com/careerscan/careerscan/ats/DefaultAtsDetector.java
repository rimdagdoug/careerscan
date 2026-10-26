
package com.careerscan.careerscan.ats;

import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class DefaultAtsDetector implements AtsDetector {

    @Override
    public AtsType detect(String url) {
        if (url == null || url.isBlank()) {
            return AtsType.UNKNOWN;
        }

        try {
            URI uri = URI.create(url);
            String host = uri.getHost();

            if (host == null) {
                return AtsType.UNKNOWN;
            }

            host = host.toLowerCase();

            if (host.equals("greenhouse.io")
                    || host.endsWith(".greenhouse.io")) {
                return AtsType.GREENHOUSE;
            }

            return AtsType.UNKNOWN;

        } catch (IllegalArgumentException exception) {
            return AtsType.UNKNOWN;
        }
    }
}
