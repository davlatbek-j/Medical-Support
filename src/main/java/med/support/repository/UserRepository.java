package med.support.repository;

import med.support.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByLogin(String login);

    Boolean existsByLogin(String login);

}
