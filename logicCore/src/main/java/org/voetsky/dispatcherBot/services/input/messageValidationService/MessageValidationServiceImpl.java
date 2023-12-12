package org.voetsky.dispatcherBot.services.input.messageValidationService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.UserState;
import org.voetsky.dispatcherBot.exceptions.IncorrectInputException;
import org.voetsky.dispatcherBot.services.logic.commandHandlerService.CommandHandlerService;
import org.voetsky.dispatcherBot.services.repo.tgUserService.TgUserRepositoryService;

import static org.voetsky.dispatcherBot.UserState.*;


@Log4j
@AllArgsConstructor
@Service
public class MessageValidationServiceImpl implements MessageValidationService {

    private final CommandHandlerService commandHandlerService;
    private final TgUserRepositoryService tgUserRepositoryService;

    public String whichStateError(Update update) {
        UserState state = getState(update);

        switch (state) {
            case AWAITING_FOR_COMMAND:
                return "ввод команды";
            case AWAITING_FOR_TEXT:
                return "ввод текста";
            case AWAITING_FOR_BUTTON:
                return "нажатие кнопки";
            case AWAITING_FOR_AUDIO:
                return "mp3 файл";
            case AWAITING_FOR_VOICE:
                return "голосовое сообщение";
            default:
                return "неизвестная ошибка";
        }
    }

    public boolean isValid(Update update){
        if (log.isDebugEnabled()) {
            log.debug("Validation...");
        }
        return isActualState(update) || throwValidationException(update);
    }

    public boolean throwValidationException(Update update) {
        String errorMessage = String.format(
                "Ошибка ввода, ожидается %s", whichStateError(update));
        throw new IncorrectInputException(errorMessage);
    }

    public UserState getState(Update update) {
        return tgUserRepositoryService.getState(update);
    }

    public boolean isActualState(Update update) {
        UserState state = getState(update);

        if (update.getMessage() != null) {
            if (update.getMessage().getText() != null) {
                var text = update.getMessage().getText();
                var chatId = update.getMessage().getChatId().toString();
                if (text.equals("/start")){
                    return true;
                }
                if (commandHandlerService.getActions().containsKey(text)) {
                    return AWAITING_FOR_COMMAND == state;
                } else if (commandHandlerService.getBindingBy().containsKey(chatId)) {
                    return AWAITING_FOR_TEXT == state;
                }
            } else if (update.getMessage().hasVoice()) {
                return AWAITING_FOR_VOICE == state;
            } else if (update.getMessage().hasAudio()) {
                return AWAITING_FOR_AUDIO == state;
            }
        } else if (update.hasCallbackQuery()) {
            return AWAITING_FOR_BUTTON == state;
        }
        return false;
    }

}

