package edu.up.cs301.cosmicwimpout;

import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;


/**
* A computer-version of a counter-player.  Since this is such a simple game,
* it just sends "+" and "-" commands with equal probability, at an average
* rate of one per second. This computer player does, however, have an option to
* display the game as it is progressing, so if there is no human player on the
* device, this player will display a GUI that shows the value of the counter
* as the game is being played.
* 
*@author Sam Lemly
* @author Olivia Dendinger* @author David Campbell
 * @author Kayla Moore
 * @version March 2019
*/
public class CosmicWimpoutComputerPlayer2 extends CosmicWimpoutComputerPlayer {
    private int scoresFromCopy[];
    private int numRollsThisTurn;
    private float odds;
    private int intelligence;
	/*
	 * instance variables
	 */
	//private static final long serialVersionUID= 478598448L;
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
		// perform superclass behavior
		super.receiveInfo(info);
		
		Log.i("computer player", "receiving");
		
		// if there is no game, ignore
		if (game == null) {
			return;
		}
		else if (info instanceof CosmicWimpoutState) {
			// if we indeed have a counter-state, update the GUI
			state = (CosmicWimpoutState)info;
			updateDisplay();
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
	public boolean runSmartAi(GameInfo info){
		//TODO: Rework for instances in which the bot needs to reroll a select number of dice
	    int currentTurn = -1;
        if(info instanceof CosmicWimpoutState){

            this.state = (CosmicWimpoutState) info;
            currentTurn = this.state.getWhoseTurn();
            if(currentTurn != playerNum){}
            else {
				//roll all dice to start the turn. Similar setup to dumb ai.
            	sleep(2500);
				CosmicWimpoutActionRollAllDice allDiceAction =
						new CosmicWimpoutActionRollAllDice(this);
				game.sendAction(allDiceAction);

				//if(){// something to check for a flash here

				//}
			}
        }
	    return false;
    }
    //these three methods come into play mostly when the bot has rolled a flash on its a first roll.
	//If the bot scores at all, it'll reroll non-scoring dice only.
	//Those dice will have to remain static in the copy and not be rerolled.
    private void getScoresFromCopy(GameInfo info){
		if(info instanceof CosmicWimpoutState){
			//this boolean array helps determine which die are to be kept static in the copy reroll
			boolean[] diceStatic = new boolean[5];
			//copy of the dice from the game state
			Die[] ourDice = ((CosmicWimpoutState) info).getDiceArray();
			for(int i = 0; i < diceStatic.length; i++){
				/*iterates through the dice in the current game state and finds which ones need to
				remain static for rerolls*/
				if(ourDice[i].dieState == 5 || ourDice[i].dieState == 1) {
					diceStatic[i] = true;
				}
				//flaming sun check, keeps the flaming sun die if it's on a scoring face
				else if(ourDice[i].dieID == 3 &&
                        (ourDice[i].dieState == 1
                        ||ourDice[i].dieState == 3
                        ||ourDice[i].dieState == 5)){
					diceStatic[i] = true;
				}
			}
			/*
			this generates a number of copies equal to the bot's intelligence,
			and then puts those scores into the bot's scoresFromCopy array.
			This method needs to be reworked for instances in which the bot doesn't have to
			reroll all its dice - this means only a few die will be copied and have to be rerolled.
			*/
			for (int i = 0; i < intelligence; i++) {
				CosmicWimpoutState newCopy = new CosmicWimpoutState((CosmicWimpoutState) info);
				newCopy.rollSelectedDice(this.playerNum,diceStatic[0], diceStatic[1],diceStatic[2],
						diceStatic[3],diceStatic[4]);
				this.scoresFromCopy[i] = ((CosmicWimpoutState) newCopy).getTurnScore();
			}
		}
	}
	private float failureOdds(int[] scores){
		float toReturn;
		int numFailed = 0;
		for(int i =0; i<scores.length;i++){
			if(scores[i] == 0){
				numFailed++;
			}
		}
		toReturn = numFailed / scores.length;
		return toReturn;
	}
	private float successOdds(int[] scores){
		float toReturn;
		int numSuccess = 0;
		for(int i =0; i<scores.length;i++){
			if(scores[i] != 0){
				numSuccess++;
			}
		}
		toReturn = numSuccess / scores.length;
		return toReturn;
	}

}
