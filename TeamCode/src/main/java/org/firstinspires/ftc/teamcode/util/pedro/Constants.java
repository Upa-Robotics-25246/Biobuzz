package org.firstinspires.ftc.teamcode.util.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static Follower create(HardwareMap h) {


        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );

    }

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(0.0810140418255423);
        c.yPodOffset.set(-6.216899841789186);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });
    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("fl");
        c.frontRightName.set("fr");
        c.backLeftName.set("bl");
        c.backRightName.set("br");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });


    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.32368784346759444);
                Controller secondaryTranslationalForward = Controller.proportional(0.11959400906280597);
                Controller primaryTranslationalLateral = Controller.proportional(0.4405436425730133);
                Controller secondaryTranslationalLateral = Controller.proportional(0.16276910438779923);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.014218122250690373));
                c.brake.set(Controller.proportionalFeedforward(0.012085403913086816));

                c.headingFeedback.set(Controller.proportional(5.725096181524072));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.05694969013073654, 0.006982533195528512));

                c.linearBrakeCoefficients.set(Matrix.diag(0.07829335953582657, 0.04861226595528764));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0017478321952421707, 0.002056946536238865));

                c.maxAchievableForwardVelocity.set(74.36616523938405);
                c.maxAchievableStrafeVelocity.set(45.47417004108452);
                c.naturalForwardDeceleration.set(39.96995297965781);
                c.naturalStrafeDeceleration.set(70.3420331107883);
            }
    );

}