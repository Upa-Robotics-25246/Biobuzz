package org.firstinspires.ftc.teamcode.Auton.lm1;

import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.math.Vector2D;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.lib.PathOptions;
import org.firstinspires.ftc.teamcode.util.pedro.Constants;
import org.firstinspires.ftc.teamcode.util.subsystems.Blocker;
import org.firstinspires.ftc.teamcode.util.subsystems.children.NectarShooter;
import org.firstinspires.ftc.teamcode.util.subsystems.children.PollenBlocker;
import org.firstinspires.ftc.teamcode.util.subsystems.children.PollenShooter;

import java.util.List;

@Autonomous(group = "Autonomous")
public class TwoTipClose extends OpMode {

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(62, 11, 90);
    private final Pose point1 = poseFactory.of(10, 5, -174);
    private final Pose path1Start = poseFactory.of(10, 5, -174);
    private final Pose path1 = poseFactory.of(48, 125, 90);
    private final Pose point3 = poseFactory.of(48, 133, 90);
    private final Pose point4 = poseFactory.of(60, 121, 270);
    private final Pose point5 = poseFactory.of(7, 121, 180);

    PollenShooter pollen;
    PollenBlocker blocker;

    // Autonomous routine
    boolean red = false;

    ElapsedTime shooterTimer = new ElapsedTime();


    List<LynxModule> hubs;

    public Command autoRoutine(boolean red) {

        return sequential(
                instant(()->shooterTimer.reset()),
                infinite(()->shoot(false)).until(()->shooterTimer.seconds() >1.5),
                instant(()->shoot(true)),
                follow(follower, path1(red)),
                follow(follower, path1_2(red)),
                follow(follower, path3(red)),
                follow(follower, path4(red)),
                follow(follower, path5(red))
        );
    }


    public void shoot(boolean stopped){

        if(stopped){
            pollen.setSpeed(0.0);
            blocker.setState(Blocker.BlockerPosition.CLOSED);
        }else{
            blocker.setState(Blocker.BlockerPosition.OPEN);
            pollen.shooterLoop();
            pollen.setSpeed();
        }

    }


    @Override
    public void init() {
        hubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : hubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();
        pollen = new PollenShooter(hardwareMap);
        blocker = new PollenBlocker(hardwareMap);
        blocker.setState(Blocker.BlockerPosition.OPEN);

    }




    @Override
    public void init_loop(){
        if(gamepad1.aWasPressed()) {
            red = !red;
        }
    }

    public void start(){
        schedule(autoRoutine(red));
        shooterTimer.reset();
    }

    @Override
    public void loop() {
        for (LynxModule hub : hubs) {
            hub.clearBulkCache();
        }
        follower.update();
        Scheduler.execute();

        telemetry.addData("x", follower.pose().x());
        telemetry.addData("y", follower.pose().y());
        telemetry.addData("heading", follower.pose().heading());

        telemetry.update();
    }


    public Path path1(boolean red) {
        if(red)
            return Paths.line(start, point1).constant(point1);
        else{
            Vector2D[] reflected = PathOptions.reflect(start.toVector2D(),point1.toVector2D());
            return Paths.line(reflected[0],reflected[1]).constant(point1);
        }
    }

    public Path path1_2(boolean red) {
        if(red)
            return Paths.line(path1Start, path1).linear(path1Start, path1);
        else{
            Vector2D[] reflected = PathOptions.reflect(path1Start.toVector2D(),path1.toVector2D());
            return Paths.line(reflected[0],reflected[1]).linear(path1Start, path1);
        }

    }

    public Path path3(boolean red) {
        if(red)
            return Paths.line(path1, point3).constant(point3);
        else{
            Vector2D[] reflected = PathOptions.reflect(path1.toVector2D(),point3.toVector2D());
            return Paths.line(reflected[0],reflected[1]).constant(point3);
        }
    }

    public Path path4(boolean red) {
        if(red)
            return Paths.line(point3, point4).linear(point3,point4);
        else{
            Vector2D[] reflected = PathOptions.reflect(point3.toVector2D(),point4.toVector2D());
            return Paths.line(reflected[0],reflected[1]).constant(point4);
        }
    }

    public Path path5(boolean red) {
        if(red)
            return Paths.line(point4, point5).constant(point5);
        else{
            Vector2D[] reflected = PathOptions.reflect(point4.toVector2D(),point5.toVector2D());
            return Paths.line(reflected[0],reflected[1]).constant(point5);
        }
    }

}
