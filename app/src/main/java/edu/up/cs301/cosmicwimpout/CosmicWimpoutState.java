package edu.up.cs301.cosmicwimpout;

import edu.up.cs301.game.infoMsg.GameState;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This contains the game state for the Cosmic Wimpout game.
 *
 * @author Olivia Dendinger, Sam Lemly, Kayla Moore, David Campbell
 * @version April 2019
 */

public class CosmicWimpoutState extends GameState implements Serializable {

	//to satisfy Serializable interface
	private static final long serialVersionUID = 12345678910L;

	//initializing game variables
	private int whoseTurn;
	private int turnScore = 0;
	private boolean canReRoll = true;
	private boolean haveToReRoll = false;
	private Die diceArray[] = new Die[5];
	private ArrayList<Player> playerArrayList = new ArrayList();

	//declaring dice re-roll variables
	private int halfMoonReRoll;
	private int triangleReRoll;
	private int boltReRoll;
	private int fiveReRoll;
	private int starReRoll;
	private int tenReRoll;

	//initializing variables for rollSelectedDice
	private int trueCounter = 0;
	private boolean die1ReRoll = false;
	private boolean die2ReRoll = false;
	private boolean die3ReRoll = false;
	private boolean die4ReRoll = false;
    private boolean die5ReRoll = false;

	//special cases
	private boolean isFlash = false;
	private boolean isFiveOf = false;
	private boolean isWimpout = false;

	//instance of previous state
	private CosmicWimpoutState prevState;

	//to determine instant winner and losers
	private boolean isSuperNova = false;
	private boolean isInstantWinner = false;


	/**
	 * CosmicWimpoutState default constructor initializes variables and
	 * four players
	 */
	public CosmicWimpoutState() {
		whoseTurn = 0;
		//numPlayers = 3;
		this.prevState = null;
		this.isFlash = this.isFlash;
		this.isFiveOf = this.isFiveOf;
		//for loop creates dice in all diceArray indices.
		for (int i = 0; i < 5; i++) {
			diceArray[i] = new Die(i);
		}
		this.playerArrayList.add(new Player(1));
		this.playerArrayList.add(new Player(2));
		this.playerArrayList.add(new Player(3));
		this.playerArrayList.add(new Player(4));

		for (int i = 0; i < diceArray.length; i++) {
			//initialize all dice
			this.diceArray[i].dieID = i + 1;
			this.diceArray[i].dieState = 1;
		}
		for (int i = 0; i < playerArrayList.size(); i++) {
			//player instance variables
			this.playerArrayList.get(i).setPlayerID(i + 1);
			this.playerArrayList.get(i).setPlayerScore(0);
		}
	}

	/**
	 * CosmicWimpoutState constructor that creates a deep copy of the current game state
	 *
	 * @param orig - original state
	 */
	public CosmicWimpoutState(CosmicWimpoutState orig) {

		whoseTurn = orig.whoseTurn;
		this.prevState = orig.prevState;
		this.turnScore = orig.turnScore;
		this.isFlash = orig.isFlash;
		this.isFiveOf = orig.isFiveOf;

		for (int i = 0; i < diceArray.length; i++) {
			this.diceArray[i] = new Die(i + 1);
			this.diceArray[i].dieID = orig.diceArray[i].dieID;
			this.diceArray[i].dieState = orig.diceArray[i].dieState;
		}

		for (int i = 0; i < orig.playerArrayList.size(); i++) {
			this.playerArrayList.add(new Player(i + 1));
			this.playerArrayList.get(i).setPlayerID(orig.playerArrayList.get(i).getPlayerID());
			this.playerArrayList.get(i).setPlayerScore(orig.playerArrayList.get(i).getPlayerScore());
		}
	}

	/**
	 * Totals the scores of all possible rolls, as well has reroll for the
	 * player when they are required.
	 *
	 * @param ourDice - array of dice
	 * @param playerId - current player's id
	 * @returns an int
	 */
	public int totalDiceScore(Die[] ourDice, int playerId) {
		//SUPERNOVA AND FREIGHT TRAIN CHECKING
		int tally = 0;
		if (ourDice[0].dieState == 1 &&
				ourDice[1].dieState == 1 &&
				ourDice[2].dieState == 1 &&
				ourDice[3].dieState == 1 &&
				ourDice[4].dieState == 1) {
			isSuperNova = true;
			return -1;
		}
		else if (ourDice[0].dieState == 2 &&
				ourDice[1].dieState == 2 &&
				ourDice[2].dieState == 2 &&
				ourDice[3].dieState == 2 &&
				ourDice[4].dieState == 2) {
			isFiveOf = true;
			haveToReRoll = true;
			tally = tally + 200;
		}
		else if (ourDice[0].dieState == 4 &&
				ourDice[1].dieState == 4 &&
				ourDice[2].dieState == 4 &&
				ourDice[3].dieState == 4 &&
				ourDice[4].dieState == 4) {
			//turnScore = turnScore + 400;
			isFiveOf = true;
			haveToReRoll = true;
			tally = tally + 400;
		}
		else if (ourDice[0].dieState == 5 &&
				ourDice[1].dieState == 5 &&
				ourDice[2].dieState == 5 &&
				ourDice[3].dieState == 5 &&
				ourDice[4].dieState == 5) {
			isFiveOf = true;
			haveToReRoll = true;
			// turnScore = turnScore + 500;
			tally = tally + 500;
		}
		else if (ourDice[0].dieState == 6 &&
				ourDice[1].dieState == 6 &&
				ourDice[2].dieState == 6 &&
				ourDice[3].dieState == 6 &&
				ourDice[4].dieState == 6) {
			this.isInstantWinner = true;
			return 0;
		}
		//END SUPERNOVA AND FREIGHT TRAIN CHECKING

		//count variables for the dice
		int halfMoonCount = 0;
		int triangleCount = 0;
		int boltCount = 0;
		int fiveCount = 0;
		int starCount = 0;
		int tenCount = 0;

		//BEGIN DICE COUNTING
		for (int i = 0; i < ourDice.length; i++) {
			if (ourDice[i].getDieState() == 1) {
				tenCount++;
				if (tenCount == 4) {
					tenReRoll = i;
				}
				else if (tenCount == 2) {
					tenReRoll = i + 1;
				}
			}
			if (ourDice[i].getDieState() == 2) {
				halfMoonCount++;
				if (halfMoonCount == 4) {
					halfMoonReRoll = i;
				}
				else if (halfMoonCount == 2) {
					halfMoonReRoll = i + 1;
				}
			}
			if (ourDice[i].getDieState() == 3 && ourDice[i].dieID != 3) {
				triangleCount++;
				if (triangleCount == 4) {
					triangleReRoll = i;
				}
				else if (triangleCount == 2) {
					triangleReRoll = i + 1;
				}
			}
			if (ourDice[i].getDieState() == 4) {
				boltCount++;
				if (boltCount == 4) {
					boltReRoll = i;
				}
				else if (boltCount == 2) {
					boltReRoll = i + 1;
				}
			}
			if (ourDice[i].getDieState() == 5) {
				fiveCount++;
				if (fiveCount == 4) {
					fiveReRoll = i;
				}
				else if (fiveCount == 2) {
					fiveReRoll = i + 1;
				}
			}
			if (ourDice[i].getDieState() == 6) {
				starCount++;
				if (starCount == 4) {
					starReRoll = i;
				}
				else if (starCount == 2) {
					starReRoll = i + 1;
				}
			}
		}
		//END DICE COUNTING


		//BEGIN FLAMING SUN FLASH CASES
		if (tenCount == 2) {
			if (ourDice[2].dieState == 3) {
				isFlash = true;
				tally = tally + 100;
			}
			else {
				tally = tally + 20;
			}
		}
		else if (tenCount == 1) {
			tally = tally + 10;
		}
		if (fiveCount == 2) {
			if (ourDice[2].dieState == 3) {
				isFlash = true;
				tally = tally + 50;
			}
			else {
				tally = tally + 10;
			}
		}
		else if (fiveCount == 1) {
			tally = tally + 5;
		}

		if (starCount == 2 && ourDice[2].dieState == 3) {
			isFlash = true;
			tally = tally + 60;
		}
		else if (boltCount == 2 && ourDice[2].dieState == 3) {
			isFlash = true;
			tally = tally + 40;
		}
		else if (triangleCount == 2 && ourDice[2].dieState == 3) {
			isFlash = true;
			tally = tally + 30;
		}
		else if (halfMoonCount == 2 && ourDice[2].dieState == 3) {
			isFlash = true;
			tally = tally + 20;
		}
		//END 10 & 5 COUNTING CASES
		//END FLAMING SUN FLASH CASE HANDLING


		//BEGIN NORMAL FLASH HANDLING
		else if (halfMoonCount >= 3 && halfMoonCount < 5) {
			isFlash = true;
			if (halfMoonCount == 4) {
				haveToReRoll = true;
			}
			tally = tally + 20;
		}
		else if (triangleCount >= 3 && triangleCount < 5) {
			isFlash = true;
			if (triangleCount == 4) {
				haveToReRoll = true;
			}
			tally = tally + 30;
		}
		else if (boltCount >= 3 && boltCount < 5) {
			isFlash = true;
			if (boltCount == 4) {
				haveToReRoll = true;
			}
			tally = tally + 40;
		}
		else if (fiveCount >= 3 && fiveCount < 5) {
			isFlash = true;
			if (fiveCount == 4) {
				haveToReRoll = true;
				tally = tally + 5;
			}
			tally = tally + 50;
		}
		else if (starCount >= 3 && starCount < 5) {
			isFlash = true;
			if (starCount == 4) {
				haveToReRoll = true;
			}
			tally = tally + 60;
		}
		else if (tenCount >= 3 && tenCount < 5) {
			isFlash = true;
			if (tenCount == 4) {
				haveToReRoll = true;
				tally = tally + 10;
			}
			tally = tally + 100;
		}
		//END NORMAL FLASH HANDLING


		//BEGIN ONLY FLAMING SUN CASE
		if (tenCount == 0 && fiveCount == 0 && ourDice[2].dieState == 3 && !isFlash) {
			tally = tally + 10;
		}
		//END ONLY FLAMING SUN CASE

		//BEGIN WIMPOUT CASE
		if (fiveCount == 0 && tenCount == 0 && ourDice[2].dieState != 3 && !isFlash && !isFiveOf) {
			this.isFlash = false;
			this.isFiveOf = false;
			this.isWimpout = true;
			return -1;
		}
		//END WIMPOUT CASE
        if(this.diceArray[0].getDieState() == 5 || this.diceArray[0].getDieState() == 1){
            this.diceArray[0].setCanReroll(false);
        }
        else {
        	this.diceArray[0].setCanReroll(true);
        }
        if(this.diceArray[1].getDieState() == 5 || this.diceArray[1].getDieState() == 1){
            this.diceArray[1].setCanReroll(false);
        }
        else {
        	this.diceArray[1].setCanReroll(true);
		}
        if(this.diceArray[2].getDieState() == 5 || this.diceArray[2].getDieState() == 1){
            this.diceArray[2].setCanReroll(false);
        }
        else {
        	this.diceArray[2].setCanReroll(true);
		}
        if(this.diceArray[3].getDieState() == 5 || this.diceArray[3].getDieState() == 1){
            this.diceArray[3].setCanReroll(false);
        }
        else {
        	this.diceArray[3].setCanReroll(true);
		}
        if(this.diceArray[4].getDieState() == 5 || this.diceArray[4].getDieState() == 1){
            this.diceArray[4].setCanReroll(false);
        }
        else {
        	this.diceArray[4].setCanReroll(true);
		}
		return tally;
	}

	/**
	 * Getter for any die's current dice state
	 *
	 * @param dieId - the die to recieve its state
	 * @return - the String value of the dice state
	 */
	public String getDiceVal(int dieId) {
		return this.diceArray[dieId].getValAsString();
	}

	/**
	 * getter for whose turn it currently is in the state
	 *
	 * @return whoseTurn - current player's turn
	 */
	public int getWhoseTurn() {
		return this.whoseTurn;
	}

	/**
	 * endTurn - if player chooses to end turn, add up their turn score to their
	 * overall game score, switch to next player
	 *
	 * @param playerId - player's id
	 * @return true if legal move
	 */
	public boolean endTurn(int playerId) {
		if (playerId == whoseTurn) {
			int currentScore = playerArrayList.get(playerId).getPlayerScore();
			if (playerId == 0) {
				//player 0's turn, get their score and add it to their total running score
				playerArrayList.get(playerId).setPlayerScore(currentScore + turnScore);
				whoseTurn = 1; // set the turn to next player
				turnScore = 0; //reset turn score to 0 for next player
				this.isFiveOf = false; //reset booleans
				this.isFlash = false;
			}
			else if (playerId == 1) {
				//get their score and add it to total running score
				playerArrayList.get(playerId).setPlayerScore(currentScore + turnScore);
				whoseTurn = 2;//set to next players turn
				turnScore = 0;// reset turn score to 0
				this.isFiveOf = false; //reset booleans
				this.isFlash = false;
			}
			else if (playerId == 2) {
				//get their score and add it to total running score
				playerArrayList.get(playerId).setPlayerScore(currentScore + turnScore);
				whoseTurn = 3; //set to next player
				turnScore = 0; //reset turnScore to 0
				this.isFiveOf = false; //reset booleans
				this.isFlash = false;
			}
			else if (playerId == 3) {
				//get score and add it to total running score
				playerArrayList.get(playerId).setPlayerScore(currentScore + turnScore);
				whoseTurn = 0; //reset to player 0's turn
				turnScore = 0; //reset turnScore to 0
				this.isFiveOf = false; //reset booleans
				this.isFlash = false;
			}
			this.prevState = new CosmicWimpoutState(this);
			return true;
		} else {
			// illegal move
			return false;
		}
	}

	/**
	 * endGame - will quit the game and return back to main menu
	 *
	 * @param playerId - player's id
	 * @return true if legal move
	 */
	public boolean endGame(int playerId) {
		if (playerId == whoseTurn) {
			//exits out of app
			System.exit(0);
			return true;
		}
		else {
			return false; //illegal move
		}
	}

	/**
	 * Rolls all five dice at once
	 *
	 * @param playerId - player's id
	 * @return return true if legal move
	 */
	public boolean rollAllDice(int playerId) {
		if (playerId == whoseTurn) {
			//rolls all dice giving each a value 1-6
			for (int i = 0; i < 5; i++) {
				this.diceArray[i].dieState = (int) (Math.random() * 6 + 1);
			}
			if (totalDiceScore(diceArray, playerId) != -1) {
				//if not a wimpout, add score to current turn score
				this.turnScore = this.turnScore + totalDiceScore(diceArray, playerId);
				this.isWimpout = false;
			}
			else {
				//wimpout set turn score to 0 and change players
				turnScore = 0;
				//they wimped out
				this.isWimpout = true;
				//set to the next players turn
				if (playerId == 0) {
					setWhoseTurn(1);
				}
				if (playerId == 1) {
					setWhoseTurn(2);
				}
				if (playerId == 2) {
					setWhoseTurn(3);
				}
				if (playerId == 3) {
					setWhoseTurn(0);
				}
			}
			return true;
		} else {
			return false; //illegal move
		}
	}

	/**
	 * Rolls a single die
	 *
	 * @param playerId - which player wants to roll
	 * @param id       - the dice id they want to roll
	 * @return - if it was a legal move or not
	 */
	public boolean rollSingleDie(int playerId, int id) {
		//roll single die
		if (playerId == whoseTurn) {
			diceArray[id - 1].rollMe();
			return true;
		}
		else {
			return false; //illegal move
		}
	}

	/**
	 * diceScoreForOneDice - calculates score if
	 * player only selects one die to roll
	 *
	 * @param ourDice  - dice array
	 * @param playerId - which player
	 * @param diceID   - which die they want to roll
	 * @return score
	 */
	public int diceScoreForOneDice(Die[] ourDice, int playerId, int diceID) {
		int diceState = ourDice[diceID - 1].dieState;
		if (diceID == 3) {
			if (diceState == 3) {
				return 10;
			}
			//flashing sun
		}
		if (ourDice[diceID - 1].dieState == 1) {
			//die state is 10
			return 10;
		}
		else if (ourDice[diceID - 1].dieState == 5) {
			//die state is 5
			return 5;
		}
		//when rolling one die, you cannot get a flash or five of a kind
		this.isFiveOf = false;
		this.isFlash = false;
		this.isWimpout = true; //wimped out
		return -1;
	}

	/**
	 * rollSelectedDice - action method for when the player
	 * sends an action of roll selected dice
	 *
	 * @param playerId - player's id
	 * @param dice1    - true if they select
	 * @param dice2    - true if they select
	 * @param dice3	   - true if they select
	 * @param dice4    - true if they select
	 * @param dice5    - true if they select
	 * @return boolean value
	 */
	public boolean rollSelectedDice
	(int playerId, boolean dice1, boolean dice2, boolean dice3, boolean dice4, boolean dice5) {
		if (playerId == whoseTurn) {
			//check to see if they selected each individual die
			int currentScore = playerArrayList.get(playerId).getPlayerScore();
			if (dice1) {
				//roll it and get the state
				rollSingleDie(playerId, 1);
				trueCounter++;// to decide how to score the dice
				die1ReRoll = true;
			}
			else {
				//player didnt select this die
				die1ReRoll = false;
			}

			if (dice2) {
				//selected, so roll the die
				rollSingleDie(playerId, 2);
				trueCounter++; //count up one
				die2ReRoll = true;
			}
			else {
				//player didnt select this die
				die2ReRoll = false;
			}
			if (dice3) {
				//roll die 3
				rollSingleDie(playerId, 3);
				trueCounter++; //die count up 1
				die3ReRoll = true;
			}
			else {
				//player didnt select this die
				die3ReRoll = false;
			}
			if (dice4) {
				//roll die 4
				rollSingleDie(playerId, 4);
				trueCounter++;// die count up 1
				die4ReRoll = true;
			}
			else {
				//player didnt select this die
				die4ReRoll = false;
			}
			if (dice5) {
				//roll die 5
				rollSingleDie(playerId, 5);
				trueCounter++; //die count up one
				die5ReRoll = true;
			}
			else {
				//player didnt select this die
				die5ReRoll = false;
			}

			if (trueCounter == 1) { //only selected one die
				//get which die they rolled
				int whichDie = whichDice();
				//score that dice
				int score = diceScoreForOneDice(diceArray, playerId, whichDie);
				if (score != -1) {
					turnScore = turnScore + score;
					trueCounter = 0;
					this.isWimpout = false;
					return true;
				}
				else {
					//wimpout end turn
					turnScore = 0; //reset turn score to 0
					trueCounter = 0; //reset
					this.isWimpout = true;
					endTurn(playerId);
					return true;
				}
			}
			if (trueCounter == 2) { //selected two dice
				//get which die they selected
				int[] whichDice = whichDice2();
				int first = (int) whichDice[0]; //first die they selected
				int second = (int) whichDice[1]; //second die they selected
				//score the two dice
				int score = getDiceScore2(diceArray, first, second);
				if (score != -1) {
					turnScore = turnScore + score;
					trueCounter = 0;
					this.isWimpout = false;
					return true;
				}
				else {
					//wimpout, reset turn score and counter
					turnScore = 0;
					trueCounter = 0;
					this.isWimpout = true;
					endTurn(playerId); //end turn
					return true;
				}
			}
			if (trueCounter == 3) { //selected three dice
				//get which dice they selected
				int[] whichDice = whichDice3();
				int first = whichDice[0]; //first die
				int second = whichDice[1]; //second die
				int third = whichDice[2]; //third die

				//score only those 3 die
				int score = getScore3Dice(diceArray, first, second, third);
				if (score != -1) {
					turnScore = turnScore + score;
					trueCounter = 0;
					this.isWimpout = false;
					return true;
				}
				else {
					//wimpout, end turn, reset turn score and counter
					turnScore = 0;
					trueCounter = 0;
					this.isWimpout = true;
					endTurn(playerId);
					return true;
				}
			}
			if (trueCounter == 4) { //selected 4 dice
				//get which dice they selected
				int[] whichDice = whichDice4();
				int first = whichDice[0]; //first die
				int second = whichDice[1]; //second die
				int third = whichDice[2]; //third die
				int fourth = whichDice[3]; //fourth die

				//score those 4 dice
				int score = getScore4Dice(diceArray, first, second, third, fourth);
				if (score != -1) {
					turnScore = turnScore + score;
					trueCounter = 0;
					this.isWimpout = false;
					return true;
				}
				else {
					//wimpout
					turnScore = 0; //reset for next player
					trueCounter = 0;
					this.isWimpout = true;
					endTurn(playerId); //end turn
					return true;
				}
			}
			if (trueCounter == 5) { //selected all 5 dice
				rollAllDice(playerId); //roll all 5
				this.turnScore = this.turnScore;
				trueCounter = 0;
				this.isWimpout = false;
				return true;
			}
		}
		trueCounter = 0;
		return false;
	}

	/**
	 * calculates score for when they select
	 * two die
	 *
	 * @param ourDice - dice array
	 * @param one     - die one they select
	 * @param two     - die two they select
	 * @return        - score
	 */
	public int getDiceScore2(Die[] ourDice, int one, int two) {
		int tally = 0;
		boolean notWimp = false;

		//flaming sun case
		if (one == 3 || two == 3) {
			if (ourDice[2].dieState == 3) {
				tally = 10;
				notWimp = true;
			}
		}
		//if the first die is a 10
		if (ourDice[one - 1].dieState == 1) {
			tally = tally + 10; //add 10
			notWimp = true;
		}
		else if (ourDice[one - 1].dieState == 5) {
			//die 1 is a 5, add 5
			tally = tally + 5;
			notWimp = true;
		}

		if (ourDice[two - 1].dieState == 1) {
			//second die is a 10, add 10
			tally = tally + 10;
			notWimp = true;
		}
		else if (ourDice[two - 1].dieState == 5) {
			//second die is a 5, add 5
			tally = tally + 5;
			notWimp = true;
		}
		//if they did not wimpout out
		if (notWimp) {
			//return their score for that roll
			return tally;
		} else {
			//wimpout
			this.isFiveOf = false;
			this.isFlash = false;
			return -1;
		}

	}

	/**
	 * get player 1s current score
	 *
	 * @return player 1's score
	 */
	public int getPlayer1Score() {
		return this.playerArrayList.get(0).getPlayerScore();
	}

	/**
	 * get player 2s current score
	 *
	 * @return player 2's score
	 */
	public int getPlayer2Score() {
		return this.playerArrayList.get(1).getPlayerScore();
	}

	/**
	 * get player 3s current score
	 *
	 * @return player 3's score
	 */
	public int getPlayer3Score() {
		return this.playerArrayList.get(2).getPlayerScore();
	}

	/**
	 * get player 4s current score
	 *
	 * @return player 4's score
	 */
	public int getPlayer4Score() {
		return this.playerArrayList.get(3).getPlayerScore();
	}

	/**
	 * set whose turn it is
	 *
	 * @param player   - whose turn it is
	 */
	private void setWhoseTurn(int player) {
		this.whoseTurn = player;
	}

	/**
	 * finds which die they selected
	 *
	 * @return - die id they select
	 */
	private int whichDice() {
		if (die1ReRoll) {
			//selected 1
			return 1;
		}
		else if (die2ReRoll) {
			//selected 2
			return 2;
		}
		else if (die3ReRoll) {
			//selected 3
			return 3;
		}
		else if (die4ReRoll) {
			//selected 4
			return 4;
		}
		else if (die5ReRoll) {
			//selected 5
			return 5;
		}
		return 0;
	}

	/**
	 * finds which 2 dice they selected
	 *
	 * @return - dice id they select in an array
	 */
	private int[] whichDice2() {
		int[] twoDice = new int[2];
		if (die1ReRoll) {
			//1 was selected, put in array
			twoDice[0] = 1;
		}
		if (die2ReRoll) {
			//2 was selected, add to array
			if (twoDice[0] == 0) {
				//check to see if die 1 was selected
				twoDice[0] = 2;
			}
			twoDice[1] = 2;
		}
		if (die3ReRoll) {
			//check to see if 1 or 2 were selected
			if (twoDice[0] == 0) {
				twoDice[0] = 3;
			}
			twoDice[1] = 3;

		}
		if (die4ReRoll) {
			//checks to see if its the first to be selected
			if (twoDice[0] == 0) {
				twoDice[0] = 4;
			}
			//second die
			twoDice[1] = 4;

		}
		if (die5ReRoll) {
			//checkes to see if it was the first to be selected
			if (twoDice[0] == 0) {
				twoDice[0] = 5;
			}
			//second die
			twoDice[1] = 5;
		}
		//return the dice array containing the id's
		return (twoDice);
	}

	/**
	 * finds which 3 dice they selected
	 *
	 * @return - die id they select in an array
	 */
	private int[] whichDice3() {
		int[] threeDice = new int[3];
		if (die1ReRoll) {
			threeDice[0] = 1;
		}
		if (die2ReRoll) {
			if (threeDice[0] == 0) {
				threeDice[0] = 2;
			}
			threeDice[1] = 2;
		}
		if (die3ReRoll) {
			if (threeDice[0] == 0) {
				threeDice[0] = 3;
			} else if (threeDice[1] == 0) {
				threeDice[1] = 3;
			}
			threeDice[2] = 3;
		}
		if (die4ReRoll) {
			if (threeDice[0] == 0) {
				threeDice[0] = 4;
			} else if (threeDice[1] == 0) {
				threeDice[1] = 4;
			}
			threeDice[2] = 4;
		}
		if (die5ReRoll) {
			if (threeDice[0] == 0) {
				threeDice[0] = 5;
			} else if (threeDice[1] == 0) {
				threeDice[1] = 5;
			}
			threeDice[2] = 5;
		}
		//return the array containing the id's
		return threeDice;
	}

	/**
	 * finds which 4 dice they selected
	 *
	 * @return - die id they select in an array
	 */
	private int[] whichDice4() {
		int[] fourDice = new int[4];
		if (die1ReRoll) {
			fourDice[0] = 1;
		}
		if (die2ReRoll) {
			if (fourDice[0] == 0) {
				fourDice[0] = 2;
			}
			fourDice[1] = 2;
		}
		if (die3ReRoll) {
			if (fourDice[0] == 0) {
				fourDice[0] = 3;
			} else if (fourDice[1] == 0) {
				fourDice[1] = 3;
			}
			fourDice[2] = 3;
		}
		if (die4ReRoll) {
			if (fourDice[1] == 0) {
				fourDice[1] = 4;
			} else if (fourDice[2] == 0) {
				fourDice[2] = 4;
			}
			fourDice[3] = 4;
		}
		if (die5ReRoll) {
			if (fourDice[1] == 0) {
				fourDice[1] = 5;
			} else if (fourDice[2] == 0) {
				fourDice[2] = 5;
			}
			fourDice[3] = 5;
		}
		return fourDice;
	}

	/**
	 * finds which dice they selected
	 *
	 * @return - die id they select in an array
	 */
	private int getScore4Dice(Die[] ourDice, int one, int two, int three, int four) {
		int score = 0;
		boolean notWimp = false;
		int tenCount = 0;
		int moonCount = 0;
		int triangleCount = 0;
		int boltCount = 0;
		int fiveCount = 0;
		int starCount = 0;

		//Counting to check for flashes
		//count die 1
		if (ourDice[one - 1].getDieState() == 1) {
			tenCount++;
		}
		else if (ourDice[one - 1].getDieState() == 2) {
			moonCount++;
		}
		else if (ourDice[one - 1].getDieState() == 3 && (one - 1) != 2) {
			triangleCount++;
		}
		else if (ourDice[one - 1].getDieState() == 4) {
			boltCount++;
		}
		else if (ourDice[one - 1].getDieState() == 5) {
			fiveCount++;
		}
		else if (ourDice[one - 1].getDieState() == 6) {
			starCount++;
		}
		//count die 2
		if (ourDice[two - 1].getDieState() == 1) {
			tenCount++;
		}
		else if (ourDice[two - 1].getDieState() == 2) {
			moonCount++;
		}
		else if (ourDice[two - 1].getDieState() == 3 && (two - 1) != 2) {
			triangleCount++;
		}
		else if (ourDice[two - 1].getDieState() == 4) {
			boltCount++;
		}
		else if (ourDice[two - 1].getDieState() == 5) {
			fiveCount++;
		}
		else if (ourDice[two - 1].getDieState() == 6) {
			starCount++;
		}
		//count die 3
		if (ourDice[three - 1].getDieState() == 1) {
			tenCount++;
		}
		else if (ourDice[three - 1].getDieState() == 2) {
			moonCount++;
		}
		else if (ourDice[three - 1].getDieState() == 3 && (three - 1) != 2) {
			triangleCount++;
		}
		else if (ourDice[three - 1].getDieState() == 4) {
			boltCount++;
		}
		else if (ourDice[three - 1].getDieState() == 5) {
			fiveCount++;
		}
		else if (ourDice[three - 1].getDieState() == 6) {
			starCount++;
		}
		//count die 4
		if (ourDice[four - 1].getDieState() == 1) {
			tenCount++;
		}
		else if (ourDice[four - 1].getDieState() == 2) {
			moonCount++;
		}
		else if (ourDice[four - 1].getDieState() == 3 && (four - 1) != 2) {
			triangleCount++;
		}
		else if (ourDice[four - 1].getDieState() == 4) {
			boltCount++;
		}
		else if (ourDice[four - 1].getDieState() == 5) {
			fiveCount++;
		}
		else if (ourDice[four - 1].getDieState() == 6) {
			starCount++;
		}

		//flash cases, add corresponding score
		//set flash to true, if there is one
		if (tenCount >= 3) {
			isFlash = true;
			//10 three of a kind
			score = score + 100;
			notWimp = true;
		}
		else if (moonCount >= 3) {
			isFlash = true;
			//moon three of a kind
			score = score + 20;
			notWimp = true;
		}
		else if (triangleCount >= 3) {
			isFlash = true;
			//triangles three of a kind
			score = score + 30;
			notWimp = true;
		}
		else if (boltCount >= 3) {
			isFlash = true;
			//bolts three of a kind
			score = score + 40;
			notWimp = true;
		}
		else if (fiveCount >= 3) {
			isFlash = true;
			//five three of a kind
			score = score + 50;
			notWimp = true;
		}
		else if (starCount >= 3) {
			isFlash = true;
			//stars three of a kind
			score = score + 60;
			notWimp = true;
		}

		//flash, flaming sun cases
		if (tenCount == 2 && (one == 3 || two == 3 || three == 3 || four == 3) &&
				ourDice[2].dieState == 3) {
			if (ourDice[2].dieState == 3) {
				isFlash = true;
				score = score + 100;
				notWimp = true;
			}
			else {
				//if no flaming score add 20
				score = score + 20;
				notWimp = true;
			}
		}
		//if no flaming sun add 20
		else if (tenCount == 2) {
			score = score + 20;
			notWimp = true;
		}

		//moon flash
		if (moonCount == 2 && (one == 3 || two == 3 || three == 3 || four == 3)) {
			if (ourDice[2].dieState == 3) {
				isFlash = true;
				score = score + 20;
				notWimp = true;
			}
		}
		//triangle flash
		if (triangleCount == 2 && (one == 3 || two == 3 || three == 3 || four == 3)) {
			if (ourDice[2].dieState == 3) {
				isFlash = true;
				score = score + 30;
				notWimp = true;
			}
		}
		//bolt flash
		if (boltCount == 2 && (one == 3 || two == 3 || three == 3 || four == 3)) {
			if (ourDice[2].dieState == 3) {
				isFlash = true;
				score = score + 40;
				notWimp = true;
			}
		}
		//five flash
		if (fiveCount == 2 && (one == 3 || two == 3 || three == 3 || four == 3) && ourDice[2].dieState == 3) {
			isFlash = true;
			score = score + 50;
			notWimp = true;
		} else if (fiveCount == 2) {
			//if no flaming sun, add 10
			score = score + 10;
			notWimp = true;
		}
		//star flsah
		if (starCount == 2 && (one == 3 || two == 3 || three == 3 || four == 3)) {
			if (ourDice[2].dieState == 3) {
				isFlash = true;
				score = score + 60;
				notWimp = true;
			}
		}

		//check to see if there is only one 10
		if (tenCount == 1) {
			//add 10
			score = score + 10;
			notWimp = true;
		}

		//check to see if only one five
		if (fiveCount == 1) {
			//add five
			score = score + 5;
			notWimp = true;
		}

		//Checking to see if the flaming sun is the only scoring die
		if ((one == 3 || two == 3 || three == 3 || four == 3) && tenCount == 0
				&& fiveCount == 0 && !isFlash) {
			if (ourDice[2].dieState == 3) {
				//if so add 10
				score = score + 10;
				notWimp = true;
			}
		}

		//if they didnt wimpout
		if (notWimp) {
			//return their score for that roll
			return score;
		} else {
			//wimpout
			this.isFiveOf = false;
			this.isFlash = false;
			return -1;
		}
	}

	/**
	 * calculates the score when they select
	 * 3 dice
	 *
	 * @param ourDice - array of dice
	 * @param one     - first dice id
	 * @param two     - second dice id
	 * @param three   - third dice id
	 * @return        - score
	 */
	private int getScore3Dice(Die[] ourDice, int one, int two, int three) {
		int score = 0;
		boolean notWimp = false;

		//Flash Cases
		if (ourDice[one - 1].dieState == 1 && ourDice[two - 1].dieState == 1 &&
				ourDice[three - 1].dieState == 1) {
			//isFlash = true;
			score = 100;
			notWimp = true;
		}
		else if (ourDice[one - 1].dieState == 2 && ourDice[two - 1].dieState == 2 &&
				ourDice[three - 1].dieState == 2) {
			isFlash = true;
			score = 20;
			notWimp = true;
		}
		else if (ourDice[one - 1].dieState == 3 && ourDice[two - 1].dieState == 3 &&
				ourDice[three - 1].dieState == 3) {
			isFlash = true;
			score = 30;
			notWimp = true;
		}
		else if (ourDice[one - 1].dieState == 4 && ourDice[two - 1].dieState == 4 &&
				ourDice[three - 1].dieState == 4) {
			isFlash = true;
			score = 40;
			notWimp = true;
		}
		else if (ourDice[one - 1].dieState == 5 && ourDice[two - 1].dieState == 5 &&
				ourDice[three - 1].dieState == 5) {
			//isFlash = true;
			score = 50;
			notWimp = true;
		}
		else if (ourDice[one - 1].dieState == 6 && ourDice[two - 1].dieState == 6 &&
				ourDice[three - 1].dieState == 6) {
			isFlash = true;
			score = 60;
			notWimp = true;
		}
		//flash and flaming sun case
		else if (flashWithSun(ourDice, one, two, three) != -1) {
			//gets score if there is a flash with flaming sun
			score = flashWithSun(ourDice, one, two, three);
			notWimp = true;
		}
		else {
			//no flashes check for five's and ten's
			if (one == 3 || two == 3 || three == 3) {
				if (ourDice[2].dieState == 3) {
					score = score + 10;
					notWimp = true;
				}
			}

			if (ourDice[one - 1].dieState == 1) {
				score = score + 10;
				notWimp = true;
			}
			else if (ourDice[one - 1].dieState == 5) {
				score = score + 5;
				notWimp = true;
			}

			if (ourDice[two - 1].dieState == 1) {
				score = score + 10;
				notWimp = true;
			}
			else if (ourDice[two - 1].dieState == 5) {
				score = score + 5;
				notWimp = true;
			}

			if (ourDice[three - 1].dieState == 1) {
				score = score + 10;
				notWimp = true;
			}
			else if (ourDice[three - 1].dieState == 5) {
				score = score + 5;
				notWimp = true;
			}
		}
		//if they did not wimpout, return their score for that roll
		if (notWimp) {
			return score;
		}
		else {
			//wimpout
			this.isFiveOf = false;
			this.isFlash = false;
			return -1;
		}
	}

	/**
	 * helper method to score flashes with flaming suns
	 *
	 * @param ourDice  - array of dice
	 * @param one      - first dice id
	 * @param two      - second dice id
	 * @param three    - third dice id
	 * @return         - score
	 */
	private int flashWithSun(Die[] ourDice, int one, int two, int three) {
		int score = 0;
		//checks to see if one of the dice they selected is die 3
		if (one == 3) {
			if (ourDice[one - 1].dieState == 3) {
				//check for flash cases with flaming sun
				if (ourDice[two - 1].dieState == 1 && ourDice[three - 1].dieState == 1) {
					isFlash = true;
					//ten flash, add 100
					score = 100;
				}
				else if (ourDice[two - 1].dieState == 2 && ourDice[three - 1].dieState == 2) {
					isFlash = true;
					//moon flash, add 20
					score = 20;
				}
				else if (ourDice[two - 1].dieState == 3 && ourDice[three - 1].dieState == 3) {
					isFlash = true;
					//triangle flash, add 30
					score = 30;
				}
				else if (ourDice[two - 1].dieState == 4 && ourDice[three - 1].dieState == 4) {
					isFlash = true;
					//bolts flash, add 40
					score = 40;
				}
				else if (ourDice[two - 1].dieState == 5 && ourDice[three - 1].dieState == 5) {
					isFlash = true;
					//fives flash add 50
					score = 50;
				}
				else if (ourDice[two - 1].dieState == 6 && ourDice[three - 1].dieState == 6) {
					isFlash = true;
					//stars flash, add 60
					score = 60;
				}
				else {
					//no flash with flaming sun
					score = -1;
				}
			}
			else {
				score = -1;
			}
			//repeat for the second die they selected
		} else if (two == 3) {
			if (ourDice[two - 1].dieState == 3) {
				if (ourDice[one - 1].dieState == 1 && ourDice[three - 1].dieState == 1) {
					isFlash = true;
					score = 100;
				}
				else if (ourDice[one - 1].dieState == 2 && ourDice[three - 1].dieState == 2) {
					isFlash = true;
					score = 20;
				}
				else if (ourDice[one - 1].dieState == 3 && ourDice[three - 1].dieState == 3) {
					isFlash = true;
					score = 30;
				}
				else if (ourDice[one - 1].dieState == 4 && ourDice[three - 1].dieState == 4) {
					isFlash = true;
					score = 40;
				}
				else if (ourDice[one - 1].dieState == 5 && ourDice[three - 1].dieState == 5) {
					isFlash = true;
					score = 50;
				}
				else if (ourDice[one - 1].dieState == 6 && ourDice[three - 1].dieState == 6) {
					isFlash = true;
					score = 60;
				}
				else {
					//no flash with flaming sun
					score = -1;
				}
			} else {
				score = -1;
			}
			//repeat for the third die they selected
		} else if (three == 3) {
			if (ourDice[three - 1].dieState == 3) {
				if (ourDice[two - 1].dieState == 1 && ourDice[one - 1].dieState == 1) {
					isFlash = true;
					score = 100;
				}
				else if (ourDice[two - 1].dieState == 2 && ourDice[one - 1].dieState == 2) {
					isFlash = true;
					score = 20;
				}
				else if (ourDice[two - 1].dieState == 3 && ourDice[one - 1].dieState == 3) {
					isFlash = true;
					score = 30;
				}
				else if (ourDice[two - 1].dieState == 4 && ourDice[one - 1].dieState == 4) {
					isFlash = true;
					score = 40;
				}
				else if (ourDice[two - 1].dieState == 5 && ourDice[one - 1].dieState == 5) {
					isFlash = true;
					score = 50;
				}
				else if (ourDice[two - 1].dieState == 6 && ourDice[one - 1].dieState == 6) {
					isFlash = true;
					score = 60;
				}
				else {
					score = -1;
				}
			}
			else {
				//no flash with flaming sun
				score = -1;
			}
		}
		else {
			//no flash with flaming sun 
			score = -1;
		}
		return score;
	}

	/**
	 * get turn score
	 *
	 * @return turnScore
	 */
	public int getTurnScore() {
		return this.turnScore;
	}

	/**
	 * get isSuperNova boolean
	 *
	 * @return isSuperNove
	 */
	public boolean getIsSuperNova() {
		return this.isSuperNova;
	}

	/**
	 * get isInstantWinner boolean
	 *
	 * @return isInstantWinner
	 */
	public boolean getIsInstantWinner() {
		return this.isInstantWinner;
	}

	/**
	 * get diceArray array
	 * @return diceArray
	 */
	public Die[] getDiceArray() {
		return diceArray;
	}

	/**
	 * get isFlash boolean
	 * @return isFlash
	 */
	public boolean getIsFlash() {
		return isFlash;
	}

	/**
	 * get isFiveOf boolean
	 * @return isFiveOf
	 */
	public boolean getIsFiveOf() {
		return isFiveOf;
	}

	/**
	 * checkAllFiveReRoll method returns a boolean
	 * @return boolean value
	 */
	public boolean checkAllFiveReRoll() {
		int tenCount = 0;
		int fiveCount = 0;
		for (int i = 0; i < this.diceArray.length; i++) {
			if (this.diceArray[i].getDieState() == 1) {
				tenCount++;
			} else if (this.diceArray[i].getDieState() == 5) {
				fiveCount++;
			}
		}
		if (tenCount == 5 || fiveCount == 5) {
		    isFlash = false;
			return true;
		}
		if(tenCount == 4 && fiveCount == 1){
		    isFlash = false;
			return true;
		}
		if(tenCount == 3 && fiveCount == 2) {
            isFlash = false;
			return true;
		}
		if(tenCount == 2 && fiveCount == 3){
            isFlash = false;
			return true;
		}
		if(tenCount == 4 && fiveCount == 1){
            isFlash = false;
			return true;
		}

		else if (isFiveOf) {
			return true;
		}
		else if (isFlash) {
			int tenCount1 = 0;
			int fiveCount1 = 0;
			for (int i = 0; i < this.diceArray.length; i++) {
				if (this.diceArray[i].getDieState() == 1) {
					tenCount1++;
				}
				else if (this.diceArray[i].getDieState() == 5) {
					fiveCount1++;
				}
			}
			if (tenCount1 == 2 || fiveCount1 == 2) {
				isFlash = false;
				return true;
			}
			else if (fiveCount1 == 1 && tenCount1 == 1) {
				//isFlash = false;
				return true;
			}
		}
		else {
			int tenCount2 = 0;
			int fiveCount2 = 0;
			for (int i = 0; i < this.diceArray.length; i++) {
				if (this.diceArray[i].getDieState() == 1) {
					tenCount2++;
				}
				else if (this.diceArray[i].getDieState() == 5) {
					fiveCount2++;
				}
			}
			if (tenCount == 5 || fiveCount == 5 || (tenCount == 4 && fiveCount == 1)
					|| (tenCount == 3 && fiveCount == 2) || (tenCount == 2 && fiveCount == 3)
					|| ((tenCount == 4 && fiveCount == 1))) {
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}

	/**
	 * check1Die method returns a boolean value
	 * @param diceId  - id of first die
	 * @return boolean value
	 */
	public boolean check1Die(int diceId) {
		if (isFlash) {
			int tenCount = 0;
			int fiveCount = 0;
			int moonCount = 0;
			int triangleCount = 0;
			int starCount = 0;
			int boltCount = 0;

			for (int i = 0; i < this.diceArray.length; i++) {
				if (this.diceArray[i].getDieState() == 1) {
					tenCount++;
				}
				else if (this.diceArray[i].getDieState() == 5) {
					fiveCount++;
				}
				else if (this.diceArray[i].getDieState() == 2) {
					moonCount++;
				}
				else if (this.diceArray[i].getDieState() == 3) {
					if (i != 3) {
						triangleCount++;
					}
				}
				else if (this.diceArray[i].getDieState() == 4) {
					boltCount++;
				}
				else if (this.diceArray[i].getDieState() == 6) {
					starCount++;
				}
			}

			if (moonCount == 3 || (moonCount == 2 && this.diceArray[2].getDieState() == 3)) {
				if (this.diceArray[diceId].getDieState() == 2) {
					return false;
				}
			}
			else if (triangleCount == 3 || (triangleCount == 2 && this.diceArray[2].getDieState() == 3)) {
				if (this.diceArray[diceId].getDieState() == 3) {
					return false;
				}
			}
			else if (boltCount == 3 || (boltCount == 2 && this.diceArray[2].getDieState() == 3)) {
				if (this.diceArray[diceId].getDieState() == 4) {
					return false;
				}
			}
			else if (starCount == 3 || (starCount == 2 && this.diceArray[2].getDieState() == 3)) {
				if (this.diceArray[diceId].getDieState() == 6) {
					if(this.diceArray[diceId].getDieState() == 5 ||
							this.diceArray[diceId].getDieState() == 1){
						this.diceArray[diceId].setCanReroll(false);
					}else this.diceArray[diceId].setCanReroll(true);
					return false;
				}
			}
			else if (this.diceArray[diceId].getDieState() == 1 ||
					this.diceArray[diceId].getDieState() == 5) {
				return false;
			}
			else {
				return true;
			}
		}
		else if (this.diceArray[diceId].getDieState() == 1 ||
				this.diceArray[diceId].getDieState() == 5) {
			return false;
		}
		return true;
	}

	/**
	 * check2Dice method returns a boolean value
	 * @param die1 - id of first die
	 * @param die2 - id of second die
	 * @return     - boolean value
	 */
	public boolean check2Dice(int die1, int die2) {
		if (isFlash) {
			int tenCount = 0;
			int fiveCount = 0;
			int moonCount = 0;
			int triangleCount = 0;
			int starCount = 0;
			int boltCount = 0;

			for (int i = 0; i < this.diceArray.length; i++) {
				if (this.diceArray[i].getDieState() == 1) {
					tenCount++;
				}
				else if (this.diceArray[i].getDieState() == 5) {
					fiveCount++;
				}
				else if (this.diceArray[i].getDieState() == 2) {
					moonCount++;
				}
				else if (this.diceArray[i].getDieState() == 3) {
					if (i != 3) {
						triangleCount++;
					}
				}
				else if (this.diceArray[i].getDieState() == 4) {
					boltCount++;
				}
				else if (this.diceArray[i].getDieState() == 6) {
					starCount++;
				}
			}
			if (moonCount == 3 || (moonCount == 2 && this.diceArray[2].getDieState() == 3)) {
				if (this.diceArray[die1].getDieState() == 2 ||
						this.diceArray[die2].getDieState() == 2) {
					return false;
				}
			}
			else if (triangleCount == 3 || (triangleCount == 2 &&
                    this.diceArray[2].getDieState() == 3)) {
				if (this.diceArray[die1].getDieState() == 3 ||
						this.diceArray[die2].getDieState() == 3) {
					return false;
				}
			}
			else if (boltCount == 3 || (boltCount == 2 &&
                    this.diceArray[2].getDieState() == 3)) {
				if (this.diceArray[die1].getDieState() == 4 ||
						this.diceArray[die2].getDieState() == 4) {
				}
			}
			else if (starCount == 3 || (starCount == 2 &&
                    this.diceArray[2].getDieState() == 3)) {
				if (this.diceArray[die1].getDieState() == 6 ||
						this.diceArray[die2].getDieState() == 6) {
					return false;
				}
			}
			else if (this.diceArray[die1].getDieState() == 1 ||
					this.diceArray[die1].getDieState() == 5 ||
					this.diceArray[die2].getDieState() == 1 ||
					this.diceArray[die2].getDieState() == 5) {
				return false;
			}
			return true;
		}
		else if (this.diceArray[die1].getDieState() == 1 ||
				this.diceArray[die1].getDieState() == 5 ||
				this.diceArray[die2].getDieState() == 1 ||
				this.diceArray[die2].getDieState() == 5) {
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * check3Dice method returns boolean value
	 * @param die1 - id of first die
	 * @param die2 - id of second die
	 * @param die3 - id of third die
	 * @return     - boolean value
	 */
	public boolean check3Dice(int die1, int die2, int die3) {
		if (isFlash) {
			int tenCount = 0;
			int fiveCount = 0;
			int moonCount = 0;
			int triangleCount = 0;
			int starCount = 0;
			int boltCount = 0;

			for (int i = 0; i < this.diceArray.length; i++) {
				if (this.diceArray[i].getDieState() == 1) {
					tenCount++;
				}
				else if (this.diceArray[i].getDieState() == 5) {
					fiveCount++;
				}
				else if (this.diceArray[i].getDieState() == 2) {
					moonCount++;
				}
				else if (this.diceArray[i].getDieState() == 3) {
					if (i != 3) {
						triangleCount++;
					}
				}
				else if (this.diceArray[i].getDieState() == 4) {
					boltCount++;
				}
				else if (this.diceArray[i].getDieState() == 6) {
					starCount++;
				}
			}
			if (moonCount == 3 || (moonCount == 2 && this.diceArray[2].getDieState() == 3)) {
				if (this.diceArray[die1].getDieState() == 2 ||
						this.diceArray[die2].getDieState() == 2 ||
						this.diceArray[die3].getDieState() == 2) {
					return false;
				}
			}
			else if (triangleCount == 3 || (triangleCount == 2 && this.diceArray[2].getDieState() == 3)) {
				if (this.diceArray[die1].getDieState() == 3 ||
						this.diceArray[die2].getDieState() == 3 ||
						this.diceArray[die3].getDieState() == 3) {
                    this.diceArray[die1].setCanReroll(true);
                    this.diceArray[die2].setCanReroll(true);
                    this.diceArray[die3].setCanReroll(true);
					return false;
				}
			}
			else if (boltCount == 3 || (boltCount == 2 && this.diceArray[2].getDieState() == 3)) {
				if (this.diceArray[die1].getDieState() == 4 ||
						this.diceArray[die2].getDieState() == 4 ||
						this.diceArray[die3].getDieState() == 4) {
					return false;
				}
			}
			else if (starCount == 3 || (starCount == 2 && this.diceArray[2].getDieState() == 3)) {
				if (this.diceArray[die1].getDieState() == 6 ||
						this.diceArray[die2].getDieState() == 6 ||
						this.diceArray[die3].getDieState() == 6) {
					return false;
				}
			}
			else if (this.diceArray[die1].getDieState() == 1 ||
					this.diceArray[die1].getDieState() == 5 ||
					this.diceArray[die2].getDieState() == 1 ||
					this.diceArray[die2].getDieState() == 5 ||
					this.diceArray[die3].getDieState() == 1 ||
					this.diceArray[die3].getDieState() == 5) {
				return false;
			}
			else {
				return true;
			}
		} else if (this.diceArray[die1].getDieState() == 1 ||
				this.diceArray[die1].getDieState() == 5 ||
				this.diceArray[die2].getDieState() == 1 ||
				this.diceArray[die2].getDieState() == 5 ||
				this.diceArray[die3].getDieState() == 1 ||
				this.diceArray[die3].getDieState() == 5) {
            return false;
		}
		else {
			return true;
		}
		return false;
	}

	/**
	 * check4Dice method returns a boolean value
	 * @param die1  - id of first die
	 * @param die2  - id of second die
	 * @param die3  - id of third die
	 * @param die4  - id of fourth die
	 * @return      - boolean value
	 */
	public boolean check4Dice(int die1, int die2, int die3, int die4) {
		if (isFlash) {
			int tenCount = 0;
			int fiveCount = 0;
			int moonCount = 0;
			int triangleCount = 0;
			int starCount = 0;
			int boltCount = 0;

			for (int i = 0; i < this.diceArray.length; i++) {
				if (this.diceArray[i].getDieState() == 1) {
					tenCount++;
				}
				else if (this.diceArray[i].getDieState() == 5) {
					fiveCount++;
				}
				else if (this.diceArray[i].getDieState() == 2) {
					moonCount++;
				}
				else if (this.diceArray[i].getDieState() == 3) {
					if (i != 3) {
						triangleCount++;
					}
				}
				else if (this.diceArray[i].getDieState() == 4) {
					boltCount++;
				}
				else if (this.diceArray[i].getDieState() == 6) {
					starCount++;
				}
			}
			if (moonCount == 3 || (moonCount == 2 && this.diceArray[2].getDieState() == 3)) {
				if (this.diceArray[die1].getDieState() == 2 ||
						this.diceArray[die2].getDieState() == 2 ||
						this.diceArray[die3].getDieState() == 2 ||
						this.diceArray[die4].getDieState() == 2) {
					return false;
				}
			}
			else if (triangleCount == 3 || (triangleCount == 2 && this.diceArray[2].getDieState() == 3)) {
				if (this.diceArray[die1].getDieState() == 3 ||
						this.diceArray[die2].getDieState() == 3 ||
						this.diceArray[die3].getDieState() == 3 ||
						this.diceArray[die4].getDieState() == 3) {
					return false;
				}
			}
			else if (boltCount == 3 || (boltCount == 2 && this.diceArray[2].getDieState() == 3)) {
				if (this.diceArray[die1].getDieState() == 4 ||
						this.diceArray[die2].getDieState() == 4 ||
						this.diceArray[die3].getDieState() == 4 ||
						this.diceArray[die4].getDieState() == 4) {
					return false;
				}
			}
			else if (starCount == 3 || (starCount == 2 && this.diceArray[2].getDieState() == 3)) {
				if (this.diceArray[die1].getDieState() == 6 ||
						this.diceArray[die2].getDieState() == 6 ||
						this.diceArray[die3].getDieState() == 6 ||
						this.diceArray[die4].getDieState() == 6) {
					return false;
				}
			}
			else if (this.diceArray[die1].getDieState() == 1 ||
					this.diceArray[die1].getDieState() == 5 ||
					this.diceArray[die2].getDieState() == 1 ||
					this.diceArray[die2].getDieState() == 5 ||
					this.diceArray[die3].getDieState() == 1 ||
					this.diceArray[die3].getDieState() == 5 ||
					this.diceArray[die4].getDieState() == 1 ||
					this.diceArray[die4].getDieState() == 5) {
				return false;
			}
			else {
				return true;
			}
		}
		else if (this.diceArray[die1].getDieState() == 1 ||
				this.diceArray[die1].getDieState() == 5 ||
				this.diceArray[die2].getDieState() == 1 ||
				this.diceArray[die2].getDieState() == 5 ||
				this.diceArray[die3].getDieState() == 1 ||
				this.diceArray[die3].getDieState() == 5 ||
				this.diceArray[die4].getDieState() == 1 ||
				this.diceArray[die4].getDieState() == 5) {
			return false;
		}
		else {
			return true;
		}
		return false;
	}

	/**
	 * get isWimpout boolean
	 * @return isWimpout
	 */
	public boolean getIsWimpout(){
	    return this.isWimpout;
    }

	/**
	 * get die1ReRoll boolean
	 * @return die1ReRoll
	 */
	public boolean isDie1ReRoll() {
        return die1ReRoll;
    }

	/**
	 * get die2ReRoll boolean
	 * @return die2ReRoll
	 */
    public boolean isDie2ReRoll() {
        return die2ReRoll;
    }

	/**
	 * get die3ReRoll boolean
	 * @return die3ReRoll
	 */
    public boolean isDie3ReRoll() {
        return die3ReRoll;
    }

	/**
	 * get die4ReRoll boolean
	 * @return die4ReRoll
	 */
    public boolean isDie4ReRoll() {
        return die4ReRoll;
    }

	/**
	 * get die5ReRoll boolean
	 * @return die5ReRoll
	 */
    public boolean isDie5ReRoll() {
        return die5ReRoll;
    }

	/**
	 * flashReRoll method
	 * @return reRolls
	 */
	public boolean[] flashReRoll() {
		boolean[] reRolls = new boolean[5];
		if (isFlash) {
			int tenCount = 0;
			int fiveCount = 0;
			int moonCount = 0;
			int triangleCount = 0;
			int starCount = 0;
			int boltCount = 0;

			for (int i = 0; i < this.diceArray.length; i++) {
				if (this.diceArray[i].getDieState() == 1) {
					tenCount++;
				} else if (this.diceArray[i].getDieState() == 5) {
					fiveCount++;
				} else if (this.diceArray[i].getDieState() == 2) {
					moonCount++;
				} else if (this.diceArray[i].getDieState() == 3) {
					if (i != 2) {
						triangleCount++;
					}
				} else if (this.diceArray[i].getDieState() == 4) {
					boltCount++;
				} else if (this.diceArray[i].getDieState() == 6) {
					starCount++;
				}
			}
			if (moonCount >= 3 || (moonCount == 2 && this.diceArray[2].getDieState() == 3)) {
				int fourMoon = 0;
				for (int i = 0; i < this.diceArray.length; i++) {
					if (this.diceArray[i].getDieState() == 2) {
						if (fourMoon == 3) {
							reRolls[i] = true;
						} else {
							reRolls[i] = false;
						}
						fourMoon++;
					} else {
						if (this.diceArray[i].getDieState() == 1 ||
								this.diceArray[i].getDieState() == 5) {
							reRolls[i] = false;
						} else if (i == 2) {
							if (this.diceArray[i].getDieState() == 3 ||
									this.diceArray[i].getDieState() == 1 ||
									this.diceArray[i].getDieState() == 5) {
								reRolls[i] = false;
							} else {
								reRolls[i] = true;
							}
						}
						reRolls[i] = true;

					}
				}
			}
			if (triangleCount >= 3 || (triangleCount == 2 && this.diceArray[2].getDieState() == 3)) {
				int fourTriangle = 0;
				for (int i = 0; i < this.diceArray.length; i++) {
					if (this.diceArray[i].getDieState() == 3) {
						if (fourTriangle == 3) {
							reRolls[i] = true;
						} else {
							reRolls[i] = false;
						}
						triangleCount++;
					} else {
						if (this.diceArray[i].getDieState() == 1 ||
								this.diceArray[i].getDieState() == 5) {
							reRolls[i] = false;
						} else if (i == 2) {
							if (this.diceArray[i].getDieState() == 3 ||
									this.diceArray[i].getDieState() == 1 ||
									this.diceArray[i].getDieState() == 5) {
								reRolls[i] = false;
							} else {
								reRolls[i] = true;
							}
						}
						reRolls[i] = true;

					}
				}
			}

			if (boltCount >= 3 || (boltCount == 2 && this.diceArray[2].getDieState() == 3)) {
				int fourBolt = 0;
				for (int i = 0; i < this.diceArray.length; i++) {
					if (this.diceArray[i].getDieState() == 4) {
						if (fourBolt == 3) {
							reRolls[i] = true;
						} else {
							reRolls[i] = false;
						}
						boltCount++;
					} else {
						if (this.diceArray[i].getDieState() == 1 ||
								this.diceArray[i].getDieState() == 5) {
							reRolls[i] = false;
						} else if (i == 2) {
							if (this.diceArray[i].getDieState() == 3 ||
									this.diceArray[i].getDieState() == 1 ||
									this.diceArray[i].getDieState() == 5) {
								reRolls[i] = false;
							} else {
								reRolls[i] = true;
							}
						}
						reRolls[i] = true;

					}
				}
			}

			if (starCount >= 3 || (starCount == 2 && this.diceArray[2].getDieState() == 3)) {
				int fourStar = 0;
				for (int i = 0; i < this.diceArray.length; i++) {
					if (this.diceArray[i].getDieState() == 6) {
						if (fourStar == 3) {
							reRolls[i] = true;
						} else {
							reRolls[i] = false;
						}
						fourStar++;
					} else {
						if (this.diceArray[i].getDieState() == 1 ||
								this.diceArray[i].getDieState() == 5) {
							reRolls[i] = false;
						} else if (i == 2) {
							if (this.diceArray[i].getDieState() == 3 ||
									this.diceArray[i].getDieState() == 1 ||
									this.diceArray[i].getDieState() == 5) {
								reRolls[i] = false;
							} else {
								reRolls[i] = true;
							}
						}
						reRolls[i] = true;
					}
				}
			}

			if (tenCount >= 3 || (tenCount == 2 && this.diceArray[2].getDieState() == 3)) {
				for (int i = 0; i < this.diceArray.length; i++) {
					if (i == 2) {
						if (this.diceArray[i].getDieState() == 1) {
							reRolls[i] = false;
						} else {
							reRolls[i] = true;
						}
					} else if (this.diceArray[i].getDieState() != 1 &&
							this.diceArray[i].getDieState() != 5) {
						reRolls[i] = true;
					} else {
						reRolls[i] = false;
					}
				}
			}

			if (fiveCount >= 3 || (fiveCount == 2 && this.diceArray[2].getDieState() == 3)) {
				for (int i = 0; i < this.diceArray.length; i++) {
					if (i == 2) {
						if (this.diceArray[i].getDieState() == 5) {
							reRolls[i] = false;
						} else {
							reRolls[i] = true;
						}
					} else if (this.diceArray[i].getDieState() != 1 &&
							this.diceArray[i].getDieState() != 5) {
						reRolls[i] = true;
					} else {
						reRolls[i] = false;
					}
				}
			}

		}
		return reRolls;
	}

	/**
	 * setter method setIsFlash
	 * @param flash - holds true or false depending if there is a flash or not
	 */
	public void setIsFlash(boolean flash){
		this.isFlash = flash;
	}
}


/** External Citation
 *  Date: April 1, 2019
 *  Problem: Could not check if the int array was null
 *  and kept getting an error
 *  Resource: https://stackoverflow.com/questions/286161/
 *  how-can-i-see-if-an-element-in-an-int-array-is-empty
 *  Solution: I got the idea to check if it was 0 from
 *  this post.
 */

