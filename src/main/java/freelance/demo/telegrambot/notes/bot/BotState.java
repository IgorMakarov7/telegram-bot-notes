package freelance.demo.telegrambot.notes.bot;

import java.util.HashMap;
import java.util.Map;

public class BotState {
    private static final Map<Long, State> botState = new HashMap<>();

    private BotState() {
    }

    public static void removeState(Long chatId) {
        botState.remove(chatId);
    }

    public static void putState(Long chatId, State state) {
        botState.put(chatId, state);
    }

    public static State getState(Long chatId) {
        return botState.get(chatId);
    }

    public static boolean hasState(Long chatId) {
        return botState.containsKey(chatId);
    }
}
