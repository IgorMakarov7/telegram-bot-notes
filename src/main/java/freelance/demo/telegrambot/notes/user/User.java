package freelance.demo.telegrambot.notes.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class User {
    @Id
    private Long chatId;
}
