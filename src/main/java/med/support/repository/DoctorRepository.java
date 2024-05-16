package med.support.repository;

import jakarta.transaction.Transactional;
import med.support.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findById(Long id);


   // Optional<Doctor> findByChatId(String chatId);
    Doctor findByChatId(String chatId);




    void deleteById(Long id);

    Doctor findByLogin(String login);


    Boolean existsById(long id);

    Boolean existsByLogin(String login);

    void deleteByLogin(String login);

    @Transactional
    @Modifying
    @Query("update doctor d set d.chatId=:chatId where d.login=:login")
    void saveChatId(@Param("login") String login, @Param("chatId") String chatId);

    boolean existsByChatId(String chatId);

}
