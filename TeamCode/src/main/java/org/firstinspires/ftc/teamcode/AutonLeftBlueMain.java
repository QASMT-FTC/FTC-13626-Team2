package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;

//Autonomous code for left blue (blue close to wall) position. Assumes no alliance obstructions.
@Autonomous(name="Autononomous Blue WallClose", group="Linear Opmode")

public class AutonLeftBlueMain extends LinearOpMode {

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

    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";


    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

    private static final String VUFORIA_KEY =
            "AZrcPbL/////AAABmQ7qhHnAOkQDjFldRi+1gXdnIol7PdUHJo1OJXAy+0C23VNo6+UBdsRdJFEpeeHMUjDZgvflIkS92jUqHhtdckNsnbBDGUBjVC5NRweYFvtc9pKmNGwgQLYvKSZwdwBKWhx/i4rYJgWItX0JEcv9lsQ6VzJChbO3VwCyxnwRylI/HkQk21nYDhHaURDE0ogSr8GqDYnoE3F9h5fw/ll0wr5rWSgyxfcsEWg3YvBigLVyzhO/zXwA+4Og98pGaOW9mhTD78B1W0P4NUGD6ywdGP7j9uDepld/wDueVykgqHR8xcZ6VXc7DlkKOHgk8Zr6HqMUzDDsMX457wwFRbDWkYJiIyzXuO7jlpZNQ+mRqvoF";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    public void goForwardSetDist(int dist) {
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

    public void positionRun() {
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void strafe(int dist)
    {
        dist = dist / 2; //Gearing compensation
        frontLeftDrive.setTargetPosition(dist);
        frontRightDrive.setTargetPosition(-dist);
        backLeftDrive.setTargetPosition(-dist);
        backRightDrive.setTargetPosition(dist);
    }

    public void setAllPower(double power) {
        frontLeftDrive.setPower(power);
        frontRightDrive.setPower(power);
        backLeftDrive.setPower(power);
        backRightDrive.setPower(power);
        //Resets encoders
    }

    public int mmPulseDef(int mmDist) {
        int pulseDist;
        pulseDist = (int) (mmDist / 0.07055555535799998);
        return pulseDist;
    }

    public void move(char axis, int dist) //General for movement, distance in mm.
    {
        encoderReset();
        if(axis == 'y')
        {
            goForwardSetDist(mmPulseDef(dist));
            positionRun();
        }
        else if(axis == 'x')
        {
            strafe(mmPulseDef(dist)); //Use different mmpulse for strafe
            positionRun();
        }

        while (frontLeftDrive.isBusy() || backRightDrive.isBusy())
        {
            //Wait until complete
        }
    }

    public void runOpMode() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();
        initTfod();


        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 1.78 or 16/9).

            // Uncomment the following line if you want to adjust the magnification and/or the aspect ratio of the input images.
            //tfod.setZoom(2.5, 1.78);
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        // step through the list of recognitions and display boundary info.
                        int i = 0;
                        for (Recognition recognition : updatedRecognitions) {
                            telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                            telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                    recognition.getLeft(), recognition.getTop());
                            telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                    recognition.getRight(), recognition.getBottom());
                            if (updatedRecognitions.size() == 1){
                                encoderReset();
                                move('y', 150);
                                setAllPower(0.3);
                                positionRun();
                                //runToColor(1);
                            }
                        }
                        telemetry.update();
                    }
                }
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

                frontLeftDrive.setPower(0.3);
                frontRightDrive.setPower(0.3);
                backLeftDrive.setPower(0.3);
                backRightDrive.setPower(0.3);

                //Put auton code under here:
                move('y', 150);
                move('x', 150);
                move('y', -150);
                move('x', -150);

/*
The following is an instruction series for what the bot needs to do:
1. Identify number of rings (A/B/C) - 0 = A, 1 = B, 4 = C
2. Clamp wobble goal & Lift
3. Move to appropriate cell
    This step concerns a switch case of movement positions, where it will be 3 similar sequential series of instructions.
4. Lower wobble goal & release


 */
            }

        }

        if (tfod != null) {
            tfod.shutdown();
        }
    }
}