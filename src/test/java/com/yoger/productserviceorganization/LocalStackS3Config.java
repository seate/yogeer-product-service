package com.yoger.productserviceorganization;

import com.yoger.productserviceorganization.product.config.AwsProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@TestConfiguration
public class LocalStackS3Config {
    @Bean(initMethod = "start", destroyMethod = "stop")
    public LocalStackContainer localStackContainer(AwsProperties awsProperties) {
        return new LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
                .withServices(LocalStackContainer.Service.S3)
                .withEnv("DEFAULT_REGION", awsProperties.region());
    }

    @Bean
    public S3Client s3TestClient(LocalStackContainer container) {
        return S3Client.builder()
                .endpointOverride(container.getEndpointOverride(LocalStackContainer.Service.S3))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(
                        container.getAccessKey(), container.getSecretKey())))
                .region(Region.of(container.getRegion()))
                .build();
    }
}
