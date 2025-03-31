package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DocumentManagerTest {
    private final DocumentManager documentManager = new DocumentManager();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonNode rootNode = objectMapper
            .readTree(DocumentManagerTest.class.getResourceAsStream("/testInput.json"));
    private void fillDocumentManager() {
        for(var node: rootNode) {
            var authorId = node.get("author").get("id").asText();
            var authorName = node.get("author").get("name").asText();

            var author = DocumentManager.Author.builder()
                    .id(authorId)
                    .name(authorName).build();

            var docTitle = node.get("document").get("title").asText();
            var docContent = node.get("document").get("content").asText();
            var docID = node.get("document").get("id").asText();
            var docCreated = Instant.parse(node.get("document").get("created").asText());

            var document = DocumentManager.Document.builder()
                    .author(author)
                    .title(docTitle)
                    .content(docContent)
                    .id(docID)
                    .created(docCreated).build();

            documentManager.save(document);
        }
    }

    DocumentManagerTest() throws IOException {
    }

    @Test
    void save() {
        for(var node: rootNode) {
            var authorId = node.get("author").get("id").asText();
            var authorName = node.get("author").get("name").asText();

            var author = DocumentManager.Author.builder()
                    .id(authorId)
                    .name(authorName).build();

            var docTitle = node.get("document").get("title").asText();
            var docContent = node.get("document").get("content").asText();
            var docID = node.get("document").get("id").asText();
            var docCreated = Instant.parse(node.get("document").get("created").asText());

            var document = DocumentManager.Document.builder()
                    .author(author)
                    .title(docTitle)
                    .content(docContent)
                    .id(docID)
                    .created(docCreated).build();

            System.out.println(document);

            assertNotNull(documentManager.save(document));

            assertNotNull(document.getId());

            assertTrue(documentManager.findById(document.getId()).isPresent());
        }

        var author = DocumentManager.Author.builder()
                .id("234")
                .name("Kevin").build();
        var document = DocumentManager.Document.builder()
                .author(author)
                .title("Footbal")
                .content("Personal tips").build();

        System.out.println(documentManager.save(document));

        assertTrue(documentManager.findById(document.getId()).isPresent());
        assertNotNull(document.getId());
    }

    @Test
    void search() {

        fillDocumentManager();

        var searchRequest = DocumentManager.SearchRequest.builder()
                .titlePrefixes(List.of("Java", "Go"))
                .build();

        var docs = documentManager.search(searchRequest);

        assertEquals(2, docs.size());

        for (var doc : docs) {
            System.out.println(doc);
        }
    }

    @Test
    void findById() {
        fillDocumentManager();

        var author = DocumentManager.Author.builder()
                .id("234")
                .name("Bohdan").build();

        var document = DocumentManager.Document.builder()
                .author(author)
                .title("C# intro")
                .content("Tutor").build();

        documentManager.save(document);

        String docID = "b4e1fc55-3a72-43eb-81fd-85aecf0ca113";


        assertTrue(documentManager.findById("b4e1fc55-3a72-43eb-81fd-85aecf0ca113").isPresent());

        System.out.println(documentManager.findById(docID).orElse(null));
    }
}