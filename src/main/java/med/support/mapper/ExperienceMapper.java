package med.support.mapper;

import med.support.entity.Experience;
import med.support.model.ExperienceDTO;
import org.springframework.stereotype.Component;

@Component
public class ExperienceMapper {

    public Experience toEntity(ExperienceDTO dto) {
        Experience experience = new Experience();
        experience.setWorkplace(dto.getWorkplace());
        experience.setBeginDate(dto.getBeginDate());
        experience.setEndDate(dto.getEndDate());
        experience.setPosition(dto.getPosition());
        return experience;
    }
    public ExperienceDTO toDTO(Experience entity) {
        ExperienceDTO experienceDTO = new ExperienceDTO();
        experienceDTO.setWorkplace(entity.getWorkplace());
        experienceDTO.setBeginDate(entity.getBeginDate());
        experienceDTO.setEndDate(entity.getEndDate());
        experienceDTO.setPosition(entity.getPosition());
        return experienceDTO;
    }
}
