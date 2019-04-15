package edu.up.cs301.cosmicwimpout;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * this is the end turn action
 *
 * @author Sam Lemly
 *  * @author Olivia Dendinger
 *  * @author David Campbell
 *  * @author Kayla Moore
 * @version March 2019
 */
public class CosmicWimpoutActionEndTurn extends GameAction {
    private static final long serialVersionUID= 100598440L;
    //invokes super constructor
    public CosmicWimpoutActionEndTurn(GamePlayer player){
        super(player);
    }
}
