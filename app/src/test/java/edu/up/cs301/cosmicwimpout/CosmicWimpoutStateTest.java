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

    }

    @Test
    public void getIsInstantWinner() {

    }

    @Test
    public void getDiceArray() {

    }

    @Test
    public void getIsFlash() {

    }

    @Test
    public void getIsFiveOf() {

    }

    @Test
    public void checkAllFiveReRoll() {
    }

    @Test
    public void check1Die() {

    }

    @Test
    public void check2Dice() {

    }

    @Test
    public void check3Dice() {
    }

    @Test
    public void check4Dice() {
    }

    @Test
    public void getIsWimpout() {
    }

    @Test
    public void isDie1ReRoll() {
    }

    @Test
    public void isDie2ReRoll() {
    }

    @Test
    public void isDie3ReRoll() {
    }

    @Test
    public void isDie4ReRoll() {
    }

    @Test
    public void isDie5ReRoll() {
    }

    @Test
    public void flashReRoll() {
    }

    @Test
    public void setIsFlash() {
    }
}