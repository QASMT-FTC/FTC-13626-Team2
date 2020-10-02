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
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="Basic: Linear OpMode", group="Linear Opmode")
//@Disabled
public class BasicOpMode_Linear extends LinearOpMode {

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

    private double motorWeight = 0.65789473684;

    //lift variables
    private DcMotor TLiftMotor = null;
    private DcMotor BLiftMotor = null;
    int TLiftPosition = 0;
    int BLiftPosition = 0;

    //Servo variables
    private Servo servo;
    private double servoPower = 0.0;
    private Servo grabServo;

    private double reverseControls = 1;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        frontLeftDrive  = hardwareMap.get(DcMotor.class, "front_left_drive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "front_right_drive");
        backLeftDrive  = hardwareMap.get(DcMotor.class, "back_left_drive");
        backRightDrive = hardwareMap.get(DcMotor.class, "back_right_drive");

        TLiftMotor = hardwareMap.get(DcMotor.class, "lift_motor");
        BLiftMotor = hardwareMap.get(DcMotor.class, "slide_motor");

        servo = hardwareMap.servo.get("servo");
        grabServo = hardwareMap.servo.get("grab_servo");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);

        TLiftMotor.setDirection(DcMotor.Direction.FORWARD);
        BLiftMotor.setDirection(DcMotor.Direction.FORWARD);
        TLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Set up initial servo positions
        servo.setPosition(0.5);
        grabServo.setPosition(0);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // setup the inputs
            double  G1LeftStickY  = reverseControls * gamepad1.left_stick_y;
            double  G1RightStickY = reverseControls * gamepad1.right_stick_y;
            double  G1LeftStickX  = -reverseControls * gamepad1.left_stick_x;
            double  G1RightStickX = -reverseControls * gamepad1.right_stick_x;
            boolean G1RightBumper = gamepad1.right_bumper;
            boolean G1LeftBumper  = gamepad1.left_bumper;
            double  G2LeftStickY  = gamepad2.left_stick_y * 0.2;
            double  G2RightStickY = gamepad2.right_stick_y * 0.2;

            // strafe Mode (allows sideways motion)
            frontLeftDrive.setPower(G1LeftStickY + G1RightStickX + G1LeftStickX);
            backLeftDrive.setPower(G1LeftStickY + G1RightStickX - G1LeftStickX);
            backRightDrive.setPower(G1LeftStickY - G1RightStickX + G1LeftStickX);
            frontRightDrive.setPower(G1LeftStickY - G1RightStickX - G1LeftStickX);
/*
            // Braking mechanism
            if(G2LeftStickY == 0){
                frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                frontLeftDrive.setTargetPosition(frontLeftDrive.getCurrentPosition());
                backLeftDrive.setTargetPosition(backLeftDrive.getCurrentPosition());
                backRightDrive.setTargetPosition(backRightDrive.getCurrentPosition());
                frontRightDrive.setTargetPosition(frontRightDrive.getCurrentPosition());

                frontLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                backLeftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                backRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                frontRightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
*/
            // Optional Lift control
//          liftMotor.setPower(G2LeftStickY);
//          slideMotor.setPower(G2RightStickY);

            // Grabber controls
            if(gamepad2.a){
                grabServo.setPosition(0);
                servo.setPosition(0.5);
            }
            if(gamepad2.b){
                grabServo.setPosition(0.5);
                servo.setPosition(0);
            }

            // Reverse controls with the x button
            if (gamepad1.x){
                reverseControls = -reverseControls;
                telemetry.addData("Controls Status", "Direction: " + reverseControls);
            }

            // SLOW mode and FAST mode. Works by halving the reverseControls variable
            if (gamepad1.y){
                if(reverseControls == 1 || reverseControls == -1){
                    reverseControls = reverseControls*0.5;
                    telemetry.addData("Controls Status", "SLOW MODE");
                }
                else if(reverseControls == 0.5 || reverseControls == -0.5){
                    reverseControls = reverseControls*2;
                    telemetry.addData("Controls Status", "FAST MODE");
                }
                telemetry.update();
            }
/*
            // Lift_Encoder.java
            // TLift is the top lift, BLift is the bottom lift.
            double TLiftPower;
            double BLiftPower;
            int TLiftLimit = 1080;
            int BLiftLimit = 720;
            int LiftSensitivity = 5; //360 will move from 0 to 90 degrees in joystick position 0 to 1.

            // YOU MAY NEED TO CHANGE THE DIRECTION OF THIS STICK.
            double TLiftStick = -gamepad2.left_stick_y;
            double BLiftStick = -gamepad2.right_stick_y;
            TLiftPower = Range.clip(TLiftStick, -1.0, 1.0) ;
            BLiftPower = Range.clip(BLiftStick, -1.0, 1.0);

            TLiftPosition += (int)TLiftPower*LiftSensitivity;
            BLiftPosition += (int)BLiftPower*LiftSensitivity;

            TLiftPosition = Range.clip(TLiftPosition, 0, TLiftLimit);
            BLiftPosition = Range.clip(BLiftPosition, 0, BLiftLimit);

            // MOVES UP FROM POSITION 0 TO 90 DEGREES UP.
            TLiftMotor.setTargetPosition(TLiftPosition);
            TLiftMotor.setPower(0.2);
            BLiftMotor.setTargetPosition(BLiftPosition);
            BLiftMotor.setPower(0.4);

            // SET MODE TO RUN TO POSITION
            TLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            BLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //END Lift_Encoder.java

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
            */
        }
    }
}
