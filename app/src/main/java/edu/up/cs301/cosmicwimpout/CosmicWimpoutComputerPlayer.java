package edu.up.cs301.cosmicwimpout;

import android.app.Activity;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.text.BreakIterator;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * A computer-version of a cosmic wimpout -player. this is the dumb AI
 * first rolls all the dice - rules of the game
 * then with equal probabilty either ends the turn
 * or randomly selects one dice to re-roll
 *
 * @author Sam Lemly, Olivia Dendinger, David Campbell, Kayla Moore
  * @version April 2019
 */
public class CosmicWimpoutComputerPlayer extends GameComputerPlayer implements Serializable {
	private static final long serialVersionUID= 390598449L;
	//instance variables
	private int ScoresFromCopy[] = new int[10];
	private int numRollsThisTurn;
	private float odds;
	private int intelligence;
	private CosmicWimpoutState state;

	private TextView player1Score;
	private TextView player2Score;
	private TextView player3Score;
	private TextView player4Score;
	private TextView turnScore;

	private int redDiceFaces[] = {R.drawable.ten, R.drawable.halfcircles, R.drawable.triangle,
			R.drawable.bolts, R.drawable.five, R.drawable.stars};
	private int blackDiceFaces[] = {R.drawable.blackten, R.drawable.blacktwocircles, R.drawable.flamingsun,
			R.drawable.blackbolt, R.drawable.blackfive, R.drawable.blackstar};

	//variables for image views
	private ImageView die1, die2, die3, die4, die5;

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
			//ignore
		}
		else {
			//delay to make it seem like they are thinking
			sleep(5000);
			CosmicWimpoutActionRollAllDice allDiceAction =
					new CosmicWimpoutActionRollAllDice(this);
			game.sendAction(allDiceAction);

			this.updateDisplay();

			if(this.state.getIsFlash()){
				boolean[] reRolls = this.state.flashReRoll();
				boolean die1 = reRolls[0];
				boolean die2 = reRolls[1];
				boolean die3 = reRolls[2];
				boolean die4 = reRolls[3];
				boolean die5 = reRolls[4];

				CosmicWimpoutActionRollSelectedDie selectedDie =
						new CosmicWimpoutActionRollSelectedDie(this,die1, die2,die3,die4,die5);
				game.sendAction(selectedDie);
				this.updateDisplay();

			}
			else {
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
				if(randomNumber < 5){

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
					}
					else if(randomDice == 2){
						two = true;
						if(state.check1Die(1)){
							CosmicWimpoutActionRollSelectedDie selectedAction =
									new CosmicWimpoutActionRollSelectedDie(this, one, two,
											three, four, five);
							game.sendAction(selectedAction);
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
					}
					else if(randomDice == 4){
						four = true;
						if(state.check1Die(3)){
							CosmicWimpoutActionRollSelectedDie selectedAction =
									new CosmicWimpoutActionRollSelectedDie(this, one, two,
											three, four, five);
							game.sendAction(selectedAction);
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
					}
					this.updateDisplay();
				}
			}
			CosmicWimpoutActionEndTurn endTurnAction = new CosmicWimpoutActionEndTurn(this);
			game.sendAction(endTurnAction);
		}
	}
}
