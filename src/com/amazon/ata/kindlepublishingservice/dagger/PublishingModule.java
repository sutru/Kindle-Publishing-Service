package com.amazon.ata.kindlepublishingservice.dagger;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.publishing.*;

import dagger.Module;
import dagger.Provides;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Singleton;

@Module
public class PublishingModule {

    @Provides
    @Singleton
    public BookPublisher provideBookPublisher(ScheduledExecutorService scheduledExecutorService,
                                              BookPublishRequestManager bookPublishRequestManager,
                                              PublishingStatusDao publishingStatusDao,
                                              CatalogDao catalogDao) {
        return new BookPublisher(scheduledExecutorService, new BookPublishTask(bookPublishRequestManager,
                publishingStatusDao, catalogDao));
    }

    @Provides
    @Singleton
    public Queue<BookPublishRequest> provideQueue() {return new ConcurrentLinkedQueue<>();}

    @Provides
    @Singleton
    public ScheduledExecutorService provideBookPublisherScheduler() {
        return Executors.newScheduledThreadPool(1);
    }
}
