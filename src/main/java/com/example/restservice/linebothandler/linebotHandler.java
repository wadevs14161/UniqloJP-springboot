package com.example.restservice.linebothandler;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
@LineMessageHandler
public class linebotHandler {

    private final Logger log = LoggerFactory.getLogger(linebotHandler.class);

//    @EventMapping
//    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
//        log.info("handleTextMessageEvent " + event);
//
//        final String originalMessageText = event.getMessage().getText();
//
//        // If originalMessageText is 6-digits string, call scrapeProduct
//        ProductService productScraper = new ProductService();
////        Product product = productScraper.scrapeProduct(originalMessageText);
//        String result = productScraper.scrapeProduct(originalMessageText);
//        return new TextMessage(result);
//    }
    @PostMapping
    @EventMapping
    public ResponseEntity handleMessageEvent(MessageEvent<TextMessageContent> event) {
        final String originalMessageText = event.getMessage().getText();
        System.out.println(originalMessageText);
        System.out.println("This is postMapping in linbot!");
        return ResponseEntity.ok(originalMessageText);
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("handleDefaultMessageEvent " + event);
    }

}
