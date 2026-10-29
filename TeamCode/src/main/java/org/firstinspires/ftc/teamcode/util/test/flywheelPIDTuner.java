package org.firstinspires.ftc.teamcode.util.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import dev.nextftc.control.feedback.PIDController;
import dev.nextftc.control.feedforward.SimpleFeedforward;

@TeleOp
@Config
public class flywheelPIDTuner extends OpMode {

    public static double kP = 0,kI = 0, kD = 0;
    public static double kS = 0.037,kV = 0;

    public static boolean tuningKS = false,reversed;

    public static double target;




    DcMotorEx flywheel;
    @Override
    public void init() {
        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        if(reversed) flywheel.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    @Override
    public void loop() {

        PIDController pid = new PIDController(kP,kI,kD);
        SimpleFeedforward ff = new SimpleFeedforward(kS,kV,0);



        if(tuningKS){
            flywheel.setPower(kS);
        }
        else {
            double error = target - flywheel.getVelocity();

            double power = pid.calculate(error) + ff.calculate(target);


            flywheel.setPower(power);

            telemetry.addData("Target:", target);
            telemetry.addData("Current:", flywheel.getVelocity());
            telemetry.addData("error:", error);
            telemetry.update();
        }



    }
}
