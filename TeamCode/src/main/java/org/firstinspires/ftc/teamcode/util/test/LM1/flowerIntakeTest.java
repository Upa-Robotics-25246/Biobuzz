package org.firstinspires.ftc.teamcode.util.test.LM1;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.List;

import dev.nextftc.control.feedback.PIDController;
import dev.nextftc.control.feedforward.SimpleFeedforward;

@Config
@TeleOp
public class flowerIntakeTest extends OpMode {




    DcMotorEx intake,fl,fr,bl,br,pollen,nectar;

    public static double intakeSpeed = 0,pollenBlockerPos = 0, nectarBlockerPos = 0,flowerLPos = 0,flowerRPos = 0;

    List<LynxModule> hubs;

    Servo pollenBlocker, nectarBlocker, flowerL,flowerR;


    //i think that since its the same motor and wheel, 1 set of tuning values should work
    //TODO use flywheelPIDTuner class to tune this
    public static double kP = 0, kD = 0;
    public static double kS = 0, kV = 0;

    PIDController nectarPid = new PIDController(kP,0,kD);
    SimpleFeedforward nectarFf = new SimpleFeedforward(kS,kV,0);

    PIDController pollenPid = new PIDController(kP,0,kD);
    SimpleFeedforward pollenFf = new SimpleFeedforward(kS,kV,0);

    public static int pollenVelo = 0, nectarVelo = 0;





    @Override
    public void init() {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());


        hubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : hubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }
        fr = hardwareMap.get(DcMotorEx.class, "fr");
        fl = hardwareMap.get(DcMotorEx.class, "fl");
        bl = hardwareMap.get(DcMotorEx.class, "bl");
        br = hardwareMap.get(DcMotorEx.class, "br");

        fl.setDirection(DcMotor.Direction.REVERSE);
        bl.setDirection(DcMotor.Direction.REVERSE);

        intake = hardwareMap.get(DcMotorEx.class,"intake");


        pollenBlocker = hardwareMap.get(Servo.class,"pollenBlocker");
        nectarBlocker = hardwareMap.get(Servo.class,"nectarBlocker");


        pollen = hardwareMap.get(DcMotorEx.class,"pollenShooter");
        nectar = hardwareMap.get(DcMotorEx.class,"nectarShooter");

        flowerL = hardwareMap.get(Servo.class,"flowerL");
        flowerR = hardwareMap.get(Servo.class,"flowerR");
    }

    @Override
    public void loop() {

        double nectarError = nectarVelo - nectar.getVelocity();
        double nectarPower = nectarPid.calculate(nectarError) + nectarFf.calculate(nectar.getVelocity());


        double pollenError = pollenVelo - pollen.getVelocity();
        double pollenPower = pollenPid.calculate(pollenError) + pollenFf.calculate(pollen.getVelocity());

        for (LynxModule hub : hubs) {
            hub.clearBulkCache();
        }


        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;


        x = x * 1.1;


        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double flPower = (y + x + rx) / denominator;
        double blPower = (y - x + rx) / denominator;
        double frPower = (y - x - rx) / denominator;
        double brPower = (y + x - rx) / denominator;

        fl.setPower(flPower);
        bl.setPower(blPower);
        fr.setPower(frPower);
        br.setPower(brPower);



        intake.setPower(intakeSpeed);


        pollenBlocker.setPosition(pollenBlockerPos);
        nectarBlocker.setPosition(nectarBlockerPos);



        pollen.setPower(pollenPower);
        nectar.setPower(nectarPower);


        flowerL.setPosition(flowerLPos);
        flowerR.setPosition(flowerRPos);


        telemetry.addData("current pollen power",pollen.getPower());
        telemetry.addData("current nectar power",nectar.getPower());
        telemetry.addData("current pollen velocity",pollen.getVelocity());
        telemetry.addData("current nectar velocty",nectar.getVelocity());
        telemetry.addData("target pollen velocity",pollenVelo);
        telemetry.addData("target nectar velocity",nectarVelo);
    }
}
