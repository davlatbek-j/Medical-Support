package med.support.mapper;

import lombok.RequiredArgsConstructor;
import med.support.entity.Education;
import med.support.model.EducationDTO;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EducationMapper {

    public Education toEntity(EducationDTO dto) {
        Education education = new Education();
        education.setName(dto.getName());
        education.setStartYear(dto.getStartYear());
        education.setEndYear(dto.getEndYear());
        education.setFaculty(dto.getFaculty());
        return education;
    }

    public EducationDTO toDto(Education education) {
        EducationDTO educationDTO = new EducationDTO();
        educationDTO.setName(education.getName());
        educationDTO.setStartYear(education.getStartYear());
        educationDTO.setEndYear(education.getEndYear());
        educationDTO.setFaculty(education.getFaculty());
        return educationDTO;
    }
}
