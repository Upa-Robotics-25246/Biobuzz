package org.firstinspires.ftc.teamcode.util.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Blocker {

    private Servo blocker;
    private double testedOpen;
    private double testedClosed;

    public static enum BlockerPosition{
        OPEN,
        CLOSED
    }

    BlockerPosition state = BlockerPosition.CLOSED;

    public Blocker(HardwareMap hardwareMap,String name,double testedOpen,double testedClosed){
        blocker = hardwareMap.get(Servo.class,name);
        this.testedClosed = testedClosed;
        this.testedOpen = testedOpen;
        setBlockerPosition();
    }

    public void setBlockerPosition(double pos){
        blocker.setPosition(pos);
    }
    private void setBlockerPosition(){
        switch(state){
            case OPEN:
                blocker.setPosition(testedOpen);
                break;
            case CLOSED:
                blocker.setPosition(testedClosed);
                break;
        }
    }

    public void setState(BlockerPosition state){
        this.state = state;
        setBlockerPosition();
    }

    public BlockerPosition getState(){
        return state;
    }

    public Servo getBlocker(){
        return blocker;
    }
}
