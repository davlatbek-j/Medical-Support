package med.support.controller;

import med.support.model.SignIn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
public class HomeController {
    @GetMapping("/")
    public String login(Model model) {
        model.addAttribute("signIn", new SignIn());
        return "login";
    }


}
