package edu.up.cs301.cosmicwimpout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;
import java.util.ArrayList;

import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.config.GameConfig;
import edu.up.cs301.game.config.GamePlayerType;

/**
 * this is the primary activity for Cosmic Wimpout game
 *
 * @author Kayla Moore, Olivia Dendinger, Sam Lemly, David Campbell
 *  @version April 2019
 */
public class CosmicWimpoutMainActivity extends GameMainActivity implements Serializable {

	// the port number that this game will use when playing over the network
	private static final int PORT_NUMBER = 2234;
	private static final long serialVersionUID= 5006874387L;

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
		// a computer player type (player type 1)
		playerTypes.add(new GamePlayerType("Computer Player") {
			public GamePlayer createPlayer(String name) {
				return new CosmicWimpoutComputerPlayer(name);
			}});

		// a computer player type (player type 2)
		playerTypes.add(new GamePlayerType("Smart Computer Player") {
			public GamePlayer createPlayer(String name) {
				return new CosmicWimpoutComputerPlayer2(name);
			}});

		// Create a game configuration class for Counter:
		// - player types as given above
		// - from 1 to 2 players
		// - name of game is "Counter Game"
		// - port number as defined above
		GameConfig defaultConfig = new GameConfig(playerTypes, 4, 4, "CosmicWimpout Game",
				PORT_NUMBER);

		// Add the default players to the configuration
		defaultConfig.addPlayer("Human", 0); // player 1: a human player
		defaultConfig.addPlayer("Siri", 1); // player 2: a computer player
		defaultConfig.addPlayer("Alexa", 2); // player 3: a computer player
		defaultConfig.addPlayer("HAL", 3); // player 4: a computer player
		
		// Set the default remote-player setup:
		// - player name: "Remote Player"
		// - IP code: (empty string)
		// - default player type: human player
		defaultConfig.setRemoteData("Remote Player", "", 0);
		
		// return the configuration
		return defaultConfig;
	}//createDefaultConfig

	/**
	 * creates a local cosmic wimpout game
	 * 
	 * @return
	 * 		the local game, a cosmic wimpout game
	 */
	@Override
	public LocalGame createLocalGame() { return new CosmicWimpoutLocalGame(); }

}
