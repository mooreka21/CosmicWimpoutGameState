package edu.up.cs301.counter;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.infoMsg.GameInfo;

/**
 * A computer-version of a counter-player.  Since this is such a simple game,
 * it just sends "+" and "-" commands with equal probability, at an average
 * rate of one per second. 
 * 
 * @author Steven R. Vegdahl
 * @author Andrew M. Nuxoll
 * @version September 2013
 */
public class CosmicWimpoutComputerPlayer extends GameComputerPlayer {

	//instance variables
	private int ScoresFromCopy[] = new int[];
	private int numRollsThisTurn;
	private float odds;
	private int intelligence;
    /**
     * Constructor for objects of class CosmicWimpoutComputerPlayer
     * 
     * @param name
     * 		the player's name
     */
    public CosmicWimpoutComputerPlayer(String name) {
        // invoke superclass constructor
        super(name);
        
        // start the timer, ticking 20 times per second
        //getTimer().setInterval(50);
        //getTimer().start();
    }
    
    /**
     * callback method--game's state has changed
     * 
     * @param info
     * 		the information (presumably containing the game's state)
     */
	@Override
	protected void receiveInfo(GameInfo info) {
		// Do nothing, as we ignore all state in deciding our next move. It
		// depends totally on the timer and random numbers.
	}

}
