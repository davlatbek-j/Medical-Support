package med.support.service;

import lombok.RequiredArgsConstructor;
import med.support.entity.Photo;
import med.support.repository.PhotoRepository;
import med.support.repository.ServiceRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoService {

    @Value("${photoUploadDir}")
    private String photoSystemPath;

    private final PhotoRepository photoRepository;
    private final ServiceRepo serviceRepo;

    public Photo save(MultipartFile photoFile, String doctorLogin) {
        String contentType = photoFile.getContentType();
        if ( contentType!=null && !contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
            System.err.println("Invalid content type: " + contentType);
        }

        try {
            String originalName = photoFile.getOriginalFilename();
            Photo photo = new Photo(
                    UUID.randomUUID()+originalName,
                    // TODO ---- file system path depends Photo's name
                    null,
                    "http://localhost:8080/doctor/image/" + doctorLogin,
                    contentType);

            photo.setSystemPath(photoSystemPath + photo.getName());

            byte[] bytes = photoFile.getBytes();
            Files.write(Paths.get(photo.getSystemPath()), bytes);
            return photoRepository.save(photo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
