package com.example.restservice.linebothandler;


import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineBotDestination;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@LineMessageHandler
@RestController
public class LinebotController {

//    @PostMapping(path = "/findPrice")
    @EventMapping
    public Message uniqlo(MessageEvent<TextMessageContent> event) {
        try {
            System.out.println("event: " + event);
            final String userMessage = event.getMessage().getText();
            return new TextMessage(userMessage);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("event: " + event);
            return new TextMessage("Something went wrong");
        }

    }
}
