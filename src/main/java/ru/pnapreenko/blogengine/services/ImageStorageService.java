package ru.pnapreenko.blogengine.services;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.pnapreenko.blogengine.api.interfaces.StorageService;
import ru.pnapreenko.blogengine.config.ImageStorageProperties;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

@Service
public class ImageStorageService implements StorageService {

    @Getter
    private final Path rootLocation;

    public ImageStorageService(ImageStorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
        init();
    }

    @Override
    public void init() {
        try {
            if (Files.notExists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public String store(MultipartFile file) {

        final long maxFileSize = 5 * 1_024 * 1_024;
        final Pattern FILE_PATTERN = Pattern.compile("^(.*)(.)(png|jpe?g)$");
        Path fullFilePath;
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file: " + filename);
            }

            if(file.getSize() > maxFileSize) {
                throw new StorageException("The file exceeds the allowed size.");
            }

            if (filename.contains("..")) {
                throw new StorageException(
                        "Cannot store file with relative path outside current directory: "
                                + filename);
            }

            if (!FILE_PATTERN.matcher(filename).matches()) {
                throw new StorageException("Can store PNG & JPE?G images only: " + filename);
            }

            try (InputStream inputStream = file.getInputStream()) {
                final Path randomSubPath = Paths.get(getRandomPath());
                final Path fullUploadPath = this.rootLocation.resolve(randomSubPath);
                fullFilePath = fullUploadPath.resolve(filename);

                Files.createDirectories(fullUploadPath);

                Files.copy(inputStream, fullFilePath,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file: " + filename, e);
        }

        return this.rootLocation.relativize(fullFilePath).toString()
                .replace('\\', '/');
    }

    @Override
    public Path load(String filename) {
        Path file = Paths.get(filename.startsWith("/") ? "/" : "")
                .resolve(rootLocation)
                .relativize(Paths.get(filename));
        return rootLocation.resolve(file);
    }

    @Override
    public boolean delete(String filename) {
        boolean result;

        try {
            result = Files.deleteIfExists(load(filename));
        } catch (NoSuchFileException e) {
            throw new StorageException("No such file exists: " + filename, e);
        } catch (IOException e) {
            throw new StorageException("Invalid permissions for file: " + filename, e);
        }

        return result;
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public static class StorageException extends RuntimeException {

        public StorageException(String message) {
            super(message);
        }

        public StorageException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class StorageFileNotFoundException extends StorageException {

        public StorageFileNotFoundException(String message) {
            super(message);
        }

        public StorageFileNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private static String getRandomPath() {
        StringBuilder sb = new StringBuilder();

        for (int iteration = 0; iteration < 3; iteration++) {
            for (int ch = 0; ch < 2; ch++) {
                sb.append((char) (new Random().nextInt('z' - 'a') + 'a'));
            }
            sb.append("/");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }
}

