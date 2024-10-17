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
            double currentArmTargetAngle = bot.getTargetArmAngle();
            double currentArmTargetLength = bot.getTargetArmLength();
            double angleIncrement = gamepad1.left_stick_y * 0.02;
            double lengthIncrement = -gamepad1.right_stick_y * 0.02;
            bot.setTargetArmAngle(currentArmTargetAngle + angleIncrement);
            bot.setTargetArmLength(currentArmTargetLength + lengthIncrement);
            bot.updateArm();
            telemetry.addData("targetangle", currentArmTargetAngle);
            telemetry.addData("targetlength", currentArmTargetLength);
            telemetry.update();
        }
    }
}
