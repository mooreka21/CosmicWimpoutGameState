package edu.up.cs301.cosmicwimpout;

import edu.up.cs301.game.infoMsg.GameState;
import java.util.ArrayList;
import edu.up.cs301.cosmicwimpout.Die;

/**
 * This contains the state for the Cosmic Wimpout game.
 *
 * @author Olivia Dendinger, Sam Lemly, Kayla Moore, David Campbell
 * @version March 2019
 */
public class CosmicWimpoutState extends GameState {


	//instance variables that encompass all needed information

	//to satisfy Serializable interface
	private static final long serialVersionUID = 12345678910L;

	//initializing game variables
	private int whoseTurn;
	//private int numPlayers;
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

	//initializing variables for rollSelectedDice
	private int trueCounter =0;
	private boolean die1ReRoll = false;
	private boolean die2ReRoll = false;
	private boolean die3ReRoll = false;
	private boolean die4ReRoll = false;
	private boolean die5ReRoll = false;


	private boolean isFlash = false;
	private boolean isFiveOf = false;

	//instance of previous state
	private CosmicWimpoutState prevState;

	//to determine instant winner and losers
	private boolean isSuperNova = false;
	private boolean isInstantWinner = false;


	/**
	 * CosmicWimpoutState default constructor
	 * In the test iteration, this initializes two players
	 */
	public CosmicWimpoutState(){
		whoseTurn = 0;
		//numPlayers = 3;
		this.prevState = null;
		//for loop creates dice in all diceArray indices.
		for(int i = 0; i < 5; i++){
			diceArray[i] = new Die(i);
		}
		this.playerArrayList.add(new Player(1));
		this.playerArrayList.add(new Player(2));
		this.playerArrayList.add(new Player(3));
		this.playerArrayList.add(new Player(4));

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
		this.prevState = orig.prevState;
		this.turnScore = orig.turnScore;

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
		//SUPERNOVA AND FREIGHT TRAIN CHECKING
        int tally = 0;
		if(     ourDice[0].dieState == 1 &&
				ourDice[1].dieState == 1 &&
				ourDice[2].dieState == 1 &&
				ourDice[3].dieState == 1 &&
				ourDice[4].dieState == 1){
			isSuperNova = true;
			return -1;
		}
		else if(ourDice[0].dieState == 2 &&
				ourDice[1].dieState == 2 &&
				ourDice[2].dieState == 2 &&
				ourDice[3].dieState == 2 &&
				ourDice[4].dieState == 2){
			isFiveOf = true;
			haveToReRoll = true;
			tally = tally + 200;
		}
		else if(ourDice[0].dieState == 4 &&
				ourDice[1].dieState == 4 &&
				ourDice[2].dieState == 4 &&
				ourDice[3].dieState == 4 &&
				ourDice[4].dieState == 4){
			//turnScore = turnScore + 400;
			isFiveOf = true;
			haveToReRoll = true;
            tally = tally + 400;		}
		else if(ourDice[0].dieState == 5 &&
				ourDice[1].dieState == 5 &&
				ourDice[2].dieState == 5 &&
				ourDice[3].dieState == 5 &&
				ourDice[4].dieState == 5){
			isFiveOf = true;
			haveToReRoll = true;
			// turnScore = turnScore + 500;
            tally = tally + 500;		}
		else if(ourDice[0].dieState == 6 &&
				ourDice[1].dieState == 6 &&
				ourDice[2].dieState == 6 &&
				ourDice[3].dieState == 6 &&
				ourDice[4].dieState == 6){
			this.isInstantWinner = true;
			return 0;
		}
		//END SUPERNOVA AND FREIGHT TRAIN CHECKING

		//count variables for the dice
		int halfMoonCount = 0;
		int triangleCount=0;
		int boltCount=0;
		int fiveCount =0;
		int starCount=0;
		int tenCount=0;

		//BEGIN DICE COUNTING
		for(int i =0; i < ourDice.length; i++){
			if(ourDice[i].getDieState() == 1){
				tenCount++;
				if(tenCount == 4){
					tenReRoll=i;
				}
				else if(tenCount == 2){
					tenReRoll = i+1;
				}
			}
			if(ourDice[i].getDieState() == 2){
				halfMoonCount++;
				if(halfMoonCount == 4){
					halfMoonReRoll=i;
				}
				else if(halfMoonCount == 2){
					halfMoonReRoll = i+1;
				}
			}
			if(ourDice[i].getDieState() == 3 && ourDice[i].dieID != 3){
				triangleCount++;
				if(triangleCount == 4){
					triangleReRoll=i;
				}
				else if(triangleCount == 2){
					triangleReRoll = i+1;
				}
			}
			if(ourDice[i].getDieState() == 4){
				boltCount++;
				if(boltCount == 4){
					boltReRoll=i;
				}
				else if(boltCount == 2){
					boltReRoll = i+1;
				}
			}
			if(ourDice[i].getDieState() == 5){
				fiveCount++;
				if(fiveCount == 4){
					fiveReRoll=i;
				}
				else if(fiveCount == 2){
					fiveReRoll = i+1;
				}
			}
			if(ourDice[i].getDieState() == 6){
				starCount++;
				if(starCount == 4){
					starReRoll=i;
				}
				else if(starCount == 2){
					starReRoll = i+1;
				}
			}
		}
		//END DICE COUNTING


		//BEGIN FLAMING SUN FLASH CASES
		if(tenCount == 2 && ourDice[2].dieState == 3){
			isFlash = true;
            tally = tally + 100;
            if(fiveCount != 0){
				tally = tally + (fiveCount*5);
				//I don't know if we need to set haveToReroll to true here --SL
			}
		}
		else if(starCount == 2 && ourDice[2].dieState == 3){
			isFlash = true;
            tally = tally + 60;
			if(tenCount != 0){
				if(fiveCount != 0){
					tally = tally + (fiveCount*5) + (tenCount*10);
					//I don't know if we need to set haveToReroll to true here --SL
				}
				tally = tally + (tenCount*10);
			}
			if(fiveCount != 0 && tenCount == 0){
				tally = tally + (fiveCount*5);
				//I don't know if we need to set haveToReroll to true here --SL
			}
		}
		else if(fiveCount == 2 && ourDice[2].dieState == 3){
			isFlash = true;
            tally = tally + 50;
			if(tenCount != 0){
				tally = tally + (tenCount*10);
				//I don't know if we need to set haveToReroll to true here --SL
			}
		}
		else if(boltCount == 2 && ourDice[2].dieState == 3){
			isFlash = true;
            tally = tally + 40;
			if(tenCount != 0){
				if(fiveCount != 0){
					tally = tally + (fiveCount*5) + (tenCount*10);
					//I don't know if we need to set haveToReroll to true here --SL
				}
				tally = tally + (tenCount*10);
			}
			if(fiveCount != 0 && tenCount == 0){
				tally = tally + (fiveCount*5);
				//I don't know if we need to set haveToReroll to true here --SL
			}
		}
		else if(triangleCount == 2 && ourDice[2].dieState == 3){
			isFlash = true;
            tally = tally + 30;
			if(tenCount != 0){
				if(fiveCount != 0){
					tally = tally + (fiveCount*5) + (tenCount*10);
					//I don't know if we need to set haveToReroll to true here --SL
				}
				tally = tally + (tenCount*10);
			}
			if(fiveCount != 0 && tenCount == 0){
				tally = tally + (fiveCount*5);
				//I don't know if we need to set haveToReroll to true here --SL
			}
		}
		else if(halfMoonCount == 2 && ourDice[2].dieState == 3){
			isFlash = true;
            tally = tally + 20;
			if(tenCount != 0){
				if(fiveCount != 0){
					tally = tally + (fiveCount*5) + (tenCount*10);
					//I don't know if we need to set haveToReroll to true here --SL
				}
				tally = tally + (tenCount*10);
			}
			if(fiveCount != 0 && tenCount == 0){
				tally = tally + (fiveCount*5);
				//I don't know if we need to set haveToReroll to true here --SL
			}
		}
		else if(tenCount < 2 && fiveCount < 2 && triangleCount < 2 &&
				boltCount < 2 && halfMoonCount < 2 && starCount <2 &&
				ourDice[2].dieState == 3){
			tally = tally + 10;
		}

		//END 10 & 5 COUNTING CASES
		//END FLAMING SUN FLASH CASE HANDLING


		//BEGIN NORMAL FLASH HANDLING
		else if(halfMoonCount >= 3 && halfMoonCount < 5){
			isFlash = true;
			if(halfMoonCount == 4) {
				//rollSingleDie(playerId, (halfMoonReRoll + 1));
				/**
				if(halfMoonReRoll == 4){
					rollSingleDie(playerId, 5);
				}
				else{
					rollSingleDie(playerId, halfMoonReRoll +1);
				}
				 */
				haveToReRoll = true;
			}
			tally = tally + 20;
		}
		else if(triangleCount >= 3 && triangleCount < 5){
			isFlash = true;
			if(triangleCount == 4) {
				/**
				if(triangleReRoll == 4){
					rollSingleDie(playerId, 5);
				}
				else{
					rollSingleDie(playerId, (triangleReRoll + 1));
				}
				 */
			}
			tally = tally + 30;

		}
		else if(boltCount >= 3 && boltCount < 5){
			isFlash = true;
			if(boltCount == 4){
				//rollSingleDie(playerId, (boltReRoll + 1));
				/**
				if(boltReRoll == 4){
					rollSingleDie(playerId, 5);
				}
				else{
					rollSingleDie(playerId, boltReRoll +1);
				}
				 */
				haveToReRoll = true;
			}
			tally = tally + 40;

		}
		else if(fiveCount >= 3 && fiveCount < 5){
			isFlash = true;
			//  turnScore = turnScore + 50;
			if(fiveCount == 4){
				//rollSingleDie(playerId, (fiveReRoll + 1));
				/**
				if(fiveReRoll == 4){
					rollSingleDie(playerId, 5);
				}
				else{
					rollSingleDie(playerId, fiveReRoll + 1);
				}
				 */
				haveToReRoll = true;
			}
			tally = tally + 50;
			if(tenCount != 0){
				tally = tally + (tenCount*10);
				//I don't know if we need to set haveToReroll to true here --SL
			}
		}
		else if(starCount >= 3 && starCount < 5){
			isFlash = true;
			//  turnScore = turnScore + 60;
			if(starCount == 4){
				//rollSingleDie(playerId, (starReRoll + 1));
				/**
				if(starReRoll == 4){
					rollSingleDie(playerId, 5);
				}
				else{
					rollSingleDie(playerId, starReRoll + 1);
				}
				 */
				haveToReRoll = true;
			}
			tally =  tally + 60;
		}
		else if(tenCount >= 3 && tenCount < 5){
			isFlash = true;
			// turnScore = turnScore + 100;
			if(tenCount == 4){
				//rollSingleDie(playerId, (tenReRoll + 1));
				/**
				if(tenReRoll == 4){
					rollSingleDie(playerId, 5);
				}
				else{
					rollSingleDie(playerId, tenReRoll + 1);
				}
				 */
				haveToReRoll = true;
			}
			tally = tally + 100;

			if(fiveCount != 0){
				tally = tally + (fiveCount*5);
				//I don't know if we need to set haveToReroll to true here --SL
			}
		}
		else if(tenCount != 0){
			if(fiveCount != 0){
				tally = tally + (fiveCount*5) + (tenCount*10);
				//I don't know if we need to set haveToReroll to true here --SL
			}
			else{
				tally = tally + (tenCount*10);
			}
		}
		else if(fiveCount != 0 && tenCount == 0){
			tally = tally + (fiveCount*5);
			//I don't know if we need to set haveToReroll to true here --SL
		}
		//END NORMAL FLASH HANDLING


		//BEGIN ONLY FLAMING SUN CASE
		//if(tenCount == 0 && fiveCount == 0 && ourDice[2].dieState == 3){
		//	tally = tally + 10;
		//}
		//END ONLY FLAMING SUN CASE

		//BEGIN WIMPOUT CASE
		if(fiveCount == 0 && tenCount == 0 && ourDice[2].dieState != 3) {
			return -1;
		}
		//END WIMPOUT CASE

		return tally;
	}

	/**
	 * Getter for any die's current dice state
	 * @param dieId - the die to recieve its state
	 * @return - the String value of the dice state
	 */
	public String getDiceVal(int dieId){
		return this.diceArray[dieId].getValAsString();
	}

	/**
	 * getter for whose turn it currently is in the state
	 * @return whose turn
	 */
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
			int currentScore = playerArrayList.get(playerId).getPlayerScore();
			if(playerId == 0){
				playerArrayList.get(playerId).setPlayerScore(currentScore + turnScore);
				whoseTurn = 1;
				turnScore = 0; //reset turn score to 0 for next player
			}
			else if(playerId == 1){
				playerArrayList.get(playerId).setPlayerScore(currentScore + turnScore);
				whoseTurn = 0;
				turnScore = 0;// reset turn score to 0
			}
			else if(playerId == 3){
				playerArrayList.get(playerId - 1).setPlayerScore(currentScore + turnScore);
				whoseTurn = 1;
				turnScore = 0; //reset turnScore to 0
			}
			this.prevState = new CosmicWimpoutState(this);
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
			System.exit(0);
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
				//not a wimpout add score to current turn score
				this.turnScore = this.turnScore + totalDiceScore(diceArray,playerId);
			}
			else{
				//wimpout set turn score to 0 and
				//and change players
				turnScore = 0;
				if(playerId == 0 ){

					setWhoseTurn(1);
				}
				if(playerId == 1){
					setWhoseTurn(0);
				}
			}
			return true;
		}
		else{
			// illegal move
			return false;
		}
	}

	/**
	 * Rolls a single die
	 * @param playerId - which player wants to roll
	 * @param id - the dice id they want to roll
	 * @return - if it was a legal move or not
	 */
	public boolean rollSingleDie(int playerId, int id ){
		if(playerId == whoseTurn) {
			diceArray[id-1].rollMe();
			return true;
		}
		else{
			// illegal move
			return false;
		}
	}

	/**
	 * diceScoreForOneDice - calculates score if
	 * player only selects one die to roll
	 * @param ourDice - dice array
	 * @param playerId - which player
	 * @param diceID - which die they want to roll
	 * @return
	 */
	public int diceScoreForOneDice(Die[] ourDice, int playerId, int diceID){
		int diceState = ourDice[diceID-1].dieState;
		if(diceID == 3){
			if(diceState == 3){
				return 10;
			}
			 //flashing sun
		}
		if(ourDice[diceID-1].dieState == 1){
			//die state is 10
			return 10;
		}
		else if(ourDice[diceID-1].dieState == 5){
			//die state is 5
			return 5;
		}
		return -1;
	}

	/**
	 * rollSelectedDice - action method for when the player
	 * sends an action of roll selected dice
	 * @param playerId
	 * @param dice1 - true if they select
	 * @param dice2 - true if they select
	 * @param dice3
	 * @param dice4
	 * @param dice5
	 * @return
	 */
	public boolean rollSelectedDice
			(int playerId, boolean dice1, boolean dice2, boolean dice3, boolean dice4, boolean dice5){
		if(playerId == whoseTurn) {
			int currentScore = playerArrayList.get(playerId).getPlayerScore();
			if(dice1){
				//roll it and get the state
				rollSingleDie(playerId, 1);
				trueCounter++;// to decide how to score the dice
				die1ReRoll = true;
			}
			else{
				//player didnt select this die
				die1ReRoll =false;
			}

			if(dice2){
				rollSingleDie(playerId, 2);
				trueCounter++;
				die2ReRoll = true;
			}
			else{
				//player didnt select this die
				die2ReRoll =false;
			}
			if(dice3){
				rollSingleDie(playerId, 3);
				trueCounter++;
				die3ReRoll = true;
			}
			else{
				//player didnt select this die
				die3ReRoll =false;
			}
			if(dice4){
				rollSingleDie(playerId, 4);
				trueCounter++;
				die4ReRoll = true;
			}
			else{
				//player didnt select this die
				die4ReRoll =false;
			}
			if(dice5){
				rollSingleDie(playerId, 5);
				trueCounter++;
				die5ReRoll = true;
			}
			else{
				//player didnt select this die
				die5ReRoll =false;
			}

			//only selected one die
			if(trueCounter == 1){
				//get which dice they rolled
				int whichDie = whichDice();
				//score that dice
				int score = diceScoreForOneDice(diceArray,playerId, whichDie);
				if(score != -1) {
					turnScore = turnScore + score;
					trueCounter=0;
					return true;
				}
				else{
					//wimpout end turn
					turnScore = 0;
					trueCounter = 0;
					endTurn(playerId);
					return true;
				}
			}
			if(trueCounter == 2){
				int[] whichDice = whichDice2();
				int first = (int)whichDice[0];
				int second = (int)whichDice[1];
				int score = getDiceScore2(diceArray,first,second);
				if(score != -1) {
					turnScore = turnScore + score;
					trueCounter=0;
					return true;
				}
				else{
					turnScore = 0;
					trueCounter = 0;
					endTurn(playerId);
					return true;
				}
			}
			if(trueCounter == 3){
				int[] whichDice = whichDice3();
				int first = whichDice[0];
				int second = whichDice[1];
				int third = whichDice[2];

				int score = getScore3Dice(diceArray, first, second, third);
				if(score != -1) {
					turnScore = turnScore + score;
					trueCounter=0;
					return true;
				}
				else{
					turnScore = 0;
					trueCounter = 0;
					endTurn(playerId);
					return true;
				}
			}
			if(trueCounter == 4){
				int[]whichDice = whichDice4();
				int first = whichDice[0];
				int second = whichDice[1];
				int third = whichDice[2];
				int fourth = whichDice[3];

				int score = getScore4Dice(diceArray, first, second, third, fourth);
				if(score != -1) {
					turnScore = turnScore + score;
					trueCounter=0;
					return true;
				}
				else{
					turnScore = 0;
					trueCounter = 0;
					endTurn(playerId);
					return true;
				}
			}
			if(trueCounter == 5){
				rollAllDice(playerId);
				this.turnScore = this.turnScore;
				trueCounter = 0;
				return true;
				/**
				int score = totalDiceScore(diceArray, playerId);
				if(score != -1) {
					this.turnScore = this.turnScore;
					turnScore = turnScore + score;
					trueCounter=0;
					return true;
				}
				else{
					turnScore = 0;
					trueCounter = 0;
					endTurn(playerId);
					return true;
				}
				 */
			}

		}
		trueCounter = 0;
		return false;
	}

	/**
	 * calculates score for when they select
	 * two die
	 * @param ourDice - dice array
	 * @param one - die one they select
	 * @param two - die two they select
	 * @return
	 */
	public int getDiceScore2(Die[] ourDice, int one, int two){
		int tally = 0;
		boolean notWimp = false;

        if(one == 3 || two == 3){
            if(ourDice[3].dieState == 3){
                tally = 10;
				notWimp = true;
            }
        }
		if(ourDice[one-1].dieState == 1){
			tally = tally + 10;
			notWimp = true;
		}
		else if(ourDice[one-1].dieState == 5){
			tally = tally + 5;
			notWimp = true;
		}

		if(ourDice[two-1].dieState == 1){
			tally = tally + 10;
			notWimp = true;
		}
		else if(ourDice[two-1].dieState == 5){
			tally = tally + 5;
			notWimp = true;
		}

		if(notWimp){
			return tally;
		}
		else {
			return -1;
		}

	}

	/**
	 * get player ones current score
	 * @return
	 */
	public int getPlayer1Score(){
		return this.playerArrayList.get(0).getPlayerScore();
	}

	/**
	 * get player twos current score
	 * @return
	 */
	public int getPlayer2Score(){
		return this.playerArrayList.get(1).getPlayerScore();
	}

	public int getPlayer3Score(){
		return this.playerArrayList.get(2).getPlayerScore();
	}

	public int getPlayer4Score(){
		return this.playerArrayList.get(3).getPlayerScore();
	}

	/**
	 * set whose turn it is
	 * @param player
	 */
	private void setWhoseTurn(int player){
		this.whoseTurn = player;
	}

	/**
	 * finds which dice they selected
	 * @return - die id they select
	 */
	private int whichDice(){
		if(die1ReRoll){
			return 1;
		}
		else if(die2ReRoll){
			return 2;
		}
		else if(die3ReRoll){
			return 3;
		}
		else if(die4ReRoll){
			return 4;
		}
		else if(die5ReRoll){
			return 5;
		}
		return 0;
	}
	/**
	 * finds which dice they selected
	 * @return - dice id they select in an array
	 */
	private int[] whichDice2(){
		int[] twoDice = new int[2];
		if(die1ReRoll){
			twoDice[0] = 1;
		}
		if(die2ReRoll){
			if(twoDice[0] == 0){
				twoDice[0] = 2;
			}
				twoDice[1] = 2;
		}
		if(die3ReRoll){
			if(twoDice[0] == 0){
				twoDice[0] = 3;
			}
				twoDice[1] = 3;

		}
		if(die4ReRoll){
			if(twoDice[0] == 0){
				twoDice[0] = 4;
			}
				twoDice[1] = 4;

		}
		if(die5ReRoll){
			if(twoDice[0] == 0){
				twoDice[0] = 5;
			}
				twoDice[1] = 5;
		}
		return (twoDice);
	}
	/**
	 * finds which dice they selected
	 * @return - die id they select in an array
	 */
	private int[] whichDice3() {
		int[] threeDice = new int[3];
		if(die1ReRoll){
			threeDice[0] = 1;
		}
		if(die2ReRoll){
			if(threeDice[0] == 0){
				threeDice[0] = 2;
			}
			threeDice[1] = 2;
		}
		if(die3ReRoll){
			if(threeDice[0] == 0){
				threeDice[0] = 3;
			}
			else if(threeDice[1] == 0) {
				threeDice[1] = 3;
			}
			threeDice[2] = 3;
		}
		if(die4ReRoll){
			if(threeDice[0] == 0){
				threeDice[0] = 4;
			}
			else if(threeDice[1] == 0) {
				threeDice[1] = 4;
			}
			threeDice[2] = 4;
		}
		if(die5ReRoll){
			if(threeDice[0] == 0){
				threeDice[0] = 5;
			}
			else if(threeDice[1] == 0) {
				threeDice[1] = 5;
			}
			threeDice[2] = 5;
		}
		return threeDice;
	}

	private int[] whichDice4(){
		int[] fourDice = new int[4];
		if(die1ReRoll){
			fourDice[0] = 1;
		}
		if(die2ReRoll){
			if(fourDice[0] == 0){
				fourDice[0] = 2;
			}
			fourDice[1] = 2;
		}
		if(die3ReRoll){
			if(fourDice[0] == 0){
				fourDice[0] = 3;
			}
			else if(fourDice[1] ==0){
				fourDice[1] = 3;
			}
			fourDice[2] = 3;
		}
		if(die4ReRoll){
			if(fourDice[1] == 0){
				fourDice[1] = 4;
			}
			else if(fourDice[2] ==0){
				fourDice[2] = 4;
			}
			fourDice[3] = 4;
		}
		if(die5ReRoll){
			if(fourDice[1] == 0){
				fourDice[1] = 5;
			}
			else if(fourDice[2] ==0){
				fourDice[2] = 5;
			}
			fourDice[3] = 5;
		}
		return fourDice;
	}
	/**
	 * finds which dice they selected
	 * @return - die id they select in an array
	 */
	private int getScore4Dice(Die[] ourDice, int one, int two, int three, int four){
		int score = 0;
		boolean notWimp = false;
		int tenCount = 0;
		int moonCount =0;
		int triangleCount = 0;
		int boltCount=0;
		int fiveCount = 0;
		int starCount =0;

		if(ourDice[one-1].getDieState() == 1){
			tenCount++;
		} else if (ourDice[one-1].getDieState() == 2) {
		moonCount++;
		} else if (ourDice[one-1].getDieState() == 3 && (one-1) != 2) {
		triangleCount++;
		} else if (ourDice[one-1].getDieState() == 4) {
		boltCount++;
		} else if (ourDice[one-1].getDieState() == 5) {
		fiveCount++;
		} else if (ourDice[one-1].getDieState() == 6) {
		starCount++;
		}
		if(ourDice[two-1].getDieState() == 1){
			tenCount++;
		} else if (ourDice[two-1].getDieState() == 2) {
			moonCount++;
		} else if (ourDice[two-1].getDieState() == 3 && (two-1) != 2) {
			triangleCount++;
		} else if (ourDice[two-1].getDieState() == 4) {
			boltCount++;
		} else if (ourDice[two-1].getDieState() == 5) {
			fiveCount++;
		} else if (ourDice[two-1].getDieState() == 6) {
			starCount++;
		}
		if(ourDice[three-1].getDieState() == 1){
			tenCount++;
		} else if (ourDice[three-1].getDieState() == 2) {
			moonCount++;
		} else if (ourDice[three-1].getDieState() == 3 && (three-1) != 2) {
			triangleCount++;
		} else if (ourDice[three-1].getDieState() == 4) {
			boltCount++;
		} else if (ourDice[three-1].getDieState() == 5) {
			fiveCount++;
		} else if (ourDice[three-1].getDieState() == 6) {
			starCount++;
		}

		if(ourDice[four-1].getDieState() == 1){
			tenCount++;
		} else if (ourDice[four-1].getDieState() == 2) {
			moonCount++;
		} else if (ourDice[four-1].getDieState() == 3 && (four-1) != 2) {
			triangleCount++;
		} else if (ourDice[four-1].getDieState() == 4) {
			boltCount++;
		} else if (ourDice[four-1].getDieState() == 5) {
			fiveCount++;
		} else if (ourDice[four-1].getDieState() == 6) {
			starCount++;
		}

			//flash cases
			if(tenCount >= 3) {
				score = 100;
				notWimp = true;
			}
			else if(moonCount >= 3){
				score = 20;
				notWimp = true;
			}
			else if(triangleCount >= 3){
				score = 30;
				notWimp = true;
			}
			else if(boltCount >= 3){
				score = 40;
				notWimp = true;
			}
			else if(fiveCount >= 3){
				score = 50;
				notWimp = true;
			}
			else if(starCount >= 3){
				score =60;
				notWimp = true;
			}

			if(tenCount == 2 && (one == 3 || two == 3 || three == 3 || four == 3)){
				if(ourDice[2].dieState == 3) {
					score = score + 100;
					notWimp = true;
				}
			}
			else if(tenCount == 2){
				score = score + 20;
				notWimp = true;
			}

			if(moonCount == 2 && (one == 3 || two == 3 || three == 3 || four == 3)){
				if(ourDice[2].dieState == 3) {
					score = score + 20;
					notWimp = true;
				}
			}
			 if(triangleCount == 2 && (one == 3 || two == 3 || three == 3 || four == 3)){
				if(ourDice[2].dieState == 3) {
					score = score + 30;
					notWimp = true;
				}
			}
			if(boltCount == 2 && (one == 3 || two == 3 || three == 3 || four == 3)){
				if(ourDice[2].dieState == 3) {
					score = score + 40;
					notWimp = true;
				}
			}
			if(fiveCount == 2 && (one == 3 || two == 3 || three == 3 || four == 3)){
				if(ourDice[2].dieState == 3) {
					score = score + 50;
					notWimp = true;
				}
			}
			else if(fiveCount == 2){
				score = score + 10;
				notWimp = true;
			}

			if(starCount == 2 && (one == 3 || two == 3 || three == 3 || four == 3)){
				if(ourDice[2].dieState == 3) {
					score = score + 60;
					notWimp = true;
				}
			}

			if(tenCount == 1){
				score = score + 10;
				notWimp = true;
			}

			if(fiveCount == 1){
				score = score + 5;
				notWimp = true;
			}

			if(one == 3 || two == 3 || three == 3 || four == 3 && tenCount== 0
			&& fiveCount == 0){
                if(ourDice[2].dieState == 3){
                    score = score + 10;
                    notWimp = true;
                }
            }


		if(notWimp){
			return score;
		}
		else {
			return -1;
		}
	}

	/**
	 * calculates the score when they select
	 *  3 dice
	 * @param ourDice
	 * @param one - first dice id
	 * @param two - second dice id
	 * @param three - third dice id
	 * @return
	 */
	private int getScore3Dice(Die[] ourDice, int one, int two, int three){
		int score = 0;
		boolean notWimp = false;

		//Flash Cases
		if(ourDice[one - 1].dieState == 1 && ourDice[two-1].dieState == 1 &&
			ourDice[three-1].dieState == 1){
			score = 100;
			notWimp = true;
		}
		else if(ourDice[one - 1].dieState == 2 && ourDice[two-1].dieState == 2 &&
				ourDice[three-1].dieState == 2){
			score = 20;
			notWimp = true;
		}
		else if(ourDice[one - 1].dieState == 3 && ourDice[two-1].dieState == 3 &&
				ourDice[three-1].dieState == 3){
			score = 30;
			notWimp = true;
		}
		else if(ourDice[one - 1].dieState == 4 && ourDice[two-1].dieState == 4 &&
				ourDice[three-1].dieState == 4){
			score = 40;
			notWimp = true;
		}
		else if(ourDice[one - 1].dieState == 5 && ourDice[two-1].dieState == 5 &&
				ourDice[three-1].dieState == 5){
			score = 50;
			notWimp = true;
		}
		else if(ourDice[one - 1].dieState == 6 && ourDice[two-1].dieState == 6 &&
				ourDice[three-1].dieState == 6){
			score = 60;
			notWimp = true;
		}
		else if(flashWithSun(ourDice, one, two, three) != -1){
			//gets score if there is a flash with flaming sun
			score = flashWithSun(ourDice, one, two, three);
			notWimp = true;
		}
		else {
			if (one == 3 || two == 3 || three == 3) {
				if (ourDice[3].dieState == 3) {
					score = score + 10;
					notWimp = true;
				}
			}

			if (ourDice[one - 1].dieState == 1) {
				score = score + 10;
				notWimp = true;
			} else if (ourDice[one - 1].dieState == 5) {
				score = score + 5;
				notWimp = true;
			}

			if (ourDice[two - 1].dieState == 1) {
				score = score + 10;
				notWimp = true;
			} else if (ourDice[two - 1].dieState == 5) {
				score = score + 5;
				notWimp = true;
			}

			if (ourDice[three - 1].dieState == 1) {
				score = score + 10;
				notWimp = true;
			} else if (ourDice[three - 1].dieState == 5) {
				score = score + 5;
				notWimp = true;
			}
		}
		if(notWimp){
			return score;
		}
		else {
			return -1;
		}
	}

	/**
	 * helper method to score flashes with flaming suns
	 * @param ourDice
	 * @param one
	 * @param two
	 * @param three
	 * @return
	 */
	private int flashWithSun(Die[] ourDice, int one, int two, int three){
		int score = 0;
		if(one == 3){
			if(ourDice[one-1].dieState == 3){
				if(ourDice[two-1].dieState == 1 && ourDice[three-1].dieState == 1){
					score = 100;
				}
				else if(ourDice[two-1].dieState == 2 && ourDice[three-1].dieState == 2){
					score = 20;
				}
				else if(ourDice[two-1].dieState == 3 && ourDice[three-1].dieState == 3){
					score = 30;
				}
				else if(ourDice[two-1].dieState == 4 && ourDice[three-1].dieState == 4){
					score = 40;
				}
				else if(ourDice[two-1].dieState == 5 && ourDice[three-1].dieState == 5){
					score = 50;
				}
				else if(ourDice[two-1].dieState == 6 && ourDice[three-1].dieState == 6){
					score = 60;
				}
				else{
					score = -1;
				}
			}
			else{
				score = -1;
			}
		}
		else if(two == 3){
			if(ourDice[two-1].dieState == 3){
				if(ourDice[one-1].dieState == 1 && ourDice[three-1].dieState == 1){
					score = 100;
				}
				else if(ourDice[one-1].dieState == 2 && ourDice[three-1].dieState == 2){
					score = 20;
				}
				else if(ourDice[one-1].dieState == 3 && ourDice[three-1].dieState == 3){
					score = 30;
				}
				else if(ourDice[one-1].dieState == 4 && ourDice[three-1].dieState == 4){
					score = 40;
				}
				else if(ourDice[one-1].dieState == 5 && ourDice[three-1].dieState == 5){
					score = 50;
				}
				else if(ourDice[one-1].dieState == 6 && ourDice[three-1].dieState == 6){
					score = 60;
				}
				else{
					score = -1;
				}
			}
			else{
				score = -1;
			}
		}
		else if(three == 3){
			if(ourDice[three-1].dieState == 3){
				if(ourDice[two-1].dieState == 1 && ourDice[one-1].dieState == 1){
					score = 100;
				}
				else if(ourDice[two-1].dieState == 2 && ourDice[one-1].dieState == 2){
					score = 20;
				}
				else if(ourDice[two-1].dieState == 3 && ourDice[one-1].dieState == 3){
					score = 30;
				}
				else if(ourDice[two-1].dieState == 4 && ourDice[one-1].dieState == 4){
					score = 40;
				}
				else if(ourDice[two-1].dieState == 5 && ourDice[one-1].dieState == 5){
					score = 50;
				}
				else if(ourDice[two-1].dieState == 6 && ourDice[one-1].dieState == 6){
					score = 60;
				}
				else{
					score = -1;
				}
			}
			else{
				score = -1;
			}
		}
		else{
			score = -1;
		}
		return score;
	}

	/**
	 * get turn score
	 * @return
	 */
	public int getTurnScore() {return this.turnScore; }

	/**
	 * get isSuperNova boolean
	 * @return
	 */
	public boolean getIsSuperNova() { return this.isSuperNova; }

	/**
	 * get isInstantWinner boolean
	 * @return
	 */
	public boolean getIsInstantWinner() {return this.isInstantWinner; }

	public Die[] getDiceArray() {
		return diceArray;
	}

	public boolean getIsFlash(){return isFlash;}
	public boolean getIsFiveOf(){return isFiveOf;}
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

