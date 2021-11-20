package ru.pnapreenko.blogengine.services;

import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ru.pnapreenko.blogengine.api.interfaces.StorageService;
import ru.pnapreenko.blogengine.api.responses.APIResponse;
import ru.pnapreenko.blogengine.api.utils.ConfigStrings;
import ru.pnapreenko.blogengine.config.ImageStorageProperties;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.security.Principal;
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
        final Pattern FILE_PATTERN = Pattern.compile("^(.*)(.)(png|jpe?g)$");
        final Path randomSubPath = Paths.get(getRandomPath());
        final Path fullUploadPath = this.rootLocation.resolve(randomSubPath);
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        Path fullFilePath = fullUploadPath.resolve(filename);

        try {
            if (file.isEmpty()) {
                throw new StorageException(ConfigStrings.IMAGE_EMPTY_NOT_SAVE + filename);
            }
            if (file.getSize() > maxFileSize) {
                throw new StorageException(ConfigStrings.IMAGE_EXCEEDS_ALLOWED_SIZE);
            }
            if (filename.contains("..")) {
                throw new StorageException(ConfigStrings.IMAGE_NOT_SAVE_WITH_EXTERNAL_PATH + filename);
            }
            if (!FILE_PATTERN.matcher(filename).matches()) {
                throw new StorageException(ConfigStrings.IMAGE_WITH_INVALID_TYPE + filename);
            }
            try (InputStream inputStream = getResizeFile(file).getInputStream()) {
                Files.createDirectories(fullUploadPath);
                Files.copy(inputStream, fullFilePath,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            throw new StorageException("Не удалось сохранить файл: " + filename, e);
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

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public ResponseEntity<?> uploadImage(MultipartFile file, Principal principal) {
        if (file == null) {
            return ResponseEntity.badRequest().body(APIResponse.error());
        }
        String pathToSavedFile = store(file);
        UriComponents uri = UriComponentsBuilder.newInstance()
                .path("/{root}/{file_uri}")
                .buildAndExpand(getRootLocation(), pathToSavedFile);
        return ResponseEntity.ok(uri.toUriString());
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

    public MultipartFile getResizeFile(MultipartFile file) {
        MultipartFile multipartFile;
        try {
            BufferedInputStream in = new BufferedInputStream(file.getInputStream());
            String fileEnd = FilenameUtils.getExtension(file.getOriginalFilename());
            BufferedImage image = ImageIO.read(in);
            BufferedImage newImage = Scalr.resize(
                    image,
                    Scalr.Method.QUALITY,
                    Scalr.Mode.AUTOMATIC,
                    36, 36);
            File newFile = new File(this.rootLocation + "/" + file.getName());
            assert fileEnd != null;
            ImageIO.write(newImage, fileEnd, newFile);
            multipartFile = new MockMultipartFile(file.getName(),
                    newFile.getName(), file.getContentType(), IOUtils.toByteArray(new FileInputStream(newFile.getAbsolutePath())));
            return multipartFile;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

