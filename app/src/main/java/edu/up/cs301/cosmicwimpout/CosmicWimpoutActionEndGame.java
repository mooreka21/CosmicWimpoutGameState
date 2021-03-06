package edu.up.cs301.cosmicwimpout;

import java.io.Serializable;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * this is the end game action
 *
 * @author Sam Lemly, Olivia Dendinger, David Campbell, Kayla Moore
 * @version March 2019
 */
public class CosmicWimpoutActionEndGame extends GameAction implements Serializable {
    //seralizable id
    private static final long serialVersionUID= 210598428L;

    /**
     * Ctor for CosmicWimpoutActionEndGame invokes super constructor
     * @param player - current player
     */
    public CosmicWimpoutActionEndGame(GamePlayer player){
        super(player);
    }
}
