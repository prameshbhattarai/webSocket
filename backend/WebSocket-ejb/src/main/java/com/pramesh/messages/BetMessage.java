/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pramesh.messages;

/**
 *
 * @author prames
 */
public class BetMessage {

    private final String winner;
    private String result;
    private Integer numberOfBets;

    public BetMessage(String winner) {
        this.winner = winner;
        this.result = "";
        this.numberOfBets = 0;
    }

    public String getWinner() {
        return winner;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getNumberOfBets() {
        return numberOfBets;
    }

    public void setNumberOfBets(Integer numberOfBets) {
        this.numberOfBets = numberOfBets;
    }
    
    
}
