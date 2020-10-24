package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="AutonTest", group="Linear Opmode")
//@Disabled
public class AutonTest extends LinearOpMode {

    // Declare OpMode members
    private ElapsedTime runtime = new ElapsedTime();
    // 152 rpm
    private DcMotor frontLeftDrive = null; //Initialises the private DcMotor objects
    // 152 rpm                               With null variables, assignment comes later
    private DcMotor frontRightDrive = null;
    // 100 rpm
    private DcMotor backLeftDrive = null;
    // 100 rpm
    private DcMotor backRightDrive = null;

    private double reverseControls = 1;

    public void goForwardSetDist(int dist)
    {
        dist = dist / 2; //Gearing compensation
        frontLeftDrive.setTargetPosition(dist);
        frontRightDrive.setTargetPosition(dist);
        backLeftDrive.setTargetPosition(dist);
        backRightDrive.setTargetPosition(dist);
        //CourseSet Method
        //A method of this type is needed as a "course set" before position running.
    }
    public void encoderReset() {
        frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //Resets encoders
    }

    public void positionRun()
    {
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public int mmPulse(int mmDist)
    {
        int pulseDist;
        pulseDist = (int) (mmDist / 0.07055555535799998);
        return pulseDist;
    }

    public int turnLeft(int degrees) //For turning degrees
    {
        int pulseDist;
        //Assuming 1 diameter sideways per 1440 ticks
        //Placehold
    }

    @Override

    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();


        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive"); //Assignment of the DcMotor objects (DcMotor Class inherited from internet)
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");
        //The DcMotor class contains the methods required for setting a direction of the motor spin,


        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD); //Sets the
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);

        //The motor direction is what direction the wheel spins for a given power.
        //Direction.reverse causes the wheel to spin -1 when given input 1, 0 when given 0, 1 when given -1, etc.
        //For reasons not yet known to science or apparently the person who coded this also does not know why this works
        //To quote "Not sure why it is like that, but it works when tested, leave it as is"
        //So leave as is

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        encoderReset();
        frontLeftDrive.setPower(0.3);
        frontRightDrive.setPower(0.3);
        backLeftDrive.setPower(0.3);
        backRightDrive.setPower(0.3);
        //In Run_To_Position (RTP) mode, the power is to define the power of the motors once run, not to immediately set the powers of the motors themselves.
        goForwardSetDist(mmPulse(50));
        positionRun();


        positionRun();
        while(opModeIsActive() && (frontLeftDrive.isBusy() || frontRightDrive.isBusy() || backLeftDrive.isBusy() || backRightDrive.isBusy()))
        {
            //Keep program alive
        }

    }
}

/*
IMPORTANT:
Robotics Control Hub:
Name:
13626-RC
Password:
Machina1

1440 pulses per motor rotation
Wheel diameter 4 inch; 10.16 cm
gearing - 2:1
Length of bot: 45 cm
Width of bot: 40 cm
Turning circle radius: 30.1 cm
Turning circle circumference: 189.1 cm
One rotation of wheels turns 10.16 cm, 10.16 / 189.1 = 5.61% of a full turn = 18.576 degrees approx.
In order

//Test propulsion per reverse strafe
multiply rotations of wheel, taking into account gearing, by diameter to determine distance per pulse roughly. (This'll probably only apply well >1000 pulses)
 */