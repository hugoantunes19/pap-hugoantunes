// bibliotecas
import com.ridgesoft.io.Display;
import com.ridgesoft.io.Speaker;
import com.ridgesoft.robotics.IrRemote;
import com.ridgesoft.robotics.sensors.SonyIrRemote;
import com.ridgesoft.robotics.SonarRangeFinder;
import com.ridgesoft.robotics.sensors.ParallaxPing;
import com.ridgesoft.ui.Screen;
import com.ridgesoft.ui.ScreenManager;
import com.ridgesoft.ui.TwoLineScreen;
import com.ridgesoft.robotics.PushButton;
import com.ridgesoft.robotics.AnalogInput;
import com.ridgesoft.robotics.ShaftEncoder;
import com.ridgesoft.robotics.DirectionListener;
import com.ridgesoft.robotics.AnalogShaftEncoder;
import com.ridgesoft.robotics.Localizer;
import com.ridgesoft.robotics.OdometricLocalizer;
import com.ridgesoft.robotics.Servo;
import com.ridgesoft.robotics.Motor;
import com.ridgesoft.robotics.ContinuousRotationServo;
import com.ridgesoft.robotics.Navigator;
import com.ridgesoft.robotics.DifferentialDriveNavigator;
import com.ridgesoft.intellibrain.IntelliBrain;
// class where we define the several buttons the robot uses
public class IntelliBrainBotDemo {
    private static final int CHANNEL_UP = 144;
    private static final int CHANNEL_DOWN = 145;
    private static final int VOLUME_UP = 146;
    private static final int VOLUME_DOWN = 147;
    private static final int PLAY = 154;

    public static void main(String args[]) {
        try {
            // Get references to objects which provide a software interface to
            // the IntelliBrain controller's functions this program will use.
            Display display = IntelliBrain.getLcdDisplay();
            Speaker speaker = IntelliBrain.getBuzzer();
            IrRemote irRemote = new SonyIrRemote(IntelliBrain.getIrReceiver());
            PushButton startButton = IntelliBrain.getStartButton();
            PushButton stopButton = IntelliBrain.getStopButton();
            AnalogInput leftRangeSensor = IntelliBrain.getAnalogInput(1);
            AnalogInput rightRangeSensor = IntelliBrain.getAnalogInput(2);
            AnalogInput leftWheelInput = IntelliBrain.getAnalogInput(4);
            AnalogInput rightWheelInput = IntelliBrain.getAnalogInput(5);
            AnalogInput leftLineSensor = IntelliBrain.getAnalogInput(6);
            AnalogInput rightLineSensor = IntelliBrain.getAnalogInput(7);
            SonarRangeFinder sonarSensor = new ParallaxPing(IntelliBrain.getDigitalIO(3));

            // Create wheel (shaft) encoders from your robot's infrared wheel
            // sensors.
            ShaftEncoder leftEncoder = new AnalogShaftEncoder(leftWheelInput,
                    250, 750, 30, Thread.MAX_PRIORITY);
            ShaftEncoder rightEncoder = new AnalogShaftEncoder(rightWheelInput,
                    250, 750, 30, Thread.MAX_PRIORITY);

            // Create a localizer to track your robot's position using the
            // wheel encoders.
            Localizer localizer = new OdometricLocalizer(leftEncoder,
                    rightEncoder, 2.65f, 4.55f, 16, Thread.MAX_PRIORITY - 1, 30);

            // Use the ContinuousRotationServo class to make your robot's
            // continuous rotation servos controllable in the same way as 
            // conventional DC motors.
            Servo leftServo = IntelliBrain.getServo(1);
            Servo rightServo =  IntelliBrain.getServo(2);
            Motor leftMotor = new ContinuousRotationServo(leftServo, 
                    false, 14, (DirectionListener) leftEncoder);
            Motor rightMotor = new ContinuousRotationServo(rightServo, 
                    true, 14, (DirectionListener) rightEncoder);

            // Create a navigator class to navigate your robot from place to
            // place.
            Navigator navigator = new DifferentialDriveNavigator(leftMotor,
                    rightMotor, localizer, 8, 6, 25.0f, 0.5f, 0.08f,
                    Thread.MAX_PRIORITY - 2, 50);

            // Create a table of functions you can select from through your
            // robot's user interface.
            Runnable functions[] = new Runnable[] {
                    //robot does nothing, usefull for making sure that sensors are ok
                    new DoNothing(),
						new PlayTune(IntelliBrain.getBuzzer()), 
                   //requiers a controller or an app to funcion
                   //new RemoteControl(irRemote, leftMotor, rightMotor, 16, 10,
                            //CHANNEL_UP, CHANNEL_DOWN, VOLUME_DOWN, VOLUME_UP,
                            //0xff),
                    // class for robot to move foward
                    new NavigateForward(navigator, 24),
                    // the robor rotates 180 degreees
                    new Rotate(navigator),
                    //robot makes a navigates making a square
                    new NavigateSquare(navigator, 16.0f),
                    //robot makes random movements looking like he is dancing
                    new RandomDance(leftMotor, rightMotor, 
                            (leftWheelInput.sample() + 1) * 
                            (rightWheelInput.sample() + 1)),
                    // robot uses sensors to "read" a line that it reads
                    new FollowLine(leftMotor, rightMotor, leftLineSensor,
                            rightLineSensor, 300, 10, 5),
                    //robot avoids any object in range from its sonars
                    new AvoidObstacles(localizer, navigator, leftRangeSensor,
                            rightRangeSensor, IntelliBrain.getStatusLed(), 200,
                            0.7f, 3000, 24.0f, 500, Thread.MAX_PRIORITY - 4),
                    // robot follows any object that is in front of the range from is sonars
                    new FollowObject(sonarSensor, leftServo, rightServo),
            };

            // Set up to execute the function selection portion of the user
            // interface.

            // Wait for the START button to be released (from initial program
            // start) before entering the function selection loop.
            startButton.waitReleased();

            // Take control of the STOP button.
            IntelliBrain.setTerminateOnStop(false);

            // Loop to update the display as input is receieved from the STOP
            // button or from the infrared remote control. Exit the loop when 
            // the START button is pressed or the play button on the infrared 
            // remote control is pressed.
            int selectedFunction = 0;
            display.print(0, "Select Function");
            display.print(1, functions[selectedFunction].toString());
            int irData = irRemote.read();
            while (!startButton.isPressed() && (irData != PLAY)) {
                int prevFunction = selectedFunction;
                irData = irRemote.read();
                if (irData > 0)
                    irData &= 0xff; // mask off the device selection bits
                if (stopButton.isPressed() || (irData == CHANNEL_DOWN)) {
                    if (++selectedFunction >= functions.length)
                        selectedFunction = 0;
                }
                else if (irData == CHANNEL_UP) {
                    if (--selectedFunction < 0)
                        selectedFunction = functions.length - 1;
                }

                // Beep the speaker and update the display if the selected
                // (displayed) function has changed.
                if (selectedFunction != prevFunction) {
                    speaker.beep();
                    display.print(1, functions[selectedFunction].toString());
                    while (irRemote.read() != -1);
                    irData = -1;
                    stopButton.waitReleased();
                }
            }

            // Relinquish control of the STOP button.
            IntelliBrain.setTerminateOnStop(true);

            speaker.beep();

            // Wait for the user to release the button on the remote control.
            while (irRemote.read() != -1)
                Thread.sleep(10);

            // Create the list of screens to display.
            Screen[] screens = new Screen[] {
                    //simple screen that dysplays random text
                    new TwoLineScreen("Meu Robo", "versao 1"),
                    //screen where it shows the sensor from the wheels
                    new TwoLineScreen("L Wheel: ", leftWheelInput, "R Wheel: ",
                            rightWheelInput),
                    new TwoLineScreen("L Line: ", leftLineSensor, "R Line: ",
                            rightLineSensor),
                    new TwoLineScreen("L Range: ", leftRangeSensor,
                            "R Range: ", rightRangeSensor),
                    //screen shows data from sonar
                    new SonarSensorScreen(sonarSensor),
                    //screen shows data from encoders
                    new TwoLineScreen("L Enc: ", leftEncoder, "R Enc: ",
                            rightEncoder),
                    new TwoLineScreen("Pose", null, null, localizer), };

            // Create a screen manager to allow you to scroll through the
            // various screens using the thumbwheel.
            new ScreenManager(display, screens, IntelliBrain.getThumbWheel(),
                    Thread.MIN_PRIORITY, 500);

            // Run the selected function.
            functions[selectedFunction].run();
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
