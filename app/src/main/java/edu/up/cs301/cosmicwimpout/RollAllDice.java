package edu.up.cs301.cosmicwimpout;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * A CounterMoveAction is an action that is a "move" the game: either increasing
 * or decreasing the counter value.
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @version September 2012
 */
public class RollAllDice extends GameAction {
	
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
	public RollAllDice(GamePlayer player, boolean isPlus) {
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
	 * getter method, to tell whether the move is a "plus"
	 * 
	 * @return
	 * 		a boolean that tells whether this move is a "plus"
	 */
	public boolean isPlus() {
		return isPlus;
		
	}
}//class CounterMoveAction
