package edu.up.cs301.cosmicwimpout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.config.GameConfig;
import edu.up.cs301.game.config.GamePlayerType;

/**
 * this is the primary activity for Counter game
 * 
 * @author Andrew M. Nuxoll
 * @author Steven R. Vegdahl
 * @version July 2013
 */
public class CosmicWimpoutMainActivity extends GameMainActivity {
	
	// the port number that this game will use when playing over the network
	private static final int PORT_NUMBER = 2234;

	//declaring Views
	Button runTest;
	EditText edit;

	/**
	 * onCreate method is the first method called when the app runs
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//initializing runTest and edit
		runTest = findViewById(R.id.runTestButton);
		runTest.setOnClickListener(this);

		edit = findViewById(R.id.editText);
	}

	/**
	 * onClick is a method implemented from the View.OnClickListener interface
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		edit.setText(""); // clear any text user entered before

		//create an instance of the game state and copy it
		CosmicWimpoutState firstInstance = new CosmicWimpoutState();
		CosmicWimpoutState secondInstance = new CosmicWimpoutState(firstInstance);

		int whoRolled = firstInstance.getWhoseTurn(); //whose turn is it

		//roll 3 individual die to test that the rollSingleDie() method works
		firstInstance.rollSingleDie(whoRolled,2);
		firstInstance.rollSingleDie(whoRolled,4);
		firstInstance.rollSingleDie(whoRolled,1);

		//append text to show what they rolled
		edit.append("Player " + whoRolled + " has rolled, " + firstInstance.getDiceVal(0) + ", "
				+ firstInstance.getDiceVal(1) + " and "
				+ firstInstance.getDiceVal(3) + "\n");

		/*get whose turn it is now cause they could of wimped out, if the player wimped out
		 * then it will switch to the next player's turn*/
		int nowWhoRolls = firstInstance.getWhoseTurn();
		//that player rolls all the die
		firstInstance.rollAllDice(nowWhoRolls);
		//edit append text to show what they rolled
		edit.append("Player " + nowWhoRolls + " has rolled, " + firstInstance.getDiceVal(0) + ", "
				+ firstInstance.getDiceVal(1) + ", " + firstInstance.getDiceVal(2) +
				", " + firstInstance.getDiceVal(3) + ", and  " +
				firstInstance.getDiceVal(4) + "\n");

		//get whose turn it is
		int whoseTurnBefore = firstInstance.getWhoseTurn();

		//that player ends the turn
		firstInstance.endTurn(whoseTurnBefore);

		//get whose turn it is after they end it
		int whoseTurn = firstInstance.getWhoseTurn();

		//append text to show whose turn it is now
		edit.append("Player " + whoseTurnBefore + " has ended their turn, it is now" +
				" Player " + whoseTurn +" turn\n");

		//player ends game
		firstInstance.endGame(2);
		edit.append("Player " + whoseTurn + " ended the game.\n");

		//create another instance of the game state and another copy
		CosmicWimpoutState thirdInstance = new CosmicWimpoutState();
		CosmicWimpoutState fourthInstance = new CosmicWimpoutState(thirdInstance);

		//show that the copies are the same
		edit.append("Second Instance: " + secondInstance.toString() + " ");
		edit.append("Fourth Instance: " + fourthInstance.toString() + " ");
	}

	/**
	 * Create the default configuration for this game:
	 * - one human player vs. one computer player
	 * - minimum of 1 player, maximum of 2
	 * - one kind of computer player and one kind of human player available
	 * 
	 * @return
	 * 		the new configuration object, representing the default configuration
	 */
	@Override
	public GameConfig createDefaultConfig() {
		
		// Define the allowed player types
		ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();
		
		// a human player player type (player type 0)
		playerTypes.add(new GamePlayerType("Local Human Player") {
			public GamePlayer createPlayer(String name) {
				return new CosmicWimpoutHumanPlayer(name);
			}});
		
		// a computer player type (player type 1)
		playerTypes.add(new GamePlayerType("Computer Player") {
			public GamePlayer createPlayer(String name) {
				return new CosmicWimpoutComputerPlayer(name);
			}});
		
		// a computer player type (player type 2)
		/*playerTypes.add(new GamePlayerType("Computer Player (GUI)") {
			public GamePlayer createPlayer(String name) {
				return new CounterComputerPlayer2(name);
			}});*/

		// Create a game configuration class for Counter:
		// - player types as given above
		// - from 1 to 2 players
		// - name of game is "Counter Game"
		// - port number as defined above
		GameConfig defaultConfig = new GameConfig(playerTypes, 1, 2, "CosmicWimpout Game",
				PORT_NUMBER);

		// Add the default players to the configuration
		defaultConfig.addPlayer("Human", 0); // player 1: a human player
		defaultConfig.addPlayer("Computer", 1); // player 2: a computer player
		
		// Set the default remote-player setup:
		// - player name: "Remote Player"
		// - IP code: (empty string)
		// - default player type: human player
		defaultConfig.setRemoteData("Remote Player", "", 0);
		
		// return the configuration
		return defaultConfig;
	}//createDefaultConfig

	/**
	 * create a local game
	 * 
	 * @return
	 * 		the local game, a counter game
	 */
	@Override
	public LocalGame createLocalGame() {
		return new CosmicWimpoutLocalGame();
	}

}
