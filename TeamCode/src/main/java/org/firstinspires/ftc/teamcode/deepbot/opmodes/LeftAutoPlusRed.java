package org.firstinspires.ftc.teamcode.deepbot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.deepbot.DeepBot;
import org.firstinspires.ftc.teamcode.deepbot.DeepBotAuto;
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

        bot.setArmDegrees(70);

        driveTo(normalSpeed, -31, 18, 180, 1);
        driveTo(normalSpeed, -48, 18, 180, 1);
        turnTo(-135, 90, 8, 2);

        waitForMotor(()->!bot.armMotor.isBusy());
        bot.setSlideInches(44);
        waitForMotor(()-> !bot.slideMotor.isBusy());

        dropSampleInBucket();

        bot.setSlideInches(16);
        waitForMotor(()-> bot.getSlideInches() < 24);

        bot.setArmDegrees(DeepBot.MIN_ARM_DEGREES);
        bot.setWristPosition(0.465);

        turnTo(90, 90, 8,2);
        waitForMotor(()->!bot.armMotor.isBusy());
        waitForMotor(()->!bot.slideMotor.isBusy());

        double lDist = getAvgLeftDistance(5);
        double x = bot.getPose().x;
        double targetX = x + 12.8  - lDist;

        driveTo(normalSpeed, targetX, 34, 90, 1);
        bot.setClawPosition(DeepBot.CLAW_CLOSED);
        sleep(400);

        bot.setArmDegrees(70);

        driveTo(medium, -51, 19, 90, 1);
        turnTo(-135,90,8, 2);

        waitForMotor(()->!bot.armMotor.isBusy());
        bot.setSlideInches(44);
        waitForMotor(()->!bot.slideMotor.isBusy());
        dropSampleInBucket();


        bot.setSlideInches(DeepBot.SLIDE_BASE_LENGTH);
        waitForMotor(()->bot.getSlideInches() < 24);
        bot.setArmDegrees(DeepBot.MIN_ARM_DEGREES);

        turnTo(-180, 90, 8, 2);
        driveTo(fast, 42, 13, 180, 1);

        while(opModeIsActive()){
            telemetry.addData("POS", "x = %.1f  y = %.1f  h = %.1f",
                    bot.getPose().x, bot.getPose().y, Math.toDegrees(bot.getPose().h));
            telemetry.update();
        }

        SavedData.pose = bot.getPose();

    }
}
