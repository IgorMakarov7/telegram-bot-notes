package freelance.demo.telegrambot.notes.bot;

import freelance.demo.telegrambot.notes.handlers.CallbackQueryService;
import freelance.demo.telegrambot.notes.handlers.CommandService;
import freelance.demo.telegrambot.notes.handlers.MessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final CommandService commandService;
    private final MessageService messageService;
    private final CallbackQueryService callbackQueryService;

    public TelegramBot(
            BotConfig botConfig,
            BotService botService,
            CommandService commandService,
            MessageService messageService,
            CallbackQueryService callbackQueryService) {
        super(botConfig.getToken());

        this.botConfig = botConfig;
        this.commandService = commandService;
        this.messageService = messageService;
        this.callbackQueryService = callbackQueryService;

        try {
            execute(botService.getSetMyCommands());
        } catch (TelegramApiException ignored) {
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = null;
        EditMessageText editMessageText = null;
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();
            if (messageText.startsWith("/")) {
                sendMessage = commandService.handleCommand(chatId, messageText);
            } else {
                sendMessage = messageService.handleMessage(chatId, messageText);
            }
        } else if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            String callbackData = update.getCallbackQuery().getData();

            editMessageText = callbackQueryService.handleCallbackData(chatId, messageId, callbackData);
        }

        executeSendMessage(sendMessage);
        executeEditMessageText(editMessageText);
    }

    private void executeSendMessage(SendMessage sendMessage) {
        try {
            if (sendMessage != null) {
                execute(sendMessage);
            }
        } catch (Exception ignored) {
        }
    }

    private void executeEditMessageText(EditMessageText editMessageText) {
        try {
            if (editMessageText != null) {
                execute(editMessageText);
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getToken();
    }
}
