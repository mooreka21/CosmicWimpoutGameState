package edu.up.cs301.cosmicwimpout;

import edu.up.cs301.game.infoMsg.GameState;
import java.util.ArrayList;

/**
 * This contains the state for the Counter game. The state consist of simply
 * the value of the counter.
 * 
 * @author Steven R. Vegdahl
 * @version July 2013
 */
public class CosmicWimpoutState extends GameState {


	//instance variables that encompass all needed information

	//to satisfy Serializable interface
	private static final long serialVersionUID = 12345678910L;

	//initializing game variables
	private int whoseTurn;
	private int numPlayers;
	private int turnScore = 0;
	private boolean canReRoll = true;
	private boolean haveToReRoll = false;
	private Die diceArray[] = new Die[5];
	private ArrayList<Player> playerArrayList = new ArrayList();

	//initializing dice re-roll variables
	private int halfMoonReRoll;
	private int triangleReRoll;
	private int boltReRoll;
	private int fiveReRoll;
	private int starReRoll;
	private int tenReRoll;

	/**
	 * CosmicWimpoutState default constructor
	 * In the test iteration, this initializes three players
	 */
	public CosmicWimpoutState(){
		whoseTurn = 1;
		numPlayers = 3;
		//for loop creates dice in all diceArray indices.
		for(int i = 0; i < 5; i++){
			diceArray[i] = new Die(i);
		}
		this.playerArrayList.add(new Player(1));
		this.playerArrayList.add(new Player(2));
		this.playerArrayList.add(new Player(3));

		for(int i = 0; i < diceArray.length; i++){
			//initialize all dice
			this.diceArray[i].dieID = i+1;
			this.diceArray[i].dieState = 1;
		}
		for(int i = 0; i < playerArrayList.size();i++){
			//player instance variables
			this.playerArrayList.get(i).setPlayerID(i+1);
			this.playerArrayList.get(i).setPlayerScore(0);
		}
	}

	/**
	 * CosmicWimpoutState constructor that creates a deep copy
	 * @param orig
	 */
	public CosmicWimpoutState(CosmicWimpoutState orig){

		whoseTurn = orig.whoseTurn;

		for(int i =0; i < diceArray.length; i++){
			this.diceArray[i] = new Die(i+1);
			this.diceArray[i].dieID = orig.diceArray[i].dieID;
			this.diceArray[i].dieState = orig.diceArray[i].dieState;
		}

		for(int i=0; i < orig.playerArrayList.size();i++){
			this.playerArrayList.add(new Player(i+1));
			this.playerArrayList.get(i).setPlayerID(orig.playerArrayList.get(i).getPlayerID());
			this.playerArrayList.get(i).setPlayerScore(orig.playerArrayList.get(i).getPlayerScore());
		}
	}
	/**
	 * Totals the scores of all possible rolls, as well has reroll for the
	 * player when they are required.
	 *
	 * @param ourDice
	 * @param playerId
	 * @returns an int
	 */
	public int totalDiceScore(Die[] ourDice, int playerId){
		return 0;
	}

	public String getDiceVal(int dieId){
		return this.diceArray[dieId].getValAsString();
	}

	public int getWhoseTurn(){
		return this.whoseTurn;
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

	public int diceScoreForOneDice(int playerId, int diceID){
		if(diceID == 1){
			return 10;
		}
		if(diceID == 5){
			return 5;
		}
		return -1;
	}
}
