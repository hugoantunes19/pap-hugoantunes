/*
 * Copyright 2005 by RidgeSoft, LLC., PO Box 482, Pleasanton, CA 94566, U.S.A.
 * www.ridgesoft.com
 * 
 * RidgeSoft grants you the right to use, modify, make derivative works and
 * redistribute this source file provided you do not remove this copyright
 * notice.
 */

import com.ridgesoft.ui.Screen;
import com.ridgesoft.io.Display;
import com.ridgesoft.robotics.SonarRangeFinder;

public class SonarSensorScreen implements Screen {
    private SonarRangeFinder mSonarSensor;

    public SonarSensorScreen(SonarRangeFinder sonarSensor) {
        mSonarSensor = sonarSensor;
    }

    public void update(Display display) {
		mSonarSensor.ping();
		try {
			Thread.sleep(100);
		}
		catch (InterruptedException e) {}
        display.print(0, "Sonar Range");
		float range = mSonarSensor.getDistanceInches();
		if (range > 0.0f)
        	display.print(1, Integer.toString((int)(range + 0.5f)) + '"');
		else
			display.print(1, "--");
    }
}