package edu.up.cs301.cosmicwimpout;

import java.io.Serializable;

/**
 * Dice
 *
 * Dice allows us to access the state of the dice after it has been rolled.
 * as well as retrieve which dice we are looking at.
 *
 * @author Olivia Dendinger, Kayla Moore, Sam Lemly, David Campbell
 *   @version April 2019
 */

public class Die implements Serializable {
    //serializable id
    private static final long serialVersionUID= 382491574830L;
    //initializing variables
    protected int dieState;
    protected int dieID;
    protected boolean canReroll;

    /**
     * Dice constructor
     * @param ID
     */
    public Die(int ID){
        this.dieID = ID;
        // dieState = 1;
    }

    /**
     * getCanReroll method is used to determine if a player can re-roll the dice
     * @return canReroll
     */
    public boolean getCanReroll() {
        return canReroll;
    }

    /**
     * setCanReroll is a setter method that sets if a player can re-roll the dice
     * @param canReroll
     */
    public void setCanReroll(boolean canReroll) {
        this.canReroll = canReroll;
    }

    /**
     * getDieState method gets the current state of the die
     * @return dieState
     */
    public int getDieState(){
        return dieState;
    }

    /**
     * setDieID sets the ID of the current die
     * @param newid
     */
    public void setDieID(int newid){
        this.dieID = newid;
    }

    /**
     * rollMe rolls the current die
     */
    public void rollMe(){
        this.dieState = (int)(Math.random()*6 + 1);

        // these booleans are for the smart AI to know if it can reroll
        if(this.dieState == 5 || this.dieState == 1){
            this.setCanReroll(false);
        }else this.setCanReroll(true);
    }

    /**
     * getValAsString returns the name of each respective
     * side of the current die.
     * @return a String value
     */
    public String getValAsString(){
        switch (this.dieState) {
            case 1:
                return "Tens";
            case 2:
                return "Moons";
            case 3:
                if(this.dieID == 3)
                    return "Flaming Sun";
                else
                    return "Triangles";
            case 4:
                return "Bolts";
            case 5:
                return "Fives";
            case 6 :
                return "Stars";
        }
        return "null";
    }
}
