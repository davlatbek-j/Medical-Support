package med.support.mapper;

import med.support.entity.ReceptionAddress;
import med.support.model.ReceptionAddressDTO;
import org.springframework.stereotype.Component;

@Component
public class ReceptionAddressMapper {

    public ReceptionAddress toEntity(ReceptionAddressDTO dto) {
        ReceptionAddress receptionAddress = new ReceptionAddress();
        receptionAddress.setAddressName(dto.getAddressName());
        receptionAddress.setHttpUrl(dto.getHttpUrl());
        return receptionAddress;
    }

    public ReceptionAddressDTO toDto(ReceptionAddress receptionAddress) {
        ReceptionAddressDTO receptionAddressDTO = new ReceptionAddressDTO();
        receptionAddressDTO.setAddressName(receptionAddress.getAddressName());
        receptionAddressDTO.setHttpUrl(receptionAddress.getHttpUrl());
        return receptionAddressDTO;
    }

}
