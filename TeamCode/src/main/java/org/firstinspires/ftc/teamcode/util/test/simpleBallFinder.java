package org.firstinspires.ftc.teamcode.util.test;

import android.graphics.Color;
import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.SortOrder;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.Circle;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.opencv.core.Point;

import java.util.List;

import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.feedback.PIDController;

public class simpleBallFinder extends OpMode {

    PIDCoefficients anglePIDcoeffs = new PIDCoefficients(0,0,0); // needs tuning
    PIDController anglePID;
    PIDCoefficients drivePIDcoeffs = new PIDCoefficients(0,0,0); // needs tuning
    PIDController drivePID;

    boolean angling = false;
    boolean driving = false;

    double dist = 0;

    ColorBlobLocatorProcessor colorLocator;

    VisionPortal portal;


    DcMotorEx fl,fr,br,bl;

    @Override
    public void init() {
        colorLocator =  new ColorBlobLocatorProcessor.Builder()
                .setTargetColorRange(ColorRange.YELLOW)
                .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)
                .setDrawContours(true)
                .setBoxFitColor(0)
                .setCircleFitColor(Color.rgb(255, 255, 0))
                .setBlurSize(5)
                .setDilateSize(15)
                .setErodeSize(15)
                .setMorphOperationType(ColorBlobLocatorProcessor.MorphOperationType.CLOSING)
                .build();
        portal = new VisionPortal.Builder()
                .addProcessor(colorLocator)
                .setCameraResolution(new Size(320, 240))
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .build();

        fl = hardwareMap.get(DcMotorEx.class,"fl");
        fr = hardwareMap.get(DcMotorEx.class,"fr");
        br = hardwareMap.get(DcMotorEx.class,"br");
        bl = hardwareMap.get(DcMotorEx.class,"bl");
        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        //filters
        List<ColorBlobLocatorProcessor.Blob> blobs = colorLocator.getBlobs();



        ColorBlobLocatorProcessor.Util.filterByCriteria(
                ColorBlobLocatorProcessor.BlobCriteria.BY_CONTOUR_AREA,
                100, 20000, blobs);

        ColorBlobLocatorProcessor.Util.filterByCriteria(
                ColorBlobLocatorProcessor.BlobCriteria.BY_CIRCULARITY,
                0.6, 1, blobs);

        ColorBlobLocatorProcessor.Util.sortByCriteria(
                ColorBlobLocatorProcessor.BlobCriteria.BY_CONTOUR_AREA,
                SortOrder.DESCENDING, blobs);


        //in actual auton we will prob have a state machine or a boolean value to control this
        if(gamepad1.aWasPressed()){
            angling = true;
        }


        if(angling){
            // the problem with this is that the larget blob could change,
            // leading to fluctuation with the pid, idk what else to do tho
            ColorBlobLocatorProcessor.Blob blob = blobs.get(0);
            Circle circle = blob.getCircle();

            double x = circle.getX();


            /*
            |------------------------------------------------------|
            |  (0,0)      --------------------------------->       |
            |              x increases                             |
            |     |                                                |
            |     |                                                |
            |     |         circle.getCenter()                     |
            |     |                                                |
            |     |                                                |
            |     |  y increases                                   |
            |     ↓                                                |
            |                                                      |
            |------------------------------------------------------/
            */

            Point center = circle.getCenter();

            double centerX = center.x;

            double xDist = x - centerX; // pid error

            double output = anglePID.calculate(xDist);

            fl.setPower(output);
            bl.setPower(output);
            fr.setPower(-output);
            br.setPower(-output);


            if(Math.abs(output) <= 5){
                angling = false;
                driving = true;

                dist = 0; // TODO CHANGE CHANGE THIS WHEN YOU ACTUALLY GET REGRESSION FOR DIST
            }
        }



        if(driving){
            double driveOutput = drivePID.calculate(dist);

            fr.setPower(driveOutput);//if it goes backwards here im gonna cry
            fl.setPower(driveOutput);
            br.setPower(driveOutput);
            bl.setPower(driveOutput);
        }
    }
}
