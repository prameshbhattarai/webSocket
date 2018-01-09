/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pramesh.service;

/**
 *
 * @author prames
 */
public class TennisMatch extends Match {

    private final String matchId;

    public TennisMatch(String matchId, String title, String playerOneName, String playerOneCountry,
            String playerTwoName, String playerTwoCountry) {
        this.matchId = matchId;
        this.title = title;
        this.p1Name = playerOneName;
        this.p2Name = playerTwoName;
        this.p1Country = playerOneCountry;
        this.p2Country = playerTwoCountry;
        this.serve = p1Name;
        liveComments.append("Welcome to this match between " + p1Name + " and " + p2Name + ".");
        LOG.info("Match started : " + title + " (" + p1Name + "-" + p2Name + ")");
    }

    public String getMatchId() {
        return matchId;
    }

    /**
     * Reset the match
     */
    public synchronized void reset() {
        p1Points = p2Points = 0;
        p1Sets = p2Sets = 0;
        p1Set1 = p1Set2 = p1Set3 = 0;
        p2Set1 = p2Set2 = p2Set3 = 0;
        p1GamesInCurrentSet = p2GamesInCurrentSet = 0;
        isSet1Finished = isSet2Finished = isSet3Finished = isFinished = false;
        liveComments = new StringBuffer();
        liveComments.append("WELCOME to this match between " + p1Name + " and " + p2Name + ".");

    }

    public String getPlayer1Score() {
        if (hasAdvantage() && p1Points > p2Points) {
            addLiveComments("Advantage " + playerWithHighestScore());
            return "AD";
        }
        if (isDeuce()) {
            addLiveComments("Deuce");
            return "40";
        }
        return translateScore(p1Points);
    }

    public String getPlayer2Score() {
        if (hasAdvantage() && p2Points > p1Points) {
            addLiveComments("Advantage " + playerWithHighestScore());
            return "AD";
        }
        if (isDeuce()) {
            return "40";
        }
        return translateScore(p2Points);
    }

    private boolean isDeuce() {
        return p1Points >= 3 && p2Points == p1Points;
    }

    private String playerWithHighestScore() {
        if (p1Points > p2Points) {
            return p1Name;
        } else {
            return p2Name;
        }
    }

    private String playerWithHighestGames() {
        if (p1GamesInCurrentSet > p2GamesInCurrentSet) {
            return p1Name;
        } else {
            return p2Name;
        }
    }

    public String playerWithHighestSets() {
        if (p1Sets > p2Sets) {
            return p1Name;
        } else {
            return p2Name;
        }
    }

    public boolean hasMatchWinner() {
        if (isSet1Finished && isSet2Finished && (isSet3Finished || p1Sets != p2Sets)) {
            return true;
        }
        return false;
    }

    public boolean hasGameWinner() {
        boolean hasGameWinner = false;
        if (p2Points >= 4 && p2Points >= p1Points + 2) {
            p2GamesInCurrentSet++;
            hasGameWinner = true;
        }
        if (p1Points >= 4 && p1Points >= p2Points + 2) {
            p1GamesInCurrentSet++;
            hasGameWinner = true;
        }
        if (hasGameWinner) {
            addLiveComments("Game " + playerWithHighestScore());
            p2Points = p1Points = 0;
            if (p1Name.equals(serve)) {
                serve = p2Name;
            } else {
                serve = p1Name;
            }
        }
        return hasGameWinner;
    }

    public boolean hasSetWinner() {
        if ((p1GamesInCurrentSet >= 6
                && (p1GamesInCurrentSet >= p2GamesInCurrentSet + 2 || p1GamesInCurrentSet
                + p2GamesInCurrentSet == 13))
                || (p2GamesInCurrentSet >= 6
                && (p2GamesInCurrentSet >= p1GamesInCurrentSet + 2 || p1GamesInCurrentSet
                + p2GamesInCurrentSet == 13))) {
            if (!isSet1Finished) {
                isSet1Finished = true;
                p1Set1 = p1GamesInCurrentSet;
                p2Set1 = p2GamesInCurrentSet;
            } else if (!isSet2Finished) {
                isSet2Finished = true;
                p1Set2 = p1GamesInCurrentSet;
                p2Set2 = p2GamesInCurrentSet;
            } else {
                isSet3Finished = true;
                p1Set3 = p1GamesInCurrentSet;
                p2Set3 = p2GamesInCurrentSet;
            }
            addLiveComments(playerWithHighestGames() + " wins this set !!");
            if (p1GamesInCurrentSet > p2GamesInCurrentSet) {
                p1Sets++;
            } else {
                p2Sets++;
            }
            p1GamesInCurrentSet = p2GamesInCurrentSet = 0;

            //check if match is finished
            if (hasMatchWinner()) {
                isFinished = true;
                addLiveComments(playerWithHighestGames() + " WINS the match !!");
            }

            return true;
        }
        return false;
    }

    private boolean hasAdvantage() {
        if (p2Points >= 4 && p2Points == p1Points + 1) {
            return true;
        }
        if (p1Points >= 4 && p1Points == p2Points + 1) {
            return true;
        }

        return false;

    }

    public void playerOneScores() {
        p1Points++;
        if (hasGameWinner()) {
            hasSetWinner();
        }
    }

    public void playerTwoScores() {
        p2Points++;
        if (hasGameWinner()) {
            hasSetWinner();
        }
    }

    private String translateScore(int score) {
        switch (score) {
            case 3:
                return "40";
            case 2:
                return "30";
            case 1:
                return "15";
            case 0:
                return "0";
        }
        return "-";
    }

}
