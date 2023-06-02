package freelance.demo.telegrambot.notes.bot;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class BotService {
    public SetMyCommands getSetMyCommands() {
        List<BotCommand> botCommands = new ArrayList<>();

        botCommands.add(new BotCommand("/notes", "открыть функционал записок"));
        botCommands.add(new BotCommand("/help", "как использовать бота"));
        botCommands.add(new BotCommand("/cancel", "прекратить выполнение текущей операции"));

        return new SetMyCommands(
                botCommands, new BotCommandScopeDefault(), null
        );
    }

    public InlineKeyboardMarkup getMarkupNotes() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();

        InlineKeyboardButton createNoteButton = new InlineKeyboardButton();
        createNoteButton.setText("создать заметку");
        createNoteButton.setCallbackData("CREATE_NOTE");
        InlineKeyboardButton readNoteButton = new InlineKeyboardButton();
        readNoteButton.setText("посмотреть заметки");
        readNoteButton.setCallbackData("READ_NOTE");
        InlineKeyboardButton updateNoteButton = new InlineKeyboardButton();
        updateNoteButton.setText("изменить заметку");
        updateNoteButton.setCallbackData("UPDATE_NOTE");
        InlineKeyboardButton deleteNoteButton = new InlineKeyboardButton();
        deleteNoteButton.setText("удалить заметку");
        deleteNoteButton.setCallbackData("DELETE_NOTE");

        firstRow.add(createNoteButton);
        firstRow.add(readNoteButton);
        secondRow.add(updateNoteButton);
        secondRow.add(deleteNoteButton);

        keyboard.add(firstRow);
        keyboard.add(secondRow);

        markup.setKeyboard(keyboard);

        return markup;
    }
}
