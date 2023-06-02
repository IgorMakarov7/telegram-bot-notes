package freelance.demo.telegrambot.notes.handlers;

import freelance.demo.telegrambot.notes.bot.BotState;
import freelance.demo.telegrambot.notes.bot.State;
import freelance.demo.telegrambot.notes.bot.StateType;
import freelance.demo.telegrambot.notes.note.Note;
import freelance.demo.telegrambot.notes.note.NoteService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class MessageService {

    private final NoteService noteService;
    private final UpdateNoteService updateNoteService;

    public MessageService(
            NoteService noteService,
            UpdateNoteService updateNoteService) {
        this.noteService = noteService;
        this.updateNoteService = updateNoteService;
    }

    public SendMessage handleMessage(Long chatId, String messageText) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (BotState.hasState(chatId)) {
            State state = BotState.getState(chatId);
            switch (state.getStateType()) {
                case CREATE_NOTE_NAME -> createNoteName(sendMessage, state, messageText, chatId);
                case CREATE_NOTE_TEXT -> createNoteText(sendMessage, state, messageText, chatId);
                case EDIT_NOTE_NAME -> editNoteName(sendMessage, state, messageText, chatId);
                case EDIT_NOTE_TEXT -> editNoteText(sendMessage, state, messageText, chatId);
            }
        } else {
            sendMessage.setText("Что бы понять, как я работаю - /help");
        }

        return sendMessage;
    }

    private void createNoteName(SendMessage sendMessage, State state, String noteName, Long chatId) {
        Note note = new Note();
        note.setChatId(chatId);
        note.setName(noteName);

        state.setNote(note);
        state.setStateType(StateType.CREATE_NOTE_TEXT);

        sendMessage.setText("""
                Введите текст заметки:
                """);
    }

    private void createNoteText(SendMessage sendMessage, State state, String noteText, Long chatId) {
        state.getNote().setText(noteText);

        noteService.saveNote(state.getNote());
        BotState.removeState(chatId);

        sendMessage.setText("Заметка сохранена. Вот она:\n\n"
                + noteService.noteToString(state.getNote())
                + "\n\nВернуть в меню заметок - /notes");
    }

    private void editNoteName(SendMessage sendMessage, State state, String noteName, Long chatId) {
        sendMessage.setText("""
                Изменения приняты.
                Для сохранения изменений нажмите кнопку завершения редактирования!
                 
                Выберите вариант продолжения редактирования:
                """);
        sendMessage.setReplyMarkup(updateNoteService.questionsAboutUpdate());

        state.getNote().setName(noteName);
        BotState.putState(chatId, state);
    }

    private void editNoteText(SendMessage sendMessage, State state, String noteText, Long chatId) {
        sendMessage.setText("""
                Изменения приняты.
                Для сохранения изменений нажмите кнопку завершения редактирования!
                 
                Выберите вариант продолжения редактирования:
                """);
        sendMessage.setReplyMarkup(updateNoteService.questionsAboutUpdate());

        state.getNote().setText(noteText);
        BotState.putState(chatId, state);
    }
}
