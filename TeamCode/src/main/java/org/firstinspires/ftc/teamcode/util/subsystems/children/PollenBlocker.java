package org.firstinspires.ftc.teamcode.util.subsystems.children;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.subsystems.Blocker;

public class PollenBlocker extends Blocker {
    static double testedOpen = 0, testedClosed = 0; // TODO test
    public PollenBlocker(HardwareMap hardwareMap){
        super(hardwareMap,"pollenBlocker",testedOpen,testedClosed);
    }
}
