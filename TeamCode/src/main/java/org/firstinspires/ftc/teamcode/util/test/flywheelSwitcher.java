package org.firstinspires.ftc.teamcode.util.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
@TeleOp
public class flywheelSwitcher extends OpMode {


    double POLLENPOS = 0; // adjust with testing
    double NECTARPOS = 0; // adjust with testing

    //color sensor for detection color
    int POLLENHUE = 0; // testing...
    int BLUENECTARHUE = 0; // testing...
    int REDNECTARHUE = 0; //testing T-T

    int ROBOTHUE = 0; // need bot done
    NormalizedColorSensor sensor;
    double hue;
    double prevHue = 0;


    //TODO give the hues an error threshold cus it wont be exact all the time


    Servo flywheelPivot;


    @Override
    public void init() {
        sensor = hardwareMap.get(NormalizedColorSensor.class, "colorSensor");
        flywheelPivot = hardwareMap.get(Servo.class,"flywheelPivot");
    }

    @Override
    public void loop() {
        NormalizedRGBA colors = sensor.getNormalizedColors();
        hue = JavaUtil.colorToHue(colors.toColor());

        //shooting button
        if(gamepad1.aWasPressed()){

            // run transfer & flywheel
            //color sensor code

            //if the color hasn't changed or if the color is the robot background that the sensor can see
            if(hue != prevHue && hue != ROBOTHUE){
                // if it is yellow, do the pollen color
                if(hue == POLLENHUE) {
                    //flywheel pivot shooting pollen
                    flywheelPivot.setPosition(POLLENPOS);
                //is the hue of the thing blue or red for pollen?
                }else if(hue == BLUENECTARHUE || hue == REDNECTARHUE){
                    //set flywheel to nectar height
                    flywheelPivot.setPosition(NECTARPOS);
                }
            }


            prevHue = hue;
        }
        // if it is just the robot background, pollen position, might change later
        if(hue == ROBOTHUE){
            flywheelPivot.setPosition(POLLENPOS);
        }


    }
}
