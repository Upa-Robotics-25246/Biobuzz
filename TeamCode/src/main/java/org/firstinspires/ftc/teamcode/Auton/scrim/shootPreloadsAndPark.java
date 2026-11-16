package org.firstinspires.ftc.teamcode.Auton.scrim;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.lib.PathOptions;
import org.firstinspires.ftc.teamcode.util.pedro.Constants;

import java.util.List;

import dev.nextftc.control.feedback.PIDController;
import dev.nextftc.control.feedforward.SimpleFeedforward;


@Autonomous
public class shootPreloadsAndPark extends OpMode {

    Follower follower;

    PoseFactory p = PoseFactory.degrees();

    Pose start, cp1,end;


    DcMotorEx intTrans;


    public static boolean red = false, far = false;

    ElapsedTime shootTimer = new ElapsedTime();
    public Path path1() {
        return curve(PathOptions.reflect(start,cp1,end)).constant(start);
    }





    public Command autonFar(){
        return follow(follower,path1());
    }

    public Command autonClose(){
        return sequential(
                infinite(()->shoot(false)).until(()->shootTimer.seconds() > 1),
                instant(()-> intTrans.setPower(1)),
                waitMs(1000),
                instant(()->intTrans.setPower(0)),
                instant(()->shoot(true)),
                follow(follower,path1())
        );
    }


    public static double kP = 0.0015,kD = 0.1,kS = 0.045,kV = 0.00034;
    DcMotorEx pollen, nectar;
    PIDController nectarPid = new PIDController(kP,0,kD);
    SimpleFeedforward nectarFf = new SimpleFeedforward(kS,kV,0);

    PIDController pollenPid = new PIDController(kP,0,kD);
    SimpleFeedforward pollenFf = new SimpleFeedforward(kS,kV,0);


    public static double nectarVelo = 1560;
    public static double pollenVelo = 500;

    public void shoot(boolean stopped){

        if(stopped){
            pollen.setPower(0);
            nectar.setPower(0);
        }else{

            double nectarError = nectarVelo - nectar.getVelocity();
            double nectarPower = nectarPid.calculate(nectarError) + nectarFf.calculate(nectar.getVelocity());
            double pollenError = pollenVelo - pollen.getVelocity();
            double pollenPower = pollenPid.calculate(pollenError) + pollenFf.calculate(pollen.getVelocity());

            nectar.setPower(nectarPower);
            pollen.setPower(pollenPower);
        }

    }




    List<LynxModule> hubs;


    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.update();
        hubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : hubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }
        pollen = hardwareMap.get(DcMotorEx.class,"pollenFly");
        pollen.setDirection(DcMotorSimple.Direction.REVERSE);
        nectar = hardwareMap.get(DcMotorEx.class,"nectarFly");


        intTrans = hardwareMap.get(DcMotorEx.class, "intTrans");
        intTrans.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void init_loop(){
        Scheduler.reset();
        if(gamepad1.aWasPressed()){
            red = !red;
        }
        if(gamepad1.bWasPressed()){
            far = !far;
        }

        int heading = 270;
        if(red){
            if(!far) heading = 90; // if close
        }
        else{
            if(far) heading = 90;
        }



        if(red){
            if(far){
                start = p.of(141.5 - 81, 130, heading);
                cp1 = p.of(141.5 - 136, 132, heading);
                end = p.of(141.5 - 135, 141.5 - 47, heading);
            }else{
                start = p.of(141.5 - 81, 141.5 - 130, heading);
                cp1 = p.of(141.5 - 136, 141.5 - 132, heading);
                end = p.of(141.5 - 135, 141.5 - 47, heading);
            }
        }else {
            if(far){
                start = p.of(81, 141.5 - 130, heading);
                cp1 = p.of(136, 141.5 - 132, heading);
                end = p.of(135, 47, heading);
            }else {
                start = p.of( 81, 130, heading);
                cp1 = p.of(136, 132, heading);
                end = p.of(135, 47, heading);
            }
        }

        follower.setPose(start);
        follower.update();


        telemetry.addLine("Press A to toggle alliance color.");
        telemetry.addLine("Press B to toggle side. Side thats starts tipped upward is \"Close\"");
        telemetry.addLine("\n\n\n\n");
        telemetry.addData("Current Alliance Color", red ?  "Red" : "Blue");
        telemetry.addData("Current Side", far ?  "Far" : "Close");
    }

    public void start(){

        if(far) schedule(autonFar());
        else schedule(autonClose());

        shootTimer.reset();
    }
    @Override
    public void loop() {

        follower.update();
        Scheduler.execute();


        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));

    }
}
