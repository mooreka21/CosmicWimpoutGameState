package edu.up.cs301.counter;

import edu.up.cs301.game.actionMsg.GameAction;

public class EndTurn extends GameAction {

    /**
     * endTurn - if player chooses to end turn, add up their turn score to their
     * overall game score, switch to next player
     * @param playerId
     * @return true if legal move
     */
    public boolean endTurn(int playerId) {
        if(playerId == whoseTurn) {
            int currentScore = playerArrayList.get(playerId-1).getPlayerScore();
            if(playerId == 1){
                playerArrayList.get(playerId - 1).setPlayerScore(currentScore + turnScore);
                whoseTurn = 2;
                turnScore = 0; //reset turn score to 0 for next player
            }
            else if(playerId == 2){
                playerArrayList.get(playerId - 1).setPlayerScore(currentScore + turnScore);
                whoseTurn = 3;
                turnScore = 0;
            }
            else if(playerId == 3){
                playerArrayList.get(playerId - 1).setPlayerScore(currentScore + turnScore);
                whoseTurn = 1;
                turnScore = 0; //reset turnScore to 0
            }
            return true;
        }
        else{
            // illegal move
            return false;
        }
    }
}
