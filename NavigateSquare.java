/*
 * Copyright 2005 by RidgeSoft, LLC., PO Box 482, Pleasanton, CA 94566, U.S.A.
 * www.ridgesoft.com
 * 
 * RidgeSoft grants you the right to use, modify, make derivative works and
 * redistribute this source file provided you do not remove this copyright
 * notice.
 */

import com.ridgesoft.robotics.Navigator;

public class NavigateSquare implements Runnable {
    private Navigator mNavigator;
    private float mSize;

    public NavigateSquare(Navigator navigator, float size) {
        mNavigator = navigator;
        mSize = size;
    }

    public void run() {
        mNavigator.moveTo(mSize, 0.0f);
        mNavigator.moveTo(mSize, -mSize);
        mNavigator.moveTo(0.0f, -mSize);
        mNavigator.moveTo(0.0f, 0.0f);
        mNavigator.turnTo(0.0f);
    }

    public String toString() {
        return "Navigate Square";
    }
}

