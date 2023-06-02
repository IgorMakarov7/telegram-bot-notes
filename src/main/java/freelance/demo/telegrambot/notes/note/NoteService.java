package freelance.demo.telegrambot.notes.note;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public void saveNote(Note note) {
        noteRepository.save(note);
    }
    public Note noteById(Long id) {
        return noteRepository.findById(id)
                .orElse(null);
    }

    public String noteToString(Note note) {
        return note.getName() + "\n" + note.getText();
    }
    public InlineKeyboardMarkup markupWithAllNotes(Long chatId, String callbackDataPrefix) {
        Iterable<Note> noteIterable = noteRepository.findAllByChatId(chatId);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (Note note : noteIterable) {
            List<InlineKeyboardButton> row = new ArrayList<>();

            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(note.getName());
            button.setCallbackData(callbackDataPrefix + note.getId());

            row.add(button);

            keyboard.add(row);
        }
        markup.setKeyboard(keyboard);
        return markup;
    }

    public void deleteNoteById(Long id) {
        Note note = new Note();
        note.setId(id);
        noteRepository.delete(note);
    }
}
