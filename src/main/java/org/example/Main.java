package org.example;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        DocumentManager docManager = new DocumentManager();

        DocumentManager.Author anton = new DocumentManager.Author("12345"
        , "23456");
        DocumentManager.Author volodia = new DocumentManager.Author("12wd"
                , "23456");

        var doc1 = DocumentManager.Document.builder()
                .title("Java")
                .content("Java")
                .author(anton).build();
        docManager.save(doc1);
        Thread.sleep(5000);

        var doc2 = DocumentManager.Document.builder()
                .title("Java")
                .content("Spring")
                .author(anton).build();
        docManager.save(doc2);
        Thread.sleep(5000);

        var doc3 = DocumentManager.Document.builder()
                .id(doc1.getId())
                .title("Python")
                .content("Django")
                .author(volodia).build();
        docManager.save(doc3);

        var requestDoc = DocumentManager.SearchRequest.builder()
                .createdTo(Instant.now())
                .createdFrom(Instant.now().minus(8,ChronoUnit.SECONDS))
                .build();

        for(var doc: docManager.search(requestDoc)) {
            System.out.println(doc);
        }

    }
}