package org.firstinspires.ftc.teamcode.util.subsystems.tests;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.subsystems.Intake;

@TeleOp
@Config
public class intakeTest extends OpMode {

    public static boolean speedSet = false;
    public static double speed = 0;

    Intake intake;

    @Override
    public void init() {
        intake = new Intake(hardwareMap);
    }

    @Override
    public void loop() {

        if(gamepad2.dpadUpWasPressed()){
            intake.setState(Intake.IntakeState.FORWARD);
        }
        if(gamepad2.dpadLeftWasPressed()){
            intake.setState(Intake.IntakeState.STOPPED);
        }
        if(gamepad2.dpadDownWasPressed()){
            intake.setState(Intake.IntakeState.REVERSE);
        }

        if(speedSet){
            intake.setIntakePower(speed);
        }
    }
}
