package org.firstinspires.ftc.teamcode.util.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
//@Config
//@TeleOp
public class servoBlockerTest extends OpMode {

    public static String hMap = "servo";
    Servo servo;
    public static double pos = 0;

    @Override
    public void init() {
        servo = hardwareMap.get(Servo.class,hMap);
    }

    @Override
    public void loop() {
        servo.setPosition(pos);
    }
}
