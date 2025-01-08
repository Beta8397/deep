package org.firstinspires.ftc.teamcode.deepbot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.deepbot.DeepBot;
import org.firstinspires.ftc.teamcode.deepbot.DeepBotAuto;

@Autonomous
public class HighSpecimenAuto extends DeepBotAuto {

    DeepBot bot = new DeepBot();
    boolean stopEarly = true;

    @Override
    public void runOpMode() throws InterruptedException {
        bot.init(hardwareMap);
        super.setBot(bot);

        autoWaitForStart();

        bot.setPose(0, 7.5, 90);
        setArmForSpecimenHang1();

        driveTo(normalSpeed, 0, 39, 90, 1);
        driveTo(normalSpeed, 0, 42, 90, 0.5);


        bot.openClaw();
        sleep(300);
        bot.setSlideInches(DeepBot.SLIDE_BASE_LENGTH);

        bot.setPose(bot.getPose().x, 41, 90);
        driveTo(fast, 34, 30, 90, 1);
        driveTo(fast, 34, 60, 90, 1);
        driveTo(fast, 46.5, 60, 90, 1);
        turnTo(-90, 90, 8, 2);

//        TODO: use left distance sensor to adjust x

        bot.setArmDegrees(30);
        driveTo(fast, 46.5, 9, -90, 1);
        driveTo(slow, 46.5 , 4, -90, 1);
        bot.setPose(bot.getPose().x, 7.5, -90);


        driveTo(normalSpeed, 46.5, 32, -90, 1);
        sleep(3000);
        setArmForWallPickup();
        driveTo(normalSpeed, 46.5, 18, -90, .5);
        bot.closeClaw();
        sleep(400);

        setArmForSpecimenHang2();
        driveTo(fast, 6, 24,-90,1);
        turnTo(90, 90, 8, 2);
        driveTo(normalSpeed, 6, 43, 90, 0.5);
        bot.openClaw();
        sleep(300);
        bot.setSlideInches(DeepBot.SLIDE_BASE_LENGTH);
        driveTo(fast, 12, 20, 90, 1);
        bot.setArmDegrees(DeepBot.MIN_ARM_DEGREES);
        driveTo(fast, 48, 20, 90, 1);




        while (opModeIsActive()){
            continue;
        }
    }
}
