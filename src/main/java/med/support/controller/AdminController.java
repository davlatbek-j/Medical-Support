package med.support.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import med.support.entity.Doctor;
import med.support.mapper.DoctorMapper;
import med.support.model.ApiResponse;
import med.support.model.DoctorDtoAdmin;
import med.support.model.LoginDTO;
import med.support.repository.DoctorRepository;
import med.support.service.AdminService;
import med.support.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DoctorService doctorService;
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final AdminService adminService;


    @GetMapping("/create")
    public String showCreateLogin(Model model , HttpServletRequest request ,HttpServletResponse response ) {
        if (request.getCookies() != null) {
            Cookie[] rc = request.getCookies();
            for (Cookie cookie : rc)
                if (cookie.getName().equals("Authorization"))
                    response.addCookie(cookie);
        }

        model.addAttribute("loginDto", new LoginDTO());
        return "admin/createLogin";
    }

    @PostMapping("/create")
    public String saveLogin(@ModelAttribute("loginDto") LoginDTO loginDTO,HttpServletRequest request ,HttpServletResponse response , Model model ) {

        if (request.getCookies() != null) {
            Cookie[] rc = request.getCookies();
            for (Cookie cookie : rc)
                if (cookie.getName().equals("Authorization"))
                    response.addCookie(cookie);
        }

        ResponseEntity<ApiResponse> login = doctorService.createLogin(loginDTO);
        if (Objects.requireNonNull(login.getBody()).getCode()==400) {
            model.addAttribute("msg", "Login already exist!!!");
            return "/admin/error";
        }
        return "redirect:/admin/dashboard";
    }


//TODO DELETE


    @GetMapping("/delete/{login}")
    public String deleteDoctorGetMethod(@PathVariable(name = "login") String login , HttpServletRequest request ,HttpServletResponse response ) {

        if (request.getCookies() != null) {
            Cookie[] rc = request.getCookies();
            for (Cookie cookie : rc)
                if (cookie.getName().equals("Authorization"))
                    response.addCookie(cookie);
        }

        doctorService.deleteByLogin(login);
        return "redirect:/admin/dashboard";
    }

//TODO EDIT

    @GetMapping("/edit/{login}")
    public String editDoctorPage(@PathVariable(name = "login") String login, Model model , HttpServletRequest request ,HttpServletResponse response ) {

        if (request.getCookies() != null) {
            Cookie[] rc = request.getCookies();
            for (Cookie cookie : rc)
                if (cookie.getName().equals("Authorization"))
                    response.addCookie(cookie);
        }


        Doctor doctor = doctorRepository.findByLogin(login);

        DoctorDtoAdmin doctorDtoAdmin = new DoctorDtoAdmin();
        doctorDtoAdmin = adminService.toDoctorDtoAdmin(doctor);

        model.addAttribute("doctorDtoAdmin", doctorDtoAdmin);

        return "admin/editDoctor";
    }

    @PostMapping("/edit/{login}")
    public String editDoctor(@PathVariable(name = "login") String login, Model model,
                             @ModelAttribute DoctorDtoAdmin doctorDtoAdmin,HttpServletRequest request ,HttpServletResponse response ) {
        if (request.getCookies() != null) {
            Cookie[] rc = request.getCookies();
            for (Cookie cookie : rc)
                if (cookie.getName().equals("Authorization"))
                    response.addCookie(cookie);
        }

        adminService.update(login, doctorDtoAdmin);

        return "redirect:/admin/dashboard";
    }

    @GetMapping("/about/{login}")
    public String aboutDoctor(@PathVariable(name = "login") String login, Model model , HttpServletRequest request ,HttpServletResponse response ) {

        if (request.getCookies() != null) {
            Cookie[] rc = request.getCookies();
            for (Cookie cookie : rc)
                if (cookie.getName().equals("Authorization"))
                    response.addCookie(cookie);
        }


        Doctor byLogin = doctorRepository.findByLogin(login);
        model.addAttribute("doctorDto", doctorMapper.toDTO(byLogin));
        return "admin/about";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("Authorization")) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        return "redirect:/login";
    }

}
