package com.juniormiqueletti.moneyapp.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.juniormiqueletti.moneyapp.config.property.MoneyApiProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static java.util.Collections.emptyList;

@Component
public class AmazonS3Storage {

    private static Logger logger = LoggerFactory.getLogger(AmazonS3Storage.class);
    private static final String EXPIRE = "EXPIRE";

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private MoneyApiProperty property;

    public String saveTemporaty(final MultipartFile file) {
        AccessControlList accessControlList = new AccessControlList();
        accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        String uniqueName = generateUniqueName(file.getName());

        try {
            PutObjectRequest putObjectRequest =
                new PutObjectRequest(
                    property.getS3().getBucket(),
                    uniqueName,
                    file.getInputStream(),
                    objectMetadata
                )
                .withAccessControlList(accessControlList);

            putObjectRequest.setTagging(new ObjectTagging(
                Arrays.asList(new Tag(EXPIRE, "true"))
            ));

            amazonS3.putObject(putObjectRequest);

            if (logger.isDebugEnabled())
                logger.debug("File send successful to S3");

            return uniqueName;
        } catch (IOException e) {
            throw new RuntimeException("Problem during file send to S3", e);
        }
    }

    public String configUrl(final String s3Object) {
        return "\\\\" + property.getS3().getBucket() + "s3.amazon.aws" + s3Object;

    }

    public void save(final String attachment) {
        SetObjectTaggingRequest setObjectTaggingRequest =
            new SetObjectTaggingRequest(
                property.getS3().getBucket(),
                attachment,
                new ObjectTagging(emptyList())
            );

        amazonS3.setObjectTagging(setObjectTaggingRequest);
    }

    public void remove(final String attachment) {
        DeleteObjectRequest deleteObjectRequest =
            new DeleteObjectRequest(
                property.getS3().getBucket(),
                attachment
            );

        amazonS3.deleteObject(deleteObjectRequest);
    }

    public void replace(final String oldAttachment, final String newAttachment) {
        if (StringUtils.hasText(oldAttachment))
            this.remove(oldAttachment);

        this.save(newAttachment);
    }

    private String generateUniqueName(final String filename) {
        return UUID.randomUUID().toString() + "_" + filename;
    }
}
