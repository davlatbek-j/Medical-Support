package med.support.repository;

import med.support.entity.Education;
import med.support.entity.ReceptionAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceptionAddressRepo extends JpaRepository<ReceptionAddress, Long> {
}
