package edu.up.cs301.cosmicwimpout;

import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.R;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameInfo;
import edu.up.cs301.game.infoMsg.IllegalMoveInfo;
import edu.up.cs301.game.infoMsg.NotYourTurnInfo;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * A GUI of a counter-player. The GUI displays the current value of the counter,
 * and allows the human player to press the '+' and '-' buttons in order to
 * send moves to the game.
 *
 * Just for fun, the GUI is implemented so that if the player presses either button
 * when the counter-value is zero, the screen flashes briefly, with the flash-color
 * being dependent on whether the player is player 0 or player 1.
 *
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @version July 2013
 */
public class CosmicWimpoutHumanPlayer extends GameHumanPlayer implements OnClickListener {

	/* instance variables */

	// The TextView the displays the current counter value
	private TextView player1Score;
	private TextView player2Score;
	private TextView player3Score;
	private TextView player4Score;

	private Button endGameButton = null;
	private Button endTurnButton = null;
	private Button rollDiceButton = null;
	private Button rollSelectedButton = null;

	private ImageView die1, die2, die3, die4, die5;

	private CheckBox check1, check2, check3, check4, check5;
	private boolean isCheck1 = false;
	private boolean isCheck2 = false;
	private boolean isCheck3 = false;
	private boolean isCheck4 = false;
	private boolean isCheck5 = false;

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

		this.player1Score.setText("Player1: " + state.getPlayer1Score());
		this.player2Score.setText("Player2: " + state.getPlayer2Score());
		this.player3Score.setText("Player3: " + state.getPlayer3Score());

		if(state.getDiceVal(0).equals("Tens")){
			this.die1.setImageResource(R.drawable.ten);
		}
		else if(state.getDiceVal(0).equals("Moons")){
			this.die1.setImageResource(R.drawable.halfcircles);
		}
		else if(state.getDiceVal(0).equals("Triangles")){
			this.die1.setImageResource(R.drawable.triangle);
		}
		else if(state.getDiceVal(0).equals("Bolts")){
			this.die1.setImageResource(R.drawable.bolts);
		}
		else if(state.getDiceVal(0).equals("Fives")){
			this.die1.setImageResource(R.drawable.five);
		}
		else if(state.getDiceVal(0).equals("Stars")){
			this.die1.setImageResource(R.drawable.stars);
		}

		if(state.getDiceVal(1).equals("Tens")){
			this.die2.setImageResource(R.drawable.ten);
		}
		else if(state.getDiceVal(1).equals("Moons")){
			this.die2.setImageResource(R.drawable.halfcircles);
		}
		else if(state.getDiceVal(1).equals("Triangles")){
			this.die2.setImageResource(R.drawable.triangle);
		}
		else if(state.getDiceVal(1).equals("Bolts")){
			this.die2.setImageResource(R.drawable.bolts);
		}
		else if(state.getDiceVal(1).equals("Fives")){
			this.die2.setImageResource(R.drawable.five);
		}
		else if(state.getDiceVal(1).equals("Stars")){
			this.die2.setImageResource(R.drawable.stars);
		}

		if(state.getDiceVal(2).equals("Tens")){
			this.die3.setImageResource(R.drawable.blackten);
		}
		else if(state.getDiceVal(2).equals("Moons")){
			this.die3.setImageResource(R.drawable.blacktwocircles);
		}
		else if(state.getDiceVal(2).equals("Flaming Sun")){
			this.die3.setImageResource(R.drawable.flamingsun);
		}
		else if(state.getDiceVal(2).equals("Bolts")){
			this.die3.setImageResource(R.drawable.blackbolt);
		}
		else if(state.getDiceVal(2).equals("Fives")){
			this.die3.setImageResource(R.drawable.blackfive);
		}
		else if(state.getDiceVal(2).equals("Stars")){
			this.die3.setImageResource(R.drawable.blackstar);
		}

		if(state.getDiceVal(3).equals("Tens")){
			this.die4.setImageResource(R.drawable.ten);
		}
		else if(state.getDiceVal(3).equals("Moons")){
			this.die4.setImageResource(R.drawable.halfcircles);
		}
		else if(state.getDiceVal(3).equals("Triangles")){
			this.die4.setImageResource(R.drawable.triangle);
		}
		else if(state.getDiceVal(3).equals("Bolts")){
			this.die4.setImageResource(R.drawable.bolts);
		}
		else if(state.getDiceVal(3).equals("Fives")){
			this.die4.setImageResource(R.drawable.five);
		}
		else if(state.getDiceVal(3).equals("Stars")){
			this.die4.setImageResource(R.drawable.stars);
		}

		if(state.getDiceVal(4).equals("Tens")){
			this.die5.setImageResource(R.drawable.ten);
		}
		else if(state.getDiceVal(4).equals("Moons")){
			this.die5.setImageResource(R.drawable.halfcircles);
		}
		else if(state.getDiceVal(4).equals("Triangles")){
			this.die5.setImageResource(R.drawable.triangle);
		}
		else if(state.getDiceVal(4).equals("Bolts")){
			this.die5.setImageResource(R.drawable.bolts);
		}
		else if(state.getDiceVal(4).equals("Fives")){
			this.die5.setImageResource(R.drawable.five);
		}
		else if(state.getDiceVal(4).equals("Stars")){
			this.die5.setImageResource(R.drawable.stars);
		}

		int turn = this.state.getWhoseTurn();
		if(turn == 0){
			this.player1Score.setTextColor(0xFFFF0000);
			this.player2Score.setTextColor(0xFF000000);
		}
		if(turn == 1){
			this.player2Score.setTextColor(0xFFFF0000);
			this.player1Score.setTextColor(0xFF000000);
		}

	}

	/**
	 * this method gets called when the user clicks the '+' or '-' button. It
	 * creates a new CounterMoveAction to return to the parent activity.
	 *
	 * @param button
	 * 		the button that was clicked
	 */
	public void onClick(View button) {

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

		CosmicWimpoutActionEndTurn endTurnAct =
				new CosmicWimpoutActionEndTurn(this);
		CosmicWimpoutActionRollAllDice rollAct =
				new CosmicWimpoutActionRollAllDice(this);
		CosmicWimpoutActionEndGame endGameAct =
				new CosmicWimpoutActionEndGame(this);
		//CosmicWimpoutActionRollSelectedDie rollSelectedAct =
			//	new CosmicWimpoutActionRollSelectedDie(this, isCheck1, isCheck2, isCheck3, isCheck4,isCheck5);

		if(actionsPressed == 0){
			if(button == rollDiceButton ){
				game.sendAction(rollAct);
				actionsPressed++;
			}
			else {
				Toast.makeText(this.myActivity, "Illegal Move! Must" +
						" Roll All Dice First!", Toast.LENGTH_LONG).show();
			}
		}

		else if (actionsPressed > 0){

			if(button == endGameButton){
				game.sendAction(endGameAct);
			}
			else if(button == endTurnButton){
				game.sendAction(endTurnAct);
				actionsPressed = 0;
			}
			else if(button == rollDiceButton){
				game.sendAction(rollAct);
			}
			else if(button == rollSelectedButton) {
				if (!(check1.isChecked())) {
					isCheck1 = false;
				}
				else {
					isCheck1 = true;
				}
				if (!(check2.isChecked())) {
					isCheck2 = false;
				}
				else {
					isCheck2 = true;
				}
				if (!(check3.isChecked())) {
					isCheck3 = false;
				}
				else {
					isCheck3 = true;
				}
				if (!(check4.isChecked())) {
					isCheck4 = false;
				} else {
					isCheck4 = true;
				}
				if (!(check5.isChecked())) {
					isCheck5 = false;
				} else {
					isCheck5 = true;
				}
				CosmicWimpoutActionRollSelectedDie rollSelectedAct =
						new CosmicWimpoutActionRollSelectedDie(this, isCheck1, isCheck2, isCheck3, isCheck4, isCheck5);
				game.sendAction(rollSelectedAct);
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

}// class CounterHumanPlayer

