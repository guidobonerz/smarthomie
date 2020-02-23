package de.drazil.homeautomation.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TelegramBot// extends TelegramLongPollingBot
{

	@Value("${app.telegram-bot.name}")
	private String name;
	@Value("${app.telegram-bot.token}")
	private String token;
	@Value("${app.telegram-bot.chat-id}")
	private Long chatId;
	/*
	 * @Override public void onUpdateReceived(Update update) { chatId =
	 * update.getMessage().getChatId(); System.out.println(chatId); }
	 * 
	 * @Override public String getBotUsername() { return name; }
	 * 
	 * @Override public String getBotToken() { return token; }
	 * 
	 * public void sendMessage(String message) { SendMessage sendMessage = new
	 * SendMessage().setChatId(chatId).setText(message); try { execute(sendMessage);
	 * } catch (TelegramApiException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 */
}
