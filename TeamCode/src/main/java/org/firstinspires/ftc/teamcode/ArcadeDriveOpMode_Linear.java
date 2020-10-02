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


@TeleOp(name="Arcade Drive", group="Linear Opmode")
//@Disabled
public class ArcadeDriveOpMode_Linear extends LinearOpMode {

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

    private double motorWeight = 0.65789473684; //Tetrix motors and "other" motors - adjusts motor speed

    private DcMotor leftIntake = null;
    private DcMotor rightIntake = null;

    private DcMotor grabberArm = null;
    private double grabberPower = 1;
    private Servo servo;
    private double servoPower = 0.0;
    private Servo grabServo;

    private int reverseControls = 1;

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

        leftIntake = hardwareMap.get(DcMotor.class, "left_intake");
        rightIntake = hardwareMap.get(DcMotor.class, "right_intake");

        grabberArm = hardwareMap.get(DcMotor.class, "grabber_arm");
        servo = hardwareMap.servo.get("servo");
        grabServo = hardwareMap.servo.get("grab_servo");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);

        leftIntake.setDirection(DcMotor.Direction.FORWARD);
        rightIntake.setDirection(DcMotor.Direction.REVERSE);

        grabberArm.setDirection((DcMotor.Direction.FORWARD));
        servo.setPosition(servoPower);
        grabServo.setPosition(0);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // setup the inputs
            double G1LeftStickY = reverseControls * gamepad1.left_stick_y;
            double G1RightStickY = -reverseControls * gamepad1.right_stick_y;
            double G1LeftStickX = -reverseControls * gamepad1.left_stick_x;
            double G1RightStickX = -reverseControls * gamepad1.right_stick_x;
            boolean G1RightBumper = gamepad1.right_bumper;
            boolean G1LeftBumper = gamepad1.left_bumper;

            double G2LeftStickY = gamepad2.left_stick_y * 0.25;

            // strafe  Mode
            frontLeftDrive.setPower(G1LeftStickY + G1RightStickX + G1LeftStickX);
            backLeftDrive.setPower(G1LeftStickY + G1RightStickX - G1LeftStickX);
            frontRightDrive.setPower(G1LeftStickY - G1RightStickX - G1LeftStickX);
            backRightDrive.setPower(G1LeftStickY - G1RightStickX + G1LeftStickX);

            // grabberArm controller uses the y button to move out and the a button to retract the grabberArm

            grabberArm.setPower(G2LeftStickY);

            if(gamepad2.a){
                servo.setPosition(0.5);
            }
            if(gamepad2.b){
                servo.setPosition(0);
            }


            //Starts intake motors
            if(gamepad1.right_bumper){
                leftIntake.setPower(1);
                rightIntake.setPower(1);
            }
            else if(gamepad1.left_bumper){
                leftIntake.setPower(-1);
                rightIntake.setPower(-1);
            }



            // Change the control direction with the x button
            if (gamepad1.x){
                reverseControls = -reverseControls;
                telemetry.addData("Controls Status", "Direction: " + reverseControls);
            }

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
//            telemetry.addData("Motors", "left (%.2f), right (%.2f)", data);
            telemetry.update();
        }
    }
}
