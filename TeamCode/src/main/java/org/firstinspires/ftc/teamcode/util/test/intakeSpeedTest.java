package org.firstinspires.ftc.teamcode.util.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

//@TeleOp
//@Config
public class intakeSpeedTest extends OpMode {

    public static double speed = 0;
    DcMotorEx intake;

    @Override
    public void init() {
        intake = hardwareMap.get(DcMotorEx.class,"intake");
    }

    @Override
    public void loop() {
        intake.setPower(speed);
    }
}
