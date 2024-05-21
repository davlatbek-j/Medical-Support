package med.support.controller;

import lombok.RequiredArgsConstructor;
import med.support.entity.User;
import med.support.model.SignIn;
import med.support.model.SignInResponse;
import med.support.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
//@RequestMapping("")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("signIn", new SignIn());
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<SignInResponse> login(@ModelAttribute SignIn signIn, Model model) {
        System.err.println(signIn);
        SignInResponse signInResponse = authService.login(signIn);
        if (signInResponse == null) {
            model.addAttribute("signIn", new SignIn());
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(signInResponse);
    }

/*    @PostMapping("/login")
    public String login(@ModelAttribute SignIn signIn, Model model){
        SignInResponse login = authService.login(signIn);

        if (login == null) {
            return "notFound";
        }
        model.addAttribute("signInResponse", login);
        return "admin/success";
    }*/

}
