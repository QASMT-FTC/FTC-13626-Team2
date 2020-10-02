package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Lift_Encoder", group="Linear Opmode")
//@Disabled
public class LiftEncoder extends LinearOpMode {

    private DcMotor liftMotor;
    int liftPosition = 0;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        liftMotor = hardwareMap.dcMotor.get("lift_motor");

        // YOU'LL NEED TO TEST IT TO SEE WHICH DIRECTION IT SHOULD BE
        liftMotor.setDirection(DcMotor.Direction.REVERSE);
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.addData("Mode", "waiting");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            double liftPower;
            int sensitivity = 360; //360 will move from 0 to 90 degrees in joystick position 0 to 1.

            // YOU MAY NEED TO CHANGE THE DIRECTION OF THIS STICK. RIGHT NOW IT IS NEGATIVE.
            double liftStick = -gamepad2.left_stick_y;
            liftPower    = Range.clip(liftStick, -1.0, 1.0) ;

            liftPosition += (int)liftPower*sensitivity;
            liftPosition = Range.clip(liftPosition, 0, 360);

            // MOVES UP FROM POSITION 0 TO 90 DEGREES UP.
            liftMotor.setTargetPosition(liftPosition);
            liftMotor.setPower(0.2);
        }
    }
}
