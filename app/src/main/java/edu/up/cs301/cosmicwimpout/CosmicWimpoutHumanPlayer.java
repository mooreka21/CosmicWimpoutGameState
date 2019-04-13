package edu.up.cs301.cosmicwimpout;

import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A GUI of a cosmic wimpout-player. The GUI displays the current value of the
 * current scores of all the players, the state of the dice, the current
 * turn score, and allows the human player to press the action buttons
 * in order to send moves to the game,
 *
 * @author Sam Lemly
 * @author Olivia Dendinger
 * @author David Campbell
 * @author Kayla Moore
 *  @version March 2019
 */
public class CosmicWimpoutHumanPlayer extends GameHumanPlayer implements OnClickListener {

	/* instance variables */
    private static final long serialVersionUID= 9876483921L;
	// The TextView the displays the current counter value
	private TextView player1Score;
	private TextView player2Score;
	private TextView player3Score;
	private TextView player4Score;
	private TextView turnScore;

	//variables for buttons
	private Button endGameButton = null;
	private Button endTurnButton = null;
	private Button rollDiceButton = null;
	private Button rollSelectedButton = null;

	//variables for image views
	private ImageView die1, die2, die3, die4, die5;

	//variables for checks
	private CheckBox check1, check2, check3, check4, check5;
	//variables to check if they are checked or not
	private boolean isCheck1 = false;
	private boolean isCheck2 = false;
	private boolean isCheck3 = false;
	private boolean isCheck4 = false;
	private boolean isCheck5 = false;

	//variables for thread
	private boolean rollDiceClicked = false;
	private boolean rollSelectedClicked = false;

	//variable for sound effects
	private CosmicWimpoutSoundPlayer sound;


	//arrays that hold the die faces
	private int redDieFaces[] = {R.drawable.ten, R.drawable.halfcircles, R.drawable.triangle,
			R.drawable.bolts, R.drawable.five, R.drawable.stars};
	private int blackDieFaces[] = {R.drawable.blackten, R.drawable.blacktwocircles, R.drawable.flamingsun,
			R.drawable.blackbolt, R.drawable.blackfive, R.drawable.blackstar};

	//to make sure they press roll all dice first
	private int actionsPressed = 0;

	// the most recent game state, as given to us by the CounterLocalGame
	private CosmicWimpoutState state;

	// the android activity that we are running
	private GameMainActivity myActivity;

	/**
	 * constructor
	 * @param name
	 * 		the player's name
	 */
	public CosmicWimpoutHumanPlayer(String name) {
		super(name);
	}

	/**
	 * Returns the GUI's top view object
	 *
	 * @return
	 * 		the top object in the GUI's view heirarchy
	 */
	public View getTopView() {
		return myActivity.findViewById(R.id.top_gui_layout);
	}

	/**
	 * sets the counter value in the text view
	 */
	protected void updateDisplay() {

		//set the text to current game state varibales
		this.player1Score.setText(allPlayerNames[0] + ": " + state.getPlayer1Score());
		this.player2Score.setText(allPlayerNames[1] + ": " + state.getPlayer2Score());
		this.player3Score.setText(allPlayerNames[2] + ": " + state.getPlayer3Score());
		this.player4Score.setText(allPlayerNames[3] + ": " + state.getPlayer4Score());
		this.turnScore.setText("Turn Score: " + state.getTurnScore() + "pts");

		//initializing sound player
		sound = new CosmicWimpoutSoundPlayer(myActivity);
		this.sound.playBackground();

		//setting die 1 face to whatever the current die state is
		if(state.getDiceVal(0).equals("Tens")){
			this.die1.setImageResource(redDieFaces[0]);
		}
		else if(state.getDiceVal(0).equals("Moons")){
			this.die1.setImageResource(redDieFaces[1]);
		}
		else if(state.getDiceVal(0).equals("Triangles")){
			this.die1.setImageResource(redDieFaces[2]);
		}
		else if(state.getDiceVal(0).equals("Bolts")){
			this.die1.setImageResource(redDieFaces[3]);
		}
		else if(state.getDiceVal(0).equals("Fives")){
			this.die1.setImageResource(redDieFaces[4]);
		}
		else if(state.getDiceVal(0).equals("Stars")){
			this.die1.setImageResource(redDieFaces[5]);
		}

		//setting die to face
		if(state.getDiceVal(1).equals("Tens")){
			this.die2.setImageResource(redDieFaces[0]);
		}
		else if(state.getDiceVal(1).equals("Moons")){
			this.die2.setImageResource(redDieFaces[1]);
		}
		else if(state.getDiceVal(1).equals("Triangles")){
			this.die2.setImageResource(redDieFaces[2]);
		}
		else if(state.getDiceVal(1).equals("Bolts")){
			this.die2.setImageResource(redDieFaces[3]);
		}
		else if(state.getDiceVal(1).equals("Fives")){
			this.die2.setImageResource(redDieFaces[4]);
		}
		else if(state.getDiceVal(1).equals("Stars")){
			this.die2.setImageResource(redDieFaces[5]);
		}

		//setting die 3 face
		if(state.getDiceVal(2).equals("Tens")){
			this.die3.setImageResource(blackDieFaces[0]);
		}
		else if(state.getDiceVal(2).equals("Moons")){
			this.die3.setImageResource(blackDieFaces[1]);
		}
		else if(state.getDiceVal(2).equals("Flaming Sun")){
			this.die3.setImageResource(blackDieFaces[2]);
		}
		else if(state.getDiceVal(2).equals("Bolts")){
			this.die3.setImageResource(blackDieFaces[3]);
		}
		else if(state.getDiceVal(2).equals("Fives")){
			this.die3.setImageResource(blackDieFaces[4]);
		}
		else if(state.getDiceVal(2).equals("Stars")){
			this.die3.setImageResource(blackDieFaces[5]);
		}

		//setting die 4 face
		if(state.getDiceVal(3).equals("Tens")){
			this.die4.setImageResource(redDieFaces[0]);
		}
		else if(state.getDiceVal(3).equals("Moons")){
			this.die4.setImageResource(redDieFaces[1]);
		}
		else if(state.getDiceVal(3).equals("Triangles")){
			this.die4.setImageResource(redDieFaces[2]);
		}
		else if(state.getDiceVal(3).equals("Bolts")){
			this.die4.setImageResource(redDieFaces[3]);
		}
		else if(state.getDiceVal(3).equals("Fives")){
			this.die4.setImageResource(redDieFaces[4]);
		}
		else if(state.getDiceVal(3).equals("Stars")){
			this.die4.setImageResource(redDieFaces[5]);
		}

		//setting die 5 face
		if(state.getDiceVal(4).equals("Tens")){
			this.die5.setImageResource(redDieFaces[0]);
		}
		else if(state.getDiceVal(4).equals("Moons")){
			this.die5.setImageResource(redDieFaces[1]);
		}
		else if(state.getDiceVal(4).equals("Triangles")){
			this.die5.setImageResource(redDieFaces[2]);
		}
		else if(state.getDiceVal(4).equals("Bolts")){
			this.die5.setImageResource(redDieFaces[3]);
		}
		else if(state.getDiceVal(4).equals("Fives")){
			this.die5.setImageResource(redDieFaces[4]);
		}
		else if(state.getDiceVal(4).equals("Stars")){
			this.die5.setImageResource(redDieFaces[5]);
		}

		//highlighting whose turn it is with red background
		int turn = this.state.getWhoseTurn();
		if(turn == 0){
			this.player1Score.setBackgroundColor(0xFFFF0000);
			this.player2Score.setBackgroundColor(0xFFCECECE);
			this.player3Score.setBackgroundColor(0xFFCECECE);
			this.player4Score.setBackgroundColor(0xFFCECECE);
		}
		if(turn == 1){
			this.player2Score.setBackgroundColor(0xFFFF0000);
			this.player1Score.setBackgroundColor(0xFFCECECE);
			this.player3Score.setBackgroundColor(0xFFCECECE);
			this.player4Score.setBackgroundColor(0xFFCECECE);
		}
		if(turn == 2){
			this.player3Score.setBackgroundColor(0xFFFF0000);
			this.player1Score.setBackgroundColor(0xFFCECECE);
			this.player2Score.setBackgroundColor(0xFFCECECE);
			this.player4Score.setBackgroundColor(0xFFCECECE);
		}
		if(turn == 3){
			this.player4Score.setBackgroundColor(0xFFFF0000);
			this.player1Score.setBackgroundColor(0xFFCECECE);
			this.player2Score.setBackgroundColor(0xFFCECECE);
			this.player3Score.setBackgroundColor(0xFFCECECE);
		}

		//when the player wimps out, any checkboxes will be cleared
		if(state.getTurnScore() == 0) {
			check1.setChecked(false);
			check2.setChecked(false);
			check3.setChecked(false);
			check4.setChecked(false);
			check5.setChecked(false);
		}


	}

	/**
	 * this method gets called when the user clicks any of the buttons. It
	 * creates a new action depending on the button to return to the
	 * parent activity.
	 *
	 * @param button
	 * 		the button that was clicked
	 */
	public void onClick(View button) {
		//starts a thread that rotates through die faces while rolling
		//Thread th1 = new Thread(this);
		//th1.start();

		//if the button is any of the checkboxes, update
		//variable to true
		if(button == check1 ){
				isCheck1 = true;
		}
		if(button == check2 ){
				isCheck2 = true;
		}
		if(button == check3){
				isCheck3 = true;

		}
		if(button == check4){
				isCheck4 = true;

		}
		if(button == check5){
				isCheck5 = true;

		}

		//if end turn button, end trun action
		CosmicWimpoutActionEndTurn endTurnAct =
				new CosmicWimpoutActionEndTurn(this);
		CosmicWimpoutActionRollAllDice rollAct =
				new CosmicWimpoutActionRollAllDice(this);
		CosmicWimpoutActionEndGame endGameAct =
				new CosmicWimpoutActionEndGame(this);
		//CosmicWimpoutActionRollSelectedDie rollSelectedAct =
			//new CosmicWimpoutActionRollSelectedDie(this, isCheck1, isCheck2, isCheck3, isCheck4,isCheck5);

		//making sure that they press the roll all dice button first
		if(this.actionsPressed == 0 || this.state.getTurnScore() == 0){
			if(button == rollDiceButton ){
				rollDiceClicked = true;
				game.sendAction(rollAct);
				actionsPressed++;
				sound.playFiveDice();
			}
            else if(button == endGameButton){
                game.sendAction(endGameAct);
            }
			else {
				//illegal move
				Toast.makeText(this.myActivity, "Illegal Move! Must" +
						" Roll All Dice First!", Toast.LENGTH_SHORT).show();
			}
		}

		else if (this.actionsPressed > 0){
            if(button == endGameButton){
                game.sendAction(endGameAct);
            }
			else if(button == endTurnButton){
				this.actionsPressed = 0;
				game.sendAction(endTurnAct);
				//clears checkboxes when player ends their turn
				check1.setChecked(false);
				check2.setChecked(false);
				check3.setChecked(false);
				check4.setChecked(false);
				check5.setChecked(false);
			}
			else if(button == rollDiceButton){
				//illegal move, must select all 5
				Toast.makeText(this.myActivity, "Cannot Roll All Dice",
						Toast.LENGTH_SHORT).show();
				//game.sendAction(rollAct);
			}
			else if(button == rollSelectedButton) {
				int checkCount = 0;
				rollSelectedClicked = true;
				//checking to see which ones are true to send in the action
				if (!(check1.isChecked())) {
					isCheck1 = false;
				}
				else {
					isCheck1 = true;
					checkCount++;
				}
				if (!(check2.isChecked())) {
					isCheck2 = false;
				}
				else {
					isCheck2 = true;
					checkCount++;
				}
				if (!(check3.isChecked())) {
					isCheck3 = false;
				}
				else {
					isCheck3 = true;
					checkCount++;
				}
				if (!(check4.isChecked())) {
					isCheck4 = false;
				} else {
					isCheck4 = true;
					checkCount++;
				}
				if (!(check5.isChecked())) {
					isCheck5 = false;
				} else {
					isCheck5 = true;
					checkCount++;
				}

				//determining what sound to play
				if(checkCount == 1){
					sound.playOneDie();
				}
				else if(checkCount == 2){
					sound.playTwoDice();
				}
				else if(checkCount == 3){
					sound.playThreeDice();
				}
				else if(checkCount == 4){
					sound.playFourDice();
				}
				else if(checkCount == 5){
					sound.playFiveDice();
				}

				if(legalMoveAllChecked5()){
					if(isCheck1 && isCheck2 && isCheck3 && isCheck4 && isCheck5){
						CosmicWimpoutActionRollSelectedDie rollSelectedAct =
								new CosmicWimpoutActionRollSelectedDie(this, isCheck1,
										isCheck2, isCheck3, isCheck4, isCheck5);
						game.sendAction(rollSelectedAct);
					}
					else{
						Toast.makeText(this.myActivity, "Must roll all dice",
								Toast.LENGTH_SHORT).show();
					}
				}
				//checking for legal move if all 5 are checked
				else if(isCheck1 && isCheck2 && isCheck3 && isCheck4 && isCheck5) {
					if (legalMoveAllChecked5()) {
						CosmicWimpoutActionRollSelectedDie rollSelectedAct =
								new CosmicWimpoutActionRollSelectedDie(this, isCheck1,
										isCheck2, isCheck3, isCheck4, isCheck5);
						game.sendAction(rollSelectedAct);
					} else if (!legalMoveAllChecked5()) {
						Toast.makeText(this.myActivity, "Cannot roll all dice",
								Toast.LENGTH_SHORT).show();
					}
				}
				//checking legal move if 4 are checked
				else if(checkCount == 4){
					if(legal4Die()){
						CosmicWimpoutActionRollSelectedDie rollSelectedDie =
								new CosmicWimpoutActionRollSelectedDie(this, isCheck1,
										isCheck2, isCheck3, isCheck4, isCheck5);
						game.sendAction(rollSelectedDie);
					}
					else{
						Toast.makeText(this.myActivity, "Cannot roll those 4 Dice!",
								Toast.LENGTH_SHORT).show();
					}
				}
				//checking legal move if 3 are checked
				else if(checkCount == 3){
					if(legal3Die()){
						CosmicWimpoutActionRollSelectedDie rollSelectedDie =
								new CosmicWimpoutActionRollSelectedDie(this, isCheck1,
										isCheck2, isCheck3, isCheck4, isCheck5);
						game.sendAction(rollSelectedDie);
					}
					else{
						Toast.makeText(this.myActivity, "Cannot roll those 3 Die!",
								Toast.LENGTH_SHORT).show();
					}
				}
				//checking legal move if 2 is checked
				else if(checkCount == 2){
					if(legal2Die()){
						CosmicWimpoutActionRollSelectedDie rollSelectedDie =
								new CosmicWimpoutActionRollSelectedDie(this, isCheck1,
										isCheck2, isCheck3, isCheck4, isCheck5);
						game.sendAction(rollSelectedDie);
					}
					else{
						Toast.makeText(this.myActivity, "Cannot roll those 2 Die!",
								Toast.LENGTH_SHORT).show();
					}
				}
				//checking for legal move if one is checked
				else if(isCheck1 && !isCheck5 && !isCheck4 && !isCheck3 && !isCheck2){
					if(legalOneChecked()){
						CosmicWimpoutActionRollSelectedDie rollSelectedAct =
								new CosmicWimpoutActionRollSelectedDie(this, isCheck1,
										isCheck2, isCheck3, isCheck4, isCheck5);
						game.sendAction(rollSelectedAct);

					}
					else{
						Toast.makeText(this.myActivity, "Cannot roll that die",
								Toast.LENGTH_SHORT).show();
					}
				}
				else if(!isCheck1 && isCheck5 && !isCheck4 && !isCheck3 && !isCheck2){
					if(legalOneChecked()){
						CosmicWimpoutActionRollSelectedDie rollSelectedAct =
								new CosmicWimpoutActionRollSelectedDie(this, isCheck1, isCheck2, isCheck3, isCheck4, isCheck5);
						game.sendAction(rollSelectedAct);

					}
					else{
						Toast.makeText(this.myActivity, "Cannot roll that die",
								Toast.LENGTH_SHORT).show();
					}
				}
				else if(!isCheck1 && !isCheck5 && isCheck4 && !isCheck3 && !isCheck2){
					if(legalOneChecked()){
						CosmicWimpoutActionRollSelectedDie rollSelectedAct =
								new CosmicWimpoutActionRollSelectedDie(this, isCheck1, isCheck2, isCheck3, isCheck4, isCheck5);
						game.sendAction(rollSelectedAct);

					}
					else{
						Toast.makeText(this.myActivity, "Cannot roll that die",
								Toast.LENGTH_SHORT).show();
					}
				}
				else if(!isCheck1 && !isCheck5 && !isCheck4 && isCheck3 && !isCheck2){
					if(legalOneChecked()){
						CosmicWimpoutActionRollSelectedDie rollSelectedAct =
								new CosmicWimpoutActionRollSelectedDie(this, isCheck1, isCheck2, isCheck3, isCheck4, isCheck5);
						game.sendAction(rollSelectedAct);

					}
					else{
						Toast.makeText(this.myActivity, "Cannot roll that die",
								Toast.LENGTH_SHORT).show();
					}
				}
				else if(!isCheck1 &&  !isCheck5 && !isCheck4 && !isCheck3 && isCheck2){
					if(legalOneChecked()){
						CosmicWimpoutActionRollSelectedDie rollSelectedAct =
								new CosmicWimpoutActionRollSelectedDie(this, isCheck1, isCheck2, isCheck3, isCheck4, isCheck5);
						game.sendAction(rollSelectedAct);
					}
					else{
						Toast.makeText(this.myActivity, "Cannot roll that die",
								Toast.LENGTH_SHORT).show();

					}
				}

				else{
					CosmicWimpoutActionRollSelectedDie rollSelectedAct =
							new CosmicWimpoutActionRollSelectedDie(this, isCheck1, isCheck2, isCheck3, isCheck4, isCheck5);
					game.sendAction(rollSelectedAct);
				}
			}
		}
		// send action to the game
	}// onClick

	/**
	 * callback method when we get a message (e.g., from the game)
	 *
	 * @param info
	 * 		the message
	 */
	@Override
	public void receiveInfo(GameInfo info) {
		if(info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo){
			flash(Color.RED, 50);
			//info instanceof IllegalMoveInfo ||
		}
		// ignore the message if it's not a CounterState message
		if (!(info instanceof CosmicWimpoutState)) return;

		// update our state; then update the display
		this.state = (CosmicWimpoutState) info;
		updateDisplay();
	}

	/**
	 * callback method--our game has been chosen/rechosen to be the GUI,
	 * called from the GUI thread
	 *
	 * @param activity
	 * 		the activity under which we are running
	 */
	public void setAsGui(GameMainActivity activity) {

		// remember the activity
		this.myActivity = activity;

		// Load the layout resource for our GUI
		activity.setContentView(R.layout.cosmicwimpout_human_player);

		this.player1Score = myActivity.findViewById(R.id.player1Score);
		this.player2Score = myActivity.findViewById(R.id.player2Score);
		this.player3Score = myActivity.findViewById(R.id.player3Score);
		this.player4Score = myActivity.findViewById(R.id.player4Score);
		this.turnScore = myActivity.findViewById(R.id.turnscore);

		this.endGameButton = myActivity.findViewById(R.id.endGameButton);
		this.endTurnButton = myActivity.findViewById(R.id.endTurnButton);
		this.rollDiceButton = myActivity.findViewById(R.id.rollDiceButton);
		this.rollSelectedButton = myActivity.findViewById(R.id.rollSelectedDieButton);

		this.die1 = myActivity.findViewById(R.id.die1);
		this.die2 = myActivity.findViewById(R.id.die2);
		this.die3 = myActivity.findViewById(R.id.die3);
		this.die4 = myActivity.findViewById(R.id.die4);
		this.die5 = myActivity.findViewById(R.id.die5);

		this.check1 = myActivity.findViewById(R.id.checkBox);
		this.check2 = myActivity.findViewById(R.id.checkBox2);
		this.check3 = myActivity.findViewById(R.id.checkBox3);
		this.check4 = myActivity.findViewById(R.id.checkBox4);
		this.check5 = myActivity.findViewById(R.id.checkBox5);


		endGameButton.setOnClickListener(this);
		endTurnButton.setOnClickListener(this);
		rollDiceButton.setOnClickListener(this);
		rollSelectedButton.setOnClickListener(this);
		check1.setOnClickListener(this);
		check2.setOnClickListener(this);
		check3.setOnClickListener(this);
		check4.setOnClickListener(this);
		check5.setOnClickListener(this);

		// if we have a game state, "simulate" that we have just received
		// the state from the game so that the GUI values are updated
		if (state != null) {
			receiveInfo(state);
		}
	}

	public boolean legalMoveAllChecked5(){
		if(state.checkAllFiveReRoll()){
			return true;
		}
		else{
			return false;
		}
		/*
		if(isCheck1 && isCheck2 && isCheck3 && isCheck4 &&isCheck5)
		{
			if(state.getDiceVal(0).equals("Moons") && state.getDiceVal(1).equals("Moons")
				&& state.getDiceVal(2).equals("Moons") &&state.getDiceVal(3).equals("Moons")
				&& state.getDiceVal(4).equals("Moons")) {

				return true;
			}
			else if(state.getDiceVal(0).equals("Triangles") && state.getDiceVal(1).equals("Triangles")
				&& state.getDiceVal(2).equals("Triangles")&& state.getDiceVal(3).equals("Triangles")
				&& state.getDiceVal(4).equals("Triangles")){

				return true;
			}
			else if( state.getDiceVal(0).equals("Bolts") && state.getDiceVal(1).equals("Bolts")
				&& state.getDiceVal(2).equals("Bolts") && state.getDiceVal(3).equals("Bolts")
				&& state.getDiceVal(4).equals("Bolts")){

				return true;
			}
			else if(state.getDiceVal(0).equals("Fives") && state.getDiceVal(1).equals("Fives")
				&& state.getDiceVal(2).equals("Fives") && state.getDiceVal(3).equals("Fives")
				&& state.getDiceVal(4).equals("Fives")){
				return true;
			}
			else if(state.getDiceVal(0).equals("Stars") && state.getDiceVal(1).equals("Stars")
				&& state.getDiceVal(2).equals("Stars") && state.getDiceVal(3).equals("Stars")
				&& state.getDiceVal(4).equals("Stars")){

				return true;
			}
			else{
				return false;
			}
		}
		*/
	}

	private boolean legalOneChecked(){
		if(isCheck1){
			boolean check = state.check1Die(0);
				if (check) {
					return true;
				}
				else {
                    return false;
                }

		}
		else if(isCheck2){
			boolean check = state.check1Die(1);
				if (check) {
					return true;
				}
				else {
                    return false;
                }

		}
		else if(isCheck3){
			boolean check = state.check1Die(2);
				if (check) {
					return true;
				}
                else {
                    return false;
                }
		}
		else if(isCheck4){
			boolean check = state.check1Die(3);
				if(check) {
					return true;
				}
                else {
                    return false;
                }
		}
		else if(isCheck5){
			boolean check = state.check1Die(4);
			if(check) {
				return true;
			}
            else {
                return false;
            }
		}
		return false;
	}

	//run method for the thread that will rotate through each face of the die once then land on the correct face
	//@Override
	/*
	public void run(){
		//for(int i = 0; i < redDieFaces.length; i++) {
			try {
				for(int i = 0; i < redDieFaces.length; i++) {
					Thread.sleep(20);
					if (rollDiceClicked) {
						this.die1.setImageResource(redDieFaces[i]);
						this.die2.setImageResource(redDieFaces[i]);
						this.die3.setImageResource(blackDieFaces[i]);
						this.die4.setImageResource(redDieFaces[i]);
						this.die5.setImageResource(redDieFaces[i]);
						//this.updateDisplay();
					}
					else if(rollSelectedClicked) {
						if (isCheck1) {
							this.die1.setImageResource(redDieFaces[i]);
						}
						if (isCheck2) {
							this.die2.setImageResource(redDieFaces[i]);
						}
						if (isCheck3) {
							this.die3.setImageResource(blackDieFaces[i]);
						}
						if (isCheck4) {
							this.die4.setImageResource(redDieFaces[i]);
						}
						if (isCheck5) {
							this.die5.setImageResource(redDieFaces[i]);
						}
					}
				}
				this.updateDisplay();
			} catch (Exception e) {/*do nothing*/
		//}
	//}

	public boolean legal2Die(){
		if(isCheck1 && isCheck2){
			if(this.state.check2Dice(0,1)){
				return true;
			}
			return false;
		}
		else if (isCheck1 && isCheck3){
			if(this.state.check2Dice(0,2)){
				return true;
			}
			return false;
		}
		else if(isCheck1 && isCheck3){
			if(this.state.check2Dice(0,2)){
				return true;
			}
			return false;
		}
		else if(isCheck1 && isCheck4){
			if(this.state.check2Dice(0,3)){
				return true;
			}
			return false;
		}
		else if(isCheck1 && isCheck5){
			if(this.state.check2Dice(0,4)){
				return true;
			}
			return false;
		}
		else if(isCheck2 && isCheck3){
			if(this.state.check2Dice(1,2)){
				return true;
			}
			return false;
		}
		else if(isCheck2 && isCheck4){
			if(this.state.check2Dice(1,3)){
				return true;
			}
			return false;
		}
		else if(isCheck2 && isCheck5){
			if(this.state.check2Dice(1,4)){
				return true;
			}
			return false;
		}
		else if(isCheck3 && isCheck4){
			if(this.state.check2Dice(2,3)){
				return true;
			}
			return false;
		}
		else if(isCheck3 && isCheck5){
			if(this.state.check2Dice(2,4)){
				return true;
			}
			return false;
		}
		else if(isCheck4 && isCheck5){
			if(this.state.check2Dice(3,4)){
				return true;
			}
			return false;
		}

		return false;
	}

	private boolean legal3Die(){
		if(isCheck1 && isCheck2 && isCheck3){
			if(this.state.check3Dice(0,1,2)){
				return true;
			}
			return false;
		}
		else if(isCheck1 && isCheck2 && isCheck4){
			if(this.state.check3Dice(0,1,3)){
				return true;
			}
			return false;
		}
		else if(isCheck1 && isCheck2 && isCheck5){
			if(this.state.check3Dice(0,1,4)){
				return true;
			}
			return false;
		}
		else if(isCheck1 && isCheck3 && isCheck4){
			if(this.state.check3Dice(0,2,3)){
				return true;
			}
			return false;
		}
		else if(isCheck1 && isCheck3 && isCheck5){
			if(this.state.check3Dice(0,2,4)){
				return true;
			}
			return false;
		}
		else if(isCheck1 && isCheck4 && isCheck5){
			if(this.state.check3Dice(0,3,4)){
				return true;
			}
			return false;
		}
		else if(isCheck2 && isCheck3 && isCheck4){
			if(this.state.check3Dice(1,2,3)){
				return true;
			}
			return false;
		}
		else if(isCheck2 && isCheck3 && isCheck5){
			if(this.state.check3Dice(1,2,4)){
				return true;
			}
			return false;
		}
		else if(isCheck2 && isCheck4 && isCheck5){
			if(this.state.check3Dice(1,3,4)){
				return true;
			}
			return false;
		}
		else if(isCheck3 && isCheck4 && isCheck5){
			if(this.state.check3Dice(2,3,4)){
				return true;
			}
			return false;
		}
		return false;
	}

	private boolean legal4Die(){
		if(isCheck1 && isCheck2 && isCheck3 && isCheck4){
			if(this.state.check4Dice(0,1,2,3)){
				return true;
			}
			return false;
		}
		else if(isCheck1 && isCheck2 && isCheck3 && isCheck5) {
			if(this.state.check4Dice(0,1,2,4)){
				return true;
			}
			return false;
		}
		else if(isCheck1 && isCheck2 && isCheck4 && isCheck5) {
			if(this.state.check4Dice(0,1,3,4)){
				return true;
			}
			return false;
		}
		else if(isCheck1 && isCheck3 && isCheck4 && isCheck5) {
			if(this.state.check4Dice(0,2,3,4)){
				return true;
			}
			return false;
		}
		else if(isCheck2 && isCheck3 && isCheck4 && isCheck5) {
			if(this.state.check4Dice(1,2,3,4)){
				return true;
			}
			return false;
		}
		return false;
	}
}// class CounterHumanPlayer

