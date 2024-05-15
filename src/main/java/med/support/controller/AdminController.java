package med.support.controller;

import lombok.RequiredArgsConstructor;
import med.support.entity.Doctor;
import med.support.mapper.DoctorMapper;
import med.support.model.DoctorDTO;
import med.support.model.LoginDTO;
import med.support.repository.DoctorRepository;
import med.support.service.DoctorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DoctorService doctorService;
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    @GetMapping({"","/"})
    public String showDoctorList(Model model){
        List<DoctorDTO> dtoList = doctorService.getAllDto();
        model.addAttribute("doctors",dtoList);
        return "admin/dashboard";
    }

    @GetMapping("/create")
    public String showCreateLogin(Model model){
        model.addAttribute("loginDto", new LoginDTO());
        return "createLogin";
    }

    @PostMapping("/create")
    public String saveLogin(@ModelAttribute("loginDto") LoginDTO loginDTO){
        doctorService.createLogin(loginDTO);
        return "admin/dashboard";
    }


//TODO DELETE


    @GetMapping("/delete/{login}")
    public String deleteDoctorGetMethod(@PathVariable(name = "login") String login) {
        doctorService.deleteByLogin(login);
        return "redirect:/admin/";
    }

//TODO EDIT

    @GetMapping("/edit/{login}")
    public String editDoctorPage(@PathVariable(name = "login") String login , Model model) {

        Doctor doctor = doctorRepository.findByLogin(login);
        model.addAttribute("doctor", doctor);
        DoctorDTO doctorDto = doctorMapper.toDTO(doctor);
        model.addAttribute("doctorDto", doctorDto);
        return "admin/editDoctor";

    }

    @PostMapping("/edit/{login}")
    public String editDoctor(@PathVariable(name = "login") String login , Model model ,
                             @ModelAttribute DoctorDTO doctorDto) {

        System.err.println(doctorDto);

        return "redirect:/admin/";
    }

}
