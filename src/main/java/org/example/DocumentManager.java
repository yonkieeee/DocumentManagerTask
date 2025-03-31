package org.example;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


/**
 * For implement this task focus on clear code, and make this solution as simple readable as possible
 * Don't worry about performance, concurrency, etc
 * You can use in Memory collection for sore data
 * <p>
 * Please, don't change class name, and signature for methods save, search, findById
 * Implementations should be in a single class
 * This class could be auto tested
 */
public class DocumentManager {
    private final List<Document> documentsStorage = new ArrayList<>();

    /**
     * Implementation of this method should upsert the document to your storage
     * And generate unique id if it does not exist, don't change [created] field
     *
     * @param document - document content and author data
     * @return saved document
     */
    public Document save(Document document) {

        if (document == null) throw new IllegalArgumentException("Document is null");

        if (document.author.getId() == null) throw new IllegalArgumentException("Author is null");

        if (document.getId() == null){
            String id = UUID.randomUUID().toString();

            document.setId(id);
            document.setCreated(Instant.now());
        } else{
            Document existDoc = findById(document.getId()).orElse(null);

            if (existDoc != null) {
                document.setCreated(existDoc.getCreated());
                documentsStorage.remove(existDoc);
            }
        }

        documentsStorage.add(document);
        return document;
    }
    /**
     * Implementation this method should find documentsStorage which match with request
     *
     * @param request - search request, each field could be null
     * @return list matched documentsStorage
     */
    public List<Document> search(SearchRequest request) {
        return documentsStorage.stream()
                .filter(doc -> request.getTitlePrefixes() == null || //filter by title
                        request.getTitlePrefixes().stream().anyMatch(prefix -> doc.getTitle().toLowerCase().contains(prefix.toLowerCase())))
                .filter(doc -> request.getContainsContents() == null || //filter by content
                        request.getContainsContents().stream().anyMatch(content -> doc.getContent().toLowerCase().contains(content.toLowerCase())))
                .filter(doc -> request.getAuthorIds() == null || //filter by authorID
                        request.getAuthorIds().stream().anyMatch(authorId -> doc.getAuthor().getId().equals(authorId)))
                .filter(doc -> request.getCreatedFrom() == null || //filter by time
                        doc.getCreated().isAfter(request.getCreatedFrom()) || doc.getCreated().equals(request.getCreatedFrom()))
                .filter(doc -> request.getCreatedFrom() == null || //filer by time
                        doc.getCreated().isBefore(request.getCreatedTo()) || doc.getCreated().equals(request.getCreatedTo()))
                .collect(Collectors.toList());
    }

    /**
     * Implementation this method should find document by id
     *
     * @param id - document id
     * @return optional document
     */
    public Optional<Document> findById(String id) {
        if (id == null) throw new IllegalArgumentException("Document id is null");

        return documentsStorage.stream().
                filter(doc -> doc.getId().equals(id))
                .findFirst();
    }

    @Data
    @Builder
    public static class SearchRequest {
        private List<String> titlePrefixes;
        private List<String> containsContents;
        private List<String> authorIds;
        private Instant createdFrom;
        private Instant createdTo;
    }

    @Data
    @Builder
    public static class Document {
        private String id;
        private String title;
        private String content;
        private Author author;
        private Instant created;
    }

    @Data
    @Builder
    public static class Author {
        private String id;
        private String name;
    }
}