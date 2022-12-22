/*
 * Copyright (c) 2021 Sebastian Erives
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package org.firstinspires.ftc.teamcode.drive.opmode.Vision;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.drive.Constants;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class JunctionPipeline extends OpenCvPipeline {


    private Point centroid;
    private Point Test;
    public static Scalar DISPLAY_COLOR = new Scalar(210, 150, 190);
    public Scalar lower = new Scalar(0, 155, 60);

    public Scalar upper = new Scalar(30, 220, 120);
    //public static Point LEFT = new Point(50, 120);
    //public static Point RIGHT = new Point(270, 120);
    public double area;
    private double minArea = 200;

    //public static int BORDER_LEFT_X = 0;   //amount of pixels from the left side of the cam to skip
    //public static int BORDER_RIGHT_X = 0;   //amount of pixels from the right of the cam to skip
    //public static int BORDER_TOP_Y = 70;   //amount of pixels from the top of the cam to skip
    //public static int BORDER_BOTTOM_Y = 70;   //amount of pixels from the bottom of the cam to skip

//    public static int VARIANCE = 50;
    //  public static double MIN_AREA = 500;





    public Exception debug;
    public double x = -1;
    public double y = -1;
    public double xTest = 640;
    public double yTest = 450;
    public double xCenterPos = 640;
    public double xError;
    public double xErrorServo;

    public ColorSpace colorSpace = ColorSpace.HSV;

    private Mat HSVMat = new Mat();
    private Mat binaryMat = new Mat();
    private Mat maskedInputMat = new Mat();

    private Telemetry telemetry;

    enum ColorSpace {
        HSV(Imgproc.COLOR_RGB2HSV);

        public int cvtCode = 0;

        ColorSpace(int cvtCode) {
            this.cvtCode = cvtCode;
        }
    }

    public JunctionPipeline(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public Mat processFrame(Mat input) {
        try {

            Imgproc.cvtColor(input, HSVMat, colorSpace.cvtCode);
            //Imgproc.cvtColor(input, ycrcbMat, Imgproc.COLOR_BGR2GRAY);
            Imgproc.cvtColor(input, HSVMat, Imgproc.COLOR_RGB2HSV);

            Core.inRange(HSVMat, lower, upper, binaryMat);
            // Remove Noise
            Imgproc.morphologyEx(binaryMat, binaryMat, Imgproc.MORPH_OPEN, new Mat());
            Imgproc.morphologyEx(binaryMat, binaryMat, Imgproc.MORPH_CLOSE, new Mat());
            // GaussianBlur
            Imgproc.GaussianBlur(binaryMat, binaryMat, new Size(5.0, 5.0), 0.00);
            List<MatOfPoint> contours = new ArrayList<>();
            List<MatOfPoint> filteredContours = new ArrayList<>();
            Mat hierarchy = new Mat();
            Imgproc.findContours(binaryMat, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
            Imgproc.drawContours(binaryMat, contours, -1, DISPLAY_COLOR, 2);

            MatOfPoint largestContour = null;
            double maxArea = 0;
            for (MatOfPoint contour : contours) {
                double area = Imgproc.contourArea(contour);
                if (area > maxArea) {
                    maxArea = area;
                    largestContour = contour;

                }
            }

            // Find the centroid of the largest contour
            Moments moments = Imgproc.moments(largestContour);
            centroid = new Point(moments.get_m10() / moments.get_m00(), moments.get_m01() / moments.get_m00());
            Test = new Point(xTest, yTest);
// Draw the centroid on the original image
            Imgproc.drawContours(input, Arrays.asList(largestContour), -1, new Scalar(255, 0, 0), -1);
            Imgproc.circle(input, centroid, 5, new Scalar(0, 0, 255), -1);
            Imgproc.circle(input, Test, 5, new Scalar(0, 255, 0), -1);


            maskedInputMat.release();

            xError = xCenterPos - centroid.x;
            xErrorServo = xError/10 * Constants.PointsPerDegree;
            //Todo with 1280x720 junction should be around 10px wide so turret wants resolution of 128 points.
            // and turret far right is 0.05 and far left is 1.
            // About 0.0059375 points per degree on servo if rotating 160 degrees.

            Core.bitwise_and(input, input, maskedInputMat, binaryMat);

            telemetry.addData("[>]", "Change these values in tuner menu");
            telemetry.addData("[Color Space]", colorSpace.name());
            telemetry.addData("[Lower Scalar]", lower);
            telemetry.addData("[Upper Scalar]", upper);
            telemetry.addData("[Position x]", centroid.x);
            telemetry.addData("[Position y]", centroid.y);
            telemetry.addData("[x Error]", xError);
            telemetry.addData("area", maxArea);
            telemetry.update();


            return maskedInputMat;
        }
        catch (Exception e){
            debug = e;
            boolean error = true;
        }
        return binaryMat;
    }
}