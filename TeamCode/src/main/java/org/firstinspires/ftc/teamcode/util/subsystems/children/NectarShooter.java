package org.firstinspires.ftc.teamcode.util.subsystems.children;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.subsystems.Shooter;

public class NectarShooter extends Shooter {

    public NectarShooter(HardwareMap hardwareMap){
        super(hardwareMap,"nectarShooter",0,false); // TODO tune velo
    }

}
