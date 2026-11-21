package org.firstinspires.ftc.teamcode.util.test.LM1;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
@TeleOp
@Config
public class shooterSetPower extends OpMode {

    DcMotorEx nectar;
    DcMotorEx pollen;

    public static double nectarSpeed = 0,pollenSpeed = 0;

    @Override
    public void init() {
        nectar = hardwareMap.get(DcMotorEx.class,"nectarShooter");
        pollen = hardwareMap.get(DcMotorEx.class,"pollenShooter");
    }

    @Override
    public void loop() {

        nectar.setPower(nectarSpeed);
        pollen.setPower(pollenSpeed);

    }
}
