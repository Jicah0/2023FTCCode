package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class Turret extends BaseRobot{


    public enum TurretHeight{
        Default,
        Low,
        Flipped
    }

    public enum TurretHorizontal{
        Left,
        Right,
        Center
    }

    private double currentTurretHeight;
    private double currentTurretHorizontal;

    private Servo ExtakeFlip1, ExtakeFlip2, Turret1, Claw, SlideExtension, SlideExtension2;

    public Turret(HardwareMap map, Telemetry tel) {
        super(map, tel);
//        MapHardware();
        currentTurretHeight = 0.0;
        currentTurretHorizontal = Constants.TurretDefault;
    }

    @Override
    protected void MapHardware() {
        ExtakeFlip1 = hardwareMap.get(Servo.class, "ExtakeFlip1");
        ExtakeFlip2 = hardwareMap.get(Servo.class, "ExtakeFlip2");
        Turret1 = hardwareMap.get(Servo.class, "Turret1");
        Claw = hardwareMap.get(Servo.class, "Claw");
        SlideExtension = hardwareMap.get(Servo.class, "SlideExtension");
        SlideExtension2 = hardwareMap.get(Servo.class, "SlideExtension2");
    }

    private double CheckBoundries(double position) {
        if(position > 1)
            position = 1.0;
        if(position < 0)
            position = 0.0;
        return position;
    }

    public void MoveVerticalOffset(double offset) {
        //TODO check these values
//        double exFlip1Position = ExtakeFlip1.getPosition() + offset;
//        double exFlip2Position = ExtakeFlip2.getPosition() - offset;
//        MoveVertical(exFlip1Position, exFlip2Position);
    }

    public void MoveVertical(double extakeFlip1Position, double extakeFlip2Position) {
        ExtakeFlip1.setPosition(extakeFlip1Position);
        ExtakeFlip2.setPosition(extakeFlip2Position);
        this.LogTelemetry("Current Turret Position 1: ", extakeFlip1Position);
        this.LogTelemetry("Current Turret Position 2: ", extakeFlip2Position);
    }

    public void MoveVertical(TurretHeight height) {
        if(height == TurretHeight.Flipped)
            MoveVertical(Constants.ExtakeFlipOut, Constants.ExtakeFlipOut2);
//        else if(height == TurretHeight.Low)
//            MoveVertical(Constants.ExtakeFlipLow);
        else
            MoveVertical(Constants.ExtakeFlipIn, Constants.ExtakeFlipIn2);
    }

    public void MoveHorizontalOffset(double offset) {
        currentTurretHorizontal += offset;
        currentTurretHeight = CheckBoundries(currentTurretHeight);
        MoveHorizontal(currentTurretHorizontal);
    }

    public void MoveHorizontal(double position) {
        Turret1.setPosition(position);
        currentTurretHorizontal = position;
        LogTelemetry("Turret Horizontal: ", position);
    }

    public void MoveHorizontal(TurretHorizontal horizontal) {
        if(horizontal == TurretHorizontal.Left)
            MoveHorizontal(Constants.TurretLeft);
        else if(horizontal == TurretHorizontal.Right)
            MoveHorizontal(Constants.TurretRight);
        else
            MoveHorizontal(Constants.TurretDefault);
    }

    public void OpenClaw() {
        Claw.setPosition(Constants.ClawOpen);
    }

    public void CloseClaw() {
        Claw.setPosition(Constants.ClawClosed);
    }

    public boolean IsClawOpen() {
        if(Claw.getPosition() == Constants.ClawOpen)
            return true;
        else
            return false;
    }

    public boolean IsClawClosed() {
        if(Claw.getPosition() == Constants.ClawClosed)
            return true;
        else
            return false;
    }

    public void SlideIn(){
        SlideExtension.setPosition(Constants.SlideIn);
        SlideExtension2.setPosition(Constants.SlideIn2);
    }

    public void SlideOut(){
        SlideExtension.setPosition(Constants.SlideOut);
        SlideExtension2.setPosition(Constants.SlideOut2);
    }

    public boolean IsSlideOut() {
        return this.IsAtPosition(Math.abs(Constants.SlideOut),
                Math.abs(SlideExtension.getPosition()),
                5.0);
    }

    public boolean IsAtVerticalPosition(double position, double buffer) {
        return this.IsAtPosition(Math.abs(position),
                Math.abs(ExtakeFlip1.getPosition()),
                buffer);
    }
    public boolean IsAtHorizontalPosition(double position, double buffer) {
        return this.IsAtPosition(Math.abs(position),
                Math.abs(Turret1.getPosition()),
                buffer);
    }
}
