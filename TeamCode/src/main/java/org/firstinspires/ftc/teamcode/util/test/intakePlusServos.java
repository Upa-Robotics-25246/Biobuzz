package org.firstinspires.ftc.teamcode.util.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

//@TeleOp
public class intakePlusServos extends OpMode {

    CRServo lServo,rServo;
    DcMotorEx intake;

    DcMotorEx fr,fl,br,bl;


    @Override
    public void init() {
        lServo = hardwareMap.get(CRServo.class,"left servo");
        rServo = hardwareMap.get(CRServo.class,"right servo");
        intake = hardwareMap.get(DcMotorEx.class,"intake");
        fl = hardwareMap.get(DcMotorEx.class,"fl");
        fr = hardwareMap.get(DcMotorEx.class,"fr");
        br = hardwareMap.get(DcMotorEx.class,"br");
        bl = hardwareMap.get(DcMotorEx.class,"bl");
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {

        if(gamepad1.dpadUpWasPressed() && intake.getPower() <= 1){

            intake.setPower(intake.getPower() + 0.1);

        }
        if(gamepad1.dpadDownWasPressed() && intake.getPower() >= -1){

            intake.setPower(intake.getPower() - 0.1);

        }
        if(gamepad1.yWasPressed() && lServo.getPower() <= 1){

            lServo.setPower(lServo.getPower() + 0.1);

        }
        if(gamepad1.aWasPressed() && lServo.getPower() >= -1){

            lServo.setPower(lServo.getPower() - 0.1);

        }
        if(gamepad1.bWasPressed() && rServo.getPower() <= 1){

            rServo.setPower(rServo.getPower() + 0.1);

        }
        if(gamepad1.xWasPressed() && rServo.getPower() >= -1){

            rServo.setPower(rServo.getPower() - 0.1);

        }


        telemetry.addData("Intake Power", intake.getPower());
        telemetry.addData("Right Servo Power",rServo.getPower());
        telemetry.addData("Left Servo Power",lServo.getPower());


        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x * 1.1;
        double rx = gamepad1.right_stick_x;


        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double flPower = (y + x + rx) / denominator;
        double blPower = (y - x + rx) / denominator;
        double frPower = (y - x - rx) / denominator;
        double brPower = (y + x - rx) / denominator;

        fl.setPower(flPower);
        bl.setPower(blPower);
        fr.setPower(frPower);
        fl.setPower(brPower);

    }
}
