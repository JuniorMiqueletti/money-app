package com.juniormiqueletti.moneyapp.repository.listener;

import com.juniormiqueletti.moneyapp.MoneyApiApplication;
import com.juniormiqueletti.moneyapp.model.Release;
import com.juniormiqueletti.moneyapp.storage.AmazonS3Storage;

import javax.persistence.PostLoad;

import static org.springframework.util.StringUtils.hasText;

public class ReleaseAttachmentListener {

    @PostLoad
    public void postLoad(final Release release) {
        if (hasText(release.getAttachment())) {
            AmazonS3Storage s3Storage = MoneyApiApplication.getBean(AmazonS3Storage.class);
            release.setAttachedmentUrl(s3Storage.configUrl(release.getAttachment()));
        }
    }
}
