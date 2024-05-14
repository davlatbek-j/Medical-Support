package med.support.service;

import lombok.RequiredArgsConstructor;
import med.support.entity.Language;
import med.support.entity.Speciality;
import med.support.repository.LanguageRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LanguageService {
    private final LanguageRepository languageRepo;

    public Language save(Language language) {
        Boolean exists = languageRepo.existsByName(language.getName());
        if (exists) {
            Long id = languageRepo.findIdByName(language.getName());
            language.setId(id);
        }
        return languageRepo.save(language);
    }
}
