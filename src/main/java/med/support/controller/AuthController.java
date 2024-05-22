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
import org.springframework.http.ResponseEntity;
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
    @ResponseBody
    public ResponseEntity<SignInResponse> login(HttpServletRequest request ,HttpServletResponse response ,
                                                @ModelAttribute SignIn signIn) throws UnsupportedEncodingException {
        SignInResponse signInResponse = authService.login(signIn);
        if (signInResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(signInResponse);
    }

    @GetMapping("/admin/dashboard")
    public String dashboard() {
        System.err.println("GET");
        return "admin/dashboard";
    }

    @PostMapping("/admin/dashboard")
    public String getDoctorTable(HttpServletRequest request, HttpServletResponse response , Model model) {
//        System.err.println("POST  method call...");
//
//        String token = request.getHeader("Authorization");
//        System.err.println("token from req.header in /admin/dashboard.POST :"+token);
//
//        response.setHeader("Authorization", "Bearer " + token);
//
//        if (token == null) {
//            model.addAttribute("signIn", new SignIn());
//            return "login";
//        }
//        token = token.replace("Bearer ", "");
//        if (!tokenService.validateToken(token)) {
//            model.addAttribute("signIn", new SignIn());
//            return "login"; // или другая страница ошибки
//        }

        // Здесь добавьте логику для получения данных докторов и добавления их в модель
        List<DoctorDTO> allDto = doctorService.getAllDto();
        model.addAttribute("doctors", allDto);

        return "admin/dashboard"; // возвращаем только фрагмент с таблицей
    }
}
