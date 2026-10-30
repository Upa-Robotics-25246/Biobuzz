package org.firstinspires.ftc.teamcode.util.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
@Config
public class BlockerServoTester extends OpMode {

    public static double nectarServoPos = 0;
    public static double pollenServoPos = 0;
    Servo pollenservo;
    Servo nectarservo;


    @Override
    public void init() {
        pollenservo = hardwareMap.get(Servo.class, "pollenServo");
        nectarservo = hardwareMap.get(Servo.class, "nectarServo");
    }

    @Override
    public void loop() {

        pollenservo.setPosition(pollenServoPos);
        nectarservo.setPosition(nectarServoPos);
    }
}
