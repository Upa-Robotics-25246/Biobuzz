package org.firstinspires.ftc.teamcode.util.subsystems.children;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.subsystems.Shooter;

public class PollenShooter extends Shooter {

    public PollenShooter(HardwareMap hardwareMap){
        super(hardwareMap,"pollenShooter",1740,true);
    }
    public PollenShooter(HardwareMap hardwareMap,double kP,double kD,double kS, double kV, int velocity){
        super(hardwareMap,"pollenShooter",velocity,true,kP,kD,kS,kV);
    }
}
