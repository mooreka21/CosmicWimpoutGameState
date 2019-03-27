package edu.up.cs301.cosmicwimpout;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * A computer-version of a counter-player.  Since this is such a simple game,
 * it just sends "+" and "-" commands with equal probability, at an average
 * rate of one per second.
 *
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @version September 2013
 */
public class CosmicWimpoutComputerPlayer extends GameComputerPlayer {

	//instance variables
	private int ScoresFromCopy[] = new int[10];
	private int numRollsThisTurn;
	private float odds;
	private int intelligence;
	private CosmicWimpoutState state;

	/**
	 * Constructor for objects of class CosmicWimpoutComputerPlayer
	 *
	 * @param name
	 * 		the player's name
	 */
	public CosmicWimpoutComputerPlayer(String name) {
		// invoke superclass constructor
		super(name);
	}

	/**
	 * callback method--game's state has changed
	 *
	 * @param info
	 * 		the information (presumably containing the game's state)
	 */
	@Override
	protected void receiveInfo(GameInfo info) {
		int currentTurn = -1;

		if(info instanceof CosmicWimpoutState ){
			this.state = (CosmicWimpoutState) info;
			currentTurn = this.state.getWhoseTurn();
		}

		if(currentTurn != playerNum){

		}
		else {
			CosmicWimpoutActionRollAllDice allDiceAction = new CosmicWimpoutActionRollAllDice(this);
			game.sendAction(allDiceAction);

			int randomNumber = (int)(Math.random() * 10);
			while(randomNumber == 0){
				randomNumber = (int)(Math.random() * 10);
			}
			if(randomNumber > 5){
				CosmicWimpoutActionEndTurn endTurnAction = new CosmicWimpoutActionEndTurn(this);
				game.sendAction(endTurnAction);
			}
			else{
				int randomDice = (int)(Math.random() * 6 + 1);
				CosmicWimpoutActionRollSelectedDie selectedAction =
						new CosmicWimpoutActionRollSelectedDie(this);
				game.sendAction(selectedAction);
			}
		}
	}

}
