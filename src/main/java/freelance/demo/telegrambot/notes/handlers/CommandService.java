package freelance.demo.telegrambot.notes.handlers;

import freelance.demo.telegrambot.notes.bot.BotService;
import freelance.demo.telegrambot.notes.bot.BotState;
import freelance.demo.telegrambot.notes.user.UserService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class CommandService {

    private final UserService userService;
    private final BotService botService;

    public CommandService(
            UserService userService,
            BotService botService) {
        this.userService = userService;
        this.botService = botService;
    }

    public SendMessage handleCommand(Long chatId, String command) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        switch (command) {
            case "/start" -> startCommand(sendMessage, chatId);
            case "/notes" -> notesCommand(sendMessage);
            case "/help" -> helpCommand(sendMessage);
            case "/cancel" -> cancelCommand(sendMessage, chatId);
            default -> unknownCommand(sendMessage);
        }

        return sendMessage;
    }

    private void startCommand(SendMessage sendMessage, Long chatId) {
        userService.saveUser(chatId);
        sendMessage.setText("""
                Привет! Я заметки-бот. Помогу тебе с твоими заметками.
                
                Вот 2 команды, которую рекомендую попробовать:
                 - Для заметок:
                        /notes
                 - Помощь:
                        /help
                 
                Составляй заметки с удобством в мессенджере!
                """);
    }

    private void notesCommand(SendMessage sendMessage) {
        sendMessage.setText("Выберите вариант действий:");
        sendMessage.setReplyMarkup(botService.getMarkupNotes());
    }

    private void helpCommand(SendMessage sendMessage) {
        sendMessage.setText("""
                Кто я такой?
                 - Бот для заметок.
                
                Что может бот?
                 Бот может выполнять следующие операции с заметками:
                 - создавать;
                 - читать;
                 - изменять;
                 - удалять.
                
                Как пользоваться заметками?
                 - Для взаимодействия с заметками
                        /notes
                """);
    }

    private void cancelCommand(SendMessage sendMessage, Long chatId) {
        BotState.removeState(chatId);
        sendMessage.setText("Операция прервана");
    }

    private void unknownCommand(SendMessage sendMessage) {
        sendMessage.setText("такой команды не существует");
    }
}
