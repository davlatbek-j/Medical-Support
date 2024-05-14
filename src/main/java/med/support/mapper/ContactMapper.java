package med.support.mapper;

import med.support.entity.Contact;
import med.support.model.ContactDTO;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {

    public Contact toEntity(ContactDTO dto) {
        Contact contact = new Contact();
        contact.setContactType(dto.getContactType());
        contact.setValue(dto.getValue());
        return contact;
    }

    public ContactDTO toDto(Contact entity) {
        ContactDTO dto = new ContactDTO();
        dto.setContactType(entity.getContactType());
        dto.setValue(entity.getValue());
        return dto;
    }
}
