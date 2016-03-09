/*
 * Copyright 2005 by RidgeSoft, LLC., PO Box 482, Pleasanton, CA  94566, U.S.A.
 * www.ridgesoft.com
 *
 * RidgeSoft grants you the right to use, modify, make derivative works and
 * redistribute this source file provided you do not remove this copyright notice.
 */

import java.util.Random;
import com.ridgesoft.robotics.Motor;

/**
 * This class uses a psuedo-random number generator to make your robot perform a
 * random dance by continuously setting the motors to random power levels for a
 * random period of time.
 */
public class RandomDance implements Runnable {
    private Random mRandom;
    private Motor mLeftMotor;
    private Motor mRightMotor;

    public RandomDance(Motor leftMotor, Motor rightMotor, int seed) {
        mLeftMotor = leftMotor;
        mRightMotor = rightMotor;
        mRandom = new Random(seed);
    }

    public void run() {
        try {
            while (true) {
                mLeftMotor.setPower(mRandom.nextInt() % Motor.MAX_FORWARD);
                mRightMotor.setPower(mRandom.nextInt() % Motor.MAX_FORWARD);
                Thread.sleep(mRandom.nextInt(400) + 100);
            }
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public String toString() {
        return "Random Dance";
    }
}