package de.drazil.homeautomation.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import de.drazil.homeautomation.bean.ActivityState;
import de.drazil.homeautomation.bean.Module;
import de.drazil.homeautomation.bean.Stage;
import de.drazil.homeautomation.bean.TrainingSlot;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

	@Value("${app.telegram-bot.name}")
	private String name;
	@Value("${app.telegram-bot.token}")
	private String token;
	@Value("${app.telegram-bot.chat-id}")
	private String chatId;

	@Autowired
	private SpringTemplateEngine templateEngine;

	private Map<String, ActivityState> userActivityStateMap = null;
	private Map<String, String> userMap = null;
	private List<TrainingSlot> trainingList = null;

	private static final String BOT_USERNAME = "/Benutzername";
	private static final String BOT_BOOKING = "/Buchen";
	private static final String BOT_PERSONAL_PLAN = "/Trainingsplan";
	private static final String BOT_BACK = "/Zurueck";
	private static final String BOT_RELEASE = "/Freigeben";
	private static final String BOT_OVERVIEW = "/Uebersicht";
	private static final String BOT_DAILY_REPORT = "/Tagesplan";

	@PostConstruct
	public void init() {

		userActivityStateMap = new HashMap<>();
		userMap = new HashMap<>();
		trainingList = new ArrayList<>();
		String location = "Aussenanlage";
		String[] lines = new String[] { "1 > 70 Meter", "2 > 50 Meter", "3 > 30 Meter", "4 > 20 Meter" };
		String[] timeslotsWorkday = new String[] { "15:00-16:30", "17:00-18:30", "19:00-20:30" };
		String[] timeslotsWeekend = new String[] { "11:00-12:30", "13:00-14:30", "15:00-16:30", "17:00-18:30",
				"19:00-20:30" };

		LocalDate today = LocalDate.now();
		for (int c = 0; c < 14; c++) {
			LocalDate d = today.plusDays(c);
			for (int l = 0; l < lines.length; l++) {
				String[] timeslots = timeslotsWorkday;
				if (d.getDayOfWeek() == DayOfWeek.SATURDAY || d.getDayOfWeek() == DayOfWeek.SUNDAY) {
					timeslots = timeslotsWeekend;
				}
				for (int ts = 0; ts < timeslots.length; ts++) {
					trainingList.add(new TrainingSlot(location, lines[l], timeslots[ts], d, null, -1, false, false));
				}
			}
		}
	}

	@Override
	public void onUpdateReceived(Update update) {
		Message message = update.getMessage();
		if (message != null) {
			Long userId = message.getFrom().getId();
			ActivityState activityState = userActivityStateMap.get(userId.toString());
			if (activityState == null) {
				log.info("New user logged in {}", userId.toString());
				activityState = new ActivityState();
				activityState.setMainStage(Stage.Greeting);
				activityState.setBookingStage(Stage.Date);
				activityState.setModule(Module.Overview);
				activityState.setUserId(message.getFrom().getId());
				activityState.setUserName(String.valueOf(message.getFrom().getId()));
				userActivityStateMap.put(userId.toString(), activityState);
				userMap.put(userId.toString(), userId.toString());
			}
			log.info("New user chatId:  {}", message.getChatId());
			activityState.setChatId(message.getChatId());
			try {
				runBot(update, activityState);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			log.error("Empty message");
		}
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

	private void runBot(Update update, ActivityState activityState) throws Exception {
		SendMessage sendMessage = new SendMessage();

		sendMessage.setChatId(String.valueOf(activityState.getChatId()));
		sendMessage.setParseMode("Markdown");
		String message = update.getMessage().getText();
		if (message.equals(BOT_OVERVIEW)) {
			activityState.setModule(Module.Overview);
			activityState.setMainStage(Stage.Greeting);
		} else if (message.equals(BOT_BOOKING)) {
			if (activityState.getModule() == Module.Overview) {
				activityState.setModule(Module.Booking);
			} else if (activityState.getModule() == Module.Booking) {
				TrainingSlot trainigSlot = getSelectedTrainingSlot(activityState);
				if (trainigSlot != null) {
					trainigSlot.setBooked(true);
					trainigSlot.setOwnerId(activityState.getUserId());
					trainigSlot.setOwnerName(activityState.getUserName());
					activityState.setBookingStage(Stage.Timeslot);
				}
			}
		} else if (message.equals(BOT_RELEASE)) {
			if (activityState.getModule() == Module.Booking) {
				TrainingSlot trainigSlot = getSelectedTrainingSlot(activityState);
				if (trainigSlot != null) {
					trainigSlot.setBooked(false);
					trainigSlot.setOwnerId(-1);
					trainigSlot.setOwnerName(null);
					activityState.setBookingStage(Stage.Timeslot);
					/*
					 * for (ActivityState state : userActivityStateMap.values()) { SendMessage
					 * sendGroupMessage = new SendMessage();
					 * sendMessage.setChatId(String.valueOf(activityState.getChatId()));
					 * sendMessage.setParseMode("Markdown"); }
					 */
				}
			}
		} else if (message.equals(BOT_USERNAME)) {
			if (activityState.getModule() == Module.Overview) {
				activityState.setMainStage(Stage.SetUserName);
			}
		} else if (message.equals(BOT_PERSONAL_PLAN)) {
			activityState.setModule(Module.Reservations);
		} else if (message.equals(BOT_DAILY_REPORT)) {
			activityState.setModule(Module.DailyReport);
		} else if (message.equals(BOT_BACK)) {
			if (activityState.getBookingStage() == Stage.Result) {
				activityState.setBookingStage(Stage.Timeslot);
			} else if (activityState.getBookingStage() == Stage.Timeslot) {
				activityState.setBookingStage(Stage.Line);
			} else if (activityState.getBookingStage() == Stage.Line) {
				activityState.setBookingStage(Stage.Date);
			} else if (activityState.getBookingStage() == Stage.Date) {
				activityState.setBookingStage(Stage.Date);
			}
		} else if (message.matches("/[0-9]*")) {
			String[] s = message.split("/");
			if (activityState.getBookingStage() == Stage.Date) {
				activityState.setBookingStage(Stage.Line);
				activityState.setSelectedDate(Integer.parseInt(s[1]));
			} else if (activityState.getBookingStage() == Stage.Line) {
				activityState.setBookingStage(Stage.Timeslot);
				activityState.setSelectedLine(Integer.parseInt(s[1]));
			} else if (activityState.getBookingStage() == Stage.Timeslot) {
				activityState.setBookingStage(Stage.Result);
				activityState.setSelectedTimeslot(Integer.parseInt(s[1]));
			}
		} else if (message.matches("[a-zA-Z]*")) {
			if (activityState.getModule() == Module.Overview && activityState.getMainStage() == Stage.SetUserName) {
				activityState.setUserName(message);
				activityState.setMainStage(Stage.ShowUserName);
				userMap.put(String.valueOf(activityState.getUserId()), message);
			}
		} else {
			sendMessage.setText("!! Unbekanntes Kommando !!");
		}

		String response = null;
		if (activityState.getModule() == Module.Overview) {
			if (activityState.getMainStage() == Stage.Greeting) {
				response = String.format(
						"*BSV Eppinghoven 1743 e.V.*\nWillkommen beim\nBogensport ReservierungsBot\n\n\n%s - anpassen\n%s - Meine Übersicht\n%s - Trainingsplan erstellen\n%s - Tagesplan\n",
						BOT_USERNAME, BOT_PERSONAL_PLAN, BOT_BOOKING, BOT_DAILY_REPORT);
			} else if (activityState.getMainStage() == Stage.SetUserName) {
				response = "Bitte gebe einen Benutzernamen ein.";
			} else if (activityState.getMainStage() == Stage.ShowUserName) {
				response = getUserName(activityState);
				activityState.setMainStage(Stage.Greeting);
			} else {
			}
		} else if (activityState.getModule() == Module.Booking) {
			if (activityState.getBookingStage() == Stage.Date) {
				response = getDateList(activityState);
			} else if (activityState.getBookingStage() == Stage.Line) {
				response = getLineList(activityState);
			} else if (activityState.getBookingStage() == Stage.Timeslot) {
				response = getTimeslotList(activityState);
			} else if (activityState.getBookingStage() == Stage.Result) {
				response = getResult(activityState);
			}
		} else if (activityState.getModule() == Module.Reservations) {
			response = getReservations(activityState);
		} else if (activityState.getModule() == Module.DailyReport) {
			sendDailyreport(LocalDate.now(), activityState);
			activityState.setModule(Module.Overview);
		}
		if (null != response) {
			sendMessage.setText(response);
			execute(sendMessage);
		}
	}

	private String getDateList(ActivityState activityState) {
		List<LocalDate> dateList = trainingList.stream().map(TrainingSlot::getDate).distinct()
				.collect(Collectors.toList());
		activityState.setDateList(dateList);
		StringBuilder sb = new StringBuilder();
		sb.append("Datum wählen\n");
		for (int i = 0; i < dateList.size(); i++) {
			sb.append(String.format("/%02d - %s\n", i, dateList.get(i).toString()));
		}
		sb.append("/Uebersicht\n");
		return sb.toString();
	}

	private String getLineList(ActivityState activityState) {
		List<String> lineList = trainingList.stream().filter(
				t -> t.getDate().compareTo(activityState.getDateList().get(activityState.getSelectedDate())) == 0)
				.map(TrainingSlot::getLine).distinct().collect(Collectors.toList());
		activityState.setLineList(lineList);
		StringBuilder sb = new StringBuilder();
		sb.append("Bahn wählen\n");
		for (int i = 0; i < lineList.size(); i++) {
			sb.append(String.format("/%02d - %s\n", i, lineList.get(i).toString()));
		}
		sb.append("/Zurueck\n");
		sb.append("/Uebersicht\n");
		return sb.toString();
	}

	private String getTimeslotList(ActivityState activityState) {
		List<TrainingSlot> trainingSlotList = trainingList.stream().filter(
				t -> t.getDate().compareTo(activityState.getDateList().get(activityState.getSelectedDate())) == 0
						&& t.getLine().equals(activityState.getLineList().get(activityState.getSelectedLine())))
				.collect(Collectors.toList());
		activityState.setTimeslotList(trainingSlotList);
		StringBuilder sb = new StringBuilder();
		sb.append("Trainingszeit wählen\n");
		for (int i = 0; i < trainingSlotList.size(); i++) {
			TrainingSlot slot = trainingSlotList.get(i);
			sb.append(String.format("%s%02d - %s - (%s)\n",
					(slot.isBooked() && activityState.getUserId() == slot.getOwnerId() || !slot.isBooked() ? "/" : ""),
					i, slot.getTimeslot(),
					slot.getOwnerId() == -1 ? "*frei*" : userMap.get(String.valueOf(slot.getOwnerId()))));
		}
		sb.append("/Zurueck\n");
		sb.append("/Uebersicht\n");
		return sb.toString();
	}

	private String getResult(ActivityState activityState) {
		StringBuilder sb = new StringBuilder();
		sb.append("Gewählte Trainingszeit\n");
		sb.append(String.format("Datum: %s\n",
				activityState.getDateList().get(activityState.getSelectedDate()).toString()));
		sb.append(String.format("Bahn: %s\n", activityState.getLineList().get(activityState.getSelectedLine())));
		sb.append(String.format("Zeit: %s\n",
				activityState.getTimeslotList().get(activityState.getSelectedTimeslot()).getTimeslot()));

		sb.append("/Buchen\n");
		sb.append("/Freigeben\n");
		sb.append("/Zurueck\n");
		sb.append("/Uebersicht\n");
		return sb.toString();
	}

	private String getReservations(ActivityState activityState) {
		List<TrainingSlot> trainingSlotList = trainingList.stream()
				.filter(t -> t.isBooked() && activityState.getUserId() == t.getOwnerId()).collect(Collectors.toList());
		StringBuilder sb = new StringBuilder();
		sb.append("Gebuchte Trainingszeit(en)\n");
		for (int i = 0; i < trainingSlotList.size(); i++) {
			TrainingSlot slot = trainingSlotList.get(i);
			sb.append(String.format("Datum: %s\n", slot.getDate().toString()));
			sb.append(String.format("Bahn: %s\n", slot.getLine()));
			sb.append(String.format("Zeit: %s\n", slot.getTimeslot()));
			if (i < trainingSlotList.size() - 1) {
				sb.append("------------------------\n");
			}
		}
		sb.append("/Uebersicht\n");
		return sb.toString();
	}

	private String getUserName(ActivityState activityState) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Dein neuer Benutzername ist : %s\n",
				userMap.get(String.valueOf(activityState.getUserId()))));
		sb.append("/Uebersicht\n");
		return sb.toString();
	}

	private TrainingSlot getSelectedTrainingSlot(ActivityState activityState) {
		return trainingList.stream().filter(
				d -> d.getDate().compareTo(activityState.getDateList().get(activityState.getSelectedDate())) == 0
						&& d.getLine().equals(activityState.getLineList().get(activityState.getSelectedLine()))
						&& d.getTimeslot().equals(
								activityState.getTimeslotList().get(activityState.getSelectedTimeslot()).getTimeslot()))
				.findFirst().orElse(null);
	}

	public void sendDailyreport(LocalDate date, ActivityState activityState) {
		SendDocument sendDocument = new SendDocument();
		List<TrainingSlot> trainingSlotList = trainingList.stream().filter(t -> t.getDate().compareTo(date) == 0)
				.collect(Collectors.toList());
		final Context context = new Context();
		context.setVariable("createDate", date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
		context.setVariable("list", trainingSlotList);

		final String html = templateEngine.process("daily_report", context);
		try {
			final ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(html);
			renderer.layout();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			renderer.createPDF(os);
			byte[] bytes = os.toByteArray();
			InputFile inputFile = new InputFile();
			InputStream is = new ByteArrayInputStream(bytes);
			inputFile.setMedia(is, String.format("Tagesplan_%tF.pdf", date));
			sendDocument.setChatId(String.valueOf(activityState.getChatId()));
			sendDocument.setDocument(inputFile);
			execute(sendDocument);
		} catch (final Exception e) {
			log.error("Failed to render pdf report", e);
		}

	}
}
