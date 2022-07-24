package com.amazon.ata.kindlepublishingservice.converters;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatusRecord;

import java.util.ArrayList;
import java.util.List;

public class PublishingStatusConverter {
    private PublishingStatusConverter(){}

    public static PublishingStatusRecord toPublishingRecordStatus(PublishingStatusItem publishingStatusItem) {
//        return PublishingStatusRecord.Builder()
//                .withStatus(publishingStatusItem.getStatus())
//                .withBookId(publishingStatusItem.getBookId())
//                .withStatusMessage(publishingStatusItem.getStatusMessage())
//                .build();
        return new PublishingStatusRecord(publishingStatusItem.getStatus().toString(),
                publishingStatusItem.getStatusMessage(),
                publishingStatusItem.getBookId());
    }

    public static List<PublishingStatusRecord> toPublishingRecordStatusList(
            List<PublishingStatusItem> publishingStatusItemList) {
        List<PublishingStatusRecord> publishingRecordStatuses = new ArrayList<>();
        for(PublishingStatusItem publishingStatusItem : publishingStatusItemList) {
            publishingRecordStatuses.add(toPublishingRecordStatus(publishingStatusItem));
        }
        return publishingRecordStatuses;
    }
}
