

import com.ridgesoft.robotics.AnalogInput;
import com.ridgesoft.io.LED;
import com.ridgesoft.robotics.Behavior2;
import com.ridgesoft.robotics.BehaviorArbiter;
import com.ridgesoft.robotics.BehaviorListener;
import com.ridgesoft.robotics.BehaviorEvent;
import com.ridgesoft.robotics.Localizer;
import com.ridgesoft.robotics.Navigator;
import com.ridgesoft.robotics.behaviors.AvoidBehavior;
import com.ridgesoft.robotics.behaviors.GoToBehavior;
import com.ridgesoft.robotics.behaviors.StopBehavior;


public class AvoidObstacles implements Runnable, BehaviorListener {
    private BehaviorArbiter mArbiter;
    private Behavior2 mAvoidBehavior;
    private Behavior2 mGoHomeBehavior;
    private Behavior2 mGoDest1Behavior;
    private Behavior2 mStopBehavior;
    private int mState;

    private static final int GO_1 = 1;
    private static final int AT_1 = 2;
    private static final int GO_HOME = 3;
    private static final int AT_HOME = 4;
    private static final int DONE = 5;

    public AvoidObstacles(Localizer localizer, Navigator navigator,
            AnalogInput leftRange, AnalogInput rightRange, LED led,
            int threshold, float turnAmount, int holdTime, float distance,
            int arbiterPeriod, int arbiterPriority) {

        mAvoidBehavior = new AvoidBehavior(localizer, navigator, leftRange,
                rightRange, threshold, turnAmount, holdTime, true);
        mGoHomeBehavior = new GoToBehavior(navigator, 0.0f, 0.0f, false);
        mGoHomeBehavior.setListener(this);
        mGoDest1Behavior = new GoToBehavior(navigator, distance, 0.0f, true);
        mGoDest1Behavior.setListener(this);
        mStopBehavior = new StopBehavior(navigator, true);

        Behavior2 behaviors[] = new Behavior2[] { mAvoidBehavior,
                mGoHomeBehavior, mGoDest1Behavior, mStopBehavior };

        mArbiter = new BehaviorArbiter(behaviors, led, arbiterPeriod);
        mArbiter.setPriority(arbiterPriority);
    }

    public void run() {
        try {
            mState = GO_1;

            mArbiter.start();

            while (true) {
                int state;
                synchronized (this) {
                    state = mState;
                }

                switch (state) {
                case AT_1:
                    mGoDest1Behavior.setEnabled(false);
                    mState = GO_HOME;
                    mGoHomeBehavior.setEnabled(true);
                    break;

                case AT_HOME:
                    mAvoidBehavior.setEnabled(false);
                    mGoHomeBehavior.setEnabled(false);
                    mState = DONE;
                    break;
                }

                synchronized (this) {
                    wait();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void behaviorEvent(BehaviorEvent event) {
        try {
            if (event.type == BehaviorEvent.BEHAVIOR_COMPLETED) {
                if (event.behavior == mGoDest1Behavior) {
                    mState = AT_1;
                    notify();
                }
                else if (event.behavior == mGoHomeBehavior) {
                    mState = AT_HOME;
                    notify();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        return "Avoid Obstacles";
    }
}