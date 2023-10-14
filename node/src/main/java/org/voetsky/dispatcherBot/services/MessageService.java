package org.voetsky.dispatcherBot.services;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageService {
    SendMessage send(Update update, String text);
}