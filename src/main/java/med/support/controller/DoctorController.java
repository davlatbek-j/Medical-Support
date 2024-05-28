package med.support.controller;

import lombok.RequiredArgsConstructor;
import med.support.enums.UserState;
import med.support.model.ApiResponse;
import med.support.model.DoctorDTO;
import med.support.model.LoginDTO;
import med.support.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/api/doctor")
public class DoctorController {
    private final DoctorService doctorService;



    @PostMapping("/create")
    public ResponseEntity<ApiResponse> addDoctor(@RequestParam(name = "json") DoctorDTO stringDTO,
                                                 @RequestParam(name = "photo") MultipartFile photo) {
        return doctorService.save(stringDTO);
    }

    @PostMapping("/create-login")
    public ResponseEntity<ApiResponse> addDoctorLogin(@RequestBody LoginDTO loginDTO) {
        return doctorService.createLogin(loginDTO);
    }


    @PostMapping("/setPhoto/{login}")
    public ResponseEntity<ApiResponse> setPhoto( @PathVariable(name = "login") String login,
                                                 @RequestParam(name = "photo") MultipartFile photo)                                                  {
        return doctorService.setDoctorPhoto(login,photo);
    }


    @GetMapping("about/{login}")
    public ResponseEntity<ApiResponse> getDoctor(@PathVariable(name = "login") String login) {
        return doctorService.findByLogin(login);
    }

    @PutMapping("/update/{login}")
    public ResponseEntity<ApiResponse> updateDoctor(
            @PathVariable(name = "login") String login,
            @RequestBody DoctorDTO doctorDTO){
        return doctorService.update(login,doctorDTO);
    }

    @DeleteMapping("/delete/{login}")
    public ResponseEntity<ApiResponse> deleteDoctor(@PathVariable(name = "login") String login) {
        return doctorService.deleteByLogin(login);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse> getAllDoctors() {
        List<DoctorDTO> allDto = doctorService.getAllDto();
        return ResponseEntity.ok().body(new ApiResponse(200,"All-list",allDto));
    }

}
