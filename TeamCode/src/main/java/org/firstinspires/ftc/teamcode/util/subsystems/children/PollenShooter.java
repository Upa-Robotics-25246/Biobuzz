package org.firstinspires.ftc.teamcode.util.subsystems.children;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.subsystems.Shooter;

public class PollenShooter extends Shooter {
    public PollenShooter(HardwareMap hardwareMap){
        super(hardwareMap,"pollenShooter",0); // TODO tune velo
    }
}
