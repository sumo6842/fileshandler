package com.duc.files.fileshandler.repo;

import com.duc.files.fileshandler.model.Document;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Long> {
    @Query("SELECT file FROM Document AS file WHERE file.name = ?1")
    public Document findDocumentByName(String name);
}
