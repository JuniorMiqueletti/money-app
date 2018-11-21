package com.juniormiqueletti.moneyapp.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.juniormiqueletti.moneyapp.config.property.MoneyApiProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Config {

    private MoneyApiProperty property;

    @Bean
    public AmazonS3 amazonS3() {

        AWSCredentials credentials = new BasicAWSCredentials(
            property.getS3().getAccessKeyId(),
            property.getS3().getSecretAccessKey()
        );

        return AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .build();
    }
}
