import com.ridgesoft.robotics.AnalogInput;
import com.ridgesoft.robotics.Motor;


public class FollowLine implements Runnable {
    // defeniçoes de estado
    private static final byte UNK = 0; // unknown
    private static final byte S2L = 1; // 2º sensors left of line
    private static final byte S1L = 2; // 1º sensor left of line
    private static final byte CTR = 3; // centered, both sensors on line
    private static final byte S1R = 4; // 1º sensor right of line
    private static final byte S2R = 5; // 2º sensors right of line

    private static final byte LEFT = 0;
    private static final byte RIGHT = 1;

   
    private static byte[][] NEXT_STATE = new byte[][] {
            //           00   01   10   11        estado actual
            new byte[] { UNK, S1L, S1R, CTR }, // unknown
            new byte[] { S2L, S1L, S1R, CTR }, // 2 left
            new byte[] { S2L, S1L, S1R, CTR }, // 1 left
            new byte[] { UNK, S1L, S1R, CTR }, // centered
            new byte[] { S2R, S1L, S1R, CTR }, // 1 right
            new byte[] { S2R, S1L, S1R, CTR }, // 2 right
    };
    //defines class
    private Motor mLeftMotor;
    private Motor mRightMotor;

    private AnalogInput mLeftSensor;
    private AnalogInput mRightSensor;

    private int mThreshold;
    private byte[][] mPower;

    public FollowLine(Motor leftMotor, Motor rightMotor,
            AnalogInput leftSensor, AnalogInput rightSensor, int threshold,
            int normalPower, int lowPower) {
        mLeftMotor = leftMotor;
        mRightMotor = rightMotor;
        mLeftSensor = leftSensor;
        mRightSensor = rightSensor;
        mThreshold = threshold;

        byte normal = (byte) normalPower;
        byte low = (byte) lowPower;

        //motor power
        mPower = new byte[][] {
        //                   Left    Right        estado
                new byte[] { 0,      0      }, // unknown
                new byte[] { normal, 0      }, // 2 left
                new byte[] { normal, low    }, // 1 left
                new byte[] { normal, normal }, // both on line
                new byte[] { low,    normal }, // 1 right
                new byte[] { 0,      normal }, // 2 right
        };
    }
    //makes the class runnable
    public void run() {
        try {
            int state = UNK;

            while (true) {
                int leftSample = mLeftSensor.sample();
                int rightSample = mRightSensor.sample();

                // Vê a linha
                int index = 0;
                if (leftSample > mThreshold)
                    index |= 0x2;
                if (rightSample > mThreshold)
                    index |= 0x1;

                // update no estado actual
                state = NEXT_STATE[state][index];

                // set the motor power according to the state
                mLeftMotor.setPower(mPower[state][LEFT]);
                mRightMotor.setPower(mPower[state][RIGHT]);

                // Deixa os outros threads correr
                Thread.sleep(100);
            }
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public String toString() {
        return "Follow Line";
    }
}
