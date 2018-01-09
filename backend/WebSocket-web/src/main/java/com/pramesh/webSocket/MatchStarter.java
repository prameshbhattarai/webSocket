/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pramesh.webSocket;

import com.pramesh.messages.MatchMessage;
import com.pramesh.service.TennisMatch;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Named;

/**
 *
 * @author prames
 */
@Startup
@Singleton
@Named
public class MatchStarter {

    private Random random;
    private final Map<String, TennisMatch> matches = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger("Match Starter Service");

    @PostConstruct
    public void init() {
        logger.log(Level.INFO, "Initializing App.");
        random = new Random();
        matches.put("1234", new TennisMatch("1234", "US OPEN - QUARTER FINALS", "Ferrer D.", "es", "Almagro N.", "es"));
        matches.put("1235", new TennisMatch("1235", "US OPEN - QUARTER FINALS", "Djokovic N.", "rs", "Berdych T.", "cz"));
        matches.put("1236", new TennisMatch("1236", "US OPEN - QUARTER FINALS", "Murray A.", "gb", "Chardy J.", "fr"));
        matches.put("1237", new TennisMatch("1237", "US OPEN - QUARTER FINALS", "Federer R.", "ch", "Tsonga J.W.", "fr"));
    }

    @Schedule(second = "*/3", minute = "*", hour = "*", persistent = false)
    public void play() {
        matches.entrySet().stream().forEach((match) -> {
            TennisMatch tennisMatch = match.getValue();
            if (tennisMatch.isFinished()) {
                //add a timer to restart a match after 20 secondes
                tennisMatch.reset();
            }
            handlePoint().accept(tennisMatch);
            MatchEndpoint.send(new MatchMessage(tennisMatch), match.getKey());
            //if there is a winner, send result and reset the game
            if (tennisMatch.isFinished()) {
                MatchEndpoint.sendBetMessages(tennisMatch.playerWithHighestSets(), match.getKey(), true);
            }
        });
    }

    //Handle point randomly
    public final Consumer<TennisMatch> handlePoint() {
        return (tennisMatch) -> {
            if (random.nextInt(2) == 1) {
                tennisMatch.playerOneScores();
            } else {
                tennisMatch.playerTwoScores();
            }
        };
    }

    public Map<String, TennisMatch> getMatches() {
        return matches;
    }
}
