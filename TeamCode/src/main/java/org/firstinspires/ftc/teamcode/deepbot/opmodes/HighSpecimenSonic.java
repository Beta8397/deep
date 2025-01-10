package org.firstinspires.ftc.teamcode.deepbot.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.deepbot.DeepBot;
import org.firstinspires.ftc.teamcode.deepbot.DeepBotAuto;

@Autonomous
public class HighSpecimenSonic extends DeepBotAuto {

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


        // Drive to sample on spike mark



        bot.setPose(bot.getPose().x, 41, 90);
        driveTo(fast, bot.getPose().x, 30, 90, 1);
        driveTo(fast, 34, 30, 90, 1);
        driveTo(fast, 34, 60, 90, 1);
        driveTo(fast, 48, 60, 90, 1);
        turnTo(-90, 90, 8, 2);

//        TODO: use left distance sensor to adjust x and adjust bot position
        sleep(100);
        double x = getXFromSonic();
        telemetry.addData("Sonic X", x);
        bot.setPose(x, bot.getPose().y, Math.toDegrees(bot.getPose().h));
        driveTo(normalSpeed, 48, 60, -90, .5);
        telemetry.addData("Adjusted X", bot.getPose().x);
        telemetry.update();


        // push sample into alliance wall and reset pose

        bot.setArmDegrees(30);
        driveTo(fast, 48, 9, -90, 1);
        driveTo(slow, 48 , 4, -90, 1);
        bot.setPose(bot.getPose().x, 7.5, -90);

        // backup, set arm position, pickup specimen from wall

        driveTo(normalSpeed, 48, 26, -90, 1);
        setArmForWallPickup();
        driveTo(normalSpeed, 48, 18, -90, .5);
        bot.closeClaw();
        sleep(400);
        setArmForSpecimenHang2();

        sleep(100);
        x = getXFromSonic();
        bot.setPose(x, bot.getPose().y, Math.toDegrees(bot.getPose().h));

        // hangs second specimen

        driveTo(fast, 3, 24,-90,1);
        turnTo(90, 90, 8, 2);
        driveTo(normalSpeed, 3, 43, 90, 0.5);
        bot.openClaw();
        sleep(300);
        bot.setSlideInches(DeepBot.SLIDE_BASE_LENGTH);

        // park!

        driveTo(fast, 12, 16, 90, 1);
        bot.setArmDegrees(DeepBot.MIN_ARM_DEGREES);
        driveTo(fast, 48, 16, 90, 1);




        while (opModeIsActive()){
            continue;
        }
    }
}
