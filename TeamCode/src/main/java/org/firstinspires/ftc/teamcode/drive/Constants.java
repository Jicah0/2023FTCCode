package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.dashboard.config.Config;

@Config
public class Constants {
    TeleopMain2023 Cases = new TeleopMain2023();

    //Boolean for if stomp is available to raise/lower
    public static boolean readyToStomp = true;
    //Boolean for if odometry is available to raise/lower
    public static boolean odoReady = true;
    //Boolean for weather or not turret is turning to the left
    public static boolean Left = false;

    //Velocity for the slide vertical movement
    public static int HighVelocity = 2700;
    public static int LowVelocity = 600;

    //Stomp servo values
    public static double StompDown = 0.5;
    public static double StompUp = 0;

    //Odometry servo values
    public static double OdoUp = 0.5;
    public static double OdoDown = 0;

    //Turret servo values
    public static double TurretRight = 0.05;
    public static double TurretLeft = 1;
    public static double TurretDefault = 0.39;
    public static double TurretHorizontalCycle = 0.707;
    public static double TurretStepOver = -0.01;
    public static double AutoRight = 0.125;

    //Claw servo values
    public static double ClawClosed = 0.8;
    public static double ClawOpen = 0.9;

    //Extake servo flip values
    public static double ExtakeFlipIn = 0.6;
    public static double ExtakeFlipIn2 = 0.4;
    public static double ExtakeFlipOut = 0;
    public static double ExtakeFlipOut2 = 1;
    public static double ExtakeFlipLow = 0.4;
    public static double ExtakeFlipLow2 = 0.6;
    public static double ExtakeFlipCycle = 0.075;
    public static double ExtakeFlipCycle2 = 0.925;

    public static double ExFlipThreshold = .35;
    public static int ExFlipTimerThreshold_Milliseconds = 500;

    public static double ArmTarget1 = 0;
    public static double ArmTarget2 = 1;
    public static double SetArmTarget1 = 1;
    public static double SetArmTarget2 = 0;
    public static double ArmError1 = 0;
    public static double ArmError2 = 0;
    public static double ArmStepOver = 0.025;

    public static double TurretVerticalStepOver = 0.005;

    //Extake servo slide values
    public static double SlideOut = 0;
    public static double SlideOut2 = 1;
    public static double SlideIn = 0.4;
    public static double SlideIn2 = 0.6;
    public static double SlideMid = 0.35;
    public static double SlideMid2 = 0.65;


    //Intake servo flip values
    public static double ServoIntakeFlipIntaking = 0.15;
    public static double ServoIntakeFlipExchanging = 0.9;

    //Intake slide motor values
    public static int IntakeOut = -670;
    public static int IntakeExchanging = -600;
    public static int IntakeIn = 0;

    //Intake flip motor values
    public static int IntakeFlips = 550;
    public static int IntakeFlips1 = 500;
    public static int IntakeFlipsLow = 400;
    public static int IntakeFlipsIn = 0;
    public static int IntakeNewExchange = 20;

    //Lift motor values
    public static int LiftHigh = -1000;
    public static int LiftMid = -500;
    public static int LiftLow = -200;
    public static int LiftDefault = 0;

    //Intake Sensor values
    public static double IntakeWheelSensor = 20;
    public static double IntakeSensorBlue = 100;
    public static double IntakeSensorRed = 100;

    //Intake Claw Values
    public static double IntakeClawOpen = 0.2;
    public static double IntakeClawClosed = 0.4;
    public static double IntakeClawPartial = 0.3;


    //Lift Values
    public static double liftGainP = 0.002;
    public static double liftGainP0 = 0.0001;
    public static double liftGainPUp = 0.0025;

    public static double liftError1 = 0;
    public static double liftError2 = 0;
    public static double liftPower1 = 0;
    public static double liftPower2 = 0;

    public static int LiftStep = 50;

    //Color Threshold
    public static double ColorAreaThreshHold = 300;

    //Drive speed percent
    public static double DrivePower = .75;

    //Timings
    public static int TimeOutTime = 3000;
    public static int CycleDropDelay = 600;






}
