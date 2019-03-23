package edu.up.cs301.cosmicwimpout;

public class RollSingleDie extends RollAllDice{

    /**
     * Rolls only a single dice
     * Can only be called once the player knows what the previous roll was
     * @param playerId
     * @param id - which dice the player wants to roll
     * @return true if valid
     */
    public boolean rollSingleDie(int playerId, int id ){
        if(playerId == whoseTurn) {
            diceArray[id-1].rollMe();
            if(totalDiceScore(diceArray,playerId) != -1) {
                turnScore = turnScore + totalDiceScore(diceArray,playerId);
            }
            else{
                turnScore = 0;
                this.endTurn(playerId);

            }
            return true;
        }
        else{
            // illegal move
            return false;
        }
    }
}
