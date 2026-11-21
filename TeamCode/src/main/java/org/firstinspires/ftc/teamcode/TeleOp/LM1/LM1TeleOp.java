package org.firstinspires.ftc.teamcode.TeleOp.LM1;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.subsystems.Blocker;
import org.firstinspires.ftc.teamcode.util.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.util.subsystems.FlowerIntake;
import org.firstinspires.ftc.teamcode.util.subsystems.Intake;
import org.firstinspires.ftc.teamcode.util.subsystems.children.NectarBlocker;
import org.firstinspires.ftc.teamcode.util.subsystems.children.PollenBlocker;
import org.firstinspires.ftc.teamcode.util.subsystems.children.PollenShooter;

import java.util.List;

@TeleOp
@Config
public class LM1TeleOp extends OpMode {

    //drivetrain
    Drivetrain dt;

    //flower
    FlowerIntake flowerIntake;

    boolean flowerDown = false;

    //intake

    Intake intake;

    Intake.IntakeState intakeState = Intake.IntakeState.STOPPED;

    //blockers

    NectarBlocker nectarBlocker;
    PollenBlocker pollenBlocker;

    //shooters

    PollenShooter pollenShooter;
    //NectarShooter nectar;


    boolean sequence = false; //too lazy to add this to Shooter subsystem
    boolean shooting = false;

    List<LynxModule> hubs;


    //shortcut ig
    ElapsedTime intakeTimer = new ElapsedTime();


    //blocker states

    Blocker.BlockerPosition blockerPosition = Blocker.BlockerPosition.CLOSED;

    //loop time
    double looptime;

    boolean finger = false;

    ElapsedTime looptimer = new ElapsedTime();

    public static double kP = 0.001,kI = 0, kD = 0.1;
    public static double kS = 0.04, kV = 0.00035;

    public static int target = 1740;
    @Override
    public void init() {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        dt = new Drivetrain(hardwareMap);


        //flowerIntake = new FlowerIntake(hardwareMap);


        intake = new Intake(hardwareMap);


        nectarBlocker = new NectarBlocker(hardwareMap);
        pollenBlocker = new PollenBlocker(hardwareMap);



        //nectarShooter = new NectarShooter(hardwareMap);


        hubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : hubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }


    }

    @Override
    public void  loop() {
        pollenShooter = new PollenShooter(hardwareMap,kP,kD,kS,kV,target);

        looptime = looptimer.milliseconds();
        looptimer.reset();

        for (LynxModule hub : hubs) {
            hub.clearBulkCache();
        }

        //pid error loop
        pollenShooter.shooterLoop();
        //drivetrain

        if(gamepad1.psWasPressed()){
            finger = !finger;
        }

        if(finger)
            dt.fingerDrive(gamepad1);
        else
            dt.drive(gamepad1);

        //flower
        if(gamepad1.crossWasPressed()){
            flowerDown = !flowerDown;
        }

//        if(flowerDown){
//            flowerIntake.setState(FlowerIntake.FlowerIntakeState.DOWN);
//        }else{
//            flowerIntake.setState(FlowerIntake.FlowerIntakeState.UP);
//        }

        //intake
        if(gamepad2.dpadUpWasPressed()){
            switch(intakeState){
                case STOPPED:
                    intakeState = Intake.IntakeState.FORWARD;
                    break;
                case REVERSE:
                    intakeState = Intake.IntakeState.FORWARD;
                    break;
                case FORWARD:
                    intakeState = Intake.IntakeState.STOPPED;
                    break;
            }
        }
        if(gamepad2.dpadDownWasPressed()){
            switch(intakeState){
                case STOPPED:
                    intakeState = Intake.IntakeState.REVERSE;
                    break;
                case REVERSE:
                    intakeState = Intake.IntakeState.STOPPED;
                    break;
                case FORWARD:
                    intakeState = Intake.IntakeState.REVERSE;
                    break;
            }
        }
        intake.setState(intakeState);

        //blockers

        if(gamepad2.circleWasPressed()){
            switch (blockerPosition){
                case CLOSED:
                    blockerPosition = Blocker.BlockerPosition.OPEN;
                    break;
                case OPEN:
                    blockerPosition = Blocker.BlockerPosition.CLOSED;
                    break;
            }
        }

        nectarBlocker.setState(blockerPosition);
        pollenBlocker.setState(blockerPosition);


        // shooter

//        if(gamepad2.squareWasPressed()) {
//
//            sequence = !sequence;
//            intakeState = Intake.IntakeState.STOPPED;
//            intakeTimer.reset();
//        }

        if(gamepad2.squareWasPressed()){
            shooting = !shooting;
            blockerPosition = Blocker.BlockerPosition.CLOSED;
        }

        if(shooting){
            pollenShooter.setSpeed();
        }else{
            pollenShooter.setSpeed(0.0);
            blockerPosition = Blocker.BlockerPosition.CLOSED;
        }

//        if(sequence) {
//            shooting = true;
//            if(intakeTimer.seconds() > 1.5 && intakeTimer.seconds() < 1.6){
//                blockerPosition = Blocker.BlockerPosition.OPEN;
//                intakeState = Intake.IntakeState.FORWARD;
//            }else if(intakeTimer.seconds() < 1.5){
//                blockerPosition = Blocker.BlockerPosition.CLOSED;
//            }
//        }else{
//            shooting = false;
//        }




        telemetry.addLine("\n~~~~~~~ FLYWHEEL ~~~~~~~\n");

        telemetry.addData("Flywheel Target",pollenShooter.getTarget());
        telemetry.addData("Flywheel Current",pollenShooter.getMotorVelocity());
        telemetry.addData("Looptime",looptime);

        telemetry.addLine("\n~~~~~~~~ INTAKE ~~~~~~~~\n");
        telemetry.addData("state",intake.getState());

        telemetry.addLine("\n~~~~~~~ BLOCKERS ~~~~~~~\n");
        telemetry.addData("Pollen State",pollenBlocker.getState());
        telemetry.addData("Nectar State",nectarBlocker.getState());

        telemetry.addLine("\n~~~~~~ DRIVETRAIN ~~~~~~\n");
        telemetry.addData("Finger Drive",finger ? "On" : "Off");

    }
}
