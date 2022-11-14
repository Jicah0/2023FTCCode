package org.firstinspires.ftc.teamcode.drive;

import static org.firstinspires.ftc.teamcode.drive.DriveConstants.MOTOR_VELO_PID;
import static org.firstinspires.ftc.teamcode.drive.DriveConstants.RUN_USING_ENCODER;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Arrays;
import java.util.List;

public class Bertha extends BaseRobot{
    enum Direction{
        Forward,
        Backward,
        Up,
        Down
    }
    enum ClawDirection{
        Open,
        Close
    }

    //private HardwareMap hardwareMap;

    //region Constants & Statics
    private final PIDCoefficients TRANSLATIONAL_PID = new PIDCoefficients(8, 0, 0);
    private final PIDCoefficients HEADING_PID = new PIDCoefficients(5, 0, 0);

    private final double LATERAL_MULTIPLIER = 1;

    private final double VX_WEIGHT = 1;
    private final double VY_WEIGHT = 1;
    private final double OMEGA_WEIGHT = 1;
    //endregion

    //region Robot Hardware
    private DcMotorEx leftFront, leftRear, rightRear, rightFront;
    public DcMotor IntakeSlideMotor, IntakeFlipMotor;
    public CRServo IntakeWheels;
    public Servo IntakeFlip, Stomp, OdoRetractRight, OdoRetractLeft, OdoRetractRear, ExtakeFlip1, ExtakeFlip2, Turret1, SlideExtension, Claw;
    public ColorRangeSensor IntakeSensor;
    private BNO055IMU imu;
    private VoltageSensor batteryVoltageSensor;

    private List<DcMotorEx> motors;
    //endregion

    ///region Robot objects
    private Lift lift;
    //TODO: Create objects for Intake, Stomp, DriveTrain(Wheels), Turret(includes claw)
    //endregion

    public Bertha(HardwareMap map, Telemetry tel){
        super(map, tel);
        this.MapHardware();

        lift = new Lift(map, tel);
    }

    public void Drive(Pose2d drivePower) {
        Pose2d vel = drivePower;

        if (Math.abs(drivePower.getX()) + Math.abs(drivePower.getY())
                + Math.abs(drivePower.getHeading()) > 1) {
            // re-normalize the powers according to the weights
            double denom = VX_WEIGHT * Math.abs(drivePower.getX())
                    + VY_WEIGHT * Math.abs(drivePower.getY())
                    + OMEGA_WEIGHT * Math.abs(drivePower.getHeading());

            vel = new Pose2d(
                    VX_WEIGHT * drivePower.getX(),
                    VY_WEIGHT * drivePower.getY(),
                    OMEGA_WEIGHT * drivePower.getHeading()
            ).div(denom);
        }

        //setDrivePower(vel);
    }

    public void ExtendFlipMotor(){
        //TODO: Implement code to extend flip motor
    }
    public void RetractFlipMotor(){
        //TODO: Implement retract flip motor
    }
    public void StartIntakeWheels(Direction direction){
        //TODO: Implement code to move intake wheels
    }
    public void StopIntakeWheels(){
        //TODO: Stop intake wheels
    }
    public void SetClawDirection(ClawDirection direction){
        //TODO: set the claw direction
    }
    public void MoveLift(Lift.LiftDirection direction){
        lift.MoveLift(direction);
    }
    public void MoveLift(Lift.LiftDirection direction, double distance){
        lift.MoveLift(direction, distance);
    }



    /**
     * Maps all the hardware to private objects
     */
    @Override
    protected void MapHardware(){
        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftRear = hardwareMap.get(DcMotorEx.class, "leftRear");
        rightRear = hardwareMap.get(DcMotorEx.class, "rightRear");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        IntakeFlipMotor = hardwareMap.get(DcMotor.class, "IntakeFlipMotor");
        IntakeSlideMotor = hardwareMap.get(DcMotor.class, "IntakeSlideMotor");


        IntakeWheels = hardwareMap.get(CRServo.class, "IntakeWheels");
        IntakeFlip = hardwareMap.get(Servo.class, "IntakeFlip");
        Stomp = hardwareMap.get(Servo.class, "Stomp");
        OdoRetractRight = hardwareMap.get(Servo.class, "OdoRetractRight");
        OdoRetractLeft = hardwareMap.get(Servo.class, "OdoRetractLeft");
        OdoRetractRear = hardwareMap.get(Servo.class, "OdoRetractRear");
        ExtakeFlip1 = hardwareMap.get(Servo.class, "ExtakeFlip1");
        ExtakeFlip2 = hardwareMap.get(Servo.class, "ExtakeFlip2");
        Turret1 = hardwareMap.get(Servo.class, "Turret1");
        SlideExtension = hardwareMap.get(Servo.class, "SlideExtension");
        Claw = hardwareMap.get(Servo.class, "Claw");

        IntakeSensor = hardwareMap.get(ColorRangeSensor.class, "IntakeSensor");

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);

        motors = Arrays.asList(leftFront, leftRear, rightRear, rightFront);

        for (DcMotorEx motor : motors) {
            MotorConfigurationType motorConfigurationType = motor.getMotorType().clone();
            motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
            motor.setMotorType(motorConfigurationType);
        }

        if (RUN_USING_ENCODER) {
            setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        if (RUN_USING_ENCODER && MOTOR_VELO_PID != null) {
            setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, MOTOR_VELO_PID);
        }

        // TODO: reverse any motors using DcMotor.setDirection()
        //Reversing motor directions as needed
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        IntakeSlideMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        IntakeFlipMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        //setting zero power behaviors
        IntakeSlideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        IntakeFlipMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        //setting servo scale ranges
        Claw.scaleRange(0.2, 0.5);
    }

    private void setMode(DcMotor.RunMode runMode) {
        for (DcMotorEx motor : motors) {
            motor.setMode(runMode);
        }
    }

    private void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        for (DcMotorEx motor : motors) {
            motor.setZeroPowerBehavior(zeroPowerBehavior);
        }
    }

    private void setPIDFCoefficients(DcMotor.RunMode runMode, PIDFCoefficients coefficients) {
        PIDFCoefficients compensatedCoefficients = new PIDFCoefficients(
                coefficients.p, coefficients.i, coefficients.d,
                coefficients.f * 12 / batteryVoltageSensor.getVoltage()
        );

        for (DcMotorEx motor : motors) {
            motor.setPIDFCoefficients(runMode, compensatedCoefficients);
        }
    }

    private void setMotorPowers(double v, double v1, double v2, double v3) {
        leftFront.setPower(v);
        leftRear.setPower(v1);
        rightRear.setPower(v2);
        rightFront.setPower(v3);
    }
}
