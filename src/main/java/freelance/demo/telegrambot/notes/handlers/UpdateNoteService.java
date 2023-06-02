package freelance.demo.telegrambot.notes.handlers;

import freelance.demo.telegrambot.notes.bot.BotState;
import freelance.demo.telegrambot.notes.bot.State;
import freelance.demo.telegrambot.notes.bot.StateType;
import freelance.demo.telegrambot.notes.note.Note;
import freelance.demo.telegrambot.notes.note.NoteService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class UpdateNoteService {

    private final NoteService noteService;

    public UpdateNoteService(NoteService noteService) {
        this.noteService = noteService;
    }

    public InlineKeyboardMarkup questionsAboutUpdate() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> questionRow = new ArrayList<>();
        List<InlineKeyboardButton> cancelRow = new ArrayList<>();

        InlineKeyboardButton editNameButton = new InlineKeyboardButton();
        editNameButton.setText("название изменить");
        editNameButton.setCallbackData("EDIT_NAME");

        InlineKeyboardButton editTextButton = new InlineKeyboardButton();
        editTextButton.setText("текст изменить");
        editTextButton.setCallbackData("EDIT_TEXT");

        InlineKeyboardButton cancelButton = new InlineKeyboardButton();
        cancelButton.setText("завершить редактирование");
        cancelButton.setCallbackData("EDIT_CANCEL");

        questionRow.add(editNameButton);
        questionRow.add(editTextButton);
        cancelRow.add(cancelButton);

        keyboard.add(questionRow);
        keyboard.add(cancelRow);

        markup.setKeyboard(keyboard);

        return markup;
    }

    public void handleEdit(String callbackData, EditMessageText editMessageText, Long chatId) {

        State state = BotState.getState(chatId);
        Note note = state.getNote();

        switch (callbackData) {
            case "EDIT_NAME" -> {
                editMessageText.setText("""
                        Укажите новое имя для заметки:
                        """);
                state.setStateType(StateType.EDIT_NOTE_NAME);
                BotState.putState(chatId, state);
            }
            case "EDIT_TEXT" -> {
                editMessageText.setText("""
                        Введите новый текст заметки:
                        """);
                state.setStateType(StateType.EDIT_NOTE_TEXT);
                BotState.putState(chatId, state);
            }
            case "EDIT_CANCEL" -> {
                editMessageText.setText("Заметка изменена на:\n\n" + noteService.noteToString(note) + "\n\nВернуть в меню заметок - /notes");
                noteService.saveNote(note);
                BotState.removeState(chatId);
            }
        }
    }
}
