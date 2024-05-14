package med.support.repository;

import med.support.entity.Education;
import med.support.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepo extends JpaRepository<Service, Long> {

}
