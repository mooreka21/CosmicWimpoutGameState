package edu.up.cs301.cosmicwimpout;

import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.io.Console;


/**
* A computer-version of a counter-player.  Since this is such a simple game,
* it just sends "+" and "-" commands with equal probability, at an average
* rate of one per second. This computer player does, however, have an option to
* display the game as it is progressing, so if there is no human player on the
* device, this player will display a GUI that shows the value of the counter
* as the game is being played.
* 
*@author Sam Lemly
* @author Olivia Dendinger
 * @author David Campbell
 * @author Kayla Moore
 * @version March 2019
*/
public class CosmicWimpoutComputerPlayer2 extends CosmicWimpoutComputerPlayer {

    private int numRollsThisTurn;
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

        if (currentTurn != playerNum) {}
        else {
            //delay to make it seem like they are thinking
            sleep(1000);
            CosmicWimpoutActionRollAllDice allDiceAction =
                    new CosmicWimpoutActionRollAllDice(this);
            game.sendAction(allDiceAction);
            runSmartAi(info);
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

		// remember who our text view is, for updating the counter value
		//this.cosmicWimpoutValueTextView =
				//(TextView) activityForGui.findViewById(R.id.counterValueTextView);
		
		// disable the buttons, since they will have no effect anyway
		//Button plusButton = (Button)activityForGui.findViewById(R.id.plusButton);
		//plusButton.setEnabled(false);
		//Button minusButton = (Button)activityForGui.findViewById(R.id.minusButton);
		//minusButton.setEnabled(false);
		
		// if the state is non=null, update the display
		if (state != null) {
			updateDisplay();
		}
	}


	//SMART AI METHODS
	public void runSmartAi(GameInfo info){
	    int currentTurn = -1;
	    try {
			sleep(1000);
			Die[] ourDice = ((CosmicWimpoutState) info).getDiceArray();
			try {
				getScoresFromCopy((CosmicWimpoutState) info);
			} catch (Exception e) {
				Log.e("Error", ("caught exception " + e + " at inner try/catch"));
			}
			int successes = getSuccess(this.scoresFromCopy);
			int rerollCount = scoresFromCopy.length;
			int turnScoreBefore = this.state.getTurnScore();
			this.odds = calcOdds(successes, rerollCount);
            Log.i(" Odds = ", this.odds +"" );
            Log.i(" Success = ",  successes+"" );
            Log.i(" Total Rolls = ",  rerollCount+"" );
			while (this.odds > 0.5) {
                for (int i = 0; i < 5; i++) {
                    switch (i) {
                        case 0:
                            this.needReroll[i] = this.state.isDie1ReRoll();

                        case 1:
                            this.needReroll[i] = this.state.isDie2ReRoll();
                        case 2:
                            this.needReroll[i] = this.state.isDie3ReRoll();
                        case 3:
                            this.needReroll[i] = this.state.isDie4ReRoll();
                        case 4:
                            this.needReroll[i] = this.state.isDie5ReRoll();
                    }
                    Log.i("Boolean "+i+"",""+(this.needReroll[i]));
                }
			    Log.i("Odds from 188 = ", this.odds+"" );
				CosmicWimpoutActionRollSelectedDie botRollsSomeDice =
						new CosmicWimpoutActionRollSelectedDie(this,
								this.needReroll[0],
                                this.needReroll[1],
                                this.needReroll[2],
                                this.needReroll[3],
                                this.needReroll[4]);
				game.sendAction(botRollsSomeDice);
				int newScore = state.getTurnScore();
				Log.i(" New score: ", ""+newScore);
				Log.i("Old Score: ",""+turnScoreBefore);
				this.updateDisplay();
				if (newScore >= turnScoreBefore) {
					successes--;
                    Log.i(" Success = ",  successes+"" );
					this.odds = calcOdds(successes, rerollCount);
					Log.i("New Odds: ", ""+ this.odds);
				} else if (state.getTurnScore() == 0) {
					break;
				}
			}
            this.updateDisplay();
            CosmicWimpoutActionEndTurn endTurn = new CosmicWimpoutActionEndTurn(this);
            game.sendAction(endTurn);
            return;
		}
		catch(Exception e){
	    	Log.e("Error", "Found Exception " + e + " at outer try/catch" );
		}
    }
	//If the bot scores at all, it'll reroll non-scoring dice only.
	//Those dice will have to remain static in the copy and not be rerolled.
    //If the bot rolls a Supernova or Instant winner, the turn score gets set to 0.
    private void getScoresFromCopy(GameInfo info){
		if(info instanceof CosmicWimpoutState){
            /*
			this generates a number of copies equal to the bot's intelligence,
			and then puts those scores into the bot's scoresFromCopy array.
			This method needs to be reworked for instances in which the bot doesn't have to
			reroll all its dice - this means only a few die will be copied and have to be rerolled.
            */
			for (int i = 0; i < intelligence; i++) {
				CosmicWimpoutState newCopy = new CosmicWimpoutState((CosmicWimpoutState) info);
				newCopy.rollSelectedDice(
				        this.playerNum,this.needReroll[0],
                        this.needReroll[1],this.needReroll[2],
						this.needReroll[3],this.needReroll[4]);
				if(newCopy.getIsInstantWinner() || newCopy.getIsSuperNova() || newCopy.getIsWimpout()){
				    this.scoresFromCopy[i] = 0;
                }
                else{
                    this.scoresFromCopy[i] = ((CosmicWimpoutState) newCopy).getTurnScore();
                }
			}
		}
	}
	private int getFailed(int[] scores){
		int numFailed = 0;
		for(int i =0; i<scores.length;i++){
			if(scores[i] == 0){
				numFailed++;
			}
		}
		return numFailed;
	}
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
	private float calcOdds(int successes, int total){
	    float newOdds = ((float)successes /(float) total);
	    Log.i("calculated odds", newOdds+"");
	    return newOdds;
    }

}
