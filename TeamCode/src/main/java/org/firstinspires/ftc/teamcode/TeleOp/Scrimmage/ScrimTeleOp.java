
package org.firstinspires.ftc.teamcode.TeleOp.Scrimmage;

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
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.List;

import dev.nextftc.control.feedback.PIDController;
import dev.nextftc.control.feedforward.SimpleFeedforward;

//@TeleOp
//@Config
public class ScrimTeleOp extends OpMode {

    // motors
    DcMotor motorTR, motorTL, motorBL, motorBR;
    IMU imu1;



    // blockers
    Servo pollenBlocker, nectarBlocker;
    final double POLLEN_OPEN = 0.925;
    final double POLLEN_CLOSED = 1;

    final double NECTAR_OPEN = 0.81;
    final double NECTAR_CLOSED = 1;


    public boolean pollenBlockersOpen = false;

    public boolean nectarBlockersOpen = false;

    DcMotorEx intTrans; //intake transfer




    //sequence
    public static int intakeReverseWait = 50;
    public static int shooterSpinUpWait = 2000;

    ElapsedTime intakeTimer = new ElapsedTime();
    ElapsedTime shootingTimer = new ElapsedTime();


    boolean firstStep = true, nextStep = false;

    boolean sequencing = false;

    //shooter stuff

    public static double kP = 0.0015,kD = 0.1,kS = 0.045,kV = 0.00034;


    DcMotorEx pollen, nectar;


    public static double nectarVelo = 1560;
    public static double pollenVelo = 1480;

    public boolean shooting = false;

    double looptime;

    int intakeState = 1;

    public static double necPower = 0.59, polPower = 0.58;
    public boolean usingPower = false;
    ElapsedTime looptimer = new ElapsedTime();

    //clear cache or smt(from ftc disky)
    List<LynxModule> hubs;

    boolean flywheelReversed = false;


    @Override
    public void init() {

        hubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : hubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }


        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());


        // Map drivetrain motors
        motorTR = hardwareMap.get(DcMotor.class, "motorTR");
        motorTL = hardwareMap.get(DcMotor.class, "motorTL");
        motorBL = hardwareMap.get(DcMotor.class, "motorBL");
        motorBR = hardwareMap.get(DcMotor.class, "motorBR");
        imu1 = hardwareMap.get(IMU.class, "imu");

        motorTL.setDirection(DcMotor.Direction.REVERSE);
        motorBL.setDirection(DcMotor.Direction.REVERSE);

        // Map servo blockers
        pollenBlocker = hardwareMap.get(Servo.class, "pollenBlocker");
        nectarBlocker = hardwareMap.get(Servo.class, "nectarBlocker");

        // Set initial blocker positions
        pollenBlocker.setPosition(POLLEN_CLOSED);
        nectarBlocker.setPosition(NECTAR_CLOSED);



        intTrans = hardwareMap.get(DcMotorEx.class, "intTrans");
        intTrans.setDirection(DcMotorSimple.Direction.FORWARD);
        pollen = hardwareMap.get(DcMotorEx.class,"pollenFly");
        pollen.setDirection(DcMotorSimple.Direction.REVERSE);
        nectar = hardwareMap.get(DcMotorEx.class,"nectarFly");

    }

    @Override
    public void loop() {




        for (LynxModule hub : hubs) {
            hub.clearBulkCache();
        }

        looptime = looptimer.milliseconds();
        looptimer.reset();



        PIDController nectarPid = new PIDController(kP,0,kD);
        SimpleFeedforward nectarFf = new SimpleFeedforward(kS,kV,0);

        PIDController pollenPid = new PIDController(kP,0,kD);
        SimpleFeedforward pollenFf = new SimpleFeedforward(kS,kV,0);

        double nectarError = nectarVelo - nectar.getVelocity();
        double nectarPower = nectarPid.calculate(nectarError) + nectarFf.calculate(nectar.getVelocity());


        double pollenError = pollenVelo - pollen.getVelocity();
        double pollenPower = pollenPid.calculate(pollenError) + pollenFf.calculate(pollen.getVelocity());

        //power toggle
        if(gamepad1.startWasPressed()){
            usingPower = !usingPower;
        }


        if(usingPower){
            nectarPower = necPower;
            pollenPower = polPower;

            if(nectar.getVelocity() <1350) nectarPower = 1;
            if(nectar.getVelocity() <1200) pollenPower = 1;
        }


        if(flywheelReversed){
            nectarPower*=-1;
            pollenPower*=-1;
        }


        // drive code
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;


        x = x * 1.1;


        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        motorTL.setPower(frontLeftPower);
        motorBL.setPower(backLeftPower);
        motorTR.setPower(frontRightPower);
        motorBR.setPower(backRightPower);

    //servo blocker code

        if (gamepad2.aWasPressed()) {
            pollenBlockersOpen = !pollenBlockersOpen; // toggle pollen blocker
            nectarBlockersOpen = !nectarBlockersOpen; // toggle nectar blocker
        }

        if (pollenBlockersOpen) {
            pollenBlocker.setPosition(POLLEN_OPEN);
        } else {
            pollenBlocker.setPosition(POLLEN_CLOSED);
        }

        if (nectarBlockersOpen) {
            nectarBlocker.setPosition(NECTAR_OPEN);
        } else {
            nectarBlocker.setPosition(NECTAR_CLOSED);
        }


    // intake + transfer code


        if(gamepad2.dpadUpWasPressed()){
            if(intakeState == 0)intakeState = 2;
            else if(intakeState == 1)intakeState = 2;
            else if(intakeState == 2)intakeState = 1;
        }

        if(gamepad2.dpadDownWasPressed()){
            if(intakeState == 2)intakeState = 0;
            else if(intakeState == 1) intakeState =0;
            else if(intakeState == 0) intakeState =1;
        }

        if(intakeState == 0)intTrans.setPower(-1);
        else if(intakeState == 1)intTrans.setPower(0);
        else if(intakeState == 2)intTrans.setPower(1);

    // shooting code
        if(gamepad2.xWasPressed()){
            shooting = !shooting;
            flywheelReversed = false;

        }

        if(gamepad2.yWasPressed()){
            shooting = !shooting;
            flywheelReversed = true;
        }

    //sequence
        if(gamepad2.rightBumperWasPressed()){
            sequencing = true;
            intakeTimer.reset();
            firstStep = true;
            nextStep = false;
            flywheelReversed = false;
        }

        if(sequencing){
            if(intakeTimer.milliseconds() > intakeReverseWait && firstStep){
                intakeState = 1;
                shooting = true;
                nectarBlockersOpen = true;
                pollenBlockersOpen = true;
                shootingTimer.reset();
                firstStep = false;
                nextStep = true;
            }
            else if(firstStep){
                intakeState = 0;
            }
            if(shootingTimer.milliseconds() > shooterSpinUpWait && nextStep){
                intakeState = 2;

                sequencing = false;
            }

        }
        //reset for sequence
        if(gamepad2.leftBumperWasPressed()){
            shooting = false;
            pollenBlockersOpen = false;
            nectarBlockersOpen = false;
            intakeState=1;
        }

        //shoot toggle
        if(shooting){
            nectar.setPower(nectarPower);
            pollen.setPower(pollenPower);
        }
        else{
            nectar.setPower(0);
            pollen.setPower(0);
        }


//
        if(gamepad1.dpadUpWasPressed()){
            nectarVelo += 20;
        }
        if(gamepad1.dpadDownWasPressed()){
            nectarVelo -= 20;
        }
        if(gamepad1.dpadRightWasPressed()){
            pollenVelo += 20;
        }
        if(gamepad1.dpadLeftWasPressed()){
            pollenVelo -= 20;
        }

        telemetry.addData("intake state",intakeState);
        telemetry.addData("pollen open?",pollenBlockersOpen);
        telemetry.addData("nectar open?",nectarBlockersOpen);
        telemetry.addData("Nectar power",nectar.getPower());
        telemetry.addData("Pollen power",pollen.getPower());
        telemetry.addData("Nectar Velocity",nectar.getVelocity());
        telemetry.addData("Pollen velocity",pollen.getVelocity());
        telemetry.addData("pollen target",pollenVelo);
        telemetry.addData("nectar target",nectarVelo);
        telemetry.addData("looptime",looptime);

    }
}
