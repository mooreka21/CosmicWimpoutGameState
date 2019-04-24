package edu.up.cs301.cosmicwimpout;

import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.Serializable;



/**
* A computer-version of a counter-player.
 * This player is the smart AI, and rerolls dice it is able to reroll until it wimps out,
 * or scores above 70 points for the turn.
 * The way the bot decides when to re-roll
 * is by making a number of copies (the number equal to its intelligence scalar)
 * of the current gamestate after its first roll,
 * and rolling the dice that it can in each of those copies.
 * The bot then looks at the number of those copies in which the new roll did not wimp out,
 * score a flash, or instantly lose; this number is stored as a number of successful rolls.
 * The bot then calculates odds by using a (successful rolls in copies count)/(total copies made)
 * formula, and if the odds of success are over 50%, it rerolls all eligible dice in the active
 * gamestate. If the bot doesn't wimpout or isntantly lose,
 * the number of successes are decremented, to symbolize a successful roll being used. The odds are
 * then recalculated - the turn ends if the odds of success are <50%(see beloow) , if else,
 * the process repeats until the bot scores over 70 points for its turn.
*
 * Note: The 50% odds mark was selected, as all of us agreed none of us would choose to reroll
 * if we knew we had >50% odds of wimpout or loss of the game.
 *
 * KNOWN ISSUES: Currently, the bot does not respond well to flashes rolled after its initial roll.
 * This is an issue with the state and retrieval of information from the state. With more time, we
 * would implement a series of methods to force an update of the master state after an action takes
 * place in-game during the smart bot's turn.
 *
 *
* @author Sam Lemly, Olivia Dendinger, David Campbell, Kayla Moore
*  @version April 2019
*/


public class CosmicWimpoutComputerPlayer2
        extends CosmicWimpoutComputerPlayer
        implements Serializable {

    private int maxTurnScore = 30;
    private float odds;
    private int intelligence = 10;
    private int[] scoresFromCopy = new int[intelligence];
    private int fails;
    private TextView player1Score;
    private TextView player2Score;
    private TextView player3Score;
    private TextView player4Score;
    private TextView turnScore;
    private int redDiceFaces[] = {R.drawable.ten, R.drawable.halfcircles, R.drawable.triangle,
            R.drawable.bolts, R.drawable.five, R.drawable.stars};
    private int blackDiceFaces[] = {R.drawable.blackten, R.drawable.blacktwocircles, R.drawable.flamingsun,
            R.drawable.blackbolt, R.drawable.blackfive, R.drawable.blackstar};

    private ImageView die1, die2, die3, die4, die5;
    //this boolean array helps determine which die are to be kept static in the copy reroll
    private boolean[] needReroll = new boolean[5];
	/*
	 * instance variables
	 */
	private static final long serialVersionUID= 478598448L;
	// the most recent game state, as given to us by the CounterLocalGame
	private CosmicWimpoutState state = null;
	
	// If this player is running the GUI, the activity (null if the player is
	// not running a GUI).
	private Activity activityForGui = null;
	
	// If this player is running the GUI, the widget containing the counter's
	// value (otherwise, null);
	private TextView cosmicWimpoutValueTextView = null;
	
	// If this player is running the GUI, the handler for the GUI thread (otherwise
	// null)
	private Handler guiHandler = null;

	/**
	 * constructor
	 * 
	 * @param name
	 * 		the player's name
	 */
	public CosmicWimpoutComputerPlayer2(String name) {
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

        if (info instanceof CosmicWimpoutState) {
            this.state = (CosmicWimpoutState) info;
            currentTurn = this.state.getWhoseTurn();
        }

        if (currentTurn != playerNum) {//ignore if not bot's turn
        }
        else {
            //delay to make it seem like they are thinking
            sleep(1000);
            CosmicWimpoutActionRollAllDice allDiceAction =
                   new CosmicWimpoutActionRollAllDice(this);
            game.sendAction(allDiceAction);
            if(this.state.getWhoseTurn() == playerNum) {
                this.updateDisplay();
                runSmartAi(info);
                sleep(1000);
            }
        }
    }
	
	
	/** 
	 * sets the counter value in the text view
	 *  */
	private void updateDisplay() {
		// if the guiHandler is available, set the new counter value
		// in the counter-display widget, doing it in the Activity's
		// thread.
		if (guiHandler != null) {
			guiHandler.post(
					new Runnable() {
						public void run() {
						if (cosmicWimpoutValueTextView != null && state != null) {
                            if (player1Score != null && state != null) {
                                player1Score.setText(allPlayerNames[0] + ": " + state.getPlayer1Score());
                                player2Score.setText(allPlayerNames[1] + ": " + state.getPlayer2Score());
                                player3Score.setText(allPlayerNames[2] + ": " + state.getPlayer3Score());
                                player4Score.setText(allPlayerNames[3] + ": " + state.getPlayer4Score());
                                turnScore.setText("Turn Score: " + state.getTurnScore() + "pts");

                                //setting die 1 face to whatever the current die state is
                                if(state.getDiceVal(0).equals("Tens")){
                                    die1.setImageResource(redDiceFaces[0]);
                                }
                                else if(state.getDiceVal(0).equals("Moons")){
                                    die1.setImageResource(redDiceFaces[1]);
                                }
                                else if(state.getDiceVal(0).equals("Triangles")){
                                    die1.setImageResource(redDiceFaces[2]);
                                }
                                else if(state.getDiceVal(0).equals("Bolts")){
                                    die1.setImageResource(redDiceFaces[3]);
                                }
                                else if(state.getDiceVal(0).equals("Fives")){
                                    die1.setImageResource(redDiceFaces[4]);
                                }
                                else if(state.getDiceVal(0).equals("Stars")){
                                    die1.setImageResource(redDiceFaces[5]);
                                }

                                //setting die to face
                                if(state.getDiceVal(1).equals("Tens")){
                                    die2.setImageResource(redDiceFaces[0]);
                                }
                                else if(state.getDiceVal(1).equals("Moons")){
                                    die2.setImageResource(redDiceFaces[1]);
                                }
                                else if(state.getDiceVal(1).equals("Triangles")){
                                    die2.setImageResource(redDiceFaces[2]);
                                }
                                else if(state.getDiceVal(1).equals("Bolts")){
                                    die2.setImageResource(redDiceFaces[3]);
                                }
                                else if(state.getDiceVal(1).equals("Fives")){
                                    die2.setImageResource(redDiceFaces[4]);
                                }
                                else if(state.getDiceVal(1).equals("Stars")){
                                    die2.setImageResource(redDiceFaces[5]);
                                }

                                //setting die 3 face
                                if(state.getDiceVal(2).equals("Tens")){
                                    die3.setImageResource(blackDiceFaces[0]);
                                }
                                else if(state.getDiceVal(2).equals("Moons")){
                                    die3.setImageResource(blackDiceFaces[1]);
                                }
                                else if(state.getDiceVal(2).equals("Flaming Sun")){
                                    die3.setImageResource(blackDiceFaces[2]);
                                }
                                else if(state.getDiceVal(2).equals("Bolts")){
                                    die3.setImageResource(blackDiceFaces[3]);
                                }
                                else if(state.getDiceVal(2).equals("Fives")){
                                    die3.setImageResource(blackDiceFaces[4]);
                                }
                                else if(state.getDiceVal(2).equals("Stars")){
                                    die3.setImageResource(blackDiceFaces[5]);
                                }

                                //setting die 4 face
                                if(state.getDiceVal(3).equals("Tens")){
                                    die4.setImageResource(redDiceFaces[0]);
                                }
                                else if(state.getDiceVal(3).equals("Moons")){
                                    die4.setImageResource(redDiceFaces[1]);
                                }
                                else if(state.getDiceVal(3).equals("Triangles")){
                                    die4.setImageResource(redDiceFaces[2]);
                                }
                                else if(state.getDiceVal(3).equals("Bolts")){
                                    die4.setImageResource(redDiceFaces[3]);
                                }
                                else if(state.getDiceVal(3).equals("Fives")){
                                    die4.setImageResource(redDiceFaces[4]);
                                }
                                else if(state.getDiceVal(3).equals("Stars")){
                                    die4.setImageResource(redDiceFaces[5]);
                                }

                                //setting die 5 face
                                if(state.getDiceVal(4).equals("Tens")){
                                    die5.setImageResource(redDiceFaces[0]);
                                }
                                else if(state.getDiceVal(4).equals("Moons")){
                                    die5.setImageResource(redDiceFaces[1]);
                                }
                                else if(state.getDiceVal(4).equals("Triangles")){
                                    die5.setImageResource(redDiceFaces[2]);
                                }
                                else if(state.getDiceVal(4).equals("Bolts")){
                                    die5.setImageResource(redDiceFaces[3]);
                                }
                                else if(state.getDiceVal(4).equals("Fives")){
                                    die5.setImageResource(redDiceFaces[4]);
                                }
                                else if(state.getDiceVal(4).equals("Stars")){
                                    die5.setImageResource(redDiceFaces[5]);
                                }

                                //highlighting whose turn it is with red background
                                int turn = state.getWhoseTurn();
                                if(turn == 0){
                                    player1Score.setBackgroundColor(0xFFFF0000);
                                    player2Score.setBackgroundColor(0xFFB7B1A3);
                                    player3Score.setBackgroundColor(0xFFB7B1A3);
                                    player4Score.setBackgroundColor(0xFFB7B1A3);
                                }
                                if(turn == 1){
                                    player2Score.setBackgroundColor(0xFFFF0000);
                                    player1Score.setBackgroundColor(0xFFB7B1A3);
                                    player3Score.setBackgroundColor(0xFFB7B1A3);
                                    player4Score.setBackgroundColor(0xFFB7B1A3);
                                }
                                if(turn == 2){
                                    player3Score.setBackgroundColor(0xFFFF0000);
                                    player1Score.setBackgroundColor(0xFFB7B1A3);
                                    player2Score.setBackgroundColor(0xFFB7B1A3);
                                    player4Score.setBackgroundColor(0xFFB7B1A3);
                                }
                                if(turn == 3){
                                    player4Score.setBackgroundColor(0xFFFF0000);
                                    player1Score.setBackgroundColor(0xFFB7B1A3);
                                    player2Score.setBackgroundColor(0xFFB7B1A3);
                                    player3Score.setBackgroundColor(0xFFB7B1A3);
                                }

                            }

						}
					}});
		}
	}
	
	/**
	 * Tells whether we support a GUI
	 * 
	 * @return
	 * 		true because we support a GUI
	 */
	public boolean supportsGui() {
		return true;
	}
	
	/**
	 * callback method--our player has been chosen/rechosen to be the GUI,
	 * called from the GUI thread.
	 * 
	 * @param a
	 * 		the activity under which we are running
	 */
	@Override
	public void setAsGui(GameMainActivity a) {
		
		// remember who our activity is
		this.activityForGui = a;
		
		// remember the handler for the GUI thread
		this.guiHandler = new Handler();
		
		// Load the layout resource for the our GUI's configuration
		activityForGui.setContentView(R.layout.cosmicwimpout_human_player);
        this.player1Score = activityForGui.findViewById(R.id.player1Score);
        this.player2Score = activityForGui.findViewById(R.id.player2Score);
        this.player3Score = activityForGui.findViewById(R.id.player3Score);
        this.player4Score = activityForGui.findViewById(R.id.player4Score);
        this.turnScore = activityForGui.findViewById(R.id.turnscore);


        this.die1 = activityForGui.findViewById(R.id.die1);
        this.die2 = activityForGui.findViewById(R.id.die2);
        this.die3 = activityForGui.findViewById(R.id.die3);
        this.die4 = activityForGui.findViewById(R.id.die4);
        this.die5 = activityForGui.findViewById(R.id.die5);

		
		// if the state is non=null, update the display
		if (state != null) {
			updateDisplay();
		}
	}



    /**
     * This method is the smart AI's thinking algorithm. The class javadoc description has
     * a more detailed breakdown of how the process exactly works.
     * @param info
     */
	private void runSmartAi(GameInfo info){
	    //int currentTurn = -1;

        this.needReroll = this.state.findScoringDice();
        getScoresFromCopy(this.state);
        int successes = getSuccess(this.scoresFromCopy);
        int rerollCount = scoresFromCopy.length;
        int turnScoreBefore = this.state.getTurnScore();
        this.odds = calcOdds(successes, rerollCount);


        while (this.odds > 0.5) {
            sleep(1250);

            //flash handling, in case the bot has rolled a flash on the first roll of the turn.
            //This includes the initial roll of the turn.
            if(this.state.getIsFlash()){

                //get the booleans (true if they have to re roll)
                boolean[] reRolls = this.state.flashReRoll();
                boolean die1 = reRolls[0];
                boolean die2 = reRolls[1];
                boolean die3 = reRolls[2];
                boolean die4 = reRolls[3];
                boolean die5 = reRolls[4];

                //send the action with the must re rolls
                CosmicWimpoutActionRollSelectedDie selectedDie =
                        new CosmicWimpoutActionRollSelectedDie(this,die1, die2,die3,die4,die5);
                game.sendAction(selectedDie);
                //update display
                updateDisplay();
            }

            this.needReroll = this.state.findScoringDice();
            //this rolls the selected dice for the bot
            CosmicWimpoutActionRollSelectedDie botRollsSomeDice =
                    new CosmicWimpoutActionRollSelectedDie(this,
                            this.needReroll[0],
                            this.needReroll[1],
                            this.needReroll[2],
                            this.needReroll[3],
                            this.needReroll[4]);
            game.sendAction(botRollsSomeDice);
            this.updateDisplay();
            this.needReroll = this.state.findScoringDice();

            /*
            if the bot rolled a roll that scored it more points than it
            started that turn with, it continues, as it is experiencing success.
            */
            int newScore = this.state.getTurnScore();
            if (newScore >= turnScoreBefore) {
                successes--;

                this.odds = calcOdds(successes, rerollCount);

            } else if (state.getTurnScore() == 0) {
                break;
            }
            if (this.state.getTurnScore() >= maxTurnScore) {
                updateDisplay();
                break;
            }
        }
        //flash handling before the bot decides to end its turn,
        //as it has now passed the score threshold.
        if(this.state.getIsFlash()){
            //get the booleans (true if they have to re roll)
            boolean[] reRolls = this.state.flashReRoll();
            boolean die1 = reRolls[0];
            boolean die2 = reRolls[1];
            boolean die3 = reRolls[2];
            boolean die4 = reRolls[3];
            boolean die5 = reRolls[4];

            //send the action with the must re rolls
            CosmicWimpoutActionRollSelectedDie selectedDie =
                    new CosmicWimpoutActionRollSelectedDie(this,die1, die2,die3,die4,die5);
            this.state.setIsFlash(false);
            game.sendAction(selectedDie);

        }
        sleep(1500);
        CosmicWimpoutActionEndTurn endTurn = new CosmicWimpoutActionEndTurn(this);
        game.sendAction(endTurn);
        updateDisplay();
    }

    /**
     * This void method takes the current game state, makes copies of it, and rolls eligible die.
     * All scores are then index in the getTurnScore array in this class.
     * If the bot scores at all in its turn, it'll reroll non-scoring dice only.
     * Those dice will remain static in the copies and not be rerolled.
     * If the created copy rolls a Supernova, wimpout, or Instant winner,
     * the turn score gets set to 0.
     * @param stateToCopy
     */
    private void getScoresFromCopy(CosmicWimpoutState stateToCopy){
        for (int i = 0; i < intelligence; i++) {
            CosmicWimpoutState newCopy = new CosmicWimpoutState(stateToCopy);
            newCopy.rollSelectedDice(this.playerNum,
                    this.needReroll[0],
                    this.needReroll[1],
                    this.needReroll[2],
                    this.needReroll[3],
                    this.needReroll[4]);
            if(newCopy.getIsInstantWinner() || newCopy.getIsSuperNova() || newCopy.getIsWimpout()){
                this.scoresFromCopy[i] = 0;
            }
            else{
                this.scoresFromCopy[i] = newCopy.getTurnScore();
            }

        }

	}

	/**
	 * Calculates the number of non-zero/nonwimpoutrolls from a sample collection of scores
	 * @param scores
	 * @return successes
	 */
	private int getSuccess(int[] scores){
		int numSuccess = 0;
		for(int i =0; i<scores.length;i++){
			if(scores[i] != 0){
				numSuccess++;
			}
		}
		Log.i("getSuccesses : ", ""+numSuccess );
		return numSuccess;
	}

    /**
     * Calculates a simple float, which equates to (successes)/(total rolls)
     * @param successes
     * @param total
     * @return newOdds
     */
	private float calcOdds(int successes, int total){
	    float newOdds = ((float)successes /(float) total);
	    Log.i("calculated odds", newOdds+"");
	    return newOdds;
    }

}
