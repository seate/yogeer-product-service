package com.yoger.productserviceorganization.product.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cloud.gcp.gcs")
public record GcpProperties(
        String projectId,
        String bucket,
        String credentialsPath
) {
}
