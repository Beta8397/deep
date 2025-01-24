package org.firstinspires.ftc.teamcode.deepbot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.deepbot.DeepBot;
import org.firstinspires.ftc.teamcode.deepbot.DeepBotAuto;
import org.firstinspires.ftc.teamcode.util.Pose;
import org.firstinspires.ftc.teamcode.util.SavedData;

@Autonomous

public class HighBucket3 extends DeepBotAuto {

    DeepBot bot = new DeepBot();

    @Override
    public void runOpMode() throws InterruptedException {
        bot.init(hardwareMap);
        super.setBot(bot);

        autoWaitForStart();

        bot.setPose(-31, 8, 180);

        bot.setArmDegrees(67);

        driveTo(fast, -31, 16.7, 180, 1);
        if (bot.getArmAngle()> 45){
            bot.setSlideInches(44);
        }
        driveTo(fast, -53.3, 16.7, 180, 1);
        if (bot.getArmAngle()> 45){
            bot.setSlideInches(44);
        }
        bot.setWristPosition(0.764);
        turnTo(-135, 120, 12, 2);

        bot.armMotor.getCurrentPosition();
        while (opModeIsActive() && armBusy()) continue;
        bot.slideMotor.getCurrentPosition();
        bot.setSlideInches(44);
        while (opModeIsActive() && slideBusy()) continue;

        dropSampleInBucket2();

        bot.slideMotor.getCurrentPosition();
        bot.setSlideInches(16);
        waitForMotor(()-> bot.getSlideInches() < 24);


        turnTo(90, 120, 12,2);

        bot.setArmDegrees(-36);
        bot.setWristPosition(0.7); // was 0.764
        bot.slideMotor.getCurrentPosition();
        bot.armMotor.getCurrentPosition();
        while (opModeIsActive()){
            if (!armBusy() && !slideBusy()) break;
        }

        double lDist = getAvgLeftDistance(5);
        double x = bot.getPose().x;
        double targetX = x + 16.5 - lDist;

        driveTo(fast, targetX, bot.getPose().y, 90, 0.5);
        Pose pose1 = new Pose(targetX, bot.getPose().y, bot.getPose().h);
        Pose pose2 = new Pose(targetX, 28.5, Math.toRadians(90));
        driveLine(normalSpeed, pose1, pose2, 0.5);

//        driveTo(normalSpeed, targetX, 31, 90, 0.5);

        bot.setWristPosition(0.764);
        sleep(200);
        bot.setClawPosition(DeepBot.CLAW_CLOSED);
        sleep(300);

        bot.setArmDegrees(67);

        driveTo(fast, -54, 18, 90, 1);
        if (bot.getArmAngle()> 45){
            bot.setSlideInches(44);
        }
        turnTo(-135,120,12, 2);


        bot.armMotor.getCurrentPosition();
        while (opModeIsActive() && armBusy() ) continue;

        bot.setSlideInches(44);
        bot.slideMotor.getCurrentPosition();
        while (opModeIsActive() && slideBusy()) continue;
        dropSampleInBucket2();

        bot.slideMotor.getCurrentPosition();
        bot.setSlideInches(16);
        waitForMotor(()-> bot.getSlideInches() < 24);


        turnTo(90, 120, 12,2);

        bot.setArmDegrees(-36);
        bot.setWristPosition(0.7); // was 0.764
        bot.slideMotor.getCurrentPosition();
        bot.armMotor.getCurrentPosition();
        while (opModeIsActive()){
            if (!armBusy() && !slideBusy()) break;
        }

        lDist = getAvgLeftDistance(5);
        x = bot.getPose().x;

        targetX = x + 6.5 - lDist;

        driveTo(fast, targetX, bot.getPose().y, 90, 0.5);
        pose1 = new Pose(targetX, bot.getPose().y, bot.getPose().h);
        pose2 = new Pose(targetX, 28.5, Math.toRadians(90));
        driveLine(normalSpeed, pose1, pose2, 0.5);

        bot.setWristPosition(0.764);
        sleep(200);
        bot.setClawPosition(DeepBot.CLAW_CLOSED);
        sleep(300);

        bot.setArmDegrees(67);

        driveTo(fast, -55, 17, 90, 1);
        if (bot.getArmAngle()>45){
            bot.setSlideInches(44);
        }
        turnTo(-135,120,12, 2);


        bot.armMotor.getCurrentPosition();
        while (opModeIsActive() && armBusy() ) continue;

        bot.setSlideInches(44);
        bot.slideMotor.getCurrentPosition();
        while (opModeIsActive() && slideBusy()) continue;
        dropSampleInBucket2();

        bot.setSlideInches(DeepBot.SLIDE_BASE_LENGTH);

        SavedData.pose = bot.getPose();
        while (opModeIsActive()) continue;

    }
}
