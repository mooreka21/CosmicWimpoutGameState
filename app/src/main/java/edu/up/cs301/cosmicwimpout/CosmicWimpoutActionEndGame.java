package edu.up.cs301.cosmicwimpout;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * this is the end game action
 *
 * @author Sam Lemly
 *  * @author Olivia Dendinger
 *  * @author David Campbell
 *  * @author Kayla Moore
 * @version March 2019
 */
public class CosmicWimpoutActionEndGame extends GameAction {
    private static final long serialVersionUID= 210598428L;
    //invokes super constructor
    public CosmicWimpoutActionEndGame(GamePlayer player){
        super(player);
    }
}
