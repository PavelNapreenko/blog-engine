package ru.pnapreenko.blogengine.services;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.pnapreenko.blogengine.api.interfaces.StorageService;
import ru.pnapreenko.blogengine.api.utils.ImageResizer;
import ru.pnapreenko.blogengine.config.ConfigStrings;
import ru.pnapreenko.blogengine.config.ImageStorageProperties;

import java.io.File;
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
            throw new StorageException("Не удалось инициализировать хранилище.", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        final long maxFileSize = 5 * 1_024 * 1_024;
        final Path randomSubPath = Paths.get(getRandomPath());
        final Path fullUploadPath = this.rootLocation.resolve(randomSubPath);
        final String fileType = Objects.requireNonNull(file.getContentType());
        final String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        final Path fullFilePath = fullUploadPath.resolve(filename);
        final Pattern FILE_PATTERN = Pattern.compile("^(.*)(.)(png|jpe?g|PNG|JPE?G)$");

        if (file.isEmpty()) {
            throw new StorageException(ConfigStrings.IMAGE_EMPTY_NOT_SAVE + filename);
        }
        if (file.getSize() > maxFileSize) {
            throw new StorageException(ConfigStrings.IMAGE_EXCEEDS_ALLOWED_SIZE.getName());
        }
        if (filename.contains("..")) {
            throw new StorageException(ConfigStrings.IMAGE_NOT_SAVE_WITH_EXTERNAL_PATH + filename);
        }
        if (!FILE_PATTERN.matcher(fileType).matches()) {
            throw new StorageException(ConfigStrings.IMAGE_WITH_INVALID_TYPE + filename);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.createDirectories(fullUploadPath);
            Files.copy(inputStream, fullFilePath,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
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
            throw new StorageFileNotFoundException("Такого файла не существует: " + filename, e);
        } catch (IOException e) {
            throw new StorageException("Недопустимые разрешения для файла: " + filename, e);
        }
        deleteFolder(rootLocation.toString());
        return result;
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
        public StorageFileNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public String uploadImage(MultipartFile file, int width, int height) {
        MultipartFile currentSizeFile = ImageResizer.getResizeFile(file, width, height);
        String pathToSavedFile = store(currentSizeFile);
        UriComponents uri = UriComponentsBuilder.newInstance()
                .path("/{root}/{file_uri}")
                .buildAndExpand(getRootLocation(), pathToSavedFile);
        return uri.toUriString();
    }

    private static long deleteFolder(String dir) {
        File f = new File(dir);
        String[] listFiles = f.list();
        long totalSize = 0;
        assert listFiles != null;
        for (String file : listFiles) {
            File folder = new File(dir + "/" + file);
            if (folder.isDirectory()) {
                totalSize += deleteFolder(folder.getAbsolutePath());
            } else {
                totalSize += folder.length();
            }
        }
        if (totalSize == 0) {
            f.delete();
        }
        return totalSize;
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

