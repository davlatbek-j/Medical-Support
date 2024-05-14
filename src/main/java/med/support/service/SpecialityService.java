package med.support.service;

import lombok.RequiredArgsConstructor;
import med.support.entity.Speciality;
import med.support.repository.SpecialityRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecialityService {
    private final SpecialityRepo specialityRepo;

    public Speciality save(Speciality speciality) {
        Boolean exists = specialityRepo.existsByName(speciality.getName());
        if (exists) {
           Long id = specialityRepo.findIdByName(speciality.getName());
           speciality.setId(id);
        }
       return specialityRepo.save(speciality);
    }
}
