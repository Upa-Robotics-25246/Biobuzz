package org.firstinspires.ftc.teamcode.util.subsystems.tests;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.subsystems.children.NectarBlocker;
import org.firstinspires.ftc.teamcode.util.subsystems.children.PollenBlocker;
import org.firstinspires.ftc.teamcode.util.subsystems.Blocker;

@Config
@TeleOp
public class blockerTest extends OpMode {

    PollenBlocker pollenBlocker;
    NectarBlocker nectarBlocker;


    @Override
    public void init() {
        pollenBlocker = new PollenBlocker(hardwareMap);
        nectarBlocker = new NectarBlocker(hardwareMap);
    }

    @Override
    public void loop() {
        if(gamepad2.crossWasPressed()){
            pollenBlocker.setState(Blocker.BlockerPosition.OPEN);
            nectarBlocker.setState(Blocker.BlockerPosition.OPEN);
        }
        if(gamepad2.circleWasPressed()){
            pollenBlocker.setState(Blocker.BlockerPosition.CLOSED);
            nectarBlocker.setState(Blocker.BlockerPosition.CLOSED);
        }
    }
}
