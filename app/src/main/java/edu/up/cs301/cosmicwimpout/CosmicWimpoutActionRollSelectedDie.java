package edu.up.cs301.cosmicwimpout;

import java.io.Serializable;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.actionMsg.GameAction;

/**
 * this is the roll selected dice action
 * takes 5 parameters besides the player
 * to determine which dice to re-roll
 *
 * @author Sam Lemly, Olivia Dendinger, David Campbell, Kayla Moore
 * @version March 2019
 */
public class CosmicWimpoutActionRollSelectedDie extends GameAction implements Serializable {
    //serializable id
    private static final long serialVersionUID= 95598428L;

    //instance variables for each die
    private boolean isDie1;
    private boolean isDie2;
    private boolean isDie3;
    private boolean isDie4;
    private boolean isDie5;

    /**
     * Ctor CosmicWimpoutActinRollSelectedDie invokes super constructor
     * @param player - current player
     * @param die1   - first die
     * @param die2   - second die
     * @param die3   - third die
     * @param die4   - fourth die
     * @param die5   - fifth die
     */
    public CosmicWimpoutActionRollSelectedDie(GamePlayer player, boolean die1,
                                              boolean die2, boolean die3,boolean die4, boolean die5){
        super(player);
        this.isDie1 = die1;
        this.isDie2 = die2;
        this.isDie3 = die3;
        this.isDie4 = die4;
        this.isDie5 = die5;
    }

    /**
     * getter for first die
     * @return isDie1
     */
    public boolean getDie1(){
        return isDie1;
    }

    /**
     * getter for second die
     * @return isDie2
     */
    public boolean getDie2(){
        return isDie2;
    }

    /**
     * getter for third die
     * @return isDie3
     */
    public boolean getDie3(){
        return isDie3;
    }

    /**
     * getter for fourth die
     * @return isDie4
     */
    public boolean getDie4(){
        return isDie4;
    }

    /**
     * getter for fifth die
     * @return isDie5
     */
    public boolean getDie5(){
        return isDie5;
    }
}
