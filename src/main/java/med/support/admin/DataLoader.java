package med.support.admin;

import lombok.RequiredArgsConstructor;
import med.support.entity.Role;
import med.support.entity.User;
import med.support.entity.enums.RoleName;
import med.support.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        User user1 = new User("admin-med-1", passwordEncoder.encode("2+2=5"), new Role(RoleName.ROLE_ADMIN));
        User user2 = new User("admin-med-2", passwordEncoder.encode("2+2=5"), new Role(RoleName.ROLE_ADMIN));
        User user3 = new User("admin-med-3", passwordEncoder.encode("2+2=5"), new Role(RoleName.ROLE_ADMIN));
        User user4 = new User("admin-med-4", passwordEncoder.encode("2+2=5"), new Role(RoleName.ROLE_ADMIN));

        if (!userRepository.existsByLogin("admin-med-1")) {
            userRepository.save(user1);
        }
        if (!userRepository.existsByLogin("admin-med-2")) {
            userRepository.save(user2);
        }
        if (!userRepository.existsByLogin("admin-med-3")) {
            userRepository.save(user3);
        }
        if (!userRepository.existsByLogin("admin-med-4")) {
            userRepository.save(user4);
        }
    }

}
