/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pramesh.service;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author prames
 */
public abstract class Match {
    
    protected static final Logger LOG = Logger.getLogger("Match");
    
    protected String title;

    protected String p2Name;
    protected String p1Name;

    protected String p2Country;
    protected String p1Country;

    protected int p1Points = 0;
    protected int p2Points = 0;
    protected int p1Sets = 0;
    protected int p2Sets = 0;
    protected int p1Set1 = 0;
    protected int p1Set2 = 0;
    protected int p1Set3 = 0;
    protected int p2Set1 = 0;
    protected int p2Set2 = 0;
    protected int p2Set3 = 0;

    protected int p1GamesInCurrentSet = 0;
    protected int p2GamesInCurrentSet = 0;

    protected boolean isSet1Finished = false;
    protected boolean isSet2Finished = false;
    protected boolean isSet3Finished = false;

    protected String serve;

    protected boolean isFinished = false;

    protected StringBuffer liveComments = new StringBuffer();
    
    public String getPlayerOneName() {
        return p1Name;
    }

    public String getPlayerTwoName() {
        return p2Name;
    }
    
    public int getP1Points() {
        return p1Points;
    }

    public int getP2Points() {
        return p2Points;
    }

    public String getP2Name() {
        return p2Name;
    }

    public String getP1Name() {
        return p1Name;
    }

    public int getP1Set1() {
        return p1Set1;
    }

    public int getP1Set2() {
        return p1Set2;
    }

    public int getP1Set3() {
        return p1Set3;
    }

    public int getP2Set1() {
        return p2Set1;
    }

    public int getP2Set2() {
        return p2Set2;
    }

    public int getP2Set3() {
        return p2Set3;
    }

    public int getP1CurrentGame() {
        return p1GamesInCurrentSet;
    }

    public int getP2CurrentGame() {
        return p2GamesInCurrentSet;
    }

    public boolean isSet1Finished() {
        return isSet1Finished;
    }

    public boolean isSet2Finished() {
        return isSet2Finished;
    }

    public boolean isSet3Finished() {
        return isSet3Finished;
    }

    public String getLiveComments() {
        return liveComments.toString();
    }

    public void addLiveComments(String comments) {
        Calendar cal = Calendar.getInstance();
        int H = cal.get(Calendar.HOUR);
        int m = cal.get(Calendar.MINUTE);
        int s = cal.get(Calendar.SECOND);
        liveComments.append("\n").append(H + ":" + m + ":" + s).append(" - ").append(comments);
        LOG.log(Level.INFO, "{0} ({1}-{2}) : {3}", new Object[]{title, p1Name, p2Name, comments});
    }

    public int getP1Sets() {
        return p1Sets;
    }

    public int getP2Sets() {
        return p2Sets;
    }

    public int getP1GamesInCurrentSet() {
        return p1GamesInCurrentSet;
    }

    public int getP2GamesInCurrentSet() {
        return p2GamesInCurrentSet;
    }

    public String getServe() {
        return serve;
    }

    public void setServe(String serve) {
        this.serve = serve;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getP2Country() {
        return p2Country;
    }

    public void setP2Country(String p2Country) {
        this.p2Country = p2Country;
    }

    public String getP1Country() {
        return p1Country;
    }

    public void setP1Country(String p1Country) {
        this.p1Country = p1Country;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }
    
}
