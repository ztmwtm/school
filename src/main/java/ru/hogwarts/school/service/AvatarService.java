package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {

    @Value("${students.avatar.dir.path}")
    private String avatarsDir;
    private final AvatarRepository avatarRepository;
    private final StudentService studentService;

    public AvatarService(AvatarRepository avatarRepository, StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }




    public void upload(Long id, MultipartFile multipartFile) throws IOException {
        Student student = studentService.findStudent(id);

        if (Objects.isNull(student)) {
            return;
        }

        Path filePath = Path.of(avatarsDir, id + "." + getExtension(Objects.requireNonNull(multipartFile.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(multipartFile.getInputStream(), 1024);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(Files.newOutputStream(filePath, CREATE_NEW), 1024)) {
            bufferedInputStream.transferTo(bufferedOutputStream);
        }

        Avatar avatar = avatarRepository.findAvatarById(id).orElse(new Avatar());
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(multipartFile.getSize());
        avatar.setMediaType(multipartFile.getContentType());
        avatar.setPreview(generateImagePreview(filePath));
        avatarRepository.save(avatar);
    }

    private byte[] generateImagePreview(Path filePath) throws IOException {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(Files.newInputStream(filePath), 1024);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);
            int height = bufferedImage.getHeight() / (bufferedImage.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, bufferedImage.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(bufferedImage, 0, 0, 100, height, null);
            graphics.dispose();

            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    public Avatar findAvatarById(Long id) {
       return avatarRepository.findAvatarById(id).orElse(null);
    }
}
