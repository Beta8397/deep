package org.firstinspires.ftc.teamcode.deepbot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.deepbot.DeepBot;
import org.firstinspires.ftc.teamcode.deepbot.DeepBotAuto;
import org.firstinspires.ftc.teamcode.util.Pose;
import org.firstinspires.ftc.teamcode.util.SavedData;

@Autonomous

public class LeftAutoPlusRed extends DeepBotAuto {

    DeepBot bot = new DeepBot();

    @Override
    public void runOpMode() throws InterruptedException {
        bot.init(hardwareMap);
        super.setBot(bot);

        autoWaitForStart();

        bot.setPose(-31, 8, 180);

        bot.setArmDegrees(67);

        driveTo(normalSpeed, -31, 16, 180, 1);
        driveTo(normalSpeed, -54, 16, 180, 1);
        bot.setWristPosition(0.764);
        turnTo(-135, 90, 8, 2);

        bot.armMotor.getCurrentPosition();
        while (opModeIsActive() && armBusy()) continue;
        bot.slideMotor.getCurrentPosition();
        bot.setSlideInches(44);
        while (opModeIsActive() && slideBusy()) continue;

        dropSampleInBucket();

        bot.slideMotor.getCurrentPosition();
        bot.setSlideInches(16);
        waitForMotor(()-> bot.getSlideInches() < 24);


        turnTo(90, 90, 8,2);

        bot.setArmDegrees(-36);
        bot.setWristPosition(0.764);
        bot.slideMotor.getCurrentPosition();
        bot.armMotor.getCurrentPosition();
        while (opModeIsActive()){
            if (!armBusy() && !slideBusy()) break;
        }

        double lDist = getAvgLeftDistance(5);
        double x = bot.getPose().x;
        double targetX = x + 17 - lDist;

        driveTo(normalSpeed, targetX, bot.getPose().y, 90, 0.5);
        Pose pose1 = new Pose(targetX, bot.getPose().y, bot.getPose().h);
        Pose pose2 = new Pose(targetX, 31, Math.toRadians(90));
        driveLine(normalSpeed, pose1, pose2, 0.5);

//        driveTo(normalSpeed, targetX, 31, 90, 0.5);

        bot.setClawPosition(DeepBot.CLAW_CLOSED);
        sleep(400);

        bot.setArmDegrees(67);

        driveTo(medium, -54, 18, 90, 1);
        turnTo(-135,90,8, 2);


        bot.armMotor.getCurrentPosition();
        while (opModeIsActive() && armBusy() ) continue;

        bot.setSlideInches(44);
        bot.slideMotor.getCurrentPosition();
        while (opModeIsActive() && slideBusy()) continue;
        dropSampleInBucket();

        bot.setSlideInches(20);
        bot.slideMotor.getCurrentPosition();
        waitForMotor(()->bot.getSlideInches() < 24);
        bot.setArmDegrees(36);

        turnTo(0, 90, 8, 2);
        driveTo(fast, -36, 57, 0, 1);
        driveTo(normalSpeed, -18, 57, 0, 1);

        bot.armMotor.setPower(0);

        while(opModeIsActive()){
            telemetry.addData("POS", "x = %.1f  y = %.1f  h = %.1f",
                    bot.getPose().x, bot.getPose().y, Math.toDegrees(bot.getPose().h));
            telemetry.update();
        }

        SavedData.pose = bot.getPose();

    }
}
