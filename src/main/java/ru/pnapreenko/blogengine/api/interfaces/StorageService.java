package ru.pnapreenko.blogengine.api.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface StorageService {
    void init();
    String store(MultipartFile file);
    Path load(String filename);
    boolean delete(String filename) throws IOException;
    void deleteAll();
    Path getRootLocation();
}
