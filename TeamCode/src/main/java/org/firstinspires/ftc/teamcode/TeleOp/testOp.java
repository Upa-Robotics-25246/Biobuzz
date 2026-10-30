package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.feedback.PIDController;
import dev.nextftc.control.feedforward.SimpleFFCoefficients;
import dev.nextftc.control.feedforward.SimpleFeedforward;


@TeleOp
@Config
public class testOp extends OpMode {

    DcMotorEx fl,bl,fr,br;

    DcMotorEx nectarFly,pollenFly;

    Servo nectarBlocker, pollenBlocker;

    DcMotorEx intTrans;

    PIDCoefficients
            nectarPIDCoeffs = new PIDCoefficients(0,0,0),
            pollenPIDCoeffs = new PIDCoefficients(0,0,0); // TODO tune these

    SimpleFFCoefficients
            pollenFFCoeffs = new SimpleFFCoefficients(0,0),
            nectarFFCoeffs = new SimpleFFCoefficients(0,0);




    PIDController nectarPID = new PIDController(nectarPIDCoeffs), pollenPID = new PIDController(pollenPIDCoeffs);
    SimpleFeedforward nectarFF = new SimpleFeedforward(nectarFFCoeffs), pollenFF = new SimpleFeedforward(nectarFFCoeffs);

    public static double intTransSpeed = 0, pollenTarget = 0, nectarTarget = 0;

    public static boolean nectarClosed = true, pollenClosed = true,flywheelPower = false;

    final double POLLEN_OPEN = 0.6;
    final double POLLEN_CLOSED = 0.75;

    final double NECTAR_OPEN = 0.7;
    final double NECTAR_CLOSED = 0.85;

    public static double nectarPower = 0, pollenPower = 0;

    @Override
    public void init() {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        intTrans = hardwareMap.get(DcMotorEx.class,"intTrans");

        nectarFly = hardwareMap.get(DcMotorEx.class,"nectarFly");
        pollenFly = hardwareMap.get(DcMotorEx.class,"pollenFly");

        nectarBlocker = hardwareMap.get(Servo.class, "nectarBlocker");
        pollenBlocker = hardwareMap.get(Servo.class, "pollenBlocker");

        nectarBlocker.setPosition(NECTAR_CLOSED);
        pollenBlocker.setPosition(POLLEN_CLOSED);


        fr = hardwareMap.get(DcMotorEx.class, "motorTR");
        fl = hardwareMap.get(DcMotorEx.class, "motorTL");
        bl = hardwareMap.get(DcMotorEx.class, "motorBL");
        br = hardwareMap.get(DcMotorEx.class, "motorBR");
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {

        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x * 1.1;
        double rx = gamepad1.right_stick_x;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        fl.setPower(frontLeftPower);
        bl.setPower(backLeftPower);
        fr.setPower(frontRightPower);
        br.setPower(backRightPower);


        //intTrans
        if(gamepad2.dpadDownWasPressed() && intTransSpeed >= -1){
            intTransSpeed -= 0.1;
        }
        if(gamepad2.dpadUpWasPressed() && intTransSpeed <= 1){
            intTransSpeed += 0.1;
        }

        intTrans.setPower(intTransSpeed);


        //nectar blocker
        if(gamepad2.bWasPressed()){
            nectarClosed = !nectarClosed;
        }

        if(nectarClosed){
            nectarBlocker.setPosition(NECTAR_CLOSED);
        }else{
            nectarBlocker.setPosition(NECTAR_OPEN);
        }

        //pollen blocker

        if(gamepad2.aWasPressed()){
            pollenClosed = !pollenClosed;
        }

        if(pollenClosed){
            pollenBlocker.setPosition(POLLEN_CLOSED);
        }else{
            pollenBlocker.setPosition(POLLEN_OPEN);
        }


        //flywheels

//        if(gamepad1.dpadUpWasPressed()){
//            nectarTarget += 100;
//        }
//        if(gamepad1.dpadDownWasPressed()) {
//            nectarTarget -= 100;
//        }
//        if(gamepad1.dpadRightWasPressed()){
//            pollenTarget += 100;
//        }
//        if(gamepad1.dpadLeftWasPressed()) {
//            pollenTarget -= 100;
//        }

        /*if(flywheelPower){ // power is only set by FTCDASH
            nectarFly.setPower(nectarPower);
            pollenFly.setPower(pollenPower);
        }else{
            double nectarError = nectarTarget - nectarFly.getVelocity();
            double nectarOut = nectarPID.calculate(nectarError) + nectarFF.calculate(nectarTarget);

            double pollenError = pollenTarget - pollenFly.getVelocity();
            double pollenOut = pollenPID.calculate(pollenError) + pollenFF.calculate(pollenTarget);

            nectarFly.setPower(nectarOut);
            pollenFly.setPower(pollenOut);

        }*/

        if(gamepad1.dpadUpWasPressed() && pollenPower <=1){
            pollenPower += 0.1;
        }
        if(gamepad1.dpadDownWasPressed() && pollenPower >=-1){
            pollenPower -= 0.1;
        }
        if(gamepad1.dpadRightWasPressed() && nectarPower <=1){
            nectarPower += 0.1;
        }
        if(gamepad1.dpadLeftWasPressed() && nectarPower >=-1){
            nectarPower -= 0.1;
        }

        //velo for nectar in -
        nectarFly.setPower(nectarPower);
        pollenFly.setPower(pollenPower);
        telemetry.addData("nectarPower",nectarPower);
        telemetry.addData("pollenPower",pollenPower);
        telemetry.addData("nectar Speed",nectarFly.getVelocity());
        telemetry.addData("pollen Speed",pollenFly.getVelocity());
        telemetry.addData("intTransPower",intTransSpeed);

    }
}
