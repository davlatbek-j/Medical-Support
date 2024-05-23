package med.support.service;

import lombok.RequiredArgsConstructor;
import med.support.entity.User;
import med.support.model.SignIn;
import med.support.model.SignInResponse;
import med.support.repository.UserRepository;
import med.support.security.JwtTokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService tokenService;

    public SignInResponse login(SignIn signIn) {
        User byLogin = userService.findByLogin(signIn.getLogin());
        if (byLogin != null) {
            if (passwordEncoder.matches(signIn.getPassword(), byLogin.getPassword())) {
                return new SignInResponse(
                        tokenService.generateToken(byLogin.getLogin()),
                        byLogin.getRole().getRole().name(),
                        byLogin.getLogin()
                );
            }
        }
        return null;
    }

}
