package org.firstinspires.ftc.teamcode.util.subsystems.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.util.subsystems.FlowerIntake;

public class flowerIntakeTest extends OpMode {

    FlowerIntake flowerIntake;


    @Override
    public void init() {
        flowerIntake = new FlowerIntake(hardwareMap);
    }

    @Override
    public void loop() {
        if(gamepad2.aWasPressed()){
            flowerIntake.setState(FlowerIntake.FlowerIntakeState.UP);
        }
        if(gamepad2.bWasPressed()){
            flowerIntake.setState(FlowerIntake.FlowerIntakeState.DOWN);
        }
    }
}
