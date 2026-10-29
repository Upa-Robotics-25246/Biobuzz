package org.firstinspires.ftc.teamcode.Auton;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.ivy.Scheduler.schedule;
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
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.util.pedro.Constants;


@Autonomous
public class shootPreloadsAndPark extends OpMode {

    Follower follower;

    PoseFactory p = PoseFactory.degrees();

    Pose start, cp1,end;


    public static boolean red = false, far = false;

    public Path path1;

    private Command auton;

    public void shoot(boolean stopped){

        //code :) needa tune and test path first

    }

    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();
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

        if(far) {
            heading = 90;

        }


        if(red){
            if(far){
                start = p.of(141.5 - 81, 141.5 - 130, heading);
                cp1 = p.of(141.5 - 136, 141.5 - 132, heading);
                end = p.of(141.5 - 135, 47, heading);
            }else{
                start = p.of(141.5 - 81, 130, heading);
                cp1 = p.of(141.5 - 136, 132, heading);
                end = p.of(141.5 - 135, 47, heading);
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


        path1 = curve(start,cp1,end).constant(heading);
        follower.setPose(start);
        follower.update();


        telemetry.addLine("Press A to toggle alliance color.");
        telemetry.addLine("Press B to toggle side. Side thats starts tipped upward is \"Close\"");
        telemetry.addLine("\n\n\n\n");
        telemetry.addData("Current Alliance Color", red ?  "Red" : "Blue");
        telemetry.addData("Current Side", far ?  "Far" : "Close");
    }

    public void start(){
        if(far){
            auton = sequential(
                follow(follower,path1)
            );
        }
        else{
            auton = sequential(
                instant(()->shoot(false)),
                waitMs(2000),
                instant(()->shoot(true)),
                follow(follower,path1)
            );
        }

        schedule(auton);
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
