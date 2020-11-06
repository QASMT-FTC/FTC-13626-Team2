/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@TeleOp(name="WobbleBaseLinear", group="Linear Opmode")
//@Disabled
public class WobbleBaseLinear extends LinearOpMode {

    // Declare OpMode members
    private ElapsedTime runtime = new ElapsedTime();
    // 152 rpm
    private DcMotor frontLeftDrive = null;
    // 152 rpm
    private DcMotor frontRightDrive = null;
    // 100 rpm
    private DcMotor backLeftDrive = null;
    // 100 rpm
    private DcMotor backRightDrive = null;

    private DcMotor wobbleMotor = null;

    private DcMotor intakeMotor = null;

    private DcMotor fireMotor = null;

    private DcMotor ringMotor = null;

    private Servo wobbleServo;

    private Servo ringServo;

    private Servo grabServo;

    private Servo aimServo;

    int wobblePos = 0;

    int ringPos = 0;

    private double reverseControls = 0.5;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        frontLeftDrive  = hardwareMap.get(DcMotor.class, "frontLeftDrive"); //Assignment of the DcMotor objects (DcMotor Class inherited from internet)
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backLeftDrive  = hardwareMap.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");
        wobbleMotor = hardwareMap.get(DcMotor.class, "wobbleMotor");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        fireMotor = hardwareMap.get(DcMotor.class, "fireMotor");
        ringMotor = hardwareMap.get(DcMotor.class, "ringMotor");
        wobbleServo = hardwareMap.servo.get("wobbleServo");
        ringServo = hardwareMap.servo.get("ringServo");
        grabServo = hardwareMap.servo.get("grabServo");
        aimServo = hardwareMap.servo.get("aimServo");

        //The DcMotor class contains the methods required for setting a direction of the motor spin,


        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE); //Sets the
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER); //Sets the
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fireMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        wobbleMotor.setMode(DcMotor.RunMode.RESET_ENCODERS);
        //intakeMotor.setMode(DcMotor.RunMode.RESET_ENCODERS);

        //The motor direction is what direction the wheel spins for a given power.
        //Direction.reverse causes the wheel to spin -1 when given input 1, 0 when given 0, 1 when given -1, etc.
        //For reasons not yet known to science or apparently the person who coded this also does not know why this works
        //To quote "Not sure why it is like that, but it works when tested, leave it as is"
        //So leave as is

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) { //This is a loop which runs as long as the boolean representing the driver pressing "play" is active (set to false by pressing STOP)
            // setup the inputs
            double  G1LeftStickY  = reverseControls * gamepad1.left_stick_y; //Sets stick inputs (analogue, ranging from -1 to 1), reverseControls is 1 unless x is held
            double  G1RightStickY = reverseControls * gamepad1.right_stick_y;
            double  G1LeftStickX  = reverseControls * gamepad1.left_stick_x;
            double  G1RightStickX = reverseControls * gamepad1.right_stick_x;

            int sensitivity = 1; //360 will move from 0 to 90 degrees in joystick position 0 to 1.
            // YOU MAY NEED TO CHANGE THE DIRECTION OF THIS STICK. RIGHT NOW IT IS NEGATIVE.
            double liftStick = -gamepad2.left_stick_y;
            //boolean rightBumper = gamepad1.right_bumper;
            //boolean leftBumper = gamepad1.left_bumper;

            double frontLeftPower = (-G1LeftStickX - G1RightStickX + G1LeftStickY);
            double frontRightPower = -G1LeftStickX - G1RightStickX - G1LeftStickY;
            double backLeftPower = -G1LeftStickX + G1RightStickX - G1LeftStickY;
            double backRightPower = -G1LeftStickX + G1RightStickX + G1LeftStickY;

            double maxVal = Math.max(Math.abs(frontLeftPower),Math.max(Math.abs(frontRightPower),Math.max(Math.abs(backLeftPower),Math.abs(backRightPower))));
            if(maxVal > 1)
            {
                frontLeftPower = frontLeftPower / maxVal;
                frontRightPower = frontRightPower / maxVal;
                backRightPower = backRightPower / maxVal;
                backLeftPower = backLeftPower / maxVal;
            }

            // strafe Mode (allows sideways motion)
            backRightDrive.setPower(backRightPower); //Sets power of the back right motor depending on left stick and x-axis right stick
            backLeftDrive.setPower(backLeftPower); //Sets power of the back left motor depending on left stick and x-axis right stick
            frontLeftDrive.setPower(frontLeftPower); //Sets power of the front left motor depending on left stick and x-axis right stick
            frontRightDrive.setPower(frontRightPower); //Sets power of the front right motor depending on left stick and x-axis right stick
            // Reverse controls with the x button
            if (gamepad1.x){
                reverseControls = -reverseControls;
                telemetry.addData("Controls Status", "Direction: " + reverseControls);
            }

            // SLOW mode and FAST mode. Works by halving the reverseControls variable
            if (gamepad1.y){
                if(reverseControls == 0.5 || reverseControls == -0.5){
                    reverseControls = reverseControls*0.5;
                    telemetry.addData("Controls Status", "SLOW MODE");
                }
                else if(reverseControls == 0.25 || reverseControls == -0.25){
                    reverseControls = reverseControls*2;
                    telemetry.addData("Controls Status", "FAST MODE");
                }
                telemetry.update();
            }

            if(gamepad2.a){
                wobbleServo.setPosition(1);
            }
            if(gamepad2.b){
                wobbleServo.setPosition(0.5);
            }

            if(gamepad2.x)
            {
                //open position
                ringServo.setPosition(0.7);
            }
            else
            {
                //under the rings
                ringServo.setPosition(1);
            }

            if(gamepad1.right_trigger > 0)
            {
                grabServo.setPosition(0.5);
            }
            else
                grabServo.setPosition(0.8);

            //Moves wobble goal arm up
            wobblePos += (int)liftStick*sensitivity;
            wobblePos = Range.clip(wobblePos, 0, 700);
            wobbleMotor.setTargetPosition(wobblePos);
            wobbleMotor.setPower(1);
            wobbleMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //Moves ring flippy arm up
            if (gamepad1.b)
            {
                ringPos-=3;
            }
            if (gamepad1.a){
                ringPos+=3;
            }
            intakeMotor.setTargetPosition(ringPos);
            intakeMotor.setPower(1);
            intakeMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            double pos = aimServo.getPosition();
            if(gamepad2.left_bumper)
            {
                aimServo.setPosition(pos += 0.05);
            }
            if (gamepad2.right_bumper)
            {
                aimServo.setPosition(pos -= 0.05);
            }


            /*if(gamepad1.b)
            {
                ringPos = 575;
                intakeMotor.setTargetPosition(-ringPos);
                intakeMotor.setPower(1);
                intakeMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            else if (gamepad1.a)
            {
                intakeMotor.setTargetPosition(0);
                intakeMotor.setPower(1);
                intakeMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }*/



            //mama-mi-a-pasta wheels
            //intakeMotor.setPower(gamepad2.left_trigger);

            //The spinny shooty one
            double fireMod;
            if(gamepad2.dpad_up)
            {
                fireMod = 1;
            }
            else if(gamepad2.dpad_left)
            {
                fireMod = 0.85;
            }
            else if(gamepad2.dpad_down)
            {
                fireMod = 0.6;
            }
            else if(gamepad2.dpad_right)
            {
                fireMod = 0.75;
            }
            else
            {
                fireMod = 0.5;
            }
            fireMotor.setPower(-gamepad2.right_trigger);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }
}
