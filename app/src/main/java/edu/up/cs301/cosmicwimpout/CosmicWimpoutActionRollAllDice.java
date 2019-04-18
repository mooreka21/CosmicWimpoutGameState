package edu.up.cs301.cosmicwimpout;
import java.io.Serializable;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * this is the roll all dice action
 *
 * @author Sam Lemly, Olivia Dendinger, David Campbell, Kayla Moore
 * @version March 2019
 */
public class CosmicWimpoutActionRollAllDice extends GameAction implements Serializable {
    //seralizable id
    private static final long serialVersionUID = 18893337L;

    /**
     * Ctor CosmicWimpoutActionRollAllDice invokes super constructor
     * @param player - current player
     */
    public CosmicWimpoutActionRollAllDice(GamePlayer player){
        super(player);

    }

}
