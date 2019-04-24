package edu.up.cs301.cosmicwimpout;

import org.junit.Test;

import static org.junit.Assert.*;

public class CosmicWimpoutStateTest {

    @Test
    public void totalDiceScore() {
        CosmicWimpoutState testState= new CosmicWimpoutState();
        int score = testState.totalDiceScore(testState.getDiceArray(),1);
        assertEquals(-1, score, 0);
    }


    @Test
    public void getDiceVal() {
        CosmicWimpoutState testState= new CosmicWimpoutState();
        String diceVal = testState.getDiceVal(1);
        assertEquals("should be tens", "Tens", diceVal);
    }

    @Test
    public void getWhoseTurn() {
        CosmicWimpoutState testState= new CosmicWimpoutState();
        int whoseTurn = testState.getWhoseTurn();
        assertEquals(0, whoseTurn, 0);
    }

    @Test
    public void endTurn() {
        CosmicWimpoutState testState= new CosmicWimpoutState();
        boolean result = testState.endTurn(0);
        int whoseTurn = testState.getWhoseTurn();
        int turnScore = testState.getTurnScore();
        assertTrue("should be player 2 turn", result);
        assertEquals(1, whoseTurn,0);
        assertEquals(0, turnScore,0);

    }

    @Test
    public void endGame() {
        //CosmicWimpoutState testState= new CosmicWimpoutState();
        //boolean result = testState.endGame(1);
        //assertTrue("expected game to be over", result);
    }

    @Test
    public void rollAllDice() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        boolean rollAllDice = testState.rollAllDice(0);
        assertTrue("Should roll all dice", rollAllDice);
    }

    @Test
    public void rollSingleDie() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        boolean rollSingleDie = testState.rollSingleDie(0, 2);
        assertTrue("Should roll first die", rollSingleDie);
    }

    @Test
    public void diceScoreForOneDice() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        int diceScoreForOneDice = testState.diceScoreForOneDice(testState.getDiceArray(), 0, 2);
        assertEquals(10, diceScoreForOneDice, 0);
    }

    @Test
    public void rollSelectedDice() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        boolean rollSelectedDice = testState.rollSelectedDice(0,true,true,true,true,true);
        assertTrue(rollSelectedDice);
    }

    @Test
    public void getDiceScore2() {

    }

    @Test
    public void getPlayer1Score() {
        CosmicWimpoutState testState= new CosmicWimpoutState();
        int player1Score = testState.getPlayer1Score();
        assertEquals(0, player1Score,0);
    }

    @Test
    public void getPlayer2Score() {
        CosmicWimpoutState testState= new CosmicWimpoutState();
        int player2Score = testState.getPlayer2Score();
        assertEquals(0, player2Score,0);
    }

    @Test
    public void getPlayer3Score() {
        CosmicWimpoutState testState= new CosmicWimpoutState();
        int player3Score = testState.getPlayer3Score();
        assertEquals(0, player3Score,0);
    }

    @Test
    public void getPlayer4Score() {
        CosmicWimpoutState testState= new CosmicWimpoutState();
        int player4Score = testState.getPlayer4Score();
        assertEquals(0, player4Score,0);
    }

    @Test
    public void getTurnScore() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        int turnScore = testState.getTurnScore();
        assertEquals(0,turnScore,0);
    }

    @Test
    public void getIsSuperNova() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        boolean isSupernova = testState.getIsSuperNova();
        assertFalse("It is not a supernova", isSupernova);
    }

    @Test
    public void getIsInstantWinner() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        boolean isInstantWinner = testState.getIsInstantWinner();
        assertFalse("It is not an instant winner", isInstantWinner);
    }

    @Test
    public void getDiceArray() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        Die[] diceArray = testState.getDiceArray();
        assertArrayEquals("Should get the dice array",diceArray,diceArray);
    }

    @Test
    public void getIsFlash() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        boolean getFlash = testState.getIsFlash();
        assertFalse("It is not a flash", getFlash);
    }

    @Test
    public void getIsFiveOf() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        boolean getIsFive = testState.getIsFiveOf();
        assertFalse("It is not five of a kind", getIsFive);
    }

    @Test
    public void checkAllFiveReRoll() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        boolean check5 = testState.checkAllFiveReRoll();
        assertTrue("It is five of a kind", check5);
    }

    @Test
    public void check1Die() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        boolean check1Die = testState.check1Die(0);
        assertFalse(check1Die);

    }

    @Test
    public void check2Dice() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        boolean check2Die = testState.check2Dice(0,1);
        assertFalse(check2Die);
    }

    @Test
    public void check3Dice() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        boolean check3Die = testState.check3Dice(0,1,2);
        assertFalse(check3Die);
    }

    @Test
    public void check4Dice() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        boolean check4Die = testState.check4Dice(0,1,2,3);
        assertFalse(check4Die);
    }

    @Test
    public void getIsWimpout() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        boolean getWimout = testState.getIsWimpout();
        assertFalse(getWimout);
    }

    @Test
    public void isDie1ReRoll() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        boolean isDie1 = testState.isDie1ReRoll();
        assertFalse(isDie1);
    }

    @Test
    public void isDie2ReRoll() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        boolean isDie2 = testState.isDie2ReRoll();
        assertFalse(isDie2);
    }

    @Test
    public void isDie3ReRoll() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        boolean isDie3 = testState.isDie3ReRoll();
        assertFalse(isDie3);
    }

    @Test
    public void isDie4ReRoll() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        boolean isDie4 = testState.isDie4ReRoll();
        assertFalse(isDie4);
    }

    @Test
    public void isDie5ReRoll() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        boolean isDie5 = testState.isDie5ReRoll();
        assertFalse(isDie5);
    }

    @Test
    public void flashReRoll() {
        CosmicWimpoutState testState = new CosmicWimpoutState();
        boolean[] flashReRoll = testState.flashReRoll();
        assertArrayEquals("Flash ReRoll", flashReRoll, flashReRoll);
    }

    @Test
    public void setIsFlash() {

    }
}