package edu.up.cs301.cosmicwimpout;

import org.junit.Test;

import static org.junit.Assert.*;

public class CosmicWimpoutComputerPlayer2Test {

    @Test
    public void receiveInfo() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        assertEquals(testState.check2Dice(0,1), testState.check2Dice(0,1));
    }

    @Test
    public void supportsGui() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        assertEquals(testState.getIsWimpout(),testState.getIsWimpout());
    }

    @Test
    public void setAsGui() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        assertEquals(testState.getIsFiveOf(),testState.getIsFiveOf());
    }

    @Test
    public void runSmartAi() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        assertEquals(testState.getIsSuperNova(), testState.getIsSuperNova());

    }
}