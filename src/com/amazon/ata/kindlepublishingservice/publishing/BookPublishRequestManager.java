package com.amazon.ata.kindlepublishingservice.publishing;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Queue;

@Singleton
public class BookPublishRequestManager {
    private final Queue<BookPublishRequest> bookPublishRequests;

    @Inject
    public BookPublishRequestManager(Queue<BookPublishRequest> bookPublishRequests) {
        this.bookPublishRequests = bookPublishRequests;
    }


    public void addBookPublishRequest(BookPublishRequest request) {
        if (request == null) throw new NullPointerException("bookPublishRequest is null");
        bookPublishRequests.add(request);
    }

    public BookPublishRequest getBookPublishRequestToProcess() {
        if (bookPublishRequests.isEmpty() || bookPublishRequests == null) {
            return null;
        }
        return bookPublishRequests.remove();
    }
}
