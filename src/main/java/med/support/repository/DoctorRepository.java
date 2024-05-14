package med.support.repository;

import med.support.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findById(Long id);

    void deleteById(Long id);

    Doctor findByLogin(String login);


    Boolean existsById(long id);

    Boolean existsByLogin(String login);

    void deleteByLogin(String login);
}
