import com.ridgesoft.robotics.SonarRangeFinder;
import com.ridgesoft.robotics.Servo;

public class FollowObject implements Runnable {
    private SonarRangeFinder mSonarSensor;
	private Servo mLeftServo;
	private Servo mRightServo;

    public FollowObject(SonarRangeFinder sonarSensor, 
						Servo leftServo, Servo rightServo) {
		mSonarSensor =  sonarSensor;
		mLeftServo = leftServo;
		mRightServo = rightServo;
    }
    
    public void run() {
		while (true) {
			mSonarSensor.ping();
			try {
				Thread.sleep(100);
			}
			catch (InterruptedException e) {}

			float range = mSonarSensor.getDistanceInches();
			if (range > 0.0f && range < 20.0f) {
				int power = ((int)(range - 6.0f)) * 3;
				mLeftServo.setPosition(50 + power);
				mRightServo.setPosition(50 - power);
			}
			else {
				mLeftServo.off();
				mRightServo.off();
			}
		}
    }
    
    public String toString() {
        return "Follow Object";
    }
}