package edu.up.cs301.cosmicwimpout;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;
import android.util.Log;

/**
 * A class that represents the state of a game. In our counter game, the only
 * relevant piece of information is the value of the game's counter. The
 * CounterState object is therefore very simple.
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @version July 2013
 */
public class CosmicWimpoutLocalGame extends LocalGame {

	// When a counter game is played, any number of players. The first player
	// is trying to get the counter value to TARGET_MAGNITUDE; the second player,
	// if present, is trying to get the counter to -TARGET_MAGNITUDE. The
	// remaining players are neither winners nor losers, but can interfere by
	// modifying the counter.
	public static final int TARGET_MAGNITUDE = 500;

	// the game's state
	private CosmicWimpoutState gameState;
	
	/**
	 * can this player move
	 * 
	 * @return
	 * 		true, because all player are always allowed to move at all times,
	 * 		as this is a fully asynchronous game
	 */
	@Override
	protected boolean canMove(int playerIdx) {
		if(gameState.getWhoseTurn() == playerIdx) {
			return true;
		}
		return false;
	}

	/**
	 * This ctor should be called when a new counter game is started
	 */
	public CosmicWimpoutLocalGame() {
		// initialize the game state, with the counter value starting at 0
		this.gameState = new CosmicWimpoutState();
	}

	/**
	 * The only type of GameAction that should be sent is CounterMoveAction
	 */
	@Override
	protected boolean makeMove(GameAction action) {
		Log.i("action", action.getClass().toString());

		if(action instanceof CosmicWimpoutActionEndTurn) {
			CosmicWimpoutActionEndTurn endTurnAction = (CosmicWimpoutActionEndTurn) action;
			int whoseTurn = this.gameState.getWhoseTurn();
			return this.gameState.endTurn(whoseTurn);
		}
		else if (action instanceof CosmicWimpoutActionEndGame) {
			CosmicWimpoutActionEndGame endGameAction = (CosmicWimpoutActionEndGame) action;
			int whoseTurn = this.gameState.getWhoseTurn();
			return this.gameState.endGame(whoseTurn);
		}
		else if (action instanceof CosmicWimpoutActionRollAllDice) {
			CosmicWimpoutActionRollAllDice rollDiceAction = (CosmicWimpoutActionRollAllDice) action;
			int whoseTurn = this.gameState.getWhoseTurn();
			return this.gameState.rollAllDice(whoseTurn);
		}
		else if (action instanceof CosmicWimpoutActionRollSelectedDie){
			CosmicWimpoutActionRollSelectedDie act = (CosmicWimpoutActionRollSelectedDie) action;
			int whoseTurn = this.gameState.getWhoseTurn();
			return this.gameState.rollSelectedDice(whoseTurn, act.getDie1(), act.getDie2(), act.getDie3(),
					act.getDie4(), act.getDie5());
		}
		return false;
	}//makeMove
	
	/**
	 * send the updated state to a given player
	 */
	@Override
	protected void sendUpdatedStateTo(GamePlayer player) {
		// this is a perfect-information game, so we'll make a
		// complete copy of the state to send to the player
		player.sendInfo(new CosmicWimpoutState(gameState));
		
	}//sendUpdatedSate
	
	/**
	 * Check if the game is over. It is over, return a string that tells
	 * who the winner(s), if any, are. If the game is not over, return null;
	 * 
	 * @return
	 * 		a message that tells who has won the game, or null if the
	 * 		game is not over
	 */
	@Override
	protected String checkIfGameOver() {

		int player1Score = this.gameState.getPlayer1Score();
		int player2Score = this.gameState.getPlayer2Score();
		boolean superNova = this.gameState.getIsSuperNova();

		if(superNova){
			int whoseTurn = this.gameState.getWhoseTurn();
			return playerNames[whoseTurn] + " has rolled a supernova and lost";
		}

		if(player1Score >= TARGET_MAGNITUDE) {
			return playerNames[0] + " has won.";
		}
		else if(player2Score >= TARGET_MAGNITUDE) {
			if (playerNames.length >= 2) {
				return playerNames[1] + " has won";
			}
			else{
				return playerNames[0] + " has lost";
			}
		}
		else {
			// game is still between the two limit: return null, as the game
			// is not yet over
			return null;
		}

	}

}// class CounterLocalGame
