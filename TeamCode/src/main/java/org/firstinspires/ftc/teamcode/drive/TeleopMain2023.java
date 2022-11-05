package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.ams.AMSColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.opencv.core.Mat;

@Config
@TeleOp(name="TeleOpMain")
public class TeleopMain2023 extends LinearOpMode {

    // An Enum is used to represent lift states.
    // (This is one thing enums are designed to do)
    public enum CycleState {
        START,
        IntakeFullDrop,
        IntakeIn,
        ExtakeTransfer,
        LiftUp,
        Drop,
        Retract,
        FullRetract
    };
    public enum StompState{
      Down,
      Up
    };

    public enum OdoState{
        Down,
        Up
    };

    // The liftState variable is declared out here
    // so its value persists between loop() calls
    CycleState cycleState = CycleState.START;
    StompState stompState = StompState.Up;
    OdoState odoState = OdoState.Down;

    // Some hardware access boilerplate; these would be initialized in init()
    // the lift motor, it's in RUN_TO_POSITION mode
    public DcMotor IntakeSlideMotor;
    public DcMotor IntakeFlipMotor;
    public DcMotor liftMotorR;
    public DcMotor liftMotorL;

    // the dump servo
    public CRServo IntakeWheels;
    public Servo IntakeFlip;
    public Servo Stomp;
    public Servo OdoRetractRight;
    public Servo OdoRetractLeft;
    public Servo OdoRetractRear;
    public Servo ExtakeFlip1;
    public Servo ExtakeFlip2;
    public Servo Turret1;
//    public Servo Turret2;
    public Servo SlideExtension;
    public Servo Claw;

    public ColorRangeSensor IntakeSensor;

    private boolean readyToStomp = true;
    private boolean odoReady = true;

    int liftHeightTop = 34; //Height lift needs to lift in inches
    int liftHeightMiddle = 24; //Height lift needs to lift in inches
    int liftHeightBottom = 0; //Height lift needs to lift in inches

    double StompDown = 0.5;
    double StompUp = 0;

    double TurretRight = 0.5;
    double TurretLeft = 0;
    double TurretDefault = 0.25;

    double OdoUp = 0.5;
    double OdoDown = 0;

//    double motorResolutionLift = 537.7; //resolution for the motor
//    double motorResolutionIntakeFlip = 537.7; //resolution for the motor
//    double motorResolutionIntakeSlide = 384.5; //resolution for the motor


    double ServoIntakeFlipIntaking = 0.9;
    double ServoIntakeFlipExchanging = 0;
    public boolean Left = false;



    public int IntakeOut = 790;
    public int IntakeExchanging = -500;
    public int IntakeIn = 0;
    public int LiftHigh = 1000;
    public int LiftMid = 400;
    public int LiftLow = 0;
    public int IntakeFlips = -600;
    public int IntakeFlipsLow = -500;
    public int IntakeFlipsIn = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        IntakeFlipMotor = hardwareMap.get(DcMotor.class, "IntakeFlipMotor");
        IntakeSlideMotor = hardwareMap.get(DcMotor.class, "IntakeSlideMotor");
        liftMotorL = hardwareMap.get(DcMotor.class, "LiftMotorL");
        liftMotorR = hardwareMap.get(DcMotor.class, "LiftMotorR");

        IntakeWheels = hardwareMap.get(CRServo.class, "IntakeWheels");
        IntakeFlip = hardwareMap.get(Servo.class, "IntakeFlip");
        Stomp = hardwareMap.get(Servo.class, "Stomp");
        OdoRetractRight = hardwareMap.get(Servo.class, "OdoRetractRight");
        OdoRetractLeft = hardwareMap.get(Servo.class, "OdoRetractLeft");
        OdoRetractRear = hardwareMap.get(Servo.class, "OdoRetractRear");
        ExtakeFlip1 = hardwareMap.get(Servo.class, "ExtakeFlip1");
        ExtakeFlip2 = hardwareMap.get(Servo.class, "ExtakeFlip2");
        Turret1 = hardwareMap.get(Servo.class, "Turret1");
//        Turret2 = hardwareMap.get(Servo.class, "Turret2");
        SlideExtension = hardwareMap.get(Servo.class, "SlideExtension");
        Claw = hardwareMap.get(Servo.class, "Claw");

        IntakeSensor = hardwareMap.get(ColorRangeSensor.class, "IntakeSensor");

        Claw.scaleRange(0.2, 0.5);
        ExtakeFlip1.scaleRange(0, 1);
        ExtakeFlip2.scaleRange(0, 1);

        IntakeFlipMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        IntakeSlideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        liftMotorR.setDirection(DcMotorSimple.Direction.REVERSE);
        IntakeSlideMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        IntakeFlipMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        IntakeSlideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        IntakeFlipMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        ElapsedTime timer = new ElapsedTime();
        timer.startTime();

        waitForStart();

        while (!isStopRequested()) {

            if(gamepad1.options){
                IntakeFlipMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                IntakeSlideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }

            if(IntakeFlipMotor.getCurrentPosition() <= 50){
                IntakeWheels.setPower(0);
            }
            if(IntakeFlipMotor.getCurrentPosition() >= 500){
                IntakeWheels.setPower(1);
            }

            switch (cycleState) {
            //Worked on
            case START:
                // Waiting for a press
                if (gamepad1.a) {
                    // x is pressed, start cycle opperation
                    IntakeSlideMotor.setTargetPosition(IntakeOut);//-790
                    IntakeSlideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    ((DcMotorEx) IntakeSlideMotor).setVelocity(2700);
                    IntakeFlip.setPosition(ServoIntakeFlipIntaking);
                    Claw.setPosition(0.8);

                }
                if (Math.abs(IntakeSlideMotor.getCurrentPosition() - IntakeOut) <= 100) {
                    IntakeFlip.setPosition(0.9);
                    IntakeFlipMotor.setTargetPosition(450);
                    IntakeFlipMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    ((DcMotorEx) IntakeFlipMotor).setVelocity(2700);
                    cycleState = CycleState.IntakeFullDrop;
                }
                break;

            //Worked on
            case IntakeFullDrop:
                if (gamepad1.b) {
                    IntakeFlipMotor.setTargetPosition(550);
                    //300 for above cone
                    IntakeFlipMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    ((DcMotorEx) IntakeFlipMotor).setVelocity(2700);
                    cycleState = CycleState.IntakeIn;
                }
                break;

                //Worked on
            case IntakeIn:
                if (gamepad1.a) {
                    IntakeFlip.setPosition(0.2);
                    IntakeWheels.setPower(0.5);
                    timer.reset();
                    timer.startTime();
                    cycleState = CycleState.ExtakeTransfer;
                }
                if (gamepad1.x) {
                    IntakeFlip.setPosition(0.2);
                    IntakeWheels.setPower(0.5);
                    Left = true;
                    timer.reset();
                    timer.startTime();
                    cycleState = CycleState.ExtakeTransfer;
                }
                break;

            //Worked on
            case ExtakeTransfer:
                    IntakeFlipMotor.setTargetPosition(0);
                    ((DcMotorEx) IntakeFlipMotor).setVelocity(600);
                    IntakeFlipMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    IntakeSlideMotor.setTargetPosition(650);
                if (Math.abs(IntakeFlipMotor.getCurrentPosition()) <= 20 && timer.time() >= 0.15) {
                    Claw.setPosition(0);
                    timer.reset();
                    timer.startTime();
                    cycleState = CycleState.LiftUp;
                }
                break;

            //Worked on
            case LiftUp:
                ExtakeFlip1.setPosition(1);
                ExtakeFlip2.setPosition(0); //Wrong dirrection
                SlideExtension.setPosition(0);
                IntakeSlideMotor.setTargetPosition(IntakeOut);
//                IntakeFlip.setPosition(ServoIntakeFlipIntaking);
                if (!Left) {
                    liftMotorR.setTargetPosition(1000);//-790
                    liftMotorL.setTargetPosition(1000);//-790
                    liftMotorR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    liftMotorL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    ((DcMotorEx) liftMotorL).setVelocity(2700);
                    ((DcMotorEx) liftMotorR).setVelocity(2700);


                    if(timer.seconds() > 2) {
                        Turret1.setPosition(TurretRight);
//                        Turret2.setPosition(TurretRight);
                        timer.reset();
                        timer.startTime();
                        cycleState = CycleState.Drop;
                    }
                }else{
                    liftMotorR.setTargetPosition(LiftMid);//-790
                    liftMotorL.setTargetPosition(LiftMid);//-790
                    liftMotorR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    liftMotorL.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                    if(timer.seconds() > 2) {
                        Turret1.setPosition(TurretLeft);
//                        Turret2.setPosition(TurretLeft);
                        timer.reset();
                        timer.startTime();
                        cycleState = CycleState.Drop;
                    }
                }
                break;

            //Worked on
            case Drop:
                if(gamepad1.a){
                    Claw.setPosition(0.5);
                    sleep(150);
                    //Retract here as well
                    Turret1.setPosition(TurretDefault);
//                    Turret2.setPosition(TurretDefault);
                    SlideExtension.setPosition(1);
                    liftMotorR.setTargetPosition(0);//-790
                    liftMotorL.setTargetPosition(0);//-790
                    liftMotorR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    liftMotorL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    ((DcMotorEx) liftMotorL).setVelocity(537);
                    ((DcMotorEx) liftMotorR).setVelocity(537);
                    if(timer.time() >= 1) {
                        ExtakeFlip1.setPosition(-1);
                        ExtakeFlip2.setPosition(1);
                        cycleState = CycleState.START;
                    }
                }
                break;

            //
            case FullRetract:
                Turret1.setPosition(TurretDefault);
//                Turret2.setPosition(TurretDefault);
                Stomp.setPosition(StompUp);
                IntakeFlip.setPosition(ServoIntakeFlipExchanging);
                IntakeSlideMotor.setTargetPosition( IntakeIn);
                IntakeFlipMotor.setTargetPosition(IntakeFlipsIn);
                SlideExtension.setPosition(1);
                liftMotorL.setTargetPosition(LiftLow);
                liftMotorR.setTargetPosition(LiftLow);
                Claw.setPosition(0.4);
                IntakeWheels.setPower(0);
                    ExtakeFlip1.setPosition(0);
//                    ExtakeFlip2.setPosition(1);
                    cycleState = CycleState.START;
                break;

            //
            default:
                // should never be reached, as liftState should never be null
                cycleState = CycleState.START;
        }

        if(readyToStomp && gamepad1.left_trigger >= 0.4) {
            switch (stompState) {

                case Up:
                    Stomp.setPosition(StompDown);
                    stompState = StompState.Down;
                    break;

                case Down:
                    Stomp.setPosition(StompUp);
                    stompState = StompState.Up;
                    break;

                default:
                    // should never be reached, as liftState should never be null
                    stompState = StompState.Up;
            }
            readyToStomp = false;
        } else if (gamepad1.left_trigger < 0.5){
            readyToStomp = true;
        }

        if(odoReady && gamepad1.share) {
            switch (odoState) {
                case Up:
                    OdoRetractRear.setPosition(0);
                    OdoRetractLeft.setPosition(OdoUp);
                    OdoRetractRight.setPosition(OdoUp);
                    odoState = OdoState.Down;
                    break;

                case Down:
                    OdoRetractRear.setPosition(0.5);
                    OdoRetractLeft.setPosition(OdoDown);
                    OdoRetractRight.setPosition(OdoDown);
                    odoState = OdoState.Up;
                    break;

                default:
                    // should never be reached, as liftState should never be null
                    odoState = OdoState.Up;
            }
            odoReady = false;
        } else if (!gamepad1.start){
                odoReady = true;
        }



    if(gamepad1.right_bumper && cycleState != CycleState.START){
        cycleState = CycleState.START;
        // small optimization, instead of repeating ourselves in each
        // lift state case besides LIFT_START for the cancel action,
        // it's just handled here
    }
        if(gamepad1.left_bumper){
            cycleState = CycleState.FullRetract;
            stompState = StompState.Up;
        }


        drive.setWeightedDrivePower(
                new Pose2d(
                    -gamepad1.left_stick_y * -1,
                    -gamepad1.left_stick_x * -1,
                    -gamepad1.right_stick_x
                )
        );

            drive.update();

            Pose2d poseEstimate = drive.getPoseEstimate();
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());
            telemetry.addData("Current states", cycleState);
            telemetry.addData("Current states", stompState);
            telemetry.addData("SensorValue", IntakeSensor.getDistance(DistanceUnit.MM));
            telemetry.addData("IntakeMotorPos", IntakeSlideMotor.getCurrentPosition());
            telemetry.addData("LiftMotorR", liftMotorR.getCurrentPosition());
            telemetry.addData("LiftMotorL", liftMotorL.getCurrentPosition());
            telemetry.addData("IntakeFlipMotor", IntakeFlipMotor.getCurrentPosition());
            telemetry.addData("IntakeFlip", IntakeFlip.getPosition());
            telemetry.addData("Stomp", Stomp.getPosition());
            telemetry.addData("OdoRetractRight", OdoRetractRight.getPosition());
            telemetry.addData("OdoRetractLeft", OdoRetractLeft.getPosition());
            telemetry.addData("OdoRetractRear", OdoRetractRear.getPosition());
            telemetry.addData("ExtakeFlip1", ExtakeFlip1.getPosition());
            telemetry.addData("ExtakeFlip2", ExtakeFlip2.getPosition());
            telemetry.addData("Turret1", Turret1.getPosition());
//            telemetry.addData("Turret2", Turret2.getPosition());
            telemetry.addData("SlideExtension", SlideExtension.getPosition());
            telemetry.addData("Claw", Claw.getPosition());
            telemetry.update();




        }
    }
}
