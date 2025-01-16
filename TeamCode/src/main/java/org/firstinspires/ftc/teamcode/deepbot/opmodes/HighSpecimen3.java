package org.firstinspires.ftc.teamcode.deepbot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.deepbot.DeepBot;
import org.firstinspires.ftc.teamcode.deepbot.DeepBotAuto;
import org.firstinspires.ftc.teamcode.util.Pose;

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

        driveTo(fast, 0, 39, 90, 1);
        driveTo(normalSpeed, 0, 42, 90, 0.5);
        bot.openClaw();
        sleep(300);
        bot.setSlideInches(DeepBot.SLIDE_BASE_LENGTH);


        // Drive to sample on spike mark



        bot.setPose(bot.getPose().x, 41, 90);
        driveTo(fast, bot.getPose().x, 30, 90, 1);
        driveTo(fast, 34, 30, 90, 1);
        driveTo(fast, 34, 56, 90, 1);
        driveTo(fast, 47.5, 56, 90, 1);
        turnTo(-90, 120, 8, 2);

        // push sample into alliance wall and reset pose

        bot.setArmDegrees(30);
        driveLine(fast, new Pose(47.5,56, Math.toRadians(-90)),
                new Pose(46.5, 9, Math.toRadians(-90)),
                1);
        driveTo(fast, 47.5 , 1, -90, 1);
        bot.setPose(bot.getPose().x, 7.5, -90);

        // backup, set arm position, pickup specimen from wall

        driveTo(fast, 47.5, 22, -90, 1);
        setArmForWallPickup();
        driveTo(normalSpeed, 47.5, 18, -90, .5);
        bot.closeClaw();
        sleep(400);
        setArmForSpecimenHang2();

        // hangs second specimen

        driveTo(superFast, 6, 21,-90,1);
        turnTo(90, 120, 8, 2);
        driveTo(superFast, 6, 43, 90, 0.5);
        bot.openClaw();
        sleep(300);
        bot.setPose(6, 41, 90);
        bot.setSlideInches(DeepBot.SLIDE_BASE_LENGTH);

        // drive to observation zone

        driveTo(superFast, 6, 24, 90, 1);
        bot.setArmDegrees(30);
        driveTo(superFast, 47.5, 24, 90, 1);
        turnTo(-90, 120, 8, 2);


        // run into wall

        driveTo(fast, 47.5, 9, -90, 1);
        driveTo(normalSpeed, 47.5 , 1, -90, 1);
        bot.setPose(bot.getPose().x, 7.5, -90);

        // backup, set arm position, pickup specimen from wall

        driveTo(normalSpeed, 47.5, 26, -90, 1);
        setArmForWallPickup();
        driveTo(normalSpeed, 47.5, 18, -90, .5);
        bot.closeClaw();
        sleep(400);
        setArmForSpecimenHang2();

        // hangs third specimen

        driveTo(superFast, -6, 21,-90,1);
        turnTo(90, 120, 8, false, 2);
        driveTo(superFast, -6, 43, 90, 0.5);
        bot.openClaw();
        sleep(300);
        bot.setPose(-6, 41, 90);
        bot.setSlideInches(DeepBot.SLIDE_BASE_LENGTH);

        // backup

        driveTo(fast, 6, 16, 90, 1);
        bot.setArmDegrees(DeepBot.MIN_ARM_DEGREES);


        while (opModeIsActive()){
            continue;
        }
    }
}
