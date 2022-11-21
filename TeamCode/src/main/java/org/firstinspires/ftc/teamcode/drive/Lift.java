package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

//import org.apache.commons.math3.analysis.function.Constant;
//import org.firstinspires.ftc.robotcore.external.Const;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Lift extends BaseRobot{
    public enum LiftHeight{
        High,
        Medium,
        Default
    }

    private DcMotor liftMotorR, liftMotorL;
    private int currentPosition;

    public Lift(HardwareMap map, Telemetry tel){
        super(map, tel);
        MapHardware();
    }

    public void MoveLift(LiftHeight height) {
         if(height == LiftHeight.Default)
            MoveLift(Constants.LiftDefault, Constants.LowVelocity);
        else if(height == LiftHeight.High)
            MoveLift(Constants.LiftHigh, Constants.HighVelocity);
        else {
            // to determine when to move fast going up, and slow moving down
            int velocity = (currentPosition > Constants.LiftMid)? Constants.LowVelocity : Constants.HighVelocity;
            MoveLift(Constants.LiftMid, velocity);
        }
    }
    public void MoveLift(int position, int velocity) {
        if(velocity > Constants.HighVelocity)
            velocity = Constants.HighVelocity;
        if(position > Constants.LiftHigh)
            position = Constants.LiftHigh;
        if(position < Constants.LiftDefault)
            position = Constants.LiftDefault;

        liftMotorR.setTargetPosition(position);
        liftMotorL.setTargetPosition(position);
        liftMotorR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotorL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ((DcMotorEx) liftMotorL).setVelocity(velocity);
        ((DcMotorEx) liftMotorR).setVelocity(velocity);
        currentPosition = position;
    }

    /**
     * moves the lift by an offset that's passed in
     * @param positionOffset
     */
    public void MoveLift(int positionOffset) {
        int newPosition = currentPosition + positionOffset;
        if(newPosition > Constants.LiftHigh)
            newPosition = Constants.LiftHigh;
        if(newPosition < Constants.LiftDefault)
            newPosition = Constants.LiftDefault;

        int velocity = (positionOffset > 0)? Constants.HighVelocity : Constants.LowVelocity;
        MoveLift(newPosition, velocity);
        currentPosition = newPosition;
    }

    @Override
    protected void MapHardware() {
        liftMotorL = hardwareMap.get(DcMotor.class, "LiftMotorL");
        liftMotorR = hardwareMap.get(DcMotor.class, "LiftMotorR");

        liftMotorR.setDirection(DcMotorSimple.Direction.REVERSE);

        liftMotorL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotorR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public void Reset() {
        liftMotorR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotorL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}
