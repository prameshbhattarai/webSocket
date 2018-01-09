/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pramesh.messages;

import com.pramesh.service.TennisMatch;

/**
 *
 * @author prames
 */
public class MatchMessage {

    private final TennisMatch match;

    public MatchMessage(TennisMatch match) {
        this.match = match;
    }

    public TennisMatch getMatch() {
        return match;
    }

    @Override
    public String toString() {
        return "[MatchMessage] " + match.getMatchId()+ "-" + match.getTitle() + "-"
                + match.getPlayerOneName() + "-" + match.getPlayerTwoName();
    }
}
