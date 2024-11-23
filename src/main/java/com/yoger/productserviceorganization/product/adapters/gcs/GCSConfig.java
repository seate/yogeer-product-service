package com.yoger.productserviceorganization.product.adapters.gcs;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.yoger.productserviceorganization.product.config.GcpProperties;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

@Configuration
@Profile({"!integration", "gcp"})  // Exclude from integration tests
public class GCSConfig {
    private final GcpProperties gcpProperties;

    public GCSConfig(GcpProperties gcpProperties) {
        this.gcpProperties = gcpProperties;
    }

    @Bean
    public Storage storage() throws IOException {
        GoogleCredentials credentials;
        if (gcpProperties.credentialsPath() != null && !gcpProperties.credentialsPath().isEmpty()) {
            ClassPathResource resource = new ClassPathResource(gcpProperties.credentialsPath());
            InputStream credentialsStream = resource.getInputStream();
            credentials = GoogleCredentials.fromStream(credentialsStream);
        } else {
            credentials = GoogleCredentials.getApplicationDefault();
        }

        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId(gcpProperties.projectId())
                .build()
                .getService();
    }
}
