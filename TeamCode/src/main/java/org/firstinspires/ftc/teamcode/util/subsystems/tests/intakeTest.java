package org.firstinspires.ftc.teamcode.util.subsystems.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.util.subsystems.Intake;

@TeleOp
public class intakeTest extends OpMode {

    public static boolean speedSet = true;

    Intake intake;




    @Override
    public void init() {
//        telemetry = new MultipleTelemetry(telemetry,FtcDashboard.getInstance().getTelemetry());
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



        telemetry.addData("Speed: ",intake.getIntake().getPower());
        telemetry.addData("state",intake.getState());
        telemetry.update();
    }
}
