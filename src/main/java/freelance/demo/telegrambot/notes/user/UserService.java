package freelance.demo.telegrambot.notes.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(Long chatId) {
        User user = new User();
        user.setChatId(chatId);
        userRepository.save(user);
    }
}
