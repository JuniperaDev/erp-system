package io.github.erp.docmgmt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "erp.documents")
public class DocumentProperties {

    private String documentsDirectory = "./documents";

    public String getDocumentsDirectory() {
        return documentsDirectory;
    }

    public void setDocumentsDirectory(String documentsDirectory) {
        this.documentsDirectory = documentsDirectory;
    }
}
