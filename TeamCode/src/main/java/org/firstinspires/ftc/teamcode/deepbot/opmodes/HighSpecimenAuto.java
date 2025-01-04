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

        bot.setArmDegrees(21);
        bot.setSlideInches(20);
        bot.setWristPosition(0.5);

        sleep(3000);


        driveTo(normalSpeed, 0, 39, 90, 1);
        sleep(500);
        bot.setDriveSpeed(0,6,0);
        sleep(2000);
        bot.setDriveSpeed(0,0,0);

        bot.openClaw();
        sleep(300);

        bot.setPose(bot.getPose().x, 41, 90);

        driveTo(normalSpeed, 0, 16, 90, 1);

    }
}
