package org.firstinspires.ftc.teamcode.drive;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.opmode.Vision.ImageDetectorPipeline;
import org.firstinspires.ftc.teamcode.drive.opmode.Vision.JunctionPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Config
public class Bertha{

    enum State {
        None,
        PreConePickUp,
        PickAndExchange,
        AutoPickAndExchange,
        PickAndExchange_Step2,
        PickAndExchange_Step3,
        PickUpOverRide,
        ExchangeToExtake,
        IntakeReturn,
        MoveToExchange,
        MoveToExchange2
    };

    ///region Robot objects
    private Lift lift;
    private DriveTrain driveTrain;
    private Turret turret;
    private Intake intake;
    //endregion

    private ElapsedTime timer;
    private State state;
    private Telemetry telemetry;
    private IntakeScheduler intakeScheduler = new IntakeScheduler();
    private ExtakeScheduler extakeScheduler = new ExtakeScheduler();

    public Bertha(HardwareMap map, Telemetry tel){
        lift = new Lift(map, tel);
        driveTrain = new DriveTrain(map, tel);
        turret = new Turret(map, tel);
        intake = new Intake(map, tel);

        timer = new ElapsedTime();
        state = State.None;
        telemetry = tel;
    }

    private void ResetStartTimer() {
        timer.reset();
        timer.startTime();
    }

    private void PauseTimeMilliseconds(int milliseconds) {
        ResetStartTimer();
        boolean flag = true;
        while(flag)
        {
            if(timer.milliseconds() > milliseconds)
                flag = false;
        }
    }

    private void LogAllTelemetry(){
        telemetry.addData("Current State: ", state);
        turret.Telemetry();
        intake.Telemetry();
        lift.Telemetry();
    }

    OpenCvCamera webcam;
    JunctionPipeline pipeline;
    //region TeleOp
    public void RunOpMode() {
        //Todo may want to move this to bertha teleop if this does not work.
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "FalconCam"), cameraMonitorViewId);


        pipeline = new JunctionPipeline(telemetry);
        webcam.setPipeline(pipeline);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
                                         @Override
                                         public void onOpened() {
                                             webcam.startStreaming(1280, 720, OpenCvCameraRotation.UPSIDE_DOWN);
                                         }


                                         public void onError(int errorCode) {

                                         }
                                     }

        );
        intakeScheduler.start();
        extakeScheduler.start();
        switch (state)
        {
//            case PreConePickUp:
//                 && intake.GetCurrentSlidePosition() > Constants.IntakeExchanging - 100
//                if(turret.IsClawOpen()) {
//                    turret.SlideMid();
//                    PauseTimeMilliseconds(350);
//                    intake.OpenClaw();
//                    intake.IntakeOut();
//                    PauseTimeMilliseconds(75);
//                    intake.FlipDown();
//                    PauseTimeMilliseconds(500);
//                    intake.OpenClaw();
//                    state = State.None;
//                }
//                break;
            case PickAndExchange:
                intake.AutoCloseClaw();
                if(intake.AutoCloseClaw()){
                    MoveToExchange2();
                    state = State.None;
                }
                break;
            case AutoPickAndExchange:
                intake.AutoCloseClaw();
                if(intake.AutoCloseClaw() || timer.milliseconds() >= 750){
                    AutoMoveToExchange2(Turret.TurretHorizontal.AutoRight);
                    state = State.None;
                }
                break;
            case PickAndExchange_Step2:
                intake.IntakeOut();
                turret.OpenClaw();
                //intake.WaitTillIntakeMotorIsComplete();
                intake.SlideMotorExchange();
                if(!intake.IsSlideMotorBusy() ) {
                    turret.SlideIn();
                    PauseTimeMilliseconds(500);
                    intake.IntakeNewExchange();
                    turret.MoveVertical(Turret.TurretHeight.Default);
                    state = State.PickAndExchange_Step3;
                }
                break;
            case PickAndExchange_Step3:
                if(turret.IsAtVerticalPosition(Constants.TurretDefault, 10)) {
                    turret.CloseClaw();
                    if(turret.IsClawClosed()) {
                        intake.OpenClaw();
                        PauseTimeMilliseconds(150);
                        lift.MoveLift(Lift.LiftHeight.Medium);
                        intake.IntakeIn();
                        state = State.None;
                    }
                }
                break;
            default:
                break;
        }
        this.LogAllTelemetry();
    }

    private void MoveToExchangeWithCone() {
        intake.IntakeLow();
        if(intake.GetIntakePosition() >= Constants.IntakeFlipsLow) {
            intake.FlipUp();
            state = State.PickAndExchange_Step2;
        }
    }

    public void Move(Pose2d drivePower) {
       driveTrain.Move(drivePower);
    }

    public void MoveLift(Lift.LiftHeight height) throws Exception {
        lift.MoveLift(height);
    }

    public void MoveLift(int offSet) throws Exception {
        lift.MoveLift(offSet);
    }

    public void IntakeOpenClaw() {
        intake.OpenClaw();
    }

    //This moves the intake into a position to grab a cone in its low position
    public void PreConePickUp() {

        turret.SlideOut();
        PauseTimeMilliseconds(500);
        turret.MoveVertical(Turret.TurretHeight.Low);
        intake.FlipDown();
        PauseTimeMilliseconds(300);
        intake.IntakeOut();
        PauseTimeMilliseconds(500);
        intake.OpenClaw();
        PauseTimeMilliseconds(100);
        intake.OpenClaw();
        state = State.PickAndExchange;
        intake.SlideMotorOut();

    }
    public void PreConePickUpTest() {
//        scheduler.schedule(() -> turret.SlideOut(), 0);
        intakeScheduler.stop();
        intakeScheduler.start();
        turret.SlideOut();
        intakeScheduler.schedule(() -> {
            intake.FlipDown();
            turret.MoveVertical(Turret.TurretHeight.Low);
        }, TimingConstants.TestTime1);

        intakeScheduler.schedule(() -> intake.IntakeOut(), TimingConstants.TestTime2);
        intakeScheduler.schedule(() -> intake.OpenClaw(), TimingConstants.TestTime3);
        intakeScheduler.schedule(() -> {
            intake.OpenClaw();
            state = State.PickAndExchange;
            intake.SlideMotorOut();
            intakeScheduler.stop();
        }, TimingConstants.TestTime4);
    }

    //This picks up a cone and moves it past the exchange point to where the cone is in possession of controller 2
    public void PickAndExchange() {
        if(state != State.PickAndExchange || state != State.PickAndExchange_Step2 || state != State.PickAndExchange_Step3) {
            state = State.PickAndExchange;
            ResetStartTimer();
            intake.AutoCloseClaw();
            intake.IntakeOut();
        }
    }

    public void PickUpOverRide() {
        turret.SlideOut();
        PauseTimeMilliseconds(500);
        turret.MoveVertical(Turret.TurretHeight.Low);
        intake.FlipDown();
        PauseTimeMilliseconds(300);
        intake.IntakeOut();
        PauseTimeMilliseconds(500);
        intake.OpenClaw();
        PauseTimeMilliseconds(100);
        intake.OpenClaw();
        intake.SlideMotorOut();
    }

    public void ExchangeToExtake() {
        if(state != State.ExchangeToExtake) {
            state = State.ExchangeToExtake;
            lift.MoveLift(Lift.LiftHeight.High);
            turret.SlideOut();
            turret.MoveVertical(Turret.TurretHeight.Flipped);
            state = State.None;
        }
    }

    public void OpenCloseIntakeClaw() {
        intake.OpenCloseClaw();
    }

    public void OpenClaw() {
        turret.OpenClaw();
    }

    public void CloseClaw() {
        turret.CloseClaw();
    }

    public void IntakeReturn() {
        if(state != State.IntakeReturn) {
            if (!turret.IsAtHorizontalPosition(Constants.TurretDefault, 5.0)) {
                turret.MoveHorizontal(Turret.TurretHorizontal.Center);
                PauseTimeMilliseconds(750);
            }
            if(turret.IsSlideOut()) {
                turret.SlideIn();
            }
            if(!turret.IsAtVerticalPosition(Constants.ExtakeFlipIn, 2.0)) {
                turret.MoveVertical(Turret.TurretHeight.Default);
            }
            turret.MoveHorizontal(Turret.TurretHorizontal.Center);
            turret.CloseClaw();
            PauseTimeMilliseconds(250);
            turret.MoveVertical(Turret.TurretHeight.Default);
            lift.MoveLift(Lift.LiftHeight.Default);
            state = State.None;
        }
    }

    public void TurretRight() {
        turret.MoveHorizontal(Turret.TurretHorizontal.Right);
    }

    public void TurretLeft() {
        turret.MoveHorizontal(Turret.TurretHorizontal.Left);
    }

    public void LiftMedium() {
        lift.MoveLift(Lift.LiftHeight.Medium);
    }

    public void ExtakeSlideInOut() {
        if(turret.IsSlideOut())
            turret.SlideIn();
        else
            turret.SlideOut();
    }

    public void MoveLiftOffset(float positionOffset) {
        int pos = (int)positionOffset;
        lift.MoveLift(pos);
    }

    public void MoveIntake(int offset) {
        intake.SetIntakePosition(offset);
    }

    public void MoveSlide(int offset) {
        intake.SetSlidePositionOffset(offset);
    }

    public void Reset() {
        turret.CloseClaw();
        intake.CloseClaw();
        lift.MoveLift(Lift.LiftHeight.Medium);
//        PauseTimeMilliseconds();
//        lift.WaitTillCompleteMoveLift();
        intake.FlipUp();
        turret.SlideOut();
        PauseTimeMilliseconds(300);
        intake.IntakeIn();
       PauseTimeMilliseconds(300);
//        intake.WaitTillIntakeMotorIsComplete();
        intake.SlideMotorIn();
        turret.MoveHorizontal(Turret.TurretHorizontal.Center);
        PauseTimeMilliseconds(500);
        turret.SlideIn();
        PauseTimeMilliseconds(500);
        turret.MoveVertical(Turret.TurretHeight.Default);
        PauseTimeMilliseconds(250);
        turret.SlideIn();
        lift.MoveLift(Lift.LiftHeight.Default);
        turret.OpenClaw();
        state = State.None;
    }

    public void StompDown() {
        driveTrain.StompDown();
    }

    public void StompUp() {
        driveTrain.StompUp();
    }

    public void TurretVertical(double offset) {
        turret.MoveVerticalOffset(offset);
    }

    public void TurretHorizontal(double offset) {
        turret.MoveHorizontalOffset(offset);
    }

    public void TurretCenter() {
        turret.MoveHorizontal(Turret.TurretHorizontal.Center);
    }
    
//    public void MoveToExchange(){
//        MoveToExchangeWithCone();
//        state = State.MoveToExchange;
//    }
    public void MoveToExchange2() {
        lift.MoveLift(Lift.LiftHeight.Default);
        intake.CloseClaw();
        turret.SlideOut();
        turret.CloseClaw();
        turret.MoveVertical(Turret.TurretHeight.Default);
        PauseTimeMilliseconds(150);
        turret.OpenClaw();
        intake.FlipUp();
        PauseTimeMilliseconds(200);
        intake.SlideMotorExchange();
        PauseTimeMilliseconds(200);
        intake.IntakeNewExchange();
//        PauseTimeMilliseconds(500);
        PauseTimeMilliseconds(450);
        turret.SlideMid();
        PauseTimeMilliseconds(500);
        turret.CloseClaw();
        PauseTimeMilliseconds(50);
        intake.OpenClaw();
        PauseTimeMilliseconds(100);
        lift.MoveLift(Lift.LiftHeight.Medium);
        intake.IntakeIn();
        turret.SlideIn();
    }

    public void TeleOpCycle() {
        lift.MoveLift(Lift.LiftHeight.High);
        PauseTimeMilliseconds(300);
        turret.MoveVertical(Turret.TurretHeight.CycleVertical);
//        turret.SlideMid();
        PauseTimeMilliseconds(750);
        turret.MoveHorizontal(Turret.TurretHorizontal.CycleHorizontal);
        PauseTimeMilliseconds(Constants.CycleDropDelay);
        turret.MoveVertical(Turret.TurretHeight.Flipped);
    }

    //endregion

    //region Autonomous

    public void AutoCheck() {
        turret.CloseClaw();
        turret.SlideIn();
    }

    public void AutoExtake(Turret.TurretHorizontal TurretSide) {
        lift.MoveLift(Constants.AutoLiftHigh, Constants.HighVelocity);
        turret.SlideMid();
        PauseTimeMilliseconds(300);
        turret.MoveVertical(Turret.TurretHeight.CycleVertical);
        PauseTimeMilliseconds(300);
        turret.MoveHorizontal(TurretSide);
        PauseTimeMilliseconds(300);
        turret.MoveHorizontal(TurretSide);
        PauseTimeMilliseconds(300);
        turret.MoveVertical(Turret.TurretHeight.Flipped);
        PauseTimeMilliseconds(600);
        turret.OpenClaw();
        PauseTimeMilliseconds(300);
        turret.MoveVertical(Turret.TurretHeight.CycleVertical);
        PauseTimeMilliseconds(100);
        turret.CloseClaw();
        turret.MoveHorizontal(Turret.TurretHorizontal.Center);
        PauseTimeMilliseconds(500);
        turret.MoveVertical(Turret.TurretHeight.Default);
        PauseTimeMilliseconds(400);
        lift.MoveLift(Lift.LiftHeight.Default);
        PauseTimeMilliseconds(700);
        turret.SlideOut();
        turret.OpenClaw();
    }

    public void AutoIntake(int ConePosition, int SlidePose, Turret.TurretHorizontal TurretSide) {
        turret.SlideOut();
        PauseTimeMilliseconds(500);
        turret.MoveVertical(Turret.TurretHeight.Low);
        intake.FlipDown();
        PauseTimeMilliseconds(300);
        intake.AutoIntakeOut(ConePosition);
        PauseTimeMilliseconds(200);
        intake.OpenClaw();
        PauseTimeMilliseconds(100);
        intake.OpenClaw();
        PauseTimeMilliseconds(200);
        intake.OpenClaw();
        intake.SlideMotorAutoOut(SlidePose);
//        PauseTimeMilliseconds(250);
//
//        state = State.AutoPickAndExchange;
//        timer.reset();
        PauseTimeMilliseconds(750);
        intake.CloseClaw();
        PauseTimeMilliseconds(250);
        AutoMoveToExchange2(TurretSide);
    }
    public boolean AutoMoveToExchange2(Turret.TurretHorizontal TurretSide){
        intake.CloseClaw();
        turret.SlideOut();
        turret.CloseClaw();
        turret.MoveVertical(Turret.TurretHeight.Default);
        PauseTimeMilliseconds(150);
        turret.OpenClaw();
        intake.AutoFlipUp();
        PauseTimeMilliseconds(400);
        intake.IntakeNewExchange();
//        PauseTimeMilliseconds(500);
        intake.SlideMotorExchange();
        PauseTimeMilliseconds(300);
        turret.SlideMid();
        PauseTimeMilliseconds(550);
        turret.CloseClaw();
        PauseTimeMilliseconds(50);
        intake.OpenClaw();
        PauseTimeMilliseconds(50);
        lift.MoveLift(Constants.LiftMid, Constants.HighVelocity);
        intake.IntakeIn();
        PauseTimeMilliseconds(500);
        AutoExtake(TurretSide);
        return true;
    }

    public void AutoReturn() {
        turret.CloseClaw();
        turret.SlideIn();
        intake.AutoFlipUp();
        intake.SlideMotorIn();
        PauseTimeMilliseconds(500);
    }

//    public void AutoExtakeRight() {
//        lift.MoveLift(Constants.AutoLiftHigh, Constants.HighVelocity);
//        turret.SlideMid();
//        PauseTimeMilliseconds(300);
//        turret.MoveVertical(Turret.TurretHeight.CycleVertical);
//        PauseTimeMilliseconds(600);
//        turret.MoveHorizontal(Turret.TurretHorizontal.AutoLeft);
//        PauseTimeMilliseconds(600);
//        turret.MoveVertical(Turret.TurretHeight.Flipped);
//        PauseTimeMilliseconds(600);
//        turret.OpenClaw();
//        PauseTimeMilliseconds(400);
//        turret.CloseClaw();
//        turret.MoveHorizontal(Turret.TurretHorizontal.Center);
//        PauseTimeMilliseconds(500);
//        turret.MoveVertical(Turret.TurretHeight.Default);
//        PauseTimeMilliseconds(400);
//        lift.MoveLift(Lift.LiftHeight.Default);
//        PauseTimeMilliseconds(700);
//        turret.SlideOut();
//        turret.OpenClaw();
//    }

    //endregion

    public void ToggleClawFlip() {
        intake.ToggleFlip();
    }
    public void IntakeFlipDown(){
        intake.FlipDown();
    }
    public void IntakeFlipUp(){
        intake.FlipUp();
    }
}
