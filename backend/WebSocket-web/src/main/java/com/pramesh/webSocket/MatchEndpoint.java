/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pramesh.webSocket;

import com.pramesh.messages.MatchMessage;
import com.pramesh.decoder.MessageDecoder;
import com.pramesh.encoder.BetMessageEncoder;
import com.pramesh.encoder.MatchMessageEncoder;
import com.pramesh.messages.BetMessage;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author prames
 */
@ServerEndpoint(
        value = "/matches/{match-id}",
        decoders = {MessageDecoder.class},
        encoders = {MatchMessageEncoder.class, BetMessageEncoder.class}
)
public class MatchEndpoint {

    @Inject
    MatchStarter matchStarter;

    private static final Logger logger = Logger.getLogger("MatchEndpoint");

    /**
     * All open WebSocket sessions
     */
    static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());

    /**
     * Handle number of bets by match
     */
    static Map<String, AtomicInteger> numberOfBetsByMatch = new ConcurrentHashMap<>();

    @OnMessage
    public void message(final Session session, BetMessage betMessage, @PathParam("match-id") String matchId) {
        logger.log(Level.INFO, "Received: Bet Match Winner - {0}", betMessage.getWinner());
        //check if the user had already bet and save this bet
        boolean hasAlreadyBet = session.getUserProperties().containsKey("bet");
        session.getUserProperties().put("bet", betMessage.getWinner());

        //Send betMsg with bet count
        if (!numberOfBetsByMatch.containsKey(matchId)) {
            numberOfBetsByMatch.put(matchId, new AtomicInteger());
        }
        if (!hasAlreadyBet) {
            numberOfBetsByMatch.get(matchId).incrementAndGet();
        }
        sendBetMessages(null, matchId, false);
    }

    @OnOpen
    public void openConnection(Session session, @PathParam("match-id") String matchId) {
        logger.log(Level.INFO, "Session ID : " + session.getId() + " - Connection opened for match : " + matchId);
        session.getUserProperties().put(matchId, true);
        peers.add(session);

        //Send live result for this match
        send(new MatchMessage(matchStarter.getMatches().get(matchId)), matchId);
    }

    @OnClose
    public void closedConnection(Session session, @PathParam("match-id") String matchId) {
        if (session.getUserProperties().containsKey("bet")) {
            /* Remove bet */
            numberOfBetsByMatch.get(matchId).decrementAndGet();
            sendBetMessages(null, matchId, false);
        }
        /* Remove this connection from the queue */
        peers.remove(session);
        logger.log(Level.INFO, "Connection closed.");
    }

    @OnError
    public void error(Session session, Throwable t) {
        peers.remove(session);
        logger.log(Level.INFO, t.toString());
        logger.log(Level.INFO, "Connection error.");
    }

    /**
     * When the match is finished, each peer which has bet on this match receive
     * a message.
     *
     * @param winner
     * @param matchId
     * @param isFinished
     */
    public static void sendBetMessages(String winner, String matchId, boolean isFinished) {
        /* Send updates to all open WebSocket sessions for this match */
        peers.stream().filter((peer) -> peer.isOpen()).forEach((peer) -> {
            if (Boolean.TRUE.equals(peer.getUserProperties().get(matchId))) {
                if (peer.getUserProperties().containsKey("bet")) {
                    BetMessage betMessage = new BetMessage((String) peer.getUserProperties().get("bet"));
                    if (isFinished) {
                        if (winner != null && winner.equals(betMessage.getWinner())) {
                            betMessage.setResult("OK");
                        } else {
                            betMessage.setResult("KO");
                        }
                    }
                    sendMessageToSessionPeer().accept(peer, betMessage);
                    logger.log(Level.INFO, "Result Sent: {0}", betMessage.getResult());
                }
            }
        });
    }

    /**
     * Send Live Match message for all peers connected to this match
     *
     * @param matchMessage
     * @param matchId
     */
    public static void send(MatchMessage matchMessage, String matchId) {
        /* Send updates to all open WebSocket sessions for this match */
        peers.stream().filter((peer) -> peer.isOpen()).forEach((peer) -> {
            if (Boolean.TRUE.equals(peer.getUserProperties().get(matchId))) {
                sendMessageToSessionPeer().accept(peer, matchMessage);
            }
        });
    }

    private static BiConsumer<Session, Object> sendMessageToSessionPeer() {
        return (session, message) -> {
            try {
                session.getBasicRemote().sendObject(message);
                logger.log(Level.INFO, " Score Sent: {0}", message);
            } catch (IOException | EncodeException e) {
                logger.log(Level.INFO, e.toString());
            }
        };
    }

}
