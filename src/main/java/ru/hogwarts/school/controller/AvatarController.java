package ru.hogwarts.school.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static ru.hogwarts.school.controller.AvatarController.BASE_PATH;

@RestController
@RequestMapping("/" + BASE_PATH)
public class AvatarController {

    public static final String BASE_PATH="avatar";
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() > 1920 * 1080) {
            return ResponseEntity.badRequest().body("File too big");
        }
        avatarService.upload(id, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/preview")
    public ResponseEntity<byte[]> showAvatarPreview(@PathVariable Long id) {
        Avatar avatar = avatarService.findAvatarById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getPreview().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getPreview());
    }

    @GetMapping(value = "")
    public ResponseEntity<List<Avatar>> showAllAvatars(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                                                       @RequestParam(value = "size", required = false, defaultValue = "1") int pageSize) {
        return ResponseEntity.ok(avatarService.findAllAvatars(pageNumber, pageSize));
    }

    @GetMapping(value = "/{id}")
    public void showAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.findAvatarById(id);

        Path path = Path.of(avatar.getFilePath());

        try (InputStream inputStream = Files.newInputStream(path);
             OutputStream outputStream = response.getOutputStream()) {
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            inputStream.transferTo(outputStream);
        }
    }
}
