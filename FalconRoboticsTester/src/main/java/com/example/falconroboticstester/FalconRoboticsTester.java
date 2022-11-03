package com.example.falconroboticstester;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.SampleMecanumDrive;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;

public class FalconRoboticsTester {


    public static Vars GetVars(Mode mode){
        Vars v = new Vars();

        if(mode == Mode.LeftRed) {
            // LEFT RED

            //start bot at pose x = 30, y = 64, heading 90 degrees
            v.Init(-35, -60, 90, -1, 0, 90);

            v.d1 = 90;
            v.d2 = -180;
            v.d3 = 0;
            v.d4 = 0;

            // This is an x value
            v.c1 = -12;
            // This is a y-value
            v.c2 = -2;
            // This is an x-value
            v.c3 = -16;
            // This is a y-value
            v.c4 = -11;
            // This is an x-value
            v.c5 = -25;
            // This is a y-value
            v.c6 = -4;
            // This is a y-value
            v.c7 = 0;
            // This is an x-value
            v.c8 = -35;
            // This is a y-value
            v.c9 = -20;
            // This is an x-value
            v.c10 = -56;
            // This is an x-value
            v.c11 = -54;
            // This is an x-value
            v.c12 = -50;
            // This is an x-value
            v.c13 = -46;
            // This is an x-value
            v.c14 = -42;
            // This is a y-value
            v.c15 = -36;
            // This is an x-value
            v.c16 = -36;
            // This is a y-value
            v.c17 = -32;
            // This is an x-value
            v.c18 = -60;
        }
        else if(mode == Mode.RightRed) {
            // RIGHT RED
            //start bot at pose x = 30, y = 64, heading 90 degrees
            //start bot at pose x = 12, y = 0, heading 90 degrees
            v.Init(35, 60, 270, 12, 0, 270);

            v.d1 = 90;
            v.d2 = 0;
            v.d3 = 180;
            v.d4 = 180;

            // This is an x value
            v.c1 = 12;
            // This is a y-value
            v.c2 = -2;
            // This is an x-value
            v.c3 = 16;
            // This is a y-value
            v.c4 = -11;
            // This is an x-value
            v.c5 = 25;
            // This is a y-value
            v.c6 = -4;
            // This is a y-value
            v.c7 = 0;
            // This is an x-value
            v.c8 = 35;
            // This is a y-value
            v.c9 = -20;
            // This is an x-value
            v.c10 = 56;
            // This is an x-value
            v.c11 = 54;
            // This is an x-value
            v.c12 = 50;
            // This is an x-value
            v.c13 = 46;
            // This is an x-value
            v.c14 = 42;
            // This is a y-value
            v.c15 = -36;
            // This is an x-value
            v.c16 = 36;
            // This is a y-value
            v.c17 = -32;
            // This is an x-value
            v.c18 = 60;
        }
        else if(mode == Mode.LeftBlue) {
            // LEFT BLUE
            //start bot at pose x = 30, y = 64, heading 90 degrees
            //static Pose2d startPose = new Pose2d(-35, 60, Math.toRadians(270));
            //start bot at pose x = 12, y = 0, heading 90 degrees
            //static Pose2d endPose = new Pose2d(-12, 0, Math.toRadians(270));
            v.Init(-35, 60, 270, -12, 0, 270);
            v.d1 = 270;
            v.d2 = -180;
            v.d3 = 0;
            v.d4 = 0;

            // This is an x value
            v.c1 = -12;
            // This is a y-value
            v.c2 = -2;
            // This is an x-value
            v.c3 = -16;
            // This is a y-value
            v.c4 = 12;
            // This is an x-value
            v.c5 = -25;
            // This is a y-value
            v.c6 = 4;
            // This is a y-value
            v.c7 = 0;
            // This is an x-value
            v.c8 = -35;
            // This is a y-value
            v.c9 = 20;
            // This is an x-value
            v.c10 = -56;
            // This is an x-value
            v.c11 = -54;
            // This is an x-value
            v.c12 = -50;
            // This is an x-value
            v.c13 = -46;
            // This is an x-value
            v.c14 = -42;
            // This is a y-value
            v.c15 = 36;
            // This is an x-value
            v.c16 = -36;
            // This is a y-value
            v.c17 = 32;
            // This is an x-value
            v.c18 = -60;
        }
        else if(mode == Mode.RightBlue) {
            // RIGHT BLUE
            //start bot at pose x = 30, y = 64, heading 90 degrees
            //static Pose2d startPose = new Pose2d(-35, 60, Math.toRadians(270));
            //start bot at pose x = 12, y = 0, heading 90 degrees
            //static Pose2d endPose = new Pose2d(-12, 0, Math.toRadians(270));
            v.Init(-35, 60, 270, -12, 0, 270);

            v.d1 = 270;
            v.d2 = -180;
            v.d3 = 0;
            v.d4 = 0;

            // These values should be x = Positive, y = Positive

            // This is an x value
            v.c1 = -12;
            // This is a y-value
            v.c2 = 2;
            // This is an x-value
            v.c3 = -16;
            // This is a y-value
            v.c4 = 11;
            // This is an x-value
            v.c5 = -25;
            // This is a y-value
            v.c6 = 4;
            // This is a y-value
            v.c7 = 0;
            // This is an x-value
            v.c8 = -35;
            // This is a y-value
            v.c9 = 20;
            // This is an x-value
            v.c10 = -56;
            // This is an x-value
            v.c11 = -54;
            // This is an x-value
            v.c12 = -50;
            // This is an x-value
            v.c13 = -46;
            // This is an x-value
            v.c14 = -42;
            // This is a y-value
            v.c15 = 36;
            // This is an x-value
            v.c16 = -36;
            // This is a y-value
            v.c17 = 32;
            // This is an x-value
            v.c18 = -60;
        }
        else
        {
            v = new Vars();
        }
        return v;
    }

    //drive.trajectoryBuilder(new Pose2d()).addTemporalMarker(3, () -> {Bucket.setPosition(intaking);}).build();
//.UNSTABLE_addTemporalMarkerOffset(0, () -> )
    //trajectory0
    public static void main(String[] args) {
        //Vars v = GetVars(Mode.RightRed);
        //Vars v = GetVars(Mode.LeftRed);
        Vars v = GetVars(Mode.RightBlue);
        //Vars v = GetVars(Mode.LeftBlue);

        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(100, 100, Math.toRadians(180), Math.toRadians(180), 12)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(v.startPose)

                                // The segment goes from the origin to the first extake
                                .lineToSplineHeading(new Pose2d(v.c8, v.c9, Math.toRadians(v.d1)))
                                .splineToConstantHeading(new Vector2d(v.c5, v.c4), Math.toRadians(v.d3))
                                .lineToSplineHeading(new Pose2d(v.c3, v.c4, Math.toRadians(v.d1)))
                                .splineToConstantHeading(new Vector2d(v.c1, v.c6), Math.toRadians(v.d1))
                                .lineToSplineHeading(new Pose2d(v.c1, v.c7, Math.toRadians(v.d1)))
                                .waitSeconds(1)

                                // This segment goes from extake #1 to intake #1
                                .lineToSplineHeading(new Pose2d(v.c1, v.c2, Math.toRadians(v.d1)))
                                .splineToConstantHeading(new Vector2d(v.c3, v.c4), Math.toRadians(v.d2))
                                .lineToSplineHeading(new Pose2d(v.c10, v.c4, Math.toRadians(v.d4)))
                                .waitSeconds(1)

                                // This segment goes from intake #1 to extake #2
                                .lineToSplineHeading(new Pose2d(v.c5, v.c4, Math.toRadians(v.d1)))
                                .lineToSplineHeading(new Pose2d(v.c3, v.c4, Math.toRadians(v.d1)))
                                .splineToConstantHeading(new Vector2d(v.c1, v.c6), Math.toRadians(v.d1))
                                .lineToSplineHeading(new Pose2d(v.c1, v.c7, Math.toRadians(v.d1)))
                                .waitSeconds(1)

                                // This segment goes from extake #2 to intake #2
                                .lineToSplineHeading(new Pose2d(v.c1, v.c2, Math.toRadians(v.d1)))
                                .splineToConstantHeading(new Vector2d(v.c3, v.c4), Math.toRadians(v.d2))
                                .lineToSplineHeading(new Pose2d(v.c11, v.c4, Math.toRadians(v.d4)))
                                .waitSeconds(1)

                                // This segment goes from intake #2 to extake #3
                                .lineToSplineHeading(new Pose2d(v.c5, v.c4, Math.toRadians(v.d1)))
                                .lineToSplineHeading(new Pose2d(v.c3, v.c4, Math.toRadians(v.d1)))
                                .splineToConstantHeading(new Vector2d(v.c1, v.c6), Math.toRadians(v.d1))
                                .lineToSplineHeading(new Pose2d(v.c1, v.c7, Math.toRadians(v.d1)))
                                .waitSeconds(1)

                                // This segment goes from extake #3 to intake #3
                                .lineToSplineHeading(new Pose2d(v.c1, v.c2, Math.toRadians(v.d1)))
                                .splineToConstantHeading(new Vector2d(v.c3, v.c4), Math.toRadians(v.d2))
                                .lineToSplineHeading(new Pose2d(v.c12, v.c4, Math.toRadians(v.d4)))
                                .waitSeconds(1)

                                // This segment goes from intake #3 to extake #4
                                .lineToSplineHeading(new Pose2d(v.c5, v.c4, Math.toRadians(v.d1)))
                                .lineToSplineHeading(new Pose2d(v.c3, v.c4, Math.toRadians(v.d1)))
                                .splineToConstantHeading(new Vector2d(v.c1, v.c6), Math.toRadians(v.d1))
                                .lineToSplineHeading(new Pose2d(v.c1, v.c7, Math.toRadians(v.d1)))
                                .waitSeconds(1)

                                // This segment goes from extake #4 to intake #4
                                .lineToSplineHeading(new Pose2d(v.c1, v.c2, Math.toRadians(v.d1)))
                                .splineToConstantHeading(new Vector2d(v.c3, v.c4), Math.toRadians(v.d2))
                                .lineToSplineHeading(new Pose2d(v.c13, v.c4, Math.toRadians(v.d4)))
                                .waitSeconds(1)

                                // This segment goes from intake #4 to extake #5
                                .lineToSplineHeading(new Pose2d(v.c5, v.c4, Math.toRadians(v.d1)))
                                .lineToSplineHeading(new Pose2d(v.c3, v.c4, Math.toRadians(v.d1)))
                                .splineToConstantHeading(new Vector2d(v.c1, v.c6), Math.toRadians(v.d1))
                                .lineToSplineHeading(new Pose2d(v.c1, v.c7, Math.toRadians(v.d1)))
                                .waitSeconds(1)

                                // This segment goes from extake #5 to intake #5
                                .lineToSplineHeading(new Pose2d(v.c1, v.c2, Math.toRadians(v.d1)))
                                .splineToConstantHeading(new Vector2d(v.c3, v.c4), Math.toRadians(v.d2))
                                .lineToSplineHeading(new Pose2d(v.c14, v.c4, Math.toRadians(v.d4)))
                                .waitSeconds(1)

                                // This segment goes from intake #5 to extake #6
                                .lineToSplineHeading(new Pose2d(v.c5, v.c4, Math.toRadians(v.d1)))
                                .lineToSplineHeading(new Pose2d(v.c3, v.c4, Math.toRadians(v.d1)))
                                .splineToConstantHeading(new Vector2d(v.c1, v.c6), Math.toRadians(v.d1))
                                .lineToSplineHeading(new Pose2d(v.c1, v.c7, Math.toRadians(v.d1)))
                                .waitSeconds(1)

                                .build()
                );

        myBot.setDimensions(12, 18);
        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(180.95f)
                .addEntity(myBot)
                .start();
    }
}