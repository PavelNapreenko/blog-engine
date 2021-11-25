package ru.pnapreenko.blogengine.api.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;

public class ImageResizer {

    private ImageResizer() {
    }

    public static MultipartFile getResizeFile(MultipartFile file, int width, int height) {
        int resizeWidth;
        int resizeHeight;
        MultipartFile multipartFile = null;
        String fileEnd = FilenameUtils.getExtension(file.getOriginalFilename());
        try {
            BufferedInputStream in = new BufferedInputStream(file.getInputStream());
            BufferedImage image = ImageIO.read(in);
            if (image.getWidth() > ConfigStrings.IMAGE_MAX_WIDTH || image.getHeight() > ConfigStrings.IMAGE_MAX_HEIGHT) {
                resizeWidth = width;
                resizeHeight = height;
            } else {
                resizeWidth = image.getWidth();
                resizeHeight = image.getHeight();
            }
            BufferedImage resizeImage = Scalr.resize(image, resizeWidth, resizeHeight);
            File newFile = new File(file.getName());
            ImageIO.write(resizeImage, Objects.requireNonNull(fileEnd), newFile);
            multipartFile = getConversionToMultipartFile(file, newFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return multipartFile;
    }

    private static MultipartFile getConversionToMultipartFile(MultipartFile mFile, File file) throws IOException {
        return new MockMultipartFile(
                mFile.getName(),
                mFile.getOriginalFilename(),
                mFile.getContentType(),
                IOUtils.toByteArray(new FileInputStream(file)));
    }


}
