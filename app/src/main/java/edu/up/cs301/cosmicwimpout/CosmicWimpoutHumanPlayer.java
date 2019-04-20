package edu.up.cs301.cosmicwimpout;

import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.io.Serializable;

/**
 * A GUI of a cosmic wimpout-player. The GUI displays the current value of the
 * current scores of all the players, the state of the dice, the current
 * turn score, and allows the human player to press the action buttons
 * in order to send moves to the game,
 *
 * @author Kayla Moore, Olivia Dendinger, Sam Lemly, David Campbell
 *  @version April 2019
 */

public class CosmicWimpoutHumanPlayer extends GameHumanPlayer implements OnClickListener, Serializable {

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
	int flash = 0;

	//variable for sound effects and background music
	private CosmicWimpoutSoundPlayer sound;
	private MediaPlayer music;

	//arrays that hold the die faces
	private int redDiceFaces[] = {R.drawable.ten, R.drawable.halfcircles, R.drawable.triangle,
			R.drawable.bolts, R.drawable.five, R.drawable.stars};
	private int blackDiceFaces[] = {R.drawable.blackten, R.drawable.blacktwocircles, R.drawable.flamingsun,
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
	 * sets display of the Cosmic Wimpout game
	 */
	protected void updateDisplay() {

		//set the text to current game state varibales
		this.player1Score.setText(allPlayerNames[0] + ": " + state.getPlayer1Score());
		this.player2Score.setText(allPlayerNames[1] + ": " + state.getPlayer2Score());
		this.player3Score.setText(allPlayerNames[2] + ": " + state.getPlayer3Score());
		this.player4Score.setText(allPlayerNames[3] + ": " + state.getPlayer4Score());
		this.turnScore.setText("Turn Score: " + state.getTurnScore() + "pts");

		//setting die 1 face to whatever the current die state is
		if(state.getDiceVal(0).equals("Tens")){
			this.die1.setImageResource(redDiceFaces[0]);
		}
		else if(state.getDiceVal(0).equals("Moons")){
			this.die1.setImageResource(redDiceFaces[1]);
		}
		else if(state.getDiceVal(0).equals("Triangles")){
			this.die1.setImageResource(redDiceFaces[2]);
		}
		else if(state.getDiceVal(0).equals("Bolts")){
			this.die1.setImageResource(redDiceFaces[3]);
		}
		else if(state.getDiceVal(0).equals("Fives")){
			this.die1.setImageResource(redDiceFaces[4]);
		}
		else if(state.getDiceVal(0).equals("Stars")){
			this.die1.setImageResource(redDiceFaces[5]);
		}

		//setting die to face
		if(state.getDiceVal(1).equals("Tens")){
			this.die2.setImageResource(redDiceFaces[0]);
		}
		else if(state.getDiceVal(1).equals("Moons")){
			this.die2.setImageResource(redDiceFaces[1]);
		}
		else if(state.getDiceVal(1).equals("Triangles")){
			this.die2.setImageResource(redDiceFaces[2]);
		}
		else if(state.getDiceVal(1).equals("Bolts")){
			this.die2.setImageResource(redDiceFaces[3]);
		}
		else if(state.getDiceVal(1).equals("Fives")){
			this.die2.setImageResource(redDiceFaces[4]);
		}
		else if(state.getDiceVal(1).equals("Stars")){
			this.die2.setImageResource(redDiceFaces[5]);
		}

		//setting die 3 face
		if(state.getDiceVal(2).equals("Tens")){
			this.die3.setImageResource(blackDiceFaces[0]);
		}
		else if(state.getDiceVal(2).equals("Moons")){
			this.die3.setImageResource(blackDiceFaces[1]);
		}
		else if(state.getDiceVal(2).equals("Flaming Sun")){
			this.die3.setImageResource(blackDiceFaces[2]);
		}
		else if(state.getDiceVal(2).equals("Bolts")){
			this.die3.setImageResource(blackDiceFaces[3]);
		}
		else if(state.getDiceVal(2).equals("Fives")){
			this.die3.setImageResource(blackDiceFaces[4]);
		}
		else if(state.getDiceVal(2).equals("Stars")){
			this.die3.setImageResource(blackDiceFaces[5]);
		}

		//setting die 4 face
		if(state.getDiceVal(3).equals("Tens")){
			this.die4.setImageResource(redDiceFaces[0]);
		}
		else if(state.getDiceVal(3).equals("Moons")){
			this.die4.setImageResource(redDiceFaces[1]);
		}
		else if(state.getDiceVal(3).equals("Triangles")){
			this.die4.setImageResource(redDiceFaces[2]);
		}
		else if(state.getDiceVal(3).equals("Bolts")){
			this.die4.setImageResource(redDiceFaces[3]);
		}
		else if(state.getDiceVal(3).equals("Fives")){
			this.die4.setImageResource(redDiceFaces[4]);
		}
		else if(state.getDiceVal(3).equals("Stars")){
			this.die4.setImageResource(redDiceFaces[5]);
		}

		//setting die 5 face
		if(state.getDiceVal(4).equals("Tens")){
			this.die5.setImageResource(redDiceFaces[0]);
		}
		else if(state.getDiceVal(4).equals("Moons")){
			this.die5.setImageResource(redDiceFaces[1]);
		}
		else if(state.getDiceVal(4).equals("Triangles")){
			this.die5.setImageResource(redDiceFaces[2]);
		}
		else if(state.getDiceVal(4).equals("Bolts")){
			this.die5.setImageResource(redDiceFaces[3]);
		}
		else if(state.getDiceVal(4).equals("Fives")){
			this.die5.setImageResource(redDiceFaces[4]);
		}
		else if(state.getDiceVal(4).equals("Stars")){
			this.die5.setImageResource(redDiceFaces[5]);
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

		//if roll is an instant winner, play win sound
		if(state.getIsInstantWinner()){
			sound.playWinner();
		}
		//if roll is a supernova, play lose sound
		if(state.getIsSuperNova()){
			sound.playLoser();
		}

		//checks if current player won or lost the game and plays win/lose sound effect
		switch(state.getWhoseTurn()){
			case 1:
				if(state.getPlayer1Score()< 500 && (state.getPlayer2Score() >= 500 ||
						state.getPlayer3Score() >= 500 || state.getPlayer4Score() >= 500)){
					sound.playLoser();
				}
				else if(state.getPlayer1Score() >= 500){
					sound.playWinner();
				}
				break;
			case 2:
				if(state.getPlayer2Score()< 500 && (state.getPlayer1Score() >= 500 ||
						state.getPlayer3Score() >= 500 || state.getPlayer4Score() >= 500)){
					sound.playLoser();
				}
				else if(state.getPlayer2Score() >= 500){
					sound.playWinner();
				}
				break;
			case 3:
				if(state.getPlayer3Score()< 500 && (state.getPlayer1Score() >= 500 ||
						state.getPlayer2Score() >= 500 || state.getPlayer4Score() >= 500)){
					sound.playLoser();
				}
				else if(state.getPlayer3Score() >= 500){
					sound.playWinner();
				}
				break;
			case 0:
				if(state.getPlayer4Score()< 500 && (state.getPlayer1Score() >= 500 ||
						state.getPlayer2Score() >= 500 || state.getPlayer3Score() >= 500)){
					sound.playLoser();
				}
				else if(state.getPlayer4Score() >= 500){
					sound.playWinner();
				}
				break;
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
		//if the button is any of the checkboxes, update variable to true
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

		//if button is pressed, send action
		CosmicWimpoutActionEndTurn endTurnAct =
				new CosmicWimpoutActionEndTurn(this);
		CosmicWimpoutActionRollAllDice rollAct =
				new CosmicWimpoutActionRollAllDice(this);
		CosmicWimpoutActionEndGame endGameAct =
				new CosmicWimpoutActionEndGame(this);

		//making sure that they press the roll all dice button first
		if(this.actionsPressed == 0 || this.state.getTurnScore() == 0){
			if(button == rollDiceButton ){
				rollDiceClicked = true;
				sound.playFiveDice();
				//rolling animation
				for(int i = 0; i < redDiceFaces.length; i++) {
					this.die1.setImageResource(redDiceFaces[i]);
					this.die2.setImageResource(redDiceFaces[i]);
					this.die3.setImageResource(blackDiceFaces[i]);
					this.die4.setImageResource(redDiceFaces[i]);
					this.die5.setImageResource(redDiceFaces[i]);
				}
				game.sendAction(rollAct);
				actionsPressed++;
				flash = 0;

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
				if(this.state.getIsFlash() && flash == 0){
					Toast.makeText(this.myActivity, "Must Clear the Flash!",
							Toast.LENGTH_SHORT).show();
				}
				else {
					this.actionsPressed = 0;
					game.sendAction(endTurnAct);
					//clears checkboxes when player ends their turn
					check1.setChecked(false);
					check2.setChecked(false);
					check3.setChecked(false);
					check4.setChecked(false);
					check5.setChecked(false);
				}
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
				if(checkCount == 1 && legalOneChecked()){
					sound.playOneDie();
				}
				else if(checkCount == 2 && legal2Die()){
					sound.playTwoDice();
				}
				else if(checkCount == 3 && legal3Die()){
					sound.playThreeDice();
				}
				else if(checkCount == 4 && legal4Die()){
					sound.playFourDice();
				}
				else if(checkCount == 5 && legalMoveAllChecked5()){
					sound.playFiveDice();
				}

				if(this.state.getIsFlash() && !legalMoveAllChecked5() && flash == 0){
					this.flashReRoll();
                    if(legal2Die()){
                        CosmicWimpoutActionRollSelectedDie rollSelectedDie =
                                new CosmicWimpoutActionRollSelectedDie(this, isCheck1,
                                        isCheck2, isCheck3, isCheck4, isCheck5);
                        game.sendAction(rollSelectedDie);
                    }
					else if(isCheck1 && !isCheck5 && !isCheck4 && !isCheck3 && !isCheck2){
						if(legalOneChecked()){
							CosmicWimpoutActionRollSelectedDie rollSelectedAct =
									new CosmicWimpoutActionRollSelectedDie(this, isCheck1,
											isCheck2, isCheck3, isCheck4, isCheck5);
							//rolling animation
							for(int i = 0; i < redDiceFaces.length; i++) {
								this.die1.setImageResource(redDiceFaces[i]);
							}
							game.sendAction(rollSelectedAct);
						}
					}
					else if(!isCheck1 && isCheck5 && !isCheck4 && !isCheck3 && !isCheck2){
						if(legalOneChecked()){
							CosmicWimpoutActionRollSelectedDie rollSelectedAct =
									new CosmicWimpoutActionRollSelectedDie(this, isCheck1, isCheck2, isCheck3, isCheck4, isCheck5);
							//rolling animation
							for(int i = 0; i < redDiceFaces.length; i++) {
								this.die5.setImageResource(redDiceFaces[i]);
							}
							game.sendAction(rollSelectedAct);

						}
					}
					else if(!isCheck1 && !isCheck5 && isCheck4 && !isCheck3 && !isCheck2){
						if(legalOneChecked()){
							CosmicWimpoutActionRollSelectedDie rollSelectedAct =
									new CosmicWimpoutActionRollSelectedDie(this, isCheck1, isCheck2, isCheck3, isCheck4, isCheck5);
							//rolling animation
							for(int i = 0; i < redDiceFaces.length; i++) {
								this.die4.setImageResource(redDiceFaces[i]);
							}
							game.sendAction(rollSelectedAct);

						}
					}
					else if(!isCheck1 && !isCheck5 && !isCheck4 && isCheck3 && !isCheck2){
						if(legalOneChecked()){
							CosmicWimpoutActionRollSelectedDie rollSelectedAct =
									new CosmicWimpoutActionRollSelectedDie(this, isCheck1, isCheck2, isCheck3, isCheck4, isCheck5);
							//rolling animation
							for(int i = 0; i < redDiceFaces.length; i++) {
								this.die3.setImageResource(blackDiceFaces[i]);
							}
							game.sendAction(rollSelectedAct);

						}
					}
					else if(!isCheck1 &&  !isCheck5 && !isCheck4 && !isCheck3 && isCheck2) {
						if (legalOneChecked()) {
							CosmicWimpoutActionRollSelectedDie rollSelectedAct =
									new CosmicWimpoutActionRollSelectedDie(this, isCheck1, isCheck2, isCheck3, isCheck4, isCheck5);
							//rolling animation
							for (int i = 0; i < redDiceFaces.length; i++) {
								this.die2.setImageResource(redDiceFaces[i]);
							}
							game.sendAction(rollSelectedAct);
						}
					}
					flash++;
				}
				else if(legalMoveAllChecked5()){
					if(isCheck1 && isCheck2 && isCheck3 && isCheck4 && isCheck5){
						this.state.setIsFlash(false);
						CosmicWimpoutActionRollSelectedDie rollSelectedAct =
								new CosmicWimpoutActionRollSelectedDie(this, isCheck1,
										isCheck2, isCheck3, isCheck4, isCheck5);
						this.state.setIsFlash(false);
						game.sendAction(rollSelectedAct);
						flash = 0;
					}
					else{
						Toast.makeText(this.myActivity, "Must roll all dice",
								Toast.LENGTH_SHORT).show();
					}
				}
				//checking for legal move if all 5 are checked
				else if(isCheck1 && isCheck2 && isCheck3 && isCheck4 && isCheck5) {
					if (legalMoveAllChecked5()) {
						this.state.setIsFlash(false);
						CosmicWimpoutActionRollSelectedDie rollSelectedAct =
								new CosmicWimpoutActionRollSelectedDie(this, isCheck1,
										isCheck2, isCheck3, isCheck4, isCheck5);
						//rolling animation
						for(int i = 0; i < redDiceFaces.length; i++) {
							this.die1.setImageResource(redDiceFaces[i]);
							this.die2.setImageResource(redDiceFaces[i]);
							this.die3.setImageResource(blackDiceFaces[i]);
							this.die4.setImageResource(redDiceFaces[i]);
							this.die5.setImageResource(redDiceFaces[i]);
						}
						flash = 0;
						this.state.setIsFlash(false);
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
						//rolling animation
						if(isCheck1){
							for(int i = 0; i < redDiceFaces.length; i++) {
								this.die1.setImageResource(redDiceFaces[i]);
							}
						}
						if(isCheck2){
							for(int i = 0; i < redDiceFaces.length; i++) {
								this.die2.setImageResource(redDiceFaces[i]);
							}
						}
						if(isCheck3){
							for(int i = 0; i < redDiceFaces.length; i++) {
								this.die3.setImageResource(blackDiceFaces[i]);
							}
						}
						if(isCheck4){
							for(int i = 0; i < redDiceFaces.length; i++) {
								this.die4.setImageResource(redDiceFaces[i]);
							}
						}
						if(isCheck5){
							for(int i = 0; i < redDiceFaces.length; i++) {
								this.die5.setImageResource(redDiceFaces[i]);
							}
						}
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
						//rolling animation
						if(isCheck1){
							for(int i = 0; i < redDiceFaces.length; i++) {
								this.die1.setImageResource(redDiceFaces[i]);
							}
						}
						if(isCheck2){
							for(int i = 0; i < redDiceFaces.length; i++) {
								this.die2.setImageResource(redDiceFaces[i]);
							}
						}
						if(isCheck3){
							for(int i = 0; i < redDiceFaces.length; i++) {
								this.die3.setImageResource(blackDiceFaces[i]);
							}
						}
						if(isCheck4){
							for(int i = 0; i < redDiceFaces.length; i++) {
								this.die4.setImageResource(redDiceFaces[i]);
							}
						}
						if(isCheck5){
							for(int i = 0; i < redDiceFaces.length; i++) {
								this.die5.setImageResource(redDiceFaces[i]);
							}
						}
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
						//rolling animation
						if(isCheck1){
							for(int i = 0; i < redDiceFaces.length; i++) {
								this.die1.setImageResource(redDiceFaces[i]);
							}
						}
						if(isCheck2){
							for(int i = 0; i < redDiceFaces.length; i++) {
								this.die2.setImageResource(redDiceFaces[i]);
							}
						}
						if(isCheck3){
							for(int i = 0; i < redDiceFaces.length; i++) {
								this.die3.setImageResource(blackDiceFaces[i]);
							}
						}
						if(isCheck4){
							for(int i = 0; i < redDiceFaces.length; i++) {
								this.die4.setImageResource(redDiceFaces[i]);
							}
						}
						if(isCheck5){
							for(int i = 0; i < redDiceFaces.length; i++) {
								this.die5.setImageResource(redDiceFaces[i]);
							}
						}
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
						//rolling animation
						for(int i = 0; i < redDiceFaces.length; i++) {
							this.die1.setImageResource(redDiceFaces[i]);
						}
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
						//rolling animation
						for(int i = 0; i < redDiceFaces.length; i++) {
							this.die5.setImageResource(redDiceFaces[i]);
						}
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
						//rolling animation
						for(int i = 0; i < redDiceFaces.length; i++) {
							this.die4.setImageResource(redDiceFaces[i]);
						}
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
						//rolling animation
						for(int i = 0; i < redDiceFaces.length; i++) {
							this.die3.setImageResource(blackDiceFaces[i]);
						}
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
						//rolling animation
						for(int i = 0; i < redDiceFaces.length; i++) {
							this.die2.setImageResource(redDiceFaces[i]);
						}
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
			Toast.makeText(this.myActivity, "Not Your Turn", Toast.LENGTH_SHORT).show();
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

		//initialize sound player
		this.sound = new CosmicWimpoutSoundPlayer(myActivity);

		//initializes media player and plays background music
		music = MediaPlayer.create(myActivity, R.raw.bensound_theelevatorbossanova);
		music.start();

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

	/**
	 * method legalMoveAllChecked5
	 * @return returns true if all five dice are checked and it is legal to re-roll
	 */
	public boolean legalMoveAllChecked5(){
		return state.checkAllFiveReRoll();
	}

	/**
	 * legalOneChecked method
	 * @return returns true if the seleced die is legal to roll
	 */
	private boolean legalOneChecked(){
		if(isCheck1){
			return state.check1Die(0);
		}
		else if(isCheck2){
			return state.check1Die(1);
		}
		else if(isCheck3){
			return state.check1Die(2);
		}
		else if(isCheck4){
			return state.check1Die(3);
		}
		else if(isCheck5){
			return state.check1Die(4);
		}
		return false;
	}

	/**
	 * legal2Die method
	 * @return returns true if it is legal to roll the 2 checked dice
	 */
	public boolean legal2Die(){
		if(isCheck1 && isCheck2){
			return this.state.check2Dice(0,1);
		}
		else if(isCheck1 && isCheck3){
			return this.state.check2Dice(0,2);
		}
		else if(isCheck1 && isCheck4){
			return this.state.check2Dice(0,3);
		}
		else if(isCheck1 && isCheck5){
			return this.state.check2Dice(0,4);
		}
		else if(isCheck2 && isCheck3){
			return this.state.check2Dice(1,2);
		}
		else if(isCheck2 && isCheck4){
			return this.state.check2Dice(1,3);
		}
		else if(isCheck2 && isCheck5){
			return this.state.check2Dice(1,4);
		}
		else if(isCheck3 && isCheck4){
			return this.state.check2Dice(2,3);
		}
		else if(isCheck3 && isCheck5){
			return this.state.check2Dice(2,4);
		}
		else if(isCheck4 && isCheck5){
			return this.state.check2Dice(3,4);
		}
		return false;
	}

	/**
	 * legal3Die method
	 * @return returns true if it is legal to roll the selected 3 dice
	 */
	private boolean legal3Die(){
		if(isCheck1 && isCheck2 && isCheck3){
			return this.state.check3Dice(0,1,2);
		}
		else if(isCheck1 && isCheck2 && isCheck4){
			return this.state.check3Dice(0,1,3);
		}
		else if(isCheck1 && isCheck2 && isCheck5){
			return this.state.check3Dice(0,1,4);
		}
		else if(isCheck1 && isCheck3 && isCheck4){
			return this.state.check3Dice(0,2,3);
		}
		else if(isCheck1 && isCheck3 && isCheck5){
			return this.state.check3Dice(0,2,4);
		}
		else if(isCheck1 && isCheck4 && isCheck5){
			return this.state.check3Dice(0,3,4);
		}
		else if(isCheck2 && isCheck3 && isCheck4){
			return this.state.check3Dice(1,2,3);
		}
		else if(isCheck2 && isCheck3 && isCheck5){
			return this.state.check3Dice(1,2,4);
		}
		else if(isCheck2 && isCheck4 && isCheck5){
			return this.state.check3Dice(1,3,4);
		}
		else if(isCheck3 && isCheck4 && isCheck5){
			return this.state.check3Dice(2,3,4);
		}
		return false;
	}

	/**
	 * legal4Die method
	 * @return returns true if it is legal to roll the selected 4 dice
	 */
	private boolean legal4Die(){
		if(isCheck1 && isCheck2 && isCheck3 && isCheck4){
			return this.state.check4Dice(0,1,2,3);
		}
		else if(isCheck1 && isCheck2 && isCheck3 && isCheck5) {
			return this.state.check4Dice(0,1,2,4);
		}
		else if(isCheck1 && isCheck2 && isCheck4 && isCheck5) {
			return this.state.check4Dice(0,1,3,4);
		}
		else if(isCheck1 && isCheck3 && isCheck4 && isCheck5) {
			return this.state.check4Dice(0,2,3,4);
		}
		else if(isCheck2 && isCheck3 && isCheck4 && isCheck5) {
			return this.state.check4Dice(1,2,3,4);
		}
		return false;
	}

	/**
	 * flashReRoll method sends messages to the player when they need to re-roll
	 * certain dice to clear a flash
	 */
	private void flashReRoll(){
		boolean[] reRolls = this.state.flashReRoll();
		boolean die1 = reRolls[0];
		boolean die2 = reRolls[1];
		boolean die3 = reRolls[2];
		boolean die4 = reRolls[3];
		boolean die5 = reRolls[4];

		if(die1 && die2){
			Toast.makeText(this.myActivity, "Must Reroll dice 1 and 2",
					Toast.LENGTH_SHORT).show();
		}
		else if (die1 && die3){
			Toast.makeText(this.myActivity, "Must Reroll dice 1 and 3",
					Toast.LENGTH_SHORT).show();
		}
		else if(die1 && die4){
			Toast.makeText(this.myActivity, "Must Reroll dice 1 and 4",
					Toast.LENGTH_SHORT).show();
		}
		else if(die1 && die5){
			Toast.makeText(this.myActivity, "Must Reroll dice 1 and 5",
					Toast.LENGTH_SHORT).show();
		}
		else if(die2 && die3){
			Toast.makeText(this.myActivity, "Must Reroll dice 2 and 3",
					Toast.LENGTH_SHORT).show();
		}
		else if(die2 && die4){
			Toast.makeText(this.myActivity, "Must Reroll dice 2 and 4",
					Toast.LENGTH_SHORT).show();
		}
		else if(die2 && die5){
			Toast.makeText(this.myActivity, "Must Reroll dice 2 and 5",
					Toast.LENGTH_SHORT).show();
		}
		else if(die3 && die4){
			Toast.makeText(this.myActivity, "Must Reroll dice 3 and 4",
					Toast.LENGTH_SHORT).show();
		}
		else if(die3 && die5){
			Toast.makeText(this.myActivity, "Must Reroll dice 3 and 5",
					Toast.LENGTH_SHORT).show();
		}
		else if(die4 && die5){
			Toast.makeText(this.myActivity, "Must Reroll dice 4 and 5",
					Toast.LENGTH_SHORT).show();
		}
		else if(die1){
			Toast.makeText(this.myActivity, "Must Reroll die 1",
					Toast.LENGTH_SHORT).show();
		}
		else if(die2){
			Toast.makeText(this.myActivity, "Must Reroll die 2",
					Toast.LENGTH_SHORT).show();
		}
		else if(die3){
			Toast.makeText(this.myActivity, "Must Reroll die 3",
					Toast.LENGTH_SHORT).show();
		}
		else if(die4){
			Toast.makeText(this.myActivity, "Must Reroll die 4",
					Toast.LENGTH_SHORT).show();
		}
		else if(die5){
			Toast.makeText(this.myActivity, "Must Reroll die 5",
					Toast.LENGTH_SHORT).show();
		}
	}

}// class CounterHumanPlayer

