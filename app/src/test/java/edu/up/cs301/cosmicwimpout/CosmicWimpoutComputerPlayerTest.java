package edu.up.cs301.cosmicwimpout;

import org.junit.Test;

import static org.junit.Assert.*;

public class CosmicWimpoutComputerPlayerTest {

    @Test
    public void receiveInfo() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        assertEquals(testState.check1Die(0), testState.check1Die(0));
    }
}