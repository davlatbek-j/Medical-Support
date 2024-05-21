package med.support.aa;

import lombok.RequiredArgsConstructor;
import med.support.entity.Role;
import med.support.entity.User;
import med.support.entity.enums.RoleName;
import med.support.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User user = new User(
                "admin-bot",
                null,
                true,
                new Role(RoleName.ROLE_ADMIN)
        );
        user.setPassword(passwordEncoder.encode("123"));
//        System.err.println("_______________________________________________");
//        System.err.println(user);
//        userRepository.save(user);
    }
}
