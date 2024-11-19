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


@LineMessageHandler
public class LinebotHandler {

    private final Logger log = LoggerFactory.getLogger(LinebotHandler.class);
    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        log.info("handleTextMessageEvent " + event);
        final String userText = event.getMessage().getText();
        // If userText is 6-digits string, call scrapeProduct
        if (userText.length() == 6 && userText.matches("\\d{6}")) {
            ProductService productService = new ProductService();
            String result = productService.scrapeProduct(userText);
            return new TextMessage(result);
        } else {
            return new TextMessage("請輸入6位數字的產品ID");
        }

    }
    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("handleDefaultMessageEvent " + event);
    }
}
