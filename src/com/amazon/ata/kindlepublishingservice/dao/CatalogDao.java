package com.amazon.ata.kindlepublishingservice.dao;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.requests.SubmitBookForPublishingRequest;
import com.amazon.ata.kindlepublishingservice.publishing.KindleFormattedBook;
import com.amazon.ata.kindlepublishingservice.utils.KindlePublishingUtils;

import com.amazon.ata.recommendationsservice.types.BookGenre;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import javax.inject.Inject;

public class CatalogDao {

    private final DynamoDBMapper dynamoDbMapper;

    /**
     * Instantiates a new CatalogDao object.
     *
     * @param dynamoDbMapper The {@link DynamoDBMapper} used to interact with the catalog table.
     */
    @Inject
    public CatalogDao(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    public CatalogItemVersion removeBookFromCatalog(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);
        if (book == null || book.isInactive()) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }
        book.setInactive(true);
        dynamoDbMapper.save(book);
        return book;
    }
    public void saveNewBook(SubmitBookForPublishingRequest request) {
        CatalogItemVersion book = new CatalogItemVersion();
        book.setBookId(request.getBookId());
        book.setAuthor(request.getAuthor());
        book.setGenre(BookGenre.valueOf(request.getGenre()));
        book.setText(request.getText());
        book.setTitle(request.getTitle());
        book.setVersion(1);
        dynamoDbMapper.save(book);
    }

    /**
     * Returns the latest version of the book from the catalog corresponding to the specified book id.
     * Throws a BookNotFoundException if the latest version is not active or no version is found.
     * @param bookId Id associated with the book.
     * @return The corresponding CatalogItem from the catalog table.
     */
    public CatalogItemVersion getBookFromCatalog(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);

        if (book == null || book.isInactive()) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }

        return book;
    }

    // Returns null if no version exists for the provided bookId
    private CatalogItemVersion getLatestVersionOfBook(String bookId) {
        CatalogItemVersion book = new CatalogItemVersion();
        book.setBookId(bookId);

        DynamoDBQueryExpression<CatalogItemVersion> queryExpression = new DynamoDBQueryExpression()
            .withHashKeyValues(book)
            .withScanIndexForward(false)
            .withLimit(1);

        List<CatalogItemVersion> results = dynamoDbMapper.query(CatalogItemVersion.class, queryExpression);
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

    public void validateBook(String bookId) {
        CatalogItemVersion result = getLatestVersionOfBook(bookId);
        if(result == null) throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
    }


    public CatalogItemVersion createOrUpdateBook(KindleFormattedBook book) {
        if (book.getBookId().isEmpty() || book.getBookId() == null) {
               return saveNewKindleBook(book);
        }
            CatalogItemVersion catalogItemVersion = getLatestVersionOfBook(book.getBookId());
        if (catalogItemVersion == null) {
            throw new BookNotFoundException(String.format("No book found for id: %s", catalogItemVersion.getBookId()));
        }
        catalogItemVersion.setInactive(true);
        dynamoDbMapper.save(catalogItemVersion);
        catalogItemVersion.setVersion(catalogItemVersion.getVersion() + 1);
        catalogItemVersion.setInactive(false);
        dynamoDbMapper.save(catalogItemVersion);
        return catalogItemVersion;
    }

    // util method to avoid clutter
    private CatalogItemVersion saveNewKindleBook(KindleFormattedBook formattedBook) {
        CatalogItemVersion book = new CatalogItemVersion();
        book.setBookId(KindlePublishingUtils.generateBookId());
        book.setAuthor(formattedBook.getAuthor());
        book.setGenre(formattedBook.getGenre());
        book.setText(formattedBook.getText());
        book.setTitle(formattedBook.getTitle());
        book.setVersion(1);
        dynamoDbMapper.save(book);
        return book;
    }
}
