package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="AutonTest", group="Linear Opmode")
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
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE); //Sets the
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        //The motor direction is what direction the wheel spins for a given power.
        //Direction.reverse causes the wheel to spin -1 when given input 1, 0 when given 0, 1 when given -1, etc.
        //For reasons not yet known to science or apparently the person who coded this also does not know why this works
        //To quote "Not sure why it is like that, but it works when tested, leave it as is"
        //So leave as is

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        encoderReset();
        frontLeftDrive.setPower(1);
        frontRightDrive.setPower(1);
        backLeftDrive.setPower(1);
        backRightDrive.setPower(1);
        //In Run_To_Position (RTP) mode, the power is to define the power of the motors once run, not to immediately set the powers of the motors themselves.
        goForwardSetDist(100);
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
Wheel diameter 4 inch
gearing - unknown
multiply rotations of wheel, taking into account gearing, by diameter to determine distance per pulse roughly. (This'll probably only apply well >1000 pulses)
 */