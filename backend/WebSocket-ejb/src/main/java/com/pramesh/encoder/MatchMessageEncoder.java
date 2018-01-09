/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pramesh.encoder;

import com.pramesh.messages.BetMessage;
import com.pramesh.messages.MatchMessage;
import java.io.StringWriter;
import java.util.function.Supplier;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
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
public class MatchMessageEncoder implements Encoder.Text<MatchMessage> {

    @Override
    public String encode(MatchMessage matchMessage) throws EncodeException {
        StringWriter swriter = new StringWriter();
        try (JsonWriter jsonWrite = Json.createWriter(swriter)) {
            jsonWrite.writeObject(jsonObjectBuilder(matchMessage).get());
        }
        return swriter.toString();
    }

    private Supplier<JsonObject> jsonObjectBuilder(MatchMessage matchMessage) {
        return () -> {
            JsonObjectBuilder builder = Json.createObjectBuilder();
            builder.add(
                    "match",
                    Json.createObjectBuilder()
                    .add("serve", matchMessage.getMatch().getServe())
                    .add("title", matchMessage.getMatch().getTitle())
                    .add("players", playerArrayBuilder(matchMessage).get())
                    .add("comments", matchMessage.getMatch().getLiveComments())
                    .add("finished", matchMessage.getMatch().isFinished()));
            return builder.build();
        };
    }

    private Supplier<JsonArrayBuilder> playerArrayBuilder(MatchMessage matchMessage) {
        return () -> {
            return Json.createArrayBuilder()
                    .add(Json.createObjectBuilder()
                            .add("name", matchMessage.getMatch().getPlayerOneName())
                            .add("country", matchMessage.getMatch().getP1Country())
                            .add("games", matchMessage.getMatch().getP1CurrentGame())
                            .add("sets", matchMessage.getMatch().getP1Sets())
                            .add("points", matchMessage.getMatch().getPlayer1Score())
                            .add("set1", matchMessage.getMatch().getP1Set1())
                            .add("set2", matchMessage.getMatch().getP1Set2())
                            .add("set3", matchMessage.getMatch().getP1Set3()))
                    .add(Json.createObjectBuilder()
                            .add("name", matchMessage.getMatch().getPlayerTwoName())
                            .add("games", matchMessage.getMatch().getP2CurrentGame())
                            .add("country", matchMessage.getMatch().getP2Country())
                            .add("sets", matchMessage.getMatch().getP2Sets())
                            .add("points", matchMessage.getMatch().getPlayer2Score())
                            .add("set1", matchMessage.getMatch().getP2Set1())
                            .add("set2", matchMessage.getMatch().getP2Set2())
                            .add("set3", matchMessage.getMatch().getP2Set3()));
        };
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }

}
