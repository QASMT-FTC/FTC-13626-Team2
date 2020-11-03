    package org.firstinspires.ftc.teamcode;

    import com.qualcomm.robotcore.eventloop.opmode.Disabled;
    import com.qualcomm.robotcore.hardware.Servo;
    import com.qualcomm.robotcore.hardware.ColorSensor;
    import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
    import com.qualcomm.robotcore.hardware.DcMotor;
    import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
    import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
    import com.qualcomm.robotcore.util.ElapsedTime;

    import org.firstinspires.ftc.robotcore.external.ClassFactory;
    import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
    import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
    import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
    import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

    import java.util.List;

    @Autonomous(name = "SensorAuto", group = "Linear Opmode")
    public class SensorAuto extends LinearOpMode {

        private ElapsedTime runtime = new ElapsedTime();

        private DcMotor frontLeftDrive = null;
        // 152 rpm
        private DcMotor frontRightDrive = null;
        // 100 rpm
        private DcMotor backLeftDrive = null;
        // 100 rpm
        private DcMotor backRightDrive = null;

        private DcMotor wobbleMotor = null;

        private Servo wobbleServo = null;

        private VuforiaLocalizer vuforia;

        private ColorSensor color;

        private double reverseControls = 1;

        int zeroCounter = 0;
        int quadCounter = 0;

        private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
        private static final String LABEL_FIRST_ELEMENT = "Quad";
        private static final String LABEL_SECOND_ELEMENT = "Single";

        private static final String VUFORIA_KEY =
                "AZrcPbL/////AAABmQ7qhHnAOkQDjFldRi+1gXdnIol7PdUHJo1OJXAy+0C23VNo6+UBdsRdJFEpeeHMUjDZgvflIkS92jUqHhtdckNsnbBDGUBjVC5NRweYFvtc9pKmNGwgQLYvKSZwdwBKWhx/i4rYJgWItX0JEcv9lsQ6VzJChbO3VwCyxnwRylI/HkQk21nYDhHaURDE0ogSr8GqDYnoE3F9h5fw/ll0wr5rWSgyxfcsEWg3YvBigLVyzhO/zXwA+4Og98pGaOW9mhTD78B1W0P4NUGD6ywdGP7j9uDepld/wDueVykgqHR8xcZ6VXc7DlkKOHgk8Zr6HqMUzDDsMX457wwFRbDWkYJiIyzXuO7jlpZNQ+mRqvoF";

        private void initVuforia() {

            VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

            parameters.vuforiaLicenseKey = VUFORIA_KEY;
            parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

            //  Instantiate the Vuforia engine
            vuforia = ClassFactory.getInstance().createVuforia(parameters);

            // Loading trackables is not necessary for the TensorFlow Object Detection engine.
        }

        private TFObjectDetector tfod;
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

        public void setAllPower(double power) {
            frontLeftDrive.setPower(power);
            frontRightDrive.setPower(power);
            backLeftDrive.setPower(power);
            backRightDrive.setPower(power);
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

        public void strafe(int dist)
        {
            dist = dist / 2; //Gearing compensation
            frontLeftDrive.setTargetPosition(dist);
            frontRightDrive.setTargetPosition(-dist);
            backLeftDrive.setTargetPosition(-dist);
            backRightDrive.setTargetPosition(dist);
        }

        public void moveToAngle(int angle, double power)
        {
            wobbleMotor.setTargetPosition(angle*4);
            wobbleMotor.setPower(power);
            wobbleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        public void move(char axis, int dist) //General for movement, distance in mm.
        {
            encoderReset();
            if(axis == 'y')
            {
                goForwardSetDist(mmPulse(dist));
                positionRun();
            }
            else if(axis == 'x')
            {
                strafe(mmPulse(dist)); //Use different mmpulse for strafe
                positionRun();
            }

            while (frontLeftDrive.isBusy() || backRightDrive.isBusy() && opModeIsActive())
            {
                //Wait until complete
            }
        }

        public String returnColor(int sensitivity)
        {
            telemetry.addData("Red", color.red());
            telemetry.addData("Green", color.green());
            telemetry.addData("Blue", color.blue());
            telemetry.update();

            if(color.red() + color.green() + color.blue() > sensitivity*3)
                return "white";

            if (color.blue() > sensitivity)
                return "blue";

            if (color.red() > sensitivity)
                return "red";

            return "black";
        }

        public void moveToColor(int stepSize, String targetColor)
        {
            if (returnColor(150) != targetColor)
            {
                goForwardSetDist(stepSize);
            }
            positionRun();
            while(opModeIsActive() && (frontLeftDrive.isBusy() || frontRightDrive.isBusy() || backLeftDrive.isBusy() || backRightDrive.isBusy()))
            {
                //Keep program alive
            }
        }


        @Override
        public void runOpMode() {
            // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
            // first.
            frontLeftDrive  = hardwareMap.get(DcMotor.class, "frontLeftDrive"); //Assignment of the DcMotor objects (DcMotor Class inherited from internet)
            frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
            backLeftDrive  = hardwareMap.get(DcMotor.class, "backLeftDrive");
            backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");
            wobbleMotor = hardwareMap.get(DcMotor.class, "wobbleMotor");
            wobbleServo = hardwareMap.servo.get("wobbleServo");
            color = hardwareMap.get(ColorSensor.class, "color" );

            frontLeftDrive.setDirection(DcMotor.Direction.REVERSE); //Sets the
            frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
            backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
            backRightDrive.setDirection(DcMotor.Direction.FORWARD);


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
            runtime.reset();
            setAllPower(0.3);

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
                            telemetry.update();
                        }

                        moveToAngle(90, 0.5);
                        wobbleServo.setPosition(1);
                        move('y', 100);
                        switch (updatedRecognitions.size())
                        {
                            case 0:
                                if (zeroCounter > 100)
                                {
                                    move('y', 500);
                                }
                                else
                                    zeroCounter++;
                                break;
                            case 1:
                                move( 'x', 100);
                                moveToColor(1, "blue");
                                wobbleServo.setPosition(0.5);
                                moveToColor(-1, "white");
                                move('y', 10);
                                break;
                            case 4:
                                if (quadCounter > 0)
                                {
                                    moveToColor(1, "blue");
                                }
                                break;
                        }
                    }
                }
            }


            if (tfod != null) {
                tfod.shutdown();
            }
        }



        private void initTfod() {
            int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                    "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
            tfodParameters.minResultConfidence = 0.8f;
            tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
            tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
        }
    }
