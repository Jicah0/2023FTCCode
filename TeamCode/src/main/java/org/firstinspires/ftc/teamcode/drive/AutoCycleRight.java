package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.opmode.Vision.ImageDetectorPipeline;
import org.firstinspires.ftc.teamcode.drive.opmode.Vision.ImageDetectorPipelineArea;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous(name = "CycleRight", group="Bertha", preselectTeleOp = "BerthaTeleOp")
public class AutoCycleRight extends LinearOpMode{

    enum State{
        PlacingCone,
        OpeningClaw,
        PreCone,
        ClawDrop, None
    }

    @Override
    public void runOpMode() throws InterruptedException {

//        String pipelineColorSeen = pipeline.ColorSeen;
        //Initializes Bertha Autonomous with the State to right for the right side
        BerthaAuto bertha = new BerthaAuto(hardwareMap, telemetry, BerthaAuto.AutoState.Right);
        bertha.AutoCheck();

        ElapsedTime timer = new ElapsedTime();
        Constants.Right = true;
        waitForStart();
        timer.startTime();

        //Drive to Cones
        bertha.DriveToConeStation(AutonomousDrive.DriveSpeed.Conservative);

        bertha.TeleOpCycle();
        while(bertha.GetConeCount() <= Constants.ConeCount && timer.seconds() <= 27){
            bertha.RunOpMode();
            if(bertha.extaking == Bertha.Extaking.None && bertha.lift.IsLiftAtPosition(Constants.LiftHigh, 200)){
                bertha.IncrementCone(1);
                bertha.lift.Wait(500);
                bertha.IntakeReturn();
            }
        }

        //TODO MAKE SURE TO UNBLOCK THIS TO ALLOW CYCLES TO HAPPEN
        //region UNBLOCK THIS TO ALLOW CYCLES
//        try {
//            //Drop First code
////            bertha.DropFirstCone();
//
//            //Cycle remaining Code while we have cones and our timer is less than 25 seconds
//            while (opModeIsActive() && bertha.GetConeCount() <= Constants.ConeCount && timer.seconds() <= 25) {
////                bertha.CycleCone();
//            }
//        }
//        catch(Exception ex){
//            telemetry.addData("Exception: ", ex.getMessage());
//            telemetry.update();
//            throw new InterruptedException();
//        }
//        //Cycle Bertha to default state
       bertha.CycleDown();
        //endregion

        //Park Bertha
        bertha.Park();
    }
}