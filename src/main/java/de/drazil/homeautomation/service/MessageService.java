package de.drazil.homeautomation.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

@Service
public class MessageService {
	private List<Message> messageList = null;

	@PostConstruct
	public void init() {
		messageList = new ArrayList<>();
	}

	public List<Message> getMessageList() {
		final List<Message> list = new ArrayList<>(messageList);
		for (final Message event : list) {
			messageList.remove(event);
		}
		return list;
	}

	public int getMessageCount() {
		return messageList.size();
	}

	public void addMessage(final Message message) {
		messageList.add(message);
	}
}
