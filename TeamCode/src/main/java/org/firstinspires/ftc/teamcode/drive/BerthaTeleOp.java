package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "BerthaTeleOp")
public class BerthaTeleOp extends LinearOpMode {

    private Bertha bertha;

    @Override
    public void runOpMode() throws InterruptedException {
        bertha = new Bertha(hardwareMap, telemetry);

        waitForStart();
        while (!isStopRequested()) {
            try {
                GamePad1Loop();
                GamePad2Loop();
                bertha.RunOpMode();
            }
            catch (Exception ex)
            {
                telemetry.addData("ERROR in main loop", ex.getMessage());
                telemetry.addData("Inner exception", ex.getStackTrace());
            }
            telemetry.update();
        }
    }

    private void GamePad1Loop() {
        //This moves the robot forwards, backwards, and strafes left and right
        //Left Joystick
        bertha.Move(new Pose2d(
                -gamepad1.left_stick_y * -1,
                -gamepad1.left_stick_x * -1,
                -gamepad1.right_stick_x
        ));

        if(gamepad1.x)
            bertha.WheelsSpinOut();
        else if(gamepad1.a)
            bertha.PreConePickUp();
        else if(gamepad1.b)
            bertha.PickAndExchange();
        else if(gamepad1.y)
            bertha.PickUpOverRide();
        else if(gamepad1.dpad_up)
            bertha.MoveIntake(-20);
        else if(gamepad1.dpad_down)
            bertha.MoveIntake(20);
        else if(gamepad1.dpad_right)
            bertha.MoveSlide(30);
        else if(gamepad1.dpad_left)
            bertha.MoveSlide(-30);
        else if(gamepad1.share)
            bertha.Reset();
        else if(gamepad1.right_bumper)
            bertha.StompDown();
        else if(gamepad1.left_bumper)
            bertha.StompUp();
        else if(gamepad1.touchpad)
            bertha.MoveToExchange();
    }

    private void GamePad2Loop() {
        if(gamepad2.a)
            bertha.ExchangeToExtake();
        else if(gamepad2.b)
            bertha.IntakeReturn();
        else if(gamepad2.y)
            bertha.LiftMedium();
        else if(gamepad2.x)
            bertha.ExtakeSlideInOut();
        else if(gamepad2.right_trigger > 0)
            bertha.OpenClaw();
        else if(gamepad2.left_trigger > 0)
            bertha.CloseClaw();
        else if(gamepad2.right_bumper)
            bertha.TurretRight();
        else if(gamepad2.left_bumper)
            bertha.TurretLeft();
        else if(gamepad2.right_stick_y != 0)
            bertha.MoveLiftOffset(gamepad2.right_stick_y);
        else if(gamepad2.share)
            bertha.Reset();
        else if(gamepad2.dpad_up)
            bertha.TurretVertical(.0015);
        else if(gamepad2.dpad_down)
            bertha.TurretVertical(-.0015);
        else if(gamepad2.dpad_right)
            bertha.TurretHorizontal(.0035);
        else if(gamepad2.dpad_left)
            bertha.TurretHorizontal(-.0035);
        else if(gamepad2.touchpad)
            bertha.TurretCenter();
        else if(gamepad2.left_stick_button)
            bertha.TeleOpCycle();
    }
}
