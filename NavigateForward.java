/*
 * Copyright 2005 by RidgeSoft, LLC., PO Box 482, Pleasanton, CA 94566, U.S.A.
 * www.ridgesoft.com
 * 
 * RidgeSoft grants you the right to use, modify, make derivative works and
 * redistribute this source file provided you do not remove this copyright
 * notice.
 */

import com.ridgesoft.robotics.Navigator;

public class NavigateForward implements Runnable {
    private Navigator mNavigator;
    private float mDistance;

    public NavigateForward(Navigator navigator, float distance) {
        mNavigator = navigator;
        mDistance = distance;
    }

    public void run() {
        mNavigator.moveTo(mDistance, 0.0f);
    }

    public String toString() {
        return "Navigate Forward";
    }
}