package org.firstinspires.ftc.teamcode.util.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class FlowerIntake {
    private Servo left,right;

    private double leftUp = 0, rightUp = 0; //TODO tune
    private double leftDown = 0,rightDown = 0; //TODO tune


    public enum FlowerIntakeState{
        UP,
        DOWN
    }

    private FlowerIntakeState state = FlowerIntakeState.UP;
    public FlowerIntake(HardwareMap hardwareMap){
        left = hardwareMap.get(Servo.class,"flowerLeft");
        right = hardwareMap.get(Servo.class,"flowerRight");
    }



    public void setPosition(){
        switch(state){
            case UP:
                setLeft(leftUp);
                setRight(rightUp);
                break;
            case DOWN:
                setLeft(leftDown);
                setRight(rightDown);
                break;
        }
    }

    public void setLeft(double pos){
        left.setPosition(pos);
    }
    public void setRight(double pos){
        left.setPosition(pos);
    }

    public void setState(FlowerIntakeState state){
        this.state = state;
    }

    public FlowerIntakeState getState(){
        return state;
    }
    public double getLeftPos(){
        return left.getPosition();
    }
    public double getRightPos(){
        return right.getPosition();
    }

    public Servo getLeft(){
        return left;
    }

    public Servo getRight(){
        return right;
    }
}
