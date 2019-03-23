package edu.up.cs301.cosmicwimpout;

import edu.up.cs301.game.actionMsg.GameAction;

public class EndGame extends GameAction {
    /**
     * endGame - will quit the game and return back to main menu
     * @param playerId
     * @return true if legal move
     */
    public boolean endGame(int playerId){
        if(playerId == whoseTurn) {
            return true;
        }
        else{
            // illegal move
            return false;
        }
    }
}
