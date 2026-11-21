package org.firstinspires.ftc.teamcode.util.test.LM1;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.List;

import dev.nextftc.control.feedback.PIDController;
import dev.nextftc.control.feedforward.SimpleFeedforward;


@TeleOp
@Config
public class shooter extends OpMode {


    DcMotorEx intake,fl,fr,bl,br,pollen;

    public static double intakeSpeed = 0,pollenBlockerPos = 0, nectarBlockerPos = 0;

    List<LynxModule> hubs;

    Servo pollenBlocker, nectarBlocker;


    //i think that since its the same motor and wheel, 1 set of tuning values should work
    private double kP = 0.001, kI = 0, kD = 0.1;
    private double kS = 0.19, kV = 0.0004;

    public static boolean tuningKS = false;

    public static double target;







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
        pollen.setDirection(DcMotorSimple.Direction.REVERSE);


    }

    @Override
    public void loop() {

        PIDController pid = new PIDController(kP,kI,kD);
        SimpleFeedforward ff = new SimpleFeedforward(kS,kV,0);


        double pollenError = target - pollen.getVelocity();
        double pollenPower = pid.calculate(pollenError) + ff.calculate(pollen.getVelocity());

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



        telemetry.addData("current pollen power",pollen.getPower());
       // telemetry.addData("current nectar power",nectar.getPower());
        telemetry.addData("current pollen velocity",pollen.getVelocity());
       // telemetry.addData("current nectar velocty",nectar.getVelocity());
        telemetry.addData("target pollen velocity",target);
       // telemetry.addData("target nectar velocity",nectarVelo);
    }
}
