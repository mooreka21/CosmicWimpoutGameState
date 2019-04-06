package edu.up.cs301.cosmicwimpout;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;
import android.util.Log;

/**
 * A class that represents the state of a game.
 * 
 * @author Sam Lemly
 *  @author Olivia Dendinger
 *  @author David Campbell
 *  @author Kayla Moore
 *  * @version March 2019
 */
public class CosmicWimpoutLocalGame extends LocalGame {

	// When a cosmic wimpout game is played, any number of players.
	// both players are trying to get to the TARGET_MAGNITUDE.
	public static final int TARGET_MAGNITUDE = 500;
	private static final long serialVersionUID= 109876254625L;
	// the game's state
	private CosmicWimpoutState gameState;
	
	/**
	 * can this player move
	 * 
	 * @return
	 * 		true, if it is that players turn in the game
	 * 		state, false if its not their turn
	 */
	@Override
	protected boolean canMove(int playerIdx) {
		if(gameState.getWhoseTurn() == playerIdx) {
			return true;
		}
		return false;
	}

	/**
	 * This should be called when a new cosmic wimpout game
	 * is created
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

		//if its an end turn action, call end turn method in game state
		if(action instanceof CosmicWimpoutActionEndTurn) {
			CosmicWimpoutActionEndTurn endTurnAction = (CosmicWimpoutActionEndTurn) action;
			int whoseTurn = this.gameState.getWhoseTurn();
			return this.gameState.endTurn(whoseTurn);
		}
		//if its an end game call end game action which will exit the game
		else if (action instanceof CosmicWimpoutActionEndGame) {
			CosmicWimpoutActionEndGame endGameAction = (CosmicWimpoutActionEndGame) action;
			int whoseTurn = this.gameState.getWhoseTurn();
			return this.gameState.endGame(whoseTurn);
		}
		else if (action instanceof CosmicWimpoutActionRollAllDice) {
			//roll all the dice with method in game state
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
		//illegal move
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
		boolean instantWinner = this.gameState.getIsInstantWinner();

		//instant loser - rolled all tens
		if(superNova){
			int whoseTurn = this.gameState.getWhoseTurn();
			return playerNames[whoseTurn] + " has rolled a supernova and lost";
		}
		//instant winner - rolled all stars
		else if(instantWinner){
			int whoseTurn = this.gameState.getWhoseTurn();
			return playerNames[whoseTurn] + " has rolled an instant winner and won!";
		}
		//if player 1 score reaches goal, they won
		else if(player1Score >= TARGET_MAGNITUDE) {
			return playerNames[0] + " has won.";
		}
		//if player 2 score reaches goal, they won
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

}// class CosmicWimpoutLocalGame
