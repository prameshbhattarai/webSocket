/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pramesh.encoder;

import com.pramesh.messages.BetMessage;
import java.io.StringWriter;
import java.util.function.Supplier;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 *
 * @author prames
 */
public class BetMessageEncoder implements Encoder.Text<BetMessage> {

    @Override
    public String encode(BetMessage betMessage) throws EncodeException {
        StringWriter swriter = new StringWriter();
        try (JsonWriter jsonWrite = Json.createWriter(swriter)) {
            jsonWrite.writeObject(jsonObjectBuilder(betMessage).get());
        }
        return swriter.toString();
    }

    private Supplier<JsonObject> jsonObjectBuilder(BetMessage betMessage) {
        return () -> {
            JsonObjectBuilder builder = Json.createObjectBuilder();
            builder.add("winner", betMessage.getWinner())
                    .add("numberOfBets", betMessage.getNumberOfBets())
                    .add("result", betMessage.getResult());
            return builder.build();
        };
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }

}
