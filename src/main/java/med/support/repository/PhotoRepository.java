package med.support.repository;

import med.support.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Optional<Photo> findByHttpUrl(String httpUrl);

}
