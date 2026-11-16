package org.firstinspires.ftc.teamcode.util.test;

import android.graphics.Color;
import android.util.Size;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.SortOrder;


import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.Circle;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ColorSpace;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import java.util.List;

import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.feedback.PIDController;

//@Config
//@TeleOp
public class anglePIDTuner extends OpMode {






    public static double kP = 0;
    public static double kI = 0;
    public static double kD = 0;
    PIDCoefficients anglePIDcoeffs = new PIDCoefficients(kP,kI,kD); // needs tuning
    PIDController anglePID = new PIDController(anglePIDcoeffs);

    ColorBlobLocatorProcessor colorLocator;

    VisionPortal portal;


    DcMotorEx fl,fr,br,bl;

    int resX = 320;
    int resY = 240;

    @Override
    public void init() {
        colorLocator =  new ColorBlobLocatorProcessor.Builder()
                .setTargetColorRange(new ColorRange(
                        ColorSpace.HSV,new Scalar(52,70,70),
                        new Scalar(57,60,95)
                ))
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
                .setCameraResolution(new Size(resX, resY))
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


        //using largest contour only for tuning
        ColorBlobLocatorProcessor.Blob blob = blobs.get(0);
        Circle circle = blob.getCircle();

        double x = circle.getCenter().x;


        /*
        |------------------------------------------------------|
        |  (0,0)      --------------------------------->       |
        |              x increases                             |
        |     |                                                |
        |     |                                                |
        |     |                 resX/2                         |
        |     |                                                |
        |     |                                                |
        |     |  y increases                                   |
        |     ↓                                                |
        |                                                      |
        |------------------------------------------------------/
        */



        int centerX = resX / 2;

        double xDist = x - centerX; // pid error

        double output = anglePID.calculate(xDist);

        fl.setPower(output);
        bl.setPower(output);
        fr.setPower(-output);
        br.setPower(-output);



    }
}
