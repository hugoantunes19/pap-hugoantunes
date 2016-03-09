/*
 * Copyright 2005 by RidgeSoft, LLC., PO Box 482, Pleasanton, CA 94566, U.S.A.
 * www.ridgesoft.com
 * 
 * RidgeSoft grants you the right to use, modify, make derivative works and
 * redistribute this source file provided you do not remove this copyright
 * notice.
 */

import com.ridgesoft.robotics.IrRemote;
import com.ridgesoft.robotics.Motor;

/**
 * This class provides a means to use a Sony compatible infrared remote control
 * to remotely control movement of your robot.
 */
public class RemoteControl implements Runnable {
    private IrRemote mIrRemote;
    private Motor mLeftMotor;
    private Motor mRightMotor;
    private int mDrivePower;
    private int mRotatePower;
    private int mForwardCommand;
    private int mReverseCommand;
    private int mRotateLeftCommand;
    private int mRotateRightCommand;
    private int mMask;

    public RemoteControl(IrRemote irRemote, Motor leftMotor, Motor rightMotor,
            int drivePower, int rotatePower, int forwardCommand,
            int reverseCommand, int rotateLeftCommand, int rotateRightCommand,
            int mask) {
        mIrRemote = irRemote;
        mLeftMotor = leftMotor;
        mRightMotor = rightMotor;
        mDrivePower = drivePower;
        mRotatePower = rotatePower;
        mForwardCommand = forwardCommand;
        mReverseCommand = reverseCommand;
        mRotateLeftCommand = rotateLeftCommand;
        mRotateRightCommand = rotateRightCommand;
        mMask = mask;
    }

    public void run() {
        try {
            while (true) {
                int irData = mIrRemote.read();
                if (irData != -1) {
                    irData &= mMask;

                    if (irData == mForwardCommand) {
                        mLeftMotor.setPower(mDrivePower);
                        mRightMotor.setPower(mDrivePower);
                    }
                    else if (irData == mReverseCommand) {
                        mLeftMotor.setPower(-mDrivePower);
                        mRightMotor.setPower(-mDrivePower);
                    }
                    else if (irData == mRotateLeftCommand) {
                        mLeftMotor.setPower(-mRotatePower);
                        mRightMotor.setPower(mRotatePower);
                    }
                    else if (irData == mRotateRightCommand) {
                        mLeftMotor.setPower(mRotatePower);
                        mRightMotor.setPower(-mRotatePower);
                    }
                    else {
                        mLeftMotor.stop();
                        mRightMotor.stop();
                    }
                }
                else {
                    mLeftMotor.stop();
                    mRightMotor.stop();
                }
                Thread.sleep(100);
            }
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public String toString() {
        return "Remote Control";
    }
}