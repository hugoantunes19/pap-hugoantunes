/*
 * Copyright 2005 by RidgeSoft, LLC., PO Box 482, Pleasanton, CA 94566, U.S.A.
 * www.ridgesoft.com
 * 
 * RidgeSoft grants you the right to use, modify, make derivative works and
 * redistribute this source file provided you do not remove this copyright
 * notice.
 */

import com.ridgesoft.io.Speaker;

public class PlayTune implements Runnable {
    public static final int QUARTER_NOTE = 400;
    public static final int HALF_NOTE = QUARTER_NOTE * 2;
    public static final int C = 262;
    public static final int C_SHARP = 277;
    public static final int D = 294;
    public static final int D_SHARP = 311;
    public static final int E = 330;
    public static final int F = 349;
    public static final int F_SHARP = 370;
    public static final int G = 392;
    public static final int G_SHARP = 415;
    public static final int A = 440;
    public static final int A_SHARP = 466;
    public static final int B = 494;

	private Speaker mSpeaker;

	public PlayTune(Speaker speaker) {
		mSpeaker = speaker;
	}

    public void run() {
        try {
            // Play Ode to Joy
            mSpeaker.play(E, QUARTER_NOTE);
            mSpeaker.play(E, QUARTER_NOTE);
            mSpeaker.play(F, QUARTER_NOTE);
            mSpeaker.play(G, QUARTER_NOTE);
            mSpeaker.play(G, QUARTER_NOTE);
            mSpeaker.play(F, QUARTER_NOTE);
            mSpeaker.play(E, QUARTER_NOTE);
            mSpeaker.play(D, QUARTER_NOTE);
            mSpeaker.play(C, QUARTER_NOTE);
            mSpeaker.play(C, QUARTER_NOTE);
            mSpeaker.play(D, QUARTER_NOTE);
            mSpeaker.play(E, QUARTER_NOTE);
            mSpeaker.play(E, HALF_NOTE);
            mSpeaker.play(D, HALF_NOTE);

            mSpeaker.play(E, QUARTER_NOTE);
            mSpeaker.play(E, QUARTER_NOTE);
            mSpeaker.play(F, QUARTER_NOTE);
            mSpeaker.play(G, QUARTER_NOTE);
            mSpeaker.play(G, QUARTER_NOTE);
            mSpeaker.play(F, QUARTER_NOTE);
            mSpeaker.play(E, QUARTER_NOTE);
            mSpeaker.play(D, QUARTER_NOTE);
            mSpeaker.play(C, QUARTER_NOTE);
            mSpeaker.play(C, QUARTER_NOTE);
            mSpeaker.play(D, QUARTER_NOTE);
            mSpeaker.play(E, QUARTER_NOTE);
            mSpeaker.play(D, HALF_NOTE);
            mSpeaker.play(C, HALF_NOTE);
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public String toString() {
        return "Play Tune";
    }
}