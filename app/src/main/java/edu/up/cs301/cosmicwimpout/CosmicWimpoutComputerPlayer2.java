package edu.up.cs301.cosmicwimpout;

import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
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
 * then recalculated - the turn ends if the odds of success are <50% , if else,
 * the process repeats until the bot scores over 70 points for its turn.
* 
* @author Sam Lemly, Olivia Dendinger, David Campbell, Kayla Moore
*  @version April 2019
*/
public class CosmicWimpoutComputerPlayer2
        extends CosmicWimpoutComputerPlayer
        implements Serializable {

    private int numRollsThisTurn;
    private int maxTurnScore = 70;
    private float odds;
    private int intelligence = 10;
    private int[] scoresFromCopy = new int[intelligence];
    private int fails;
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
							//cosmicWimpoutValueTextView.setText("" + currentGameState.getCounter());
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
	    try {
            this.needReroll = this.state.findScoringDice();
            getScoresFromCopy(this.state);
			int successes = getSuccess(this.scoresFromCopy);
			int rerollCount = scoresFromCopy.length;
			int turnScoreBefore = this.state.getTurnScore();
			this.odds = calcOdds(successes, rerollCount);

            while (this.odds > 0.5) {
                sleep(750);
                //flash handling, in case the bot has rolled a flash on the roll prior.
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
//                for (int i = 0; i < this.state.getDiceArray().length; i++) {
//                    this.needReroll[i] = this.state.getDiceArray()[i].getCanReroll();
//                }
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
                int newScore = state.getTurnScore();
                this.updateDisplay();
                if (this.state.getTurnScore() >= turnScoreBefore) {
                    successes--;
                    this.odds = calcOdds(successes, rerollCount);
                } else {
                    updateDisplay();
                    break;
                }
                if (this.state.getTurnScore() >= maxTurnScore && !state.getIsFlash()) {
                    updateDisplay();
                    break;
                }
                this.needReroll = null;
            }

		}
		catch(Exception e){
	    	Log.e("", e + "" );
		}
        CosmicWimpoutActionEndTurn endTurn = new CosmicWimpoutActionEndTurn(this);
        game.sendAction(endTurn);
        updateDisplay();

    }
	/*If the bot scores at all, it'll reroll non-scoring dice only.
	Those dice will have to remain static in the copy and not be rerolled.
    If the bot rolls a Supernova or Instant winner, the turn score gets set to 0.*/
    private void getScoresFromCopy(CosmicWimpoutState stateToCopy){
        /*
        this generates a number of copies equal to the bot's intelligence,
        and then puts those scores into the bot's scoresFromCopy array.
        This method needs to be reworked for instances in which the bot doesn't have to
        reroll all its dice - this means only a few die will be copied and have to be rerolled.
        */
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
	 * Calculates the number of failed rolls from a sample collection of scores
	 * @param scores
	 * @return numfailed
	 */
	private int getFailed(int[] scores){
		int numFailed = 0;
		for(int i =0; i<scores.length;i++){
			if(scores[i] == 0){
				numFailed++;
			}
		}
		return numFailed;
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

    private void findRerolls(){
	    Die[] ourDice = this.state.getDiceArray();
	    for(int i = 0 ;i < ourDice.length; i++){
	        //if
        }
    }

}
