package edu.up.cs301.cosmicwimpout;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.Serializable;

import edu.up.cs301.game.R;

/**
 * CosmicWimpoutSoundPlayer is a class that uses a SoundPool object to import sounds
 * that will be used as sound effects in our game. (citations listed below code)
 *
 * @author Kayla Moore, Olivia Dendinger, Sam Lemly, David Campbell
 *  @version April 2019
 */

public class CosmicWimpoutSoundPlayer implements Serializable {
    private static final long serialVersionUID= 5006874389L;

    private SoundPool soundPool;
    private int oneDie;
    private int twoDice;
    private int threeDice;
    private int fourDice;
    private int fiveDice;
    private int background;
    private int winner;
    private int loser;

    /**
     * Ctor for CosmicWimpoutSoundPlayer
     *
     * @param context - context of the sound player (activity)
     */
    public CosmicWimpoutSoundPlayer(Context context) {
        //initialize soundPool player
        soundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);

        //load in sounds
        oneDie = soundPool.load(context, R.raw.one_die, 1);
        twoDice = soundPool.load(context, R.raw.two_dice, 1);
        threeDice = soundPool.load(context, R.raw.three_dice, 1);
        fourDice = soundPool.load(context, R.raw.four_dice, 1);
        fiveDice = soundPool.load(context, R.raw.five_dice, 1);
        background = soundPool.load(context, R.raw.bensound_theelevatorbossanova, 1);
        winner = soundPool.load(context, R.raw.win_sound, 1);
        loser = soundPool.load(context, R.raw.lose_sound, 1);
    }

    /**
     * playOneDie method is void and has no parameters, it plays the oneDie rolling sound effect
     */
    public void playOneDie(){
        soundPool.play(oneDie, 1.0f, 1.0f, 1, 0,1.0f);
    }

    /**
     * playTwoDice method is void and has no parameters, it plays the twoDice rolling sound effect
     */
    public void playTwoDice(){
        soundPool.play(twoDice, 1.0f, 1.0f, 1, 0,1.0f);
    }

    /**
     * playThreeDice method is void and has no parameters, it plays the threeDice rolling sound effect
     */
    public void playThreeDice(){
        soundPool.play(threeDice, 1.0f, 1.0f, 1, 0,1.0f);
    }

    /**
     * playFourDice method is void and has no parameters, it plays the fourDice rolling sound effect
     */
    public void playFourDice(){
        soundPool.play(fourDice, 1.0f, 1.0f, 1, 0,1.0f);
    }

    /**
     * playFiveDice method is void and has no parameters, it plays the fiveDice rolling sound effect
     */
    public void playFiveDice(){
        soundPool.play(fiveDice, 1.0f, 1.0f, 1, 0,1.0f);
    }

    /**
     * playBackground method is void and has no parameters, it plays the background music
     */
    public void playBackground(){
        soundPool.play(background, 0.1f, 0.1f, 1, 0,1.0f);
    }

    /**
     * playWinner method is void and has no parameters, it plays the winner sound effect
     */
    public void playWinner(){
        soundPool.play(winner, 1.0f, 1.0f, 1, 0,1.0f);
    }

    /**
     * playLoser method is void and has no parameters, it plays the loser sound effect
     */
    public void playLoser(){
        soundPool.play(loser, 1.0f, 1.0f, 1, 0,1.0f);
    }



    /**
     * Citations:
     * all dice rolling sounds were recorded by Kayla
     * background music was found here: https://www.bensound.com/royalty-free-music/jazz
     * winner sound was found here: http://soundbible.com/1003-Ta-Da.html
     * loser sound was found here: http://soundbible.com/1830-Sad-Trombone.html
     *
     * tutorial on adding sounds using SoundPool: https://www.youtube.com/watch?v=r2Oz_bV5trU
     * further info on SoundPool: https://developer.android.com/reference/android/media/SoundPool.html
     */
}