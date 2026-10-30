
package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.feedback.PIDController;
import dev.nextftc.control.feedforward.SimpleFFCoefficients;
import dev.nextftc.control.feedforward.SimpleFeedforward;

@TeleOp
public class ScrimTeleOp extends OpMode {

    // motors
    DcMotor motorTR, motorTL, motorBL, motorBR;
    IMU imu1;



    // blockers
    Servo pollenBlocker, nectarBlocker;
    final double POLLEN_OPEN = 0.6;
    final double POLLEN_CLOSED = 0.75;

    final double NECTAR_OPEN = 0.7;
    final double NECTAR_CLOSED = 0.85;


    boolean pollenBlockersOpen = false;

    boolean nectarBlockersOpen = false;

    DcMotorEx intTrans; //intake transfer



    //shooter stuff
    PIDCoefficients
            nectarPIDCoeffs = new PIDCoefficients(0,0,0),
            pollenPIDCoeffs = new PIDCoefficients(0,0,0); // TODO tune these

    SimpleFFCoefficients
            pollenFFCoeffs = new SimpleFFCoefficients(0.037,0.00037),
            nectarFFCoeffs = new SimpleFFCoefficients(0,0);




    PIDController nectarPID = new PIDController(nectarPIDCoeffs), pollenPID = new PIDController(pollenPIDCoeffs);
    SimpleFeedforward nectarFF = new SimpleFeedforward(nectarFFCoeffs), pollenFF = new SimpleFeedforward(pollenFFCoeffs);

    DcMotorEx pollen, nectar;


    double nectarVelo = 0; // TODO tune this
    double pollenVelo = 0;// TODO tune this

    boolean shooting = false;


    enum IntakeState{
        FORWARD,
        STOPPED,
        REVERSE
    }

    IntakeState intakeState = IntakeState.STOPPED;


    public static double necPower = 0, polPower = 0;
    public static boolean usingPower;



    @Override
    public void init() {



        // Map drivetrain motors
        motorTR = hardwareMap.get(DcMotor.class, "motorTR");
        motorTL = hardwareMap.get(DcMotor.class, "motorTL");
        motorBL = hardwareMap.get(DcMotor.class, "motorBL");
        motorBR = hardwareMap.get(DcMotor.class, "motorBR");
        imu1 = hardwareMap.get(IMU.class, "imu");

        motorTL.setDirection(DcMotor.Direction.REVERSE);
        motorBL.setDirection(DcMotor.Direction.REVERSE);

        // Map servo blockers
        pollenBlocker = hardwareMap.get(Servo.class, "pollenBlocker");
        nectarBlocker = hardwareMap.get(Servo.class, "nectarBlocker");

        // Set initial blocker positions
        pollenBlocker.setPosition(POLLEN_CLOSED);
        nectarBlocker.setPosition(NECTAR_CLOSED);

        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        // Without this, the REV Hub's orientation is assumed to be logo up / USB forward
        imu1.initialize(parameters);

        intTrans = hardwareMap.get(DcMotorEx.class, "intTrans");
        intTrans.setDirection(DcMotorSimple.Direction.FORWARD); // TODO make sure this direction is accurate for intaking balls

        pollen = hardwareMap.get(DcMotorEx.class,"pollenFly");
        nectar = hardwareMap.get(DcMotorEx.class,"nectarFly");

    }

    @Override
    public void loop() {

        double nectarError = nectarVelo - nectar.getVelocity();
        double pollenError = pollenVelo - pollen.getVelocity();
        double pollenPower = pollenPID.calculate(pollenError) + pollenFF.calculate(pollenVelo);
        double nectarPower = nectarPID.calculate(nectarError) + nectarFF.calculate(nectarVelo);

        if(usingPower){
            nectarPower = necPower;
            pollenPower = polPower;
        }


        // drive code
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;

        if (gamepad1.options) {
            imu1.resetYaw();
        }

        double botHeading = imu1.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

//        // Rotate the movement direction counter to the bot's rotation
//        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
//        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);
//
//        rotX = rotX * 1.1;  // Counteract imperfect strafing

        x = x * 1.1;

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        motorTL.setPower(frontLeftPower);
        motorBL.setPower(backLeftPower);
        motorTR.setPower(frontRightPower);
        motorBR.setPower(backRightPower);

    //servo blocker code

        if (gamepad2.aWasPressed()) {
            pollenBlockersOpen = !pollenBlockersOpen; // toggle pollen blocker
        }

        if (pollenBlockersOpen) {
            pollenBlocker.setPosition(POLLEN_OPEN);
        } else {
            pollenBlocker.setPosition(POLLEN_CLOSED);
        }


        if (gamepad2.bWasPressed()) {
            nectarBlockersOpen = !nectarBlockersOpen; // toggle nectar blocker
        }

        if (nectarBlockersOpen) {
            nectarBlocker.setPosition(NECTAR_OPEN);
        } else {
            nectarBlocker.setPosition(NECTAR_CLOSED);
        }


    // intake + transfer code

        if(gamepad2.dpadUpWasPressed()){
            switch(intakeState){

                case REVERSE:
                    intakeState = IntakeState.STOPPED;
                case STOPPED:
                    intakeState = IntakeState.FORWARD;
            }
        }

        if(gamepad2.dpadDownWasPressed()){
            switch(intakeState){

                case FORWARD:
                    intakeState = IntakeState.STOPPED;
                case STOPPED:
                    intakeState = IntakeState.REVERSE;
            }
        }

        switch(intakeState){
            case FORWARD:
                intTrans.setPower(1);
            case STOPPED:
                intTrans.setPower(0);
            case REVERSE:
                intTrans.setPower(-1);
        }




    // shooting code
        if(gamepad2.xWasPressed()){
            shooting = !shooting;
        }

        if(shooting){
            nectar.setPower(nectarPower);
            pollen.setPower(pollenPower);
        }
        else{
            nectar.setPower(0);
            pollen.setPower(0);
        }


    }
}
