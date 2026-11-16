package org.firstinspires.ftc.teamcode.Auton.lm1;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.math.Vector2D;
import com.pedropathing.paths.Path;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.lib.PathOptions;
import org.firstinspires.ftc.teamcode.util.pedro.Constants;
import org.firstinspires.ftc.teamcode.util.subsystems.Blocker;
import org.firstinspires.ftc.teamcode.util.subsystems.children.PollenBlocker;
import org.firstinspires.ftc.teamcode.util.subsystems.children.PollenShooter;

import java.util.List;

public class OneTipFar extends OpMode {


    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees().mirrorX(70.75);

    private final Pose start = poseFactory.of(58, 129, 270);
    private final Pose path1 = poseFactory.of(4, 116, 180);

    boolean red;

    // Autonomous routine
    ElapsedTime shooterTimer = new ElapsedTime();
    public Command autoRoutine(boolean red) {
        return
            sequential(
                instant(()->shooterTimer.reset()),
                waitMs(10000),
                infinite(()->shoot(false)).until(()->shooterTimer.seconds() > 1.5),
                instant(()->shoot(true)),
                follow(follower, path1(red)
            )
        );
    }

    PollenShooter pollen;
    PollenBlocker blocker;
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

    List<LynxModule> hubs;
    @Override
    public void start(){
        schedule(autoRoutine(red));
        shooterTimer.reset();
    }

    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();
        pollen = new PollenShooter(hardwareMap);
        blocker = new PollenBlocker(hardwareMap);
        blocker.setState(Blocker.BlockerPosition.OPEN);
        hubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : hubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }
    }
    public void init_loop(){
        if(gamepad1.aWasPressed()){
            red = !red;
        }
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

        if (follower.currentPath() != null) {
            telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
            telemetry.addData("Path number", follower.pathIndex());
        }

        telemetry.update();
    }
    public Path path1( boolean red) {
        if(red)
            return line(start, path1).linear(start, path1);
        else{
            Vector2D[] reflected = PathOptions.reflect(start.toVector2D(),path1.toVector2D());
            return Paths.line(reflected[0],reflected[1]).linear(start,path1);
        }
    }
}
