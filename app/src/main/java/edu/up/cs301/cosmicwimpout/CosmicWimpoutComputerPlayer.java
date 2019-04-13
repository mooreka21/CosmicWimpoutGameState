package edu.up.cs301.cosmicwimpout;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * A computer-version of a cosmic wimpout -player. this is the dumb AI
 * first rolls all the dice first - rules of the game
 * then with equal probabilty either ends the turn
 * or randomly selects one dice to re-roll
 *
 * @author Sam Lemly
 * @author Olivia Dendinger
 * @author David Campbell
 * @author Kayla Moore
  * @version March 2019
 */
public class CosmicWimpoutComputerPlayer extends GameComputerPlayer {
	//private static final long serialVersionUID= 390598448L;
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
			//delay to make it seem like they are thinking
			sleep(5000);
			CosmicWimpoutActionRollAllDice allDiceAction =
					new CosmicWimpoutActionRollAllDice(this);
			game.sendAction(allDiceAction);

			//random probability
			int randomNumber = (int)(Math.random() * 10);
			while(randomNumber == 0){
				randomNumber = (int)(Math.random() * 10);
			}


			boolean one = false;
			boolean two = false;
			boolean three = false;
			boolean four = false;
			boolean five = false;

			if(randomNumber > 5){
				CosmicWimpoutActionEndTurn endTurnAction = new CosmicWimpoutActionEndTurn(this);
				game.sendAction(endTurnAction);
			}
			else{
				CosmicWimpoutActionEndTurn endTurnAction = new CosmicWimpoutActionEndTurn(this);

				//select random die to re roll
				int randomDice = (int)(Math.random() * 5 + 1);
				if(randomDice == 1){
					one = true;
					if(state.check1Die(0)){
						CosmicWimpoutActionRollSelectedDie selectedAction =
								new CosmicWimpoutActionRollSelectedDie(this, one, two,
										three, four, five);
						game.sendAction(selectedAction);
					}
					else{
						game.sendAction(endTurnAction);
					}
				}
				else if(randomDice == 2){
					two = true;
					if(state.check1Die(1)){
						CosmicWimpoutActionRollSelectedDie selectedAction =
								new CosmicWimpoutActionRollSelectedDie(this, one, two,
										three, four, five);
						game.sendAction(selectedAction);
					}
					else{
						game.sendAction(endTurnAction);
					}
				}
				else if(randomDice == 3){
					three = true;
					if(state.check1Die(2)){
						CosmicWimpoutActionRollSelectedDie selectedAction =
								new CosmicWimpoutActionRollSelectedDie(this, one, two,
										three, four, five);
						game.sendAction(selectedAction);
					}
					else{
						game.sendAction(endTurnAction);
					}
				}
				else if(randomDice == 4){
					four = true;
					if(state.check1Die(3)){
						CosmicWimpoutActionRollSelectedDie selectedAction =
								new CosmicWimpoutActionRollSelectedDie(this, one, two,
										three, four, five);
						game.sendAction(selectedAction);
					}
					else{
						game.sendAction(endTurnAction);
					}
				}
				else if(randomDice == 5){
					five = true;
					if(state.check1Die(4)){
						CosmicWimpoutActionRollSelectedDie selectedAction =
								new CosmicWimpoutActionRollSelectedDie(this, one, two,
										three, four, five);
						game.sendAction(selectedAction);
					}
					else{
						game.sendAction(endTurnAction);
					}
				}


			}
		}
	}

}
