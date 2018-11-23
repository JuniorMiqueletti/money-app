package com.juniormiqueletti.moneyapp.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.Tag;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;
import com.amazonaws.services.s3.model.lifecycle.LifecycleTagPredicate;
import com.juniormiqueletti.moneyapp.config.property.MoneyApiProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.amazonaws.services.s3.model.BucketLifecycleConfiguration.ENABLED;

@Configuration
public class AmazonS3Config {

    private static final int EXPIRATION_DAYS = 1;
    private static final String BUCKET_ID = "Temporary files automatic expiration rule";
    private static final String EXPIRE = "EXPIRE";
    private static final String TRUE = "true";

    private MoneyApiProperty property;

    @Bean
    public AmazonS3 amazonS3() {

        MoneyApiProperty.S3 propertyS3 = property.getS3();

        AWSCredentials credentials = new BasicAWSCredentials(
            propertyS3.getAccessKeyId(),
            propertyS3.getSecretAccessKey()
        );

        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.US_EAST_2)
            .build();

        String bucketName = propertyS3.getBucket();

        if (!amazonS3.doesBucketExistV2(bucketName))
            createBucketWithExpirationRule(amazonS3, bucketName);

        return amazonS3;
    }

    private void createBucketWithExpirationRule(final AmazonS3 amazonS3, final String bucketName) {
        amazonS3.createBucket(new CreateBucketRequest(bucketName));

        BucketLifecycleConfiguration configuration = new BucketLifecycleConfiguration()
            .withRules(getExpirationRule());

        amazonS3.setBucketLifecycleConfiguration(bucketName, configuration);
    }

    private BucketLifecycleConfiguration.Rule getExpirationRule() {

        return new BucketLifecycleConfiguration.Rule()
            .withId(BUCKET_ID)
            .withFilter(
                new LifecycleFilter(
                    new LifecycleTagPredicate(new Tag(EXPIRE, TRUE))
                )
            )
            .withExpirationInDays(EXPIRATION_DAYS)
            .withStatus(ENABLED);
    }
}
