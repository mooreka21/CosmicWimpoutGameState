package edu.up.cs301.cosmicwimpout;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

public class CosmicWimpoutActionRollSelectedDie extends GameAction {
    private static final long serialVersionUID= 95598448L;
    private boolean isDie1;
    private boolean isDie2;
    private boolean isDie3;
    private boolean isDie4;
    private boolean isDie5;

    public CosmicWimpoutActionRollSelectedDie(GamePlayer player, boolean die1,
                                              boolean die2, boolean die3,boolean die4, boolean die5){
        super(player);
        this.isDie1 = die1;
        this.isDie2 = die2;
        this.isDie3 = die3;
        this.isDie4 = die4;
        this.isDie5 = die5;

    }

    public boolean getDie1(){
        return isDie1;
    }

    public boolean getDie2(){
        return isDie2;
    }

    public boolean getDie3(){
        return isDie3;
    }

    public boolean getDie4(){
        return isDie4;
    }

    public boolean getDie5(){
        return isDie5;
    }
}
