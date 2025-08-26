package io.github.erp.docmgmt.internal;

import io.github.erp.docmgmt.config.DocumentProperties;
import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.files.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service("documentFSStorageService")
public class DocumentFSStorageService implements FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(DocumentFSStorageService.class);

    private final Path fileStorageLocation;

    public DocumentFSStorageService(DocumentProperties documentProperties) {
        this.fileStorageLocation = Paths.get(documentProperties.getDocumentsDirectory()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            log.error("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(fileStorageLocation);
        } catch (IOException e) {
            log.error("Could not initialize storage", e);
        }
    }

    @Override
    public void save(MultipartFile file, String fileMd5CheckSum) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                log.error("Sorry! Filename contains invalid path sequence " + fileName);
                return;
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            log.error("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public void save(byte[] fileContent, String fileName) {
        try {
            if (fileName.contains("..")) {
                log.error("Sorry! Filename contains invalid path sequence " + fileName);
                return;
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.write(targetLocation, fileContent);
        } catch (IOException ex) {
            log.error("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public void save(MultipartFile file) {
        save(file, null);
    }

    @Override
    public Resource load(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                log.error("File not found " + fileName);
                return null;
            }
        } catch (MalformedURLException ex) {
            log.error("File not found " + fileName, ex);
            return null;
        }
    }

    @Override
    public void deleteAll() {
        try {
            Files.walk(this.fileStorageLocation)
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        log.error("Could not delete file: " + path, e);
                    }
                });
        } catch (IOException e) {
            log.error("Could not delete files", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.fileStorageLocation, 1)
                .filter(path -> !path.equals(this.fileStorageLocation))
                .map(this.fileStorageLocation::relativize);
        } catch (IOException e) {
            log.error("Could not load files", e);
            return Stream.empty();
        }
    }

    @Override
    public String calculateMD5CheckSum(String filename) {
        return FileUtils.calculateMD5CheckSum(this.fileStorageLocation, filename);
    }

    @Override
    public String calculateSha512CheckSum(String filename) {
        return FileUtils.calculateSha512CheckSum(this.fileStorageLocation, filename);
    }

    @Override
    public String calculateCheckSum(String fileName, String algorithmName) {
        if ("MD5".equalsIgnoreCase(algorithmName)) {
            return calculateMD5CheckSum(fileName);
        } else if ("SHA-512".equalsIgnoreCase(algorithmName)) {
            return calculateSha512CheckSum(fileName);
        } else {
            log.warn("Unsupported algorithm: " + algorithmName + ", defaulting to SHA-512");
            return calculateSha512CheckSum(fileName);
        }
    }

    @Override
    public boolean fileRemainsUnTampered(String fileName, String originalChecksum) {
        String currentChecksum = calculateSha512CheckSum(fileName);
        return originalChecksum != null && originalChecksum.equals(currentChecksum);
    }
}
