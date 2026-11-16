package org.firstinspires.ftc.teamcode.util.subsystems.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.subsystems.Drivetrain;

@TeleOp
public class drivetrainTest extends OpMode {
    Drivetrain dt;

    @Override
    public void init() {
        dt = new Drivetrain(hardwareMap);
    }

    @Override
    public void loop() {
        dt.drive(gamepad1);
    }
}
