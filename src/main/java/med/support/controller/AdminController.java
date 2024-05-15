package med.support.controller;

import lombok.RequiredArgsConstructor;
import med.support.model.ApiResponse;
import med.support.model.DoctorDTO;
import med.support.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DoctorService doctorService;

    @GetMapping({"","/"})
    public String showDoctorList(Model model){
        List<DoctorDTO> dtoList = doctorService.getAllDto();
        model.addAttribute("doctors",dtoList);
        return "admin/dashboard";
    }

    @GetMapping("/create")
    public String showCreateDoctorForm(Model model){
        model.addAttribute("doctorDto", new DoctorDTO());
        return "admin/createDoctor";
    }

    @PostMapping("/create")
    public String saveDoctor(@ModelAttribute("doctorDto") DoctorDTO doctorDto){

        System.err.println("doctorDto " + doctorDto);

        return "admin/dashboard";
    }



    @GetMapping("/delete/{login}")
    public String deleteDoctorGetMethod(@PathVariable(name = "login") String login) {
        doctorService.deleteByLogin(login);
        return "redirect:/admin/";
    }


    @GetMapping("/edit/{login}")
    public String editDoctor(@PathVariable(name = "login") String login , Model model) {
        DoctorDTO doctor = (DoctorDTO) doctorService.findByLogin(login).getBody().getData();
        model.addAttribute("doctor", doctor);
        return "admin/editDoctor";
    }

}
