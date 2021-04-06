package de.drazil.homeautomation.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TelegramBot extends TelegramLongPollingBot {

	@Value("${app.telegram-bot.name}")
	private String name;
	@Value("${app.telegram-bot.token}")
	private String token;
	@Value("${app.telegram-bot.chat-id}")
	private String chatId;

	@Override
	public void onUpdateReceived(Update update) {
		Message message = update.getMessage();
		Long chatId = message.getChatId();
		System.out.printf("%d:%s\n", chatId, message.getText());
	}

	@Override
	public String getBotUsername() {
		return name;
	}

	@Override
	public String getBotToken() {
		return token;
	}

	public void sendMessage(String message) throws Exception {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(chatId);
		sendMessage.setText(message);
		execute(sendMessage);
	}
}
