package org.firstinspires.ftc.teamcode.util.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import dev.nextftc.control.feedback.PIDController;
import dev.nextftc.control.feedforward.SimpleFeedforward;

public class Shooter {

    private DcMotorEx shooter;
    private int shooterVelocity;

    private double kP = 0.001,kI = 0, kD = 0.1;
    private double kS = 0.04, kV = 0.00035;

    private double error;
    private double power;






    public Shooter(HardwareMap hardwareMap,String name, int velo,boolean reversed){
        shooter = hardwareMap.get(DcMotorEx.class,name);
        shooterVelocity = velo;

        if(reversed) shooter.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    public Shooter(HardwareMap hardwareMap,String name, int velo,boolean reversed,double kP, double kD, double kS, double kV){
        shooter = hardwareMap.get(DcMotorEx.class,name);
        shooterVelocity = velo;

        if(reversed) shooter.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void shooterLoop(){
        PIDController pid = new PIDController(kP,kI,kD);
        SimpleFeedforward ff = new SimpleFeedforward(kS,kV,0);
        error = shooterVelocity - shooter.getVelocity();
        power = pid.calculate(error) + ff.calculate(shooter.getVelocity());

    }


    public void setSpeed(double power){

        shooter.setPower(power);

    }
    public void setSpeed(){
        shooter.setPower(this.power);
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
