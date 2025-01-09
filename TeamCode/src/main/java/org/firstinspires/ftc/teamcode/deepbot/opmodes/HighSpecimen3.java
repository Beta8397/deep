package org.firstinspires.ftc.teamcode.deepbot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.deepbot.DeepBot;
import org.firstinspires.ftc.teamcode.deepbot.DeepBotAuto;

@Autonomous
public class HighSpecimen3 extends DeepBotAuto {

    DeepBot bot = new DeepBot();
    boolean stopEarly = true;

    @Override
    public void runOpMode() throws InterruptedException {
        bot.init(hardwareMap);
        super.setBot(bot);

        autoWaitForStart();

        bot.setPose(0, 7.5, 90);

        // Set arm and deposit first specimen

        setArmForSpecimenHang1();

        driveTo(normalSpeed, 0, 39, 90, 1);
        driveTo(normalSpeed, 0, 42, 90, 0.5);
        bot.openClaw();
        sleep(300);
        bot.setSlideInches(DeepBot.SLIDE_BASE_LENGTH);
        bot.setPose(bot.getPose().x, 41, 90);


        // Drive to sample on spike mark



        driveTo(fast, 34, 30, 90, 1);
        driveTo(fast, 34, 60, 90, 1);
        driveTo(fast, 47, 60, 90, 1);
        turnTo(-90, 90, 8, 2);
        double x = getXFromLeftDistance();
        bot.setPose(x, bot.getPose().y, Math.toDegrees(bot.getPose().h));
        driveTo(normalSpeed,  47, bot.getPose().y, -90, 0.5);

//        TODO: use left distance sensor to adjust x and adjust bot position


        // push sample into alliance wall and reset pose

        bot.setArmDegrees(30);
        driveTo(fast, 47, 9, -90, 1);
        driveTo(slow, 47 , 4, -90, 1);
        bot.setPose(bot.getPose().x, 7.5, -90);

        // backup, set arm position, pickup specimen from wall

        driveTo(normalSpeed, 47, 26, -90, 1);
        setArmForWallPickup();
        driveTo(normalSpeed, 47, 18, -90, .5);
        bot.closeClaw();
        sleep(400);
        setArmForSpecimenHang2();
        x = getXFromLeftDistance();
        bot.setPose(x, bot.getPose().y, Math.toDegrees(bot.getPose().h));

        // hangs second specimen

        driveTo(fast, 6, 24,-90,1);
        turnTo(90, 90, 8, 2);
        driveTo(normalSpeed, 6, 43, 90, 0.5);
        bot.setPose(bot.getPose().x, 41, 90);
        bot.openClaw();
        sleep(300);
        bot.setSlideInches(DeepBot.SLIDE_BASE_LENGTH);

        // drive to pickup specimen 3

        driveTo(fast, 6, 20, 90, 1);
        bot.setArmDegrees(30);
        turnTo(-90, 90,8,2);
        driveTo(fast, 50, 20, -90, 1);
        driveTo(normalSpeed, 50, 3, -90,1);
        bot.setPose(bot.getPose().x, 7.5, -90);
        driveTo(normalSpeed, 50, 26, -90, 1);
        setArmForWallPickup();
        driveTo(normalSpeed, 50, 18, -90, 0.5);
        bot.closeClaw();
        sleep(400);
        setArmForSpecimenHang2();
        x = getXFromLeftDistance();
        bot.setPose(x, bot.getPose().y, Math.toDegrees(bot.getPose().h));

        // deliver specimen 3


        driveTo(fast, -6, 24,-90,1);
        turnTo(90, 90, 8, 2);
        driveTo(normalSpeed, -6, 43, 90, 0.5);
        bot.setPose(bot.getPose().x, 41, 90);
        bot.openClaw();
        sleep(300);
        bot.setSlideInches(DeepBot.SLIDE_BASE_LENGTH);


        // back away from submersible

        driveTo(normalSpeed, bot.getPose().x, 24, 90, 1);
        bot.setArmDegrees(DeepBot.MIN_ARM_DEGREES);

        while (opModeIsActive()){
            continue;
        }
    }
}
