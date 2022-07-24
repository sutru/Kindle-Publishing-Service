package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;

import javax.inject.Inject;

public class BookPublishTask implements Runnable{
    BookPublishRequestManager bookPublishRequestManager;
    PublishingStatusDao publishingStatusDao;
    CatalogDao catalogDao;

    @Inject
    public BookPublishTask(BookPublishRequestManager bookPublishRequestManager, PublishingStatusDao publishingStatusDao, CatalogDao catalogDao) {
        this.bookPublishRequestManager = bookPublishRequestManager;
        this.publishingStatusDao = publishingStatusDao;
        this.catalogDao = catalogDao;
    }

    @Override
    public void run() {
        BookPublishRequest bookPublishRequest = bookPublishRequestManager.getBookPublishRequestToProcess();
        if (bookPublishRequest == null) {
            return;
        }
        publishingStatusDao.setPublishingStatus(bookPublishRequest.getPublishingRecordId(),
                PublishingRecordStatus.IN_PROGRESS, bookPublishRequest.getBookId());
        KindleFormattedBook formattedBook = KindleFormatConverter.format(bookPublishRequest);
        try {
            CatalogItemVersion civ = catalogDao.createOrUpdateBook(formattedBook);
            publishingStatusDao.setPublishingStatus(bookPublishRequest.getPublishingRecordId(),
                    PublishingRecordStatus.SUCCESSFUL, civ.getBookId());
        } catch (Exception e) {
            publishingStatusDao.setPublishingStatus(bookPublishRequest.getPublishingRecordId(),
                    PublishingRecordStatus.FAILED, bookPublishRequest.getBookId(), e.getMessage());
        }
    }
}
