package org.firstinspires.ftc.teamcode.deepbot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.deepbot.DeepBot;
import org.firstinspires.ftc.teamcode.util.Toggle;
import org.firstinspires.ftc.teamcode.xdrive.XDriveTele;

@TeleOp
public class DeepTeleop extends XDriveTele {
    DeepBot bot = new DeepBot();

    Toggle toggleLB1 = new Toggle(()->gamepad1.left_bumper);

    boolean ascending =false;

    @Override
    public void runOpMode(){
        bot.init(hardwareMap);
        super.setBot(bot);
        bot.setPose(0,0,0);

        while (opModeIsActive()){
            oneDriveCycle();

            double currentArmLength = bot.getArmLength();
            double currentArmAngle = bot.getArmAngle();
            double targetArmLength = bot.getTargetSlideLength();
            double targetArmAngle = bot.getTargetArmAngle();
            bot.setTargetArmAngleSafe(targetArmAngle + gamepad1.left_stick_y * 0.5);
            bot.setTargetSlideLengthSafe(targetArmLength - gamepad1.right_stick_y * 0.2);
            bot.updateArm(ascending);

            telemetry.addData("targetangle", targetArmAngle);
            telemetry.addData("targetlength", targetArmLength);
            telemetry.addData("armdegrees", currentArmAngle);
            telemetry.addData("slideinches", currentArmLength);
            telemetry.update();
        }
    }
}
