/*
 * Copyright 2005 by RidgeSoft, LLC., PO Box 482, Pleasanton, CA 94566, U.S.A.
 * www.ridgesoft.com
 * 
 * RidgeSoft grants you the right to use, modify, make derivative works and
 * redistribute this source file provided you do not remove this copyright
 * notice.
 */

import com.ridgesoft.robotics.Navigator;

public class Rotate implements Runnable {
    private Navigator mNavigator;

    public Rotate(Navigator navigator) {
        mNavigator = navigator;
    }

    public void run() {
        mNavigator.turnTo(3.14f); // Pi radians (180 degrees)
    }

    public String toString() {
        return "Rotate 180";
    }
}