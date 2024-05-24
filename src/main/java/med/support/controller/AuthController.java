package med.support.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import med.support.model.DoctorDTO;
import med.support.model.SignIn;
import med.support.model.SignInResponse;
import med.support.security.JwtTokenService;
import med.support.service.AuthService;
import med.support.service.DoctorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class AuthController {

    private final AuthService authService;
    private final DoctorService doctorService;
    private final JwtTokenService tokenService;


    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("signIn", new SignIn());
        return "login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request ,HttpServletResponse response ,
                                                @ModelAttribute SignIn signIn) throws UnsupportedEncodingException {

        SignInResponse signInResponse = authService.login(signIn);
        if (signInResponse == null) {
            return "login";
        }
        String token = "Bearer " + signInResponse.getToken();

        Cookie tokenCookie = new Cookie("Authorization",URLEncoder.encode( token, "UTF-8" ));

        tokenCookie.setMaxAge(Integer.MAX_VALUE);
        response.addCookie(tokenCookie);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model , HttpServletRequest request ,HttpServletResponse response ) {

        if (request.getCookies() != null) {
            Cookie[] rc = request.getCookies();
            for (Cookie cookie : rc)
                if (cookie.getName().equals("Authorization"))
                    response.addCookie(cookie);
        }


        List<DoctorDTO> allDto = doctorService.getAllDto();
        allDto.sort((o1, o2) -> o1.getId()>o2.getId() ? 1 : -1);
        model.addAttribute("doctors", allDto);
        return "admin/dashboard";
    }

/*    @PostMapping("/admin/dashboard")
    public String getDoctorTable(HttpServletRequest request, HttpServletResponse response , Model model) {

        List<DoctorDTO> allDto = doctorService.getAllDto();
        model.addAttribute("doctors", allDto);

        return "admin/dashboard";
    }*/
}
