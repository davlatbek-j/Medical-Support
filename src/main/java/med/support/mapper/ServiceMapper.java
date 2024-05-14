package med.support.mapper;

import med.support.entity.Service;
import med.support.model.ServiceDTO;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {

    public Service getEntity(ServiceDTO dto) {
        Service service = new Service();
        service.setName(dto.getName());
        service.setPrice(dto.getPrice());
        return service;
    }
    public ServiceDTO getDto(Service entity) {
        ServiceDTO dto = new ServiceDTO();
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        return dto;
    }
}
