package com.duc.files.fileshandler.repo;

import com.duc.files.fileshandler.config.FileMvcConfig;
import com.duc.files.fileshandler.model.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableConfigurationProperties({FileMvcConfig.class})
@Rollback(value =false)
class DocumentRepositoryTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private DocumentRepository repo;

    @Test
    public void createDocument() throws IOException {
        var file = new File("C:\\Users\\DELL\\Downloads\\users_2021-08-12_11-08-56.xlsx");

        var document = new Document();

        document. setName(file.getName());
        byte[] bytes = Files.readAllBytes(file.toPath());

        document.setContent(bytes);
        document.setSize(bytes.length);
        document.setUploadTime(LocalDate.now());

        var save = repo.save(document);
        var document1 = entityManager.find(Document.class, save.getId());

        assertThat(document1.getSize()).isEqualTo(bytes.length);

    }

    @Test
    public void getTest() {
        var documentByName = repo.findDocumentByName("curl.txt");
        assertThat(documentByName).isNotNull();
        System.out.println("Size: " + documentByName.getSize());
        System.out.println("Id: " + documentByName.getId());
    }

}