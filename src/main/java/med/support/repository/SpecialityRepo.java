package med.support.repository;

import med.support.entity.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpecialityRepo extends JpaRepository<Speciality, Long> {
    Boolean existsByName(String name);

    @Query(value = "select id from Speciality where name=?1",nativeQuery = true)
    Long findIdByName(String name);
}
