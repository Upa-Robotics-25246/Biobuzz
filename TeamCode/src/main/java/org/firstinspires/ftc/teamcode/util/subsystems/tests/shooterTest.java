package org.firstinspires.ftc.teamcode.util.subsystems.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.subsystems.children.NectarShooter;
import org.firstinspires.ftc.teamcode.util.subsystems.children.PollenShooter;

import java.util.List;

@TeleOp
@Config
public class shooterTest extends OpMode {

    PollenShooter pollen;
    NectarShooter nectar;


    List<LynxModule> hubs;


    @Override
    public void init() {

        telemetry = new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(),telemetry);

        hubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : hubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        for (LynxModule hub : hubs) {
            hub.clearBulkCache();
        }


        pollen = new PollenShooter(hardwareMap);
        nectar = new NectarShooter(hardwareMap);
    }

    @Override
    public void loop() {

        pollen.shooterLoop();
        nectar.shooterLoop();


        if(gamepad2.dpadUpWasPressed()){
            pollen.setSpeed();//sets to default velo
            nectar.setSpeed();
        } else{
            pollen.setSpeed(0.0);//power to 0 rather than velocity cus double
            nectar.setSpeed(0.0);
        }

        telemetry.addData("pollen target",pollen.getTarget());
        telemetry.addData("nectar target",nectar.getTarget());
        telemetry.addData("pollen current velocity",pollen.getMotorVelocity());
        telemetry.addData("nectar current velocity",nectar.getMotorVelocity());
    }
}
