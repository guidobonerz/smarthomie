package de.drazil.homeautomation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import de.drazil.homeautomation.bean.ActivityState;
import de.drazil.homeautomation.bean.Button;
import de.drazil.homeautomation.bean.TrainingSlot;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TelegramTestBot extends TelegramLongPollingBot {

	@Value("${app.telegram--test-bot.name}")
	private String botuserName;
	@Value("${app.telegram-test-bot.token}")
	private String botToken;

	private Map<String, ActivityState> userActivityStateMap = null;
	private Map<String, String> userMap = null;
	private List<TrainingSlot> trainingList = null;

	private static final Button overviewButton = new Button("Übersicht", "overview");
	private static final Button changeUsernameButton = new Button("Benutzernamen ändern", "changeUsername");
	private static final Button bookButton = new Button("Buchen", "book");
	private static final Button releaseButton = new Button("Freigeben", "release");
	private static final Button personalPlanButton = new Button("Persönlicher Trainingsplan", "personalPlan");
	private static final Button dailyPlanButton = new Button("Täglicher Trainingsplan", "dailyPlan");
	private static final Button backButton = new Button("Zurück", "back");
	private static final Button todayButton = new Button("Heute", "today");
	private static final Button tomorrowButton = new Button("Morgen", "tomorrow");
	private static final Button line1Button = new Button("Bahn1", "line1");
	private static final Button line2Button = new Button("Bahn2", "line2");
	private static final Button line3Button = new Button("Bahn3", "line3");
	private static final Button line4Button = new Button("Bahn4", "line4");
	private static final Button slot1Button = new Button("11:00-12:30", "1112");
	private static final Button slot2Button = new Button("13:00-14:30", "1314");
	private static final Button slot3Button = new Button("15:00-16:30", "1516");
	private static final Button slot4Button = new Button("17:00-18:30", "1718");
	private static final Button slot5Button = new Button("19:00-20:30", "1920");

	private static final String[] lines = new String[] { "1", "2", "3", "4" };
	private static final String[] linesPayload = new String[] { "1 - 70 Meter", "2 - 50 Meter", "3 - 30 Meter",
			"4 - 20 Meter" };
	private static final Button[] timeslotsWorkday = new Button[] { slot3Button, slot4Button, slot5Button };
	private static final Button[] timeslotsWeekend = new Button[] { slot1Button, slot2Button, slot3Button };

	@PostConstruct
	public void init() {

	}

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage()) {
			handleMessage(update.getMessage());
		} else if (update.hasCallbackQuery()) {
			handleCallbackQuery(update.getCallbackQuery());
		}
	}

	@Override
	public void onRegister() {
		// TODO Auto-generated method stub
		super.onRegister();
	}

	@Override
	public void onClosing() {
		// TODO Auto-generated method stub
		super.onClosing();
	}

	private void handleMessage(Message message) {
		String command = getCommand(message.getText());
		if (command.equals("start")) {
			SendMessage sendMessage = new SendMessage();
			sendMessage.setChatId(String.valueOf(message.getFrom().getId()));
			sendMessage.setText("Hallo User...");
			buildInlineKeyboard(new Button[][] { { new Button("Click me :-)", "click") } }, sendMessage);
			try {
				execute(sendMessage);
			} catch (Exception ex) {
				log.error("error send message", ex);
			}
		}
	}

	private void handleCallbackQuery(CallbackQuery callbackQuery) {
		AnswerCallbackQuery answer = new AnswerCallbackQuery(callbackQuery.getId());
		if (callbackQuery.getData().equals("click")) {
			answer.setShowAlert(false);
			answer.setText("button clicked");
			try {
				sendApiMethod(answer);
				EditMessageText t = new EditMessageText();
				t.setChatId(callbackQuery.getFrom().getId().toString());
				t.setMessageId(callbackQuery.getMessage().getMessageId());
				t.setText("updated...");
				execute(t);

			} catch (Exception ex) {
				log.error("Something went wrong", ex);
			}
		}
	}

	private String getCommand(String message) {
		if (message.startsWith("/")) {
			return message.substring(1);
		}
		return message;
	}

	@Override
	public String getBotToken() {
		return botToken;
	}

	@Override
	public String getBotUsername() {

		return botuserName;
	}

	private void buildInlineKeyboard(Button[][] matrix, SendMessage message) {
		List<List<InlineKeyboardButton>> keyboardConfig = new ArrayList<>();
		int i = 1;
		for (Button[] row : matrix) {
			List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
			for (Button button : row) {
				InlineKeyboardButton b = new InlineKeyboardButton(button.getText());
				b.setCallbackData(button.getCallback());
				keyboardRow.add(b);
			}
			keyboardConfig.add(keyboardRow);
		}
		InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(keyboardConfig);
		message.setReplyMarkup(keyboard);

	}

	private void buildKeyboard(String[][] matrix, SendMessage message) {
		List<KeyboardRow> keyboardConfig = new ArrayList<>();
		for (String[] row : matrix) {
			KeyboardRow keyboardRow = new KeyboardRow();
			for (String button : row) {
				keyboardRow.add(new KeyboardButton(button));
			}
			keyboardConfig.add(keyboardRow);
		}

		ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(keyboardConfig);
		keyboard.setResizeKeyboard(true);
		message.setReplyMarkup(keyboard);
	}
}
