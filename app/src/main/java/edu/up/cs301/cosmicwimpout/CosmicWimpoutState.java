package edu.up.cs301.cosmicwimpout;

import edu.up.cs301.game.infoMsg.GameState;
import java.util.ArrayList;
import edu.up.cs301.cosmicwimpout.Die;

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

	private int trueCounter =0;
	private boolean die1ReRoll = false;
	private boolean die2ReRoll = false;
	private boolean die3ReRoll = false;
	private boolean die4ReRoll = false;
	private boolean die5ReRoll = false;

	private CosmicWimpoutState prevState;

	private boolean isSuperNova = false;

	/**
	 * CosmicWimpoutState default constructor
	 * In the test iteration, this initializes three players
	 */
	public CosmicWimpoutState(){
		whoseTurn = 0;
		numPlayers = 3;
		this.prevState = null;
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
			haveToReRoll = true;
			return 200;
		}
		else if(ourDice[0].dieState == 4 &&
				ourDice[1].dieState == 4 &&
				ourDice[2].dieState == 4 &&
				ourDice[3].dieState == 4 &&
				ourDice[4].dieState == 4){
			//turnScore = turnScore + 400;
			haveToReRoll = true;
			return 400;
		}
		else if(ourDice[0].dieState == 5 &&
				ourDice[1].dieState == 5 &&
				ourDice[2].dieState == 5 &&
				ourDice[3].dieState == 5 &&
				ourDice[4].dieState == 5){
			haveToReRoll = true;
			// turnScore = turnScore + 500;
			return 500;
		}
		else if(ourDice[0].dieState == 6 &&
				ourDice[1].dieState == 6 &&
				ourDice[2].dieState == 6 &&
				ourDice[3].dieState == 6 &&
				ourDice[4].dieState == 6){
			//Instant Winner
			//Some action leading to the game ending happens here
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
			if(ourDice[i].getDieState() == 3 && ourDice[i].dieID != 5){
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
		if(tenCount == 2 && ourDice[4].dieState == 3){
			return 100;
		}
		else if(starCount == 2 && ourDice[4].dieState == 3){
			return 60;
		}
		else if(fiveCount == 2 && ourDice[4].dieState == 3){
			return 50;
		}
		else if(boltCount == 2 && ourDice[4].dieState == 3){
			return 40;
		}
		else if(triangleCount == 2 && ourDice[4].dieState == 3){
			return 30;
		}
		else if(halfMoonCount == 2 && ourDice[4].dieState == 3){
			return 20;
		}
		//END FLAMING SUN FLASH CASE HANDLING


		//BEGIN NORMAL FLASH HANDLING
		if(halfMoonCount >= 3 && halfMoonCount < 5){
			if(halfMoonCount == 4) {
				//rollSingleDie(playerId, (halfMoonReRoll + 1));
				if(halfMoonReRoll == 4){
					rollSingleDie(playerId, 5);
				}
				else{
					rollSingleDie(playerId, halfMoonReRoll +1);
				}
				haveToReRoll = true;
			}
			return 20;
		}
		if(triangleCount >= 3 && triangleCount < 5){
			if(triangleCount == 4) {
				if(triangleReRoll == 4){
					rollSingleDie(playerId, 5);
				}
				else{
					rollSingleDie(playerId, (triangleReRoll + 1));
				}
			}
			return 30;
		}
		if(boltCount >= 3 && boltCount < 5){
			if(boltCount == 4){
				//rollSingleDie(playerId, (boltReRoll + 1));
				if(boltReRoll == 4){
					rollSingleDie(playerId, 5);
				}
				else{
					rollSingleDie(playerId, boltReRoll +1);
				}
				haveToReRoll = true;
			}
			return 40;
		}
		if(fiveCount >= 3 && fiveCount < 5){
			//  turnScore = turnScore + 50;
			if(fiveCount == 4){
				//rollSingleDie(playerId, (fiveReRoll + 1));
				if(fiveReRoll == 4){
					rollSingleDie(playerId, 5);
				}
				else{
					rollSingleDie(playerId, fiveReRoll + 1);
				}
				haveToReRoll = true;
			}
			return 50;
		}
		if(starCount >= 3 && starCount < 5){
			//  turnScore = turnScore + 60;
			if(starCount == 4){
				//rollSingleDie(playerId, (starReRoll + 1));
				if(starReRoll == 4){
					rollSingleDie(playerId, 5);
				}
				else{
					rollSingleDie(playerId, starReRoll + 1);
				}
				haveToReRoll = true;
			}
			return 60;
		}
		if(tenCount >= 3 && tenCount < 5){
			// turnScore = turnScore + 100;
			if(tenCount == 4){
				//rollSingleDie(playerId, (tenReRoll + 1));
				if(tenReRoll == 4){
					rollSingleDie(playerId, 5);
				}
				else{
					rollSingleDie(playerId, tenReRoll + 1);
				}
				haveToReRoll = true;
			}
			return 100;
		}
		//END NORMAL FLASH HANDLING




		//BEGIN 10 & 5 COUNTING CASES
		if(tenCount != 0){
			if(fiveCount != 0){
				return (fiveCount*5) + (tenCount*10);
				//I don't know if we need to set haveToReroll to true here --SL
			}
			return tenCount*10;
		}
		if(fiveCount != 0){
			return fiveCount*5;
			//I don't know if we need to set haveToReroll to true here --SL
		}
		//END 10 & 5 COUNTING CASES

		//BEGIN ONLY FLAMING SUN CASE
		if(tenCount == 0 && fiveCount == 0 && ourDice[4].dieState == 3){
			return 10;
		}
		//END ONLY FLAMING SUN CASE

		//BEGIN WIMPOUT CASE
		if(fiveCount == 0 && tenCount == 0 && ourDice[4].dieState != 3) {
			return -1;
		}
		//END WIMPOUT CASE

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
			int currentScore = playerArrayList.get(playerId).getPlayerScore();
			if(playerId == 0){
				playerArrayList.get(playerId).setPlayerScore(currentScore + turnScore);
				whoseTurn = 1;
				turnScore = 0; //reset turn score to 0 for next player
			}
			else if(playerId == 1){
				playerArrayList.get(playerId).setPlayerScore(currentScore + turnScore);
				whoseTurn = 0;
				turnScore = 0;
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
				this.turnScore = this.turnScore + totalDiceScore(diceArray,playerId);
			}
			else{
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

	public int diceScoreForOneDice(Die[] ourDice, int playerId, int diceID){
		int diceState = ourDice[diceID-1].dieState;
		if(diceID == 3){
			diceState = 3;
			return 10; //flashing sun
		}
		if(ourDice[diceID-1].dieState == 1){

			return 10;
		}
		else if(ourDice[diceID-1].dieState == 5){

			return 5;
		}
		return -1;
	}

	public boolean rollSelectedDice
			(int playerId, boolean dice1, boolean dice2, boolean dice3, boolean dice4, boolean dice5){
		if(playerId == whoseTurn) {
			int currentScore = playerArrayList.get(playerId).getPlayerScore();
			if(dice1){
				rollSingleDie(playerId, 1);
				trueCounter++;
				die1ReRoll = true;
			}
			else{
				die1ReRoll =false;
			}

			if(dice2){
				rollSingleDie(playerId, 2);
				trueCounter++;
				die2ReRoll = true;
			}
			else{
				die2ReRoll =false;
			}
			if(dice3){
				rollSingleDie(playerId, 3);
				trueCounter++;
				die3ReRoll = true;
			}
			else{
				die3ReRoll =false;
			}
			if(dice4){
				rollSingleDie(playerId, 4);
				trueCounter++;
				die4ReRoll = true;
			}
			else{
				die4ReRoll =false;
			}
			if(dice5){
				rollSingleDie(playerId, 5);
				trueCounter++;
				die5ReRoll = true;
			}
			else{
				die5ReRoll =false;
			}

			if(trueCounter == 1){
				int whichDie = whichDice();
				int score = diceScoreForOneDice(diceArray,playerId, whichDie);
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
				int score = totalDiceScore(diceArray, playerId);
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

		}
		trueCounter = 0;
		return false;
	}

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
	public int getPlayer1Score(){
		return this.playerArrayList.get(0).getPlayerScore();
	}

	public int getPlayer2Score(){
		return this.playerArrayList.get(1).getPlayerScore();
	}

	public int getPlayer3Score(){
		return this.playerArrayList.get(2).getPlayerScore();
	}

	private void setWhoseTurn(int player){
		this.whoseTurn = player;
	}

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

	private int getScore4Dice(Die[] ourDice, int one, int two, int three, int four){
		int score = 0;
		boolean notWimp = false;
		int tenCount = 0;
		int moonCount =0;
		int triangleCount = 0;
		int boltCount=0;
		int fiveCount = 0;
		int starCount =0;

		for(int i = 0; i < ourDice.length; i++) {
			if (ourDice[i].getDieState() == 1) {
				tenCount++;
			} else if (ourDice[i].getDieState() == 2) {
				moonCount++;
			} else if (ourDice[i].getDieState() == 3 && i != 2) {
				triangleCount++;
			} else if (ourDice[i].getDieState() == 4) {
				boltCount++;
			} else if (ourDice[i].getDieState() == 5) {
				fiveCount++;
			} else if (ourDice[i].getDieState() == 6) {
				boltCount++;
			}
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
				if(ourDice[3].dieState == 3) {
					score = score + 100;
					notWimp = true;
				}
			}
			else if(tenCount == 2){
				score = score + 20;
				notWimp = true;
			}

			if(moonCount == 2 && (one == 3 || two == 3 || three == 3 || four == 3)){
				if(ourDice[3].dieState == 3) {
					score = score + 20;
					notWimp = true;
				}
			}
			if(triangleCount == 2 && (one == 3 || two == 3 || three == 3 || four == 3)){
				if(ourDice[3].dieState == 3) {
					score = score + 30;
					notWimp = true;
				}
			}
			if(boltCount == 2 && (one == 3 || two == 3 || three == 3 || four == 3)){
				if(ourDice[3].dieState == 3) {
					score = score + 40;
					notWimp = true;
				}
			}
			if(fiveCount == 2 && (one == 3 || two == 3 || three == 3 || four == 3)){
				if(ourDice[3].dieState == 3) {
					score = score + 50;
					notWimp = true;
				}
			}
			else if(fiveCount == 2){
				score = score + 10;
				notWimp = true;
			}
			if(starCount == 2 && (one == 3 || two == 3 || three == 3 || four == 3)){
				if(ourDice[3].dieState == 3) {
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

			if(one == 3 || two == 3 || three == 3 || four == 3){
				if(ourDice[3].dieState == 3){
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
			score = flashWithSun(ourDice, one, two, three);
			notWimp = true;
		}
		else {
			if (one == 3 || two == 3 || three == 3) {
				if (ourDice[3].dieState == 3) {
					score = 10;
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

	public int getTurnScore() {return this.turnScore; }

	public boolean getIsSuperNova() { return this.isSuperNova; }
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

