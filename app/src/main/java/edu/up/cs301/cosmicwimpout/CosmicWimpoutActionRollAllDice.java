package edu.up.cs301.cosmicwimpout;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;


public class CosmicWimpoutActionRollAllDice extends GameAction {
    private static final long serialVersionUID = 18893347L;
    public CosmicWimpoutActionRollAllDice(GamePlayer player, diceArray ourDice){
        for(int i = 0; i < ourDice.length; i++){
            ourDice[i].rollMe();
        }
        super(player);
    }

}
