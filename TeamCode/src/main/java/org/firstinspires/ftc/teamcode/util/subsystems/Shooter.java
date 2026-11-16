package org.firstinspires.ftc.teamcode.util.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import dev.nextftc.control.feedback.PIDController;
import dev.nextftc.control.feedforward.SimpleFeedforward;

public class Shooter {

    private DcMotorEx shooter;
    private int shooterVelocity;

    private int kP = 0, kI = 0, kD = 0; // TODO tune ts
    private int kS = 0, kV = 0; // TODO TUNE TS

    private double error;
    private double power;

    PIDController pid = new PIDController(kP,0,kD);
    SimpleFeedforward ff = new SimpleFeedforward(kS,kV,0);


    public Shooter(HardwareMap hardwareMap,String name, int velo){
        shooter = hardwareMap.get(DcMotorEx.class,name);
        shooterVelocity = velo;
    }

    public void shooterLoop(){
        error = shooterVelocity - shooter.getVelocity();
        power = pid.calculate(error) + ff.calculate(shooter.getVelocity());
    }

    public void setSpeed(int velocity){
        shooterVelocity = velocity;
        shooter.setPower(power);
    }
    public void setSpeed(double power){
        shooter.setPower(power);
    }
    public void setSpeed(){
        shooter.setPower(power);
    }

    public DcMotorEx getShooter(){
        return shooter;
    }

    public double getError(){
        return error;
    }
    public double getMotorPower(){
        return shooter.getPower();
    }
    public double getMotorVelocity(){
        return shooter.getVelocity();
    }

    public int getTarget(){
        return shooterVelocity;
    }
}
