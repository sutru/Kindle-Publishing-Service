package com.amazon.ata.kindlepublishingservice.publishing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.Queue;

public class BookPublishRequestManagerTest {
    Queue<BookPublishRequest> queue = new LinkedList<>();
    BookPublishRequestManager requestManager = new BookPublishRequestManager(queue);

    @BeforeEach
    public void setup() {
    }
    // figure out how to mock BookPublishRequest

//
//    @Test
//    public void addBookPublishRequest_anyValue_valueIsAdded() {
//        requestManager.addBookPublishRequest(null);
//        assertNull(queue.remove());
//    }
//
//    @Test
//    public void removeBookPublishRequest_anyValue_valueIsAdded() {
//        queue.add(null);
//        requestManager.getBookPublishRequestToProcess();
//        assertTrue(queue.isEmpty());
//    }
}
