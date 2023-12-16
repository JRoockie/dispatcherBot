package org.voetsky.dispatcherBot.services.input;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voetsky.dispatcherBot.exceptions.ParentException.LogicCoreException;
import org.voetsky.dispatcherBot.services.input.messageValidationService.MessageValidationService;
import org.voetsky.dispatcherBot.services.logic.commandHandlerService.CommandHandler;
import org.voetsky.dispatcherBot.services.output.messageMakerService.MessageMakerService;
import org.voetsky.dispatcherBot.services.output.producerService.ProducerService;

@Log4j
@AllArgsConstructor
@Service
public class ReceiverController {

    private final CommandHandler commandHandler;
    private final MessageMakerService messageMakerService;
    private final MessageValidationService messageValidationService;
    private final ProducerService producerService;

    public void processTextMessage(Update update) {
        try {
            if (messageValidationService.isValid(update)) {
                SendMessage sendMessage = updateReceived(update);
                sendMessageToView(sendMessage);
            }
        } catch (LogicCoreException e) {
            processError(e, update);
        }
    }

    public void consumeAudioMessageUpdates(Update update) {
        try {
            if (messageValidationService.isValid(update)) {
                sendMessageToView(updateReceived(update));
            }
        } catch (LogicCoreException e) {
            processError(e, update);
        }
    }

    public void consumeVoiceMessageUpdates(Update update) {
        try {
            if (messageValidationService.isValid(update)) {
                sendMessageToView(updateReceived(update));
            }
        } catch (LogicCoreException e) {
            processError(e, update);
        }
    }

    public void consumeButtonUpdates(Update update) {
        try {
            if (messageValidationService.isValid(update)) {
                sendMessageToView(updateReceived(update));
            }
        } catch (LogicCoreException e) {
            processError(e, update);
        }
    }

    public void processError(Exception e, Update update) {
        if (log.isDebugEnabled()) {
            log.debug(e);
        }
        String t = messageMakerService.getTextFromProperties(update, e.getMessage());
        sendErrorMessageToView(update, t);
    }

    public SendMessage updateReceived(Update update) {
        return commandHandler.updateReceived(update);
    }

    public SendMessage makeSendMessage(Update update, String text) {
        String t = messageMakerService.getTextFromProperties(update, text);
        return messageMakerService.makeSendMessage(update, t);
    }

    public void sendMessageToView(SendMessage s) {
        producerService.producerAnswer(s);
    }

    public void sendErrorMessageToView(Update update, String err) {
        SendMessage sendMessage = makeSendMessage(update, err);
        sendMessageToView(sendMessage);
    }

}