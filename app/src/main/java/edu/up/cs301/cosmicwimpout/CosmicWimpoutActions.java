package edu.up.cs301.cosmicwimpout;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

public class CosmicWimpoutActions extends GameAction {

    // to satisfy the serializable interface
    private static final long serialVersionUID = 28062013L;

    //whether this move is a plus (true) or minus (false)
    private boolean isPlus;

    /**
     * Constructor for the CounterMoveAction class.
     *
     * @param player
     *            the player making the move
     * @param isPlus
     *            value to initialize this.isPlus
     */
    public CosmicWimpoutActions(GamePlayer player, boolean isPlus) {
        super(player);
        this.isPlus = isPlus;
    }

    /**
     * Rolls all five dice at once
     * @param playerId
     * @return return true if legal move
     */
    public boolean rollAllDice(int playerId){
        if(playerId == whoseTurn) {
            //rolls all dice giving each a value 1-6
            for(int i = 0; i < 5; i++) {
                this.diceArray[i].dieState = (int) (Math.random() * 6 + 1);
            }
            if(totalDiceScore(diceArray,playerId) != -1) {
                turnScore = turnScore + totalDiceScore(diceArray,playerId);
            }
            else{
                turnScore = 0;
            }
            return true;
        }
        else{
            // illegal move
            return false;
        }
    }

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
    /**
     * getter method, to tell whether the move is a "plus"
     *
     * @return
     * 		a boolean that tells whether this move is a "plus"
     */
    public boolean isPlus() {
        return isPlus;

    }
}
