package edu.up.cs301.cosmicwimpout;

import java.io.Serializable;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * this is the end turn action
 *
 * @author Sam Lemly, Olivia Dendinger, David Campbell, Kayla Moore
 * @version March 2019
 */
public class CosmicWimpoutActionEndTurn extends GameAction implements Serializable {
    private static final long serialVersionUID= 100598440L;
    //invokes super constructor
    public CosmicWimpoutActionEndTurn(GamePlayer player){
        super(player);
    }
}
