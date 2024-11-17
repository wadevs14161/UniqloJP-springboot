package com.example.restservice.linebothandler;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


@RestController
public class LineMessageController {
    @Value("${line.bot.channel-secret}")
    private String LINE_SECRET;


    @PostMapping("/messaging")
    public ResponseEntity<String> messagingAPI(@RequestHeader("X-Line-Signature") String X_Line_Signature,
                                               @RequestBody String requestBody) throws UnsupportedEncodingException, IOException {
        System.out.println("============messaging============");

        System.out.println("驗證通過" + requestBody);

        JSONObject object = new JSONObject(requestBody);
        for (int i = 0; i < object.getJSONArray("events").length(); i++) {
            if (object.getJSONArray("events").getJSONObject(i).getString("type").equals("message")) {
//                lineReplyMessageService.Message(object.getJSONArray("events").getJSONObject(i));
                System.out.println(object.getJSONArray("events").getJSONObject(i));
            }
        }

        return new ResponseEntity<String>("OK", HttpStatus.OK);


//        System.out.println("驗證不通過");
//        return new ResponseEntity<String>("Not line platform", HttpStatus.BAD_GATEWAY);
    }


}