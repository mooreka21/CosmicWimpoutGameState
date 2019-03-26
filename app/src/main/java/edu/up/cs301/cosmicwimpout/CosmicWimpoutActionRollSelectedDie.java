package edu.up.cs301.cosmicwimpout;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

public class CosmicWimpoutActionRollSelectedDie extends GameAction {
    private static final long serialVersionUID= 95598448L;
    private int selectedDieId;

    public CosmicWimpoutActionRollSelectedDie(GamePlayer player, int selectedDieId){
        super(player);
        this.selectedDieId = selectedDieId;

    }

    public int getThisDieId(){
        return selectedDieId;
    }
}
