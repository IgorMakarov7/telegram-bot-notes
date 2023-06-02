package freelance.demo.telegrambot.notes.handlers;

import freelance.demo.telegrambot.notes.bot.BotState;
import freelance.demo.telegrambot.notes.bot.State;
import freelance.demo.telegrambot.notes.bot.StateType;
import freelance.demo.telegrambot.notes.note.Note;
import freelance.demo.telegrambot.notes.note.NoteService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Service
public class CallbackQueryService {

    private final NoteService noteService;
    private final UpdateNoteService updateNoteService;

    public CallbackQueryService(
            NoteService noteService,
            UpdateNoteService updateNoteService) {
        this.noteService = noteService;
        this.updateNoteService = updateNoteService;
    }

    public EditMessageText handleCallbackData(Long chatId, Integer messageId, String callbackData) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);

        switch (callbackData) {
            case "CREATE_NOTE" -> createNote(editMessageText, chatId);
            case "READ_NOTE" -> readNote(editMessageText, chatId);
            case "UPDATE_NOTE" -> updateNote(editMessageText, chatId);
            case "DELETE_NOTE" -> deleteNote(editMessageText, chatId);
            default -> {
                if (callbackData.startsWith("READ_")) {
                    Long noteId = Long.valueOf(callbackData.substring(5));
                    Note note = noteService.noteById(noteId);
                    editMessageText.setText(noteService.noteToString(note) + "\n\nМеню заметок - /notes");
                } else if (callbackData.startsWith("UPDATE_")) {
                    editMessageText.setText("Выберите что хотите изменить:");
                    editMessageText.setReplyMarkup(updateNoteService.questionsAboutUpdate());

                    Long noteId = Long.valueOf(callbackData.substring(7));
                    Note note = noteService.noteById(noteId);

                    State state = new State();
                    state.setNote(note);

                    BotState.putState(chatId, state);
                } else if (callbackData.startsWith("EDIT_")) {
                    updateNoteService.handleEdit(callbackData, editMessageText, chatId);
                } else if (callbackData.startsWith("DELETE_")) {
                    Long noteId = Long.valueOf(callbackData.substring(7));
                    noteService.deleteNoteById(noteId);
                    editMessageText.setText("Заметка успешно удалена.\n\nОткрыть меню заметок - /notes");
                }
            }
        }

        return editMessageText;
    }

    private void createNote(EditMessageText editMessageText, Long chatId) {
        editMessageText.setText("""
                Вы выбрали создать.
                                
                Введите название заметки:
                """);
        State state = new State();
        state.setStateType(StateType.CREATE_NOTE_NAME);
        BotState.putState(chatId, state);
    }

    private void readNote(EditMessageText editMessageText, Long chatId) {
        editMessageText.setText("""
                Выберите заметку по её названию.
                """);
        editMessageText.setReplyMarkup(noteService.markupWithAllNotes(chatId, "READ_"));
    }

    private void updateNote(EditMessageText editMessageText, Long chatId) {
        editMessageText.setText("""
                Выберите заметку по её названию.
                """);
        editMessageText.setReplyMarkup(noteService.markupWithAllNotes(chatId, "UPDATE_"));
    }

    private void deleteNote(EditMessageText editMessageText, Long chatId) {
        editMessageText.setText("Выберите заметку по названию, которую хотите удалить:");
        editMessageText.setReplyMarkup(noteService.markupWithAllNotes(chatId, "DELETE_"));
    }
}
