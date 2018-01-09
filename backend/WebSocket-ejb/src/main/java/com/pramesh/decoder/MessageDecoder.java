/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pramesh.decoder;

import com.pramesh.messages.BetMessage;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author prames
 */
public class MessageDecoder implements Decoder.Text<BetMessage> {

    private Map<String, String> messageMap = new HashMap<>();

    @Override
    public BetMessage decode(String string) throws DecodeException {
        BetMessage msg = null;
        if (willDecode(string)) {
            switch (this.messageMap.get("type")) {
                case "betMatchWinner":
                    msg = new BetMessage(this.messageMap.get("name"));
                    break;
            }
        } else {
            throw new DecodeException(string, "[Message] Can't decode.");
        }
        return msg;
    }

    @Override
    public boolean willDecode(String string) {
        this.messageMap = parseJsonObject(string).get();
        return isMessageDecode().test(new Object());
    }

    /* Check the kind of message and if all fields are included */
    private Predicate<Object> isMessageDecode() {
        return (nullObject) -> {
            boolean decodes = false;
            Set keys = this.messageMap.keySet();
            if (keys.contains("type")) {
                switch (this.messageMap.get("type")) {
                    case "betMatchWinner":
                        if (keys.contains("name")) {
                            decodes = true;
                        }
                        break;
                }
            }
            return decodes;
        };
    }

    /* Convert the message into a map */
    private Supplier<Map<String, String>> parseJsonObject(String string) {
        return () -> {
            JsonParser parser = Json.createParser(new StringReader(string));
            while (parser.hasNext()) {
                if (parser.next() == JsonParser.Event.KEY_NAME) {
                    String key = parser.getString();
                    parser.next();
                    String value = parser.getString();
                    this.messageMap.put(key, value);
                }
            }
            return this.messageMap;
        };
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }

}
