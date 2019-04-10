package edu.up.cs301.cosmicwimpout;

import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
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
    private int fails;
    private int threshold = 100;
    //this boolean array helps determine which die are to be kept static in the copy reroll
    private boolean[] needReroll = new boolean[5];
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
        int currentTurn = -1;

        if (info instanceof CosmicWimpoutState) {
            this.state = (CosmicWimpoutState) info;
            currentTurn = this.state.getWhoseTurn();
        }

        if (currentTurn != playerNum) {}
        else {
            //delay to make it seem like they are thinking
            sleep(5000);
            CosmicWimpoutActionRollAllDice allDiceAction =
                    new CosmicWimpoutActionRollAllDice(this);
            game.sendAction(allDiceAction);

            do{
                //random probability
                int randomNumber = (int) (Math.random() * 10);
                while (randomNumber == 0) {
                    randomNumber = (int) (Math.random() * 10);
                }
                boolean one = false;
                boolean two = false;
                boolean three = false;
                boolean four = false;
                boolean five = false;

                if (randomNumber > 5) {
                    CosmicWimpoutActionEndTurn endTurnAction = new CosmicWimpoutActionEndTurn(this);
                    game.sendAction(endTurnAction);
                } else {
                    //select random die to re roll
                    int randomDice = (int) (Math.random() * 5 + 1);
                    if (randomDice == 1) {
                        one = true;
                    } else if (randomDice == 2) {
                        two = true;
                    } else if (randomDice == 3) {
                        three = true;
                    } else if (randomDice == 4) {
                        four = true;
                    } else if (randomDice == 5) {
                        five = true;
                    }

                    CosmicWimpoutActionRollSelectedDie selectedAction =
                            new CosmicWimpoutActionRollSelectedDie(this, one, two,
                                    three, four, five);
                    game.sendAction(selectedAction);
                }
            }while(this.state.getTurnScore() < threshold);
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


}
