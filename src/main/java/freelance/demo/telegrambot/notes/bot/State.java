package freelance.demo.telegrambot.notes.bot;

import freelance.demo.telegrambot.notes.note.Note;
import lombok.Data;

@Data
public class State {
    private StateType stateType;
    private Note note;
}
