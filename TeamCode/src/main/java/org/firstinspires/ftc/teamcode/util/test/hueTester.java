package org.firstinspires.ftc.teamcode.util.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Utility;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp
public class hueTester extends OpMode {

    NormalizedColorSensor sensor;
    double hue;

    @Override
    public void init() {
        sensor = hardwareMap.get(NormalizedColorSensor.class, "colorSensor");

    }

    @Override
    public void loop() {

        NormalizedRGBA colors = sensor.getNormalizedColors();
        hue = JavaUtil.colorToHue(colors.toColor());
        telemetry.addData("Hue", JavaUtil.colorToHue(colors.toColor()));
    }
}
