package edu.up.cs301.cosmicwimpout;

import java.io.Serializable;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * this class holds all the cosmic wimpout game actions
 *
 * @author Kayla Moore, Olivia Dendinger, Sam Lemly, David Campbell
 * @version March 2019
 */

public class CosmicWimpoutActions extends GameAction implements Serializable {

    // to satisfy the serializable interface
    private static final long serialVersionUID = 28062013L;

    /**
     * Constructor for the CounterMoveAction class.
     *
     * @param player
     *            the player making the move
     */
    public CosmicWimpoutActions(GamePlayer player) {
        super(player);
    }

    /**
     * Rolls all five dice at once
     * @param playerId
     * @return return true if legal move
     */
    public boolean rollAllDice(int playerId){
        return false;
    }

    /**
     * endGame - will quit the game and return back to main menu
     * @param playerId
     * @return true if legal move
     */
    public boolean endGame(int playerId){
        return false;
    }

    /**
     * endTurn - if player chooses to end turn, add up their turn score to their
     * overall game score, switch to next player
     * @param playerId
     * @return true if legal move
     */
    public boolean endTurn(int playerId) { return false; }

    /**
     * Rolls only a single dice
     * Can only be called once the player knows what the previous roll was
     * @param playerId
     * @param id - which dice the player wants to roll
     * @return true if valid
     */
    public boolean rollSingleDie(int playerId, int id ){
        return false;
    }
}
