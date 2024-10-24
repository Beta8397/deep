package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.deepbot.DeepBot;

@TeleOp
public class TestDeepArm extends LinearOpMode {

    DeepBot bot = new DeepBot();

    public void runOpMode(){
        bot.init(hardwareMap);
        waitForStart();

        while (opModeIsActive()){
            double currentArmLength = bot.getArmLength();
            double currentArmAngle = bot.getArmAngle();
            double targetArmLength = bot.getTargetSlideLength();
            double targetArmAngle = bot.getTargetArmAngle();
            bot.setTargetArmAngleSafe(targetArmAngle + gamepad1.left_stick_y * 0.5);
            bot.setTargetSlideLengthSafe(targetArmLength - gamepad1.right_stick_y * 0.2);
            bot.updateArm();

            telemetry.addData("targetangle", targetArmAngle);
            telemetry.addData("targetlength", targetArmLength);
            telemetry.addData("armdegrees", currentArmAngle);
            telemetry.addData("slideinches", currentArmLength);
            telemetry.update();
        }
    }
}
