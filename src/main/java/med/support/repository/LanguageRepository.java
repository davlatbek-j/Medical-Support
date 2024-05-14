package med.support.repository;

import med.support.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LanguageRepository extends JpaRepository<Language, Long> {

    Boolean existsByName(String name);
    @Query(value = "SELECT id from Language where name=?1",nativeQuery = true)
    Long findIdByName(String name);
}
