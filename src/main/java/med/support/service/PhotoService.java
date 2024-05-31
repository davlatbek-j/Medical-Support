package med.support.service;

import lombok.RequiredArgsConstructor;
import med.support.entity.Photo;
import med.support.repository.PhotoRepository;
import med.support.repository.ServiceRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
//            System.err.println("Invalid content type: " + contentType);
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

    public Photo savePhotoFromTelegram(String filePath, String doctorLogin)  {
            File file = new File(filePath);
            try {
                // Fayl turi va original nomini aniqlash
                String contentType = Files.probeContentType(file.toPath());
                String originalName = file.getName();

                // Content type tekshiruvi
                if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
//                    System.err.println("Invalid content type: " + contentType);
                    return null;
                }

                // Photo ob'ektini yaratish
                Photo photo = new Photo(
                         originalName,
                        filePath,
                        "http://localhost:8080/doctor/image/" + doctorLogin,
                        contentType);

                // Foto ma'lumotlarini bazaga saqlash
                return photoRepository.save(photo);
            } catch (IOException e) {
                throw new RuntimeException("Error processing file", e);
            }

    }

    public Photo findByUrl(String httpUrl){
        return photoRepository.findByHttpUrl(httpUrl).orElse(null);
    }

    public Photo findById(Long photoId) {
        return photoRepository.findById(photoId).orElse(null);
    }
}
