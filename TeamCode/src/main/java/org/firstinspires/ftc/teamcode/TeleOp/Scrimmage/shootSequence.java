package org.firstinspires.ftc.teamcode.TeleOp.Scrimmage;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

//@TeleOp
//@Config
public class shootSequence extends OpMode {

    //intake reverse 1/20th of a second
    // intake off
    //shooter on
    //blockers open
    //intake on
    //shooter off
    //blockers down


    //REMEMBER TO DO THE BLOCKER STUFF
    public static int intakeReverseWait = 50;
    public static int shooterSpinUpWait = 1000;
    DcMotorEx intake,nectarFly,pollenFly;
    Servo pollenBlocker,nectarBlocker;

    ElapsedTime intakeTimer = new ElapsedTime();
    ElapsedTime shootingTimer = new ElapsedTime();

    boolean shooting;

    boolean firstStep = true, nextStep = false;

    @Override
    public void init() {
        intake = hardwareMap.get(DcMotorEx.class, "intTrans");
        nectarFly = hardwareMap.get(DcMotorEx.class, "nectarFly");
        pollenFly = hardwareMap.get(DcMotorEx.class, "pollenFly");

        pollenBlocker = hardwareMap.get(Servo.class,"pollenBlocker");
        nectarBlocker = hardwareMap.get(Servo.class,"nectarBlocker");

        pollenFly.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        if(gamepad1.xWasPressed()){
            shooting = true;
            intakeTimer.reset();
            firstStep = true;
            nextStep = false;
        }

        if(shooting){
            if(intakeTimer.milliseconds() > intakeReverseWait && firstStep){
                intake.setPower(0);
                nectarFly.setPower(0.59);
                pollenFly.setPower(0.58);
                shootingTimer.reset();
                firstStep = false;
                nextStep = true;
            }
            else if(firstStep){
                intake.setPower(-1);
            }
            if(shootingTimer.milliseconds() > shooterSpinUpWait && nextStep){
                intake.setPower(1);

                shooting = false;
            }

        }
        telemetry.addData("timer1",intakeTimer.milliseconds());
        telemetry.addData("timer2",shootingTimer.milliseconds());
    }





}
