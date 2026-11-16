package org.firstinspires.ftc.teamcode.util.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {

    public enum IntakeState{
        REVERSE,
        STOPPED,
        FORWARD
    }

    private double testedSpeed = 0; // TODO change after testing

    private DcMotorEx intake;

    private IntakeState state = IntakeState.STOPPED;
    public Intake(HardwareMap hardwareMap){
        intake = hardwareMap.get(DcMotorEx.class,"intake");
        setIntakePower();
    }

    public void setIntakePower(double speed){
        intake.setPower(speed);
    }
    private void setIntakePower(){
        switch(state){
            case REVERSE:
                setIntakePower(-testedSpeed);
                break;
            case STOPPED:
                setIntakePower(0);
                break;
            case FORWARD:
                setIntakePower(testedSpeed);
                break;
        }
    }

    public void setState(IntakeState state){
        this.state = state;
        setIntakePower();
    }

    public IntakeState getState(){
        return state;
    }
    public DcMotorEx getIntake(){
        return intake;
    }
}
