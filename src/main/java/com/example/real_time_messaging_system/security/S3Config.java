package com.example.real_time_messaging_system.security;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Slf4j
@Configuration
public class S3Config {

       @Value("${aws.access-key}")
       private String accessKey;
       @Value("${aws.secret-key}")
       private String secretKey;
       @Value("${aws.region}")
       private String region;

       @PostConstruct
       public void postConstruct() {
              log.info("accessKey={}", accessKey);
              log.info("secretKey={}", secretKey);
              log.info("region={}", region);
       }


       @Bean
       public S3Client s3Client() {
           AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
           return S3Client.builder()
                   .region(Region.of(region))
                   .credentialsProvider(StaticCredentialsProvider.create(credentials))
                   .build();
       }
}
