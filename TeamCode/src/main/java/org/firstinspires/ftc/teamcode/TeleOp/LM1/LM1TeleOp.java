package org.firstinspires.ftc.teamcode.TeleOp.LM1;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.util.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.util.subsystems.FlowerIntake;

public class LM1TeleOp extends OpMode {

    Drivetrain dt;
    FlowerIntake flowerIntake;

    boolean flowerDown = false;

    @Override
    public void init() {
        dt = new Drivetrain(hardwareMap);
        flowerIntake = new FlowerIntake(hardwareMap);
    }

    @Override
    public void loop() {
        dt.drive(gamepad1);

        if(gamepad1.bWasPressed()){
            flowerDown = !flowerDown;
        }

        if(flowerDown){
            flowerIntake.setState(FlowerIntake.FlowerIntakeState.DOWN);
        }else{
            flowerIntake.setState(FlowerIntake.FlowerIntakeState.UP);
        }


    }
}
