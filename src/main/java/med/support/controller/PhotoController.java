package med.support.controller;

import lombok.RequiredArgsConstructor;
import med.support.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/doctor/image")
public class PhotoController {

    private final DoctorService doctorService;

    @GetMapping("/{login}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable(name = "login") String login) {
        return doctorService.getPhoto(login);
    }

}
