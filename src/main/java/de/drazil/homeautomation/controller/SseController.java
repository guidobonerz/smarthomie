package de.drazil.homeautomation.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import de.drazil.homeautomation.service.MessageService;

@Controller
public class SseController {
    private final ExecutorService nonBlockingService = Executors.newCachedThreadPool();

    @Autowired
    MessageService messageService;

    @GetMapping("/sse")
    public SseEmitter handleSse() {
        SseEmitter emitter = new SseEmitter(15000L);
        nonBlockingService.execute(() -> {
            try {
                if (messageService.getMessageCount() > 0) {
                    emitter.send(messageService.getMessageList());
                    emitter.complete();
                }

                // we could send more events

            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }
}
